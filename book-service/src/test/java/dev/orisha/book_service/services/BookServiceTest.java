package dev.orisha.book_service.services;

import dev.orisha.book_service.data.models.Book;
import dev.orisha.book_service.data.repositories.BookRepository;
import dev.orisha.book_service.dto.BookDto;
import dev.orisha.book_service.dto.responses.ApiResponse;
import dev.orisha.book_service.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class BookServiceTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BookService bookService;


    @BeforeEach
    void setUp() {
        bookRepository.deleteAll(); // Clean up database before each test
    }

    @Test
    void testAddBook() {
        // Given
        BookDto bookDto = new BookDto();
        bookDto.setTitle("Book Title");
        bookDto.setAuthor("Author Name");

        // When
        ApiResponse<BookDto> response = bookService.addBook(bookDto);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.isSuccessful()).isTrue();
        assertThat(response.getData()).isNotNull();
        assertThat(response.getData().getTitle()).isEqualTo("Book Title");
        assertThat(response.getData().getAuthor()).isEqualTo("Author Name");
    }

    @Test
    void testAddBookWithNullBookDto_ThrowsNullPointerException() {
        // Given
        BookDto bookDto = null;

        // When / Then
        assertThatThrownBy(() -> bookService.addBook(bookDto))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testUpdateBook() {
        // Given
        Book book = new Book();
        book.setTitle("Old Title");
        book = bookRepository.save(book);

        BookDto bookDto = modelMapper.map(book, BookDto.class);
        bookDto.setTitle("Updated Title");

        // When
        ApiResponse<BookDto> response = bookService.updateBook(bookDto);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.isSuccessful()).isTrue();
        assertThat(response.getData()).isNotNull();
        assertThat(response.getData().getTitle()).isEqualTo("Updated Title");
    }

    @Test
    void testUpdateBookWithNonExistingId_ThrowsResourceNotFoundException() {
        // Given
        BookDto bookDto = new BookDto();
        bookDto.setId(999L);

        // When / Then
        assertThatThrownBy(() -> bookService.updateBook(bookDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Book not found with id " + bookDto.getId());
    }

    @Test
    void testDeleteBook() {
        // Given
        Book book = new Book();
        book.setTitle("Book Title");
        book = bookRepository.save(book);

        // When
        bookService.deleteBook(book.getId());

        // Then
        assertThat(bookRepository.findById(book.getId())).isEmpty();
    }

    @Test
    void testDeleteBookWithNonExistingId_throwsResourceNotFoundException() {
        // Given
        Long bookId = 999L;

        // When / Then
        assertThatThrownBy(() -> bookService.deleteBook(bookId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Book with id " + bookId + " not found");
    }

    @Test
    void testGetBookBy() {
        // Given
        Book book = new Book();
        book.setTitle("Book Title");
        book = bookRepository.save(book);

        // When
        ApiResponse<BookDto> response = bookService.getBookBy(book.getId());

        // Then
        assertThat(response).isNotNull();
        assertThat(response.isSuccessful()).isTrue();
        assertThat(response.getData()).isNotNull();
        assertThat(response.getData().getTitle()).isEqualTo("Book Title");
    }

    @Test
    void testGetBookByWithNonExistingId_throwsResourceNotFoundException() {
        // Given
        Long bookId = 999L;

        // When / Then
        assertThatThrownBy(() -> bookService.getBookBy(bookId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Book not found with id " + bookId);
    }

    @Test
    void testGetAllBooks() {
        // Given
        assertThat(bookRepository.findAll()).hasSize(0);
        Book book1 = new Book();
        book1.setTitle("Book 1");
        Book book2 = new Book();
        book2.setTitle("Book 2");
        bookRepository.saveAll(List.of(book1, book2));

        // When
        ApiResponse<List<BookDto>> response = bookService.getAllBooks();

        // Then
        assertThat(response).isNotNull();
        assertThat(response.isSuccessful()).isTrue();
        assertThat(response.getData()).hasSize(2);
        assertThat(bookRepository.findAll()).hasSize(2);
    }
}
