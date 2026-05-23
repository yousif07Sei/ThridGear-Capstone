package com.ga.thirdgear.seeder;

import com.ga.thirdgear.enums.InquiryStatus;
import com.ga.thirdgear.model.Car;
import com.ga.thirdgear.model.Inquiry;
import com.ga.thirdgear.model.User;
import com.ga.thirdgear.repository.CarRepository;
import com.ga.thirdgear.repository.InquiryRepository;
import com.ga.thirdgear.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

@Component
public class InquirySeeder {

    private static final Logger logger = LoggerFactory.getLogger(InquirySeeder.class);

    private InquiryRepository inquiryRepository;
    private UserRepository userRepository;
    private CarRepository carRepository;

    @Autowired
    public void setInquiryRepository(InquiryRepository inquiryRepository) { this.inquiryRepository = inquiryRepository; }
    @Autowired
    public void setUserRepository(UserRepository userRepository) { this.userRepository = userRepository; }
    @Autowired
    public void setCarRepository(CarRepository carRepository) { this.carRepository = carRepository; }

    public void seed() {
        if (inquiryRepository.count() > 0) return;

        User alice = userRepository.findByEmail("alice@test.com").orElseThrow();
        User bob = userRepository.findByEmail("bob@test.com").orElseThrow();
        Car car1 = carRepository.findById(1L).orElseThrow();
        Car car2 = carRepository.findById(2L).orElseThrow();
        Car car3 = carRepository.findById(3L).orElseThrow();

        inquiryRepository.save(Inquiry.builder()
                .message("Is the 240Z still available? Can we meet this weekend?")
                .status(InquiryStatus.OPEN).buyer(bob).car(car1).build());

        inquiryRepository.save(Inquiry.builder()
                .message("What is the lowest price you'd accept for the RX-8?")
                .status(InquiryStatus.OPEN).buyer(bob).car(car2).build());

        inquiryRepository.save(Inquiry.builder()
                .message("Does the Supra have any modifications?")
                .status(InquiryStatus.OPEN).buyer(alice).car(car3).build());

        logger.info("✅ Inquiries seeded!");
    }
}