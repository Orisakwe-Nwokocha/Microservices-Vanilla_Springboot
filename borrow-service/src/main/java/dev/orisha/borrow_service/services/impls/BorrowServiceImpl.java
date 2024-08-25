package dev.orisha.borrow_service.services.impls;

import dev.orisha.borrow_service.data.models.Borrow;
import dev.orisha.borrow_service.data.repositories.BorrowRepository;
import dev.orisha.borrow_service.dto.BookDto;
import dev.orisha.borrow_service.dto.responses.ApiResponse;
import dev.orisha.borrow_service.dto.responses.BorrowBookResponse;
import dev.orisha.borrow_service.exceptions.ResourceNotFoundException;
import dev.orisha.borrow_service.services.BorrowService;
import dev.orisha.borrow_service.services.utils.BookServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

import static dev.orisha.borrow_service.services.utils.BorrowServiceUtils.fetchBookFromBookService;
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
    private final BookServiceClient bookServiceClient;


    @Override
    public ApiResponse<BorrowBookResponse> borrowBook(Long bookId, String token, String email) {
        log.info("Borrow book request by user: {}", email);
//        BookDto bookDto = getBookFromBookService(token, bookId);
        BookDto bookDto = fetchBookWithFeignClient(bookId);
        Borrow borrow = saveBorrowEntity(bookId, email);
        BorrowBookResponse response = buildResponse(borrow, bookDto);
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
    public ApiResponse<List<BorrowBookResponse>> findAllBorrowRecordsFor(String email) {
        log.info("Request to retrieve all borrowed books for : {}", email);
        var borrowedBooks = borrowRepository.findAllBorrowedBooksFor(email);
        List<BorrowBookResponse> response = List.of(modelMapper.map(borrowedBooks, BorrowBookResponse[].class));
        log.info("Borrowed records for user : {} retrieved successfully : {}", email, response);
        return new ApiResponse<>(now(), true, response);
    }

    private Borrow updateBorrowEntity(Long id) {
        Borrow borrow = borrowRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Borrow record not found"));
        borrow.setIsReturned(TRUE);
        borrow.setReturnedAt(now());
        return borrowRepository.save(borrow);
    }

    private BorrowBookResponse buildResponse(Borrow borrow, BookDto bookDto) {
        BorrowBookResponse borrowBookResponse = modelMapper.map(borrow, BorrowBookResponse.class);
        borrowBookResponse.setBook(bookDto);
        return borrowBookResponse;
    }

    private Borrow saveBorrowEntity(Long bookId, String email) {
        Borrow borrow = new Borrow();
        borrow.setBookId(bookId);
        borrow.setEmail(email);
        return borrowRepository.save(borrow);
    }

    private BookDto fetchBookWithFeignClient(Long bookId) {
        log.info("Fetching book from book-service with id : {}", bookId);
        ApiResponse<BookDto> response = bookServiceClient.getBookById(bookId);
        log.info("Book fetched successfully via feign client : {}", response);
        return response.getData();
    }

    private BookDto getBookFromBookService(String token, Long bookId) {
        log.info("Fetching book from book-service");
        ResponseEntity<ApiResponse<BookDto>> response =
                            fetchBookFromBookService(discoveryClient, restTemplate, token, bookId);
        log.info("Fetched book successfully");
        return Objects.requireNonNull(response.getBody()).getData();
    }
}
