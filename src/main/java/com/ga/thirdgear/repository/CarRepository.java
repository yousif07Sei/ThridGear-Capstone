package com.ga.thirdgear.repository;

import com.ga.thirdgear.model.Car;
import com.ga.thirdgear.enums.CarStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Car entity operations.
 */
@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    List<Car> findByUserId(Long userId);

    List<Car> findByCategoryId(Long categoryId);

    List<Car> findByStatus(CarStatus status);

    List<Car> findByMakeIgnoreCase(String make);
}