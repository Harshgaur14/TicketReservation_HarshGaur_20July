package com.pmu.pmu.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(
		origins = { "http://10.226.49.255:3000","http://localhost:3001","http://localhost:3000","http://10.226.39.57:3000","http://10.226.48.52:3000","http://10.226.39.57:3001" }, 
		maxAge = 3600, 
		allowCredentials = "true", 
		allowedHeaders = "*")
@RestController
@RequestMapping("/user")
public class UserController {

}
