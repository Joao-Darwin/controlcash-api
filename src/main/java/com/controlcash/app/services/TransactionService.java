package com.controlcash.app.services;

import com.controlcash.app.dtos.request.TransactionCreateRequestDTO;
import com.controlcash.app.dtos.response.TransactionCreateResponseDTO;
import com.controlcash.app.models.Transaction;
import com.controlcash.app.repositories.TransactionRepository;
import com.controlcash.app.utils.converters.TransactionConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public TransactionCreateResponseDTO create(TransactionCreateRequestDTO transactionCreateRequestDTO) {
        Transaction transaction = TransactionConverter.convertTransactionCreateRequestDTOToTransaction(transactionCreateRequestDTO);

        transaction = transactionRepository.save(transaction);

        return TransactionConverter.convertTransactionToTransactionCreateResponseDTO(transaction);
    }
}
