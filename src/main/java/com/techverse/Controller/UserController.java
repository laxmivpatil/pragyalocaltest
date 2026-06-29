package com.techverse.Controller;
 
import java.util.HashMap;
import java.util.List;
import java.util.Map; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody; 
import org.springframework.web.bind.annotation.RequestParam; 
import org.springframework.web.bind.annotation.RestController;

import com.techverse.Model.EmailType;
import com.techverse.Model.UserForm;
import com.techverse.Repository.UserFormRepository;
import com.techverse.Response.ApiResponse;
 
import com.techverse.Service.EmailService1;
 
import com.techverse.Service.UserService;

@RestController 
public class UserController {
	String schoolEmail="contactus@pragyagirlsschool.com";
	 @Autowired
	    private UserFormRepository userFormRepository;
	 
	@Autowired
	private EmailService1 emailService1;
	@Autowired
	private UserService userService;
	 
	
	 
	 
 
    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse> getAllUserForms() {
        List<UserForm> userForms = userService.getAllUserForms();
        ApiResponse apiResponse=new ApiResponse(userForms);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    
    
    /*
 
    @PostMapping("/create")
    public ResponseEntity<UserForm> createUserForm(@RequestBody UserForm userForm) {
        UserForm createdUserForm = userService.createUserForm(userForm);
        String schoolSubject = "Contact Us Request";
        
        StringBuilder schoolBodyBuilder = new StringBuilder();
        schoolBodyBuilder.append("<html><body>")
            .append("<p>Dear Pragya School Admissions Committee,</p>")
            .append("<p>A new message has been received through the contact form:</p>")
            .append("<ul>")
            .append("<li>Full Name: ").append(userForm.getFullName()).append("</li>")
            .append("<li>Email: ").append(userForm.getEmail()).append("</li>")
            .append("<li>Phone Number: ").append(userForm.getPhoneNumber()).append("</li>")
            .append("<li>Subject: ").append(userForm.getSubject()).append("</li>")
            .append("<li>Message: ").append(userForm.getMessage()).append("</li>")
          //  .append("<li>Message:</li>")
            .append("<p>").append(userForm.getMessage()).append("</p>");
        
        // Add city and state if provided
        if (!userForm.getCity().isEmpty()) {
            schoolBodyBuilder.append("<li>City: ").append(userForm.getCity()).append("</li>");
        }
        if (!userForm.getState().isEmpty()) {
            schoolBodyBuilder.append("<li>State: ").append(userForm.getState()).append("</li>");
        }
        
        schoolBodyBuilder.append("</ul>")
            .append("<p>Please review and respond accordingly.</p>")
            .append("<p>Thank you.</p>")
            .append("</body></html>");
        
        String schoolBody = schoolBodyBuilder.toString();

        String userSubject = "Your Message to Pragya School";
        
        StringBuilder userBodyBuilder = new StringBuilder();
        userBodyBuilder.append("<html><body>")
            .append("<p>Dear ").append(userForm.getFullName()).append(",</p>")
            .append("<p>Thank you for contacting Pragya School. We have received your message and will get back to you soon.</p>")
            .append("<p>Here are the details of your query:</p>")
            .append("<ul>")
            .append("<li>Subject: ").append(userForm.getSubject()).append("</li>")
            .append("<li>Message: ").append(userForm.getMessage()).append("</li>")
          //  .append("<li>Message:</li>")
            .append("<p>").append(userForm.getMessage()).append("</p>");
        
        // Add city and state if provided
        if (!userForm.getCity().isEmpty()) {
            userBodyBuilder.append("<li>City: ").append(userForm.getCity()).append("</li>");
        }
        if (!userForm.getState().isEmpty()) {
            userBodyBuilder.append("<li>State: ").append(userForm.getState()).append("</li>");
        }
        
        userBodyBuilder.append("</ul>")
            .append("<p>Best regards,<br/>Pragya School Admissions Team</p>")
            .append("</body></html>");
        
        String userBody = userBodyBuilder.toString();

      //  emailService1.sendEmail(schoolEmail, schoolSubject, schoolBody);
        //  emailService1.sendEmail(userForm.getEmail(), userSubject, userBody);
        sendEmailAsync(schoolEmail, schoolSubject, schoolBody);
        sendEmailAsync(userForm.getEmail(), userSubject, userBody);
        return new ResponseEntity<>(createdUserForm, HttpStatus.OK);
    }
    
    */
    @PostMapping("/create")
    public ResponseEntity<Void> createUser(@RequestBody UserForm userForm) {
        // Save user data to the database
    	   userFormRepository.save(userForm);

        // Prepare variables for the email templates
        Map<String, Object> variables = new HashMap<>();
        variables.put("fullName", userForm.getFullName());
        variables.put("email", userForm.getEmail());
        variables.put("phoneNumber", userForm.getPhoneNumber());
        variables.put("subject", userForm.getSubject());
        variables.put("message", userForm.getMessage());
        variables.put("city", userForm.getCity());
        variables.put("state", userForm.getState());

        // Generate email content using Thymeleaf
        String schoolBody = emailService1.generateEmailContent("schoolEmailTemplate", variables);
        String userBody = emailService1.generateEmailContent("userEmailTemplate", variables);

        // Send emails asynchronously
        sendEmailAsync(schoolEmail, "Contact Us Request", schoolBody,EmailType.CONTACT_SCHOOL);
        sendEmailAsync(userForm.getEmail(), "Your Message to Pragya School", userBody,EmailType.CONTACT_PARENT);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /*
    @GetMapping("/create")
    public ResponseEntity<Void> createUser(
        @RequestParam String fullName,
        @RequestParam String email,
        @RequestParam String phoneNumber,
        @RequestParam String subject,
        @RequestParam String message,
        @RequestParam(required = false) String city,
        @RequestParam(required = false) String state) {
    	  // Construct the UserForm object if needed by your service layer
        UserForm userForm = new UserForm(fullName, email, phoneNumber, subject, message, city, state);
        //userService.createUserForm(userForm);
        userFormRepository.save(userForm);
        // Prepare variables for the email templates
        Map<String, Object> variables = new HashMap<>();
        variables.put("fullName", fullName);
        variables.put("email", email);
        variables.put("phoneNumber", phoneNumber);
        variables.put("subject", subject);
        variables.put("message", message);
        variables.put("city", city);
        variables.put("state", state);
         
        
        // Generate email content using Thymeleaf
        String schoolBody = emailService1.generateEmailContent("schoolEmailTemplate", variables);
        String userBody = emailService1.generateEmailContent("userEmailTemplate", variables);
        ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.OK);

        // Send emails asynchronously
      sendEmailAsync(schoolEmail, "Contact Us Request", schoolBody,EmailType.CONTACT_SCHOOL);
        sendEmailAsync(email, "Your Message to Pragya School", userBody,EmailType.CONTACT_PARENT);
       
        return response;
    }
*/
    @Async("taskExecutor")
    public void sendEmailAsync(String to, String subject, String body,EmailType type) {
    	 
        
        emailService1.sendEmail(to, subject, body,type);
    }

    
}
