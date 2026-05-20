package com.ga.thirdgear.repository;

import com.ga.thirdgear.model.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    List<Inquiry> findByCarId(Long carId);

    List<Inquiry> findByBuyerId(Long buyerId);
}