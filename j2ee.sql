CREATE TABLE `role`
(
    `id`          varchar(255) PRIMARY KEY,
    `code`        varchar(100) UNIQUE,
    `name`        varchar(100),
    `description` text,
    `isDeleted`   bool,
    `createdDate` datetime,
    `createdBy`   varchar(255),
    `updatedDate` datetime,
    `updatedBy`   varchar(255)
);

CREATE TABLE `rolePermission`
(
    `id`              varchar(255) PRIMARY KEY,
    `roleId`          varchar(255),
    `isCanRead`       bool,
    `isCanCreate`     bool,
    `isCanUpdate`     bool,
    `isCanDelete`     bool,
    `isCanActivate`   bool,
    `isCanDeactivate` bool,
    `isCanImport`     bool,
    `isCanExport`     bool,
    `isDeleted`       bool,
    `createdDate`     datetime,
    `createdBy`       varchar(255),
    `updatedDate`     datetime,
    `updatedBy`       varchar(255)
);

CREATE TABLE `users`
(
    `id`            varchar(255) PRIMARY KEY,
    `username`      varchar(255),
    `password`      varchar(255),
    `isGoogle`      bool,
    `googleAccount` varchar(255),
    `roleId`        varchar(255),
    `employeeId`    varchar(255),
    `customerId`    varchar(255),
    `isDeleted`     bool,
    `createdDate`   datetime,
    `createdBy`     varchar(255),
    `updatedDate`   datetime,
    `updatedBy`     varchar(255)
);

CREATE TABLE `employee`
(
    `id`          varchar(255) PRIMARY KEY,
    `code`        varchar(255),
    `name`        varchar(255),
    `email`       varchar(100),
    `phone`       varchar(15),
    `description` text,
    `isDeleted`   bool,
    `createdDate` datetime,
    `createdBy`   varchar(255),
    `updatedDate` datetime,
    `updatedBy`   varchar(255)
);

CREATE TABLE `customer`
(
    `id`             varchar(255) PRIMARY KEY,
    `code`           varchar(255),
    `name`           varchar(255),
    `email`          varchar(100),
    `phone`          varchar(15),
    `customerTypeId` varchar(255),
    `description`    text,
    `isDeleted`      bool,
    `createdDate`    datetime,
    `createdBy`      varchar(255),
    `updatedDate`    datetime,
    `updatedBy`      varchar(255)
);

CREATE TABLE `customerType`
(
    `id`          varchar(255) PRIMARY KEY,
    `code`        varchar(255),
    `name`        varchar(255),
    `description` text,
    `isDeleted`   bool,
    `createdDate` datetime,
    `createdBy`   varchar(255),
    `updatedDate` datetime,
    `updatedBy`   varchar(255)
);

CREATE TABLE `carTypes`
(
    `id`          varchar(255) PRIMARY KEY,
    `code`        varchar(255),
    `name`        varchar(255),
    `description` varchar(255),
    `seatCount`   int,
    `isDeleted`   bool,
    `createdDate` datetime,
    `createdBy`   varchar(255),
    `updatedDate` datetime,
    `updatedBy`   varchar(255)
);

CREATE TABLE `cars`
(
    `id`           varchar(255) PRIMARY KEY,
    `carTypeId`    varchar(255),
    `licensePlate` varchar(20) UNIQUE,
    `isDeleted`    bool,
    `createdDate`  datetime,
    `createdBy`    varchar(255),
    `updatedDate`  datetime,
    `updatedBy`    varchar(255)
);

CREATE TABLE `carSeats`
(
    `id`           varchar(255) PRIMARY KEY,
    `carId`        varchar(255),
    `seatNumber`   varchar(10),
    `seatPosition` varchar(50),
    `isDeleted`    bool,
    `createdDate`  datetime,
    `createdBy`    varchar(255),
    `updatedDate`  datetime,
    `updatedBy`    varchar(255)
);

CREATE TABLE `carEmployees`
(
    `id`          varchar(255) PRIMARY KEY,
    `carId`       varchar(255),
    `employeeId`  varchar(255),
    `isDeleted`   bool,
    `createdDate` datetime,
    `createdBy`   varchar(255),
    `updatedDate` datetime,
    `updatedBy`   varchar(255)
);

CREATE TABLE `routes`
(
    `id`                varchar(255) PRIMARY KEY,
    `startLocation`     varchar(100),
    `endLocation`       varchar(100),
    `distanceKm`        decimal(6, 2),
    `estimatedDuration` int,
    `isDeleted`         bool,
    `createdDate`       datetime,
    `createdBy`         varchar(255),
    `updatedDate`       datetime,
    `updatedBy`         varchar(255)
);

CREATE TABLE `trips`
(
    `id`             varchar(255) PRIMARY KEY,
    `routeId`        varchar(255),
    `departureTime`  datetime,
    `price`          decimal(10, 2),
    `availableSeats` int,
    `isDeleted`      bool,
    `createdDate`    datetime,
    `createdBy`      varchar(255),
    `updatedDate`    datetime,
    `updatedBy`      varchar(255)
);

CREATE TABLE `tripStops`
(
    `id`          varchar(255) PRIMARY KEY,
    `tripId`      varchar(255),
    `stopType`    varchar(10),
    `location`    varchar(255),
    `orderIndex`  int,
    `isDeleted`   bool,
    `createdDate` datetime,
    `createdBy`   varchar(255),
    `updatedDate` datetime,
    `updatedBy`   varchar(255)
);

CREATE TABLE `tripCars`
(
    `id`             varchar(255) PRIMARY KEY,
    `tripId`         varchar(255),
    `carId`          varchar(255),
    `price`          decimal(10, 2),
    `availableSeats` int,
    `isDeleted`      bool,
    `createdDate`    datetime,
    `createdBy`      varchar(255),
    `updatedDate`    datetime,
    `updatedBy`      varchar(255)
);

CREATE TABLE `bookings`
(
    `id`            varchar(255) PRIMARY KEY,
    `code`          varchar(255),
    `type`          varchar(255),
    `userId`        varchar(255),
    `tripId`        varchar(255),
    `pickupStopId`  varchar(255),
    `dropoffStopId` varchar(255),
    `bookingStatus` varchar(20),
    `totalPrice`    decimal(10, 2),
    `isDeleted`     bool,
    `createdDate`   datetime,
    `createdBy`     varchar(255),
    `updatedDate`   datetime,
    `updatedBy`     varchar(255)
);

CREATE TABLE `bookingSeats`
(
    `id`          varchar(255) PRIMARY KEY,
    `bookingId`   varchar(255),
    `seatId`      varchar(255),
    `status`      varchar(20),
    `price`       decimal(10, 2),
    `isDeleted`   bool,
    `createdDate` datetime,
    `createdBy`   varchar(255),
    `updatedDate` datetime,
    `updatedBy`   varchar(255)
);

CREATE TABLE `paymentMethods`
(
    `id`          varchar(255) PRIMARY KEY,
    `code`        varchar(255) UNIQUE,
    `name`        varchar(50) UNIQUE,
    `description` varchar(255),
    `isActive`    bool,
    `isDeleted`   bool,
    `createdDate` datetime,
    `createdBy`   varchar(255),
    `updatedDate` datetime,
    `updatedBy`   varchar(255)
);

CREATE TABLE `payments`
(
    `id`              varchar(255) PRIMARY KEY,
    `bookingId`       varchar(255),
    `methodId`        varchar(255),
    `amount`          decimal(10, 2),
    `status`          varchar(20),
    `transactionCode` varchar(100),
    `paidAt`          datetime,
    `isDeleted`       bool,
    `createdDate`     datetime,
    `createdBy`       varchar(255),
    `updatedDate`     datetime,
    `updatedBy`       varchar(255)
);

CREATE TABLE `invoices`
(
    `id`            varchar(255) PRIMARY KEY,
    `paymentId`     varchar(255),
    `invoiceNumber` varchar(50) UNIQUE,
    `fileUrl`       varchar(255),
    `issuedAt`      datetime,
    `isDeleted`     bool,
    `createdDate`   datetime,
    `createdBy`     varchar(255),
    `updatedDate`   datetime,
    `updatedBy`     varchar(255)
);

CREATE TABLE `notificationTypes`
(
    `id`          varchar(255) PRIMARY KEY,
    `code`        varchar(255) UNIQUE,
    `name`        varchar(50) UNIQUE,
    `description` varchar(255),
    `isDeleted`   bool,
    `createdDate` datetime,
    `createdBy`   varchar(255),
    `updatedDate` datetime,
    `updatedBy`   varchar(255)
);

CREATE TABLE `notifications`
(
    `id`          varchar(255) PRIMARY KEY,
    `userId`      varchar(255),
    `bookingId`   varchar(255),
    `tripId`      varchar(255),
    `typeId`      varchar(255),
    `channel`     varchar(20),
    `title`       varchar(100),
    `message`     text,
    `isSent`      bool,
    `sentAt`      datetime,
    `isDeleted`   bool,
    `createdDate` datetime,
    `createdBy`   varchar(255),
    `updatedDate` datetime,
    `updatedBy`   varchar(255)
);

ALTER TABLE `users`
    ADD FOREIGN KEY (`roleId`) REFERENCES `role` (`id`);

ALTER TABLE `users`
    ADD FOREIGN KEY (`employeeId`) REFERENCES `employee` (`id`);

ALTER TABLE `users`
    ADD FOREIGN KEY (`customerId`) REFERENCES `customer` (`id`);

ALTER TABLE `carEmployees`
    ADD FOREIGN KEY (`employeeId`) REFERENCES `employee` (`id`);

ALTER TABLE `carEmployees`
    ADD FOREIGN KEY (`carId`) REFERENCES `cars` (`id`);

ALTER TABLE `customer`
    ADD FOREIGN KEY (`customerTypeId`) REFERENCES `customerType` (`id`);

ALTER TABLE `cars`
    ADD FOREIGN KEY (`carTypeId`) REFERENCES `carTypes` (`id`);

ALTER TABLE `carSeats`
    ADD FOREIGN KEY (`carId`) REFERENCES `cars` (`id`);

ALTER TABLE `trips`
    ADD FOREIGN KEY (`routeId`) REFERENCES `routes` (`id`);

ALTER TABLE `tripCars`
    ADD FOREIGN KEY (`tripId`) REFERENCES `trips` (`id`);

ALTER TABLE `tripCars`
    ADD FOREIGN KEY (`carId`) REFERENCES `cars` (`id`);

ALTER TABLE `tripStops`
    ADD FOREIGN KEY (`tripId`) REFERENCES `trips` (`id`);

ALTER TABLE `bookings`
    ADD FOREIGN KEY (`userId`) REFERENCES `users` (`id`);

ALTER TABLE `bookings`
    ADD FOREIGN KEY (`tripId`) REFERENCES `trips` (`id`);

ALTER TABLE `bookings`
    ADD FOREIGN KEY (`pickupStopId`) REFERENCES `tripStops` (`id`);

ALTER TABLE `bookings`
    ADD FOREIGN KEY (`dropoffStopId`) REFERENCES `tripStops` (`id`);

ALTER TABLE `bookingSeats`
    ADD FOREIGN KEY (`bookingId`) REFERENCES `bookings` (`id`);

ALTER TABLE `bookingSeats`
    ADD FOREIGN KEY (`seatId`) REFERENCES `carSeats` (`id`);

ALTER TABLE `payments`
    ADD FOREIGN KEY (`bookingId`) REFERENCES `bookings` (`id`);

ALTER TABLE `payments`
    ADD FOREIGN KEY (`methodId`) REFERENCES `paymentMethods` (`id`);

ALTER TABLE `invoices`
    ADD FOREIGN KEY (`paymentId`) REFERENCES `payments` (`id`);

ALTER TABLE `notifications`
    ADD FOREIGN KEY (`userId`) REFERENCES `users` (`id`);

ALTER TABLE `notifications`
    ADD FOREIGN KEY (`bookingId`) REFERENCES `bookings` (`id`);

ALTER TABLE `notifications`
    ADD FOREIGN KEY (`tripId`) REFERENCES `trips` (`id`);

ALTER TABLE `notifications`
    ADD FOREIGN KEY (`typeId`) REFERENCES `notificationTypes` (`id`);
