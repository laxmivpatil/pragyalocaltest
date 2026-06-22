package com.techverse.Controller;
 
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import com.techverse.Model.CareerApplication;
import com.techverse.Repository.CareerApplicationRepository;
import com.techverse.Service.EmailService1;
import com.techverse.Service.StorageSevice;
 

@RestController
@RequestMapping("/api/career")
@CrossOrigin(origins = "http://localhost:5000", allowCredentials = "true")
public class CareerApplicationController {

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
                return ResponseEntity.badRequest().body("Full name is required");
            }

            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Email is required");
            }

            if (position == null || position.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Position is required");
            }

            if (resume == null || resume.isEmpty()) {
                return ResponseEntity.badRequest().body("Resume file is required");
            }

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

            careerApplicationRepository.save(application);

            Map<String, Object> variables = new HashMap<>();

            variables.put("fullName", fullName);
            variables.put("email", email);
            variables.put("position", position);

            String schoolBody = emailService1.generateEmailContent("schoolcareerapplication", variables);
            String userBody = emailService1.generateEmailContent("usercareerapplication", variables);
           //email to school for new application  change email to school email
            emailService1.sendCareerEmailWithResumeAsync(
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
                    userBody
            );
            return new ResponseEntity<>(application, HttpStatus.OK);

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