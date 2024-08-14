package dev.orisha.borrow_service.services;

import dev.orisha.borrow_service.dto.responses.ApiResponse;
import dev.orisha.borrow_service.dto.responses.BorrowBookResponse;

import java.util.List;

public interface BorrowService {
    ApiResponse<BorrowBookResponse> borrowBook(Long bookId, String token, String email);
    ApiResponse<BorrowBookResponse> returnBook(Long id);
    ApiResponse<List<BorrowBookResponse>> findAllBorrowedBooksFor(String email);
}
