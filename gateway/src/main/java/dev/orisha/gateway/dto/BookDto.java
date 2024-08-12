package dev.orisha.gateway.dto;

import dev.orisha.gateway.data.constants.Genre;
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
