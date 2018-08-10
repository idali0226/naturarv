/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.naturarv.portal.controller;

import java.io.IOException;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.extern.slf4j.Slf4j; 

/**
 *
 * @author idali
 */
@Named("navigate")
@SessionScoped
@Slf4j
public class Navigator implements Serializable {

//    private RequestContext requestContext;
    private ExternalContext externalContext;

    private static final String HOME_PATH = "/faces/pages/home.xhtml";
    private static final String COLLECTIONS_PATH = "/faces/pages/collections.xhtml";
    private static final String PARTNERS_PATH = "/faces/pages/partners.xhtml";
    private static final String FAQ_PATH = "/faces/pages/faq.xhtml";
    private static final String ABOUT_PATH = "/faces/pages/about.xhtml";
    private static final String RESULTS_PATH = "/faces/pages/results.xhtml";

    @Inject
    private Style style;
 
    public Navigator() {
        log.info("Navigator");
    }

    private void redirectPage(String path) {
        externalContext = FacesContext.getCurrentInstance().getExternalContext();
        try {
            externalContext.redirect(externalContext.getRequestContextPath() + path);
        } catch (IOException ex) {
        }
    }

    public void home() {
        log.info("home");

        style.resetTabStyle(0);
        redirectPage(HOME_PATH);
    }
    
    public void results() {
        redirectPage(RESULTS_PATH);
    }

    public void collections() {
        log.info("collections");
  
        style.resetTabStyle(1);
        redirectPage(COLLECTIONS_PATH);
    }

    public void partners() {
        log.info("partners");

        style.resetTabStyle(2);
        redirectPage(PARTNERS_PATH);
    }

    public void faq() {
        log.info("faq");

        style.resetTabStyle(3);
        redirectPage(FAQ_PATH);
    }

    public void about() {
        log.info("about");

        style.resetTabStyle(4);
        redirectPage(ABOUT_PATH);
    }
}
