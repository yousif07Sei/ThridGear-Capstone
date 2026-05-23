package com.ga.thirdgear.service;

import com.ga.thirdgear.enums.CarCondition;
import com.ga.thirdgear.enums.CarStatus;
import com.ga.thirdgear.enums.InquiryStatus;
import com.ga.thirdgear.exception.InformationNotFoundException;
import com.ga.thirdgear.model.Car;
import com.ga.thirdgear.model.Inquiry;
import com.ga.thirdgear.model.User;
import com.ga.thirdgear.repository.CarRepository;
import com.ga.thirdgear.repository.InquiryRepository;
import com.ga.thirdgear.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InquiryServiceTest {

    @Mock
    private InquiryRepository inquiryRepository;

    @Mock
    private CarRepository carRepository;

    @Mock
    private UserRepository userRepository;

    private InquiryService inquiryService;

    @BeforeEach
    void setUp() {
        inquiryService = new InquiryService();
        inquiryService.setInquiryRepository(inquiryRepository);
        inquiryService.setCarRepository(carRepository);
        inquiryService.setUserRepository(userRepository);
    }

    @Test
    @DisplayName("Should create inquiry successfully")
    void shouldCreateInquiry() {
        User buyer = User.builder().id(1L).email("bob@test.com").build();
        Car car = Car.builder().id(1L).title("Nissan 240Z").make("Nissan").model("240Z")
                .year(1970).mileage(85000).price(25000.0).location("Kuwait City")
                .condition(CarCondition.EXCELLENT).status(CarStatus.AVAILABLE).build();

        Inquiry inquiry = Inquiry.builder().message("Is it available?").build();

        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(userRepository.findByEmail("bob@test.com")).thenReturn(Optional.of(buyer));
        when(inquiryRepository.save(any(Inquiry.class))).thenAnswer(i -> i.getArgument(0));

        Inquiry created = inquiryService.createInquiry(1L, inquiry, "bob@test.com");

        assertThat(created.getStatus()).isEqualTo(InquiryStatus.OPEN);
        assertThat(created.getBuyer()).isEqualTo(buyer);
        assertThat(created.getCar()).isEqualTo(car);
        verify(inquiryRepository).save(any(Inquiry.class));
    }

    @Test
    @DisplayName("Should throw exception when car not found for inquiry")
    void shouldThrowExceptionWhenCarNotFound() {
        when(carRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> inquiryService.createInquiry(99L, new Inquiry(), "bob@test.com"))
                .isInstanceOf(InformationNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("Should delete inquiry successfully")
    void shouldDeleteInquiry() {
        User buyer = User.builder().id(1L).email("bob@test.com").build();
        Car car = Car.builder().id(1L).build();
        Inquiry inquiry = Inquiry.builder().id(1L).message("Is it available?")
                .status(InquiryStatus.OPEN).buyer(buyer).car(car).build();

        when(inquiryRepository.findById(1L)).thenReturn(Optional.of(inquiry));
        doNothing().when(inquiryRepository).delete(inquiry);

        inquiryService.deleteInquiry(1L, "bob@test.com");

        verify(inquiryRepository).delete(inquiry);
    }

    @Test
    @DisplayName("Should throw exception when deleting inquiry of another user")
    void shouldThrowExceptionWhenDeletingOtherUserInquiry() {
        User buyer = User.builder().id(1L).email("bob@test.com").build();
        Inquiry inquiry = Inquiry.builder().id(1L).message("Is it available?")
                .status(InquiryStatus.OPEN).buyer(buyer).build();

        when(inquiryRepository.findById(1L)).thenReturn(Optional.of(inquiry));

        assertThatThrownBy(() -> inquiryService.deleteInquiry(1L, "alice@test.com"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("You are not authorized to delete this inquiry");
    }
}