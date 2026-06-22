package com.techverse.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.techverse.Model.AdmissionEnquiry;

public interface AdmissionEnquiryRepository extends JpaRepository<AdmissionEnquiry, Long> {
}