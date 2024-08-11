package dev.orisha.borrow_service.services;

import dev.orisha.book_service.dto.BookDto;
import dev.orisha.book_service.dto.responses.ApiResponse;

import java.util.List;

public interface BorrowService {
    ApiResponse<BookDto> addBook(BookDto bookDto);
    ApiResponse<BookDto> updateBook(BookDto bookDto);
    void deleteBook(Long id);
    ApiResponse<BookDto> getBookBy(Long id);
    ApiResponse<List<BookDto>> getAllBooks();
}
