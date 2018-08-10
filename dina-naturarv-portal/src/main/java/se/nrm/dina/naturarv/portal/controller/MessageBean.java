/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.naturarv.portal.controller;

import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author idali
 */
@Named(value = "messages") 
@RequestScoped
public class MessageBean implements Serializable {
    
    private final FacesContext facesContext;
    
    public MessageBean() {
        facesContext = FacesContext.getCurrentInstance();
    }
    

    public void addErrors(String errorTitle, List<String> errorMsgs) { 
        errorMsgs.parallelStream().forEach((errorMsg) -> {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorTitle, errorMsg));
        }); 
    }
        
    public void addError(String errorTitle, String errorMsg) {
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorTitle, errorMsg));
    }

    public void addInfo(String msgTitle, String msg) {
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msgTitle, msg));
    }

    public void addWarning(String warningTitle, String warningMsg) {
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, warningTitle, warningMsg));
    } 
    
    public void addWarnings(String warningTitle, List<String> warningMsgs) { 
        warningMsgs.parallelStream().forEach((warningMsg) -> {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, warningTitle, warningMsg));
        }); 
    }
}
