package com.controlcash.app.utils.converters;

import com.controlcash.app.builder.TransactionBuilder;
import com.controlcash.app.dtos.category.response.CategoryResponseDTO;
import com.controlcash.app.dtos.transaction.request.TransactionCreateRequestDTO;
import com.controlcash.app.dtos.transaction.response.TransactionCompleteResponseDTO;
import com.controlcash.app.dtos.transaction.response.TransactionCreateResponseDTO;
import com.controlcash.app.models.Transaction;

import java.util.List;

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

    public static TransactionCompleteResponseDTO convertTransactionToTransactionCompleteResponseDTO(Transaction transaction) {
        List<CategoryResponseDTO> categories = transaction.getCategories().stream().map(CategoryConverter::convertCategoryToCategoryResponseDTO).toList();

        return new TransactionCompleteResponseDTO(transaction.getId(),
                transaction.getName(),
                transaction.getDescription(),
                transaction.getCreatedDate(),
                transaction.getValue(),
                transaction.getAmountRepeat(),
                transaction.getTransactionType(),
                categories);
    }
}
