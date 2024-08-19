package dev.orisha.borrow_service.services.utils;

import dev.orisha.borrow_service.dto.BookDto;
import dev.orisha.borrow_service.dto.responses.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@FeignClient(name = "book-service", path = "/books")
public interface BookServiceClient {

    @GetMapping("/{bookId}")
    ApiResponse<BookDto> getBookById(@RequestHeader(AUTHORIZATION) String token,
                                     @PathVariable("bookId") Long bookId);
}
