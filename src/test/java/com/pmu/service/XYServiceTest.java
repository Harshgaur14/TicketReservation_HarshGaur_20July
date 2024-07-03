package com.pmu.service;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pmu.pmu.PmuApplication;
import com.pmu.pmu.services.XYService;

@SpringBootTest(classes = PmuApplication.class)
public class XYServiceTest {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private XYService xyService;
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	
//	  @Test
//	    public void testGetFilteredXY() throws Exception {
//	        // Sample data for testing
//	        List<DBObject> mockDocuments = new ArrayList<>();
//	        
//	        DBObject doc1 = new BasicDBObject();
//	        doc1.put("_id", new ObjectId());
//	        doc1.put("platform", "YOUTUBE");
//	        doc1.put("sentiment", "negative");
//	        doc1.put("intensity", "High");
//	        doc1.put("languages", "hindi");
//	        doc1.put("all_sections", Arrays.asList("3(1)b(II)"));
//	        doc1.put("datetime", new Date());
//
//	        DBObject doc2 = new BasicDBObject();
//	        doc2.put("_id", new ObjectId());
//	        doc2.put("platform", "TWITTER");
//	        doc2.put("sentiment", "negative");
//	        doc2.put("intensity", "Low");
//	        doc2.put("languages", "hin_Deva");
//	        doc2.put("all_sections", Arrays.asList("3(1)b(V)", "3(1)b(VI)"));
//	        doc2.put("datetime", new Date());
//
//	        mockDocuments.add(doc1);
//	        mockDocuments.add(doc2);
//
//	        List<DBObject> result = xyService.getFilteredXY(null, null, null, null, null, null, null, null);
//	    	assertThat(result).isNotEmpty();
//	        assertThat(result).containsAll(mockDocuments);
//	    
//	    }

}
