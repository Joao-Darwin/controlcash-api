package com.controlcash.app.utils.converters;

import com.controlcash.app.dtos.request.TransactionCreateRequestDTO;
import com.controlcash.app.dtos.response.TransactionCreateResponseDTO;
import com.controlcash.app.models.Transaction;

public class TransactionConverter {

    public static Transaction convertTransactionCreateRequestDTOToTransaction(TransactionCreateRequestDTO transactionCreateRequestDTO) {
        Transaction transaction = new Transaction();

        transaction.setName(transactionCreateRequestDTO.name());
        transaction.setDescription(transactionCreateRequestDTO.description());
        transaction.setValue(transactionCreateRequestDTO.value());
        transaction.setAmountRepeat(transactionCreateRequestDTO.amountRepeat());
        transaction.setTransactionType(transactionCreateRequestDTO.transactionType());
        transaction.setCategories(transactionCreateRequestDTO.categories());

        return transaction;
    }

    public static TransactionCreateResponseDTO convertTransactionToTransactionCreateResponseDTO(Transaction transaction) {
        return new TransactionCreateResponseDTO(transaction.getId(), transaction.getName(), transaction.getDescription(), transaction.getCreatedDate());
    }
}
