package dev.orisha.borrow_service.services.impls;


import dev.orisha.borrow_service.data.models.Borrow;
import dev.orisha.borrow_service.data.repositories.BorrowRepository;
import dev.orisha.borrow_service.dto.BookDto;
import dev.orisha.borrow_service.dto.responses.ApiResponse;
import dev.orisha.borrow_service.dto.responses.BorrowBookResponse;
import dev.orisha.borrow_service.services.BorrowService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

import static dev.orisha.borrow_service.services.utils.ServicesUtils.getHttpEntity;
import static dev.orisha.borrow_service.services.utils.ServicesUtils.getServiceInstances;
import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpMethod.GET;

@Service
@AllArgsConstructor
@Slf4j
public class BorrowServiceImpl implements BorrowService {

    private final BorrowRepository borrowRepository;
    private final ModelMapper modelMapper;
    private final RestTemplate restTemplate;
    private final DiscoveryClient discoveryClient;
    private static final String BOOK_APP_URL = "/client/book-app/";

    @Override
    public ApiResponse<BorrowBookResponse> borrowBook(Long bookId, String token, String email) {
        log.info("Borrow book request by user: {}", email);
        ResponseEntity<ApiResponse<BookDto>> response = getBookDtoFromGateway(token, bookId);
        log.info("Retrieved book response from gateway");
        BookDto bookDto = Objects.requireNonNull(response.getBody()).getData();
        Borrow borrow = createAndSaveBorrowEntity(bookId, email);
        BorrowBookResponse borrowBookResponse = modelMapper.map(borrow, BorrowBookResponse.class);
        borrowBookResponse.setBook(bookDto);
        log.info("Borrow book response: {}", borrowBookResponse);
        log.info("Book borrowed successfully");
        return new ApiResponse<>(now(), true, borrowBookResponse);
    }

    @Override
    public ApiResponse<BorrowBookResponse> returnBook(Long id, String token) {
        return null;
    }

    private Borrow createAndSaveBorrowEntity(Long bookId, String email) {
        Borrow borrow = new Borrow();
        borrow.setBookId(bookId);
        borrow.setEmail(email);
        borrow = borrowRepository.save(borrow);
        return borrow;
    }

    private ResponseEntity<ApiResponse<BookDto>> getBookDtoFromGateway(String token, Long bookId) {
        List<ServiceInstance> instances = getServiceInstances(discoveryClient, "gateway");
        if (instances.isEmpty()) throw new IllegalStateException("No instances of gateway found");
        String baseUrl = instances.getFirst().getUri().toString();
        String url = baseUrl + BOOK_APP_URL + bookId;

        HttpEntity<String> entity = getHttpEntity(token);
        ParameterizedTypeReference<ApiResponse<BookDto>> responseType = new ParameterizedTypeReference<>() {};
        return restTemplate.exchange(url, GET, entity, responseType);
    }

}
