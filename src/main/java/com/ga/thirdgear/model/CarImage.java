package com.ga.thirdgear.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Entity
@Table(name = "car_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Image URL is required")
    @Column(nullable = false)
    private String imageUrl;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

}
