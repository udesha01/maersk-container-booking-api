package com.example.demo.model;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Data
public class BookingRequest {
    @Min(value = 20, message = "Container size must be 20")
    @Max(value = 40, message = "Container size must be 40")
    private Integer containerSize;

    private ContainerType containerType;

    @Size(min = 5, max = 20, message = "Origin must be between 5 and 20 characters")
    private String origin;

    @Size(min = 5, max = 20, message = "Destination must be between 5 and 20 characters")
    private String destination;

    @Min(value = 1, message = "Quantity must be at least 1")
    @Max(value = 100, message = "Quantity cannot exceed 100")
    private Integer quantity;
}