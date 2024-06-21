package com.pmu.pmu.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.DBObject;
import com.pmu.pmu.models.User;
import com.pmu.pmu.payload.request.LoginRequest;
import com.pmu.pmu.payload.request.SignupRequest;
import com.pmu.pmu.payload.response.JwtResponse;
import com.pmu.pmu.payload.response.MessageResponse;
import com.pmu.pmu.payload.token.TokenBlacklist;
import com.pmu.pmu.security.jwt.JwtUtils;
import com.pmu.pmu.services.EvidhurService;
import com.pmu.pmu.services.PostService;
import com.pmu.pmu.services.SaptangService;
import com.pmu.pmu.services.UserDetailsImpl;
import com.pmu.pmu.services.UsersService;
import com.pmu.pmu.services.XPostService;
import com.pmu.pmu.services.YPostService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;




@CrossOrigin(
		origins = { "http://10.226.49.255:3000","http://localhost:3001","http://localhost:3000","http://10.226.39.57:3000","http://10.226.48.52:3000","http://10.226.39.57:3001" }, 
		maxAge = 3600, 
		allowCredentials = "true", 
		allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class AuthController {
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private UsersService usersService;
	
	                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             
	@Autowired
	JwtUtils jwtUtils;
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private EvidhurService evidhurService;
	
	@Autowired
	static
	TokenBlacklist tokenBlacklist;
	
	@Autowired
	private XPostService xpostService;
	
	@Autowired
	private YPostService ypostService;
	
	
	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		String jwt = null;
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();	
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());
		try {
			usersService.getUserByUsername(loginRequest.getUsername());
			
			jwt = jwtUtils.generateJwtToken(authentication);
		
			
			
			     
	    }catch (Exception e2) {
	    	
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");	
		}
	System.out.println(jwt);
//		System.out.println(roles);
		return ResponseEntity.ok(new JwtResponse(jwt));
	}
	
	@PostMapping("/signup")
	  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
	    
		if (usersService.isUsernameExists(signUpRequest.getUsername())) {
			return ResponseEntity
	          .badRequest()
	          .body(new MessageResponse("Error: Username is already taken!"));
	    }

	    if (usersService.isEmailExists(signUpRequest.getEmail())) {
	    	return ResponseEntity
	          .badRequest()
	          .body(new MessageResponse("Error: Email is already in use!"));
	    }

	    User user = new User();
	    user.setEmail(signUpRequest.getEmail());
	    user.setUsername(signUpRequest.getUsername());
	    user.setPassword(encoder.encode(signUpRequest.getPassword()));
	    user.setRole(signUpRequest.getRole());
	    usersService.saveUser(user);

	    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	  }
	
		//logout 
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	 @GetMapping("/logout")
	    public static void logout(HttpServletRequest request, HttpServletResponse response) {
	        // Extract JWT token from the Authorization header
	        String authorizationHeader = request.getHeader("Authorization");
	        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
	            // Remove "Bearer " prefix
	            String jwtToken = authorizationHeader.substring(7);
	            
	            // Now you have the JWT token, you can perform further actions like invalidation
	            TokenBlacklist.invalidateToken(jwtToken);
	            
	        }
	    }

	
	
		
		
		//@PreAuthorize("hasRole('ROLE_ADMIN')")
		@GetMapping("/getdata")
		public List<DBObject> getAllDocuments(){
//			System.out.println(postService.getAllDocuments());
		System.out.println("hello working");
			return postService.getAllDocuments();
		}
		
		@GetMapping("/gettrends")
		public List<DBObject> gettrends(){
			System.out.println(postService.getTrends());
			return postService.getTrends();
		}
	
		@PreAuthorize("hasRole('ROLE_ADMIN')")
		@GetMapping("/getdataplatform/{platform}")
		public List<DBObject> getDocumentsByPlatform(@PathVariable String platform){
			System.out.println(platform);
			//System.out.println(postService.getDocumentsByPlatform(platform));
		
			return postService.getDocumentsByPlatform(platform);
		}
		
		
		@PreAuthorize("hasRole('ROLE_ADMIN')")
		@GetMapping("/getCountofPlatform")
		public List<DBObject> getCountofPlatform(){
			return postService.getCountOfDocumentsByPlatform();
		}
		
		
		//@PreAuthorize("hasRole('ROLE_ADMIN')")
		@GetMapping("/getCountofPlatformlatest")
		public List<DBObject> getCountofPlatformlatest( @RequestParam(value = "startDateStr", required = false) String startDateStr,
		        @RequestParam(value = "endDateStr", required = false) String endDateStr){
		
			return postService.getCountOfDocumentsByPlatformlatest(startDateStr,endDateStr);
		}
		
		
		
		@PreAuthorize("hasRole('ROLE_ADMIN')")
		@GetMapping("/getCountBySentiments")
		public List<DBObject> getCountBySentiments(){
			System.out.println(postService.getCountOfDocumentsBySentiment());
			return postService.getCountOfDocumentsBySentiment();
		}
		
		
		@GetMapping("/getCountBySentimentslatest")
		public List<DBObject> getCountBySentimentslatest( @RequestParam(value = "startDateStr", required = false) String startDateStr,
		        @RequestParam(value = "endDateStr", required = false) String endDateStr){
			//System.out.println(postService.getCountOfDocumentsBySentimentlatest());
			return postService.getCountOfDocumentsBySentimentlatest(startDateStr,endDateStr);
		}
		
		
		
		
		@PreAuthorize("hasRole('ROLE_ADMIN')")
		@GetMapping("/getsentiments/{sentiments}")
		public List<DBObject> getBySentiments(@PathVariable String sentiments){
			System.out.println(postService.getbySentiments(sentiments));
			return postService.getbySentiments(sentiments);
		}
		
		@PreAuthorize("hasRole('ROLE_ADMIN')")
		@GetMapping("/getSectionsCounts")
		public Map<String, Integer> getSectionsCounts(){
			System.out.println(postService.getAllSections());
			postService.getAllSections();
			return postService.getAllSections();
		}
		//@PreAuthorize("hasRole('ROLE_ADMIN')")
		@GetMapping("/getSectionsCountslatest")
		public Map<String, Integer> getSectionsCountslatest( @RequestParam(value = "startDateStr", required = false) String startDateStr,
		        @RequestParam(value = "endDateStr", required = false) String endDateStr){
			System.out.println(postService.getAllSectionslatest(startDateStr,endDateStr));
		
			return postService.getAllSectionslatest(startDateStr,endDateStr);
		}
		
		
		
		
		
		
		
		
		
		@PreAuthorize("hasRole('ROLE_ADMIN')")
		@GetMapping("/getbySection/{section}")
		public List<DBObject> getbySections(@PathVariable String section){
			System.out.println(postService.getBySection(section));
			return postService.getBySection(section);
		}
		
		@PreAuthorize("hasRole('ROLE_ADMIN')")
		@GetMapping("/getplatformfilter/{platform}")
		public List<DBObject> getDocumentsByPlatformfilter(@PathVariable String platform){
			System.out.println(postService.getDocumentsByPlatformfilter(platform));
		
			return postService.getDocumentsByPlatformfilter(platform);
		}
		
		@PreAuthorize("hasRole('ROLE_ADMIN')")
		@GetMapping("/getHashtags")
		public List<Map.Entry<String, Integer>> Hashtags(){
			System.out.println(postService.getTrendingHashtags());			
			return postService.getTrendingHashtags();
		}
		@PreAuthorize("hasRole('ROLE_ADMIN')")
		@GetMapping("/getTrendingHashtags")
		public List<Map.Entry<String, Integer>> TrendingHashtags(){
			List<Map.Entry<String, Integer>> newdata=new ArrayList<>();
			newdata=postService.getTrendingHashtags();
			
			  List<Map.Entry<String, Integer>> filteredData = newdata.stream()
		                .filter(entry -> entry.getValue() > 10)
		                .collect(Collectors.toList());
			  System.out.println("Filtered Data:");
		        for (Map.Entry<String, Integer> entry : filteredData) {
		            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
		        }
			
			System.out.println(postService.getTrendingHashtags());			
			return filteredData;
		}
		
	//	@PreAuthorize("hasRole('ROLE_ADMIN')")
		@GetMapping("/getTrendingHashtagslatest")
		public List<Map.Entry<String, Integer>> TrendingHashtagslatest( @RequestParam(value = "startDateStr", required = false) String startDateStr,
		        @RequestParam(value = "endDateStr", required = false) String endDateStr){
			List<Map.Entry<String, Integer>> newdata=new ArrayList<>();	
			
			newdata=postService.getTrendingHashtagsLatest(startDateStr,endDateStr);
			System.out.println("hey"+newdata);
			List<Map.Entry<String, Integer>> filteredData = newdata.stream()
	                .filter(entry -> entry.getValue() > 1)
	                .collect(Collectors.toList());
		  System.out.println("Filtered Data:");
	        for (Map.Entry<String, Integer> entry : filteredData) {
	            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
	        }
	        System.out.println(filteredData);
//		System.out.println(postService.getTrendingHashtags());			
		return filteredData;
		}
		
		
		
		
		
		
		
		@PreAuthorize("hasRole('ROLE_ADMIN')")
		@GetMapping("/getmultipleplatform")
		public List<DBObject> getMultiplePlatformfilter( @RequestParam List<String> platforms ) {//change it when the request parameter is coming from frontend
//			List<String> p1=new ArrayList<>();
//			p1.add("FACEBOOK");
//			p1.add("TWITTER");
			System.out.println(platforms);
//		    System.out.println(postService.getMultiplePlatformfilter(platforms));
		    return postService.getMultiplePlatformfilter(platforms);
		}
		
		
		
		
		
		
		
		
		
		@PreAuthorize("hasRole('ROLE_ADMIN')")
		@GetMapping("/getmultiplesentiments")
		public List<DBObject> getMultipleSentiment(/* @RequestParam List<String> sentiments */) {//change it when the request parameter is coming from frontend
			List<String> p1=new ArrayList<>();
			p1.add("positive");
			p1.add("neutral");
			System.out.println(p1.toString());
		    System.out.println(postService.getMultipleSentiments(p1));
		    return postService.getMultipleSentiments(p1);
		}

		@PreAuthorize("hasRole('ROLE_ADMIN')")
		@GetMapping("/getmultiplesections")
		public List<DBObject> getMultiplesections(/* @RequestParam List<String> sections */) {//change it when the request parameter is coming from frontend
			List<String> p1=new ArrayList<>();
			//p1.add("Section 31b1");
			p1.add("Section 31b9");
			System.out.println(p1.toString());
		    System.out.println(postService.getMultipleSections(p1));
		    return postService.getMultipleSections(p1);
		}
	   	
		
		//@PreAuthorize("hasRole('ROLE_ADMIN')")
		@GetMapping("/getfromdate")
		public List<DBObject> getfromdate(){
		
//			postService.getFromToDate("2024-05-22T00:00:00Z", "2024-06-02T00:00:00Z");
		return	postService.getFromToDate("2024-05-22T00:00:00Z", "2024-06-02T00:00:00Z");
			
		//	return postService.getFromToDate("22-05-2024 00:00:00","02-06-2024 00:00:00");
		}

		@PreAuthorize("hasRole('ROLE_ADMIN')")
		@GetMapping("/getCountofLegality")
		public List<DBObject> getCountofLegality(){
			return postService.getCountByLaw();
		}
		
		//@PreAuthorize("hasRole('ROLE_ADMIN')")
		@GetMapping("/filter")
		public List<DBObject> filterPosts(
		        @RequestParam(value = "platforms", required = false) List<String> platforms,
		        @RequestParam(value = "sections", required = false) List<String> sections,
		        @RequestParam(value = "sentiments", required = false) List<String> sentiments,
		        @RequestParam(value = "languages", required = false) List<String> languages
		       , @RequestParam(value = "startDateStr", required = false) String startDateStr,
		        @RequestParam(value = "endDateStr", required = false) String endDateStr,
		        @RequestParam(value = "intensity", required = false) List<String> intensity
		        
		    ) {		

		
		    
		    // Initialize languages list if null
		    if(languages == null) {
		        languages = new ArrayList<>();
		        languages.add("kannad");
		        languages.add("telugu");
		        languages.add("bengali");
		        languages.add("english");
		        languages.add("hindi");
		        languages.add("romanian hindi");
		        languages.add("urdu");
		        languages.add("tamil");
		        languages.add("malyalam");
		        languages.add("punjabi");
		    }
		    
		    // Initialize sections list if null
		   
		    return postService.getFilteredPosts(platforms, sections, sentiments, languages,startDateStr,endDateStr,intensity);
		}
	
		@PreAuthorize("hasRole('ROLE_ADMIN')")
		@GetMapping("/getdata2")
		public List<DBObject> getAllDocuments2(){
//			System.out.println(postService.getAllDocuments());
		System.out.println("hello working");
			return evidhurService.getAllDocuments2();
		}
		

		@PreAuthorize("hasRole('ROLE_ADMIN')")
		@GetMapping("/getIntensity")
		public List<DBObject> getMultiplePlatformfilter(  ) {//change it when the request parameter is coming from frontend

		    return postService.getIntensity();
		}
		
		
		// Saptang API
		@Autowired
		private SaptangService spatangService;
	
		
		@GetMapping("saptangdata")
		public List<DBObject> saptangData(){
			return spatangService.getAllData();
		}
		
		
		@GetMapping("/filter2")
		public List<DBObject> filterPosts2(
		        @RequestParam(value = "platforms", required = false) List<String> platforms,
		        @RequestParam(value = "sections", required = false) List<String> sections,
		        @RequestParam(value = "sentiments", required = false) List<String> sentiments,
		        @RequestParam(value = "languages", required = false) List<String> languages
		       , @RequestParam(value = "startDateStr", required = false) String startDateStr,
		        @RequestParam(value = "endDateStr", required = false) String endDateStr,
		        @RequestParam(value = "intensity", required = false) List<String> intensity
		        
		    ){
			 return spatangService.getFilteredPosts(platforms, sections, sentiments, languages,startDateStr,endDateStr,intensity);
			
		}
		
		@GetMapping("/filter3")
		public List<DBObject> filterPosts3(
		        @RequestParam(value = "platforms", required = false) List<String> platforms,
		        @RequestParam(value = "sections", required = false) List<String> sections,
		        @RequestParam(value = "sentiments", required = false) List<String> sentiments,
		        @RequestParam(value = "languages", required = false) List<String> languages
		       , @RequestParam(value = "startDateStr", required = false) String startDateStr,
		        @RequestParam(value = "endDateStr", required = false) String endDateStr,
		        @RequestParam(value = "intensity", required = false) List<String> intensity
		        
		    ){
			 return evidhurService.getFilteredPosts(platforms, sections, sentiments, languages,startDateStr,endDateStr,intensity);
			
		}
		@GetMapping("/filter4")
		public List<DBObject> filterXpost(
				@RequestParam(value = "platforms", required = false) List<String> platforms,
		        @RequestParam(value = "sections", required = false) List<String> sections,
		        @RequestParam(value = "sentiments", required = false) List<String> sentiments,
		        @RequestParam(value = "languages", required = false) List<String> languages
		       , @RequestParam(value = "startDateStr", required = false) String startDateStr,
		        @RequestParam(value = "endDateStr", required = false) String endDateStr,
		        @RequestParam(value = "intensity", required = false) List<String> intensity,
		        @RequestParam(value = "ctype", required = false) List<String> ctype){
			
			return xpostService.getFilteredPosts(platforms, sections, sentiments, languages,startDateStr,endDateStr,intensity,ctype);
			
		}
		
		
		@GetMapping("/filter5")
		public List<DBObject> filterYpost(
				@RequestParam(value = "platforms", required = false) List<String> platforms,
		        @RequestParam(value = "sections", required = false) List<String> sections,
		        @RequestParam(value = "sentiments", required = false) List<String> sentiments,
		        @RequestParam(value = "languages", required = false) List<String> languages
		       , @RequestParam(value = "startDateStr", required = false) String startDateStr,
		        @RequestParam(value = "endDateStr", required = false) String endDateStr,
		        @RequestParam(value = "intensity", required = false) List<String> intensity,
		        @RequestParam(value = "ctype", required = false) List<String> ctype){
			
			return ypostService.getFilteredPosts(platforms, sections, sentiments, languages,startDateStr,endDateStr,intensity,ctype);
			
		}
		
		
		
		@GetMapping("/sentimentsfilter")
		public List<DBObject> senfilter()
		{
			ArrayList<String> newarr=new ArrayList<>();
			newarr.add("negative");
//			newarr.add("neutral");
			System.out.println(postService.getsenfilter(newarr));
			
			return postService.getsenfilter(newarr);
			
		}
		
		
		
}





