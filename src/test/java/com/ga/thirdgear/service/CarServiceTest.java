package com.ga.thirdgear.service;

import com.ga.thirdgear.enums.CarCondition;
import com.ga.thirdgear.enums.CarStatus;
import com.ga.thirdgear.exception.InformationNotFoundException;
import com.ga.thirdgear.model.Car;
import com.ga.thirdgear.model.User;
import com.ga.thirdgear.repository.CarRepository;
import com.ga.thirdgear.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private UserRepository userRepository;

    private CarService carService;

    @BeforeEach
    void setUp() {
        carService = new CarService();
        carService.setCarRepository(carRepository);
        carService.setUserRepository(userRepository);
    }

    @Test
    @DisplayName("Should return all cars")
    void shouldReturnAllCars() {
        Car car1 = Car.builder().title("Nissan 240Z").make("Nissan").model("240Z")
                .year(1970).mileage(85000).price(25000.0).location("Kuwait City")
                .condition(CarCondition.EXCELLENT).status(CarStatus.AVAILABLE).build();

        Car car2 = Car.builder().title("Mazda RX-8").make("Mazda").model("RX-8")
                .year(2005).mileage(60000).price(12000.0).location("Manama")
                .condition(CarCondition.GOOD).status(CarStatus.AVAILABLE).build();

        when(carRepository.findAll()).thenReturn(List.of(car1, car2));

        List<Car> cars = carService.getAllCars();

        assertThat(cars).hasSize(2);
        assertThat(cars.get(0).getTitle()).isEqualTo("Nissan 240Z");
        verify(carRepository).findAll();
    }

    @Test
    @DisplayName("Should return car by id")
    void shouldReturnCarById() {
        Car car = Car.builder().title("Toyota Supra").make("Toyota").model("Supra")
                .year(1998).mileage(120000).price(45000.0).location("Riyadh")
                .condition(CarCondition.GOOD).status(CarStatus.AVAILABLE).build();

        when(carRepository.findById(1L)).thenReturn(Optional.of(car));

        Car found = carService.getCarById(1L);

        assertThat(found.getTitle()).isEqualTo("Toyota Supra");
        verify(carRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when car not found")
    void shouldThrowExceptionWhenCarNotFound() {
        when(carRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> carService.getCarById(99L))
                .isInstanceOf(InformationNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("Should create car successfully")
    void shouldCreateCar() {
        User user = User.builder().email("alice@test.com").build();
        Car car = Car.builder().title("Honda Civic").make("Honda").model("Civic")
                .year(2010).mileage(95000).price(8000.0).location("Dubai")
                .condition(CarCondition.MODRATE).status(CarStatus.AVAILABLE).build();

        when(userRepository.findByEmail("alice@test.com")).thenReturn(Optional.of(user));
        when(carRepository.save(any(Car.class))).thenReturn(car);

        Car created = carService.createCar(car, "alice@test.com");

        assertThat(created.getTitle()).isEqualTo("Honda Civic");
        verify(carRepository).save(any(Car.class));
    }

    @Test
    @DisplayName("Should delete car successfully")
    void shouldDeleteCar() {
        User user = User.builder().email("alice@test.com").build();
        Car car = Car.builder().title("Honda Civic").make("Honda").model("Civic")
                .year(2010).mileage(95000).price(8000.0).location("Dubai")
                .condition(CarCondition.MODRATE).status(CarStatus.AVAILABLE)
                .user(user).build();

        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        doNothing().when(carRepository).delete(car);

        carService.deleteCar(1L, "alice@test.com");

        verify(carRepository).delete(car);
    }
}