package com.techverse.Model;

import java.math.BigDecimal;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class GeneralAdmission {
	
	
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String gender;
    private String dateOfBirth;
    private String admissionClass;
    private String preferredSubject="";
    private String fatherName;
    private String motherName;
    private String mobileNo;
    private String email;
    private String boardingType;
    
    private String type;
    
    
    private String PEN;
    private String birthCertificate;
    private String lastResult;
    private String parentAadhar;
    private String studentAadhar;
    private String SSSMID;
    private String bankDoc;
    private String cast;
    private String transferCertificate;
    private String profile;
    
    
    
    
    @Column(name = "razorpay_order_id")
    private String razorpayOrderId;


	@Column(name = "razorpay_payment_id")
    private String razorpayPaymentId;

    @Column(name = "razorpay_signature")
    private String razorpaySignature;

    @Column(name = "admission_fee")
    private BigDecimal admissionFee;

    @Column(name = "registration_fee")
    private BigDecimal registrationFee;

    @Column(name = "total_fee")
    private BigDecimal totalFee;

    @Column(name = "payment_status")
    private String paymentStatus; // PAID, PENDING, FAILED

    @Column(name = "payment_date")
    private Instant paymentDate;
    
    
    public GeneralAdmission(
            String firstName,
            String lastName,
            String gender,
            String dateOfBirth,
            String admissionClass,
            String preferredSubject,
            String boardingType,
            String fatherName,
            String motherName,
            String mobileNo,
            String email,
            String type,
            String pen,
            String birthCertificate,
            String lastResult,
            String parentAadhar,
            String studentAadhar,
            String sssmid,
            String bankDoc,
            String cast,
            String transferCertificate,
            String profile,

            String razorpayOrderId,
            String razorpayPaymentId,
            String razorpaySignature,
            BigDecimal admissionFee,
            BigDecimal registrationFee,
            BigDecimal totalFee,
            String paymentStatus,
            Instant paymentDate
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.admissionClass = admissionClass;
        this.preferredSubject = preferredSubject;
        this.boardingType = boardingType;
        this.fatherName = fatherName;
        this.motherName = motherName;
        this.mobileNo = mobileNo;
        this.email = email;
        this.type = type;
        this.PEN = pen;

        this.birthCertificate = birthCertificate;
        this.lastResult = lastResult;
        this.parentAadhar = parentAadhar;
        this.studentAadhar = studentAadhar;
        this.SSSMID = sssmid;
        this.bankDoc = bankDoc;
        this.cast = cast;
        this.transferCertificate = transferCertificate;
        this.profile = profile;

        this.razorpayOrderId = razorpayOrderId;
        this.razorpayPaymentId = razorpayPaymentId;
        this.razorpaySignature = razorpaySignature;
        this.admissionFee = admissionFee;
        this.registrationFee = registrationFee;
        this.totalFee = totalFee;
        this.paymentStatus = paymentStatus;
        this.paymentDate = paymentDate;
    }
    
    
    public GeneralAdmission(Long id, String firstName, String lastName, String gender, String dateOfBirth,
			String admissionClass,String preferredSubject, String boardingType,String fatherName, String motherName, String mobileNo, String email, String type,
			String pEN, String birthCertificate, String lastResult, String parentAadhar, String studentAadhar,
			String sSSMID, String bankDoc, String cast, String transferCertificate, String profile) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.dateOfBirth = dateOfBirth;
		this.admissionClass = admissionClass;

		this.preferredSubject= preferredSubject;
		this.boardingType=boardingType;
		this.fatherName = fatherName;
		this.motherName = motherName;
		this.mobileNo = mobileNo;
		this.email = email;
		this.type = type;
		this.PEN = pEN;
		this.birthCertificate = birthCertificate;
		this.lastResult = lastResult;
		this.parentAadhar = parentAadhar;
		this.studentAadhar = studentAadhar;
		SSSMID = sSSMID;
		this.bankDoc = bankDoc;
		this.cast = cast;
		this.transferCertificate = transferCertificate;
		this.profile = profile;
	
	}
    public GeneralAdmission( String firstName, String lastName, String gender, String dateOfBirth,
			String admissionClass,String preferredSubject,String boardingType, String fatherName, String motherName, String mobileNo, String email, String type,
			String pEN, String birthCertificate, String lastResult, String parentAadhar, String studentAadhar,
			String sSSMID, String bankDoc, String cast, String transferCertificate, String profile) {
		super();
		 
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.dateOfBirth = dateOfBirth;
		this.admissionClass = admissionClass;
		this.preferredSubject= preferredSubject;
		this.boardingType=boardingType;

		this.fatherName = fatherName;
		this.motherName = motherName;
		this.mobileNo = mobileNo;
		this.email = email;
		this.type = type;
		this.PEN = pEN;
		this.birthCertificate = birthCertificate;
		this.lastResult = lastResult;
		this.parentAadhar = parentAadhar;
		this.studentAadhar = studentAadhar;
		this.SSSMID = sSSMID;
		this.bankDoc = bankDoc;
		this.cast = cast;
		this.transferCertificate = transferCertificate;
		this.profile = profile;
	}

	public GeneralAdmission() {
		// TODO Auto-generated constructor stub
	}
    
 
	
	
	
	public String getBoardingType() {
		return boardingType;
	}
	public void setBoardingType(String boardingType) {
		this.boardingType = boardingType;
	}
	public String getPreferredSubject() {
		return preferredSubject;
	}
	public void setPreferredSubject(String preferredSubject) {
		this.preferredSubject = preferredSubject;
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPEN() {
		return PEN;
	}

	public void setPEN(String pEN) {
		PEN = pEN;
	}

	public String getBirthCertificate() {
		return birthCertificate;
	}

	public void setBirthCertificate(String birthCertificate) {
		this.birthCertificate = birthCertificate;
	}

	public String getLastResult() {
		return lastResult;
	}

	public void setLastResult(String lastResult) {
		this.lastResult = lastResult;
	}

	public String getParentAadhar() {
		return parentAadhar;
	}

	public void setParentAadhar(String parentAadhar) {
		this.parentAadhar = parentAadhar;
	}

	public String getStudentAadhar() {
		return studentAadhar;
	}

	public void setStudentAadhar(String studentAadhar) {
		this.studentAadhar = studentAadhar;
	}

	public String getSSSMID() {
		return SSSMID;
	}

	public void setSSSMID(String sSSMID) {
		SSSMID = sSSMID;
	}

	public String getBankDoc() {
		return bankDoc;
	}

	public void setBankDoc(String bankDoc) {
		this.bankDoc = bankDoc;
	}

	public String getCast() {
		return cast;
	}

	public void setCast(String cast) {
		this.cast = cast;
	}

	public String getTransferCertificate() {
		return transferCertificate;
	}

	public void setTransferCertificate(String transferCertificate) {
		this.transferCertificate = transferCertificate;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getAdmissionClass() {
		return admissionClass;
	}
	public void setAdmissionClass(String admissionClass) {
		this.admissionClass = admissionClass;
	}
	public String getFatherName() {
		return fatherName;
	}
	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}
	public String getMotherName() {
		return motherName;
	}
	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

    
    
    public String getRazorpayOrderId() {
		return razorpayOrderId;
	}


	public void setRazorpayOrderId(String razorpayOrderId) {
		this.razorpayOrderId = razorpayOrderId;
	}


	public String getRazorpayPaymentId() {
		return razorpayPaymentId;
	}


	public void setRazorpayPaymentId(String razorpayPaymentId) {
		this.razorpayPaymentId = razorpayPaymentId;
	}


	public String getRazorpaySignature() {
		return razorpaySignature;
	}


	public void setRazorpaySignature(String razorpaySignature) {
		this.razorpaySignature = razorpaySignature;
	}


	public BigDecimal getAdmissionFee() {
		return admissionFee;
	}


	public void setAdmissionFee(BigDecimal admissionFee) {
		this.admissionFee = admissionFee;
	}


	public BigDecimal getRegistrationFee() {
		return registrationFee;
	}


	public void setRegistrationFee(BigDecimal registrationFee) {
		this.registrationFee = registrationFee;
	}


	public BigDecimal getTotalFee() {
		return totalFee;
	}


	public void setTotalFee(BigDecimal totalFee) {
		this.totalFee = totalFee;
	}


	public String getPaymentStatus() {
		return paymentStatus;
	}


	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}


	public Instant getPaymentDate() {
		return paymentDate;
	}


	public void setPaymentDate(Instant paymentDate) {
		this.paymentDate = paymentDate;
	}
    
}
