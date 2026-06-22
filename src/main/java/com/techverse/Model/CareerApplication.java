package com.techverse.Model;

 
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

 
@Entity
@Table(name = "career_applications")
public class CareerApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    private String email;

    private String position;

    @Column(length = 1000)
    private String resume;

    private Instant createdAt;

    public CareerApplication() {
    }

    public CareerApplication(String fullName, String email, String position, String resume) {
        this.fullName = fullName;
        this.email = email;
        this.position = position;
        this.resume = resume;
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPosition() {
        return position;
    }

    public String getResume() {
        return resume;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}