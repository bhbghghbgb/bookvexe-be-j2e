-- Add is_active column to cars table
ALTER TABLE cars ADD COLUMN is_active BIT(1) DEFAULT 1 NOT NULL;

-- Update existing records to be active by default (if any exist)
UPDATE cars SET is_active = 1;