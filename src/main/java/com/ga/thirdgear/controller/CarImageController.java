package com.ga.thirdgear.controller;

import com.ga.thirdgear.model.CarImage;
import com.ga.thirdgear.service.CarImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/cars/{carId}/images")
public class CarImageController {

    private CarImageService carImageService;

    @Autowired
    public void setCarImageService(CarImageService carImageService) {
        this.carImageService = carImageService;
    }

    @GetMapping
    public ResponseEntity<List<CarImage>> getImages(@PathVariable Long carId) {
        return ResponseEntity.ok(carImageService.getImagesByCarId(carId));
    }

    @PostMapping
    public ResponseEntity<List<CarImage>> uploadImages(
            @PathVariable Long carId,
            @RequestParam("files") List<MultipartFile> files) throws IOException {
        return ResponseEntity.ok(carImageService.uploadImages(carId, files));
    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> deleteImage(
            @PathVariable Long carId,
            @PathVariable Long imageId) {
        carImageService.deleteImage(imageId);
        return ResponseEntity.noContent().build();
    }
}