package com.controlcash.app.controllers.transaction;

import com.controlcash.app.dtos.transaction.request.TransactionCreateRequestDTO;
import com.controlcash.app.dtos.transaction.response.TransactionCreateResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface ITransactionController {
    ResponseEntity<TransactionCreateResponseDTO> create(TransactionCreateRequestDTO transactionCreateRequestDTO);
    ResponseEntity<Page<TransactionCreateResponseDTO>> findAll(int page, int size, String sort);
}
