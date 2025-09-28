CREATE TABLE booking_seats
(
    booking_seat_id INT AUTO_INCREMENT NOT NULL,
    booking_id      INT                NULL,
    seat_id         INT                NULL,
    is_reserved     BIT(1)             NULL,
    price           DECIMAL(10, 2)     NOT NULL,
    CONSTRAINT pk_booking_seats PRIMARY KEY (booking_seat_id)
);

CREATE TABLE bookings
(
    booking_id     INT AUTO_INCREMENT NOT NULL,
    user_id        INT                NULL,
    trip_id        INT                NULL,
    booking_status VARCHAR(20)        NOT NULL,
    total_price    DECIMAL(10, 2)     NOT NULL,
    created_at     datetime           NULL,
    updated_at     datetime           NULL,
    CONSTRAINT pk_bookings PRIMARY KEY (booking_id)
);

CREATE TABLE car_seats
(
    seat_id       INT AUTO_INCREMENT NOT NULL,
    car_id        INT                NULL,
    seat_number   VARCHAR(10)        NOT NULL,
    seat_position VARCHAR(50)        NULL,
    is_active     BIT(1)             NULL,
    CONSTRAINT pk_car_seats PRIMARY KEY (seat_id)
);

CREATE TABLE car_types
(
    car_type_id   INT AUTO_INCREMENT NOT NULL,
    type_name     VARCHAR(50)        NOT NULL,
    `description` VARCHAR(255)       NULL,
    CONSTRAINT pk_car_types PRIMARY KEY (car_type_id)
);

CREATE TABLE cars
(
    car_id        INT AUTO_INCREMENT NOT NULL,
    owner_id      INT                NULL,
    car_type_id   INT                NULL,
    license_plate VARCHAR(20)        NOT NULL,
    seat_count    INT                NOT NULL,
    created_at    datetime           NULL,
    updated_at    datetime           NULL,
    CONSTRAINT pk_cars PRIMARY KEY (car_id)
);

CREATE TABLE invoices
(
    invoice_id     INT AUTO_INCREMENT NOT NULL,
    payment_id     INT                NULL,
    invoice_number VARCHAR(50)        NOT NULL,
    file_url       VARCHAR(255)       NULL,
    issued_at      datetime           NULL,
    created_at     datetime           NULL,
    CONSTRAINT pk_invoices PRIMARY KEY (invoice_id)
);

CREATE TABLE notification_types
(
    type_id       INT AUTO_INCREMENT NOT NULL,
    type_name     VARCHAR(50)        NOT NULL,
    `description` VARCHAR(255)       NULL,
    CONSTRAINT pk_notification_types PRIMARY KEY (type_id)
);

CREATE TABLE notifications
(
    notification_id INT AUTO_INCREMENT NOT NULL,
    user_id         INT                NULL,
    booking_id      INT                NULL,
    trip_id         INT                NULL,
    type_id         INT                NULL,
    channel         VARCHAR(20)        NOT NULL,
    title           VARCHAR(100)       NOT NULL,
    message         TEXT               NOT NULL,
    is_sent         BIT(1)             NULL,
    sent_at         datetime           NULL,
    created_at      datetime           NULL,
    CONSTRAINT pk_notifications PRIMARY KEY (notification_id)
);

CREATE TABLE password_reset_tokens
(
    token_id   INT AUTO_INCREMENT NOT NULL,
    user_id    INT                NULL,
    token      VARCHAR(255)       NOT NULL,
    expires_at datetime           NOT NULL,
    created_at datetime           NULL,
    CONSTRAINT pk_password_reset_tokens PRIMARY KEY (token_id)
);

CREATE TABLE payment_methods
(
    method_id     INT AUTO_INCREMENT NOT NULL,
    method_name   VARCHAR(50)        NOT NULL,
    `description` VARCHAR(255)       NULL,
    is_active     BIT(1)             NULL,
    CONSTRAINT pk_payment_methods PRIMARY KEY (method_id)
);

CREATE TABLE payments
(
    payment_id       INT AUTO_INCREMENT NOT NULL,
    booking_id       INT                NULL,
    method_id        INT                NULL,
    amount           DECIMAL(10, 2)     NOT NULL,
    status           VARCHAR(20)        NOT NULL,
    transaction_code VARCHAR(100)       NULL,
    paid_at          datetime           NULL,
    created_at       datetime           NULL,
    updated_at       datetime           NULL,
    CONSTRAINT pk_payments PRIMARY KEY (payment_id)
);

CREATE TABLE `role`
(
    id         INT AUTO_INCREMENT NOT NULL,
    code       VARCHAR(100)       NOT NULL,
    name       VARCHAR(100)       NULL,
    created_at datetime           NULL,
    CONSTRAINT pk_role PRIMARY KEY (id)
);

CREATE TABLE role_permission
(
    role_id        INT    NOT NULL,
    can_read       BIT(1) NULL,
    can_create     BIT(1) NULL,
    can_update     BIT(1) NULL,
    can_delete     BIT(1) NULL,
    can_activate   BIT(1) NULL,
    can_deactivate BIT(1) NULL,
    can_import     BIT(1) NULL,
    can_export     BIT(1) NULL,
    CONSTRAINT pk_role_permission PRIMARY KEY (role_id)
);

CREATE TABLE role_user
(
    id        INT AUTO_INCREMENT NOT NULL,
    user_id   INT                NULL,
    role_id   INT                NULL,
    is_active BIT(1)             NULL,
    CONSTRAINT pk_role_user PRIMARY KEY (id)
);

CREATE TABLE routes
(
    route_id           INT AUTO_INCREMENT NOT NULL,
    start_location     VARCHAR(100)       NOT NULL,
    end_location       VARCHAR(100)       NOT NULL,
    distance_km        DECIMAL(6, 2)      NULL,
    estimated_duration INT                NULL,
    created_at         datetime           NULL,
    updated_at         datetime           NULL,
    CONSTRAINT pk_routes PRIMARY KEY (route_id)
);

CREATE TABLE trips
(
    trip_id         INT AUTO_INCREMENT NOT NULL,
    route_id        INT                NULL,
    bus_id          INT                NULL,
    departure_time  datetime           NOT NULL,
    price           DECIMAL(10, 2)     NOT NULL,
    available_seats INT                NOT NULL,
    created_at      datetime           NULL,
    updated_at      datetime           NULL,
    CONSTRAINT pk_trips PRIMARY KEY (trip_id)
);

CREATE TABLE user_sessions
(
    session_id   INT AUTO_INCREMENT NOT NULL,
    user_id      INT                NULL,
    access_token VARCHAR(255)       NOT NULL,
    expires_at   datetime           NOT NULL,
    revoked      BIT(1)             NULL,
    created_at   datetime           NULL,
    CONSTRAINT pk_user_sessions PRIMARY KEY (session_id)
);

CREATE TABLE users
(
    user_id       INT AUTO_INCREMENT NOT NULL,
    user_uuid     BINARY(16)         NULL,
    full_name     VARCHAR(100)       NOT NULL,
    email         VARCHAR(100)       NOT NULL,
    phone_number  VARCHAR(15)        NOT NULL,
    password_hash VARCHAR(255)       NOT NULL,
    role_id       INT                NULL,
    is_active     BIT(1)             NULL,
    created_at    datetime           NULL,
    updated_at    datetime           NULL,
    CONSTRAINT pk_users PRIMARY KEY (user_id)
);

ALTER TABLE car_types
    ADD CONSTRAINT uc_car_types_type_name UNIQUE (type_name);

ALTER TABLE cars
    ADD CONSTRAINT uc_cars_license_plate UNIQUE (license_plate);

ALTER TABLE invoices
    ADD CONSTRAINT uc_invoices_invoice_number UNIQUE (invoice_number);

ALTER TABLE notification_types
    ADD CONSTRAINT uc_notification_types_type_name UNIQUE (type_name);

ALTER TABLE password_reset_tokens
    ADD CONSTRAINT uc_password_reset_tokens_token UNIQUE (token);

ALTER TABLE payment_methods
    ADD CONSTRAINT uc_payment_methods_method_name UNIQUE (method_name);

ALTER TABLE `role`
    ADD CONSTRAINT uc_role_code UNIQUE (code);

ALTER TABLE user_sessions
    ADD CONSTRAINT uc_user_sessions_access_token UNIQUE (access_token);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE users
    ADD CONSTRAINT uc_users_phone_number UNIQUE (phone_number);

ALTER TABLE users
    ADD CONSTRAINT uc_users_user_uuid UNIQUE (user_uuid);

ALTER TABLE bookings
    ADD CONSTRAINT FK_BOOKINGS_ON_TRIP FOREIGN KEY (trip_id) REFERENCES trips (trip_id);

ALTER TABLE bookings
    ADD CONSTRAINT FK_BOOKINGS_ON_USER FOREIGN KEY (user_id) REFERENCES users (user_id);

ALTER TABLE booking_seats
    ADD CONSTRAINT FK_BOOKING_SEATS_ON_BOOKING FOREIGN KEY (booking_id) REFERENCES bookings (booking_id);

ALTER TABLE booking_seats
    ADD CONSTRAINT FK_BOOKING_SEATS_ON_SEAT FOREIGN KEY (seat_id) REFERENCES car_seats (seat_id);

ALTER TABLE cars
    ADD CONSTRAINT FK_CARS_ON_CAR_TYPE FOREIGN KEY (car_type_id) REFERENCES car_types (car_type_id);

ALTER TABLE cars
    ADD CONSTRAINT FK_CARS_ON_OWNER FOREIGN KEY (owner_id) REFERENCES users (user_id);

ALTER TABLE car_seats
    ADD CONSTRAINT FK_CAR_SEATS_ON_CAR FOREIGN KEY (car_id) REFERENCES cars (car_id);

ALTER TABLE invoices
    ADD CONSTRAINT FK_INVOICES_ON_PAYMENT FOREIGN KEY (payment_id) REFERENCES payments (payment_id);

ALTER TABLE notifications
    ADD CONSTRAINT FK_NOTIFICATIONS_ON_BOOKING FOREIGN KEY (booking_id) REFERENCES bookings (booking_id);

ALTER TABLE notifications
    ADD CONSTRAINT FK_NOTIFICATIONS_ON_TRIP FOREIGN KEY (trip_id) REFERENCES trips (trip_id);

ALTER TABLE notifications
    ADD CONSTRAINT FK_NOTIFICATIONS_ON_TYPE FOREIGN KEY (type_id) REFERENCES notification_types (type_id);

ALTER TABLE notifications
    ADD CONSTRAINT FK_NOTIFICATIONS_ON_USER FOREIGN KEY (user_id) REFERENCES users (user_id);

ALTER TABLE password_reset_tokens
    ADD CONSTRAINT FK_PASSWORD_RESET_TOKENS_ON_USER FOREIGN KEY (user_id) REFERENCES users (user_id);

ALTER TABLE payments
    ADD CONSTRAINT FK_PAYMENTS_ON_BOOKING FOREIGN KEY (booking_id) REFERENCES bookings (booking_id);

ALTER TABLE payments
    ADD CONSTRAINT FK_PAYMENTS_ON_METHOD FOREIGN KEY (method_id) REFERENCES payment_methods (method_id);

ALTER TABLE role_permission
    ADD CONSTRAINT FK_ROLE_PERMISSION_ON_ROLE FOREIGN KEY (role_id) REFERENCES `role` (id);

ALTER TABLE role_user
    ADD CONSTRAINT FK_ROLE_USER_ON_ROLE FOREIGN KEY (role_id) REFERENCES `role` (id);

ALTER TABLE role_user
    ADD CONSTRAINT FK_ROLE_USER_ON_USER FOREIGN KEY (user_id) REFERENCES users (user_id);

ALTER TABLE trips
    ADD CONSTRAINT FK_TRIPS_ON_BUS FOREIGN KEY (bus_id) REFERENCES cars (car_id);

ALTER TABLE trips
    ADD CONSTRAINT FK_TRIPS_ON_ROUTE FOREIGN KEY (route_id) REFERENCES routes (route_id);

ALTER TABLE users
    ADD CONSTRAINT FK_USERS_ON_ROLE FOREIGN KEY (role_id) REFERENCES `role` (id);

ALTER TABLE user_sessions
    ADD CONSTRAINT FK_USER_SESSIONS_ON_USER FOREIGN KEY (user_id) REFERENCES users (user_id);