/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.naturarv.portal.controller;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.IntStream;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped; 
import javax.inject.Inject;
import javax.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import se.nrm.dina.naturarv.portal.exceptions.SolrServerConnectionException;
import se.nrm.dina.naturarv.portal.solr.SolrSearch;
import se.nrm.dina.naturarv.portal.util.ConstantString;
import se.nrm.dina.naturarv.portal.util.Month;
import se.nrm.dina.naturarv.portal.util.Util;

/**
 *
 * @author idali
 */
@Named("mainChart")
@SessionScoped
@Slf4j
public class MainChart implements Serializable {

    private BarChartModel totalMonthModel;                                              // month chart
    private BarChartModel totalTenYearsModel;                                           // year chart

    private LocalDate today;
    private int lastTenYear; 

    private int year;
    private int lastYear;
    private int month;
    private LocalDate lastYearDate;
    private boolean isSwedish;

    @Inject
    private SolrSearch solr;
    
    @Inject
    private Languages language;
    
    @Inject
    private MessageBean messages;

    public MainChart() {
        totalMonthModel = new BarChartModel();
        totalTenYearsModel = new BarChartModel();
    }

    @PostConstruct
    public void init() {
        log.info("init");

        initDate();

//        if (totalMonthModel.getSeries() == null || totalMonthModel.getSeries().isEmpty()) {
//            try {
//                createTotalMonthModel();
//            } catch(SolrServerConnectionException e) {
//                messages.addError("Unable to connect to Solr server", e.getMessage());  
//            } 
//        }
//
//        if (totalTenYearsModel.getSeries() == null || totalTenYearsModel.getSeries().isEmpty()) {
//            try {
//                createLastTenYearsModel();
//            } catch(SolrServerConnectionException e) {
//                messages.addError("Unable to connect to Solr server", e.getMessage());  
//            } 
//        }
    }

    /**
     * Initiate date now
     */
    private void initDate() {
        log.info("initDate");

        isSwedish = language.isIsSwedish();
        today = LocalDate.now();
        year = today.getYear();
        lastYear = year - 1;
        month = today.getMonthValue();

        lastYearDate = LocalDate.of(lastYear, month, today.getDayOfMonth());
        lastTenYear = year - 10;
    }

    private String buildLabel(Month month, boolean isSwedish) {
        log.info("month : {} -- {}", month, month.getEnglish());
        StringJoiner sj = new StringJoiner(" ");

        String monName = isSwedish ? month.getSwedish() : month.getEnglish();
        int monNum = month.getNumberOfMonth();
        sj.add(monName);
        if (monNum > today.getMonthValue()) {
            sj.add(String.valueOf(lastYearDate.getYear()));
        } else {
            sj.add(String.valueOf(today.getYear()));
        }
        return sj.toString();
    }

    /**
     * Create chart model for all collection data for last 12 months
     */
    private void createTotalMonthModel() {

        String startDate = Util.getInstance().firstDayOfMonthInString(lastYearDate.getMonthValue() + 1, lastYearDate.getYear());
            
        try {
            Map<String, Integer> monthMap = solr.getLastYearRegistedDataForCollection(startDate, null);
 
            ChartSeries series = new ChartSeries();

            IntStream.range(1, 13)
                    .forEach(x -> {
                        int m = lastYearDate.plusMonths(x).getMonthValue();
                        int total = monthMap.containsKey(String.valueOf(m)) ? monthMap.get(String.valueOf(m)) : 0;

                        series.set(buildLabel(Month.getMonth(m), isSwedish), total);
                    });

            totalMonthModel = new BarChartModel();
            totalMonthModel.addSeries(series);
            totalMonthModel.setTitle(ConstantString.getInstance().getMonthChartTitle(isSwedish));
            totalMonthModel.setShowPointLabels(true);
            totalMonthModel.setShowDatatip(false);

            Axis axis = totalMonthModel.getAxis(AxisType.X);
            axis.setTickAngle(-50);
            axis.setLabel(ConstantString.getInstance().getMonthChartAxis(isSwedish));

            Axis yaxis = totalMonthModel.getAxis(AxisType.Y);
            yaxis.setLabel(ConstantString.getInstance().getChartYaxis(isSwedish));
        } catch(SolrServerConnectionException e) {
//            messages.addError("Unable to connect to Solr server", e.getMessage());  
            throw e;
        } 
    }

    /**
     * Create chart model for all collection data for last ten years
     */
    private void createLastTenYearsModel() {

        ChartSeries series = new ChartSeries();

        try {
             Map<String, Integer> yearMap = solr.getLastTenYearsRegistereddData(lastTenYear, year, null);
             IntStream.range(lastTenYear, lastTenYear + 11)
                    .forEach(i -> {
                        series.set(String.valueOf(i), getCount(yearMap, String.valueOf(i)));
                    });
            totalTenYearsModel = new BarChartModel();
            totalTenYearsModel.addSeries(series);

            totalTenYearsModel.setTitle(ConstantString.getInstance().getYearChartTitle(isSwedish));
            totalTenYearsModel.setShowPointLabels(true);
            totalTenYearsModel.setShowDatatip(false);

            Axis axis = totalTenYearsModel.getAxis(AxisType.X);
            axis.setTickAngle(-50);
            axis.setLabel(ConstantString.getInstance().getYearChartAxis(isSwedish));

            Axis yaxis = totalTenYearsModel.getAxis(AxisType.Y);
            yaxis.setLabel(ConstantString.getInstance().getChartYaxis(isSwedish));
        } catch(SolrServerConnectionException e) {
            messages.addError("Unable to connect to Solr server", e.getMessage());  
            throw e;
        }  
    }

    private int getCount(Map<String, Integer> map, String key) {
        return map.containsKey(key) ? map.get(key) : 0;
    }

    /**
     * changeLanguage - change chart labels
     *
     * @param locale
     */
    public void changeLanguage(String locale) {

        log.info("changeLanguage : {}", locale);

        isSwedish = locale.equals("sv");
        totalMonthModel = changeSeriesLable(totalMonthModel);
        totalTenYearsModel = changeTenYearsSeriesLable(totalTenYearsModel);
    }

    private BarChartModel changeSeriesLable(BarChartModel model) {

        log.info("changeSeriesLable : {}", model);

        ChartSeries series = model.getSeries().get(0);
        ChartSeries newSeries = new ChartSeries();

        series.getData().entrySet().stream()
                .forEach(x -> {
                    String key = (String) x.getKey();
                    String mon = StringUtils.split(key, " ")[0];
                    String y = StringUtils.split(key, " ")[1];
                    int total = 0;
                    if (x.getValue() != null) {
                        total = (Integer) x.getValue();
                    }
                    String label = buildLabel(Month.getMonth(mon, isSwedish), isSwedish); 
                    newSeries.set(label, total);
                });

        model.clear();
        model.addSeries(newSeries);

        model.setTitle(ConstantString.getInstance().getMonthChartTitle(isSwedish));
        Axis axis = model.getAxis(AxisType.X);
        axis.setTickAngle(-50);
        axis.setLabel(ConstantString.getInstance().getMonthChartAxis(isSwedish));

        Axis yaxis = model.getAxis(AxisType.Y);
        yaxis.setLabel(ConstantString.getInstance().getChartYaxis(isSwedish));

        return model;
    }

    private BarChartModel changeTenYearsSeriesLable(BarChartModel model) {

        log.info("changeTenYearsSeriesLable : {}", model);

        model.setTitle(ConstantString.getInstance().getYearChartTitle(isSwedish));
        Axis axis = model.getAxis(AxisType.X);
        axis.setLabel(ConstantString.getInstance().getYearChartAxis(isSwedish));

        Axis yaxis = model.getAxis(AxisType.Y);
        yaxis.setLabel(ConstantString.getInstance().getChartYaxis(isSwedish));

        return model;
    }

    public BarChartModel getTotalMonthModel() {
        if (totalMonthModel.getSeries() == null || totalMonthModel.getSeries().isEmpty()) {
            try {
                createTotalMonthModel();
            } catch(SolrServerConnectionException e) {
                messages.addError("Unable to connect to Solr server", e.getMessage());  
            }
            
        } 
        return totalMonthModel;
    }

    public BarChartModel getTotalTenYearsModel() {
        if (totalTenYearsModel.getSeries() == null || totalTenYearsModel.getSeries().isEmpty()) {
            try {
                createLastTenYearsModel();
            } catch(SolrServerConnectionException e) {
                messages.addError("Unable to connect to Solr server", e.getMessage());  
            } 
        }  
        return totalTenYearsModel; 
    }
}
