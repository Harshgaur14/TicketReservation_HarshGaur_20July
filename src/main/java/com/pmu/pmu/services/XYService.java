package com.pmu.pmu.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
public class XYService {

	@Autowired
    private MongoTemplate mongoTemplate;
	
	  public List<DBObject> getAlldata() {
	        try {
	            Query query = new BasicQuery("{}");
	            return mongoTemplate.find(query, DBObject.class, "XYdata");
	        } catch (DataAccessException e) {
	            throw new CustomException("Failed to retrieve documents: " + e.getMessage());
	        }
	    }
	  
	  public List<DBObject> getFilteredPosts(List<String> platforms, List<String> sections, List<String> sentiments,
	            List<String> languages, String startDateStr, String endDateStr, List<String> intensity) {
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
						documents = mongoTemplate.find(query, DBObject.class, "XYdata");
						} catch (Exception e) {
						throw new CustomException("Error parsing dates or retrieving documents: " + e.getMessage());
						}
						} else {
						Query query = new BasicQuery("{}");
						documents = mongoTemplate.find(query, DBObject.class, "XYdata");
						}
						
						// Filter by platforms if specified
						if (platforms != null && !platforms.isEmpty() && !platforms.get(0).equals("ALL")) {
						documents = documents.stream()
						.filter(doc -> platforms.contains(doc.get("platform")))
						.collect(Collectors.toList());
						}
						
						// Filter by sentiments if specified
						if (sentiments != null && !sentiments.isEmpty()) {
						documents = documents.stream()
						.filter(doc -> sentiments.contains(doc.get("sentiment")) &&
						 doc.containsField("all_sections"))
						.collect(Collectors.toList());
						} else {
						List<String> sentimentList = Arrays.asList("negative", "neutral");
						documents = documents.stream()
						.filter(doc -> sentimentList.contains(doc.get("sentiment")) &&
						 doc.containsField("all_sections"))
						.collect(Collectors.toList());
						}
						
						// Filter by intensity if specified
						if (intensity != null && !intensity.isEmpty()) {
						documents = documents.stream()
						.filter(doc -> intensity.contains(doc.get("intensity")))
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
	  
	  
	
}
