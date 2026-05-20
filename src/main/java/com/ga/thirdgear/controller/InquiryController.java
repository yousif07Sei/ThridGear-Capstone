package com.ga.thirdgear.controller;

import com.ga.thirdgear.enums.InquiryStatus;
import com.ga.thirdgear.model.Inquiry;
import com.ga.thirdgear.service.InquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inquiries")
public class InquiryController {

    private InquiryService inquiryService;

    @Autowired
    public void setInquiryService(InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }

    @GetMapping("/car/{carId}")
    public ResponseEntity<List<Inquiry>> getInquiriesByCarId(@PathVariable Long carId) {
        return ResponseEntity.ok(inquiryService.getInquiriesByCarId(carId));
    }

    @GetMapping("/my")
    public ResponseEntity<List<Inquiry>> getMyInquiries(Authentication authentication) {
        return ResponseEntity.ok(inquiryService.getMyInquiries(authentication.getName()));
    }

    @PostMapping("/car/{carId}")
    public ResponseEntity<Inquiry> createInquiry(@PathVariable Long carId,
                                                 @RequestBody Inquiry inquiry,
                                                 Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(inquiryService.createInquiry(carId, inquiry, authentication.getName()));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Inquiry> updateInquiryStatus(@PathVariable Long id,
                                                       @RequestParam InquiryStatus status,
                                                       Authentication authentication) {
        return ResponseEntity.ok(inquiryService.updateInquiryStatus(id, status, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInquiry(@PathVariable Long id, Authentication authentication) {
        inquiryService.deleteInquiry(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}