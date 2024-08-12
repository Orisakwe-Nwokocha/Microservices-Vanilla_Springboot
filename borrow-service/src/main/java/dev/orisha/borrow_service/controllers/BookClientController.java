//package dev.orisha.borrow_service.controllers;
//
//import dev.orisha.gateway.dto.BookDto;
//import dev.orisha.gateway.dto.responses.ApiResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.client.ServiceInstance;
//import org.springframework.cloud.client.discovery.DiscoveryClient;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.List;
//
//import static dev.orisha.gateway.controllers.utils.ControllerUtils.getHttpEntity;
//import static dev.orisha.gateway.controllers.utils.ControllerUtils.getServiceInstances;
//import static org.springframework.http.HttpMethod.GET;
//import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;
//
//@RestController
//@RequestMapping("/client/book-app")
//@Slf4j
//public class BookClientController {
//
//    private final RestTemplate restTemplate;
//    private final DiscoveryClient discoveryClient;
//
//
//    @Autowired
//    public BookClientController(RestTemplate restTemplate, DiscoveryClient discoveryClient) {
//        this.restTemplate = restTemplate;
//        this.discoveryClient = discoveryClient;
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<?> getBooks(@RequestHeader("Authorization") String token, @PathVariable String id) {
//        log.info("Getting book with id {}", id);
//
//        List<ServiceInstance> instances = getServiceInstances(discoveryClient);
//        if (instances.isEmpty()) return ResponseEntity.status(SERVICE_UNAVAILABLE).build();
//        String baseUrl = instances.getFirst().getUri().toString();
//        String url = baseUrl + "/books/" + id;
//
//        HttpEntity<String> entity = getHttpEntity(token);
//        ParameterizedTypeReference<ApiResponse<BookDto>> responseType = new ParameterizedTypeReference<>() {};
//        ResponseEntity<ApiResponse<BookDto>> response = restTemplate.exchange(url, GET, entity, responseType);
//
//        return ResponseEntity.ok(response.getBody());
//    }
//
//    @GetMapping()
//    public ResponseEntity<?> getBooks(@RequestHeader("Authorization") String token) {
//        log.info("Getting books");
//
//        List<ServiceInstance> instances = getServiceInstances(discoveryClient);
//        if (instances.isEmpty()) return ResponseEntity.status(SERVICE_UNAVAILABLE).build();
//        String baseUrl = instances.getFirst().getUri().toString();
//        String url = baseUrl + "/books";
//
//        HttpEntity<String> entity = getHttpEntity(token);
//        ParameterizedTypeReference<ApiResponse<List<BookDto>>> responseType = new ParameterizedTypeReference<>() {};
//        ResponseEntity<ApiResponse<List<BookDto>>> response = restTemplate.exchange(url, GET, entity, responseType);
//
//        return ResponseEntity.ok(response.getBody());
//    }
//
//
//}
