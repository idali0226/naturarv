/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.naturarv.portal.solr;
 
import java.io.Serializable;
import lombok.extern.slf4j.Slf4j;
import se.nrm.dina.naturarv.portal.util.Util;


/**
 *
 * @author idali
 */
@Slf4j
public class SolrSearchHelper implements Serializable {
    
    private final static String FTX = "ftx";
    private final static String TEXT = "text";
    private final static String HT = "ht";
    
    public SolrSearchHelper() {
        
    }
    
    public String buildFullTextSearch(String text) { 
        log.info("buildFullTextSearch");
        
        if(text != null && !text.isEmpty()) {
            text = Util.getInstance().replaceChars(text.trim());
             
            StringBuilder sb = new StringBuilder();
            sb.append("(");
            sb.append(Util.getInstance().buildContainsString(text, FTX, true));
            sb.append(") ");
            sb.append("(");
            sb.append(Util.getInstance().buildStartsWithString(text, FTX, true));
            sb.append(") ");
            sb.append("(");
            sb.append(Util.getInstance().buildContainsString(text, TEXT, false));
            sb.append(")");
            return sb.toString().trim();
        } 
        
        return text;
    }  
    

}
