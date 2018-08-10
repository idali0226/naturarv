/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.naturarv.portal.solr;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.util.NamedList;
import se.nrm.dina.naturarv.portal.exceptions.SolrServerConnectionException;
import se.nrm.dina.naturarv.portal.logic.ConfigProperties;
import se.nrm.dina.naturarv.portal.vo.CollectionData;
import se.nrm.dina.naturarv.portal.vo.SolrRecord;
import se.nrm.dina.naturarv.portal.vo.SolrResult;

/**
 *
 * @author idali
 */
@Slf4j
public class SolrSearch implements Serializable {

    private HttpSolrClient client;
    private SolrQuery query;

    private final String WILD_SEARCH_TEXT = "*";

    @Inject
    private ConfigProperties config;

    @Inject
    private SolrSearchHelper searchHelper;

    public SolrSearch() {
        log.info("solr");
    }

    @PostConstruct
    public void init() {
        log.info("init...");

        client = new HttpSolrClient.Builder(config.getSolrPath()).build();
        query = new SolrQuery();
    }

    public SolrResult fullTextSearch(String text, int start) {
        String searchText = searchHelper.buildFullTextSearch(text);
        query = new SolrQuery();
        query.setQuery(searchText);
        query.setStart(start);

        try {
//            if (sort != null) {
//                if (sort.equals("score")) {
//                    query.addSort("score", SolrQuery.ORDER.desc);
//                } else {
//                    query.addSort(sort, SolrQuery.ORDER.asc);
//                }
//            }
//            addSearchFilters(query, null, type, map, image);

            QueryResponse response = client.query(query);

            long numFound = response.getResults().getNumFound();

            log.info("num of results: {}", numFound);
            if (numFound > 0) {
                List<SolrRecord> resultList = response.getBeans(SolrRecord.class);
                return new SolrResult(numFound, start, resultList);
            }
        } catch (SolrServerException | IOException ex) {
        }
        return null;
    }

//    public SolrResult fullTextSearch(String text, int start, int numFetch, boolean type, boolean map, boolean image, String sort) {
//        String searchText = searchHelper.buildFullTextSearch(text);
//        query = new SolrQuery();
//        query.setQuery(searchText);  
//        query.setStart(start);
//        query.setRows(numFetch);
//        
//        try {
//            if (sort != null) {
//                if (sort.equals("score")) {
//                    query.addSort("score", SolrQuery.ORDER.desc);
//                } else {
//                    query.addSort(sort, SolrQuery.ORDER.asc);
//                }
//            } 
////            addSearchFilters(query, null, type, map, image);
// 
//            QueryResponse response = client.query(query); 
//        
//            long numFound = response.getResults().getNumFound();
//            
//
//            if (numFound > 0) {
//                List<SolrRecord> resultList = response.getBeans(SolrRecord.class);
//                return new SolrResult(numFound, start, resultList);
//            } else {
//                return null;
//            }
//        } catch (SolrServerException | IOException ex) { 
//        }
//        return null; 
//
//    }
    public List<CollectionData> getCollections() {

        List<CollectionData> list = new ArrayList();
        try {
            query = new SolrQuery();
            query.setQuery(WILD_SEARCH_TEXT);
            query.setFields("clnm");
            query.setParam("group", true);
            query.setParam("group.field", "cln");
            query.setStart(0);
            query.setRows(200);

            NamedList response = client.query(query).getResponse();
            NamedList groupInfo = (NamedList) response.get("grouped");
            NamedList thisGroupInfo = (NamedList) groupInfo.get("cln");

            List<Object> groupData = (List<Object>) thisGroupInfo.get("groups");

            list = groupData.stream()
                    .map(d -> (NamedList) d)
                    .map(this::buildCollectionData)
                    .collect(Collectors.toList());
        } catch (SolrServerException | IOException ex) {
        }

        list.sort((CollectionData c1, CollectionData c2) -> c1.getTotal() - c2.getTotal());
        Collections.reverse(list);
        return list;
    }

    private CollectionData buildCollectionData(NamedList d) {
        SolrDocumentList sdl = (SolrDocumentList) d.get("doclist");
        SolrDocument document = sdl.get(0);
        String code = (String) d.getAll("groupValue").get(0);
        int totalDocsInThisGroup = (int) sdl.getNumFound();
        return new CollectionData(code, (String) document.getFieldValue("clnm"), totalDocsInThisGroup);
    }

    /**
     * Search for collectionobjects created in last year. Facet by create date
     * of month.
     *
     * @param strDate - the date to start
     * @param collectionCode - collection code
     * @return Map<String, Integer> - key = Number of month in String. Value =
     * total of collectionobjects created
     */
    public Map<String, Integer> getLastYearRegistedDataForCollection(String strDate, String collectionCode) {

        log.info("getLastYearRegistedData : {} -- {}", strDate, collectionCode);

        StringBuilder sb = new StringBuilder();
        sb.append("ctd:[");
        sb.append(strDate);
        sb.append(" TO *]");

        String searchText = WILD_SEARCH_TEXT;
        if (collectionCode != null) {
            searchText = "cln:" + collectionCode.trim();
        }

        query = new SolrQuery();
        query.setQuery(searchText.trim())
                .setFilterQueries(sb.toString())
                .setFacet(true)
                .addFacetField("ctdmt")
                .setFacetMinCount(1)
                .setFacetLimit(100);
        query.setStart(0);
        query.setRows(30);
        try {
            FacetField field = client.query(query).getFacetField("ctdmt");

            List<FacetField.Count> counts = field.getValues();
            return counts.stream()
                    .collect(Collectors.toMap(
                            m -> m.getName(),
                            m -> (int) m.getCount()
                    ));
        } catch (SolrServerException | IOException ex) {
            throw new SolrServerConnectionException(ex.getMessage());
        } 
    }

    /**
     * getLastTenYearsRegistedData - get registered data from last ten years.
     *
     * @param fromYear - the start year
     * @param toYear - the current year
     * @return Map<String, Integer>
     */
    public Map<String, Integer> getLastTenYearsRegistereddData(int fromYear, int toYear, String collectionCode) {
        log.info("getLastTenYearsRegistedData : {} - {}", fromYear, toYear);
        
        
        buildText(collectionCode);

        Map<String, Integer> map = new HashMap<>();
        int accumlateCount = getPreviousRegisteredData(fromYear, collectionCode);
        map.put(String.valueOf(fromYear), accumlateCount);
        String searchText;
         
        
        for (int i = fromYear + 1; i <= toYear; i++) {
            searchText = "ctdyr:" + i; 
            query = new SolrQuery();
            query.setQuery(searchText); 
            try {
                SolrDocumentList documents = client.query(query).getResults(); 
                accumlateCount += (int)documents.getNumFound();
                map.put(String.valueOf(i), accumlateCount);  
            } catch (SolrServerException | IOException ex) {
                log.warn(ex.getMessage());
            } 
        } 
        return map;
    }
    
    private String buildText(String collectionCode) {
        StringBuilder sb = new StringBuilder();
        if (collectionCode != null) {
            sb.append("cln:");
            sb.append(collectionCode);
        }
        return sb.toString();
    }

    private int getPreviousRegisteredData(int endYear, String collectionCode) {
        String searchText = "ctdyr:[0 TO " + endYear + "]";

        query = new SolrQuery();
        query.setQuery(searchText);

        try {
            return (int)client.query(query).getResults().getNumFound(); 
        } catch (SolrServerException | IOException ex) {
            log.warn(ex.getMessage());
        }
        return 0;
    }

    /**
     * getLastTenYearsRegistedData - get registed data from last ten years.
     *
     * @param fromYear - the start year
     * @param code - collection code
     * @return Map<String, Integer>
     */
    public Map<String, Integer> getLastTenYearsRegistedData(int fromYear, String code) {
        log.info("getLastTenYearsRegistedData : {}", fromYear);

        String searchText = WILD_SEARCH_TEXT;
        if (code != null) {
            searchText = "cln:" + code;
        }

        try {
            query = new SolrQuery();
            query.setQuery(searchText).setFacet(true).addFacetField("ctdyr").setFacetMinCount(1).setFacetLimit(1000);
            query.setStart(0);
            query.setRows(100);
            query.addSort("ctdyr", SolrQuery.ORDER.asc);

            QueryResponse response = client.query(query);

            FacetField field = response.getFacetField("ctdyr");

            List<FacetField.Count> counts = field.getValues();
            counts.sort((c1, c2) -> c1.getName().compareTo(c2.getName()));

            Map<String, Integer> map = new HashMap<>();
            Accumulator accumulator = new Accumulator();
            counts.stream()
                    .forEach(x -> {
                        accumulator.add(x.getCount());
                        if (Integer.parseInt(x.getName()) >= fromYear) {
                            map.put(x.getName(), (int) accumulator.total);
                        }
                    });
            return map;
        } catch (SolrServerException | IOException ex) {
            log.warn(ex.getMessage());
        }
        return null;
    }

    /**
     * Accumulator class to help accumulator long in foreach statement
     */
    private static class Accumulator {

        private long total = 0;

        private long nrmTotal = 0;
        private long gnmTotal = 0;

        public void add(long value) {
            total += value;
        }

        public void addNrmTotal(long value) {
            nrmTotal += value;
        }

        public void addGnmTotal(long value) {
            gnmTotal += value;
        }
    }

}
