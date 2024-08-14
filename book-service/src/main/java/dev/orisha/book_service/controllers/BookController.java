package dev.orisha.book_service.controllers;

import dev.orisha.book_service.dto.BookDto;
import dev.orisha.book_service.services.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/books")
@Slf4j
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<?> addBook(@RequestBody BookDto bookDto) {
        log.debug("REST request to save Book : {}", bookDto);
        return ResponseEntity.status(CREATED).body(bookService.addBook(bookDto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateBook(@RequestBody BookDto bookDto, @PathVariable Long id) {
        log.debug("REST request to update Book : {}", id);
        bookDto.setId(id);
        return ResponseEntity.ok(bookService.updateBook(bookDto));
    }

    @GetMapping
    public ResponseEntity<?> getAllBooks() {
        log.debug("REST request to get all Books");
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBook(@PathVariable Long id) {
        log.debug("REST request to get Book : {}", id);
        return ResponseEntity.ok(bookService.getBookBy(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        log.debug("REST request to delete Book : {}", id);
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}

