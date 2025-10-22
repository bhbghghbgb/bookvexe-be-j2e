-- PostgreSQL database dump converted from MySQL
-- Database: bookvexedb_j2e
-- Note:
-- - binary(16) converted to UUID (add DEFAULT gen_random_uuid() if auto-generation is needed).
-- - bit(1) converted to BOOLEAN.
-- - datetime converted to TIMESTAMP WITHOUT TIME ZONE.
-- - decimal(10,2) converted to NUMERIC(10,2).
-- - int/tinyint converted to INTEGER/BOOLEAN as appropriate.
-- - Removed MySQL-specific directives (ENGINE, CHARSET, /*! */ comments, LOCK TABLES, etc.).
-- - Constraints (UNIQUE, INDEX, FK) added via ALTER TABLE after all tables are created to avoid dependency issues.
-- - Data inserts omitted due to binary UUID values requiring manual hex-to-UUID conversion (e.g., via tools like hexdump or Python). Regenerate or convert data separately if needed.
-- - Run in order: DROPs, CREATEs, ALTERs for UNIQUE/INDEX/FK.

-- Enable required extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "vector";

-- Drop all tables if they exist (in reverse dependency order for safety, but since no data, order is flexible)
DROP TABLE IF EXISTS booking_seats;
DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS car_employees;
DROP TABLE IF EXISTS car_seats;
DROP TABLE IF EXISTS car_types;
DROP TABLE IF EXISTS cars;
DROP TABLE IF EXISTS customer;
DROP TABLE IF EXISTS customer_type;
DROP TABLE IF EXISTS employee;
DROP TABLE IF EXISTS invoices;
DROP TABLE IF EXISTS notification_types;
DROP TABLE IF EXISTS notifications;
DROP TABLE IF EXISTS password_reset_tokens;
DROP TABLE IF EXISTS payment_methods;
DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS role_permission;
DROP TABLE IF EXISTS role_user;
DROP TABLE IF EXISTS routes;
DROP TABLE IF EXISTS tokens;
DROP TABLE IF EXISTS trip_cars;
DROP TABLE IF EXISTS trip_stops;
DROP TABLE IF EXISTS trips;
DROP TABLE IF EXISTS user_sessions;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS role;

-- Create tables (columns and PK only)

CREATE TABLE car_types (
  uuid UUID NOT NULL,
  is_deleted BOOLEAN,
  created_date TIMESTAMP WITHOUT TIME ZONE,
  created_by UUID,
  updated_date TIMESTAMP WITHOUT TIME ZONE,
  updated_by UUID,
  code VARCHAR(255),
  name VARCHAR(255),
  description VARCHAR(255),
  seat_count INTEGER,
  PRIMARY KEY (uuid)
);

CREATE TABLE customer_type (
  uuid UUID NOT NULL,
  is_deleted BOOLEAN,
  created_date TIMESTAMP WITHOUT TIME ZONE,
  created_by UUID,
  updated_date TIMESTAMP WITHOUT TIME ZONE,
  updated_by UUID,
  code VARCHAR(255),
  name VARCHAR(255),
  description TEXT,
  PRIMARY KEY (uuid)
);

CREATE TABLE employee (
  uuid UUID NOT NULL,
  is_deleted BOOLEAN,
  created_date TIMESTAMP WITHOUT TIME ZONE,
  created_by UUID,
  updated_date TIMESTAMP WITHOUT TIME ZONE,
  updated_by UUID,
  code VARCHAR(255),
  name VARCHAR(255),
  email VARCHAR(100),
  phone VARCHAR(15),
  description TEXT,
  PRIMARY KEY (uuid)
);

CREATE TABLE role (
  uuid UUID NOT NULL,
  is_deleted BOOLEAN,
  created_date TIMESTAMP WITHOUT TIME ZONE,
  created_by UUID,
  updated_date TIMESTAMP WITHOUT TIME ZONE,
  updated_by UUID,
  code VARCHAR(100),
  name VARCHAR(100),
  description TEXT,
  PRIMARY KEY (uuid)
);

CREATE TABLE routes (
  uuid UUID NOT NULL,
  is_deleted BOOLEAN,
  created_date TIMESTAMP WITHOUT TIME ZONE,
  created_by UUID,
  updated_date TIMESTAMP WITHOUT TIME ZONE,
  updated_by UUID,
  start_location VARCHAR(100),
  end_location VARCHAR(100),
  distance_km NUMERIC(6,2),
  estimated_duration INTEGER,
  PRIMARY KEY (uuid)
);

CREATE TABLE payment_methods (
  uuid UUID NOT NULL,
  is_deleted BOOLEAN,
  created_date TIMESTAMP WITHOUT TIME ZONE,
  created_by UUID,
  updated_date TIMESTAMP WITHOUT TIME ZONE,
  updated_by UUID,
  code VARCHAR(255),
  name VARCHAR(50),
  description VARCHAR(255),
  PRIMARY KEY (uuid)
);

CREATE TABLE notification_types (
  uuid UUID NOT NULL,
  is_deleted BOOLEAN,
  created_date TIMESTAMP WITHOUT TIME ZONE,
  created_by UUID,
  updated_date TIMESTAMP WITHOUT TIME ZONE,
  updated_by UUID,
  code VARCHAR(255),
  name VARCHAR(50),
  description VARCHAR(255),
  PRIMARY KEY (uuid)
);

CREATE TABLE cars (
  uuid UUID NOT NULL,
  is_deleted BOOLEAN,
  created_date TIMESTAMP WITHOUT TIME ZONE,
  created_by UUID,
  updated_date TIMESTAMP WITHOUT TIME ZONE,
  updated_by UUID,
  car_type_id UUID,
  code VARCHAR(255),
  license_plate VARCHAR(20),
  PRIMARY KEY (uuid)
);

CREATE TABLE customer (
  uuid UUID NOT NULL,
  is_deleted BOOLEAN,
  created_date TIMESTAMP WITHOUT TIME ZONE,
  created_by UUID,
  updated_date TIMESTAMP WITHOUT TIME ZONE,
  updated_by UUID,
  code VARCHAR(255),
  name VARCHAR(255),
  email VARCHAR(100),
  phone VARCHAR(15),
  customer_type_id UUID,
  description TEXT,
  PRIMARY KEY (uuid)
);

CREATE TABLE users (
  uuid UUID NOT NULL,
  is_deleted BOOLEAN,
  created_date TIMESTAMP WITHOUT TIME ZONE,
  created_by UUID,
  updated_date TIMESTAMP WITHOUT TIME ZONE,
  updated_by UUID,
  username VARCHAR(255),
  password VARCHAR(255),
  is_google BOOLEAN,
  google_account VARCHAR(255),
  employee_id UUID,
  customer_id UUID,
  is_admin BOOLEAN DEFAULT false,
  PRIMARY KEY (uuid)
);

CREATE TABLE trips (
  uuid UUID NOT NULL,
  is_deleted BOOLEAN,
  created_date TIMESTAMP WITHOUT TIME ZONE,
  created_by UUID,
  updated_date TIMESTAMP WITHOUT TIME ZONE,
  updated_by UUID,
  route_id UUID,
  departure_time TIMESTAMP WITHOUT TIME ZONE,
  price NUMERIC(10,2),
  available_seats INTEGER,
  PRIMARY KEY (uuid)
);

CREATE TABLE trip_stops (
  uuid UUID NOT NULL,
  is_deleted BOOLEAN,
  created_date TIMESTAMP WITHOUT TIME ZONE,
  created_by UUID,
  updated_date TIMESTAMP WITHOUT TIME ZONE,
  updated_by UUID,
  trip_id UUID,
  stop_type VARCHAR(10),
  location VARCHAR(255),
  order_index INTEGER,
  PRIMARY KEY (uuid)
);

CREATE TABLE bookings (
  uuid UUID NOT NULL,
  is_deleted BOOLEAN,
  created_date TIMESTAMP WITHOUT TIME ZONE,
  created_by UUID,
  updated_date TIMESTAMP WITHOUT TIME ZONE,
  updated_by UUID,
  code VARCHAR(255),
  type VARCHAR(255),
  customer_id UUID,
  trip_id UUID,
  pickup_stop_id UUID,
  dropoff_stop_id UUID,
  booking_status VARCHAR(20),
  total_price NUMERIC(10,2),
  PRIMARY KEY (uuid)
);

CREATE TABLE car_seats (
  uuid UUID NOT NULL,
  is_deleted BOOLEAN,
  created_date TIMESTAMP WITHOUT TIME ZONE,
  created_by UUID,
  updated_date TIMESTAMP WITHOUT TIME ZONE,
  updated_by UUID,
  car_id UUID,
  seat_number VARCHAR(10),
  seat_position VARCHAR(50),
  PRIMARY KEY (uuid)
);

CREATE TABLE booking_seats (
  uuid UUID NOT NULL,
  is_deleted BOOLEAN,
  created_date TIMESTAMP WITHOUT TIME ZONE,
  created_by UUID,
  updated_date TIMESTAMP WITHOUT TIME ZONE,
  updated_by UUID,
  code VARCHAR(255),
  booking_id UUID,
  seat_id UUID,
  status VARCHAR(20),
  price NUMERIC(10,2),
  PRIMARY KEY (uuid)
);

CREATE TABLE car_employees (
  uuid UUID NOT NULL,
  is_deleted BOOLEAN,
  created_date TIMESTAMP WITHOUT TIME ZONE,
  created_by UUID,
  updated_date TIMESTAMP WITHOUT TIME ZONE,
  updated_by UUID,
  car_id UUID,
  employee_id UUID,
  role VARCHAR(20),
  PRIMARY KEY (uuid)
);

CREATE TABLE trip_cars (
  uuid UUID NOT NULL,
  is_deleted BOOLEAN,
  created_date TIMESTAMP WITHOUT TIME ZONE,
  created_by UUID,
  updated_date TIMESTAMP WITHOUT TIME ZONE,
  updated_by UUID,
  trip_id UUID,
  car_id UUID,
  price NUMERIC(10,2),
  available_seats INTEGER,
  PRIMARY KEY (uuid)
);

CREATE TABLE payments (
  uuid UUID NOT NULL,
  is_deleted BOOLEAN,
  created_date TIMESTAMP WITHOUT TIME ZONE,
  created_by UUID,
  updated_date TIMESTAMP WITHOUT TIME ZONE,
  updated_by UUID,
  booking_id UUID,
  method_id UUID,
  amount NUMERIC(10,2),
  status VARCHAR(20),
  transaction_code VARCHAR(100),
  paid_at TIMESTAMP WITHOUT TIME ZONE,
  PRIMARY KEY (uuid)
);

CREATE TABLE invoices (
  uuid UUID NOT NULL,
  is_deleted BOOLEAN,
  created_date TIMESTAMP WITHOUT TIME ZONE,
  created_by UUID,
  updated_date TIMESTAMP WITHOUT TIME ZONE,
  updated_by UUID,
  payment_id UUID,
  invoice_number VARCHAR(50),
  file_url VARCHAR(255),
  issued_at TIMESTAMP WITHOUT TIME ZONE,
  PRIMARY KEY (uuid)
);

CREATE TABLE notifications (
  uuid UUID NOT NULL,
  is_deleted BOOLEAN,
  created_date TIMESTAMP WITHOUT TIME ZONE,
  created_by UUID,
  updated_date TIMESTAMP WITHOUT TIME ZONE,
  updated_by UUID,
  user_id UUID,
  booking_id UUID,
  trip_id UUID,
  type_id UUID,
  channel VARCHAR(20),
  title VARCHAR(100),
  message TEXT,
  is_sent BOOLEAN,
  sent_at TIMESTAMP WITHOUT TIME ZONE,
  PRIMARY KEY (uuid)
);

CREATE TABLE password_reset_tokens (
  uuid UUID NOT NULL,
  is_deleted BOOLEAN,
  created_date TIMESTAMP WITHOUT TIME ZONE,
  created_by UUID,
  updated_date TIMESTAMP WITHOUT TIME ZONE,
  updated_by UUID,
  user_id UUID,
  token VARCHAR(255) NOT NULL,
  expires_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  PRIMARY KEY (uuid)
);

CREATE TABLE tokens (
  uuid UUID NOT NULL,
  is_deleted BOOLEAN,
  created_date TIMESTAMP WITHOUT TIME ZONE,
  created_by UUID,
  updated_date TIMESTAMP WITHOUT TIME ZONE,
  updated_by UUID,
  user_id UUID NOT NULL,
  token VARCHAR(255) NOT NULL,
  token_type VARCHAR(255) NOT NULL,
  expires_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  revoked BOOLEAN,
  PRIMARY KEY (uuid)
);

CREATE TABLE user_sessions (
  uuid UUID NOT NULL,
  is_deleted BOOLEAN,
  created_date TIMESTAMP WITHOUT TIME ZONE,
  created_by UUID,
  updated_date TIMESTAMP WITHOUT TIME ZONE,
  updated_by UUID,
  user_id UUID,
  access_token VARCHAR(255) NOT NULL,
  expires_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  revoked BOOLEAN,
  PRIMARY KEY (uuid)
);

CREATE TABLE role_permission (
  uuid UUID NOT NULL,
  is_deleted BOOLEAN,
  created_date TIMESTAMP WITHOUT TIME ZONE,
  created_by UUID,
  updated_date TIMESTAMP WITHOUT TIME ZONE,
  updated_by UUID,
  role_id UUID,
  is_can_read BOOLEAN,
  is_can_create BOOLEAN,
  is_can_update BOOLEAN,
  is_can_delete BOOLEAN,
  is_can_activate BOOLEAN,
  is_can_deactivate BOOLEAN,
  is_can_import BOOLEAN,
  is_can_export BOOLEAN,
  module VARCHAR(255),
  PRIMARY KEY (uuid)
);

CREATE TABLE role_user (
  uuid UUID NOT NULL,
  is_deleted BOOLEAN,
  created_date TIMESTAMP WITHOUT TIME ZONE,
  created_by UUID,
  updated_date TIMESTAMP WITHOUT TIME ZONE,
  updated_by UUID,
  role_id UUID,
  user_id UUID,
  PRIMARY KEY (uuid)
);

CREATE TABLE knowledge (
  id bigserial PRIMARY KEY,
  title text,
  content text NOT NULL,
  embedding vector(1536),  -- số chiều tùy model
  created_at timestamptz DEFAULT now()
);


-- Add UNIQUE constraints
ALTER TABLE booking_seats ADD CONSTRAINT uc_bookingseats_code UNIQUE (code);
ALTER TABLE bookings ADD CONSTRAINT uc_bookings_code UNIQUE (code);
ALTER TABLE car_types ADD CONSTRAINT uc_cartypes_code UNIQUE (code);
ALTER TABLE cars ADD CONSTRAINT uc_cars_code UNIQUE (code);
ALTER TABLE cars ADD CONSTRAINT uc_cars_licenseplate UNIQUE (license_plate);
ALTER TABLE customer ADD CONSTRAINT uc_customer_code UNIQUE (code);
ALTER TABLE customer_type ADD CONSTRAINT uc_customertype_code UNIQUE (code);
ALTER TABLE employee ADD CONSTRAINT uc_employee_code UNIQUE (code);
ALTER TABLE invoices ADD CONSTRAINT uc_invoices_invoicenumber UNIQUE (invoice_number);
ALTER TABLE invoices ADD CONSTRAINT uc_invoices_paymentid UNIQUE (payment_id);
ALTER TABLE notification_types ADD CONSTRAINT uc_notificationtypes_code UNIQUE (code);
ALTER TABLE notification_types ADD CONSTRAINT uc_notificationtypes_name UNIQUE (name);
ALTER TABLE payments ADD CONSTRAINT uc_payments_bookingid UNIQUE (booking_id);
ALTER TABLE payment_methods ADD CONSTRAINT uc_paymentmethods_code UNIQUE (code);
ALTER TABLE payment_methods ADD CONSTRAINT uc_paymentmethods_name UNIQUE (name);
ALTER TABLE role ADD CONSTRAINT uc_role_code UNIQUE (code);
ALTER TABLE password_reset_tokens ADD CONSTRAINT uc_password_reset_tokens_token UNIQUE (token);
ALTER TABLE tokens ADD CONSTRAINT uc_tokens_token UNIQUE (token);
ALTER TABLE users ADD CONSTRAINT uc_users_username UNIQUE (username);
ALTER TABLE users ADD CONSTRAINT uc_users_employeeid UNIQUE (employee_id);
ALTER TABLE users ADD CONSTRAINT uc_users_customerid UNIQUE (customer_id);
ALTER TABLE user_sessions ADD CONSTRAINT uc_user_sessions_accesstoken UNIQUE (access_token);

-- Add INDEXes (for KEYs)
CREATE INDEX "FK_BOOKINGSEATS_ON_BOOKINGID" ON booking_seats (booking_id);
CREATE INDEX "FK_BOOKINGSEATS_ON_SEATID" ON booking_seats (seat_id);
CREATE INDEX "FK_BOOKINGS_ON_CUSTOMERID" ON bookings (customer_id);
CREATE INDEX "FK_BOOKINGS_ON_DROPOFFSTOPID" ON bookings (dropoff_stop_id);
CREATE INDEX "FK_BOOKINGS_ON_PICKUPSTOPID" ON bookings (pickup_stop_id);
CREATE INDEX "FK_BOOKINGS_ON_TRIPID" ON bookings (trip_id);
CREATE INDEX "FK_CAREMPLOYEES_ON_CARID" ON car_employees (car_id);
CREATE INDEX "FK_CAREMPLOYEES_ON_EMPLOYEEID" ON car_employees (employee_id);
CREATE INDEX "FK_CARSEATS_ON_CARID" ON car_seats (car_id);
CREATE INDEX "FK_CARS_ON_CARTYPEID" ON cars (car_type_id);
CREATE INDEX "FK_CUSTOMER_ON_CUSTOMERTYPEID" ON customer (customer_type_id);
CREATE INDEX "FK_INVOICES_ON_PAYMENTID" ON invoices (payment_id);
CREATE INDEX "FK_NOTIFICATIONS_ON_BOOKINGID" ON notifications (booking_id);
CREATE INDEX "FK_NOTIFICATIONS_ON_TRIPID" ON notifications (trip_id);
CREATE INDEX "FK_NOTIFICATIONS_ON_TYPEID" ON notifications (type_id);
CREATE INDEX "FK_NOTIFICATIONS_ON_USERID" ON notifications (user_id);
CREATE INDEX "FK_PASSWORD_RESET_TOKENS_ON_USERID" ON password_reset_tokens (user_id);
CREATE INDEX "FK_PAYMENTS_ON_METHODID" ON payments (method_id);
CREATE INDEX "FK_ROLEPERMISSION_ON_ROLEID" ON role_permission (role_id);
CREATE INDEX "FK_ROLEUSER_ON_ROLEID" ON role_user (role_id);
CREATE INDEX "FK_ROLEUSER_ON_USERID" ON role_user (user_id);
CREATE INDEX "FK_TOKENS_ON_USERID" ON tokens (user_id);
CREATE INDEX "FK_TRIPCARS_ON_CARID" ON trip_cars (car_id);
CREATE INDEX "FK_TRIPCARS_ON_TRIPID" ON trip_cars (trip_id);
CREATE INDEX "FK_TRIPSTOPS_ON_TRIPID" ON trip_stops (trip_id);
CREATE INDEX "FK_TRIPS_ON_ROUTEID" ON trips (route_id);
CREATE INDEX "FK_USER_SESSIONS_ON_USERID" ON user_sessions (user_id);
CREATE INDEX "FK_USERS_ON_CUSTOMERID" ON users (customer_id);
CREATE INDEX "FK_USERS_ON_EMPLOYEEID" ON users (employee_id);

-- Add FOREIGN KEY constraints
ALTER TABLE booking_seats ADD CONSTRAINT "FK_BOOKINGSEATS_ON_BOOKINGID" FOREIGN KEY (booking_id) REFERENCES bookings (uuid);
ALTER TABLE booking_seats ADD CONSTRAINT "FK_BOOKINGSEATS_ON_SEATID" FOREIGN KEY (seat_id) REFERENCES car_seats (uuid);
ALTER TABLE bookings ADD CONSTRAINT "FK_BOOKINGS_ON_CUSTOMERID" FOREIGN KEY (customer_id) REFERENCES customer (uuid);
ALTER TABLE bookings ADD CONSTRAINT "FK_BOOKINGS_ON_DROPOFFSTOPID" FOREIGN KEY (dropoff_stop_id) REFERENCES trip_stops (uuid);
ALTER TABLE bookings ADD CONSTRAINT "FK_BOOKINGS_ON_PICKUPSTOPID" FOREIGN KEY (pickup_stop_id) REFERENCES trip_stops (uuid);
ALTER TABLE bookings ADD CONSTRAINT "FK_BOOKINGS_ON_TRIPID" FOREIGN KEY (trip_id) REFERENCES trips (uuid);
ALTER TABLE car_employees ADD CONSTRAINT "FK_CAREMPLOYEES_ON_CARID" FOREIGN KEY (car_id) REFERENCES cars (uuid);
ALTER TABLE car_employees ADD CONSTRAINT "FK_CAREMPLOYEES_ON_EMPLOYEEID" FOREIGN KEY (employee_id) REFERENCES employee (uuid);
ALTER TABLE car_seats ADD CONSTRAINT "FK_CARSEATS_ON_CARID" FOREIGN KEY (car_id) REFERENCES cars (uuid);
ALTER TABLE cars ADD CONSTRAINT "FK_CARS_ON_CARTYPEID" FOREIGN KEY (car_type_id) REFERENCES car_types (uuid);
ALTER TABLE customer ADD CONSTRAINT "FK_CUSTOMER_ON_CUSTOMERTYPEID" FOREIGN KEY (customer_type_id) REFERENCES customer_type (uuid);
ALTER TABLE invoices ADD CONSTRAINT "FK_INVOICES_ON_PAYMENTID" FOREIGN KEY (payment_id) REFERENCES payments (uuid);
ALTER TABLE notifications ADD CONSTRAINT "FK_NOTIFICATIONS_ON_BOOKINGID" FOREIGN KEY (booking_id) REFERENCES bookings (uuid);
ALTER TABLE notifications ADD CONSTRAINT "FK_NOTIFICATIONS_ON_TRIPID" FOREIGN KEY (trip_id) REFERENCES trips (uuid);
ALTER TABLE notifications ADD CONSTRAINT "FK_NOTIFICATIONS_ON_TYPEID" FOREIGN KEY (type_id) REFERENCES notification_types (uuid);
ALTER TABLE notifications ADD CONSTRAINT "FK_NOTIFICATIONS_ON_USERID" FOREIGN KEY (user_id) REFERENCES users (uuid);
ALTER TABLE password_reset_tokens ADD CONSTRAINT "FK_PASSWORD_RESET_TOKENS_ON_USERID" FOREIGN KEY (user_id) REFERENCES users (uuid);
ALTER TABLE payments ADD CONSTRAINT "FK_PAYMENTS_ON_BOOKINGID" FOREIGN KEY (booking_id) REFERENCES bookings (uuid);
ALTER TABLE payments ADD CONSTRAINT "FK_PAYMENTS_ON_METHODID" FOREIGN KEY (method_id) REFERENCES payment_methods (uuid);
ALTER TABLE role_permission ADD CONSTRAINT "FK_ROLEPERMISSION_ON_ROLEID" FOREIGN KEY (role_id) REFERENCES role (uuid);
ALTER TABLE role_user ADD CONSTRAINT "FK_ROLEUSER_ON_ROLEID" FOREIGN KEY (role_id) REFERENCES role (uuid);
ALTER TABLE role_user ADD CONSTRAINT "FK_ROLEUSER_ON_USERID" FOREIGN KEY (user_id) REFERENCES users (uuid);
ALTER TABLE tokens ADD CONSTRAINT "FK_TOKENS_ON_USERID" FOREIGN KEY (user_id) REFERENCES users (uuid);
ALTER TABLE trip_cars ADD CONSTRAINT "FK_TRIPCARS_ON_CARID" FOREIGN KEY (car_id) REFERENCES cars (uuid);
ALTER TABLE trip_cars ADD CONSTRAINT "FK_TRIPCARS_ON_TRIPID" FOREIGN KEY (trip_id) REFERENCES trips (uuid);
ALTER TABLE trip_stops ADD CONSTRAINT "FK_TRIPSTOPS_ON_TRIPID" FOREIGN KEY (trip_id) REFERENCES trips (uuid);
ALTER TABLE trips ADD CONSTRAINT "FK_TRIPS_ON_ROUTEID" FOREIGN KEY (route_id) REFERENCES routes (uuid);
ALTER TABLE user_sessions ADD CONSTRAINT "FK_USER_SESSIONS_ON_USERID" FOREIGN KEY (user_id) REFERENCES users (uuid);
ALTER TABLE users ADD CONSTRAINT "FK_USERS_ON_CUSTOMERID" FOREIGN KEY (customer_id) REFERENCES customer (uuid);
ALTER TABLE users ADD CONSTRAINT "FK_USERS_ON_EMPLOYEEID" FOREIGN KEY (employee_id) REFERENCES employee (uuid);
