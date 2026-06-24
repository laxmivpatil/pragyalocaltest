package com.techverse.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.razorpay.Order;
import com.techverse.Model.GeneralAdmission;
import com.techverse.Repository.GeneralAdmissionRepository;
import com.techverse.Response.GeneralAdmissionResponse;
import com.techverse.Service.ApiResponseService;
import com.techverse.Service.EmailService1;
import com.techverse.Service.GeneralAdmissionService;
import com.techverse.Service.RazorpayService;
import com.techverse.Service.StorageSevice;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/admissions")
public class GeneralAdmissionController {

	@Autowired
	private StorageSevice storageService;
	
	 @Autowired
	    private ApiResponseService apiResponseService;


	@Autowired
	private EmailService1 emailService1;

	@Autowired
	private RazorpayService razorpayService;
	@Autowired
	private GeneralAdmissionService generalAdmissionService;
	String schoolEmail = "admissionenquiry@pragyagirlsschool.com";

	@Autowired
	private GeneralAdmissionRepository generalAdmissionRepository;

	@GetMapping("/")
	public ResponseEntity<GeneralAdmissionResponse> getAllAdmissions() {
		List<GeneralAdmission> admissions = generalAdmissionService.getAllAdmissions();

		GeneralAdmissionResponse admissionResponse = new GeneralAdmissionResponse(admissions);

		return new ResponseEntity<>(admissionResponse, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<GeneralAdmission> getAdmissionById(@PathVariable Long id) {
		Optional<GeneralAdmission> admission = generalAdmissionService.getAdmissionById(id);
		return admission.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/*
	 * @PostMapping("/") public ResponseEntity<?> createAdmission(@RequestPart(value
	 * = "firstName", required = false) String firstName,
	 * 
	 * @RequestPart(value = "lastName", required = false) String lastName,
	 * 
	 * @RequestPart(value = "gender", required = false) String gender,
	 * 
	 * @RequestPart(value = "dateOfBirth", required = false) String dateOfBirth,
	 * 
	 * @RequestPart(value = "admissionClass", required = false) String
	 * admissionClass,
	 * 
	 * @RequestPart(value = "fatherName", required = false) String fatherName,
	 * 
	 * @RequestPart(value = "motherName", required = false) String motherName,
	 * 
	 * @RequestPart(value = "mobileNo", required = false) String mobileNo,
	 * 
	 * @RequestPart(value = "email", required = false) String email,
	 * 
	 * @RequestPart(value = "type", required = false) String type,
	 * 
	 * @RequestPart(value = "pen", required = false) String pen,
	 * 
	 * @RequestPart(value = "birthCertificate", required = false) MultipartFile
	 * birthCertificate,
	 * 
	 * @RequestPart(value = "lastResult", required = false) MultipartFile
	 * lastResult,
	 * 
	 * @RequestPart(value = "parentAadhar", required = false) MultipartFile
	 * parentAadhar,
	 * 
	 * @RequestPart(value = "studentAadhar", required = false) MultipartFile
	 * studentAadhar,
	 * 
	 * @RequestPart(value = "bankDoc", required = false) MultipartFile bankDoc,
	 * 
	 * @RequestPart(value = "cast", required = false) MultipartFile cast,
	 * 
	 * @RequestPart(value = "transferCertificate", required = false) MultipartFile
	 * transferCertificate,
	 * 
	 * @RequestPart(value = "profile", required = false) MultipartFile profile,
	 * 
	 * @RequestPart(value = "sssmid", required = false) MultipartFile sssmid) {
	 * 
	 * 
	 * if(type.equals("general")) { GeneralAdmission createdAdmission = new
	 * GeneralAdmission(firstName,lastName, gender,dateOfBirth,
	 * admissionClass,fatherName, motherName, mobileNo, email, type, "", "", "","",
	 * "", "","", "", "","");
	 * 
	 * 
	 * String subject = "New Admission Request";
	 * 
	 * String body = "<html><body>" +
	 * "<p>Dear Pragya School Admissions Committee,</p>" +
	 * "<p>We are pleased to inform you that a new admission request has been received for the academic year 2024.</p>"
	 * + "<p>Details of the applicant are as follows:</p>" + "<ul>" +
	 * "<li>Full Name: " + firstName + " " + lastName + "</li>" +
	 * "<li>Date of Birth: " + dateOfBirth + "</li>" + "<li>Class Applying For: " +
	 * admissionClass + "</li>" + "<li>Father's Name: " + fatherName + "</li>" +
	 * "<li>Mother's Name: " + motherName + "</li>" + "<li>Mobile Number: " +
	 * mobileNo + "</li>" + "<li>Email Address: " + email + "</li>" + "</ul>" +
	 * "<p>Please review the details. If further information or clarification is required, please feel free to contact us.</p>"
	 * + "<p>Thank you for considering this admission request.</p>" +
	 * "<p>Best regards,<br/>" + firstName + " " + lastName + "<br/>" + mobileNo +
	 * "</p>" + "</body></html>";
	 * 
	 * emailService1.sendEmail(schoolEmail, subject, body);
	 * 
	 * 
	 * 
	 * String userSubject = "Your Admission Enquiry"; String userBody =
	 * "<html><body>" + "<p>Dear " + firstName + " " + lastName + ",</p>" +
	 * "<p>Thank you for your admission enquiry. We have received your request and will get back to you shortly.</p>"
	 * + "<p>Best regards,<br/>Pragya School Admissions Team</p>" +
	 * "</body></html>";
	 * 
	 * emailService1.sendEmail(email, userSubject, userBody);
	 * 
	 * // emailService.sendSimpleEmail(email, "New Admission", firstName+lastName);
	 * return new
	 * ResponseEntity<>(generalAdmissionRepository.save(createdAdmission),
	 * HttpStatus.OK);
	 * 
	 * 
	 * 
	 * } else { String birthC="",lastR="",parentA="",studentA="", sssmi="",
	 * bankD="", castC="",transferC="", profileP=""; //name String String
	 * birthCN="",lastRN="",parentAN="",studentAN="", sssmiN="",
	 * bankDN="",castCN="",transferCN="", profilePN=""; //extension String String
	 * birthT="",lastRT="",parentAT="",studentAT="", sssmiT="", bankDT="",
	 * castCT="",transferCT="", profilePT="";
	 * 
	 * 
	 * if (birthCertificate != null && !birthCertificate.isEmpty()) { birthT =
	 * getExtension(birthCertificate);
	 * 
	 * birthCN = "Birth_Certificate" + " " + Instant.now().toEpochMilli();
	 * 
	 * birthC = storageService.uploadFileOnAzure(birthCertificate,
	 * birthCN+"."+birthT );
	 * 
	 * }
	 * 
	 * if (lastResult != null && !lastResult.isEmpty()) { lastRT=
	 * getExtension(lastResult); lastRN= "Last_Year_Result" + " " +
	 * Instant.now().toEpochMilli(); lastR =
	 * storageService.uploadFileOnAzure(lastResult, lastRN+"."+lastRT); }
	 * 
	 * if (parentAadhar != null && !parentAadhar.isEmpty()) { parentAT=
	 * getExtension(parentAadhar); parentAN = "Parent_Aadhar" + " " +
	 * Instant.now().toEpochMilli(); parentA =
	 * storageService.uploadFileOnAzure(parentAadhar,parentAN+"."+parentAT); }
	 * 
	 * if (studentAadhar != null && !studentAadhar.isEmpty()) { studentAT =
	 * getExtension(studentAadhar); studentAN = "Student_Aadhar" + " " +
	 * Instant.now().toEpochMilli(); studentA =
	 * storageService.uploadFileOnAzure(studentAadhar, studentAN+"."+studentAT ); }
	 * 
	 * if (bankDoc != null && !bankDoc.isEmpty()) { bankDT = getExtension(bankDoc);
	 * bankDN = "Bank_Doc" + " " + Instant.now().toEpochMilli() ; bankD =
	 * storageService.uploadFileOnAzure(bankDoc, bankDN+"."+bankDT); }
	 * 
	 * if (cast != null && !cast.isEmpty()) { castCT = getExtension(cast); castCN =
	 * "Cast" + " " + Instant.now().toEpochMilli(); castC =
	 * storageService.uploadFileOnAzure(cast, castCN+"."+castCT); }
	 * 
	 * if (transferCertificate != null && !transferCertificate.isEmpty()) {
	 * transferCT= getExtension(transferCertificate); transferCN =
	 * "Transfer_Certificate" + " " + Instant.now().toEpochMilli(); transferC =
	 * storageService.uploadFileOnAzure(transferCertificate,
	 * transferCN+"."+transferCT); }
	 * 
	 * if (profile != null && !profile.isEmpty()) { profilePT =
	 * getExtension(profile); profilePN = "Profile" + " " +
	 * Instant.now().toEpochMilli() ; profileP =
	 * storageService.uploadFileOnAzure(profile, profilePN+"."+profilePT); }
	 * 
	 * if (sssmid != null && !sssmid.isEmpty()) { sssmiT = getExtension(sssmid);
	 * sssmiN = "SSSMID" + " " + Instant.now().toEpochMilli() ; sssmi =
	 * storageService.uploadFileOnAzure(sssmid, sssmiN+"."+sssmiT); }
	 * 
	 * GeneralAdmission createdAdmission = new GeneralAdmission(firstName,lastName,
	 * gender,dateOfBirth, admissionClass,fatherName, motherName, mobileNo, email,
	 * type, pen, birthC, lastR,parentA, studentA, sssmi, bankD, castC, transferC,
	 * profileP); String subject = "New Admission Request";
	 * 
	 * String body = "<html><body>" +
	 * "<p>Dear Pragya School Admissions Committee,</p>" +
	 * "<p>We are pleased to inform you that a new admission request has been received for the academic year 2024.</p>"
	 * + "<p>Details of the applicant are as follows:</p>" + "<ul>" +
	 * "<li>Full Name: " + firstName + " " + lastName + "</li>" +
	 * "<li>Date of Birth: " + dateOfBirth + "</li>" + "<li>Class Applying For: " +
	 * admissionClass + "</li>" + "<li>Father's Name: " + fatherName + "</li>" +
	 * "<li>Mother's Name: " + motherName + "</li>" + "<li>Mobile Number: " +
	 * mobileNo + "</li>" + "<li>Email Address: " + email + "</li>" + "<li>PEN: " +
	 * pen + "</li>" + "</ul>" + "<p>Attached documents:</p>" + "<ul>" +
	 * "<li>Birth Certificate</li>" + "<li>Previous Year's Result</li>" +
	 * "<li>Parent's Aadhar Card</li>" + "<li>Student's Aadhar Card</li>" +
	 * "<li>Bank Documents</li>" + "<li>Caste Certificate</li>" +
	 * "<li>Transfer Certificate</li>" + "<li>Profile Picture</li>" + "</ul>" +
	 * "<p>Please review the details and documents attached. If further information or clarification is required, please feel free to contact us.</p>"
	 * + "<p>Thank you for considering this admission request.</p>" +
	 * "<p>Best regards,<br/>" + firstName + " " + lastName + "<br/>" + mobileNo +
	 * "</p>" + "</body></html>";
	 * 
	 * 
	 * emailService1.sendEmailWithAttachment(schoolEmail, subject, body,
	 * birthCertificate, lastResult, parentAadhar, studentAadhar, bankDoc, cast,
	 * transferCertificate, profile, sssmid);
	 * 
	 * String userSubject = "Your Admission Enquiry"; String userBody =
	 * "<html><body>" + "<p>Dear " + firstName + " " + lastName + ",</p>" +
	 * "<p>Thank you for your admission enquiry. We have received your request and will get back to you shortly.</p>"
	 * + "<p>Best regards,<br/>Pragya School Admissions Team</p>" +
	 * "</body></html>";
	 * 
	 * emailService1.sendEmail(email, userSubject, userBody); return new
	 * ResponseEntity<>(generalAdmissionRepository.save(createdAdmission),HttpStatus
	 * .OK); } }
	 */
	/*@PostMapping("/")
	public ResponseEntity<?> createAdmission(@RequestPart(value = "firstName", required = false) String firstName,
			@RequestPart(value = "lastName", required = false) String lastName,
			@RequestPart(value = "gender", required = false) String gender,
			@RequestPart(value = "dateOfBirth", required = false) String dateOfBirth,
			@RequestPart(value = "admissionClass", required = false) String admissionClass,
			@RequestPart(value = "fatherName", required = false) String fatherName,
			@RequestPart(value = "motherName", required = false) String motherName,
			@RequestPart(value = "mobileNo", required = false) String mobileNo,
			@RequestPart(value = "email", required = false) String email,
			@RequestPart(value = "type", required = false) String type,
			@RequestPart(value = "pen", required = false) String pen,
			@RequestPart(value = "boardingType", required = false) String boardingType,
			@RequestPart(value = "preferredSubject", required = false) String preferredSubject,
			
			@RequestPart(value = "razorpayOrderId", required = false) String razorpayOrderId,
			@RequestPart(value = "razorpayPaymentId", required = false) String razorpayPaymentId,
			@RequestPart(value = "razorpaySignature", required = false) String razorpaySignature,
			@RequestPart(value = "admissionFee", required = false) String admissionFee,
			@RequestPart(value = "registrationFee", required = false) String registrationFee,
			@RequestPart(value = "totalFee", required = false) String totalFee,	
			
			@RequestPart(value = "birthCertificate", required = false) MultipartFile birthCertificate,
			@RequestPart(value = "lastResult", required = false) MultipartFile lastResult,
			@RequestPart(value = "parentAadhar", required = false) MultipartFile parentAadhar,
			@RequestPart(value = "studentAadhar", required = false) MultipartFile studentAadhar,
			@RequestPart(value = "bankDoc", required = false) MultipartFile bankDoc,
			@RequestPart(value = "cast", required = false) MultipartFile cast,
			@RequestPart(value = "transferCertificate", required = false) MultipartFile transferCertificate,
			@RequestPart(value = "profile", required = false) MultipartFile profile,
			@RequestPart(value = "sssmid", required = false) MultipartFile sssmid)
		
	{
		BigDecimal admissionFeeAmount = parseAmount(admissionFee);
		BigDecimal registrationFeeAmount = parseAmount(registrationFee);
		BigDecimal totalFeeAmount = parseAmount(totalFee);

		String paymentStatus = "PENDING";
		Instant paymentDate = null;

		if (razorpayOrderId != null && razorpayPaymentId != null && razorpaySignature != null) {
		    boolean validPayment = razorpayService.verifyRazorpaySignature(
		            razorpayOrderId,
		            razorpayPaymentId,
		            razorpaySignature
		    );

		    if (!validPayment) {
		        return ResponseEntity
		                .badRequest()
		                .body("Invalid Razorpay payment signature");
		    }

		    paymentStatus = "PAID";
		    paymentDate = Instant.now();
		}
		Map<String, Object> variables = new HashMap<>();
		  variables.put("firstName", firstName);
		  variables.put("lastName", lastName);
		  variables.put("dateOfBirth", dateOfBirth);
		  variables.put("admissionClass", admissionClass);
		  variables.put("fatherName", fatherName);
		  variables.put("motherName", motherName);
		  variables.put("mobileNo", mobileNo);
		  variables.put("email", email);
		  variables.put("preferredSubject", preferredSubject);
		  variables.put("boardingType", boardingType);
		  variables.put("admissionFee", admissionFeeAmount);
		  variables.put("registrationFee", registrationFeeAmount);
		  variables.put("totalFee", totalFeeAmount);
		  variables.put("paymentStatus", paymentStatus);
		  variables.put("paymentDate", paymentDate);
		  variables.put("razorpayPaymentId", razorpayPaymentId);
		  variables.put("razorpayOrderId", razorpayOrderId);
		
		  if(type.equals("general")) { 
		/*	  GeneralAdmission createdAdmission = new
		 GeneralAdmission(firstName,lastName, gender,dateOfBirth,
		  admissionClass,preferredSubject,boardingType,fatherName, motherName, mobileNo, email, type, "", "", "","",
		  "", "","", "", "","");
		  */
		/*			  GeneralAdmission createdAdmission = new GeneralAdmission(
						        firstName,
						        lastName,
						        gender,
						        dateOfBirth,
						        admissionClass,
						        preferredSubject,
						        boardingType,
						        fatherName,
						        motherName,
						        mobileNo,
						        email,
						        type,
						        "",
						        "",
						        "",
						        "",
						        "",
						        "",
						        "",
						        "",
						        "",
						        "",

						        razorpayOrderId,
						        razorpayPaymentId,
						        razorpaySignature,
						        admissionFeeAmount,
						        registrationFeeAmount,
						        totalFeeAmount,
						        paymentStatus,
						        paymentDate
						);
 	  
			  generalAdmissionRepository.save(createdAdmission);
			  ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.OK);
		  String schoolBody = emailService1.generateEmailContent("schoolgeneraladmission", variables);
		   String userBody = emailService1.generateEmailContent("usergeneraladmission", variables);
		   
		   sendEmailAsync(schoolEmail, "New Admission Enquiry", schoolBody);
	        sendEmailAsync(email, "Your Admission Enquiry", userBody);
	  	   return response;
		   
		  
		  
		  } 
		  else { 
			  variables.put("pen", pen);
			  String  birthC="",lastR="",parentA="",studentA="", sssmi="", bankD="", castC="",transferC="", profileP=""; //name String
			  String birthCN="",lastRN="",parentAN="",studentAN="", sssmiN="", bankDN="",castCN="",transferCN="", profilePN=""; //extension String
			  String birthT="",lastRT="",parentAT="",studentAT="", sssmiT="", bankDT="", castCT="",transferCT="", profilePT="";
		  
		  
		   
			  birthT = getExtension(birthCertificate);
		  
		  birthCN = "Birth_Certificate" + " " + Instant.now().toEpochMilli();
		  
		  birthC = storageService.uploadFileOnAzure(birthCertificate,
		  birthCN+"."+birthT );
		   
		  
		  
			  lastRT= getExtension(lastResult);
			  lastRN= "Last_Year_Result" + " " + Instant.now().toEpochMilli();
			  lastR = storageService.uploadFileOnAzure(lastResult, lastRN+"."+lastRT);  
		  
		   parentAT=
		  getExtension(parentAadhar); parentAN = "Parent_Aadhar" + " " +
		  Instant.now().toEpochMilli(); parentA =
		  storageService.uploadFileOnAzure(parentAadhar,parentAN+"."+parentAT); 
		  
		  studentAT =
		  getExtension(studentAadhar); studentAN = "Student_Aadhar" + " " +
		  Instant.now().toEpochMilli(); studentA =
		  storageService.uploadFileOnAzure(studentAadhar, studentAN+"."+studentAT );  
		  
		    bankDT = getExtension(bankDoc);
		  bankDN = "Bank_Doc" + " " + Instant.now().toEpochMilli() ; bankD =
		  storageService.uploadFileOnAzure(bankDoc, bankDN+"."+bankDT); 
		  
		    castCT = getExtension(cast); castCN =
		  "Cast" + " " + Instant.now().toEpochMilli(); castC =
		  storageService.uploadFileOnAzure(cast, castCN+"."+castCT);  
		  
		  
		  transferCT= getExtension(transferCertificate); transferCN =
		  "Transfer_Certificate" + " " + Instant.now().toEpochMilli(); 
		  transferC = storageService.uploadFileOnAzure(transferCertificate,
		  transferCN+"."+transferCT); 
		  
		   profilePT =
		  getExtension(profile); profilePN = "Profile" + " " +
		  Instant.now().toEpochMilli() ; profileP =
		  storageService.uploadFileOnAzure(profile, profilePN+"."+profilePT); 
		  
		   sssmiT = getExtension(sssmid);
		  sssmiN = "SSSMID" + " " + Instant.now().toEpochMilli() ; sssmi =
		  storageService.uploadFileOnAzure(sssmid, sssmiN+"."+sssmiT); 
		  
		 /*GeneralAdmission createdAdmission = new GeneralAdmission(firstName,lastName,
		  gender,dateOfBirth, admissionClass,preferredSubject,boardingType,fatherName, motherName, mobileNo, email,
		  type, pen, birthC, lastR,parentA, studentA, sssmi, bankD, castC, transferC,
		  profileP);
		  */
		/*  GeneralAdmission createdAdmission = new GeneralAdmission(
			        firstName,
			        lastName,
			        gender,
			        dateOfBirth,
			        admissionClass,
			        preferredSubject,
			        boardingType,
			        fatherName,
			        motherName,
			        mobileNo,
			        email,
			        type,
			        pen,
			        birthC,
			        lastR,
			        parentA,
			        studentA,
			        sssmi,
			        bankD,
			        castC,
			        transferC,
			        profileP,

			        razorpayOrderId,
			        razorpayPaymentId,
			        razorpaySignature,
			        admissionFeeAmount,
			        registrationFeeAmount,
			        totalFeeAmount,
			        paymentStatus,
			        paymentDate
			);

			 
		  generalAdmissionRepository.save(createdAdmission);
		  
		  
		  ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.OK);
		 
			

		
		  String schoolBody = emailService1.generateEmailContent("schooladvanceadmission", variables);
		  
		  String userBody = emailService1.generateEmailContent("useradvanceadmission", variables);
		    
		  
		  
		   sendEmailwithattchAsync(schoolEmail, "New Admission Enquiry", schoolBody, birthC,birthCN+"."+birthT, lastR,lastRN+"."+lastRT, parentA,parentAN+"."+parentAT, studentA,studentAN+"."+studentAT, bankD,bankDN+"."+bankDT, castC,castCN+"."+castCT,
		 	  transferC,transferCN+"."+transferCT, profileP,profilePN+"."+profilePT, sssmi,sssmiN+"."+sssmiT);
		 
	       sendEmailAsync(email, "Your Admission Enquiry", userBody);
		  
		   
	 
		 return response;
		  }
	}
	
	*/
	
	/*
	@PostMapping("/create-admission")
	public ResponseEntity<?> createAdmissionOrder(
	        @RequestPart(value = "firstName", required = false) String firstName,
	        @RequestPart(value = "lastName", required = false) String lastName,
	        @RequestPart(value = "gender", required = false) String gender,
	        @RequestPart(value = "dateOfBirth", required = false) String dateOfBirth,
	        @RequestPart(value = "admissionClass", required = false) String admissionClass,
	        @RequestPart(value = "fatherName", required = false) String fatherName,
	        @RequestPart(value = "motherName", required = false) String motherName,
	        @RequestPart(value = "mobileNo", required = false) String mobileNo,
	        @RequestPart(value = "email", required = false) String email,
	        @RequestPart(value = "type", required = false) String type,
	        @RequestPart(value = "pen", required = false) String pen,
	        @RequestPart(value = "boardingType", required = false) String boardingType,
	        @RequestPart(value = "preferredSubject", required = false) String preferredSubject,

	        @RequestPart(value = "admissionFee", required = false) String admissionFee,
	        @RequestPart(value = "registrationFee", required = false) String registrationFee,
	        @RequestPart(value = "totalFee", required = false) String totalFee,

	        @RequestPart(value = "birthCertificate", required = false) MultipartFile birthCertificate,
	        @RequestPart(value = "lastResult", required = false) MultipartFile lastResult,
	        @RequestPart(value = "parentAadhar", required = false) MultipartFile parentAadhar,
	        @RequestPart(value = "studentAadhar", required = false) MultipartFile studentAadhar,
	        @RequestPart(value = "bankDoc", required = false) MultipartFile bankDoc,
	        @RequestPart(value = "cast", required = false) MultipartFile cast,
	        @RequestPart(value = "transferCertificate", required = false) MultipartFile transferCertificate,
	        @RequestPart(value = "profile", required = false) MultipartFile profile,
	        @RequestPart(value = "sssmid", required = false) MultipartFile sssmid) {

	    try {
	        BigDecimal admissionFeeAmount = parseAmount(admissionFee);
	        BigDecimal registrationFeeAmount = parseAmount(registrationFee);
	        BigDecimal totalFeeAmount = parseAmount(totalFee);

	        if (totalFeeAmount.compareTo(BigDecimal.ZERO) <= 0) {
	            return apiResponse(false, "Total fee must be greater than zero");
	        }

	        String paymentStatus = "PENDING";
	        Instant paymentDate = null;

	        String birthC = "";
	        String lastR = "";
	        String parentA = "";
	        String studentA = "";
	        String sssmi = "";
	        String bankD = "";
	        String castC = "";
	        String transferC = "";
	        String profileP = "";

	        if (!"general".equalsIgnoreCase(type)) {
	            birthC = uploadIfPresent(birthCertificate, "Birth_Certificate");
	            lastR = uploadIfPresent(lastResult, "Last_Year_Result");
	            parentA = uploadIfPresent(parentAadhar, "Parent_Aadhar");
	            studentA = uploadIfPresent(studentAadhar, "Student_Aadhar");
	            bankD = uploadIfPresent(bankDoc, "Bank_Doc");
	            castC = uploadIfPresent(cast, "Cast");
	            transferC = uploadIfPresent(transferCertificate, "Transfer_Certificate");
	            profileP = uploadIfPresent(profile, "Profile");
	            sssmi = uploadIfPresent(sssmid, "SSSMID");
	        }

	        GeneralAdmission createdAdmission = new GeneralAdmission(
	                firstName,
	                lastName,
	                gender,
	                dateOfBirth,
	                admissionClass,
	                preferredSubject,
	                boardingType,
	                fatherName,
	                motherName,
	                mobileNo,
	                email,
	                type,
	                "general".equalsIgnoreCase(type) ? "" : pen,
	                birthC,
	                lastR,
	                parentA,
	                studentA,
	                sssmi,
	                bankD,
	                castC,
	                transferC,
	                profileP,

	                null,
	                null,
	                null,
	                admissionFeeAmount,
	                registrationFeeAmount,
	                totalFeeAmount,
	                paymentStatus,
	                paymentDate
	        );

	        generalAdmissionRepository.save(createdAdmission);

	        String receipt = "admission_" + createdAdmission.getId();

	        Order razorpayOrder = razorpayService.createOrder(totalFeeAmount, receipt);

	        String razorpayOrderId = razorpayOrder.get("id");
	        Integer amountInPaise = razorpayOrder.get("amount");
	        String currency = razorpayOrder.get("currency");

	        createdAdmission.setRazorpayOrderId(razorpayOrderId);
	        generalAdmissionRepository.save(createdAdmission);

	        Map<String, Object> data = new HashMap<>();
	        data.put("admissionId", createdAdmission.getId());
	        data.put("razorpayOrderId", razorpayOrderId);
	        data.put("amount", amountInPaise);
	        data.put("currency", currency);
	       // data.put("key", razorpayService.getRazorpayKeyId());
	        data.put("paymentStatus", "PENDING");

	        return apiResponse(true, "Admission form saved and Razorpay order created", data);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return apiResponse(false, "Something went wrong: " + e.getMessage());
	    }
	}
	
	 
	*/
	
	@PostMapping("/create-admission")
	public ResponseEntity<?> createAdmissionOrder(
	        @RequestPart(value = "firstName", required = false) String firstName,
	        @RequestPart(value = "lastName", required = false) String lastName,
	        @RequestPart(value = "gender", required = false) String gender,
	        @RequestPart(value = "dateOfBirth", required = false) String dateOfBirth,
	        @RequestPart(value = "admissionClass", required = false) String admissionClass,
	        @RequestPart(value = "fatherName", required = false) String fatherName,
	        @RequestPart(value = "motherName", required = false) String motherName,
	        @RequestPart(value = "mobileNo", required = false) String mobileNo,
	        @RequestPart(value = "email", required = false) String email,
	        @RequestPart(value = "type", required = false) String type,
	        @RequestPart(value = "pen", required = false) String pen,
	        @RequestPart(value = "boardingType", required = false) String boardingType,
	        @RequestPart(value = "preferredSubject", required = false) String preferredSubject,

	        @RequestPart(value = "admissionFee", required = false) String admissionFee,
	        @RequestPart(value = "registrationFee", required = false) String registrationFee,
	        @RequestPart(value = "totalFee", required = false) String totalFee,

	        @RequestPart(value = "birthCertificate", required = false) MultipartFile birthCertificate,
	        @RequestPart(value = "lastResult", required = false) MultipartFile lastResult,
	        @RequestPart(value = "parentAadhar", required = false) MultipartFile parentAadhar,
	        @RequestPart(value = "studentAadhar", required = false) MultipartFile studentAadhar,
	        @RequestPart(value = "bankDoc", required = false) MultipartFile bankDoc,
	        @RequestPart(value = "cast", required = false) MultipartFile cast,
	        @RequestPart(value = "transferCertificate", required = false) MultipartFile transferCertificate,
	        @RequestPart(value = "profile", required = false) MultipartFile profile,
	        @RequestPart(value = "sssmid", required = false) MultipartFile sssmid) {

	    try {
	        boolean isGeneralAdmission = "general".equalsIgnoreCase(type);

	        BigDecimal admissionFeeAmount = parseAmount(admissionFee);
	        BigDecimal registrationFeeAmount = parseAmount(registrationFee);
	        BigDecimal totalFeeAmount = parseAmount(totalFee);

	        /*
	         * GENERAL ADMISSION
	         * No payment, no Razorpay order.
	         */
	        if (isGeneralAdmission) {

	            GeneralAdmission createdAdmission = new GeneralAdmission(
	                    firstName,
	                    lastName,
	                    gender,
	                    dateOfBirth,
	                    admissionClass,
	                    preferredSubject,
	                    boardingType,
	                    fatherName,
	                    motherName,
	                    mobileNo,
	                    email,
	                    type,
	                    "",
	                    "",
	                    "",
	                    "",
	                    "",
	                    "",
	                    "",
	                    "",
	                    "",
	                    "",

	                    null,
	                    null,
	                    null,
	                    BigDecimal.ZERO,
	                    BigDecimal.ZERO,
	                    BigDecimal.ZERO,
	                    "NOT_REQUIRED",
	                    null
	            );

	            generalAdmissionRepository.save(createdAdmission);

	            Map<String, Object> variables = new HashMap<>();
	            variables.put("firstName", firstName);
	            variables.put("lastName", lastName);
	            variables.put("dateOfBirth", dateOfBirth);
	            variables.put("admissionClass", admissionClass);
	            variables.put("fatherName", fatherName);
	            variables.put("motherName", motherName);
	            variables.put("mobileNo", mobileNo);
	            variables.put("email", email);
	            variables.put("preferredSubject", preferredSubject);
	            variables.put("boardingType", boardingType);
	            variables.put("paymentStatus", "NOT_REQUIRED");

	            String schoolBody = emailService1.generateEmailContent("schoolgeneraladmission", variables);
	            String userBody = emailService1.generateEmailContent("usergeneraladmission", variables);

	          //  sendEmailAsync(schoolEmail, "New General Admission Enquiry", schoolBody);
	          //  sendEmailAsync(email, "Your Admission Enquiry", userBody);

	            Map<String, Object> data = new HashMap<>();
	            data.put("admissionId", createdAdmission.getId());
	            data.put("paymentRequired", false);
	            data.put("paymentStatus", "NOT_REQUIRED");

	            return apiResponse(true, "General admission submitted successfully", data);
	        }

	        /*
	         * ADVANCE ADMISSION
	         * Payment required, Razorpay order created.
	         */
	        if (totalFeeAmount.compareTo(BigDecimal.ZERO) <= 0) {
	            return apiResponse(false, "Total fee must be greater than zero");
	        }

	        String birthC = uploadIfPresent(birthCertificate, "Birth_Certificate");
	        String lastR = uploadIfPresent(lastResult, "Last_Year_Result");
	        String parentA = uploadIfPresent(parentAadhar, "Parent_Aadhar");
	        String studentA = uploadIfPresent(studentAadhar, "Student_Aadhar");
	        String bankD = uploadIfPresent(bankDoc, "Bank_Doc");
	        String castC = uploadIfPresent(cast, "Cast");
	        String transferC = uploadIfPresent(transferCertificate, "Transfer_Certificate");
	        String profileP = uploadIfPresent(profile, "Profile");
	        String sssmi = uploadIfPresent(sssmid, "SSSMID");

	        GeneralAdmission createdAdmission = new GeneralAdmission(
	                firstName,
	                lastName,
	                gender,
	                dateOfBirth,
	                admissionClass,
	                preferredSubject,
	                boardingType,
	                fatherName,
	                motherName,
	                mobileNo,
	                email,
	                type,
	                pen,
	                birthC,
	                lastR,
	                parentA,
	                studentA,
	                sssmi,
	                bankD,
	                castC,
	                transferC,
	                profileP,

	                null,
	                null,
	                null,
	                admissionFeeAmount,
	                registrationFeeAmount,
	                totalFeeAmount,
	                "PENDING",
	                null
	        );

	        generalAdmissionRepository.save(createdAdmission);

	        String receipt = "admission_" + createdAdmission.getId();

	        Order razorpayOrder = razorpayService.createOrder(totalFeeAmount, receipt);

	        String razorpayOrderId = razorpayOrder.get("id");
	        Integer amountInPaise = razorpayOrder.get("amount");
	        String currency = razorpayOrder.get("currency");

	        createdAdmission.setRazorpayOrderId(razorpayOrderId);
	        generalAdmissionRepository.save(createdAdmission);

	        Map<String, Object> data = new HashMap<>();
	        data.put("admissionId", createdAdmission.getId());
	        data.put("razorpayOrderId", razorpayOrderId);
	        data.put("amount", amountInPaise);
	        data.put("currency", currency);
	        data.put("paymentRequired", true);
	        data.put("paymentStatus", "PENDING");
	        data.put("mobileNo", mobileNo);

	        // Add this if frontend needs Razorpay key from backend
	        // data.put("key", razorpayService.getRazorpayKeyId());

	        return apiResponse(true, "Advance admission saved and Razorpay order created", data);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return apiResponse(false, "Something went wrong: " + e.getMessage());
	    }
	}
	
	@PostMapping("/payment-success")
	public ResponseEntity<?> admissionPaymentSuccess(@RequestBody Map<String, String> request) {

	    try {
	        String admissionIdText = request.get("admissionId");
	        String razorpayOrderId = request.get("razorpayOrderId");
	        String razorpayPaymentId = request.get("razorpayPaymentId");
	        String razorpaySignature = request.get("razorpaySignature");

	        if (isBlank(admissionIdText)) {
	            return apiResponse(false, "Admission id is required");
	        }

	        if (isBlank(razorpayOrderId)) {
	            return apiResponse(false, "Razorpay order id is required");
	        }

	        if (isBlank(razorpayPaymentId)) {
	            return apiResponse(false, "Razorpay payment id is required");
	        }

	        if (isBlank(razorpaySignature)) {
	            return apiResponse(false, "Razorpay signature is required");
	        }

	        Long admissionId = Long.parseLong(admissionIdText);

	        GeneralAdmission admission = generalAdmissionRepository
	                .findById(admissionId)
	                .orElse(null);

	        if (admission == null) {
	            return apiResponse(false, "Admission not found");
	        }

	        if ("PAID".equalsIgnoreCase(admission.getPaymentStatus())) {
	            return apiResponse(true, "Payment already verified");
	        }

	        if (!razorpayOrderId.equals(admission.getRazorpayOrderId())) {
	            return apiResponse(false, "Razorpay order id does not match");
	        }

	        boolean validPayment = razorpayService.verifyRazorpaySignature(
	                admission.getRazorpayOrderId(),
	                razorpayPaymentId,
	                razorpaySignature
	        );

	        if (!validPayment) {
	            return apiResponse(false, "Invalid Razorpay payment signature");
	        }

	        admission.setRazorpayPaymentId(razorpayPaymentId);
	        admission.setRazorpaySignature(razorpaySignature);
	        admission.setPaymentStatus("PAID");
	        admission.setPaymentDate(Instant.now());

	        generalAdmissionRepository.save(admission);

	      //  sendAdmissionEmailsAfterPayment(admission);

	        return apiResponse(true, "Payment verified and admission submitted successfully", admission);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return apiResponse(false, "Something went wrong: " + e.getMessage());
	    }
	}
	private void sendAdmissionEmailsAfterPayment(GeneralAdmission admission) {

	    Map<String, Object> variables = new HashMap<>();

	    variables.put("firstName", admission.getFirstName());
	    variables.put("lastName", admission.getLastName());
	    variables.put("dateOfBirth", admission.getDateOfBirth());
	    variables.put("admissionClass", admission.getAdmissionClass());
	    variables.put("fatherName", admission.getFatherName());
	    variables.put("motherName", admission.getMotherName());
	    variables.put("mobileNo", admission.getMobileNo());
	    variables.put("email", admission.getEmail());
	    variables.put("preferredSubject", admission.getPreferredSubject());
	    variables.put("boardingType", admission.getBoardingType());

	    variables.put("admissionFee", admission.getAdmissionFee());
	    variables.put("registrationFee", admission.getRegistrationFee());
	    variables.put("totalFee", admission.getTotalFee());
	    variables.put("paymentStatus", admission.getPaymentStatus());
	    variables.put("paymentDate", admission.getPaymentDate());
	    variables.put("razorpayPaymentId", admission.getRazorpayPaymentId());
	    variables.put("razorpayOrderId", admission.getRazorpayOrderId());
	    variables.put("pen", admission.getPEN());

	        String schoolBody = emailService1.generateEmailContent("schooladvanceadmission", variables);
	        String userBody = emailService1.generateEmailContent("useradvanceadmission", variables);
/*
	        sendEmailwithattchAsync(
	                schoolEmail,
	                "New Admission Enquiry",
	                schoolBody,

	                admission.getBirthCertificate(),
	                fileNameFromUrl(admission.getBirthCertificate(), "Birth_Certificate"),

	                admission.getLastResult(),
	                fileNameFromUrl(admission.getLastResult(), "Last_Year_Result"),

	                admission.getParentAadhar(),
	                fileNameFromUrl(admission.getParentAadhar(), "Parent_Aadhar"),

	                admission.getStudentAadhar(),
	                fileNameFromUrl(admission.getStudentAadhar(), "Student_Aadhar"),

	                admission.getBankDoc(),
	                fileNameFromUrl(admission.getBankDoc(), "Bank_Doc"),

	                admission.getCast(),
	                fileNameFromUrl(admission.getCast(), "Cast"),

	                admission.getTransferCertificate(),
	                fileNameFromUrl(admission.getTransferCertificate(), "Transfer_Certificate"),

	                admission.getProfile(),
	                fileNameFromUrl(admission.getProfile(), "Profile"),

	                admission.getSSSMID(),
	                fileNameFromUrl(admission.getSSSMID(), "SSSMID")
	        );

	        sendEmailAsync(admission.getEmail(), "Your Admission Enquiry", userBody);
	        */
	    
	}
	@Async("taskExecutor")
	public void sendEmailAsync(String to, String subject, String body) {

		emailService1.sendEmail(to, subject, body);
	}

	@Async("taskExecutor")
	public void sendEmailwithattchAsync(String to, String subject, String body, String birthCertificate,
			String birthCertificatefile, String lastResult, String lastResultfile, String parentAadhar,
			String parentAadharfile, String studentAadhar, String studentAadharfile, String bankDoc, String bankDocfile,
			String cast, String castfile, String transferCertificate, String transferCertificatefile, String profile,
			String profilefile, String sssmid, String sssmidfile) {

		emailService1.sendEmailWithAttachment(to, subject, body, birthCertificate, birthCertificatefile, lastResult,
				lastResultfile, parentAadhar, parentAadharfile, studentAadhar, studentAadharfile, bankDoc, bankDocfile,
				cast, castfile, transferCertificate, transferCertificatefile, profile, profilefile, sssmid, sssmidfile);
	}

	public String getExtension(MultipartFile file) {
		String originalFileName = file.getOriginalFilename();
		String ext = originalFileName.substring(originalFileName.lastIndexOf('.') + 1);
		return ext;
	}

	private BigDecimal parseAmount(String value) {
		if (value == null || value.trim().isEmpty()) {
			return BigDecimal.ZERO;
		}
		return new BigDecimal(value.trim());
	}

	// laxmi.patil@techverse.world "laxmipatil070295@gmail.com"
	// schooladvanceadmission

	@PostMapping("/                                                        ")
	public String sendEmail() {
		Map<String, Object> variables = new HashMap<>();
		variables.put("firstName", "laxmi");
		variables.put("lastName", "patil");
		variables.put("mobile", "9860540621");
		variables.put("email", "madhavsahu@gmail.com");
		variables.put("subject", "connect form");
		variables.put("city", "connect form");
		String schoolBody = emailService1.generateEmailContent("schoolEmailTemplate", variables);
		sendEmailAsync("laxmipatil070295@gmail.com", "Contact Form", schoolBody);

		return "hello";
	}
	
	
	
	private String uploadIfPresent(MultipartFile file, String prefix) {
	    try {
	        if (file == null || file.isEmpty()) {
	            return "";
	        }

	        String extension = getExtension(file);
	        String fileName = prefix + "_" + Instant.now().toEpochMilli();

	        return storageService.uploadFileOnAzure(file, fileName + "." + extension);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return "";
	    }
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

 

	private boolean isBlank(String value) {
	    return value == null || value.trim().isEmpty();
	}

	private ResponseEntity<?> apiResponse(boolean status, String message) {
	    Map<String, Object> response = new HashMap<>();
	    response.put("status", status);
	    response.put("message", message);
	    return ResponseEntity.ok(response);
	}

	private ResponseEntity<?> apiResponse(boolean status, String message, Object data) {
	    Map<String, Object> response = new HashMap<>();
	    response.put("status", status);
	    response.put("message", message);
	    response.put("data", data);
	    return ResponseEntity.ok(response);
	}

}

// <td th:text="${firstName} + ' ' + ${lastName}">Navya Sahu</td>
// // <p class="unsubscribe"><a href="#" th:href="@{/unsubscribe}">Unsubscribe</a></p>