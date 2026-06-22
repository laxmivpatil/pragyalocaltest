package com.techverse.Repository;

 
import org.springframework.data.jpa.repository.JpaRepository;

import com.techverse.Model.CareerApplication;


public interface CareerApplicationRepository extends JpaRepository<CareerApplication, Long> {
}