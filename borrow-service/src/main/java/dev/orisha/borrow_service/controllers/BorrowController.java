package dev.orisha.borrow_service.controllers;

import dev.orisha.borrow_service.services.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/borrow")
public class BorrowController {
    private final BorrowService borrowService;

    @Autowired
    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @PostMapping("/{bookId}")
    public ResponseEntity<?> borrowBook(@PathVariable Long bookId, Principal principal,
                                        @RequestHeader("Authorization") String token) {
        return ResponseEntity.status(CREATED)
                .body(borrowService.borrowBook(bookId, token, principal.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> returnBook(@RequestHeader("Authorization") String token,
                                        @PathVariable Long id) {
        return ResponseEntity.ok(borrowService.returnBook(id, token));
    }

}

