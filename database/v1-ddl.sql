create table car_types
(
    car_type_id int auto_increment
        primary key,
    type_name   varchar(50)  not null,
    description varchar(255) null,
    constraint uc_car_types_type_name
        unique (type_name)
);

create table flyway_schema_history
(
    installed_rank int                                 not null
        primary key,
    version        varchar(50)                         null,
    description    varchar(200)                        not null,
    type           varchar(20)                         not null,
    script         varchar(1000)                       not null,
    checksum       int                                 null,
    installed_by   varchar(100)                        not null,
    installed_on   timestamp default CURRENT_TIMESTAMP not null,
    execution_time int                                 not null,
    success        tinyint(1)                          not null
);

create index flyway_schema_history_s_idx
    on flyway_schema_history (success);

create table notification_types
(
    type_id     int auto_increment
        primary key,
    type_name   varchar(50)  not null,
    description varchar(255) null,
    constraint uc_notification_types_type_name
        unique (type_name)
);

create table payment_methods
(
    method_id   int auto_increment
        primary key,
    method_name varchar(50)  not null,
    description varchar(255) null,
    is_active   bit          null,
    constraint uc_payment_methods_method_name
        unique (method_name)
);

create table role
(
    id         int auto_increment
        primary key,
    code       varchar(100) not null,
    name       varchar(100) null,
    created_at datetime     null,
    constraint uc_role_code
        unique (code)
);

create table role_permission
(
    role_id        int not null
        primary key,
    can_read       bit null,
    can_create     bit null,
    can_update     bit null,
    can_delete     bit null,
    can_activate   bit null,
    can_deactivate bit null,
    can_import     bit null,
    can_export     bit null,
    constraint FK_ROLE_PERMISSION_ON_ROLE
        foreign key (role_id) references role (id)
);

create table routes
(
    route_id           int auto_increment
        primary key,
    start_location     varchar(100)  not null,
    end_location       varchar(100)  not null,
    distance_km        decimal(6, 2) null,
    estimated_duration int           null,
    created_at         datetime      null,
    updated_at         datetime      null
);

create table users
(
    user_id       int auto_increment
        primary key,
    user_uuid     binary(16)   null,
    full_name     varchar(100) not null,
    email         varchar(100) not null,
    phone_number  varchar(15)  not null,
    password_hash varchar(255) not null,
    role_id       int          null,
    is_active     bit          null,
    created_at    datetime     null,
    updated_at    datetime     null,
    constraint uc_users_email
        unique (email),
    constraint uc_users_phone_number
        unique (phone_number),
    constraint uc_users_user_uuid
        unique (user_uuid),
    constraint FK_USERS_ON_ROLE
        foreign key (role_id) references role (id)
);

create table cars
(
    car_id        int auto_increment
        primary key,
    owner_id      int         null,
    car_type_id   int         null,
    license_plate varchar(20) not null,
    seat_count    int         not null,
    created_at    datetime    null,
    updated_at    datetime    null,
    constraint uc_cars_license_plate
        unique (license_plate),
    constraint FK_CARS_ON_CAR_TYPE
        foreign key (car_type_id) references car_types (car_type_id),
    constraint FK_CARS_ON_OWNER
        foreign key (owner_id) references users (user_id)
);

create table car_seats
(
    seat_id       int auto_increment
        primary key,
    car_id        int         null,
    seat_number   varchar(10) not null,
    seat_position varchar(50) null,
    is_active     bit         null,
    constraint FK_CAR_SEATS_ON_CAR
        foreign key (car_id) references cars (car_id)
);

create table password_reset_tokens
(
    token_id   int auto_increment
        primary key,
    user_id    int          null,
    token      varchar(255) not null,
    expires_at datetime     not null,
    created_at datetime     null,
    constraint uc_password_reset_tokens_token
        unique (token),
    constraint FK_PASSWORD_RESET_TOKENS_ON_USER
        foreign key (user_id) references users (user_id)
);

create table role_user
(
    id        int auto_increment
        primary key,
    user_id   int null,
    role_id   int null,
    is_active bit null,
    constraint FK_ROLE_USER_ON_ROLE
        foreign key (role_id) references role (id),
    constraint FK_ROLE_USER_ON_USER
        foreign key (user_id) references users (user_id)
);

create table trips
(
    trip_id         int auto_increment
        primary key,
    route_id        int            null,
    bus_id          int            null,
    departure_time  datetime       not null,
    price           decimal(10, 2) not null,
    available_seats int            not null,
    created_at      datetime       null,
    updated_at      datetime       null,
    constraint FK_TRIPS_ON_BUS
        foreign key (bus_id) references cars (car_id),
    constraint FK_TRIPS_ON_ROUTE
        foreign key (route_id) references routes (route_id)
);

create table bookings
(
    booking_id     int auto_increment
        primary key,
    user_id        int            null,
    trip_id        int            null,
    booking_status varchar(20)    not null,
    total_price    decimal(10, 2) not null,
    created_at     datetime       null,
    updated_at     datetime       null,
    constraint FK_BOOKINGS_ON_TRIP
        foreign key (trip_id) references trips (trip_id),
    constraint FK_BOOKINGS_ON_USER
        foreign key (user_id) references users (user_id)
);

create table booking_seats
(
    booking_seat_id int auto_increment
        primary key,
    booking_id      int            null,
    seat_id         int            null,
    is_reserved     bit            null,
    price           decimal(10, 2) not null,
    constraint FK_BOOKING_SEATS_ON_BOOKING
        foreign key (booking_id) references bookings (booking_id),
    constraint FK_BOOKING_SEATS_ON_SEAT
        foreign key (seat_id) references car_seats (seat_id)
);

create table notifications
(
    notification_id int auto_increment
        primary key,
    user_id         int          null,
    booking_id      int          null,
    trip_id         int          null,
    type_id         int          null,
    channel         varchar(20)  not null,
    title           varchar(100) not null,
    message         text         not null,
    is_sent         bit          null,
    sent_at         datetime     null,
    created_at      datetime     null,
    constraint FK_NOTIFICATIONS_ON_BOOKING
        foreign key (booking_id) references bookings (booking_id),
    constraint FK_NOTIFICATIONS_ON_TRIP
        foreign key (trip_id) references trips (trip_id),
    constraint FK_NOTIFICATIONS_ON_TYPE
        foreign key (type_id) references notification_types (type_id),
    constraint FK_NOTIFICATIONS_ON_USER
        foreign key (user_id) references users (user_id)
);

create table payments
(
    payment_id       int auto_increment
        primary key,
    booking_id       int            null,
    method_id        int            null,
    amount           decimal(10, 2) not null,
    status           varchar(20)    not null,
    transaction_code varchar(100)   null,
    paid_at          datetime       null,
    created_at       datetime       null,
    updated_at       datetime       null,
    constraint FK_PAYMENTS_ON_BOOKING
        foreign key (booking_id) references bookings (booking_id),
    constraint FK_PAYMENTS_ON_METHOD
        foreign key (method_id) references payment_methods (method_id)
);

create table invoices
(
    invoice_id     int auto_increment
        primary key,
    payment_id     int          null,
    invoice_number varchar(50)  not null,
    file_url       varchar(255) null,
    issued_at      datetime     null,
    created_at     datetime     null,
    constraint uc_invoices_invoice_number
        unique (invoice_number),
    constraint FK_INVOICES_ON_PAYMENT
        foreign key (payment_id) references payments (payment_id)
);

create table user_sessions
(
    session_id   int auto_increment
        primary key,
    user_id      int          null,
    access_token varchar(255) not null,
    expires_at   datetime     not null,
    revoked      bit          null,
    created_at   datetime     null,
    constraint uc_user_sessions_access_token
        unique (access_token),
    constraint FK_USER_SESSIONS_ON_USER
        foreign key (user_id) references users (user_id)
);

