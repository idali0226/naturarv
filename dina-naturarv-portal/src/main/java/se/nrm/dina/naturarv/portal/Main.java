/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.naturarv.portal;

import java.io.IOException; 
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

/**
 *
 * @author idali
 */
public class Main {
     
    public static void main(String[] args) {
        try {
            HttpSolrClient client = new HttpSolrClient.Builder("http://localhost:8983/solr").build();
            SolrQuery query = new SolrQuery();
            
            query.setQuery("*");
            query.setFields("clnm");
            query.setStart(0);
            
            QueryResponse response = client.query(query);
            SolrDocumentList results = response.getResults();
            for (int i = 0; i < results.size(); ++i) {
                System.out.println(results.get(i));
            }
        } catch (SolrServerException | IOException ex) { 
            
        }
    }
}
