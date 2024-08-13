package dev.orisha.gateway.controllers;

import dev.orisha.gateway.dto.responses.ApiResponse;
import dev.orisha.gateway.dto.responses.BorrowBookResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static dev.orisha.gateway.controllers.utils.ControllerUtils.getHttpEntity;
import static dev.orisha.gateway.controllers.utils.ControllerUtils.getServiceInstances;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

@RestController
@RequestMapping("/client/borrow-app")
@Slf4j
public class BorrowClientController {

    public static final String BORROW_SERVICE = "borrow-service";
    private final RestTemplate restTemplate;
    private final DiscoveryClient discoveryClient;


    @Autowired
    public BorrowClientController(RestTemplate restTemplate, DiscoveryClient discoveryClient) {
        this.restTemplate = restTemplate;
        this.discoveryClient = discoveryClient;
    }


    @PostMapping("/{bookId}")
    public ResponseEntity<?> borrow(@RequestHeader(AUTHORIZATION) String token, @PathVariable String bookId) {
        log.info("Trying to borrow book with id: {} from service: {}", bookId, BORROW_SERVICE);
        return getResponseEntity(token, bookId, POST);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> returnBook(@RequestHeader(AUTHORIZATION) String token,
                                                                    @PathVariable String id) {
        log.info("Returning book with id {}", id);
        return getResponseEntity(token, id, PUT);
    }


    private ResponseEntity<?> getResponseEntity(@RequestHeader(AUTHORIZATION) String token,
                                                    @PathVariable String id, HttpMethod method) {
        List<ServiceInstance> instances = getServiceInstances(discoveryClient, BORROW_SERVICE);
        if (instances.isEmpty()) return errorResponse();

        String baseUrl = instances.getFirst().getUri().toString();
        String url = baseUrl + "/borrow/" + id;
        HttpEntity<String> entity = getHttpEntity(token);
        ParameterizedTypeReference<ApiResponse<BorrowBookResponse>> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<ApiResponse<BorrowBookResponse>> response = restTemplate.exchange(url, method, entity, responseType);

        log.info("Response successfully returned from service: {}", BORROW_SERVICE);
        return ResponseEntity.ok(response.getBody());
    }

    private static ResponseEntity<Object> errorResponse() {
        log.error("No instances found for service: {}", BORROW_SERVICE);
        return ResponseEntity.status(SERVICE_UNAVAILABLE).build();
    }


}
