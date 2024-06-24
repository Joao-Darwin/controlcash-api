package com.controlcash.app.services;

import com.controlcash.app.dtos.transaction.request.TransactionCreateRequestDTO;
import com.controlcash.app.dtos.transaction.response.TransactionCompleteResponseDTO;
import com.controlcash.app.dtos.transaction.response.TransactionCreateResponseDTO;
import com.controlcash.app.exceptions.TransactionNotFoundException;
import com.controlcash.app.models.Transaction;
import com.controlcash.app.repositories.TransactionRepository;
import com.controlcash.app.utils.converters.TransactionConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    public List<TransactionCreateResponseDTO> findAll(Pageable pageable) {
        Page<Transaction> transactions = transactionRepository.findAll(pageable);

        return transactions.map(TransactionConverter::convertTransactionToTransactionCreateResponseDTO).toList();
    }

    public TransactionCompleteResponseDTO findById(UUID id) {
        Transaction transaction = findTransactionByIdAndVerifyIfExists(id);

        return TransactionConverter.convertTransactionToTransactionCompleteResponseDTO(transaction);
    }

    private Transaction findTransactionByIdAndVerifyIfExists(UUID id) {
        Optional<Transaction> transactionOptional = transactionRepository.findById(id);
        boolean transactionExists = transactionOptional.isPresent();

        if (!transactionExists) {
            throw new TransactionNotFoundException("Transaction not found. Id used: " + id);
        }

        return transactionOptional.get();
    }
}
