package dev.orisha.gateway.controllers;

import dev.orisha.gateway.dto.BookDto;
import dev.orisha.gateway.dto.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static dev.orisha.gateway.controllers.utils.ControllerUtils.getHttpEntity;
import static dev.orisha.gateway.controllers.utils.ControllerUtils.getServiceInstances;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

@RestController
@RequestMapping("/client/book-app")
@Slf4j
public class BookClientController {

    public static final String BOOK_SERVICE = "book-service";
    private final RestTemplate restTemplate;
    private final DiscoveryClient discoveryClient;

    @Autowired
    public BookClientController(RestTemplate restTemplate, DiscoveryClient discoveryClient) {
        this.restTemplate = restTemplate;
        this.discoveryClient = discoveryClient;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBooks(@RequestHeader(AUTHORIZATION) String token, @PathVariable String id) {
        log.info("Getting book with id: {} from service: {}", id, BOOK_SERVICE);

        List<ServiceInstance> instances = getServiceInstances(discoveryClient, BOOK_SERVICE);
        if (instances.isEmpty()) return errorResponse();
        String baseUrl = instances.getFirst().getUri().toString();
        String url = baseUrl + "/books/" + id;

        HttpEntity<String> entity = getHttpEntity(token);
        ParameterizedTypeReference<ApiResponse<BookDto>> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<ApiResponse<BookDto>> response = restTemplate.exchange(url, GET, entity, responseType);

        log.info("Book found with id: {} from service: {}", id, BOOK_SERVICE);
        return ResponseEntity.ok(response.getBody());
    }

    @GetMapping()
    public ResponseEntity<?> getBooks(@RequestHeader(AUTHORIZATION) String token) {
        log.info("Getting books from service: {}", BOOK_SERVICE);

        List<ServiceInstance> instances = getServiceInstances(discoveryClient, BOOK_SERVICE);
        if (instances.isEmpty()) return errorResponse();

        String baseUrl = instances.getFirst().getUri().toString();
        String url = baseUrl + "/books";
        HttpEntity<String> entity = getHttpEntity(token);
        ParameterizedTypeReference<ApiResponse<List<BookDto>>> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<ApiResponse<List<BookDto>>> response = restTemplate.exchange(url, GET, entity, responseType);

        log.info("Books found from service: {}", BOOK_SERVICE);
        return ResponseEntity.ok(response.getBody());
    }

    private static ResponseEntity<Object> errorResponse() {
        log.error("No instances found for service: {}", BOOK_SERVICE);
        return ResponseEntity.status(SERVICE_UNAVAILABLE).build();
    }

}
