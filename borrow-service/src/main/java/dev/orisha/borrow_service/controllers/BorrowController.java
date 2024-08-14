package dev.orisha.borrow_service.controllers;

import dev.orisha.borrow_service.services.BorrowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/borrow")
@Slf4j
public class BorrowController {
    private final BorrowService borrowService;

    @Autowired
    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @PostMapping()
    public ResponseEntity<?> borrowBook(@RequestParam Long book_id, Principal principal,
                                        @RequestHeader("Authorization") String token) {
        log.debug("REST request to borrow a book : {}", book_id);
        return ResponseEntity.status(CREATED)
                .body(borrowService.borrowBook(book_id, token, principal.getName()));
    }

    @PatchMapping
    public ResponseEntity<?> returnBook(@RequestParam Long borrow_id) {
        log.debug("REST request to return a book : {}", borrow_id);
        return ResponseEntity.ok(borrowService.returnBook(borrow_id));
    }

    @GetMapping
    public ResponseEntity<?> getBorrowedBooks(Principal principal) {
        log.debug("REST request to get all books");
        return ResponseEntity.ok(borrowService.findAllBorrowedBooksFor(principal.getName()));
    }

}

