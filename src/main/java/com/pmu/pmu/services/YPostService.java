package com.pmu.pmu.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.mongodb.DBObject;
import com.pmu.pmu.custom.exception.CustomException;

@Service
public class YPostService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<DBObject> getAllDocuments2() {
        try {
            Query query = new BasicQuery("{}");
            return mongoTemplate.find(query, DBObject.class, "Yposts3");
        } catch (DataAccessException e) {
            throw new CustomException("Failed to retrieve documents: " + e.getMessage());
        }
    }

    public List<DBObject> getFilteredPosts(List<String> platforms, List<String> sections, List<String> sentiments,
			List<String> languages, String startDateStr, String endDateStr, List<String> intensity,List<String> ctype) {
		 List<DBObject> documents=null;
    	 if(startDateStr!=null&&endDateStr!=null) {
	        	
    		 
    		 try {
    		        
    	          
    	            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    	            // Create query to retrieve documents between startDate and endDate
    	        	  dateFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Ensure UTC time zone

    	              // Parse the date strings into Date objects
    	              Date startDate = dateFormat.parse(startDateStr);
    	              Date endDate = dateFormat.parse(endDateStr);

    	              // Create query to retrieve documents between startDate and endDate
    	              Query query = new Query(Criteria.where("datetime").gte(startDate).lte(endDate));

    	              // Execute query and retrieve documents
    	              documents = mongoTemplate.find(query, DBObject.class, "Yposts3");
//    	              System.out.println(documents);
//    	              return documents;
    	        }catch (Exception e) {
    	            throw new CustomException("An error occurred while retrieving documents: " + e.getMessage());
    	        }
    		 
    		 
    		 
    		 
//    		 Criteria dateCriteria = Criteria.where("datetime").gte(startDateStr).lte(endDateStr);
//    	        Query query = new Query(dateCriteria);
//    	    	
//    	    	
//    	         documents = mongoTemplate.find(query, DBObject.class, "evidhur");
//    		 System.out.println(documents);
    		 
			}else
			{
				Query query=new BasicQuery("{}");
				 documents=mongoTemplate.find(query,DBObject.class,"Yposts3");
//				 System.out.println(documents);
			}
    	
    	
    	
    	
    		if(platforms != null&&!platforms.get(0).equals("ALL")) {
    			
    		
		if (platforms != null && !platforms.isEmpty() ) {
            System.out.println("Filtering by platforms: " + platforms);
            documents = documents.stream()
                    .filter(doc -> platforms.contains(doc.get("platform")))
                    .collect(Collectors.toList());
        }
    		}

//    	 System.out.println("hey2"+documents);
		
		 if (sentiments != null && !sentiments.isEmpty()) {
	            documents = documents.stream()
	                    .filter(doc -> sentiments.contains(doc.get("sentiment")))
	                    .collect(Collectors.toList());
	        }else 
	        {
	        	List<String> sentimentlist=new ArrayList<>();
	    		sentimentlist.add("negative");
	    		sentimentlist.add("neutral");
	    		
	    		 if (sentimentlist != null && !sentimentlist.isEmpty()) {
	    	            documents = documents.stream()
	    	            		.filter(doc -> sentimentlist.contains(doc.get("sentiment")) &&
	                                 doc.containsField("all_sections"))
	                  .collect(Collectors.toList());
	    	        }
	    		 
	        }
		 
		 
		 if (intensity != null && !intensity.isEmpty()) {
	            documents = documents.stream()
	                    .filter(doc -> intensity.contains(doc.get("intensity")))
	                    .collect(Collectors.toList());
	        }
		// System.out.println("after sentiments"+documents);
		 
		 
		 if (languages != null && !languages.isEmpty()) {
			 if(languages.contains("other"))
			 {
				 languages.remove("other");
				 for(String lang:getDistinctLanguages())
				 {
					 if(!languages.contains(lang)) {
						 languages.add(lang);
					 }
					
				 }
				 System.out.println(languages);
			 }
	            documents = documents.stream()
	                    .filter(doc -> languages.contains(doc.get("languages")))
	                    .collect(Collectors.toList());
	        }
		 
		 if (sections != null && !sections.isEmpty()) {
	            documents = documents.stream()
	                    .filter(doc -> {
	                        List<String> allSections = (List<String>) doc.get("all_sections");
	                        return allSections != null && allSections.stream().anyMatch(sections::contains);
	                    })
	                    .collect(Collectors.toList());
	        }
		 //System.out.println("after sections"+documents);
		
		 if (ctype != null && !ctype.isEmpty()) {
	            documents = documents.stream()
	                    .filter(doc -> {
	                        String contentType = doc.get("content_type").toString();
	                        return ctype.stream().anyMatch(contentType::contains);
	                    })
	                    .collect(Collectors.toList());
	        }

		
		return documents;
	}
    
    public List<String> getDistinctLanguages(){
    	 String[] existLang= {"english","hindi","romanized hindi","urdu","tamil","kannada","telugu","malayalam","bengali","punjabi"};
		 List<String> lang=mongoTemplate.getCollection("Yposts3").distinct("languages", String.class).into(new ArrayList<>());
		 for(String existlang:existLang)
		 {
			 lang.remove(existlang);
		 }
	        return lang;
	        }
}
