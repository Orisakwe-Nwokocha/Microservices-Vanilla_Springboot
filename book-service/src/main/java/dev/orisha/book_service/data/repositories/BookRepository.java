package dev.orisha.book_service.data.repositories;

import dev.orisha.book_service.data.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
