package com.techverse.Service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ApiResponseService {
	
	public ResponseEntity<?> apiResponseService(boolean status, String message) {
	    Map<String, Object> response = new HashMap<>();
	    response.put("status", status);
	    response.put("message", message);
	    return ResponseEntity.ok(response);
	}
public ResponseEntity<?> apiResponseService(boolean status, String message, Object data) {
	    Map<String, Object> response = new HashMap<>();
	    response.put("status", status);
	    response.put("message", message);
	    response.put("data", data);
	    return ResponseEntity.ok(response);
	}

}
