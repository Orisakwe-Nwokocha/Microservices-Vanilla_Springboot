package dev.orisha.borrow_service.dto;

import dev.orisha.borrow_service.data.constants.Genre;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BookDto {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private Genre genre;
}
