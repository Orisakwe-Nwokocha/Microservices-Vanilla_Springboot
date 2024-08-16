package dev.orisha.borrow_service.services;

import dev.orisha.borrow_service.dto.responses.ApiResponse;
import dev.orisha.borrow_service.dto.responses.BorrowBookResponse;
import dev.orisha.borrow_service.data.models.Borrow;
import dev.orisha.borrow_service.data.repositories.BorrowRepository;
import dev.orisha.borrow_service.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class BorrowServiceTest {

    @Autowired
    private BorrowService borrowService;

    @Autowired
    private BorrowRepository borrowRepository;

    @BeforeEach
    void setUp() {
        borrowRepository.deleteAll();
    }


    @Test
    void testReturnBook() {
        // Given
        Borrow borrow = new Borrow();
        borrow.setIsReturned(false);
        borrow.setReturnedAt(null);
        borrow = borrowRepository.save(borrow);

        // When
        ApiResponse<BorrowBookResponse> response = borrowService.returnBook(borrow.getId());

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getData()).isNotNull();
        assertThat(response.getData().getIsReturned()).isTrue();
        assertThat(response.getData().getReturnedAt()).isNotNull();
    }

    @Test
    void testReturnBookWithNonExistingId_throwsResourceNotFoundException() {
        // Given
        Long borrowId = 999L;

        // When / Then
        assertThatThrownBy(() -> borrowService.returnBook(borrowId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Borrow record not found");
    }

    @Test
    void testFindAllBorrowRecordsFor() {
        // Given
        String email = "user@example.com";
        saveMultipleBorrowEntities(email);

        // When
        ApiResponse<List<BorrowBookResponse>> response = borrowService.findAllBorrowRecordsFor(email);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getData()).isNotNull();
        assertThat(response.isSuccessful()).isTrue();
        assertThat(response.getData()).hasSize(2);
        assertThat(response.getData().get(0).getEmail()).isEqualTo(email);
        assertThat(response.getData().get(1).getEmail()).isEqualTo(email);
    }

    @Test
    void testFindAllBorrowRecordsForWithNoRecords() {
        // Given
        String email = "user@example.com";

        // When
        ApiResponse<List<BorrowBookResponse>> response = borrowService.findAllBorrowRecordsFor(email);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.isSuccessful()).isTrue();
        assertThat(response.getData()).isEmpty();
    }

    private void saveMultipleBorrowEntities(String email) {
        Borrow borrow1 = new Borrow();
        borrow1.setEmail(email);
        borrow1.setBookId(1L);
        borrow1.setIsReturned(false);

        Borrow borrow2 = new Borrow();
        borrow2.setEmail(email);
        borrow2.setBookId(2L);
        borrow2.setIsReturned(false);
        borrowRepository.saveAll(List.of(borrow1, borrow2));
    }
}
