/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.naturarv.portal.controller;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import lombok.extern.slf4j.Slf4j;
import se.nrm.dina.naturarv.portal.util.CSSName;

/**
 *
 * @author idali
 */
@SessionScoped
@Named("style")
@Slf4j
public class Style implements Serializable {
    
    private String svLink; 
    private String enLink; 
    
    private static final String SWEDISH = "sv";
    private static final String ENGLISH = "en"; 
      
    private String tabStart;
    private String tabCollections;
    private String tabPartner;
    private String tabFaq;
    private String tabAbout;
    
    
    
    private final String activeLink = CSSName.getInstance().ACTIVE_LINK; 
    private final String inactiveLink = CSSName.getInstance().INACTIVE_LINK;
//    
    private final String activeTabLink = CSSName.getInstance().ACTIVE_TAB_LINK;
    private final String inactiveTabLink = CSSName.getInstance().INACTIVE_TAB_LINK;
       
    public Style() {
        svLink = activeLink;
        enLink = inactiveLink; 
        
        log.info("language link : {} -- {}", svLink, enLink);
        
        tabStart = activeTabLink;
        tabCollections = inactiveTabLink;
        tabPartner = inactiveTabLink;
        tabFaq = inactiveTabLink;
        tabAbout = inactiveTabLink;
    }
  
    public String getEnLink() {
        return enLink;
    }

    public void setEnLink(boolean isActive) {
        this.enLink = isActive ? activeLink : inactiveLink;
    }
 
    public String getSvLink() { 
        return svLink;
    }

    public void setSvLink(boolean isActive) {
        this.svLink = isActive ? activeLink : inactiveLink;
    } 
 
    public String getTabStart() {
        return tabStart;
    }

    public String getTabCollections() {
        return tabCollections;
    }

    public String getTabPartner() {
        return tabPartner;
    }

    public String getTabFaq() {
        return tabFaq;
    }

    public String getTabAbout() {
        return tabAbout;
    }
 
    
    public void resetTabStyle(int tabIndex) {
         
        tabStart = CSSName.getInstance().INACTIVE_TAB_LINK;
        tabCollections = CSSName.getInstance().INACTIVE_TAB_LINK;
        tabPartner = CSSName.getInstance().INACTIVE_TAB_LINK;
        tabAbout = CSSName.getInstance().INACTIVE_TAB_LINK;
        
        switch (tabIndex) {
            case 0:
                tabStart = CSSName.getInstance().ACTIVE_TAB_LINK;
                break;
            case 1:
                tabCollections = CSSName.getInstance().ACTIVE_TAB_LINK;
                break;
            case 2:
                tabPartner = CSSName.getInstance().ACTIVE_TAB_LINK;
                break;
            case 3:
                tabFaq = CSSName.getInstance().ACTIVE_TAB_LINK;
                break;
            case 4: 
                tabAbout =CSSName.getInstance().ACTIVE_TAB_LINK;
                break;
            default:
                tabStart = CSSName.getInstance().ACTIVE_TAB_LINK;
                break;
        }
    }
   
    public void setLanguageLink(String locale) {
        
        log.info("setLanguageLink : {}", locale);
         
        
        setSvLink(false);
        setEnLink(false); 

        switch (locale) {
            case SWEDISH:
                setSvLink(true);
                break;
            case ENGLISH:
                setEnLink(true);
                break; 
        }
    }    
    
}
