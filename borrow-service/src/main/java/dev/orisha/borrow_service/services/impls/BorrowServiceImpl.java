package dev.orisha.borrow_service.services.impls;

import dev.orisha.book_service.data.models.Book;
import dev.orisha.book_service.data.repositories.BookRepository;
import dev.orisha.book_service.dto.BookDto;
import dev.orisha.book_service.dto.responses.ApiResponse;
import dev.orisha.book_service.exceptions.ResourceNotFoundException;
import dev.orisha.book_service.services.BookService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.time.LocalDateTime.now;

@Service
@Slf4j
public class BorrowServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public BorrowServiceImpl(BookRepository bookRepository, ModelMapper modelMapper) {
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ApiResponse<BookDto> addBook(BookDto bookDto) {
        bookDto.setId(null);
        Book newBook = modelMapper.map(bookDto, Book.class);
        newBook = bookRepository.save(newBook);
        bookDto = modelMapper.map(newBook, BookDto.class);
        return new ApiResponse<>(now(), true, bookDto);
    }

    @Override
    public ApiResponse<BookDto> updateBook(BookDto bookDto) {
        Book book = findBookBy(bookDto.getId());
        modelMapper.map(bookDto, book);
        book = bookRepository.save(book);
        bookDto = modelMapper.map(book, BookDto.class);
        return new ApiResponse<>(now(), true, bookDto);
    }

    @Override
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) throw new ResourceNotFoundException("Book with id " + id + " not found");
        bookRepository.deleteById(id);
    }

    @Override
    public ApiResponse<BookDto> getBookBy(Long id) {
        BookDto bookDto = modelMapper.map(findBookBy(id), BookDto.class);
        return new ApiResponse<>(now(), true, bookDto);
    }

    @Override
    public ApiResponse<List<BookDto>> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        List<BookDto> bookDtos = books.stream()
                                        .map(book -> modelMapper.map(book, BookDto.class))
                                        .toList();
        return new ApiResponse<>(now(), true, bookDtos);
    }

    private Book findBookBy(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id " + id));
    }
}
