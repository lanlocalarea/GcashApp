-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 02, 2025 at 12:47 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `gcashdb`
--

-- --------------------------------------------------------

--
-- Table structure for table `balance`
--

CREATE TABLE `balance` (
  `ID` int(10) NOT NULL,
  `amount` float NOT NULL,
  `user_ID` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `balance`
--

INSERT INTO `balance` (`ID`, `amount`, `user_ID`) VALUES
(1, 2200, 1),
(3, 3000, 2),
(4, 0, 3);

-- --------------------------------------------------------

--
-- Table structure for table `transaction`
--

CREATE TABLE `transaction` (
  `ID` int(10) NOT NULL,
  `amount` float NOT NULL,
  `name` varchar(128) NOT NULL,
  `account_ID` int(10) NOT NULL,
  `date` varchar(256) NOT NULL,
  `transferToID` int(10) NOT NULL,
  `transferFromID` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `transaction`
--

INSERT INTO `transaction` (`ID`, `amount`, `name`, `account_ID`, `date`, `transferToID`, `transferFromID`) VALUES
(3, 100, 'Carlos', 1, '2025-06-29T16:06:49.705', 1, 1),
(4, 100, 'Carlos', 1, '2025-06-29T16:07:32.916', 1, 1),
(5, 200, 'Carlos', 1, '2025-06-29T16:15:45.254', 1, 1),
(6, 300, 'Carlos', 1, '2025-06-29T16:15:45.289', 1, 1),
(7, 200, 'Carlos', 1, '2025-07-01T17:15:56.477', 1, 1),
(8, 300, 'Carlos', 1, '2025-07-01T17:15:56.521', 1, 1),
(9, 200, 'Carlos', 1, '2025-07-01T17:18:26.301', 1, 1),
(10, 300, 'Carlos', 1, '2025-07-01T17:18:26.344', 1, 1),
(11, 200, 'Carlos', 1, '2025-07-01T17:18:49.562', 1, 1),
(12, 300, 'Carlos', 1, '2025-07-01T17:18:49.606', 1, 1),
(13, 200, 'Charls', 1, '2025-07-01T17:21:43.915', 1, 2),
(14, 300, 'Charls', 1, '2025-07-01T17:21:43.964', 1, 2),
(15, 200, 'Carlos', 1, '2025-07-01T17:27:39.071', 2, 1),
(16, 300, 'Carlos', 1, '2025-07-01T17:27:39.111', 2, 1),
(17, 200, 'Carlos', 1, '2025-07-01T18:02:31.333', 1, 2),
(18, 300, 'Carlos', 1, '2025-07-01T18:02:31.379', 1, 2),
(19, 200, 'Carlos', 1, '2025-07-01T19:52:37.570', 1, 2),
(20, 500, 'Carlos', 1, '2025-07-02T00:56:30.649', 1, 2),
(21, 700, 'Carlos', 1, '2025-07-02T00:57:00.855', 2, 1),
(22, 500, 'Carlos', 1, '2025-07-02T01:49:15.415', 1, 1),
(23, 500, 'Carlos', 1, '2025-07-02T01:49:41.717', 1, 1),
(24, 500, 'Carlos', 1, '2025-07-02T18:17:11.953', 2, 1),
(25, 200, 'Carlos', 1, '2025-07-02T18:17:12.073', 1, 2),
(26, 500, 'Carlos', 1, '2025-07-02T18:19:21.140', 2, 1),
(27, 200, 'Carlos', 1, '2025-07-02T18:19:21.258', 1, 2),
(28, 500, 'Carlos', 1, '2025-07-02T18:20:58.013', 2, 1),
(29, 500, 'Carlos', 1, '2025-07-02T18:22:33.111', 2, 1),
(30, 500, 'Carlos', 1, '2025-07-02T18:26:00.814', 2, 1),
(31, 500, 'Carlos', 1, '2025-07-02T18:27:25.578', 2, 1),
(32, 500, 'Carlos', 1, '2025-07-02T18:28:05.099', 2, 1),
(33, 500, 'Carlos', 1, '2025-07-02T18:28:25.832', 2, 1),
(34, 200, 'Carlos', 1, '2025-07-02T18:28:25.954', 1, 2),
(35, 500, 'Carlos', 1, '2025-07-02T18:31:56.686', 2, 1),
(36, 200, 'Carlos', 1, '2025-07-02T18:31:56.771', 1, 2);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `ID` int(10) NOT NULL,
  `Name` varchar(128) NOT NULL,
  `Email` varchar(128) NOT NULL,
  `Number` bigint(10) NOT NULL,
  `PIN` smallint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`ID`, `Name`, `Email`, `Number`, `PIN`) VALUES
(1, 'Carlos', 'gocarlos519@gmail.com', 9265305606, 1234),
(2, 'Charls', 'charlsgo123@gmail.com', 9265305607, 1234),
(3, 'Helen', 'helengo123@gmail.com', 9265305608, 1234);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `balance`
--
ALTER TABLE `balance`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `transaction`
--
ALTER TABLE `transaction`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`ID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `balance`
--
ALTER TABLE `balance`
  MODIFY `ID` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `transaction`
--
ALTER TABLE `transaction`
  MODIFY `ID` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=37;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `ID` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
