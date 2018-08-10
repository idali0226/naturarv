/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.naturarv.portal.logic;

import java.time.LocalDate;
import javax.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author idali
 */
@ApplicationScoped
@Slf4j
public class ApplicationInitData {
    
    private LocalDate today;

    public ApplicationInitData() {
        log.info("ApplicationInitData");
        
        today = LocalDate.now();
    }
    
    public LocalDate getTodaysDate() {
        return today;
    }
    
    

}
