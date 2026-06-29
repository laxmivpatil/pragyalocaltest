package com.techverse.Controller;
 
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import com.techverse.Model.CareerApplication;
import com.techverse.Model.EmailType;
import com.techverse.Repository.CareerApplicationRepository;
import com.techverse.Service.ApiResponseService;
import com.techverse.Service.EmailService1;
import com.techverse.Service.StorageSevice;
 

@RestController
@RequestMapping("/api/career")
@CrossOrigin(origins = "http://localhost:5000", allowCredentials = "true")
public class CareerApplicationController {
	
	 @Autowired
	    private ApiResponseService apiResponseService;

    @Autowired
    private CareerApplicationRepository careerApplicationRepository;
    
    @Autowired
	private EmailService1 emailService1;

    @Autowired
    private StorageSevice storageService;
    
    String schoolEmail = "info@pragyagirlsschool.com";

    @PostMapping("/")
    public ResponseEntity<?> createCareerApplication(
            @RequestPart(value = "fullName", required = false) String fullName,
            @RequestPart(value = "email", required = false) String email,
            @RequestPart(value = "position", required = false) String position,
            @RequestPart(value = "resume", required = false) MultipartFile resume) {

        try {
            if (fullName == null || fullName.trim().isEmpty()) {
                 return apiResponseService.apiResponseService(false, "Full name is required");

            }

            if (email == null || email.trim().isEmpty()) {
                 return apiResponseService.apiResponseService(false, "Email is required");

            }

            if (position == null || position.trim().isEmpty()) {
                 return apiResponseService.apiResponseService(false, "Position is required");

            }

           
/*
            String resumeUrl = "";
            String extension = getExtension(resume);

            String resumeName = "Resume_" + fullName.replaceAll("\\s+", "_") + "_" + Instant.now().toEpochMilli();

            resumeUrl = storageService.uploadFileOnAzure(
                    resume,
                    resumeName + "." + extension
            );

            
            
            
            
         // For email attachment
            byte[] resumeBytes = resume.getBytes();
            CareerApplication application = new CareerApplication(
                    fullName,
                    email,
                    position,
                    resumeUrl
            );
*/
            
         // Read bytes once — synchronously (unavoidable)
            byte[] resumeBytes = resume.getBytes();
            String extension = getExtension(resume);
            String resumeName = "Resume_" + fullName.replaceAll("\\s+", "_") 
                              + "_" + Instant.now().toEpochMilli();

            // Save to DB immediately with empty URL
            CareerApplication application = new CareerApplication(fullName, email, position, "");
            careerApplicationRepository.save(application);

            // Prepare email content (fast — no network call)
            Map<String, Object> variables = new HashMap<>();
            variables.put("fullName", fullName);
            variables.put("email", email);
            variables.put("position", position);

            String schoolBody = emailService1.generateEmailContent("schoolcareerapplication", variables);
            String userBody = emailService1.generateEmailContent("usercareerapplication", variables);

           
            // ✅ Everything heavy runs in background
            CompletableFuture.runAsync(() -> {
                try {
                	
                     
                    // Azure upload
                	 String resumeUrl =	storageService.uploadFileOnAzure(
                             resume, resumeName + "." + extension
                         );
                         application.setResume(resumeUrl);
                         careerApplicationRepository.save(application);

                    // Email to school with resume
                 	System.out.println(resumeName);
               // 	sendEmailResumeAsync(email, "New Career Application - " + position,
                 //       schoolBody, EmailType.CAREER_SCHOOL, resumeUrl, fileNameFromUrl(resumeUrl, resumeName));
                    

                    // Email to user
                //    sendEmailAsync(email, "Your Career Application Has Been Received",
                  //      userBody, EmailType.CAREER_USER);

                } catch (Exception e) {
                    System.err.println("Background task failed: " + e.getMessage());
                }
            });

            // ✅ Respond immediately — no waiting
            return apiResponseService.apiResponseService(true, "Career application saved successfully", application);
      
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong: " + e.getMessage());
        }
    }

    
    
    
    @Async("taskExecutor")
  	public void sendEmailAsync(String to, String subject, String body, EmailType type) {

  		emailService1.sendEmail(to, subject, body,type);
  	}
    @Async("taskExecutor")
  	public void sendEmailResumeAsync(String to, String subject, String body, EmailType type,String resumeUrl,String resumename) {

  		emailService1.sendCareerEmailWithResumeAsync(to, subject, body,type,resumeUrl,resumename);
  	}
    private String getExtension(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();

        if (originalFileName == null || !originalFileName.contains(".")) {
            return "";
        }

        return originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
    }
    
    private String fileNameFromUrl(String url, String defaultName) {
	    if (url == null || url.trim().isEmpty()) {
	        return defaultName;
	    }

	    try {
	        String cleanUrl = url.split("\\?")[0];
	        String fileName = cleanUrl.substring(cleanUrl.lastIndexOf("/") + 1);

	        if (fileName == null || fileName.trim().isEmpty()) {
	            return defaultName;
	        }

	        return fileName;

	    } catch (Exception e) {
	        return defaultName;
	    }
	}
}