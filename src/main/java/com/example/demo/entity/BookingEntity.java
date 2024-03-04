package com.example.demo.entity;

import com.example.demo.model.BookingRequest;
import com.example.demo.model.ContainerType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Data
@Document(collection = "bookings")
public class BookingEntity {
    @Id
    private String id;

    private Integer containerSize;
    private ContainerType containerType;
    private String origin;
    private String destination;
    private Integer quantity;
    private String timestamp;

    public BookingEntity(BookingRequest request) {
        this.containerSize = request.getContainerSize();
        this.containerType = request.getContainerType();
        this.origin = request.getOrigin();
        this.destination = request.getDestination();
        this.quantity = request.getQuantity();
        this.timestamp = generateTimestamp();
    }
    private String generateTimestamp() {
        return Instant.now().atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
    }
}

