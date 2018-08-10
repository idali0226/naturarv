/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.naturarv.portal.controller;

import java.io.Serializable;
import java.time.LocalDate; 
import java.util.HashMap; 
import java.util.List;
import java.util.Map;
import java.util.StringJoiner; 
import java.util.stream.IntStream;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped; 
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;  
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import se.nrm.dina.naturarv.portal.solr.SolrSearch;
import se.nrm.dina.naturarv.portal.util.ConstantString;
import se.nrm.dina.naturarv.portal.util.Month;
import se.nrm.dina.naturarv.portal.util.Util; 
import se.nrm.dina.naturarv.portal.vo.CollectionData;

/**
 *
 * @author idali
 */
@Named("chart")
@SessionScoped
@Slf4j
public class CollectionsChart implements Serializable {
    
    private final HttpSession session;
     
    private LocalDate today;
    private LocalDate lastYearDate; 
  
    @Inject
    private SolrSearch solr;
    
    @Inject
    private Languages language;

    public CollectionsChart() { 
        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    }

    @PostConstruct
    public void init() { 
        log.info("init");

        initDate();
//        locale = (String) session.getAttribute("locale");
//        if (locale == null) {
//            locale = "sv";
//        }
//        isSwedish = locale.equals("sv");
//
//        if (collections == null || collections.isEmpty()) {
//            collections = solrSearch.getGroupData("cln");                               // get collection codes from solr search
//        }
//
//        if (collectionMonthsModelMap == null || collectionMonthsModelMap.isEmpty()) {
//            createModelForLastYearForCollections();
//        }
//
//        if (collectionYearsModelMap == null || collectionYearsModelMap.isEmpty()) {
//            createModelForLastTenYearsForCollections();
//        }
//
//        if (totalMonthModel.getSeries() == null || totalMonthModel.getSeries().isEmpty()) { 
//            createTotalMonthModel();
//        }
//
//        if (totalTenYearsModel.getSeries() == null || totalTenYearsModel.getSeries().isEmpty()) {
//            createSpecimenModelForLastTenYears();
//        } 
    }

    /**
     * Initiate date now
     */
    private void initDate() {
        
        log.info("initDate");
        today = LocalDate.now();
        int year = today.getYear();
        int lastYear = year - 1;
        int month = today.getMonthValue();

        lastYearDate = LocalDate.of(lastYear, month, today.getDayOfMonth()); 
        
//        strLastYearDate = Util.getInstance().dateToDateTimeString(lastYearDate); 
        int lastTenYear = year - 11;
    }
    

    
    public void onCollectionTabOpen(TabChangeEvent event) {
        log.info("onCollectionTabOpen");
        String collectionName = event.getTab().getTitle();
         
        List<CollectionData> collections = (List<CollectionData>) session.getAttribute("collections");
        String collectionCode = Util.getInstance().getCollectionCodeByName(collections, collectionName);
        
        Map<String, BarChartModel> map = (Map<String, BarChartModel>) session.getAttribute("collectionBarChart");
      
        if(map == null || map.get(collectionCode) == null) { 
            buildMonthChart(collectionCode, map);
        }  
    }

    private void buildMonthChart(String collectionCode, Map<String, BarChartModel> map) {

        boolean isSwedish = language.isIsSwedish();
        String startDate = Util.getInstance().firstDayOfMonthInString(lastYearDate.getMonthValue() + 1, lastYearDate.getYear());

        Map<String, Integer> collectionMap = solr.getLastYearRegistedDataForCollection(startDate, collectionCode); 
        ChartSeries series = new ChartSeries();
        IntStream.range(1, 13)
                .forEach(x -> {
                    int m = lastYearDate.plusMonths(x).getMonthValue();
                    int total = collectionMap.containsKey(String.valueOf(m)) ? collectionMap.get(String.valueOf(m)) : 0;
 
                    series.set(buildLabel(Month.getMonth(m), isSwedish), total);
                });
 
        BarChartModel model = new BarChartModel();
        model.addSeries(series);

        model.setTitle(ConstantString.getInstance().getMonthChartTitle(isSwedish));
        model.setShowPointLabels(true);
        model.setShowDatatip(false);

        Axis axis = model.getAxis(AxisType.X);
        axis.setTickAngle(-50);
        axis.setLabel(ConstantString.getInstance().getMonthChartAxis(isSwedish));

        Axis yaxis = model.getAxis(AxisType.Y);
        yaxis.setLabel(ConstantString.getInstance().getChartYaxis(isSwedish)); 
        
        if(map == null) {
            map = new HashMap();
        }
        map.put(collectionCode, model);  
        session.setAttribute("collectionBarChart", map);
    }
    
     private String buildLabel(Month month,  boolean isSwedish) {
        
        StringJoiner sj = new StringJoiner(" ");
          
        String monName = isSwedish ? month.getSwedish() : month.getEnglish();
        int monNum = month.getNumberOfMonth();
        sj.add(monName);
        if(monNum > today.getMonthValue()) {
            sj.add(String.valueOf(lastYearDate.getYear())); 
        } else {
            sj.add(String.valueOf(today.getYear()));
        }
        return sj.toString();
    }
 
    public BarChartModel getCollectionChart(String collectionCode) {
        log.info("getCollectionChart : {}", collectionCode);
        Map<String, BarChartModel> map =  (Map<String, BarChartModel>) session.getAttribute("collectionBarChart");
      
        if(map == null || map.get(collectionCode) == null) {
            buildMonthChart(collectionCode, map);
        }  
        return map != null ? map.get(collectionCode) : null;
    }
    
//    private String buildLabel(MonthElement element) {
//        
//        StringJoiner sj = new StringJoiner(" ");
//        String monName = StringMap.getInstance().getMonthName(element, isSwedish);
//        int monNum = element.getMonthNumber();
//        sj.add(monName);
//        if(monNum > month) {
//            sj.add(String.valueOf(lastYear)); 
//        } else {
//            sj.add(String.valueOf(year));
//        }
//        return sj.toString();
//    }
//
//    
//    private void buildChartSeries(String collectionCode) {
//        ChartSeries series = new ChartSeries();
//        Map<String, Integer> monthMap = solrSearch.getLastYearRegistedDataForCollection(strLastYearDate, collectionCode);
//       
//        logger.info("month map : {}", monthMap); 
//        IntStream.range(1, 13)
//                .forEach(x -> { 
//                    int m = lastYearDate.plusMonths(x).getMonthValue(); 
//                    int total = monthMap.containsKey(String.valueOf(m)) ? monthMap.get(String.valueOf(m)) : 0;
//
//                    series.set(buildLabel(MonthElement.getName(m)), total);
//                });
// 
//        BarChartModel model = new BarChartModel();
//        model.addSeries(series);
//
//        model.setTitle(StringMap.getInstance().getMonthChartTitle(locale));
//        model.setShowPointLabels(true);
//        model.setShowDatatip(false);
//
//        Axis axis = model.getAxis(AxisType.X);
//        axis.setTickAngle(-50);
//        axis.setLabel(StringMap.getInstance().getMonthChartAxis(locale));
//
//        Axis yaxis = model.getAxis(AxisType.Y);
//        yaxis.setLabel(StringMap.getInstance().getChartYaxis(locale));
//        collectionMonthsModelMap.put(collectionCode, model);  
//    }
//
//    /**
//     * To create chart model for last 12 months data for each collection
//     */
//    private void createModelForLastYearForCollections() { 
//        collections.stream().forEach(this::buildChartSeries); 
//    }
//
//    /**
//     * Create chart model for last ten years data for each collection
//     */
//    private void createModelForLastTenYearsForCollections() {  
//        collections.stream().forEach(this::buildLastTenYearsForCollection); 
//    }
//    
//    
//    /**
//     * Create chart model for all collection data for last ten years
//     */
//    private void buildLastTenYearsForCollection(String collection) {
//
//        ChartSeries series = new ChartSeries();
// 
//        Map<String, Integer> yearMap = solrSearch.getLastTenYearsRegistedData(lastTenYear, collection);
//  
//        IntStream.range(lastTenYear, lastTenYear + 12) 
//                            .forEach(i -> {      
//                                series.set(String.valueOf(i), getCount(yearMap, String.valueOf(i))); 
//                });
//        BarChartModel model = new BarChartModel();
//        model.addSeries(series);
//
//        model.setTitle(StringMap.getInstance().getYearChartTitle(locale));
//        model.setShowPointLabels(true);
//        model.setShowDatatip(false);
//
//        Axis axis = model.getAxis(AxisType.X);
//        axis.setTickAngle(-50);
//        axis.setLabel(StringMap.getInstance().getYearChartAxis(locale));
//
//        Axis yaxis = model.getAxis(AxisType.Y);
//        yaxis.setLabel(StringMap.getInstance().getChartYaxis(locale));
//
//        collectionYearsModelMap.put(collection, model);
//    }
//
//
//
//    /**
//     * Create chart model for all collection data for last 12 months
//     */
//    private void createTotalMonthModel() {
//       
//        Map<String, Integer> monthMap = solrSearch.getLastYearRegistedDataForCollection(strLastYearDate, null); 
//
//        ChartSeries series = new ChartSeries();
//
//        IntStream.range(1, 13)
//                .forEach(x -> { 
//                    int m = lastYearDate.plusMonths(x).getMonthValue(); 
//                    int total = monthMap.containsKey(String.valueOf(m)) ? monthMap.get(String.valueOf(m)) : 0;
//
//                    series.set(buildLabel(MonthElement.getName(m)), total);
//                });
// 
//        totalMonthModel = new BarChartModel();
//        totalMonthModel.addSeries(series);
//        totalMonthModel.setTitle(StringMap.getInstance().getMonthChartTitle(locale));
//        totalMonthModel.setShowPointLabels(true);
//        totalMonthModel.setShowDatatip(false);
//
//        Axis axis = totalMonthModel.getAxis(AxisType.X);
//        axis.setTickAngle(-50);
//        axis.setLabel(StringMap.getInstance().getMonthChartAxis(locale));
//
//        Axis yaxis = totalMonthModel.getAxis(AxisType.Y);
//        yaxis.setLabel(StringMap.getInstance().getChartYaxis(locale));
//    }
//    
//    private int getCount(Map<String, Integer> map, String key) {
//        return map.containsKey(key) ? map.get(key) : 0;
//    }
//
//
//    /**
//     * Create chart model for all collection data for last ten years
//     */
//    private void createSpecimenModelForLastTenYears() {
//
//        ChartSeries series = new ChartSeries();
// 
//        Map<String, Integer> yearMap = solrSearch.getLastTenYearsRegistedData(lastTenYear, null);
//  
//        IntStream.range(lastTenYear, lastTenYear + 12) 
//                            .forEach(i -> {      
//                                series.set(String.valueOf(i), getCount(yearMap, String.valueOf(i))); 
//                            }); 
//        totalTenYearsModel = new BarChartModel();
//        totalTenYearsModel.addSeries(series);
//
//        totalTenYearsModel.setTitle(StringMap.getInstance().getYearChartTitle(locale));
//        totalTenYearsModel.setShowPointLabels(true);
//        totalTenYearsModel.setShowDatatip(false);
//
//        Axis axis = totalTenYearsModel.getAxis(AxisType.X);
//        axis.setTickAngle(-50);
//        axis.setLabel(StringMap.getInstance().getYearChartAxis(locale));
//
//        Axis yaxis = totalTenYearsModel.getAxis(AxisType.Y);
//        yaxis.setLabel(StringMap.getInstance().getChartYaxis(locale));
//    }
//  
//    /**
//     * changeLanguage - change chart labels
//     *
//     * @param locale
//     */
//    public void changeLanguage(String locale) {
//
//        logger.info("changeLanguage : {}", locale);
//
//        this.locale = locale;
//        session.setAttribute("locale", locale);
//
//        changeLocaleForCollectionsChart();
//        changeLocaleFoeCollectionsTenYearsChart();
//        totalMonthModel = changeSeriesLable(totalMonthModel);
//        totalTenYearsModel = changeTenYearsSeriesLable(totalTenYearsModel);
//    }
//
//    private void changeLocaleFoeCollectionsTenYearsChart() {
//
//        BarChartModel model;
//        if (collectionYearsModelMap != null && !collectionYearsModelMap.isEmpty()) {
//            for (Map.Entry<String, BarChartModel> entry : collectionYearsModelMap.entrySet()) {
//                model = changeTenYearsSeriesLable(entry.getValue());
//                collectionYearsModelMap.put(entry.getKey(), model);
//            }
//        } else {
//            createModelForLastYearForCollections();
//        }
//    }
//
//    private void changeLocaleForCollectionsChart() {
//
//        BarChartModel model;
//        if (collectionMonthsModelMap != null && !collectionMonthsModelMap.isEmpty()) {
//            for (Map.Entry<String, BarChartModel> entry : collectionMonthsModelMap.entrySet()) {
//                model = changeSeriesLable(entry.getValue());
//                collectionMonthsModelMap.put(entry.getKey(), model);
//            }
//        } else {
//            createModelForLastYearForCollections();
//        }
//    }
//
//    private BarChartModel changeTenYearsSeriesLable(BarChartModel model) {
//
//        logger.info("changeTenYearsSeriesLable : {}", model);
//
//        model.setTitle(StringMap.getInstance().getYearChartTitle(locale));
//        Axis axis = model.getAxis(AxisType.X);
//        axis.setLabel(StringMap.getInstance().getYearChartAxis(locale));
//
//        Axis yaxis = model.getAxis(AxisType.Y);
//        yaxis.setLabel(StringMap.getInstance().getChartYaxis(locale));
//
//        return model;
//    }
//
//    private BarChartModel changeSeriesLable(BarChartModel model) {
//
//        logger.info("changeSeriesLable : {}", model);
//
//        ChartSeries series = model.getSeries().get(0);
//        ChartSeries newSeries = new ChartSeries();
//
//        series.getData().entrySet().stream()
//                .forEach(x -> {
//                    String key = (String) x.getKey();
//                    String mon = StringUtils.split(key, " ")[0];
//                    String y = StringUtils.split(key, " ")[1];
//                    int total = 0;
//                    if (x.getValue() != null) {
//                        total = (Integer) x.getValue();
//                    }
//                    newSeries.set(StringMap.getInstance().languageMonthMatch(mon, isSwedish) + " " + y, total);
//                });
//
//        model.clear();
//        model.addSeries(newSeries);
//
//        model.setTitle(StringMap.getInstance().getMonthChartTitle(locale));
//        Axis axis = model.getAxis(AxisType.X);
//        axis.setTickAngle(-50);
//        axis.setLabel(StringMap.getInstance().getMonthChartAxis(locale));
//
//        Axis yaxis = model.getAxis(AxisType.Y);
//        yaxis.setLabel(StringMap.getInstance().getChartYaxis(locale));
//
//        return model;
//    }
//
//    public BarChartModel createMonChartForCollection(String collection) {
//
//        logger.info("createMonChartForCollection : {} - {}", collection, collectionMonthsModelMap);
//
//        BarChartModel model = collectionMonthsModelMap.get(collection);
//
//        if (model == null) {
//            createModelForLastYearForCollections();
//            model = collectionMonthsModelMap.get(collection);
//        }
//        return model;
//    }
//
//    public BarChartModel createTenYearsChartForCollection(String collection) {
//
//        BarChartModel model = collectionYearsModelMap.get(collection);
//
//        if (model == null) {
//            createModelForLastTenYearsForCollections();
//            model = collectionYearsModelMap.get(collection);
//        }
//        return model;
//    }
//
//    public BarChartModel getTotalMonthModel() {
//        return totalMonthModel;
//    }
//
//    public BarChartModel getTotalTenYearsModel() {
//        return totalTenYearsModel;
//    } 
}
