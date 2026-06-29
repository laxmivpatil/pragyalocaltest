package com.techverse.Controller;
 
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

            if (resume == null || resume.isEmpty()) {
                return apiResponseService.apiResponseService(false, "Resume file is required");

          
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
            
            String extension = getExtension(resume);
            String resumeName = "Resume_" + fullName.replaceAll("\\s+", "_") + "_" + Instant.now().toEpochMilli();

            // ✅ Save application first with placeholder URL
            CareerApplication application = new CareerApplication(fullName, email, position, "");
            careerApplicationRepository.save(application);

            // ✅ Upload async — don't block the response
            byte[] resumeBytes = resume.getBytes(); // Read once before async
            CompletableFuture.runAsync(() -> {
                try {
                    String resumeUrl = storageService.uploadFileOnAzure(resume, resumeName + "." + extension);
                    // Update URL after upload
                    application.setResume(resumeUrl);
                    careerApplicationRepository.save(application);
                } catch (Exception e) {
                    System.err.println("Async upload failed: " + e.getMessage());
                }
            });
 
            Map<String, Object> variables = new HashMap<>();

            variables.put("fullName", fullName);
            variables.put("email", email);
            variables.put("position", position);

            String schoolBody = emailService1.generateEmailContent("schoolcareerapplication", variables);
            String userBody = emailService1.generateEmailContent("usercareerapplication", variables);
           //email to school for new application  change email to school email
          /*  emailService1.sendCareerEmailWithResumeAsync(
                    email,
                    "New Career Application - " + position,
                    schoolBody,
                    resumeBytes,
                    resumeName
            );
            //email to person
            emailService1.sendEmail(
                    email,
                    "Your Career Application Has Been Received",
                    userBody, EmailType.CAREER_USER
            );
            
            */
          return  apiResponseService.apiResponseService(true, "Career application Saved successfully",application);
      
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong: " + e.getMessage());
        }
    }

    
    
    private String getExtension(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();

        if (originalFileName == null || !originalFileName.contains(".")) {
            return "";
        }

        return originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
    }
}