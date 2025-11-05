package org.example.bookvexebej2e.models.constant;

public class SeatStatus {
    // Seat statuses in booking_seat table
    public static final String AVAILABLE = "AVAILABLE";
    public static final String RESERVED = "RESERVED"; // When booking is in AWAIT_PAYMENT
    public static final String BOOKED = "BOOKED"; // When booking is in AWAIT_GO or later
    public static final String CANCELLED = "CANCELLED";

    // Seat hold statuses in seat_holds table
    public static final String HOLD_ACTIVE = "ACTIVE";
    public static final String HOLD_EXPIRED = "EXPIRED";
    public static final String HOLD_RELEASED = "RELEASED";

    private SeatStatus() {
    }
}