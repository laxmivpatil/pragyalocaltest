package com.techverse.Model;

 

import java.time.Instant;

import javax.persistence.*;

@Entity
@Table(name = "admission_enquiries")
public class AdmissionEnquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String boardingType; // HOSTEL, DAY_BOARDING, BOTH

    private String studentFullName;

    private String studentDateOfBirth;

    private String gender;

    private String gradeApplyingFor;

    private String parentGuardianFullName;

    private String relationshipToStudent;

    private String contactNumber;

    private String city;

    private String email;

    @Column(length = 2000)
    private String comments;

    private Boolean otpVerified;

    private String status; // RECEIVED, IN_PROGRESS, CLOSED

    private Instant createdAt;

    public AdmissionEnquiry() {
    }

    public AdmissionEnquiry(
            String boardingType,
            String studentFullName,
            String studentDateOfBirth,
            String gender,
            String gradeApplyingFor,
            String parentGuardianFullName,
            String relationshipToStudent,
            String contactNumber,
            String city,
            String email,
            String comments,
            Boolean otpVerified,
            String status
    ) {
        this.boardingType = boardingType;
        this.studentFullName = studentFullName;
        this.studentDateOfBirth = studentDateOfBirth;
        this.gender = gender;
        this.gradeApplyingFor = gradeApplyingFor;
        this.parentGuardianFullName = parentGuardianFullName;
        this.relationshipToStudent = relationshipToStudent;
        this.contactNumber = contactNumber;
        this.city = city;
        this.email = email;
        this.comments = comments;
        this.otpVerified = otpVerified;
        this.status = status;
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public String getBoardingType() {
        return boardingType;
    }

    public String getStudentFullName() {
        return studentFullName;
    }

    public String getStudentDateOfBirth() {
        return studentDateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public String getGradeApplyingFor() {
        return gradeApplyingFor;
    }

    public String getParentGuardianFullName() {
        return parentGuardianFullName;
    }

    public String getRelationshipToStudent() {
        return relationshipToStudent;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getCity() {
        return city;
    }

    public String getEmail() {
        return email;
    }

    public String getComments() {
        return comments;
    }

    public Boolean getOtpVerified() {
        return otpVerified;
    }

    public String getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBoardingType(String boardingType) {
        this.boardingType = boardingType;
    }

    public void setStudentFullName(String studentFullName) {
        this.studentFullName = studentFullName;
    }

    public void setStudentDateOfBirth(String studentDateOfBirth) {
        this.studentDateOfBirth = studentDateOfBirth;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setGradeApplyingFor(String gradeApplyingFor) {
        this.gradeApplyingFor = gradeApplyingFor;
    }

    public void setParentGuardianFullName(String parentGuardianFullName) {
        this.parentGuardianFullName = parentGuardianFullName;
    }

    public void setRelationshipToStudent(String relationshipToStudent) {
        this.relationshipToStudent = relationshipToStudent;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setOtpVerified(Boolean otpVerified) {
        this.otpVerified = otpVerified;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}