package com.example.demo.controller;

import com.example.demo.external.ExternalService;
import com.example.demo.model.AvailabilityResponse;
import com.example.demo.model.BookingReferenceResponse;
import com.example.demo.model.BookingRequest;
import com.example.demo.model.ContainerType;
import com.example.demo.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class BookingControllerTest {

    @Mock
    private ExternalService externalService;

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testValidBookingRequestWithAvailableSpace() {
        BookingRequest validRequest = createValidBookingRequest();

        when(externalService.checkAvailableSpace()).thenReturn(Mono.just(6));

        StepVerifier.create(bookingController.bookContainer(validRequest))
                .expectNextMatches(responseEntity -> responseEntity.getStatusCode() == HttpStatus.OK &&
                        responseEntity.getBody() instanceof AvailabilityResponse &&
                        ((AvailabilityResponse) responseEntity.getBody()).isAvailable())
                .verifyComplete();
    }

    @Test
    void testValidBookingRequestWithZeroAvailableSpace() {
        BookingRequest validRequest = createValidBookingRequest();

        when(externalService.checkAvailableSpace()).thenReturn(Mono.just(0));

        StepVerifier.create(bookingController.bookContainer(validRequest))
                .expectNextMatches(responseEntity -> responseEntity.getStatusCode() == HttpStatus.OK &&
                        responseEntity.getBody() instanceof AvailabilityResponse &&
                        !((AvailabilityResponse) responseEntity.getBody()).isAvailable())
                .verifyComplete();
    }

    @Test
    void testInvalidBookingRequest() {
        BookingRequest invalidRequest = createInvalidBookingRequest();

        StepVerifier.create(bookingController.bookContainer(invalidRequest))
                .expectNextMatches(responseEntity -> responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST)
                .verifyComplete();
    }


    @Test
    void testInvalidContainerSize() {
        BookingRequest invalidRequest = createValidBookingRequest();
        invalidRequest.setContainerSize(30);

        StepVerifier.create(bookingController.saveBooking(invalidRequest))
                .expectNextMatches(responseEntity -> responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST &&
                        responseEntity.getBody().equals("Invalid container size"))
                .verifyComplete();
    }

    @Test
    void testInvalidContainerType() {
        BookingRequest invalidRequest = createValidBookingRequest();
        invalidRequest.setContainerType(null);

        assertThrows(RuntimeException.class, () -> {
            bookingController.saveBooking(invalidRequest).block();
        });
    }

    private BookingRequest createValidBookingRequest() {
        BookingRequest request = new BookingRequest();
        request.setContainerSize(20);
        request.setContainerType(ContainerType.DRY);
        request.setOrigin("Southampton");
        request.setDestination("Singapore");
        request.setQuantity(5);
        return request;
    }

    private BookingRequest createInvalidBookingRequest() {
        BookingRequest request = new BookingRequest();
        request.setContainerSize(50);
        request.setContainerType(ContainerType.DRY);
        request.setOrigin("Sout");
        request.setDestination("Sing");
        request.setQuantity(0);
        return request;
    }
}
