package com.pmu.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pmu.pmu.PmuApplication;
import com.pmu.pmu.services.XPostService;

@SpringBootTest(classes = PmuApplication.class)
public class XPostServiceTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private XPostService xpostService;

    @BeforeEach
    public void setUp() {
    	System.out.println("started");
        MockitoAnnotations.openMocks(this);
    }
    
    
    @Test
    public void testGetAllDocuments2Success() {
        // Create mock documents
        List<DBObject> mockDocuments = new ArrayList<>();
        DBObject mockDocument1 = new BasicDBObject();
        mockDocument1.put("_id", new ObjectId()); // Generate unique ObjectId
        mockDocument1.put("content", "Mock content 1");
        mockDocuments.add(mockDocument1);

        DBObject mockDocument2 = new BasicDBObject();
        mockDocument2.put("_id", new ObjectId()); // Generate unique ObjectId
        mockDocument2.put("content", "Mock content 2");
        mockDocuments.add(mockDocument2);

        // Insert mock documents into MongoDB
        mongoTemplate.insert(mockDocuments, "Xposts3");
        
        // Retrieve documents using the service method
        List<DBObject> result = xpostService.getAllDocuments2();
		/*
		 * System.out.println("result"+result); // Assert expectations
		 * System.out.println(mockDocuments);
		 */     
					assertThat(result).isNotEmpty();
					        assertThat(result).containsAll(mockDocuments);
    }
    @AfterEach
    public void setUp2() {
    	System.out.println("started ended");
      //  MockitoAnnotations.openMocks(this);
    }
    
}
