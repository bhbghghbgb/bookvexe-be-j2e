-- MySQL dump 10.13  Distrib 9.4.0, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: bookvexedb_j2e
-- ------------------------------------------------------
-- Server version	9.4.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE = @@TIME_ZONE */;
/*!40103 SET TIME_ZONE = '+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;

--
-- Table structure for table `booking_seats`
--

DROP TABLE IF EXISTS `booking_seats`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `booking_seats`
(
    `booking_seat_id` int            NOT NULL AUTO_INCREMENT,
    `booking_id`      int    DEFAULT NULL,
    `seat_id`         int    DEFAULT NULL,
    `is_reserved`     bit(1) DEFAULT NULL,
    `price`           decimal(10, 2) NOT NULL,
    PRIMARY KEY (`booking_seat_id`),
    KEY `FK_BOOKING_SEATS_ON_BOOKING` (`booking_id`),
    KEY `FK_BOOKING_SEATS_ON_SEAT` (`seat_id`),
    CONSTRAINT `FK_BOOKING_SEATS_ON_BOOKING` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`booking_id`),
    CONSTRAINT `FK_BOOKING_SEATS_ON_SEAT` FOREIGN KEY (`seat_id`) REFERENCES `car_seats` (`seat_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `booking_seats`
--

LOCK TABLES `booking_seats` WRITE;
/*!40000 ALTER TABLE `booking_seats`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `booking_seats`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bookings`
--

DROP TABLE IF EXISTS `bookings`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bookings`
(
    `booking_id`     int                                    NOT NULL AUTO_INCREMENT,
    `user_id`        int      DEFAULT NULL,
    `trip_id`        int      DEFAULT NULL,
    `booking_status` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
    `total_price`    decimal(10, 2)                         NOT NULL,
    `created_at`     datetime DEFAULT NULL,
    `updated_at`     datetime DEFAULT NULL,
    PRIMARY KEY (`booking_id`),
    KEY `FK_BOOKINGS_ON_TRIP` (`trip_id`),
    KEY `FK_BOOKINGS_ON_USER` (`user_id`),
    CONSTRAINT `FK_BOOKINGS_ON_TRIP` FOREIGN KEY (`trip_id`) REFERENCES `trips` (`trip_id`),
    CONSTRAINT `FK_BOOKINGS_ON_USER` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bookings`
--

LOCK TABLES `bookings` WRITE;
/*!40000 ALTER TABLE `bookings`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `bookings`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `car_seats`
--

DROP TABLE IF EXISTS `car_seats`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `car_seats`
(
    `seat_id`       int                                    NOT NULL AUTO_INCREMENT,
    `car_id`        int                                    DEFAULT NULL,
    `seat_number`   varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
    `seat_position` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `is_active`     bit(1)                                 DEFAULT NULL,
    PRIMARY KEY (`seat_id`),
    KEY `FK_CAR_SEATS_ON_CAR` (`car_id`),
    CONSTRAINT `FK_CAR_SEATS_ON_CAR` FOREIGN KEY (`car_id`) REFERENCES `cars` (`car_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `car_seats`
--

LOCK TABLES `car_seats` WRITE;
/*!40000 ALTER TABLE `car_seats`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `car_seats`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `car_types`
--

DROP TABLE IF EXISTS `car_types`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `car_types`
(
    `car_type_id` int                                    NOT NULL AUTO_INCREMENT,
    `type_name`   varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
    `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`car_type_id`),
    UNIQUE KEY `uc_car_types_type_name` (`type_name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `car_types`
--

LOCK TABLES `car_types` WRITE;
/*!40000 ALTER TABLE `car_types`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `car_types`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cars`
--

DROP TABLE IF EXISTS `cars`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cars`
(
    `car_id`        int                                    NOT NULL AUTO_INCREMENT,
    `owner_id`      int      DEFAULT NULL,
    `car_type_id`   int      DEFAULT NULL,
    `license_plate` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
    `seat_count`    int                                    NOT NULL,
    `created_at`    datetime DEFAULT NULL,
    `updated_at`    datetime DEFAULT NULL,
    PRIMARY KEY (`car_id`),
    UNIQUE KEY `uc_cars_license_plate` (`license_plate`),
    KEY `FK_CARS_ON_CAR_TYPE` (`car_type_id`),
    KEY `FK_CARS_ON_OWNER` (`owner_id`),
    CONSTRAINT `FK_CARS_ON_CAR_TYPE` FOREIGN KEY (`car_type_id`) REFERENCES `car_types` (`car_type_id`),
    CONSTRAINT `FK_CARS_ON_OWNER` FOREIGN KEY (`owner_id`) REFERENCES `users` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cars`
--

LOCK TABLES `cars` WRITE;
/*!40000 ALTER TABLE `cars`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `cars`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flyway_schema_history`
--

DROP TABLE IF EXISTS `flyway_schema_history`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `flyway_schema_history`
(
    `installed_rank` int                                      NOT NULL,
    `version`        varchar(50) COLLATE utf8mb4_unicode_ci            DEFAULT NULL,
    `description`    varchar(200) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `type`           varchar(20) COLLATE utf8mb4_unicode_ci   NOT NULL,
    `script`         varchar(1000) COLLATE utf8mb4_unicode_ci NOT NULL,
    `checksum`       int                                               DEFAULT NULL,
    `installed_by`   varchar(100) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `installed_on`   timestamp                                NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `execution_time` int                                      NOT NULL,
    `success`        tinyint(1)                               NOT NULL,
    PRIMARY KEY (`installed_rank`),
    KEY `flyway_schema_history_s_idx` (`success`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flyway_schema_history`
--

LOCK TABLES `flyway_schema_history` WRITE;
/*!40000 ALTER TABLE `flyway_schema_history`
    DISABLE KEYS */;
INSERT INTO `flyway_schema_history`
VALUES (1, '1', 'initial schema', 'SQL', 'V1__initial_schema.sql', -282290285, 'root', '2025-09-28 17:43:54', 31924, 1);
/*!40000 ALTER TABLE `flyway_schema_history`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoices`
--

DROP TABLE IF EXISTS `invoices`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoices`
(
    `invoice_id`     int                                    NOT NULL AUTO_INCREMENT,
    `payment_id`     int                                     DEFAULT NULL,
    `invoice_number` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
    `file_url`       varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `issued_at`      datetime                                DEFAULT NULL,
    `created_at`     datetime                                DEFAULT NULL,
    PRIMARY KEY (`invoice_id`),
    UNIQUE KEY `uc_invoices_invoice_number` (`invoice_number`),
    KEY `FK_INVOICES_ON_PAYMENT` (`payment_id`),
    CONSTRAINT `FK_INVOICES_ON_PAYMENT` FOREIGN KEY (`payment_id`) REFERENCES `payments` (`payment_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoices`
--

LOCK TABLES `invoices` WRITE;
/*!40000 ALTER TABLE `invoices`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `invoices`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification_types`
--

DROP TABLE IF EXISTS `notification_types`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notification_types`
(
    `type_id`     int                                    NOT NULL AUTO_INCREMENT,
    `type_name`   varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
    `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`type_id`),
    UNIQUE KEY `uc_notification_types_type_name` (`type_name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification_types`
--

LOCK TABLES `notification_types` WRITE;
/*!40000 ALTER TABLE `notification_types`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `notification_types`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notifications`
(
    `notification_id` int                                     NOT NULL AUTO_INCREMENT,
    `user_id`         int      DEFAULT NULL,
    `booking_id`      int      DEFAULT NULL,
    `trip_id`         int      DEFAULT NULL,
    `type_id`         int      DEFAULT NULL,
    `channel`         varchar(20) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `title`           varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    `message`         text COLLATE utf8mb4_unicode_ci         NOT NULL,
    `is_sent`         bit(1)   DEFAULT NULL,
    `sent_at`         datetime DEFAULT NULL,
    `created_at`      datetime DEFAULT NULL,
    PRIMARY KEY (`notification_id`),
    KEY `FK_NOTIFICATIONS_ON_BOOKING` (`booking_id`),
    KEY `FK_NOTIFICATIONS_ON_TRIP` (`trip_id`),
    KEY `FK_NOTIFICATIONS_ON_TYPE` (`type_id`),
    KEY `FK_NOTIFICATIONS_ON_USER` (`user_id`),
    CONSTRAINT `FK_NOTIFICATIONS_ON_BOOKING` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`booking_id`),
    CONSTRAINT `FK_NOTIFICATIONS_ON_TRIP` FOREIGN KEY (`trip_id`) REFERENCES `trips` (`trip_id`),
    CONSTRAINT `FK_NOTIFICATIONS_ON_TYPE` FOREIGN KEY (`type_id`) REFERENCES `notification_types` (`type_id`),
    CONSTRAINT `FK_NOTIFICATIONS_ON_USER` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notifications`
--

LOCK TABLES `notifications` WRITE;
/*!40000 ALTER TABLE `notifications`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `notifications`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `password_reset_tokens`
--

DROP TABLE IF EXISTS `password_reset_tokens`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `password_reset_tokens`
(
    `token_id`   int                                     NOT NULL AUTO_INCREMENT,
    `user_id`    int      DEFAULT NULL,
    `token`      varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `expires_at` datetime                                NOT NULL,
    `created_at` datetime DEFAULT NULL,
    PRIMARY KEY (`token_id`),
    UNIQUE KEY `uc_password_reset_tokens_token` (`token`),
    KEY `FK_PASSWORD_RESET_TOKENS_ON_USER` (`user_id`),
    CONSTRAINT `FK_PASSWORD_RESET_TOKENS_ON_USER` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `password_reset_tokens`
--

LOCK TABLES `password_reset_tokens` WRITE;
/*!40000 ALTER TABLE `password_reset_tokens`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `password_reset_tokens`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment_methods`
--

DROP TABLE IF EXISTS `payment_methods`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment_methods`
(
    `method_id`   int                                    NOT NULL AUTO_INCREMENT,
    `method_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
    `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `is_active`   bit(1)                                  DEFAULT NULL,
    PRIMARY KEY (`method_id`),
    UNIQUE KEY `uc_payment_methods_method_name` (`method_name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment_methods`
--

LOCK TABLES `payment_methods` WRITE;
/*!40000 ALTER TABLE `payment_methods`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `payment_methods`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payments`
--

DROP TABLE IF EXISTS `payments`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payments`
(
    `payment_id`       int                                    NOT NULL AUTO_INCREMENT,
    `booking_id`       int                                     DEFAULT NULL,
    `method_id`        int                                     DEFAULT NULL,
    `amount`           decimal(10, 2)                         NOT NULL,
    `status`           varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
    `transaction_code` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `paid_at`          datetime                                DEFAULT NULL,
    `created_at`       datetime                                DEFAULT NULL,
    `updated_at`       datetime                                DEFAULT NULL,
    PRIMARY KEY (`payment_id`),
    KEY `FK_PAYMENTS_ON_BOOKING` (`booking_id`),
    KEY `FK_PAYMENTS_ON_METHOD` (`method_id`),
    CONSTRAINT `FK_PAYMENTS_ON_BOOKING` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`booking_id`),
    CONSTRAINT `FK_PAYMENTS_ON_METHOD` FOREIGN KEY (`method_id`) REFERENCES `payment_methods` (`method_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payments`
--

LOCK TABLES `payments` WRITE;
/*!40000 ALTER TABLE `payments`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `payments`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role`
(
    `id`         int                                     NOT NULL AUTO_INCREMENT,
    `code`       varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    `name`       varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `created_at` datetime                                DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uc_role_code` (`code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `role`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_permission`
--

DROP TABLE IF EXISTS `role_permission`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role_permission`
(
    `role_id`        int NOT NULL,
    `can_read`       bit(1) DEFAULT NULL,
    `can_create`     bit(1) DEFAULT NULL,
    `can_update`     bit(1) DEFAULT NULL,
    `can_delete`     bit(1) DEFAULT NULL,
    `can_activate`   bit(1) DEFAULT NULL,
    `can_deactivate` bit(1) DEFAULT NULL,
    `can_import`     bit(1) DEFAULT NULL,
    `can_export`     bit(1) DEFAULT NULL,
    PRIMARY KEY (`role_id`),
    CONSTRAINT `FK_ROLE_PERMISSION_ON_ROLE` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_permission`
--

LOCK TABLES `role_permission` WRITE;
/*!40000 ALTER TABLE `role_permission`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `role_permission`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_user`
--

DROP TABLE IF EXISTS `role_user`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role_user`
(
    `id`        int NOT NULL AUTO_INCREMENT,
    `user_id`   int    DEFAULT NULL,
    `role_id`   int    DEFAULT NULL,
    `is_active` bit(1) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_ROLE_USER_ON_ROLE` (`role_id`),
    KEY `FK_ROLE_USER_ON_USER` (`user_id`),
    CONSTRAINT `FK_ROLE_USER_ON_ROLE` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
    CONSTRAINT `FK_ROLE_USER_ON_USER` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_user`
--

LOCK TABLES `role_user` WRITE;
/*!40000 ALTER TABLE `role_user`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `role_user`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `routes`
--

DROP TABLE IF EXISTS `routes`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `routes`
(
    `route_id`           int                                     NOT NULL AUTO_INCREMENT,
    `start_location`     varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    `end_location`       varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    `distance_km`        decimal(6, 2) DEFAULT NULL,
    `estimated_duration` int           DEFAULT NULL,
    `created_at`         datetime      DEFAULT NULL,
    `updated_at`         datetime      DEFAULT NULL,
    PRIMARY KEY (`route_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `routes`
--

LOCK TABLES `routes` WRITE;
/*!40000 ALTER TABLE `routes`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `routes`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `trips`
--

DROP TABLE IF EXISTS `trips`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trips`
(
    `trip_id`         int            NOT NULL AUTO_INCREMENT,
    `route_id`        int      DEFAULT NULL,
    `bus_id`          int      DEFAULT NULL,
    `departure_time`  datetime       NOT NULL,
    `price`           decimal(10, 2) NOT NULL,
    `available_seats` int            NOT NULL,
    `created_at`      datetime DEFAULT NULL,
    `updated_at`      datetime DEFAULT NULL,
    PRIMARY KEY (`trip_id`),
    KEY `FK_TRIPS_ON_BUS` (`bus_id`),
    KEY `FK_TRIPS_ON_ROUTE` (`route_id`),
    CONSTRAINT `FK_TRIPS_ON_BUS` FOREIGN KEY (`bus_id`) REFERENCES `cars` (`car_id`),
    CONSTRAINT `FK_TRIPS_ON_ROUTE` FOREIGN KEY (`route_id`) REFERENCES `routes` (`route_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trips`
--

LOCK TABLES `trips` WRITE;
/*!40000 ALTER TABLE `trips`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `trips`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_sessions`
--

DROP TABLE IF EXISTS `user_sessions`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_sessions`
(
    `session_id`   int                                     NOT NULL AUTO_INCREMENT,
    `user_id`      int      DEFAULT NULL,
    `access_token` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `expires_at`   datetime                                NOT NULL,
    `revoked`      bit(1)   DEFAULT NULL,
    `created_at`   datetime DEFAULT NULL,
    PRIMARY KEY (`session_id`),
    UNIQUE KEY `uc_user_sessions_access_token` (`access_token`),
    KEY `FK_USER_SESSIONS_ON_USER` (`user_id`),
    CONSTRAINT `FK_USER_SESSIONS_ON_USER` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_sessions`
--

LOCK TABLES `user_sessions` WRITE;
/*!40000 ALTER TABLE `user_sessions`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `user_sessions`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users`
(
    `user_id`       int                                     NOT NULL AUTO_INCREMENT,
    `user_uuid`     binary(16) DEFAULT NULL,
    `full_name`     varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    `email`         varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    `phone_number`  varchar(15) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `password_hash` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `role_id`       int        DEFAULT NULL,
    `is_active`     bit(1)     DEFAULT NULL,
    `created_at`    datetime   DEFAULT NULL,
    `updated_at`    datetime   DEFAULT NULL,
    PRIMARY KEY (`user_id`),
    UNIQUE KEY `uc_users_email` (`email`),
    UNIQUE KEY `uc_users_phone_number` (`phone_number`),
    UNIQUE KEY `uc_users_user_uuid` (`user_uuid`),
    KEY `FK_USERS_ON_ROLE` (`role_id`),
    CONSTRAINT `FK_USERS_ON_ROLE` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `users`
    ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE = @OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;

-- Dump completed on 2025-09-29  0:58:17
