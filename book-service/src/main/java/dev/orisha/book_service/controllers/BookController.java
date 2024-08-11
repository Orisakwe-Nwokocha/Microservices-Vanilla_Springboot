package dev.orisha.book_service.controllers;

import dev.orisha.book_service.dto.BookDto;
import dev.orisha.book_service.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<?> addBook(@RequestBody BookDto bookDto) {
        return ResponseEntity.status(CREATED).body(bookService.addBook(bookDto));
    }

    @PatchMapping
    public ResponseEntity<?> updateBook(@RequestBody BookDto bookDto) {
        return ResponseEntity.ok(bookService.updateBook(bookDto));
    }

    @GetMapping
    public ResponseEntity<?> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBook(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookBy(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}

