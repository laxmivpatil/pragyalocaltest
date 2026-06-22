package com.techverse.Controller;

 
import java.security.SecureRandom;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.techverse.Model.AdmissionEnquiry;
import com.techverse.Repository.AdmissionEnquiryRepository;
import com.techverse.Service.ApiResponseService;
import com.techverse.Service.EmailService1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/enquiry")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AdmissionEnquiryController {

    @Autowired
    private AdmissionEnquiryRepository admissionEnquiryRepository;

    @Autowired
    private ApiResponseService apiResponseService;

    
    @Autowired
    private EmailService1 emailService1;

    private static final SecureRandom secureRandom = new SecureRandom();

    private final Map<String, OtpData> otpStore = new ConcurrentHashMap<>();

    private static class OtpData {
        private String otp;
        private Instant expiryTime;
        private boolean verified;

        public OtpData(String otp, Instant expiryTime, boolean verified) {
            this.otp = otp;
            this.expiryTime = expiryTime;
            this.verified = verified;
        }
    }

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> request) {

        String email = request.get("email");

        if (isBlank(email)) {
            return apiResponseService.apiResponseService(false, "Email required");
        }

        email = email.trim().toLowerCase();

        String otp = String.valueOf(1000 + secureRandom.nextInt(9000));
        System.out.println(otp);
        otpStore.put(
                email,
                new OtpData(otp, Instant.now().plusSeconds(300), false)
        );

        String body = "<h2>Pragya Girls School Enquiry OTP</h2>"
                + "<p>Your OTP for admission enquiry verification is:</p>"
                + "<h1 style='letter-spacing:3px'>" + otp + "</h1>"
                + "<p>This OTP is valid for 5 minutes.</p>";
        System.out.println(body);
/*
        emailService1.sendEmail(
                email,
                "Your OTP for Admission Enquiry",
                body
        );
*/
        return apiResponseService.apiResponseService(true, "OTP has been sent to your email address");
        
        
    }

    

	@PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> request) {

        String email = request.get("email");
        String otp = request.get("otp");

        if (isBlank(email)) {
            return apiResponseService.apiResponseService(false, "Email required");
        }

        if (isBlank(otp)) {
            return apiResponseService.apiResponseService(false, "OTP required");
        }

        email = email.trim().toLowerCase();

        OtpData otpData = otpStore.get(email);

        if (otpData == null) {
            return apiResponseService.apiResponseService(false, "OTP not found. Please send OTP again.");

         }

        if (Instant.now().isAfter(otpData.expiryTime)) {
            otpStore.remove(email);
            return apiResponseService.apiResponseService(false, "OTP has expired. Please send OTP again.");

         }

        if (!otpData.otp.equals(otp.trim())) {
            return apiResponseService.apiResponseService(false, "OTP is incorrect");

         }

        otpData.verified = true;
        otpStore.put(email, otpData);
        return apiResponseService.apiResponseService(true, "OTP verified successfully");

     }

    @PostMapping("/")
    public ResponseEntity<?> createEnquiry(@RequestBody Map<String, String> request) {

        String boardingType = request.get("boardingType");
        String studentFullName = request.get("studentFullName");
        String studentDateOfBirth = request.get("studentDateOfBirth");
        String gender = request.get("gender");
        String gradeApplyingFor = request.get("gradeApplyingFor");

        String parentGuardianFullName = request.get("parentGuardianFullName");
        String relationshipToStudent = request.get("relationshipToStudent");
        String contactNumber = request.get("contactNumber");
        String city = request.get("city");
        String email = request.get("email");
        String comments = request.get("comments");

        if (isBlank(boardingType)) {
        	
            return apiResponseService.apiResponseService(true, "Boarding type is required");

             
        }

        if (isBlank(studentFullName)) {
            return apiResponseService.apiResponseService(true, "Student full name is required");

         }

        if (isBlank(studentDateOfBirth)) {
            return apiResponseService.apiResponseService(true, "Student date of birth is required");

         }

        if (isBlank(gender)) {
            return apiResponseService.apiResponseService(true, "Gender is required");

         }

        if (isBlank(gradeApplyingFor)) {
            return apiResponseService.apiResponseService(true, "Grade applying for is required");

         }

        if (isBlank(parentGuardianFullName)) {
            return apiResponseService.apiResponseService(true, "Parent/Guardian full name is required");

         }

        if (isBlank(relationshipToStudent)) {
            return apiResponseService.apiResponseService(true, "Relationship to student is required");

         }

        if (isBlank(contactNumber)) {
            return apiResponseService.apiResponseService(true, "Contact number is required");

         }

        if (isBlank(city)) {
            return apiResponseService.apiResponseService(true, "City is required");

         }

        if (isBlank(email)) {
            return apiResponseService.apiResponseService(true, "Email is required");

         }

        email = email.trim().toLowerCase();

        OtpData otpData = otpStore.get(email);

        if (otpData == null || !otpData.verified) {
            return apiResponseService.apiResponseService(true, "Please verify OTP before submitting enquiry");

         }

        AdmissionEnquiry enquiry = new AdmissionEnquiry(
                boardingType,
                studentFullName,
                studentDateOfBirth,
                gender,
                gradeApplyingFor,
                parentGuardianFullName,
                relationshipToStudent,
                contactNumber,
                city,
                email,
                comments,
                true,
                "RECEIVED"
        );

        admissionEnquiryRepository.save(enquiry);

        otpStore.remove(email);

        Map<String, Object> variables = new HashMap<>();
        variables.put("boardingType", boardingType);
        variables.put("studentFullName", studentFullName);
        variables.put("studentDateOfBirth", studentDateOfBirth);
        variables.put("gender", gender);
        variables.put("gradeApplyingFor", gradeApplyingFor);
        variables.put("parentGuardianFullName", parentGuardianFullName);
        variables.put("relationshipToStudent", relationshipToStudent);
        variables.put("contactNumber", contactNumber);
        variables.put("city", city);
        variables.put("email", email);
        variables.put("comments", comments);

       // String schoolBody = emailService1.generateEmailContent("schooladmissionenquiry", variables);
        //String userBody = emailService1.generateEmailContent("useradmissionenquiry", variables);

     /*   emailService1.sendEmail(
                "info@pragyagirlsschool.com",
                "New Admission Enquiry - " + studentFullName,
                schoolBody
        );

        emailService1.sendEmail(
                email,
                "Your Admission Enquiry Has Been Received",
                userBody
        );
        
        */
        return apiResponseService.apiResponseService(true, "Enquiry Saved successfully",enquiry);

         
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}