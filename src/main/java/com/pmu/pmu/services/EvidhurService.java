package com.pmu.pmu.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.mongodb.DBObject;

@Service
public class EvidhurService {

	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	
	public List<DBObject> getAllDocuments2(){
		Query query=new BasicQuery("{}");
		List<DBObject> documents=mongoTemplate.find(query,DBObject.class,"evidhur");
		return documents;
	}
	
}
