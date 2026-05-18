package com.ga.thirdgear.controller;

import com.ga.thirdgear.enums.CarStatus;
import com.ga.thirdgear.model.Car;
import com.ga.thirdgear.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    private CarService carService;

    @Autowired
    public void setCarService(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public ResponseEntity<List<Car>> getAllCars() {
        return ResponseEntity.ok(carService.getAllCars());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        return ResponseEntity.ok(carService.getCarById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Car>> getCarsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(carService.getCarsByUserId(userId));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Car>> getCarsByCategoryId(@PathVariable Long categoryId) {
        return ResponseEntity.ok(carService.getCarsByCategoryId(categoryId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Car>> getCarsByStatus(@PathVariable CarStatus status) {
        return ResponseEntity.ok(carService.getCarsByStatus(status));
    }

    @PostMapping
    public ResponseEntity<Car> createCar(@RequestBody Car car, Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(carService.createCar(car, authentication.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Car> updateCar(@PathVariable Long id,
                                         @RequestBody Car car,
                                         Authentication authentication) {
        return ResponseEntity.ok(carService.updateCar(id, car, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id, Authentication authentication) {
        carService.deleteCar(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}