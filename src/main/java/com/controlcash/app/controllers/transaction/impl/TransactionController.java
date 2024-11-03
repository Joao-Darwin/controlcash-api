package com.controlcash.app.controllers.transaction.impl;

import com.controlcash.app.controllers.transaction.ITransactionController;
import com.controlcash.app.dtos.transaction.request.TransactionCreateRequestDTO;
import com.controlcash.app.dtos.transaction.response.TransactionCompleteResponseDTO;
import com.controlcash.app.dtos.transaction.response.TransactionCreateResponseDTO;
import com.controlcash.app.exceptions.ResponseEntityException;
import com.controlcash.app.exceptions.TransactionNotFoundException;
import com.controlcash.app.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.UUID;

@RequestMapping("${path-api}/transactions")
@RestController
class TransactionController implements ITransactionController {

    private final TransactionService transactionService;

    @Autowired
    TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<TransactionCreateResponseDTO> create(@RequestBody TransactionCreateRequestDTO transactionCreateRequestDTO) {
        TransactionCreateResponseDTO transactionCreateResponseDTO = transactionService.create(transactionCreateRequestDTO);

        return ResponseEntity.status(HttpStatus.OK).body(transactionCreateResponseDTO);
    }

    @GetMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<Page<TransactionCreateResponseDTO>> findAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "asc") String sort) {

        Sort.Direction sortDirection = sort.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "createdDate"));

        Page<TransactionCreateResponseDTO> transactionCreateResponseDTOPage = transactionService.findAll(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(transactionCreateResponseDTOPage);
    }

    @GetMapping(
            value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<?> findById(@PathVariable UUID id) {
        try {
            TransactionCompleteResponseDTO transactionCompleteResponseDTO = transactionService.findById(id);

            return ResponseEntity.status(HttpStatus.OK).body(transactionCompleteResponseDTO);
        } catch (TransactionNotFoundException transactionNotFoundException) {
            ResponseEntityException responseEntityException = new ResponseEntityException(
                    Instant.now(),
                    transactionNotFoundException.getMessage(),
                    ""
            );

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseEntityException);
        }
    }

    @Override
    @PutMapping(
            value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<?> update(@RequestBody TransactionCreateRequestDTO transactionCreateRequestDTO, @PathVariable UUID id) {
        try {
            TransactionCreateResponseDTO transactionCreateResponseDTO = transactionService.update(transactionCreateRequestDTO, id);

            return ResponseEntity.status(HttpStatus.OK).body(transactionCreateResponseDTO);
        } catch (TransactionNotFoundException transactionNotFoundException) {
            ResponseEntityException responseEntityException = new ResponseEntityException(
                    Instant.now(),
                    transactionNotFoundException.getMessage(),
                    ""
            );

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseEntityException);
        }
    }

    @Override
    @DeleteMapping(
            value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        transactionService.delete(id);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
