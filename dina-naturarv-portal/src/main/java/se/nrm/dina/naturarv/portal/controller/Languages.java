/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.naturarv.portal.controller;
 
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author idali
 */
@SessionScoped
@Named("language")
@Slf4j
public class Languages implements Serializable {
     
    private String locale = "sv"; 
    
    @Inject
    private Style style;
    
    @Inject
    private MainChart mainChart;

    public Languages() {
        log.info("Languages");
    } 
 
    @PostConstruct
    public void init() {   
        style.setLanguageLink(locale); 
    }
    
    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
      
    public void changelanguage(String locale) { 
        log.info("changelanguage - locale: {}", locale);
         
        setLocale(locale);
        style.setLanguageLink(locale);
        mainChart.changeLanguage(locale);
    }
  
    public String getLanguage() {
        return locale.equals("sv") ? "English" : "Svenska";
    }
      
    public boolean isIsSwedish() {
        return locale.equals("sv");
    }
    
}
