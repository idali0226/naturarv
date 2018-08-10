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
public class NaturarvException  extends RuntimeException  {
 
    public NaturarvException(String errorMsg) {
        super(errorMsg);
    }

    public NaturarvException(String errorMsg, Throwable throwable) {
        super(errorMsg, throwable);
    }

    public NaturarvException(Throwable throwable) {
        super(throwable);
    }   
}
