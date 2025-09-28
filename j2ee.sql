CREATE TABLE `role` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `code` varchar(100) UNIQUE NOT NULL,
  `name` varchar(100)
);

CREATE TABLE `role_user` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `user_id` int,
  `role_id` int
);

CREATE TABLE `role_permission` (
  `role_id` int,
  `canRead` boolean,
  `canCreate` boolean,
  `canUpdate` boolean,
  `canDelete` boolean,
  `canActivate` boolean,
  `canDeactivate` boolean,
  `canImport` boolean,
  `canExport` boolean
);

CREATE TABLE `users` (
  `user_id` int PRIMARY KEY AUTO_INCREMENT,
  `full_name` varchar(100) NOT NULL,
  `email` varchar(100) UNIQUE NOT NULL,
  `phone_number` varchar(15) UNIQUE NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `role_id` int,
  `is_active` boolean DEFAULT true,
  `created_at` datetime DEFAULT (CURRENT_TIMESTAMP),
  `updated_at` datetime
);

CREATE TABLE `password_reset_tokens` (
  `token_id` int PRIMARY KEY AUTO_INCREMENT,
  `user_id` int,
  `token` varchar(255) UNIQUE NOT NULL,
  `expires_at` datetime NOT NULL,
  `created_at` datetime DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE `user_sessions` (
  `session_id` int PRIMARY KEY AUTO_INCREMENT,
  `user_id` int,
  `access_token` varchar(255) UNIQUE NOT NULL,
  `created_at` datetime DEFAULT (CURRENT_TIMESTAMP),
  `expires_at` datetime NOT NULL,
  `revoked` boolean DEFAULT false
);

CREATE TABLE `routes` (
  `route_id` int PRIMARY KEY AUTO_INCREMENT,
  `start_location` varchar(100) NOT NULL,
  `end_location` varchar(100) NOT NULL,
  `distance_km` decimal(6,2),
  `estimated_duration` int,
  `created_at` datetime DEFAULT (CURRENT_TIMESTAMP),
  `updated_at` datetime
);

CREATE TABLE `car_types` (
  `car_type_id` int PRIMARY KEY AUTO_INCREMENT,
  `type_name` varchar(50) UNIQUE NOT NULL,
  `description` varchar(255)
);

CREATE TABLE `cars` (
  `car_id` int PRIMARY KEY AUTO_INCREMENT,
  `owner_id` int,
  `car_type_id` int,
  `license_plate` varchar(20) UNIQUE NOT NULL,
  `seat_count` int NOT NULL,
  `created_at` datetime DEFAULT (CURRENT_TIMESTAMP),
  `updated_at` datetime
);

CREATE TABLE `trips` (
  `trip_id` int PRIMARY KEY AUTO_INCREMENT,
  `route_id` int,
  `bus_id` int,
  `departure_time` datetime NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `available_seats` int NOT NULL,
  `created_at` datetime DEFAULT (CURRENT_TIMESTAMP),
  `updated_at` datetime
);

CREATE TABLE `car_seats` (
  `seat_id` int PRIMARY KEY AUTO_INCREMENT,
  `car_id` int,
  `seat_number` varchar(10) NOT NULL,
  `seat_position` varchar(50),
  `is_active` boolean DEFAULT true
);

CREATE TABLE `bookings` (
  `booking_id` int PRIMARY KEY AUTO_INCREMENT,
  `user_id` int,
  `trip_id` int,
  `booking_status` varchar(20) NOT NULL DEFAULT 'pending',
  `total_price` decimal(10,2) NOT NULL,
  `created_at` datetime DEFAULT (CURRENT_TIMESTAMP),
  `updated_at` datetime
);

CREATE TABLE `booking_seats` (
  `booking_seat_id` int PRIMARY KEY AUTO_INCREMENT,
  `booking_id` int,
  `seat_id` int,
  `is_reserved` boolean DEFAULT true,
  `price` decimal(10,2) NOT NULL
);

CREATE TABLE `payment_methods` (
  `method_id` int PRIMARY KEY AUTO_INCREMENT,
  `method_name` varchar(50) UNIQUE NOT NULL,
  `description` varchar(255),
  `is_active` boolean DEFAULT true
);

CREATE TABLE `payments` (
  `payment_id` int PRIMARY KEY AUTO_INCREMENT,
  `booking_id` int,
  `method_id` int,
  `amount` decimal(10,2) NOT NULL,
  `status` varchar(20) NOT NULL DEFAULT 'pending',
  `transaction_code` varchar(100),
  `paid_at` datetime,
  `created_at` datetime DEFAULT (CURRENT_TIMESTAMP),
  `updated_at` datetime
);

CREATE TABLE `invoices` (
  `invoice_id` int PRIMARY KEY AUTO_INCREMENT,
  `payment_id` int,
  `invoice_number` varchar(50) UNIQUE NOT NULL,
  `file_url` varchar(255),
  `issued_at` datetime DEFAULT (CURRENT_TIMESTAMP),
  `created_at` datetime DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE `notification_types` (
  `type_id` int PRIMARY KEY AUTO_INCREMENT,
  `type_name` varchar(50) UNIQUE NOT NULL,
  `description` varchar(255)
);

CREATE TABLE `notifications` (
  `notification_id` int PRIMARY KEY AUTO_INCREMENT,
  `user_id` int,
  `booking_id` int,
  `trip_id` int,
  `type_id` int,
  `channel` varchar(20) NOT NULL DEFAULT 'email',
  `title` varchar(100) NOT NULL,
  `message` text NOT NULL,
  `is_sent` boolean DEFAULT false,
  `sent_at` datetime,
  `created_at` datetime DEFAULT (CURRENT_TIMESTAMP)
);

ALTER TABLE `role_user` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

ALTER TABLE `role_user` ADD FOREIGN KEY (`role_id`) REFERENCES `role` (`id`);

ALTER TABLE `role_permission` ADD FOREIGN KEY (`role_id`) REFERENCES `role` (`id`);

ALTER TABLE `users` ADD FOREIGN KEY (`role_id`) REFERENCES `role` (`id`);

ALTER TABLE `password_reset_tokens` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

ALTER TABLE `user_sessions` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

ALTER TABLE `cars` ADD FOREIGN KEY (`owner_id`) REFERENCES `users` (`user_id`);

ALTER TABLE `cars` ADD FOREIGN KEY (`car_type_id`) REFERENCES `car_types` (`car_type_id`);

ALTER TABLE `trips` ADD FOREIGN KEY (`route_id`) REFERENCES `routes` (`route_id`);

ALTER TABLE `trips` ADD FOREIGN KEY (`bus_id`) REFERENCES `cars` (`car_id`);

ALTER TABLE `car_seats` ADD FOREIGN KEY (`car_id`) REFERENCES `cars` (`car_id`);

ALTER TABLE `bookings` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

ALTER TABLE `bookings` ADD FOREIGN KEY (`trip_id`) REFERENCES `trips` (`trip_id`);

ALTER TABLE `booking_seats` ADD FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`booking_id`);

ALTER TABLE `booking_seats` ADD FOREIGN KEY (`seat_id`) REFERENCES `car_seats` (`seat_id`);

ALTER TABLE `payments` ADD FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`booking_id`);

ALTER TABLE `payments` ADD FOREIGN KEY (`method_id`) REFERENCES `payment_methods` (`method_id`);

ALTER TABLE `invoices` ADD FOREIGN KEY (`payment_id`) REFERENCES `payments` (`payment_id`);

ALTER TABLE `notifications` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

ALTER TABLE `notifications` ADD FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`booking_id`);

ALTER TABLE `notifications` ADD FOREIGN KEY (`trip_id`) REFERENCES `trips` (`trip_id`);

ALTER TABLE `notifications` ADD FOREIGN KEY (`type_id`) REFERENCES `notification_types` (`type_id`);
