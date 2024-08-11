package dev.orisha.borrow_service.services;

import dev.orisha.borrow_service.dto.BorrowDto;
import dev.orisha.borrow_service.dto.responses.ApiResponse;

import java.util.List;

public interface BorrowService {
    ApiResponse<BorrowDto> borrowBook(BorrowDto bookDto);
    ApiResponse<BorrowDto> returnBook(BorrowDto bookDto);
    ApiResponse<BorrowDto> getBookBy(Long id);
    ApiResponse<List<BorrowDto>> getAllBooks();
}
