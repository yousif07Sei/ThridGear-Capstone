package com.ga.thirdgear.service;

import com.ga.thirdgear.enums.InquiryStatus;
import com.ga.thirdgear.exception.InformationNotFoundException;
import com.ga.thirdgear.model.Car;
import com.ga.thirdgear.model.Inquiry;
import com.ga.thirdgear.model.User;
import com.ga.thirdgear.repository.CarRepository;
import com.ga.thirdgear.repository.InquiryRepository;
import com.ga.thirdgear.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InquiryService {

    private InquiryRepository inquiryRepository;
    private CarRepository carRepository;
    private UserRepository userRepository;

    @Autowired
    public void setInquiryRepository(InquiryRepository inquiryRepository) {
        this.inquiryRepository = inquiryRepository;
    }

    @Autowired
    public void setCarRepository(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<Inquiry> getInquiriesByCarId(Long carId) {
        return inquiryRepository.findByCarId(carId);
    }

    public List<Inquiry> getMyInquiries(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new InformationNotFoundException("User not found"));
        return inquiryRepository.findByBuyerId(user.getId());
    }

    public Inquiry createInquiry(Long carId, Inquiry inquiry, String userEmail) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new InformationNotFoundException("Car not found with id: " + carId));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new InformationNotFoundException("User not found"));

        inquiry.setCar(car);
        inquiry.setBuyer(user);
        inquiry.setStatus(InquiryStatus.OPEN);
        return inquiryRepository.save(inquiry);
    }

    public Inquiry updateInquiryStatus(Long id, InquiryStatus status, String userEmail) {
        Inquiry inquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new InformationNotFoundException("Inquiry not found with id: " + id));

        // Only the buyer or car owner can update the status
        if (!inquiry.getBuyer().getEmail().equals(userEmail) &&
                !inquiry.getCar().getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("You are not authorized to update this inquiry");
        }

        inquiry.setStatus(status);
        return inquiryRepository.save(inquiry);
    }

    public void deleteInquiry(Long id, String userEmail) {
        Inquiry inquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new InformationNotFoundException("Inquiry not found with id: " + id));

        // Only the buyer can delete their inquiry
        if (!inquiry.getBuyer().getEmail().equals(userEmail)) {
            throw new RuntimeException("You are not authorized to delete this inquiry");
        }

        inquiryRepository.delete(inquiry);
    }
}