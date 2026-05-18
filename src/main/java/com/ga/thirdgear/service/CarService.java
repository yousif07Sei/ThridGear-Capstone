package com.ga.thirdgear.service;

import com.ga.thirdgear.enums.CarStatus;
import com.ga.thirdgear.exception.InformationNotFoundException;
import com.ga.thirdgear.model.Car;
import com.ga.thirdgear.model.User;
import com.ga.thirdgear.repository.CarRepository;
import com.ga.thirdgear.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {

    private CarRepository carRepository;
    private UserRepository userRepository;

    @Autowired
    public void setCarRepository(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Car getCarById(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new InformationNotFoundException("Car not found with id: " + id));
    }

    public List<Car> getCarsByUserId(Long userId) {
        return carRepository.findByUserId(userId);
    }

    public List<Car> getCarsByCategoryId(Long categoryId) {
        return carRepository.findByCategoryId(categoryId);
    }

    public List<Car> getCarsByStatus(CarStatus status) {
        return carRepository.findByStatus(status);
    }

    public Car createCar(Car car, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new InformationNotFoundException("User not found"));
        car.setUser(user);
        car.setStatus(CarStatus.AVAILABLE);
        return carRepository.save(car);
    }

    public Car updateCar(Long id, Car updatedCar, String userEmail) {
        Car existing = getCarById(id);

        // Make sure only the owner can update
        if (!existing.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("You are not authorized to update this listing");
        }

        existing.setTitle(updatedCar.getTitle());
        existing.setMake(updatedCar.getMake());
        existing.setModel(updatedCar.getModel());
        existing.setYear(updatedCar.getYear());
        existing.setMileage(updatedCar.getMileage());
        existing.setPrice(updatedCar.getPrice());
        existing.setLocation(updatedCar.getLocation());
        existing.setDescription(updatedCar.getDescription());
        existing.setCondition(updatedCar.getCondition());
        existing.setCategory(updatedCar.getCategory());
        return carRepository.save(existing);
    }

    public void deleteCar(Long id, String userEmail) {
        Car existing = getCarById(id);

        // Make sure only the owner can delete
        if (!existing.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("You are not authorized to delete this listing");
        }

        carRepository.delete(existing);
    }
}