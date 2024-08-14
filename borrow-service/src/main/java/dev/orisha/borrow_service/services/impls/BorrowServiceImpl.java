package dev.orisha.borrow_service.services.impls;

import dev.orisha.borrow_service.data.models.Borrow;
import dev.orisha.borrow_service.data.repositories.BorrowRepository;
import dev.orisha.borrow_service.dto.BookDto;
import dev.orisha.borrow_service.dto.responses.ApiResponse;
import dev.orisha.borrow_service.dto.responses.BorrowBookResponse;
import dev.orisha.borrow_service.exceptions.ResourceNotFoundException;
import dev.orisha.borrow_service.services.BorrowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

import static dev.orisha.borrow_service.services.utils.BorrowServiceUtils.buildResponse;
import static dev.orisha.borrow_service.services.utils.BorrowServiceUtils.fetchBookFromGateway;
import static java.lang.Boolean.TRUE;
import static java.time.LocalDateTime.now;

@Service
@RequiredArgsConstructor
@Slf4j
public class BorrowServiceImpl implements BorrowService {

    private final BorrowRepository borrowRepository;
    private final ModelMapper modelMapper;
    private final RestTemplate restTemplate;
    private final DiscoveryClient discoveryClient;


    @Override
    public ApiResponse<BorrowBookResponse> borrowBook(Long bookId, String token, String email) {
        log.info("Borrow book request by user: {}", email);
        BookDto bookDto = getBookDetailsFromGateway(token, bookId);
        Borrow borrow = saveBorrowEntity(bookId, email);
        BorrowBookResponse response = buildResponse(borrow, bookDto, modelMapper);
        log.info("Book borrowed successfully : {}", response);
        return new ApiResponse<>(now(), true, response);
    }

    @Override
    public ApiResponse<BorrowBookResponse> returnBook(Long id) {
        log.info("Return book request for borrow id : {}", id);
        Borrow borrow = updateBorrowEntity(id);
        BorrowBookResponse response = modelMapper.map(borrow, BorrowBookResponse.class);
        log.info("Book returned successfully : {}", response);
        return new ApiResponse<>(now(), true, response);
    }

    @Override
    public ApiResponse<List<BorrowBookResponse>> findAllBorrowedBooksFor(String email) {
        log.info("Request to retrieve all borrowed books for : {}", email);
        var borrowedBooks = borrowRepository.findAllBorrowedBooksFor(email);
        List<BorrowBookResponse> response = List.of(modelMapper.map(borrowedBooks, BorrowBookResponse[].class));
        log.info("Borrowed books for user : {} retrieved successfully : {}", email, response);
        return new ApiResponse<>(now(), true, response);
    }

    private Borrow updateBorrowEntity(Long id) {
        Borrow borrow = borrowRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book loan details not found"));
        borrow.setIsReturned(TRUE);
        borrow.setReturnedAt(now());
        return borrowRepository.save(borrow);
    }

    private Borrow saveBorrowEntity(Long bookId, String email) {
        Borrow borrow = new Borrow();
        borrow.setBookId(bookId);
        borrow.setEmail(email);
        return borrowRepository.save(borrow);
    }

    private BookDto getBookDetailsFromGateway(String token, Long bookId) {
        log.info("Fetching book from book-service via gateway");
        ResponseEntity<ApiResponse<BookDto>> response = fetchBookFromGateway(discoveryClient, restTemplate, token, bookId);
        log.info("Fetched book successfully");
        return Objects.requireNonNull(response.getBody()).getData();
    }
}
