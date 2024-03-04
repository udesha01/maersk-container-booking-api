package com.example.demo.service;

import com.example.demo.entity.BookingEntity;
import com.example.demo.model.BookingReferenceResponse;
import com.example.demo.model.BookingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class BookingService {
 @Autowired
 private MongoTemplate mongoTemplate;
    private static String bookingRefCounter = "957000000";

    public Mono<BookingReferenceResponse> saveBooking(BookingRequest request) {
       BookingEntity bookingEntity = new BookingEntity(request);
        bookingEntity.setId(String.valueOf(getBookingRef(bookingRefCounter)));
        BookingReferenceResponse bookingReferenceResponse = new BookingReferenceResponse(mongoTemplate.insert(bookingEntity).getId());
        return Mono.just(bookingReferenceResponse);
    }

    public int getBookingRef(String id) {
        int numericId = Integer.parseInt(id) +1;
        return  numericId;
    }
}

