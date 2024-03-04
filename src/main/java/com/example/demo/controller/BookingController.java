package com.example.demo.controller;

import com.example.demo.external.ExternalService;
import com.example.demo.model.AvailabilityResponse;
import com.example.demo.model.BookingReferenceResponse;
import com.example.demo.model.BookingRequest;
import com.example.demo.model.ContainerType;
import com.example.demo.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/bookings")
public class BookingController {
    @Autowired
    private ExternalService externalService;
    @Autowired
    private BookingService bookingService;


    @PostMapping("/availability")
    public Mono<ResponseEntity<?>> bookContainer(@Valid @RequestBody BookingRequest request) {
        if (request.getContainerSize() != 20 && request.getContainerSize() != 40) {
            return Mono.just(ResponseEntity.badRequest().body("Invalid container size"));
        }
        if (!request.getContainerType().equals(ContainerType.DRY) && !request.getContainerType().equals(ContainerType.REEFER)) {
            return Mono.just(ResponseEntity.badRequest().body("Invalid container type"));
        }
        if (request.getOrigin().length() < 5  || request.getOrigin().length() > 20) {
            return Mono.just(ResponseEntity.badRequest().body("Invalid origin "));
        }
        if (request.getDestination().length() < 5  || request.getDestination().length() > 20) {
            return Mono.just(ResponseEntity.badRequest().body("Invalid destination "));
        }
        if (request.getQuantity() < 1  || request.getQuantity() > 100) {
            return Mono.just(ResponseEntity.badRequest().body("Invalid Quantity "));
        }

        return externalService.checkAvailableSpace()
                .map(availableSpace -> {
                    if (availableSpace == 0) {
                        return ResponseEntity.ok().body(new AvailabilityResponse(false));
                    } else {
                        return ResponseEntity.ok().body(new AvailabilityResponse(true));
                    }
                });
    }

    @PostMapping("/savebooking")
    public Mono<ResponseEntity<?>> saveBooking(@Valid @RequestBody BookingRequest request) {
        try {
            if (request.getContainerSize() != 20 && request.getContainerSize() != 40) {
                return Mono.just(ResponseEntity.badRequest().body("Invalid container size"));
            }
            if (!request.getContainerType().equals(ContainerType.DRY) && !request.getContainerType().equals(ContainerType.REEFER)) {
                return Mono.just(ResponseEntity.badRequest().body("Invalid container type"));
            }
            if (request.getOrigin().length() < 5  || request.getOrigin().length() > 20) {
                return Mono.just(ResponseEntity.badRequest().body("Invalid origin "));
            }
            if (request.getDestination().length() < 5  || request.getDestination().length() > 20) {
                return Mono.just(ResponseEntity.badRequest().body("Invalid destination "));
            }
            if (request.getQuantity() < 1  || request.getQuantity() > 100) {
                return Mono.just(ResponseEntity.badRequest().body("Invalid Quantity "));
            }

            return Mono.just(ResponseEntity.ok().body(bookingService.saveBooking(request)));
        } catch (Exception e) {
            throw new RuntimeException("Sorry there was a problem processing your request");
        }
    }
}

