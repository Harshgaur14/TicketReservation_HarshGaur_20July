package com.pmu.pmu.services;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pmu.pmu.custom.exception.CustomException;

@Service
public class XYService {
	private static final ArrayList<String> myList;
	
	static {
		 myList=new ArrayList<>();
		 myList.add("3(1)b(I)");
	        myList.add("3(1)b(II)");
	        myList.add("3(1)b(III)");
	        myList.add("3(1)b(IV)");
	        myList.add("3(1)b(V)");
	        myList.add("3(1)b(VI)");
	        myList.add("3(1)b(VII)");
	        myList.add("3(1)b(VIII)");
	        myList.add("3(1)b(IX)");

	        myList.add("3(1)b(X)");
	        myList.add("3(1)b(XI)");
	}
	
	@Autowired
    private MongoTemplate mongoTemplate;
	
	  public List<DBObject> getAlldata() {
	        try {
	        	Query query=new BasicQuery("{}");
	    		List<DBObject> documents=mongoTemplate.find(query,DBObject.class,"Digiposts");
	    		System.out.println(documents);
	    		return documents;
	       
	        } catch (DataAccessException e) {
	            throw new CustomException("Failed to retrieve documents: " + e.getMessage());
	        }
	    }
	  
	  
	  
	  public List<DBObject> getCountbyPlatform(String startDateStr, String endDateStr,String platformwise)
	  {
		  	
	    	 if(startDateStr==null&&endDateStr==null) {
	    		 MatchOperation matchOperation=null;
	    		 if(platformwise!=null) {
	    		  matchOperation = Aggregation.match(Criteria.where("sentiment").in("negative", "neutral")
	    				 .and("platform").is(platformwise));
	    		 }else
	    		 {
	    			 matchOperation = Aggregation.match(Criteria.where("sentiment").in("negative", "neutral"));
	    		 }
	    		    // Group by platform and sentiment, and count them
	    		    GroupOperation groupByPlatformAndSentiment = Aggregation.group("platform", "sentiment").count().as("count");

	    		    // Project to reshape the results
	    		    ProjectionOperation projectToReshape = Aggregation.project()
	    		            .andExpression("platform").as("_id")
	    		            .andExpression("sentiment").as("sentiment")
	    		            .andExpression("count").as("count");

	    		    // Run the aggregation
	    		    Aggregation aggregation = Aggregation.newAggregation(
	    		            matchOperation,
	    		            groupByPlatformAndSentiment,
	    		            projectToReshape
	    		    );

	    		    // Get the results
	    		    AggregationResults<DBObject> results = mongoTemplate.aggregate(aggregation, "Digiposts", DBObject.class);
	    		    List<DBObject> countsByPlatform = results.getMappedResults();

	    		    // Reshape the results to get the desired format
	    		    Map<String, DBObject> platformMap = new HashMap<>();
	    		    for (DBObject obj : countsByPlatform) {
	    		        String platform = (String) obj.get("_id");
	    		        String sentiment = (String) obj.get("sentiment");
	    		        int count = ((Number) obj.get("count")).intValue();

	    		        DBObject platformCount = platformMap.getOrDefault(platform, new BasicDBObject("_id", platform));
	    		        platformCount.put(sentiment, count);
	    		        platformMap.put(platform, platformCount);
	    		    }

	    		    return new ArrayList<>(platformMap.values());
	    		 
	        
	    	 }else {	        
	        try {
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

	        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Ensure UTC time zone

	        // Parse the date strings into Date objects
	        Date startDate = dateFormat.parse(startDateStr);
	        Date endDate = dateFormat.parse(endDateStr);
	      	
	        
	        Criteria dateCriteria = Criteria.where("datetime").gte(startDate).lte(endDate);
	        Criteria sentimentCriteria = Criteria.where("sentiment").in("negative", "neutral");
	        MatchOperation matchOperation=null;
	        if(platformwise!=null) {
	        Criteria platformCriteria = Criteria.where("platform").in(platformwise);
	        matchOperation = Aggregation.match(new Criteria().andOperator(dateCriteria, sentimentCriteria,platformCriteria));
	        }else {
	        matchOperation = Aggregation.match(new Criteria().andOperator(dateCriteria, sentimentCriteria));
	        }
	        // Group by platform and sentiment, and count them
	        GroupOperation groupByPlatformAndSentiment = Aggregation.group("platform", "sentiment").count().as("count");

	        // Project to reshape the results
	        ProjectionOperation projectToReshape = Aggregation.project()
	                .andExpression("_id.platform").as("platform")
	                .andExpression("_id.sentiment").as("sentiment")
	                .andExpression("count").as("count");

	        // Run the aggregation
	        Aggregation aggregation = Aggregation.newAggregation(
	                matchOperation,
	                groupByPlatformAndSentiment,
	                projectToReshape
	        );

	        // Get the results
	        AggregationResults<DBObject> results = mongoTemplate.aggregate(aggregation, "Digiposts", DBObject.class);
	        List<DBObject> countsByPlatform = results.getMappedResults();

	        // Reshape the results to get the desired format
	        Map<String, DBObject> platformMap = new HashMap<>();
	        for (DBObject obj : countsByPlatform) {
	            String platform = (String) obj.get("platform");
	            String sentiment = (String) obj.get("sentiment");
	            int count = ((Number) obj.get("count")).intValue();

	            DBObject platformCount = platformMap.getOrDefault(platform, new BasicDBObject("_id", platform));
	            platformCount.put(sentiment, count);
	            platformMap.put(platform, platformCount);
	        }

	     

	        return new ArrayList<>(platformMap.values());
	        }catch (Exception e) {
	            throw new CustomException("An error occurred while retrieving documents: " + e.getMessage());
	        }
	    	 }

	  }
	  
	  
	  public List<DBObject> getFilteredXY(List<String> platforms, List<String> sections, List<String> sentiments,
				List<String> languages, String startDateStr, String endDateStr, List<String> intensity,List<String> ctype) {
						List<DBObject> documents = null;
						try {
						if (startDateStr != null && endDateStr != null) {
						try {
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
						dateFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Ensure UTC time zone
						
						// Parse the date strings into Date objects
						Date startDate = dateFormat.parse(startDateStr);
						Date endDate = dateFormat.parse(endDateStr);
						
						// Create query to retrieve documents between startDate and endDate
						Query query = new Query(Criteria.where("datetime").gte(startDate).lte(endDate));
						
						// Execute query and retrieve documents
						documents = mongoTemplate.find(query, DBObject.class, "Digiposts");
						} catch (Exception e) {
						throw new CustomException("Error parsing dates or retrieving documents: " + e.getMessage());
						}
						} else {
							Query query=new BasicQuery("{}");
				    		documents=mongoTemplate.find(query,DBObject.class,"Digiposts");
//				    		System.out.println("result"+documents);
						}
						
						// Filter by platforms if specified
						if (platforms != null && !platforms.isEmpty() && !platforms.get(0).equals("ALL")) {
						documents = documents.stream()
						.filter(doc -> platforms.contains(doc.get("platform")))
						.collect(Collectors.toList());
						}
						
						// Filter by sentiments if specified
						if (sentiments != null && !sentiments.isEmpty()) {
							System.out.println("going");
						    documents = documents.stream()
						        .filter(doc -> sentiments.contains(doc.get("sentiment")))
						        .collect(Collectors.toList());
						} else {
						    // If sentiments is null or empty, default to filtering by "negative" and "neutral"
						    List<String> sentimentList = Arrays.asList("negative", "neutral");
						    documents = documents.stream()
						        .filter(doc -> sentimentList.contains(doc.get("sentiment")) && doc.containsField("all_sections"))
						        .collect(Collectors.toList());
						}
						
						// Filter by intensity if specified
						if (intensity != null && !intensity.isEmpty()) {
						documents = documents.stream()
						.filter(doc -> intensity.contains(doc.get("intensity")))
						.collect(Collectors.toList());
						}
						
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
						
						// Filter by sections if specified
						if (sections != null && !sections.isEmpty()) {
						documents = documents.stream()
						.filter(doc -> {
						List<String> allSections = (List<String>) doc.get("all_sections");
						return allSections != null && allSections.stream().anyMatch(sections::contains);
						})
						.collect(Collectors.toList());
						}
						
						} catch (Exception e) {
						throw new CustomException("An error occurred while filtering documents: " + e.getMessage());
						}
			
			return documents;
	}
	  
	  public List<String> getDistinctLanguages() {
			 String[] existLang= {"english","hindi","romanized hindi","urdu","tamil","kannada","telugu","malayalam","bengali","punjabi"};
			 List<String> lang=mongoTemplate.getCollection("Digiposts").distinct("languages", String.class).into(new ArrayList<>());
			 for(String existlang:existLang)
			 {
				 lang.remove(existlang);
			 }
		        return lang;
		    }
	
	  public List<Map.Entry<String, Integer>> getTrendingHashtagsLatest(String startDateStr, String endDateStr,String platformwise) {
			 List<DBObject> documents=null;
		       
		        
		            	        // Fetch documents based on date criteria
		        try {
		        	if(startDateStr!=null&&endDateStr!=null) {
		            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

		            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Ensure UTC time zone

		            // Parse the date strings into Date objects
		            Date startDate = dateFormat.parse(startDateStr);
		            Date endDate = dateFormat.parse(endDateStr);
		           
		            	System.out.println("startDate"+startDate+"  endDate"+endDate);
		                // Create query to retrieve documents between startDate and endDate
		                Criteria dateCriteria = Criteria.where("datetime").gte(startDate).lte(endDate);
		                Query query = new Query(dateCriteria);
		                
		         documents = mongoTemplate.find(query, DBObject.class, "Digiposts");
		         System.out.println("date ");
		        	}else
			        { 
		        		Query query = new BasicQuery("{}");
			        documents = mongoTemplate.find(query, DBObject.class, "Digiposts");
			        System.out.println("date not ");

		        }
		        }catch (Exception e) {
		            throw new CustomException("An error occurred while retrieving documents: " + e.getMessage());
		        }
	
		        
		        // Process documents to extract and count hashtags
		        Map<String, Integer> hashtagCountMap = new HashMap<>();
		        for (DBObject document : documents) {
		            List<String> hashtags = (List<String>) document.get("hashtags");
		            if (hashtags != null) {
		                for (String hashtag : hashtags) {
		                    if (!hashtag.isEmpty()) {
		                        hashtagCountMap.put(hashtag, hashtagCountMap.getOrDefault(hashtag, 0) + 1);
		                    }
		                }
		            }
		        }

		        // Sort hashtags by count in descending order
		        List<Map.Entry<String, Integer>> sortedHashtags = new ArrayList<>(hashtagCountMap.entrySet());
		        sortedHashtags.sort((a, b) -> b.getValue().compareTo(a.getValue()));
		        System.out.println("hey"+sortedHashtags);
		        return sortedHashtags;
	  }
	  
	  
	  
	  
	  public List<DBObject> getCountOfDocumentsBySentimentlatest(String startDateStr, String endDateStr) {
	        
	    	 List<DBObject> documents=null;
	       

	         if(startDateStr==null&&endDateStr==null) {
	          
	             
	         	 }


	         // Fetch documents based on date criteria
	         try {
	             SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

	             dateFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Ensure UTC time zone

	             // Parse the date strings into Date objects
	             Date startDate = dateFormat.parse(startDateStr);
	             Date endDate = dateFormat.parse(endDateStr);
	             
	             
	             
	             
	             
	            
	                 // Create query to retrieve documents between startDate and endDate
	                 Criteria dateCriteria = Criteria.where("datetime").gte(startDate).lte(endDate);
	                 Query query = new Query(dateCriteria);
	        
	          documents = mongoTemplate.find(query, DBObject.class, "posts");
	      
	          Aggregation aggregation = Aggregation.newAggregation(
	                  Aggregation.match(dateCriteria),
	                  Aggregation.group("sentiment").count().as("count")
	          );
	     	 
	         AggregationResults<DBObject> results = mongoTemplate.aggregate(aggregation, "posts", DBObject.class);
	         List<DBObject> countsBySentiment = results.getMappedResults();
	         System.out.println(countsBySentiment);
	         return countsBySentiment;
	         } catch (Exception e) {
	             throw new CustomException("An error occurred while retrieving documents: " + e.getMessage());
	         }
	    	
	    	

	    
	    }
	  
	  public Map<String,Integer> getAllSectionslatest(String startDateStr, String endDateStr)
	    {
	    	Map<String,Integer> countAll=new HashMap<>();
	    	for(String s:myList)
	    	{
	    		countAll.put(s, countSectionlatest(s,startDateStr,endDateStr));
	    	}
	    	System.out.println(countAll);
	    	
	    	return countAll;
	    }
	  
	  
	  public int countSectionlatest(String section,String startDateStr, String endDateStr) {
	    	
	    	 List<DBObject> documents=null;
	      
	         try {
	         if(startDateStr==null&&endDateStr==null) {
	        	 Query query=new BasicQuery("{}");
		    		 documents=mongoTemplate.find(query,DBObject.class,"Digiposts");
		    	
	             
	         	 }else {
	         		 System.out.println("withdate");


	         // Fetch documents based on date criteria
	         
	             SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

	             dateFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Ensure UTC time zone

	             // Parse the date strings into Date objects
	             Date startDate = dateFormat.parse(startDateStr);
	             Date endDate = dateFormat.parse(endDateStr);
	             
	             
	             
	             
	             
	            
	                 // Create query to retrieve documents between startDate and endDate
	                 Criteria dateCriteria = Criteria.where("datetime").gte(startDate).lte(endDate);
	                 Query query = new Query(dateCriteria);
	        
	          documents = mongoTemplate.find(query, DBObject.class, "Digiposts");
	         	 }

	         } catch (Exception e) {
	             throw new CustomException("An error occurred while retrieving documents: " + e.getMessage());
	         }
	    	
	    	int count = 0;
	        for (DBObject document : documents) {
	            List<String> allSections = (List<String>) document.get("all_sections");
	            if (allSections != null && allSections.contains(section)) {
	                count++;
	            }
	        }

	        return count;
	    }
	  
	  
//	  public Map<String,Integer> XYSections(String section,String startDateStr, String endDateStr)
//	    {
//	    	Map<String,Integer> countAll=new HashMap<>();
//	    
//	  		countAll.put(section, countSectionPlatformwise(section,startDateStr,endDateStr));
//	    	
//	    	
//	    	return countAll;
//	    }
	  
	  public Map<String,Integer> countSectionPlatformwise(String section,String startDateStr, String endDateStr) {
	    	
	    	 List<DBObject> documents=null;
	      
	         try {
	         if(startDateStr==null&&endDateStr==null) {
	        	 Query query=new BasicQuery("{}");
		    		 documents=mongoTemplate.find(query,DBObject.class,"Digiposts");
	             
	         	 }else {
	         		

	         // Fetch documents based on date criteria
	         
	             SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

	             dateFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Ensure UTC time zone

	             // Parse the date strings into Date objects
	             Date startDate = dateFormat.parse(startDateStr);
	             Date endDate = dateFormat.parse(endDateStr);
	             
	             
	             
	             
	             
	            
	                 // Create query to retrieve documents between startDate and endDate
	                 Criteria dateCriteria = Criteria.where("datetime").gte(startDate).lte(endDate);
	                 Query query = new Query(dateCriteria);
	        
	          documents = mongoTemplate.find(query, DBObject.class, "Digiposts");
	         	 }

	         } catch (Exception e) {
	             throw new CustomException("An error occurred while retrieving documents: " + e.getMessage());
	         }
	         Map<String,Integer> countplatform=new HashMap<>();
	    	int count = 0;
	        for (DBObject document : documents) {
	            List<String> allSections = (List<String>) document.get("all_sections");
	            String platform = (String) document.get("platform");
	            if (allSections != null && allSections.contains(section)) {
	                count++;
	               
	                countplatform.put(platform,countplatform.getOrDefault(platform, 0)+1);
	            }
	        }
System.out.println("heeloooo"+countplatform);
	        return countplatform;
	    }
	  
	  
	  
	  
}
