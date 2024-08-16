package dev.orisha.book_service.data.models;


import dev.orisha.book_service.data.constants.Genre;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static java.time.LocalDateTime.now;

@Entity
@Table(name = "books")
@Getter
@Setter
public class Book {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String title;
    private String author;
    @Column(unique = true)
    private String isbn;
    @Enumerated(STRING)
    private Genre genre;

    @Setter(AccessLevel.NONE)
    private LocalDateTime dateCreated;


    @PrePersist
    private void setDateCreated() {
        dateCreated = now();
    }
}

