/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.naturarv.portal.util;

/**
 *
 * @author idali
 */
public class ConstantString {
    
    private final String SIMPLE_SEARCH_DEFAULT_TEXT_EN = "Search collections (species, genus, family, collectors, location, etc.)";
    private final String SIMPLE_SEARCH_DEFAULT_TEXT_SV = "Sök i samlingar (art, släkte, familj, insamlare, plats etc.)";

    private final String MONTH_CHART_TITLE_SV = "Registrerade föremål senaste 12 månaderna";
    private final String YEAR_CHART_TITLE_SV = "Ackumulerat antal registrerade föremål";

    private final String MONTH_CHART_TITLE_EN = "Registered specimens last 12 months";
    private final String YEAR_CHART_TITLE_EN = "Cumulative number of registered specimens";
    
    private final String MONTH_CHART_AXIS_SV = "Månad";
    private final String MONTH_CHART_AXIS_EN = "Month";
    
    private final String YEAR_CHART_AXIS_SV = "År";
    private final String YEAR_CHART_AXIS_EN = "Year";
    
    private final String CHART_YAXIS_SV = "Antal föremål";
    private final String CHART_YAXIS_EN = "Total specimens";
    
    private final String WILD_CHAR = "*"; 

    private static ConstantString instance = null;

    public static synchronized ConstantString getInstance() {
        if (instance == null) {
            instance = new ConstantString();
        }
        return instance;
    }
    
    public String getWildChar() {
        return WILD_CHAR;
    }
    
    public String getDefaultSeachText(boolean isSwedish) {
        return isSwedish ? SIMPLE_SEARCH_DEFAULT_TEXT_SV : SIMPLE_SEARCH_DEFAULT_TEXT_EN;
    }
    
    public String getChartYaxis(boolean isSwedish) {
        return isSwedish ? CHART_YAXIS_SV : CHART_YAXIS_EN;
    }
    
    public String getMonthChartAxis(boolean isSwedish) {
        return isSwedish ? MONTH_CHART_AXIS_SV : MONTH_CHART_AXIS_EN;
    }
    
    public String getYearChartAxis(boolean isSwedish) {
        return isSwedish ? YEAR_CHART_AXIS_SV : YEAR_CHART_AXIS_EN;
    }
    
    public String getMonthChartTitle(boolean isSwedish) {
        return isSwedish ? MONTH_CHART_TITLE_SV : MONTH_CHART_TITLE_EN;
    }
    
    public String getYearChartTitle(boolean isSwedish) {
        return isSwedish ? YEAR_CHART_TITLE_SV : YEAR_CHART_TITLE_EN;
    }

}
