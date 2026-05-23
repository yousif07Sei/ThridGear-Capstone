package com.ga.thirdgear.seeder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MainSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(MainSeeder.class);

    private UserSeeder userSeeder;
    private CategorySeeder categorySeeder;
    private CarSeeder carSeeder;
    private InquirySeeder inquirySeeder;

    @Autowired
    public void setUserSeeder(UserSeeder userSeeder) { this.userSeeder = userSeeder; }
    @Autowired
    public void setCategorySeeder(CategorySeeder categorySeeder) { this.categorySeeder = categorySeeder; }
    @Autowired
    public void setCarSeeder(CarSeeder carSeeder) { this.carSeeder = carSeeder; }
    @Autowired
    public void setInquirySeeder(InquirySeeder inquirySeeder) { this.inquirySeeder = inquirySeeder; }

    @Override
    public void run(String... args) {
        logger.info("🌱 Starting data seeding...");
        categorySeeder.seed();
        userSeeder.seed();
        carSeeder.seed();
        inquirySeeder.seed();
        logger.info("✅ Data seeding completed!");
    }
}