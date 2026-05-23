package com.ga.thirdgear.seeder;

import com.ga.thirdgear.enums.CarCondition;
import com.ga.thirdgear.enums.CarStatus;
import com.ga.thirdgear.model.Car;
import com.ga.thirdgear.model.Category;
import com.ga.thirdgear.model.User;
import com.ga.thirdgear.repository.CarRepository;
import com.ga.thirdgear.repository.CategoryRepository;
import com.ga.thirdgear.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

@Component
public class CarSeeder {

    private static final Logger logger = LoggerFactory.getLogger(CarSeeder.class);

    private CarRepository carRepository;
    private UserRepository userRepository;
    private CategoryRepository categoryRepository;

    @Autowired
    public void setCarRepository(CarRepository carRepository) { this.carRepository = carRepository; }
    @Autowired
    public void setUserRepository(UserRepository userRepository) { this.userRepository = userRepository; }
    @Autowired
    public void setCategoryRepository(CategoryRepository categoryRepository) { this.categoryRepository = categoryRepository; }

    public void seed() {
        if (carRepository.count() > 0) return;

        User alice = userRepository.findByEmail("alice@test.com").orElseThrow();
        User bob = userRepository.findByEmail("bob@test.com").orElseThrow();
        Category classic = categoryRepository.findByName("Classic").orElseThrow();
        Category sports = categoryRepository.findByName("Sports Car").orElseThrow();
        Category sedan = categoryRepository.findByName("Sedan").orElseThrow();

        carRepository.save(Car.builder()
                .title("1970 Nissan 240Z").make("Nissan").model("240Z").year(1970)
                .mileage(85000).price(25000.0).location("Kuwait City")
                .description("A beautifully restored classic Nissan 240Z")
                .condition(CarCondition.EXCELLENT).status(CarStatus.AVAILABLE)
                .user(alice).category(classic).build());

        carRepository.save(Car.builder()
                .title("2005 Mazda RX-8").make("Mazda").model("RX-8").year(2005)
                .mileage(60000).price(12000.0).location("Manama")
                .description("Rotary engine in great condition, 6-speed manual")
                .condition(CarCondition.GOOD).status(CarStatus.AVAILABLE)
                .user(alice).category(sports).build());

        carRepository.save(Car.builder()
                .title("1998 Toyota Supra").make("Toyota").model("Supra").year(1998)
                .mileage(120000).price(45000.0).location("Riyadh")
                .description("Iconic JDM sports car, 5-speed manual")
                .condition(CarCondition.GOOD).status(CarStatus.AVAILABLE)
                .user(bob).category(sports).build());

        carRepository.save(Car.builder()
                .title("2010 Honda Civic Si").make("Honda").model("Civic Si").year(2010)
                .mileage(95000).price(8000.0).location("Dubai")
                .description("Fun daily driver with 6-speed manual")
                .condition(CarCondition.MODRATE).status(CarStatus.AVAILABLE)
                .user(bob).category(sedan).build());

        logger.info("✅ Cars seeded!");
    }
}