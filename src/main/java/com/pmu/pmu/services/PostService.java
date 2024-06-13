package com.pmu.pmu.services;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.mongodb.DBObject;



@Service
public class PostService{
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	
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
	
	
	public List<DBObject> getAllDocuments(){
		Query query=new BasicQuery("{}");
		List<DBObject> documents=mongoTemplate.find(query,DBObject.class,"posts");
		List<String> sentimentlist=new ArrayList<>();
		sentimentlist.add("negative");
		sentimentlist.add("neutral");
		
		 if (sentimentlist != null && !sentimentlist.isEmpty()) {
	            documents = documents.stream()
	            		.filter(doc -> sentimentlist.contains(doc.get("sentiment")) &&
                             doc.containsField("all_sections"))
              .collect(Collectors.toList());
	        }
		
		return documents;
	}
	
	
	
	
	public List<DBObject> getDocumentsByPlatform(String platform) {
        Query query = new Query(Criteria.where("platform").is(platform));
        List<DBObject> documents = mongoTemplate.find(query, DBObject.class, "posts");
        return documents;
    }
	
	public List<DBObject> getbySentiments(String sentiments){
		Query query=new Query(Criteria.where("sentiment").is(sentiments));
		List<DBObject> documents=mongoTemplate.find(query, DBObject.class,"posts");
		return documents;
	}
	

    public List<DBObject> getCountOfDocumentsByPlatform() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("platform").count().as("count")
        );

        AggregationResults<DBObject> results = mongoTemplate.aggregate(aggregation, "posts", DBObject.class);
        List<DBObject> countsByPlatform = results.getMappedResults();

        return countsByPlatform;
    }
    
    
    //last 6 days count
    public List<DBObject> getCountOfDocumentsByPlatformlatest( String startDateStr, String endDateStr) {
    	
    	//current date 
    	LocalDateTime currentDateTime = LocalDateTime.now();
        //6days before date
    	LocalDateTime lastWeekDateTime = currentDateTime.minusDays(6);
    	
    	
        // Print the current date and time
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    	 if(startDateStr==null&&endDateStr==null) {
        // Format the current date and time
        String newendDateStr = currentDateTime.format(formatter);
        String newstartDateStr= lastWeekDateTime.format(formatter);
        // Print the formatted date and time
        endDateStr=newendDateStr;
        startDateStr=newstartDateStr;
        
    	 }
        System.out.println("current Date and Time: " + endDateStr);
        System.out.println("Last Week Date and Time: " + startDateStr);
       
        
        
        try {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Ensure UTC time zone

        // Parse the date strings into Date objects
        Date startDate = dateFormat.parse(startDateStr);
        Date endDate = dateFormat.parse(endDateStr);
        
        
        
        
        
       
            // Create query to retrieve documents between startDate and endDate
            Criteria dateCriteria = Criteria.where("datetime").gte(startDate).lte(endDate);
            Query query = new Query(dateCriteria);

            // Execute query and retrieve documents
            List<DBObject> documents = mongoTemplate.find(query, DBObject.class, "posts");
            
            
           // System.out.println("data is "+documents);
            // Create aggregation to count documents by platform within the date interval
            Aggregation aggregation = Aggregation.newAggregation(
                    Aggregation.match(dateCriteria),
                    Aggregation.group("platform").count().as("count")
            );

            // Execute aggregation and retrieve counts by platform within the date interval
            AggregationResults<DBObject> results = mongoTemplate.aggregate(aggregation, "posts", DBObject.class);
            List<DBObject> countsByPlatform = results.getMappedResults();
            
            return countsByPlatform;
        } catch (Exception e) {
            // Handle any exceptions
            e.printStackTrace();
            return null;
        }

    }
    
    
    
    
    
    public List<DBObject> getCountByLaw(){
    	Aggregation aggregation= Aggregation.newAggregation(
    			Aggregation.group("legality").count().as("count")
    			);
    	AggregationResults<DBObject> results=mongoTemplate.aggregate(aggregation, "posts",DBObject.class);
    	List<DBObject> countbyLegality=results.getMappedResults();
    	return countbyLegality;
    	
    }
    
    
    
 public List<Map.Entry<String, Integer>> getTrendingHashtagsLatest(String startDateStr, String endDateStr) {
	 List<DBObject> documents=null;
        // Current date
        LocalDateTime currentDateTime = LocalDateTime.now();
        // 6 days before date
        LocalDateTime lastWeekDateTime = currentDateTime.minusDays(6);

        // Format the current date and time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

        if(startDateStr==null&&endDateStr==null) {
            // Format the current date and time
            String newendDateStr = currentDateTime.format(formatter);
            String newstartDateStr= lastWeekDateTime.format(formatter);
            // Print the formatted date and time
            endDateStr=newendDateStr;
            startDateStr=newstartDateStr;
            
        	 }

        System.out.println("current Date and Time: " + endDateStr);
        System.out.println("Last Week Date and Time: " + startDateStr);

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
         
       

        } catch (Exception e) {
	            // Handle any exceptions
	            e.printStackTrace();
//	            return null;
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
 
        // Return all hashtags with counts
        System.out.println(sortedHashtags);
        return sortedHashtags;
    }
    
    
    
    public List<Map.Entry<String, Integer>> getTrendingHashtags() {
    	
        Query query = new BasicQuery("{}");
        List<DBObject> documents = mongoTemplate.find(query, DBObject.class, "trends");

        // Create a map to store hashtag counts
        Map<String, Integer> hashtagCount = new HashMap<>();

        // Iterate through the documents and count hashtags
        for (  DBObject document : documents) {
            List<String> hashtags = (List<String>) document.get("trends");
            for (String hashtag : hashtags) {
                // Remove leading '#' character
                hashtag = hashtag.replace("#", "");

                // Update the count for the current hashtag
                hashtagCount.put(hashtag, hashtagCount.getOrDefault(hashtag, 0) + 1);
            }
        }

        // Convert the map to a list of entries for sorting
        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(hashtagCount.entrySet());

        // Sort the list based on the count in descending order
        sortedEntries.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        return sortedEntries;
    }
    
    
    
    public List<DBObject> getCountOfDocumentsBySentiment() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("sentiment").count().as("count")
        );

        AggregationResults<DBObject> results = mongoTemplate.aggregate(aggregation, "posts", DBObject.class);
        List<DBObject> countsBySentiment = results.getMappedResults();

        return countsBySentiment;
    }

    
    
    public List<DBObject> getCountOfDocumentsBySentimentlatest(String startDateStr, String endDateStr) {
        
    	 List<DBObject> documents=null;
         // Current date
         LocalDateTime currentDateTime = LocalDateTime.now();
         // 6 days before date
         LocalDateTime lastWeekDateTime = currentDateTime.minusDays(6);

         // Format the current date and time
         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

         if(startDateStr==null&&endDateStr==null) {
             // Format the current date and time
             String newendDateStr = currentDateTime.format(formatter);
             String newstartDateStr= lastWeekDateTime.format(formatter);
             // Print the formatted date and time
             endDateStr=newendDateStr;
             startDateStr=newstartDateStr;
             
         	 }

         System.out.println("current Date and Time: " + endDateStr);
         System.out.println("Last Week Date and Time: " + startDateStr);

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
 	            // Handle any exceptions
 	            e.printStackTrace();
// 	            return null;
 	        }
    	
    	

        return null;
    }
    
   
   
    public int countSection(String section) {
    	List<DBObject> documents = getAllDocuments();
    	int count = 0;
        for (DBObject document : documents) {
            List<String> allSections = (List<String>) document.get("all_sections");
            if (allSections != null && allSections.contains(section)) {
                count++;
            }
        }

        return count;
    }
    
    public Map<String,Integer> getAllSections()
    {
    	Map<String,Integer> countAll=new HashMap<>();
    	for(String s:myList)
    	{
    		countAll.put(s, countSection(s));
    	}
    	System.out.println(countAll);
    	
    	return countAll;
    }
    
    
    public int countSectionlatest(String section,String startDateStr, String endDateStr) {
    	
    	 List<DBObject> documents=null;
         // Current date
         LocalDateTime currentDateTime = LocalDateTime.now();
         // 6 days before date
         LocalDateTime lastWeekDateTime = currentDateTime.minusDays(6);

         // Format the current date and time
         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

         if(startDateStr==null&&endDateStr==null) {
             // Format the current date and time
             String newendDateStr = currentDateTime.format(formatter);
             String newstartDateStr= lastWeekDateTime.format(formatter);
             // Print the formatted date and time
             endDateStr=newendDateStr;
             startDateStr=newstartDateStr;
             
         	 }

         System.out.println("current Date and Time: " + endDateStr);
         System.out.println("Last Week Date and Time: " + startDateStr);

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
      

         } catch (Exception e) {
 	            // Handle any exceptions
 	            e.printStackTrace();
// 	            return null;
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
    
    
    
    public List<DBObject> getBySection(String section) {
        Query query = new Query(Criteria.where("all_sections").is(section));
       
        List<DBObject> documents=mongoTemplate.find(query, DBObject.class, "posts");
		return documents;
       
    }
    
    
    //multiple sections
    
    public List<DBObject> getMultipleSections(List<String> section) {
        Query query = new Query(Criteria.where("all_sections").in(section));
       
        List<DBObject> documents=mongoTemplate.find(query, DBObject.class, "posts");
		return documents;
       
    }
    
    public List<DBObject> getDocumentsByPlatformfilter(String platform) {
        Query query = new Query(Criteria.where("platform").is(platform));
        query.fields().include("content").include("sentiment").include("all_sections").include("all_labels").include("Hashtags");
        return mongoTemplate.find(query, DBObject.class, "posts");
    }
    
   

    public List<DBObject> getTrends(){
    	Query query=new BasicQuery("{}");
    	List<DBObject> documents=mongoTemplate.find(query, DBObject.class,"trends");
    	
    return documents;
    }
  
    public List<DBObject> getMultiplePlatformfilter(List<String> platforms) {
        Query query = new Query(Criteria.where("platform").in(platforms));
        List<DBObject> documents=mongoTemplate.find(query, DBObject.class, "posts");
        return documents;
    }
    public List<DBObject> getMultipleSentiments(List<String> sentiments) {
        Query query = new Query(Criteria.where("sentiment").in(sentiments));
        List<DBObject> documents=mongoTemplate.find(query, DBObject.class, "posts");
        return documents;
    }
//date 
    
    public List<DBObject> getFromToDate(String startDateStr, String endDateStr) {
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
              List<DBObject> documents = mongoTemplate.find(query, DBObject.class, "datetime");

              return documents;
        } catch (Exception e) {
            // Handle any exceptions
            e.printStackTrace();
            return null;
        }
    }
    
    public List<DBObject> getFilteredPosts(List<String> platforms, List<String> sections, List<String> sentiments,
    		List<String> languages,String startDateStr, String endDateStr,List<String> intensity) {
        
//    	System.out.println(platforms);
    	System.out.println("--"+sections);
//    	System.out.println(sentiments);
//    	System.out.println(languages);
    	System.out.println(startDateStr);
    	System.out.println(endDateStr);
    	
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
  	              documents = mongoTemplate.find(query, DBObject.class, "posts");
  	              System.out.println(documents);
//  	              return documents;
  	        } catch (Exception e) {
  	            // Handle any exceptions
  	            e.printStackTrace();
//  	            return null;
  	        }
    		 
    		 
//    		 
//	        	
//    		 Criteria dateCriteria = Criteria.where("datetime").gte(startDateStr).lte(endDateStr);
//    	        Query query = new Query(dateCriteria);
//    	    	
//    	    	
//    	         documents = mongoTemplate.find(query, DBObject.class, "posts");
//    		 System.out.println(documents);
    		 
			}else
			{
				Query query=new BasicQuery("{}");
				 documents=mongoTemplate.find(query,DBObject.class,"posts");
				 System.out.println(documents);
			}
    	
    	
    	
    	
    		if(platforms != null&&!platforms.get(0).equals("ALL")) {
    			
    		
		if (platforms != null && !platforms.isEmpty() ) {
            System.out.println("Filtering by platforms: " + platforms);
            documents = documents.stream()
                    .filter(doc -> platforms.contains(doc.get("platform")))
                    .collect(Collectors.toList());
        }
    		}

		 
		
		 if (sentiments != null && !sentiments.isEmpty()) {
	            documents = documents.stream()
	            		.filter(doc -> sentiments.contains(doc.get("sentiment")) &&
                                doc.containsField("all_sections"))
                 .collect(Collectors.toList());
	          
	        }else {
	        	
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
////		 
//		 if (languages != null && !languages.isEmpty()) {
//	            documents = documents.stream()
//	                    .filter(doc -> languages.contains(doc.get("languages")))
//	                    .collect(Collectors.toList());
//	        }
		 
		 if (sections != null && !sections.isEmpty()) {
	            documents = documents.stream()
	                    .filter(doc -> {
	                        List<String> allSections = (List<String>) doc.get("all_sections");
	                        return allSections != null && allSections.stream().anyMatch(sections::contains);
	                    })
	                    .collect(Collectors.toList());
	        }
		 //System.out.println("after sections"+documents);
		
		 System.out.println("working fine------");
		
		return documents;
    
    }


    	
    
    public List<Map.Entry<String,Integer>> getTopWords()
    {
    	List<DBObject> documents=getAllDocuments();
    	
    	Map<String,Integer> wordCount=new HashMap<>();
    	
    	for(DBObject document:documents) {
    		String content=(String) document.get("content");
    		List<String> words=Arrays.asList(content.split("\\s+"));
    		
    		for(String word:words) {
    			if(word.length()>=5) {
    				wordCount.put(word, wordCount.getOrDefault(word, 0)+1);
    			}
    		}
    		
    		
    	}
    	
    	List<Map.Entry<String,Integer>> sortedWords=new ArrayList<>(wordCount.entrySet());
    	Collections.sort(sortedWords,(a,b)->b.getValue().compareTo(a.getValue()));
    	return sortedWords.subList(0, Math.min(5, sortedWords.size()));
    }
    

    public List<DBObject> getIntensity() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("intensity").count().as("count")
        );

        AggregationResults<DBObject> results = mongoTemplate.aggregate(aggregation, "posts", DBObject.class);
        List<DBObject> countsByIntensity = results.getMappedResults();

        return countsByIntensity;
    }
    
    
    public List<DBObject> getsenfilter(List<String> sentiments) {
        Query query = new BasicQuery("{}");
        List<DBObject> documents = mongoTemplate.find(query, DBObject.class, "posts");

        if (sentiments != null && !sentiments.isEmpty()) {
            documents = documents.stream()
                    .filter(doc -> sentiments.contains(doc.get("sentiment")) &&
                                   doc.containsField("all_sections"))
                    .collect(Collectors.toList());
        }

        System.out.println(documents);
        return documents;
    }
  
    
}









