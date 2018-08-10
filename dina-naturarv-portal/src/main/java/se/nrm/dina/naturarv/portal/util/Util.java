/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.naturarv.portal.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import se.nrm.dina.naturarv.portal.vo.CollectionData;

/**
 *
 * @author idali
 */
public class Util {

    private static final String DEFAULT_LANGUAGE = "defaultlanguage";

    private static final DateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final DateFormat SIMPLE_DATE_FORMAT_WITH_TIME = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:SSS'Z'");

    private static final SimpleDateFormat GENERIC_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:SSS'Z'");

    private static Util instance = null;

    public static synchronized Util getInstance() {
        if (instance == null) {
            instance = new Util();
        }
        return instance;
    }

    public String dateToDateTimeString(LocalDate date) {

        if (date == null) {
            return null;
        }
        Instant instant = date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        return SIMPLE_DATE_FORMAT_WITH_TIME.format(Date.from(instant));
    }
    
    public String dateToStringWithTime(Date date) {
        if(date == null) {
            return null;
        } else {
            return SIMPLE_DATE_FORMAT_WITH_TIME.format(date);
        }
    }
    
    public String firstDayOfMonthInString(int month, int year) {
        LocalDate date = LocalDate.of(year, month, 1); 
        return dateToDateTimeString(date);
    }
    
    public String getCollectionCodeByName(List<CollectionData> collections, String collectionName) { 
        if(collections != null) {
            return collections.stream()
                    .filter(c -> (c.getName() == null ? collectionName == null : c.getName().equals(collectionName)))
                    .findFirst()
                    .get()
                    .getCode();
        }
        return null;
    }
    
    public String buildContainsString(String value, String field, boolean boost) { 
        StringBuilder sb = new StringBuilder();
        String[] strings = value.split(" ");
        if(strings.length == 1) {
            sb.append(field);
            sb.append(":*");
            sb.append(value);
            sb.append(boost ? "*^2" : "*");
        } else { 
            sb.append("(");
            for (String s : strings) {
                if(!s.isEmpty()) {
                    sb.append("+");
                    sb.append(field);
                    sb.append(":*");
                    sb.append(s);
                    sb.append(boost ? "*^2 " : "* "); 
                }
            }
            sb.append(")");
        }
        return sb.toString().trim();
    }
    
    public String buildStartsWithString(String value, String field, boolean boost) {
        StringBuilder sb = new StringBuilder();
        String[] strings = value.split(" ");
        
        if(strings.length > 1) {
            sb.append("(+"); 
        } 
        sb.append(field);
        sb.append(":");
        sb.append(strings[0]);
        sb.append(boost ? "*^2 " : "* ");

        for (int i = 1; i < strings.length; i++) {
            if(!strings[i].isEmpty()) {
                sb.append("+");
                sb.append(field);
                sb.append(":*");
                sb.append(strings[i]);
                sb.append(boost ? "*^2 " : "* "); 
            }
        }
        if(strings.length > 1) {
            sb.append(")"); 
        } 
        return sb.toString().trim();
    }
    
     
    /**
     * Replace "[,],(,)" chars with empty space
     * 
     * @param value
     * @return String
     */
    public String replaceChars(String value) {
        String s = value.replaceAll("[\\[\\](),]", " ");  
        return s.trim(); 
    }
 
}
