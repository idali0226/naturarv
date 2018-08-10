/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.naturarv.portal.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List; 
import java.util.Map;
import javax.annotation.PostConstruct;  
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j; 
import se.nrm.dina.naturarv.portal.solr.SolrSearch; 
import se.nrm.dina.naturarv.portal.util.ConstantString;
import se.nrm.dina.naturarv.portal.vo.CollectionData;
import se.nrm.dina.naturarv.portal.vo.SolrRecord;
import se.nrm.dina.naturarv.portal.vo.SolrResult;

/**
 *
 * @author idali
 */
@SessionScoped
@Named 
@Slf4j
public class Search implements Serializable {
    
    private final HttpSession session; 
    private boolean isSimpleSearch; 
    private String defaulSearchText;
    private String locale; 
    private String searchText;
    private List<SolrRecord> results;
    private int totalNumberFound;
    
    private SolrResult solrResult; 
    
//    private List<CollectionData> collections;



    
    @Inject
    private SolrSearch solr;
    
    @Inject
    private Navigator navigator;


    public Search() {
        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true); 
        
        locale = (String) session.getAttribute("locale");
        if (locale == null) {
            locale = "sv";
        }
        
        defaulSearchText = ConstantString.getInstance().getDefaultSeachText("sv".equals(locale)); 
        searchText = defaulSearchText;
    }



    @PostConstruct
    public void init() {
        log.info("init..."); 
        initData();
    }
    
    private void initData() {
        isSimpleSearch = true;  
    }
 
    public void blur() {
        log.info("blur : {}", searchText); 
        if(searchText == null || searchText.length() < 1) { 
            searchText = getDefaulSearchText();
        } 
        log.info("searchText : {}", searchText);
    }
 
    public void advanceSearch() {
        this.isSimpleSearch = false;
    }
    
    /**
     * fullTextSearch - full text search
     * full text search is simple search starts when user start type in search field
     * 
     */
    public void fullTextSearch() {
        log.info("fullTextSearch : {} ", searchText); 
        
  
           
//        session.removeAttribute(SAVED_SEARCH_TEXT);                             // remove previous search text
//        preSearch();                                                            // clear data for new search
        
//        String searchText = ConstantString.getInstance().getWildChar();  
        results = new ArrayList();
        if (searchText != null && searchText.length() > 2) {
            if (!searchText.equals(defaulSearchText)) { 
                SolrResult result = solr.fullTextSearch(searchText, 0);
                if(result != null) {
                    results = result.getRecords(); 
                    totalNumberFound = (int) result.getNumFound();
                    log.info("results: {} -- {}", results.get(0).getCatalogNumber(), totalNumberFound);
                } 
                navigator.results();
//                searchText = SolrSearchHelper.getInstance().buildFullTextSearchText(input);
            }
        }
//        session.setAttribute(SAVED_SEARCH_TYPE, false);
 
//        updateResult = ":searchform:result welcomepagepanel";                                   
//        try { 
//            Map<String, String> savedFilterMap = getSavedFilterMap();
//            solrResult = search(searchText, savedFilterMap, 0);                                 // search
//            session.setAttribute(SAVED_SEARCH_TEXT, searchText);
//            statistic.searchFilteredData(searchText, savedFilterMap, map, type, image);         // statistic data with new search query
// 
//            setResultViewWithNewFullTextSearch();  
//        } catch (EJBException e) {
//            if (e.getCause().getClass().getSimpleName().equals("SolrServerConnectionException")) {
//                addError("Server is not available", e.getLocalizedMessage());
//            }
//        }   
    } 
    
        
//    private SolrResult search(String searchText, Map<String, String> filterMap, int start) {  
//       
//         
//        int numPerPage = getNumPerPage();    
//         
//        try {
//            return solrSearch.searchWithQueryFilter(searchText, filterMap, start, numPerPage, type, map, image, sortby); 
//        } catch (EJBException e) { 
//            if (e.getCause().getClass().getSimpleName().equals("SolrServerConnectionException")) {
//                logger.error("Solr is not available");
//                addError("Solr is not available", e.getLocalizedMessage());
//            }
//        }  
//        return null;
//    } 
//    
        
    
    public List<CollectionData> getCollections() {
        log.info("getCollections");
        List<CollectionData> collections = (List<CollectionData>) session.getAttribute("collections");
         
        if(collections == null || collections.isEmpty()) {
            collections = solr.getCollections(); 
            session.setAttribute("collections", collections);
        }
        return collections;
    } 
    
    
    
 
    
    public void backtoresult() {
        log.info("backtoresult"); 
        navigator.results();
    }

    /**
     * staticData - simple search to retrieve all records
     */
    public void staticData() {
        log.info("staticData");

        resetData();
        
//        solrResult = search(WILD_CHAR, new HashMap<>(), 0);
//        statistic.searchFilteredData(WILD_CHAR, new HashMap<>(), false, false, false);
//        setResultView();
    }
    
//    private SolrResult search(String searchText, Map<String, String> filterMap, int start) {  
//        log.info("search : {}", searchText );
//         
////        int numPerPage = getNumPerPage();    
//         
//        try {
//            return solrSearch.searchWithQueryFilter(searchText, filterMap, start, numPerPage, type, map, image, sortby); 
//        } catch (EJBException e) { 
//            if (e.getCause().getClass().getSimpleName().equals("SolrServerConnectionException")) {
//                logger.error("Solr is not available");
//                addError("Solr is not available", e.getLocalizedMessage());
//            }
//        }  
//        return null;
//    } 


    /**
     * Set page to default data.   
     */
    private void resetData() {  
        
//        type = false;
//        image = false;
//        map = false;  
//        resultview = 0;
//        advance = false;
//        hideadvance = true;
//        expandsAdvance = true;
////        searchQuery = "";
//        
//        sortby = "score";                                                       // reset sorting to sore -- default sort
//        numDisplay = "10";  
//      
//        pageDataMap = new LinkedHashMap<>();                               // remove selected results
//        selectedRecords = new ArrayList<>();
//        checkedRecordsCatlogNumList = new ArrayList<>(); 
//        
//        selectall = false;  
//        searchStart = false;
//         
//        // Remove session data
//        session.removeAttribute(SAVED_QUERY);           
//        session.removeAttribute(SAVED_SEARCH_TEXT);  
//        // End of remove session data
//
//        input = getDefaultText();                                         // reset input field to default text
//        querytext = "";
//                                                            // default display number of page is 10     -- default value
//
//        queries = new HashMap<>(); 
//         
//        QueryBean qb = new QueryBean("", "contains", "text", "");
//        queryBeans = new ArrayList<>();
//        queryBeans.add(qb);
//        querytext = ""; 
//        
//        preSearch();
    }
    
    
    public boolean isIsSimpleSearch() {
        return isSimpleSearch;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }
    
    public String getDefaulSearchText() {
        return defaulSearchText;
    }
    
    public List<SolrRecord> getResults() {
        return results;
    }

    public int getTotalNumberFound() {
        log.info("getTotalNumberFound : {}", totalNumberFound);
        return totalNumberFound;
    } 
}
