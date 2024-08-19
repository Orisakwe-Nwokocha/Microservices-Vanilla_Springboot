package dev.orisha.borrow_service.services.utils;

import dev.orisha.borrow_service.dto.BookDto;
import dev.orisha.borrow_service.dto.responses.ApiResponse;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpMethod.GET;

public class BorrowServiceUtils {

    private BorrowServiceUtils() {}

    private static final String BOOK_APP_BASE_PATH = "/books/";

    public static ResponseEntity<ApiResponse<BookDto>> fetchBookFromBookService(DiscoveryClient discoveryClient,
                                                                                RestTemplate restTemplate,
                                                                                String token, Long bookId) {
        String url = getRouteForBookService(discoveryClient, bookId);
        HttpEntity<String> entity = getHttpEntity(token);
        ParameterizedTypeReference<ApiResponse<BookDto>> responseType = new ParameterizedTypeReference<>() {};
        return restTemplate.exchange(url, GET, entity, responseType);
    }

    private static String getRouteForBookService(DiscoveryClient discoveryClient, Long bookId) {
        List<ServiceInstance> instances = discoveryClient.getInstances("book-service");
        if (instances.isEmpty()) throw new IllegalStateException("No instances of book service found");
        String baseUrl = instances.getFirst().getUri().toString();
        return baseUrl + BOOK_APP_BASE_PATH + bookId;
    }

    private static HttpEntity<String> getHttpEntity(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTHORIZATION, token);
        return new HttpEntity<>(headers);
    }

    /** USING WEBCLIENT INSTEAD OF REST TEMPLATE
     *
     *     public static ApiResponse<BookDto> fetchBookFromGateway(DiscoveryClient discoveryClient,
     *                                                             WebClient.Builder webClientBuilder,
     *                                                             String token, Long bookId) {
     *         String url = getRouteForBookService(discoveryClient, bookId);
     *
     *         return webClientBuilder.build()
     *                 .get()
     *                 .uri(url)
     *                 .header(AUTHORIZATION, token)
     *                 .retrieve()
     *                 .bodyToMono(new ParameterizedTypeReference<ApiResponse<BookDto>>() {})
     *                 .block(); // Blocking for simplicity; consider handling this asynchronously
     *     }
     *
     *     private static String getRouteForBookService(DiscoveryClient discoveryClient, Long bookId) {
     *         List<ServiceInstance> instances = discoveryClient.getInstances("gateway-reactive");
     *         if (instances.isEmpty()) throw new IllegalStateException("No instances of gateway found");
     *         String baseUrl = instances.get(0).getUri().toString();  // Fixed to use get(0) instead of getFirst()
     *         return baseUrl + BOOK_APP_URL + bookId;
     *     }*/
}
