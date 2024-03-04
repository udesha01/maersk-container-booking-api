Maersk Container Booking API

The Maersk Container Booking API provides endpoints for checking available space for booking containers and saving bookings with Maersk.

Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

Prerequisites

Java 11 or above

Maven

MongoDB (if running locally)

Installing

Clone the repository:

bash

git clone <repository-url>

Navigate to the project directory:

bash

cd maersk-container-booking-api

Build the project using Maven:

Copy code

mvn clean install

Run the application:

Copy code

mvn spring-boot:run

The application will start on port 8080 by default.

Endpoints

Check Available Space for Booking:

Endpoint: POST /api/bookings/checkAvailable

Request Body: BookingRequest

Response: AvailabilityResponse

Save Booking:

Endpoint: POST /api/bookings/saveBooking

Request Body: BookingRequest

Response: BookingReferenceResponse

Request and Response Models

BookingRequest
json


{
"containerSize": 20,
"containerType": "DRY",
"origin": "Southampton",
"destination": "Singapore",
"quantity": 5,
"timestamp": "2020-10-12T13:53:09Z"
}

AvailabilityResponse
json

{
"available": true
}

BookingReferenceResponse
json


{
"bookingRef": "957000001"
}

Built With

Spring Boot

Maven


MongoDB

OpenAPI Specification (OAS)

Authors

Udesha