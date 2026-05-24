package com.ga.thirdgear.service;

import com.ga.thirdgear.model.Car;
import com.ga.thirdgear.model.CarImage;
import com.ga.thirdgear.repository.CarImageRepository;
import com.ga.thirdgear.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CarImageService {

    private CarImageRepository carImageRepository;
    private CarRepository carRepository;

    @Autowired
    public void setCarImageRepository(CarImageRepository carImageRepository) {
        this.carImageRepository = carImageRepository;
    }

    @Autowired
    public void setCarRepository(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public List<CarImage> getImagesByCarId(Long carId) {
        return carImageRepository.findByCar_Id(carId);
    }

    public List<CarImage> uploadImages(Long carId, List<MultipartFile> files) throws IOException {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Car not found"));

        String uploadDir = "uploads/cars/";
        Files.createDirectories(Paths.get(uploadDir));

        List<CarImage> savedImages = new ArrayList<>();

        for (MultipartFile file : files) {
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            CarImage image = CarImage.builder()
                    .imageUrl(uploadDir + filename)
                    .car(car)
                    .build();

            savedImages.add(carImageRepository.save(image));
        }

        return savedImages;
    }

    public void deleteImage(Long imageId) {
        CarImage image = carImageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        try {
            Files.deleteIfExists(Paths.get(image.getImageUrl()));
        } catch (IOException e) {
            // log but don't block deletion
        }

        carImageRepository.delete(image);
    }
}