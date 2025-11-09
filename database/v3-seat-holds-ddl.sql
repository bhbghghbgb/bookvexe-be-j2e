-- Seat holds table for realtime seat reservation
CREATE TABLE seat_holds (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    trip_id UUID NOT NULL,
    car_id UUID NOT NULL,
    seat_id UUID NOT NULL,
    user_id UUID,
    session_id VARCHAR(100),
    hold_until TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE,
    
    CONSTRAINT fk_seat_holds_trip FOREIGN KEY (trip_id) REFERENCES trips(id),
    CONSTRAINT fk_seat_holds_car FOREIGN KEY (car_id) REFERENCES cars(id),
    CONSTRAINT fk_seat_holds_seat FOREIGN KEY (seat_id) REFERENCES car_seats(id),
    CONSTRAINT fk_seat_holds_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Indexes for performance
CREATE INDEX idx_seat_holds_trip_car ON seat_holds(trip_id, car_id);
CREATE INDEX idx_seat_holds_status_holduntil ON seat_holds(status, hold_until);
CREATE INDEX idx_seat_holds_seat_status ON seat_holds(seat_id, status);

-- Comments
COMMENT ON TABLE seat_holds IS 'Temporary seat reservations during booking process';
COMMENT ON COLUMN seat_holds.hold_until IS '5-minute hold timeout';
COMMENT ON COLUMN seat_holds.status IS 'ACTIVE, EXPIRED, RELEASED';