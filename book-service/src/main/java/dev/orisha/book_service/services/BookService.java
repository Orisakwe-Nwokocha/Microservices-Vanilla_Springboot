package dev.orisha.book_service.services;

import dev.orisha.book_service.dto.BookDto;
import dev.orisha.book_service.dto.responses.ApiResponse;

import java.util.List;

public interface BookService {
    ApiResponse<BookDto> addBook(BookDto bookDto);
    ApiResponse<BookDto> updateBook(BookDto bookDto);
    void deleteBook(Long id);
    ApiResponse<BookDto> getBookBy(Long id);
    ApiResponse<List<BookDto>> getAllBooks();
}
