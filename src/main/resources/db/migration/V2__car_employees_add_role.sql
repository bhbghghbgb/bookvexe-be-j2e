-- Add role column for car employee assignments
ALTER TABLE car_employees
  ADD COLUMN role VARCHAR(20) NULL;
