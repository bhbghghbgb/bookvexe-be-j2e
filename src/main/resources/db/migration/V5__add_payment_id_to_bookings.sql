-- Add payment_id to bookings to align with refactor (store only UUID reference)
ALTER TABLE bookings
    ADD COLUMN IF NOT EXISTS payment_id UUID;

-- Helpful index for lookups
CREATE INDEX IF NOT EXISTS "IDX_BOOKINGS_PAYMENT_ID" ON bookings (payment_id);
