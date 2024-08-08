package com.controlcash.app.models.enums;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class TransactionTypeTest {

    @ParameterizedTest
    @CsvSource({
            "1",
            "2"
    })
    void testValueOf_GivenValidCodes_ShouldReturnTheCorrespondingTransactionType(int code) {
        TransactionType actualTransactionType = TransactionType.valueOf(code);

        Assertions.assertEquals(code, actualTransactionType.getCode());
    }
}
