package dev.orisha.borrow_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BorrowDto {
    private Long id;
    private String title;
    private String author;
    private String isbn;
}
