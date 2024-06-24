package com.controlcash.app.utils.converters;

import com.controlcash.app.builder.TransactionBuilder;
import com.controlcash.app.dtos.request.TransactionCreateRequestDTO;
import com.controlcash.app.dtos.response.TransactionCreateResponseDTO;
import com.controlcash.app.models.Transaction;

public class TransactionConverter {

    public static Transaction convertTransactionCreateRequestDTOToTransaction(TransactionCreateRequestDTO transactionCreateRequestDTO) {

        return new TransactionBuilder(transactionCreateRequestDTO.transactionType())
                .addName(transactionCreateRequestDTO.name())
                .addDescription(transactionCreateRequestDTO.description())
                .addValue(transactionCreateRequestDTO.value())
                .addAmountRepeat(transactionCreateRequestDTO.amountRepeat())
                .addCategories(transactionCreateRequestDTO.categories())
                .build();
    }

    public static TransactionCreateResponseDTO convertTransactionToTransactionCreateResponseDTO(Transaction transaction) {
        return new TransactionCreateResponseDTO(transaction.getId(), transaction.getName(), transaction.getDescription(), transaction.getCreatedDate());
    }
}
