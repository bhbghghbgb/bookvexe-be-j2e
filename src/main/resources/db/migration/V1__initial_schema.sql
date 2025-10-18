CREATE TABLE booking_seats
(
    uuid         BINARY(16)     NOT NULL,
    id_deleted   BIT(1)         NULL,
    created_date datetime       NULL,
    created_by   BINARY(16)     NULL,
    updated_date datetime       NULL,
    updated_by   BINARY(16)     NULL,
    booking_id   BINARY(16)     NULL,
    seat_id      BINARY(16)     NULL,
    status       VARCHAR(20)    NULL,
    price        DECIMAL(10, 2) NULL,
    CONSTRAINT pk_bookingseats PRIMARY KEY (uuid)
);

CREATE TABLE bookings
(
    uuid            BINARY(16)     NOT NULL,
    id_deleted      BIT(1)         NULL,
    created_date    datetime       NULL,
    created_by      BINARY(16)     NULL,
    updated_date    datetime       NULL,
    updated_by      BINARY(16)     NULL,
    code            VARCHAR(255)   NULL,
    type            VARCHAR(255)   NULL,
    customer_id     BINARY(16)     NULL,
    trip_id         BINARY(16)     NULL,
    pickup_stop_id  BINARY(16)     NULL,
    dropoff_stop_id BINARY(16)     NULL,
    booking_status  VARCHAR(20)    NULL,
    total_price     DECIMAL(10, 2) NULL,
    CONSTRAINT pk_bookings PRIMARY KEY (uuid)
);

CREATE TABLE car_employees
(
    uuid         BINARY(16) NOT NULL,
    id_deleted   BIT(1)     NULL,
    created_date datetime   NULL,
    created_by   BINARY(16) NULL,
    updated_date datetime   NULL,
    updated_by   BINARY(16) NULL,
    car_id       BINARY(16) NULL,
    employee_id  BINARY(16) NULL,
    CONSTRAINT pk_caremployees PRIMARY KEY (uuid)
);

CREATE TABLE car_seats
(
    uuid          BINARY(16)  NOT NULL,
    id_deleted    BIT(1)      NULL,
    created_date  datetime    NULL,
    created_by    BINARY(16)  NULL,
    updated_date  datetime    NULL,
    updated_by    BINARY(16)  NULL,
    car_id        BINARY(16)  NULL,
    seat_number   VARCHAR(10) NULL,
    seat_position VARCHAR(50) NULL,
    CONSTRAINT pk_carseats PRIMARY KEY (uuid)
);

CREATE TABLE car_types
(
    uuid          BINARY(16)   NOT NULL,
    id_deleted    BIT(1)       NULL,
    created_date  datetime     NULL,
    created_by    BINARY(16)   NULL,
    updated_date  datetime     NULL,
    updated_by    BINARY(16)   NULL,
    code          VARCHAR(255) NULL,
    name          VARCHAR(255) NULL,
    `description` VARCHAR(255) NULL,
    seat_count    INT          NULL,
    CONSTRAINT pk_cartypes PRIMARY KEY (uuid)
);

CREATE TABLE cars
(
    uuid          BINARY(16)  NOT NULL,
    id_deleted    BIT(1)      NULL,
    created_date  datetime    NULL,
    created_by    BINARY(16)  NULL,
    updated_date  datetime    NULL,
    updated_by    BINARY(16)  NULL,
    car_type_id   BINARY(16)  NULL,
    license_plate VARCHAR(20) NULL,
    CONSTRAINT pk_cars PRIMARY KEY (uuid)
);

CREATE TABLE customer
(
    uuid             BINARY(16)   NOT NULL,
    id_deleted       BIT(1)       NULL,
    created_date     datetime     NULL,
    created_by       BINARY(16)   NULL,
    updated_date     datetime     NULL,
    updated_by       BINARY(16)   NULL,
    code             VARCHAR(255) NULL,
    name             VARCHAR(255) NULL,
    email            VARCHAR(100) NULL,
    phone            VARCHAR(15)  NULL,
    customer_type_id BINARY(16)   NULL,
    `description`    TEXT         NULL,
    CONSTRAINT pk_customer PRIMARY KEY (uuid)
);

CREATE TABLE customer_type
(
    uuid          BINARY(16)   NOT NULL,
    id_deleted    BIT(1)       NULL,
    created_date  datetime     NULL,
    created_by    BINARY(16)   NULL,
    updated_date  datetime     NULL,
    updated_by    BINARY(16)   NULL,
    code          VARCHAR(255) NULL,
    name          VARCHAR(255) NULL,
    `description` TEXT         NULL,
    CONSTRAINT pk_customertype PRIMARY KEY (uuid)
);

CREATE TABLE employee
(
    uuid          BINARY(16)   NOT NULL,
    id_deleted    BIT(1)       NULL,
    created_date  datetime     NULL,
    created_by    BINARY(16)   NULL,
    updated_date  datetime     NULL,
    updated_by    BINARY(16)   NULL,
    code          VARCHAR(255) NULL,
    name          VARCHAR(255) NULL,
    email         VARCHAR(100) NULL,
    phone         VARCHAR(15)  NULL,
    `description` TEXT         NULL,
    CONSTRAINT pk_employee PRIMARY KEY (uuid)
);

CREATE TABLE invoices
(
    uuid           BINARY(16)   NOT NULL,
    id_deleted     BIT(1)       NULL,
    created_date   datetime     NULL,
    created_by     BINARY(16)   NULL,
    updated_date   datetime     NULL,
    updated_by     BINARY(16)   NULL,
    payment_id     BINARY(16)   NULL,
    invoice_number VARCHAR(50)  NULL,
    file_url       VARCHAR(255) NULL,
    issued_at      datetime     NULL,
    CONSTRAINT pk_invoices PRIMARY KEY (uuid)
);

CREATE TABLE notification_types
(
    uuid          BINARY(16)   NOT NULL,
    id_deleted    BIT(1)       NULL,
    created_date  datetime     NULL,
    created_by    BINARY(16)   NULL,
    updated_date  datetime     NULL,
    updated_by    BINARY(16)   NULL,
    code          VARCHAR(255) NULL,
    name          VARCHAR(50)  NULL,
    `description` VARCHAR(255) NULL,
    CONSTRAINT pk_notificationtypes PRIMARY KEY (uuid)
);

CREATE TABLE notifications
(
    uuid         BINARY(16)   NOT NULL,
    id_deleted   BIT(1)       NULL,
    created_date datetime     NULL,
    created_by   BINARY(16)   NULL,
    updated_date datetime     NULL,
    updated_by   BINARY(16)   NULL,
    user_id      BINARY(16)   NULL,
    booking_id   BINARY(16)   NULL,
    trip_id      BINARY(16)   NULL,
    type_id      BINARY(16)   NULL,
    channel      VARCHAR(20)  NULL,
    title        VARCHAR(100) NULL,
    message      TEXT         NULL,
    is_sent      BIT(1)       NULL,
    sent_at      datetime     NULL,
    CONSTRAINT pk_notifications PRIMARY KEY (uuid)
);

CREATE TABLE password_reset_tokens
(
    uuid         BINARY(16)   NOT NULL,
    id_deleted   BIT(1)       NULL,
    created_date datetime     NULL,
    created_by   BINARY(16)   NULL,
    updated_date datetime     NULL,
    updated_by   BINARY(16)   NULL,
    user_id      BINARY(16)   NULL,
    token        VARCHAR(255) NOT NULL,
    expires_at   datetime     NOT NULL,
    CONSTRAINT pk_password_reset_tokens PRIMARY KEY (uuid)
);

CREATE TABLE payment_methods
(
    uuid          BINARY(16)   NOT NULL,
    id_deleted    BIT(1)       NULL,
    created_date  datetime     NULL,
    created_by    BINARY(16)   NULL,
    updated_date  datetime     NULL,
    updated_by    BINARY(16)   NULL,
    code          VARCHAR(255) NULL,
    name          VARCHAR(50)  NULL,
    `description` VARCHAR(255) NULL,
    is_active     BIT(1)       NULL,
    CONSTRAINT pk_paymentmethods PRIMARY KEY (uuid)
);

CREATE TABLE payments
(
    uuid             BINARY(16)     NOT NULL,
    id_deleted       BIT(1)         NULL,
    created_date     datetime       NULL,
    created_by       BINARY(16)     NULL,
    updated_date     datetime       NULL,
    updated_by       BINARY(16)     NULL,
    booking_id       BINARY(16)     NULL,
    method_id        BINARY(16)     NULL,
    amount           DECIMAL(10, 2) NULL,
    status           VARCHAR(20)    NULL,
    transaction_code VARCHAR(100)   NULL,
    paid_at          datetime       NULL,
    CONSTRAINT pk_payments PRIMARY KEY (uuid)
);

CREATE TABLE `role`
(
    uuid          BINARY(16)   NOT NULL,
    id_deleted    BIT(1)       NULL,
    created_date  datetime     NULL,
    created_by    BINARY(16)   NULL,
    updated_date  datetime     NULL,
    updated_by    BINARY(16)   NULL,
    code          VARCHAR(100) NULL,
    name          VARCHAR(100) NULL,
    `description` TEXT         NULL,
    CONSTRAINT pk_role PRIMARY KEY (uuid)
);

CREATE TABLE role_permission
(
    uuid              BINARY(16) NOT NULL,
    id_deleted        BIT(1)     NULL,
    created_date      datetime   NULL,
    created_by        BINARY(16) NULL,
    updated_date      datetime   NULL,
    updated_by        BINARY(16) NULL,
    role_id           BINARY(16) NULL,
    is_can_read       BIT(1)     NULL,
    is_can_create     BIT(1)     NULL,
    is_can_update     BIT(1)     NULL,
    is_can_delete     BIT(1)     NULL,
    is_can_activate   BIT(1)     NULL,
    is_can_deactivate BIT(1)     NULL,
    is_can_import     BIT(1)     NULL,
    is_can_export     BIT(1)     NULL,
    CONSTRAINT pk_rolepermission PRIMARY KEY (uuid)
);

CREATE TABLE role_user
(
    uuid         BINARY(16) NOT NULL,
    id_deleted   BIT(1)     NULL,
    created_date datetime   NULL,
    created_by   BINARY(16) NULL,
    updated_date datetime   NULL,
    updated_by   BINARY(16) NULL,
    role_id      BINARY(16) NULL,
    user_id      BINARY(16) NULL,
    CONSTRAINT pk_roleuser PRIMARY KEY (uuid)
);

CREATE TABLE routes
(
    uuid               BINARY(16)    NOT NULL,
    id_deleted         BIT(1)        NULL,
    created_date       datetime      NULL,
    created_by         BINARY(16)    NULL,
    updated_date       datetime      NULL,
    updated_by         BINARY(16)    NULL,
    start_location     VARCHAR(100)  NULL,
    end_location       VARCHAR(100)  NULL,
    distance_km        DECIMAL(6, 2) NULL,
    estimated_duration INT           NULL,
    CONSTRAINT pk_routes PRIMARY KEY (uuid)
);

CREATE TABLE trip_cars
(
    uuid            BINARY(16)     NOT NULL,
    id_deleted      BIT(1)         NULL,
    created_date    datetime       NULL,
    created_by      BINARY(16)     NULL,
    updated_date    datetime       NULL,
    updated_by      BINARY(16)     NULL,
    trip_id         BINARY(16)     NULL,
    car_id          BINARY(16)     NULL,
    price           DECIMAL(10, 2) NULL,
    available_seats INT            NULL,
    CONSTRAINT pk_tripcars PRIMARY KEY (uuid)
);

CREATE TABLE trip_stops
(
    uuid         BINARY(16)   NOT NULL,
    id_deleted   BIT(1)       NULL,
    created_date datetime     NULL,
    created_by   BINARY(16)   NULL,
    updated_date datetime     NULL,
    updated_by   BINARY(16)   NULL,
    trip_id      BINARY(16)   NULL,
    stop_type    VARCHAR(10)  NULL,
    location     VARCHAR(255) NULL,
    order_index  INT          NULL,
    CONSTRAINT pk_tripstops PRIMARY KEY (uuid)
);

CREATE TABLE trips
(
    uuid            BINARY(16)     NOT NULL,
    id_deleted      BIT(1)         NULL,
    created_date    datetime       NULL,
    created_by      BINARY(16)     NULL,
    updated_date    datetime       NULL,
    updated_by      BINARY(16)     NULL,
    route_id        BINARY(16)     NULL,
    departure_time  datetime       NULL,
    price           DECIMAL(10, 2) NULL,
    available_seats INT            NULL,
    CONSTRAINT pk_trips PRIMARY KEY (uuid)
);

CREATE TABLE user_sessions
(
    uuid         BINARY(16)   NOT NULL,
    id_deleted   BIT(1)       NULL,
    created_date datetime     NULL,
    created_by   BINARY(16)   NULL,
    updated_date datetime     NULL,
    updated_by   BINARY(16)   NULL,
    user_id      BINARY(16)   NULL,
    access_token VARCHAR(255) NOT NULL,
    expires_at   datetime     NOT NULL,
    revoked      BIT(1)       NULL,
    CONSTRAINT pk_user_sessions PRIMARY KEY (uuid)
);

CREATE TABLE users
(
    uuid           BINARY(16)   NOT NULL,
    id_deleted     BIT(1)       NULL,
    created_date   datetime     NULL,
    created_by     BINARY(16)   NULL,
    updated_date   datetime     NULL,
    updated_by     BINARY(16)   NULL,
    username       VARCHAR(255) NULL,
    password       VARCHAR(255) NULL,
    is_google      BIT(1)       NULL,
    google_account VARCHAR(255) NULL,
    employee_id    BINARY(16)   NULL,
    customer_id    BINARY(16)   NULL,
    CONSTRAINT pk_users PRIMARY KEY (uuid)
);

ALTER TABLE cars
    ADD CONSTRAINT uc_cars_licenseplate UNIQUE (license_plate);

ALTER TABLE invoices
    ADD CONSTRAINT uc_invoices_invoicenumber UNIQUE (invoice_number);

ALTER TABLE invoices
    ADD CONSTRAINT uc_invoices_paymentid UNIQUE (payment_id);

ALTER TABLE notification_types
    ADD CONSTRAINT uc_notificationtypes_code UNIQUE (code);

ALTER TABLE notification_types
    ADD CONSTRAINT uc_notificationtypes_name UNIQUE (name);

ALTER TABLE password_reset_tokens
    ADD CONSTRAINT uc_password_reset_tokens_token UNIQUE (token);

ALTER TABLE payment_methods
    ADD CONSTRAINT uc_paymentmethods_code UNIQUE (code);

ALTER TABLE payment_methods
    ADD CONSTRAINT uc_paymentmethods_name UNIQUE (name);

ALTER TABLE payments
    ADD CONSTRAINT uc_payments_bookingid UNIQUE (booking_id);

ALTER TABLE `role`
    ADD CONSTRAINT uc_role_code UNIQUE (code);

ALTER TABLE user_sessions
    ADD CONSTRAINT uc_user_sessions_accesstoken UNIQUE (access_token);

ALTER TABLE users
    ADD CONSTRAINT uc_users_customerid UNIQUE (customer_id);

ALTER TABLE users
    ADD CONSTRAINT uc_users_employeeid UNIQUE (employee_id);

ALTER TABLE users
    ADD CONSTRAINT uc_users_username UNIQUE (username);

ALTER TABLE booking_seats
    ADD CONSTRAINT FK_BOOKINGSEATS_ON_BOOKINGID FOREIGN KEY (booking_id) REFERENCES bookings (uuid);

ALTER TABLE booking_seats
    ADD CONSTRAINT FK_BOOKINGSEATS_ON_SEATID FOREIGN KEY (seat_id) REFERENCES car_seats (uuid);

ALTER TABLE bookings
    ADD CONSTRAINT FK_BOOKINGS_ON_DROPOFFSTOPID FOREIGN KEY (dropoff_stop_id) REFERENCES trip_stops (uuid);

ALTER TABLE bookings
    ADD CONSTRAINT FK_BOOKINGS_ON_PICKUPSTOPID FOREIGN KEY (pickup_stop_id) REFERENCES trip_stops (uuid);

ALTER TABLE bookings
    ADD CONSTRAINT FK_BOOKINGS_ON_TRIPID FOREIGN KEY (trip_id) REFERENCES trips (uuid);

ALTER TABLE bookings
    ADD CONSTRAINT FK_BOOKINGS_ON_CUSTOMERID FOREIGN KEY (customer_id) REFERENCES customer(uuid);


ALTER TABLE car_employees
    ADD CONSTRAINT FK_CAREMPLOYEES_ON_CARID FOREIGN KEY (car_id) REFERENCES cars (uuid);

ALTER TABLE car_employees
    ADD CONSTRAINT FK_CAREMPLOYEES_ON_EMPLOYEEID FOREIGN KEY (employee_id) REFERENCES employee (uuid);

ALTER TABLE car_seats
    ADD CONSTRAINT FK_CARSEATS_ON_CARID FOREIGN KEY (car_id) REFERENCES cars (uuid);

ALTER TABLE cars
    ADD CONSTRAINT FK_CARS_ON_CARTYPEID FOREIGN KEY (car_type_id) REFERENCES car_types (uuid);

ALTER TABLE customer
    ADD CONSTRAINT FK_CUSTOMER_ON_CUSTOMERTYPEID FOREIGN KEY (customer_type_id) REFERENCES customer_type (uuid);

ALTER TABLE invoices
    ADD CONSTRAINT FK_INVOICES_ON_PAYMENTID FOREIGN KEY (payment_id) REFERENCES payments (uuid);

ALTER TABLE notifications
    ADD CONSTRAINT FK_NOTIFICATIONS_ON_BOOKINGID FOREIGN KEY (booking_id) REFERENCES bookings (uuid);

ALTER TABLE notifications
    ADD CONSTRAINT FK_NOTIFICATIONS_ON_TRIPID FOREIGN KEY (trip_id) REFERENCES trips (uuid);

ALTER TABLE notifications
    ADD CONSTRAINT FK_NOTIFICATIONS_ON_TYPEID FOREIGN KEY (type_id) REFERENCES notification_types (uuid);

ALTER TABLE notifications
    ADD CONSTRAINT FK_NOTIFICATIONS_ON_USERID FOREIGN KEY (user_id) REFERENCES users (uuid);

ALTER TABLE password_reset_tokens
    ADD CONSTRAINT FK_PASSWORD_RESET_TOKENS_ON_USERID FOREIGN KEY (user_id) REFERENCES users (uuid);

ALTER TABLE payments
    ADD CONSTRAINT FK_PAYMENTS_ON_BOOKINGID FOREIGN KEY (booking_id) REFERENCES bookings (uuid);

ALTER TABLE payments
    ADD CONSTRAINT FK_PAYMENTS_ON_METHODID FOREIGN KEY (method_id) REFERENCES payment_methods (uuid);

ALTER TABLE role_permission
    ADD CONSTRAINT FK_ROLEPERMISSION_ON_ROLEID FOREIGN KEY (role_id) REFERENCES `role` (uuid);

ALTER TABLE role_user
    ADD CONSTRAINT FK_ROLEUSER_ON_ROLEID FOREIGN KEY (role_id) REFERENCES `role` (uuid);

ALTER TABLE role_user
    ADD CONSTRAINT FK_ROLEUSER_ON_USERID FOREIGN KEY (user_id) REFERENCES users (uuid);

ALTER TABLE trip_cars
    ADD CONSTRAINT FK_TRIPCARS_ON_CARID FOREIGN KEY (car_id) REFERENCES cars (uuid);

ALTER TABLE trip_cars
    ADD CONSTRAINT FK_TRIPCARS_ON_TRIPID FOREIGN KEY (trip_id) REFERENCES trips (uuid);

ALTER TABLE trip_stops
    ADD CONSTRAINT FK_TRIPSTOPS_ON_TRIPID FOREIGN KEY (trip_id) REFERENCES trips (uuid);

ALTER TABLE trips
    ADD CONSTRAINT FK_TRIPS_ON_ROUTEID FOREIGN KEY (route_id) REFERENCES routes (uuid);

ALTER TABLE users
    ADD CONSTRAINT FK_USERS_ON_CUSTOMERID FOREIGN KEY (customer_id) REFERENCES customer (uuid);

ALTER TABLE users
    ADD CONSTRAINT FK_USERS_ON_EMPLOYEEID FOREIGN KEY (employee_id) REFERENCES employee (uuid);

ALTER TABLE user_sessions
    ADD CONSTRAINT FK_USER_SESSIONS_ON_USERID FOREIGN KEY (user_id) REFERENCES users (uuid);