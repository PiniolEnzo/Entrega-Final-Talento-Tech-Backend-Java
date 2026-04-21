package com.techlab.entity;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Enum representing the possible payment statuses of an order.
 * <p>
 * Used to indicate whether the order has been paid, is pending, or has canceled.
 * <p>
 *     Payment Status possible values:
 * <dl>
 *     <dt>-{@code PENDING}</dt>
 *     <dd>The order has not yet been paid.</dd>
 *     <dt>-{@code PAID}</dt>
 *     <dd>The order was successfully paid.</dd>
 *     <dt>-{@code CANCELED}</dt>
 *     <dd>The order was canceled.</dd>
 * </dl>
 * <p>
 * This enum is stored as a string in the database using {@code @Enumerated(EnumType.STRING)}.
 * <p>
 * Commonly used in the {@link Order} entity to track the current state of payment.
 */
@Schema(description = "Enum representing the payment status of an order.")
public enum PaymentStatus {
    /**
     * The order has not yet been paid.
     */
    @Schema(description = "The order has not yet been paid.")
    PENDING,
    /**
     * The order was successfully paid.
     */
    @Schema(description = "The order was successfully paid.")
    PAID,
    /**
     * The order was canceled.
     */
    @Schema(description = "The order was canceled.")
    CANCELED
}
