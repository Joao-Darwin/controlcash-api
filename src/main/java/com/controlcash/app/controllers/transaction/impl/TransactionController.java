package com.controlcash.app.controllers.transaction.impl;

import com.controlcash.app.controllers.transaction.ITransactionController;
import com.controlcash.app.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("${path-api}/transactions")
@RestController
class TransactionController implements ITransactionController {

    private final TransactionService transactionService;

    @Autowired
    TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
}
