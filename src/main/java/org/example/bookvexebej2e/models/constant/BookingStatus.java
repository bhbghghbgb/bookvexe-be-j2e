package org.example.bookvexebej2e.models.constant;

public class BookingStatus {
    public static final String NEW = "new";
    public static final String AWAIT_PAYMENT = "await_payment";
    public static final String AWAIT_GO = "await_go";
    public static final String DEPARTING = "departing";
    public static final String COMPLETED = "completed";
    public static final String CANCELLED = "cancelled";

    private BookingStatus() {
        // Utility class
    }
}