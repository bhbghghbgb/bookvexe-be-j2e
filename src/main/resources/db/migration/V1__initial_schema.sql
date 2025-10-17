CREATE TABLE booking_seats
(
    uuid             BINARY(16)     NOT NULL,
    `xóa mềm`        BIT(1)         NULL,
    `ngày tạo`       datetime       NULL,
    `người tạo`      BINARY(16)     NULL,
    `ngày cập nhật`  datetime       NULL,
    `người cập nhật` BINARY(16)     NULL,
    booking_id       BINARY(16)     NULL,
    seat_id          BINARY(16)     NULL,
    `trạng thái`     VARCHAR(20)    NULL,
    `giá`            DECIMAL(10, 2) NULL,
    CONSTRAINT pk_bookingseats PRIMARY KEY (uuid)
);

CREATE TABLE bookings
(
    uuid                 BINARY(16)     NOT NULL,
    `xóa mềm`            BIT(1)         NULL,
    `ngày tạo`           datetime       NULL,
    `người tạo`          BINARY(16)     NULL,
    `ngày cập nhật`      datetime       NULL,
    `người cập nhật`     BINARY(16)     NULL,
    `mã đặt chỗ`         VARCHAR(255)   NULL,
    `loại đặt chỗ`       VARCHAR(255)   NULL,
    user_id              BINARY(16)     NULL,
    trip_id              BINARY(16)     NULL,
    pickup_stop_id       BINARY(16)     NULL,
    dropoff_stop_id      BINARY(16)     NULL,
    `trạng thái đặt chỗ` VARCHAR(20)    NULL,
    `tổng giá`           DECIMAL(10, 2) NULL,
    CONSTRAINT pk_bookings PRIMARY KEY (uuid)
);

CREATE TABLE car_employees
(
    uuid             BINARY(16) NOT NULL,
    `xóa mềm`        BIT(1)     NULL,
    `ngày tạo`       datetime   NULL,
    `người tạo`      BINARY(16) NULL,
    `ngày cập nhật`  datetime   NULL,
    `người cập nhật` BINARY(16) NULL,
    car_id           BINARY(16) NULL,
    employee_id      BINARY(16) NULL,
    CONSTRAINT pk_caremployees PRIMARY KEY (uuid)
);

CREATE TABLE car_seats
(
    uuid             BINARY(16)  NOT NULL,
    `xóa mềm`        BIT(1)      NULL,
    `ngày tạo`       datetime    NULL,
    `người tạo`      BINARY(16)  NULL,
    `ngày cập nhật`  datetime    NULL,
    `người cập nhật` BINARY(16)  NULL,
    car_id           BINARY(16)  NULL,
    `số ghế`         VARCHAR(10) NULL,
    `vị trí ghế`     VARCHAR(50) NULL,
    CONSTRAINT pk_carseats PRIMARY KEY (uuid)
);

CREATE TABLE car_types
(
    uuid             BINARY(16)   NOT NULL,
    `xóa mềm`        BIT(1)       NULL,
    `ngày tạo`       datetime     NULL,
    `người tạo`      BINARY(16)   NULL,
    `ngày cập nhật`  datetime     NULL,
    `người cập nhật` BINARY(16)   NULL,
    `mã loại xe`     VARCHAR(255) NULL,
    `tên loại xe`    VARCHAR(255) NULL,
    `mô tả`          VARCHAR(255) NULL,
    `số ghế`         INT          NULL,
    CONSTRAINT pk_cartypes PRIMARY KEY (uuid)
);

CREATE TABLE cars
(
    uuid             BINARY(16)  NOT NULL,
    `xóa mềm`        BIT(1)      NULL,
    `ngày tạo`       datetime    NULL,
    `người tạo`      BINARY(16)  NULL,
    `ngày cập nhật`  datetime    NULL,
    `người cập nhật` BINARY(16)  NULL,
    car_type_id      BINARY(16)  NULL,
    `biển số xe`     VARCHAR(20) NULL,
    CONSTRAINT pk_cars PRIMARY KEY (uuid)
);

CREATE TABLE customer
(
    uuid             BINARY(16)   NOT NULL,
    `xóa mềm`        BIT(1)       NULL,
    `ngày tạo`       datetime     NULL,
    `người tạo`      BINARY(16)   NULL,
    `ngày cập nhật`  datetime     NULL,
    `người cập nhật` BINARY(16)   NULL,
    `mã khách hàng`  VARCHAR(255) NULL,
    `tên khách hàng` VARCHAR(255) NULL,
    email            VARCHAR(100) NULL,
    `số điện thoại`  VARCHAR(15)  NULL,
    customer_type_id BINARY(16)   NULL,
    `description`    TEXT         NULL,
    CONSTRAINT pk_customer PRIMARY KEY (uuid)
);

CREATE TABLE customer_type
(
    uuid                  BINARY(16)   NOT NULL,
    `xóa mềm`             BIT(1)       NULL,
    `ngày tạo`            datetime     NULL,
    `người tạo`           BINARY(16)   NULL,
    `ngày cập nhật`       datetime     NULL,
    `người cập nhật`      BINARY(16)   NULL,
    `mã loại khách hàng`  VARCHAR(255) NULL,
    `tên loại khách hàng` VARCHAR(255) NULL,
    `mô tả`               TEXT         NULL,
    CONSTRAINT pk_customertype PRIMARY KEY (uuid)
);

CREATE TABLE employee
(
    uuid             BINARY(16)   NOT NULL,
    `xóa mềm`        BIT(1)       NULL,
    `ngày tạo`       datetime     NULL,
    `người tạo`      BINARY(16)   NULL,
    `ngày cập nhật`  datetime     NULL,
    `người cập nhật` BINARY(16)   NULL,
    `mã nhân viên`   VARCHAR(255) NULL,
    `tên nhân viên`  VARCHAR(255) NULL,
    email            VARCHAR(100) NULL,
    `số điện thoại`  VARCHAR(15)  NULL,
    `mô tả`          TEXT         NULL,
    CONSTRAINT pk_employee PRIMARY KEY (uuid)
);

CREATE TABLE invoices
(
    uuid             BINARY(16)   NOT NULL,
    `xóa mềm`        BIT(1)       NULL,
    `ngày tạo`       datetime     NULL,
    `người tạo`      BINARY(16)   NULL,
    `ngày cập nhật`  datetime     NULL,
    `người cập nhật` BINARY(16)   NULL,
    payment_id       BINARY(16)   NULL,
    `số hóa đơn`     VARCHAR(50)  NULL,
    `file url`       VARCHAR(255) NULL,
    `ngày phát hành` datetime     NULL,
    CONSTRAINT pk_invoices PRIMARY KEY (uuid)
);

CREATE TABLE notification_types
(
    uuid                 BINARY(16)   NOT NULL,
    `xóa mềm`            BIT(1)       NULL,
    `ngày tạo`           datetime     NULL,
    `người tạo`          BINARY(16)   NULL,
    `ngày cập nhật`      datetime     NULL,
    `người cập nhật`     BINARY(16)   NULL,
    `mã loại thông báo`  VARCHAR(255) NULL,
    `tên loại thông báo` VARCHAR(50)  NULL,
    `mô tả`              VARCHAR(255) NULL,
    CONSTRAINT pk_notificationtypes PRIMARY KEY (uuid)
);

CREATE TABLE notifications
(
    uuid             BINARY(16)   NOT NULL,
    `xóa mềm`        BIT(1)       NULL,
    `ngày tạo`       datetime     NULL,
    `người tạo`      BINARY(16)   NULL,
    `ngày cập nhật`  datetime     NULL,
    `người cập nhật` BINARY(16)   NULL,
    user_id          BINARY(16)   NULL,
    booking_id       BINARY(16)   NULL,
    trip_id          BINARY(16)   NULL,
    type_id          BINARY(16)   NULL,
    `kênh`           VARCHAR(20)  NULL,
    `tiêu đề`        VARCHAR(100) NULL,
    `nội dung`       TEXT         NULL,
    `đã gửi`         BIT(1)       NULL,
    `thời gian gửi`  datetime     NULL,
    CONSTRAINT pk_notifications PRIMARY KEY (uuid)
);

CREATE TABLE password_reset_tokens
(
    uuid             BINARY(16)   NOT NULL,
    `xóa mềm`        BIT(1)       NULL,
    `ngày tạo`       datetime     NULL,
    `người tạo`      BINARY(16)   NULL,
    `ngày cập nhật`  datetime     NULL,
    `người cập nhật` BINARY(16)   NULL,
    user_id          BINARY(16)   NULL,
    token            VARCHAR(255) NOT NULL,
    expires_at       datetime     NOT NULL,
    CONSTRAINT pk_password_reset_tokens PRIMARY KEY (uuid)
);

CREATE TABLE payment_methods
(
    uuid                         BINARY(16)   NOT NULL,
    `xóa mềm`                    BIT(1)       NULL,
    `ngày tạo`                   datetime     NULL,
    `người tạo`                  BINARY(16)   NULL,
    `ngày cập nhật`              datetime     NULL,
    `người cập nhật`             BINARY(16)   NULL,
    `mã phương thức thanh toán`  VARCHAR(255) NULL,
    `tên phương thức thanh toán` VARCHAR(50)  NULL,
    `mô tả`                      VARCHAR(255) NULL,
    CONSTRAINT pk_paymentmethods PRIMARY KEY (uuid)
);

CREATE TABLE payments
(
    uuid              BINARY(16)     NOT NULL,
    `xóa mềm`         BIT(1)         NULL,
    `ngày tạo`        datetime       NULL,
    `người tạo`       BINARY(16)     NULL,
    `ngày cập nhật`   datetime       NULL,
    `người cập nhật`  BINARY(16)     NULL,
    booking_id        BINARY(16)     NULL,
    method_id         BINARY(16)     NULL,
    `số tiền`         DECIMAL(10, 2) NULL,
    `trạng thái`      VARCHAR(20)    NULL,
    `mã giao dịch`    VARCHAR(100)   NULL,
    `ngày thanh toán` datetime       NULL,
    CONSTRAINT pk_payments PRIMARY KEY (uuid)
);

CREATE TABLE `role`
(
    uuid             BINARY(16)   NOT NULL,
    `xóa mềm`        BIT(1)       NULL,
    `ngày tạo`       datetime     NULL,
    `người tạo`      BINARY(16)   NULL,
    `ngày cập nhật`  datetime     NULL,
    `người cập nhật` BINARY(16)   NULL,
    `mã vai trò`     VARCHAR(100) NULL,
    `tên vai trò`    VARCHAR(100) NULL,
    `mô tả`          TEXT         NULL,
    CONSTRAINT pk_role PRIMARY KEY (uuid)
);

CREATE TABLE role_permission
(
    uuid                 BINARY(16) NOT NULL,
    `xóa mềm`            BIT(1)     NULL,
    `ngày tạo`           datetime   NULL,
    `người tạo`          BINARY(16) NULL,
    `ngày cập nhật`      datetime   NULL,
    `người cập nhật`     BINARY(16) NULL,
    role_id              BINARY(16) NULL,
    `quyền xem chi tiết` BIT(1)     NULL,
    `quyền tạo`          BIT(1)     NULL,
    `quyền cập nhật`     BIT(1)     NULL,
    `quyền xóa`          BIT(1)     NULL,
    `quyền kích hoạt`    BIT(1)     NULL,
    `quyền vô hiệu hóa`  BIT(1)     NULL,
    `quyền nhập khẩu`    BIT(1)     NULL,
    `quyền xuất khẩu`    BIT(1)     NULL,
    CONSTRAINT pk_rolepermission PRIMARY KEY (uuid)
);

CREATE TABLE role_user
(
    uuid             BINARY(16) NOT NULL,
    `xóa mềm`        BIT(1)     NULL,
    `ngày tạo`       datetime   NULL,
    `người tạo`      BINARY(16) NULL,
    `ngày cập nhật`  datetime   NULL,
    `người cập nhật` BINARY(16) NULL,
    role_id          BINARY(16) NULL,
    user_id          BINARY(16) NULL,
    CONSTRAINT pk_roleuser PRIMARY KEY (uuid)
);

CREATE TABLE routes
(
    uuid                        BINARY(16)    NOT NULL,
    `xóa mềm`                   BIT(1)        NULL,
    `ngày tạo`                  datetime      NULL,
    `người tạo`                 BINARY(16)    NULL,
    `ngày cập nhật`             datetime      NULL,
    `người cập nhật`            BINARY(16)    NULL,
    `điểm bắt đầu`              VARCHAR(100)  NULL,
    `điểm kết thúc`             VARCHAR(100)  NULL,
    `khoảng cách (km)`          DECIMAL(6, 2) NULL,
    `thời gian ước tính (phút)` INT           NULL,
    CONSTRAINT pk_routes PRIMARY KEY (uuid)
);

CREATE TABLE trip_cars
(
    uuid             BINARY(16)     NOT NULL,
    `xóa mềm`        BIT(1)         NULL,
    `ngày tạo`       datetime       NULL,
    `người tạo`      BINARY(16)     NULL,
    `ngày cập nhật`  datetime       NULL,
    `người cập nhật` BINARY(16)     NULL,
    trip_id          BINARY(16)     NULL,
    car_id           BINARY(16)     NULL,
    `giá`            DECIMAL(10, 2) NULL,
    `ghế còn trống`  INT            NULL,
    CONSTRAINT pk_tripcars PRIMARY KEY (uuid)
);

CREATE TABLE trip_stops
(
    uuid             BINARY(16)   NOT NULL,
    `xóa mềm`        BIT(1)       NULL,
    `ngày tạo`       datetime     NULL,
    `người tạo`      BINARY(16)   NULL,
    `ngày cập nhật`  datetime     NULL,
    `người cập nhật` BINARY(16)   NULL,
    trip_id          BINARY(16)   NULL,
    `loại điểm dừng` VARCHAR(10)  NULL,
    `địa điểm`       VARCHAR(255) NULL,
    `chỉ số thứ tự`  INT          NULL,
    CONSTRAINT pk_tripstops PRIMARY KEY (uuid)
);

CREATE TABLE trips
(
    uuid                  BINARY(16)     NOT NULL,
    `xóa mềm`             BIT(1)         NULL,
    `ngày tạo`            datetime       NULL,
    `người tạo`           BINARY(16)     NULL,
    `ngày cập nhật`       datetime       NULL,
    `người cập nhật`      BINARY(16)     NULL,
    route_id              BINARY(16)     NULL,
    `thời gian khởi hành` datetime       NULL,
    `giá`                 DECIMAL(10, 2) NULL,
    `ghế còn trống`       INT            NULL,
    CONSTRAINT pk_trips PRIMARY KEY (uuid)
);

CREATE TABLE user_sessions
(
    uuid             BINARY(16)   NOT NULL,
    `xóa mềm`        BIT(1)       NULL,
    `ngày tạo`       datetime     NULL,
    `người tạo`      BINARY(16)   NULL,
    `ngày cập nhật`  datetime     NULL,
    `người cập nhật` BINARY(16)   NULL,
    user_id          BINARY(16)   NULL,
    access_token     VARCHAR(255) NOT NULL,
    expires_at       datetime     NOT NULL,
    revoked          BIT(1)       NULL,
    CONSTRAINT pk_user_sessions PRIMARY KEY (uuid)
);

CREATE TABLE users
(
    uuid                    BINARY(16)   NOT NULL,
    `xóa mềm`               BIT(1)       NULL,
    `ngày tạo`              datetime     NULL,
    `người tạo`             BINARY(16)   NULL,
    `ngày cập nhật`         datetime     NULL,
    `người cập nhật`        BINARY(16)   NULL,
    `tên đăng nhập`         VARCHAR(255) NULL,
    `mật khẩu`              VARCHAR(255) NULL,
    `đăng nhập bằng google` BIT(1)       NULL,
    `tài khoản google`      VARCHAR(255) NULL,
    employee_id             BINARY(16)   NULL,
    customer_id             BINARY(16)   NULL,
    CONSTRAINT pk_users PRIMARY KEY (uuid)
);

ALTER TABLE cars
    ADD CONSTRAINT `uc_cars_biển_số_xe` UNIQUE (`biển số xe`);

ALTER TABLE invoices
    ADD CONSTRAINT uc_invoices_paymentid UNIQUE (payment_id);

ALTER TABLE invoices
    ADD CONSTRAINT `uc_invoices_số_hóa_đơn` UNIQUE (`số hóa đơn`);

ALTER TABLE notification_types
    ADD CONSTRAINT `uc_notificationtypes_mã_loại_thông_báo` UNIQUE (`mã loại thông báo`);

ALTER TABLE notification_types
    ADD CONSTRAINT `uc_notificationtypes_tên_loại_thông_báo` UNIQUE (`tên loại thông báo`);

ALTER TABLE password_reset_tokens
    ADD CONSTRAINT uc_password_reset_tokens_token UNIQUE (token);

ALTER TABLE payment_methods
    ADD CONSTRAINT `uc_paymentmethods_mã_phương_thức_thanh_toán` UNIQUE (`mã phương thức thanh toán`);

ALTER TABLE payment_methods
    ADD CONSTRAINT `uc_paymentmethods_tên_phương_thức_thanh_toán` UNIQUE (`tên phương thức thanh toán`);

ALTER TABLE payments
    ADD CONSTRAINT uc_payments_bookingid UNIQUE (booking_id);

ALTER TABLE `role`
    ADD CONSTRAINT `uc_role_mã_vai_trò` UNIQUE (`mã vai trò`);

ALTER TABLE user_sessions
    ADD CONSTRAINT uc_user_sessions_accesstoken UNIQUE (access_token);

ALTER TABLE users
    ADD CONSTRAINT uc_users_customerid UNIQUE (customer_id);

ALTER TABLE users
    ADD CONSTRAINT uc_users_employeeid UNIQUE (employee_id);

ALTER TABLE users
    ADD CONSTRAINT `uc_users_tên_đăng_nhập` UNIQUE (`tên đăng nhập`);

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
    ADD CONSTRAINT FK_BOOKINGS_ON_USERID FOREIGN KEY (user_id) REFERENCES users (uuid);

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