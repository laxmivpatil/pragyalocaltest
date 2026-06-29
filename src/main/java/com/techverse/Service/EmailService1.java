 

package com.techverse.Service;

 
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
 
import java.util.Map;
 
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource; 
import javax.annotation.PostConstruct;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service; 
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.techverse.Model.EmailType;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
 

@Service
public class EmailService1 {
	  private static final String DIGITS = "0123456789";
	    private static final SecureRandom random = new SecureRandom();
	    private Session session;

	    @Autowired
		private StorageSevice storageService;
	    @Value("${spring.mail.host}")
	    private String host;

	    @Value("${spring.mail.port}")
	    private String port;

	    @Value("${spring.mail.username}")
	    private String senderEmail;

	    @Value("${spring.mail.password}")
	    private String senderPassword;

     
     @Autowired
     private JavaMailSender emailSender;

     @Autowired
     private SpringTemplateEngine templateEngine;

     public String generateEmailContent(String templateName, Map<String, Object> variables) {
         Context context = new Context();
         context.setVariables(variables);
         return templateEngine.process(templateName, context);
     }
     
     
     
   
//use for general admission form
     /**
      * Sends an email asynchronously using JavaMailSender.
      * @param recipientEmail the recipient's email address
      * @param emailSubject the subject of the email
      * @param emailBody the body of the email (HTML content)
      * @return CompletableFuture indicating success (true) or failure (false)
      */
     @Async
     public CompletableFuture<Boolean> sendEmail(
             String recipientEmail,
             String emailSubject,
             String emailBody,
             EmailType emailType
     ) {
         try {
             System.out.println(recipientEmail);

             MimeMessage message = emailSender.createMimeMessage();

             MimeMessageHelper helper = new MimeMessageHelper(
                     message,
                     MimeMessageHelper.MULTIPART_MODE_RELATED,
                     "UTF-8"
             );

             helper.setFrom("info@pragyagirlsschool.com");
             helper.setTo(recipientEmail);
             helper.setSubject(emailSubject);
             helper.setText(emailBody, true);

             addInlineImages(helper, emailType);

             emailSender.send(message);

             System.out.println("Email sent successfully");
             return CompletableFuture.completedFuture(true);

         } catch (Exception e) {
             e.printStackTrace();
             return CompletableFuture.completedFuture(false);
         }
     }     
     
     
     
     
     private void addInlineImages(MimeMessageHelper helper, EmailType emailType) throws MessagingException {

    	 ClassPathResource logoImage = new ClassPathResource("static/images/logo.png"); 
 	    ClassPathResource fbImage = new ClassPathResource("static/images/fb.png"); 
 	    ClassPathResource instaImage = new ClassPathResource("static/images/insta.png"); 
 	    ClassPathResource thankyouImage = new ClassPathResource("static/images/thankyou.png"); 
 	    ClassPathResource heroImage = new ClassPathResource("static/images/hero-img.webp"); 
 	    ClassPathResource phoneImage = new ClassPathResource("static/images/phone.webp"); 
 	    ClassPathResource emailImage = new ClassPathResource("static/images/email.webp"); 
 	    ClassPathResource otpHeroImage = new ClassPathResource("static/images/otp-email-hero.webp");
 	    

    	    switch (emailType) {

    	    
    	 
    	   
    	        case OTP:  	            
    	            helper.addInline("logoImage", logoImage);
    	            helper.addInline("otpheroImage", otpHeroImage);
    	            helper.addInline("fbImage", fbImage);
    	            helper.addInline("instaImage", instaImage);
    	   
    	            break;

    	        case THANK_YOU:
    	            
    	            break;

    	        case ADMISSION_ENQUIRY_ADMIN:
    	        case ADMISSION_ENQUIRY_PARENT:
    	        	helper.addInline("logoImage", logoImage);
    	        	helper.addInline("heroImage", heroImage);
      	            helper.addInline("phoneImage", phoneImage);
      	            helper.addInline("emailImage", emailImage);
    	            break;
    	        case  ADMISSION_ENQUIRY_SCHOOL:
    	        	helper.addInline("logoImage", logoImage);    
      	            helper.addInline("phoneImage", phoneImage);
      	            helper.addInline("emailImage", emailImage); 
    	        	break;
    	        

    	        default:
    	            break;
    	    }
    	}
     
     
     
     
     
     
     
     
     
     
     
     
      
     //old use
     public boolean sendEmail4(String recipientEmail, String emailSubject, String emailBody) {
         try {
             // Create a message
             Message message = new MimeMessage(session);
             message.setFrom(new InternetAddress(senderEmail)); // Correctly setting the sender email
             message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
             message.setSubject(emailSubject);
             message.setContent(emailBody, "text/html");


             // Use Transport object for better control
             Transport transport = session.getTransport("smtp");
             transport.connect();
             transport.sendMessage(message, message.getAllRecipients());
             transport.close();

             
             return true;
         } catch (MessagingException e) {
             System.err.println("Error sending email: " + e.getMessage());
             return false;
         }
     }
     
     
     
    
	/*
	public boolean sendEmail(String recipientEmail,String emailSubject, String emailBody)
	{
		Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.trust", "mail.pragyagirlsschool.com");
        //props.put("mail.debug", "true");
        // Create a mail session with the specified properties
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Create a message
        	
        	
        	
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(emailSubject);
            message.setContent(emailBody, "text/html");
            // message.setContent();

            // Send the message
            Transport.send(message);

            System.out.println("Email sent successfully.");
            return true;

        } catch (MessagingException e) {
            System.out.println("Error sending email: " + e.getMessage());
            return false;
        }
    }
	 
	*/
	
     
     
	public boolean sendEmail1(String recipientEmail, String emailSubject, String emailBody, String imagePath) {
	    Properties props = new Properties();
	    props.put("mail.smtp.host", host);
	    props.put("mail.smtp.port", port);
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");

	    // Create a mail session with the specified properties
	    Session session = Session.getInstance(props, new Authenticator() {
	        protected PasswordAuthentication getPasswordAuthentication() {
	            return new PasswordAuthentication(senderEmail, senderPassword);
	        }
	    });

	    try {
	        // Create a MimeMessage
	        Message message = new MimeMessage(session);
	        message.setFrom(new InternetAddress(senderEmail));
	        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
	        message.setSubject(emailSubject);

	        // Create a multipart message for email content (text + image)
	        Multipart multipart = new MimeMultipart();

	        // Part one is the HTML text
	        BodyPart messageBodyPart = new MimeBodyPart();
	        String htmlText = emailBody + "<br><img src='cid:image'>";
	        messageBodyPart.setContent(htmlText, "text/html");
	        multipart.addBodyPart(messageBodyPart);

	        // Part two is the image
	        messageBodyPart = new MimeBodyPart();
	        DataSource fds = new FileDataSource(imagePath);
	        messageBodyPart.setDataHandler(new DataHandler(fds));
	        messageBodyPart.setHeader("Content-ID", "<image>");
	        multipart.addBodyPart(messageBodyPart);

	        // Set the multipart message to the email message
	        message.setContent(multipart);

	        // Send the message
	        Transport.send(message);

	        System.out.println("Email sent successfully with embedded image.");
	        return true;

	    } catch (MessagingException e) {
	        System.out.println("Error sending email: " + e.getMessage());
	        return false;
	    }
	}

	
	
	//use for advance admission form section
	@Async
	public CompletableFuture<Boolean> sendEmailWithAttachment(String recipientEmail, String emailSubject, String emailBody,
	                                                          String birthCertificate, String birthCertificatefile,String lastResult,String lastResultfile,
	                                          	    		String parentAadhar,String parentAadharfile,
	                                        	    		String studentAadhar,String studentAadharfile,
	                                        	    		String bankDoc, String bankDocfile,
	                                        	    		String cast,String castfile,
	                                        	    		String transferCertificate, String transferCertificatefile, 
	                                        	    		String profile,String profilefile,
	                                        	    		String sssmid,String sssmidfile) {
	    try {
	        // Create a MimeMessage
	        MimeMessage message = emailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message, true);
	         
	        // Set email details
	        helper.setFrom("info@pragyagirlsschool.com");
	        helper.setTo(recipientEmail);
	        helper.setSubject(emailSubject); 
	        helper.setText(emailBody, true);  // HTML content
	         
	          helper.addAttachment(birthCertificatefile,storageService.downloadFileFromAzure(birthCertificate, birthCertificatefile));
	          helper.addAttachment(lastResultfile, storageService.downloadFileFromAzure(lastResult, lastResultfile));
	          helper.addAttachment(parentAadharfile, storageService.downloadFileFromAzure(parentAadhar, parentAadharfile));
	          helper.addAttachment(studentAadharfile, storageService.downloadFileFromAzure(studentAadhar, studentAadharfile));
	          helper.addAttachment(bankDocfile, storageService.downloadFileFromAzure(bankDoc, bankDocfile));
	          helper.addAttachment(castfile, storageService.downloadFileFromAzure(cast, castfile));
	          helper.addAttachment(transferCertificatefile, storageService.downloadFileFromAzure(transferCertificate, transferCertificatefile));
	          helper.addAttachment(profilefile, storageService.downloadFileFromAzure(profile, profilefile));
	          helper.addAttachment(sssmidfile, storageService.downloadFileFromAzure(sssmid, sssmidfile));
	          
	        
	        
	        // Send the email
	        emailSender.send(message);

	          
	        return CompletableFuture.completedFuture(true);  // Successfully sent
	    } catch (Exception e) {
	        e.printStackTrace();
	        return CompletableFuture.completedFuture(false);  // Failure
	    }
	}

	 
	 public  String generateOTP(int length) {
	        StringBuilder otp = new StringBuilder(length);
	        for (int i = 0; i < length; i++) {
	            int index = random.nextInt(DIGITS.length());
	            otp.append(DIGITS.charAt(index));
	        }
	        return otp.toString();
	 }

	 


@Async
public void sendCareerEmailWithResumeAsync(
        String to,
        String subject,
        String body,
        byte[] resumeBytes,
        String resumeFileName) {

    try {
        MimeMessage message = emailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom("info@pragyagirlsschool.com");  // Sender's email
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);

        helper.addAttachment(
                resumeFileName,
                new ByteArrayResource(resumeBytes)
        );
        System.out.println(helper.toString());
        emailSender.send(message);
        System.out.println("mail send"+to);

    } catch (Exception e) {
        e.printStackTrace();
    }
}
}
/*
// Add all attachments
if (birthCertificate != null && !birthCertificate.isEmpty()) {
    helper.addAttachment(birthCertificate.getOriginalFilename(), birthCertificate);
}
if (lastResult != null && !lastResult.isEmpty()) {
    helper.addAttachment(lastResult.getOriginalFilename(), lastResult);
}
if (parentAadhar != null && !parentAadhar.isEmpty()) {
    helper.addAttachment(parentAadhar.getOriginalFilename(), parentAadhar);
}
if (studentAadhar != null && !studentAadhar.isEmpty()) {
    helper.addAttachment(studentAadhar.getOriginalFilename(), studentAadhar);
}
if (bankDoc != null && !bankDoc.isEmpty()) {
    helper.addAttachment(bankDoc.getOriginalFilename(), bankDoc);
}
if (cast != null && !cast.isEmpty()) {
    helper.addAttachment(cast.getOriginalFilename(), cast);
}
if (transferCertificate != null && !transferCertificate.isEmpty()) {
    helper.addAttachment(transferCertificate.getOriginalFilename(), transferCertificate);
}
if (profile != null && !profile.isEmpty()) {
    helper.addAttachment(profile.getOriginalFilename(), profile);
}
if (sssmid != null && !sssmid.isEmpty()) {
    helper.addAttachment(sssmid.getOriginalFilename(), sssmid);
}
*/