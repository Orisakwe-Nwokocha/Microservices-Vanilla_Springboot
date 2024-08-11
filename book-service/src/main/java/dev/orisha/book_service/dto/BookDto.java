package dev.orisha.book_service.dto;

import dev.orisha.book_service.data.constants.Genre;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDto {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private Genre genre;
}
