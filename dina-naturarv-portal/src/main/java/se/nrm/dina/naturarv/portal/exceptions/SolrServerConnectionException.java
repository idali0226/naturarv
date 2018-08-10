/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.naturarv.portal.exceptions;

/**
 *
 * @author idali
 */
public class SolrServerConnectionException extends NaturarvException {
    
    public SolrServerConnectionException(String message) {
        super(message);
    }

    public SolrServerConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public SolrServerConnectionException(Throwable cause) {
        super(cause);
    }
}
