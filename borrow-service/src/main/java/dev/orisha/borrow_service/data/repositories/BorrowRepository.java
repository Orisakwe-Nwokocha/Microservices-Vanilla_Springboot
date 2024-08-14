package dev.orisha.borrow_service.data.repositories;

import dev.orisha.borrow_service.data.models.Borrow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BorrowRepository extends JpaRepository<Borrow, Long> {
    @Query("SELECT b FROM Borrow b WHERE b.email=:email AND b.returnedAt IS NULL")
    List<Borrow> findAllBorrowedBooksFor(String email);
}
