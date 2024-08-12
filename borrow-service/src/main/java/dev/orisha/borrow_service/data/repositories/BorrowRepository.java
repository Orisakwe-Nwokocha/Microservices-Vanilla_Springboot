package dev.orisha.borrow_service.data.repositories;

import dev.orisha.borrow_service.data.models.Borrow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowRepository extends JpaRepository<Borrow, Long> {
}
