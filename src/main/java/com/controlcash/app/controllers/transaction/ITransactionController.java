package com.controlcash.app.controllers.transaction;

import com.controlcash.app.dtos.transaction.request.TransactionCreateRequestDTO;
import com.controlcash.app.dtos.transaction.response.TransactionCreateResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "Transactions", description = "Endpoints for managing financial transactions")
public interface ITransactionController {

    @Operation(
            summary = "Create a new transaction",
            description = "Creates a new financial transaction by providing a JSON, XML, or YML representation.",
            tags = {"Transactions"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "201",
                            content = @Content(schema = @Schema(implementation = TransactionCreateResponseDTO.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Internal Error", responseCode = "500")
            }
    )
    ResponseEntity<TransactionCreateResponseDTO> create(TransactionCreateRequestDTO transactionCreateRequestDTO);

    @Operation(
            summary = "Fetch all transactions",
            description = "Retrieves a paginated list of all transactions, with sorting and pagination options.",
            tags = {"Transactions"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = TransactionCreateResponseDTO.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Internal Error", responseCode = "500")
            }
    )
    ResponseEntity<Page<TransactionCreateResponseDTO>> findAll(int page, int size, String sort);

    @Operation(
            summary = "Fetch a transaction by ID",
            description = "Retrieves the details of a transaction by its unique identifier (UUID).",
            tags = {"Transactions"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = TransactionCreateResponseDTO.class))
                    ),
                    @ApiResponse(description = "Transaction Not Found", responseCode = "404"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Internal Error", responseCode = "500")
            }
    )
    ResponseEntity<?> findById(UUID id);

    @Operation(
            summary = "Update a transaction",
            description = "Updates a transaction's details by providing its unique identifier (UUID) and the updated data.",
            tags = {"Transactions"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200"),
                    @ApiResponse(description = "Transaction Not Found", responseCode = "404"),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Internal Error", responseCode = "500")
            }
    )
    ResponseEntity<?> update(TransactionCreateRequestDTO transactionCreateRequestDTO, UUID id);

    @Operation(
            summary = "Delete a transaction",
            description = "Deletes a transaction by its unique identifier (UUID).",
            tags = {"Transactions"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "204"),
                    @ApiResponse(description = "Transaction Not Found", responseCode = "404"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Internal Error", responseCode = "500")
            }
    )
    ResponseEntity<?> delete(UUID id);
}
