package dev.orisha.borrow_service.services.impls;


import dev.orisha.borrow_service.data.repositories.BorrowRepository;
import dev.orisha.borrow_service.dto.BorrowDto;
import dev.orisha.borrow_service.dto.responses.ApiResponse;
import dev.orisha.borrow_service.services.BorrowService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BorrowServiceImpl implements BorrowService {

    private final BorrowRepository borrowRepository;
    private final ModelMapper modelMapper;

    @Override
    public ApiResponse<BorrowDto> borrowBook(BorrowDto bookDto) {
        return null;
    }

    @Override
    public ApiResponse<BorrowDto> returnBook(BorrowDto bookDto) {
        return null;
    }

    @Override
    public ApiResponse<BorrowDto> getBookBy(Long id) {
        return null;
    }

    @Override
    public ApiResponse<List<BorrowDto>> getAllBooks() {
        return null;
    }
}
