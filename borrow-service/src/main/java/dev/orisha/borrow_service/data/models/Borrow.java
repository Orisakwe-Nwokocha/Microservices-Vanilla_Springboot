package dev.orisha.borrow_service.data.models;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;
import static java.lang.Boolean.FALSE;
import static java.time.LocalDateTime.now;

@Entity
@Table(name = "book_loans")
@Getter
@Setter
public class Borrow {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String email;

    private Long bookId;

    private Boolean isReturned = FALSE;

    @Setter(AccessLevel.NONE)
    private LocalDateTime borrowedAt;

    private LocalDateTime returnedAt;


    @PrePersist
    private void setDateCreated() {
        borrowedAt = now();
    }
}

