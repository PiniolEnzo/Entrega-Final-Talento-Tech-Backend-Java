package com.techlab.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PaymentStatusTest {
    @Test
    @DisplayName("Enum values are not null")
    void enumValuesAreNotNull() {
        for (PaymentStatus status : PaymentStatus.values()) {
            Assertions.assertNotNull(status);
        }
    }
}

