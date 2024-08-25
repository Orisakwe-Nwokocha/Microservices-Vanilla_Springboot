package dev.orisha.borrow_service.services.utils;

import dev.orisha.borrow_service.dto.BookDto;
import dev.orisha.borrow_service.dto.responses.ApiResponse;
import dev.orisha.borrow_service.services.utils.config.FeignClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "book-service", path = "/books", configuration = FeignClientConfiguration.class)
public interface BookServiceClient {

    @GetMapping("/{bookId}")
    ApiResponse<BookDto> getBookById(@PathVariable("bookId") Long bookId);
}
