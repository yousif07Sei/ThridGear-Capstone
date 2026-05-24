package com.ga.thirdgear.repository;

import com.ga.thirdgear.model.CarImage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CarImageRepository extends JpaRepository<CarImage, Long> {
    List<CarImage> findByCar_Id(Long carId);
}