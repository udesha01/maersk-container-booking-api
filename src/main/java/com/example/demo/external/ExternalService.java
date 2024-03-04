package com.example.demo.external;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ExternalService {

    private final WebClient webClient;

    public ExternalService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://maersk.com/api/bookings").build();
    }

    public Mono<Integer> checkAvailableSpace() {
        return webClient.get().uri("/checkAvailable")
                .retrieve()
                .bodyToMono(Response.class)
                .map(Response::getAvailableSpace);
    }

    private static class Response {
        private Integer availableSpace;

        public Integer getAvailableSpace() {
            return availableSpace;
        }

        public void setAvailableSpace(Integer availableSpace) {
            this.availableSpace = availableSpace;
        }
    }
}