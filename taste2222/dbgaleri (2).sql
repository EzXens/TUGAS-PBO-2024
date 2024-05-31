-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 29 Bulan Mei 2024 pada 01.54
-- Versi server: 10.4.28-MariaDB
-- Versi PHP: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `dbgaleri`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `akun`
--

CREATE TABLE `akun` (
  `uid` int(11) NOT NULL,
  `username` varchar(11) NOT NULL,
  `password` varchar(11) NOT NULL,
  `email` text NOT NULL,
  `izin` varchar(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `akun`
--

INSERT INTO `akun` (`uid`, `username`, `password`, `email`, `izin`) VALUES
(1, 'admin', 'admin', 'admin@gmail.com', 'admin'),
(2, '123', '123', '123', 'user'),
(3, 'bima', 'bima', 'bima', 'user'),
(4, 'anjay', 'anhjay', 'anjay', 'user'),
(5, 'anjay2', 'bima', 'bima', 'user'),
(6, 'tes', 'tes', 'tes@gmail.com', 'user');

-- --------------------------------------------------------

--
-- Struktur dari tabel `gambar`
--

CREATE TABLE `gambar` (
  `id_gambar` int(15) NOT NULL,
  `gambar` varchar(255) NOT NULL,
  `nama_gambar` varchar(50) NOT NULL,
  `deskripsi_gambar` varchar(255) NOT NULL,
  `tanggal_gambar` date NOT NULL,
  `id_tags` int(12) NOT NULL,
  `likes` int(11) NOT NULL DEFAULT 0,
  `id_kategori` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `gambar`
--

INSERT INTO `gambar` (`id_gambar`, `gambar`, `nama_gambar`, `deskripsi_gambar`, `tanggal_gambar`, `id_tags`, `likes`, `id_kategori`) VALUES
(84, 'C:\\Users\\ashad\\OneDrive\\Pictures\\channels4_profile.jpg', 'Ga Tawuu', 'coool ga', '2024-05-28', 9, 0, 8),
(85, 'C:\\Users\\ashad\\OneDrive\\Pictures\\DSC08768.png', 'orang', 'tes', '2024-05-29', 5, 0, 4),
(88, 'C:\\Users\\ashad\\OneDrive\\Pictures\\illust_101473755_20230821_112014.png', 'nilou', 'beautiful', '2024-05-10', 9, 0, 2),
(89, 'C:\\Users\\ashad\\OneDrive\\Pictures\\universitas-trunojoyo-madura.jpg', 'sasas', 'dsdfdf', '2024-05-01', 5, 0, 1),
(90, 'C:\\Users\\ashad\\OneDrive\\Pictures\\ASHADI PERMANA coba coba pake AI-02.jpg', 'ranger', 'dsdgfas', '2024-05-01', 14, 0, 1),
(91, 'C:\\Users\\ashad\\OneDrive\\Pictures\\mahasiswa.png', 'sdds', 'sd', '2024-05-01', 8, 0, 2);

-- --------------------------------------------------------

--
-- Struktur dari tabel `kategori`
--

CREATE TABLE `kategori` (
  `id_kategori` int(10) NOT NULL,
  `nama_kategori` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `kategori`
--

INSERT INTO `kategori` (`id_kategori`, `nama_kategori`) VALUES
(1, 'artfull\r\n'),
(2, 'anime'),
(3, 'modern'),
(4, 'realistic'),
(8, 'cool');

-- --------------------------------------------------------

--
-- Struktur dari tabel `resep`
--

CREATE TABLE `resep` (
  `id_resep` int(11) NOT NULL,
  `nama_resep` text NOT NULL,
  `kategori_resep` text NOT NULL,
  `alat` text NOT NULL,
  `bahan` text NOT NULL,
  `id_user` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `tags`
--

CREATE TABLE `tags` (
  `id_tags` int(15) NOT NULL,
  `nama_tags` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `tags`
--

INSERT INTO `tags` (`id_tags`, `nama_tags`) VALUES
(5, 'asep'),
(8, 'beauty'),
(9, 'anime'),
(10, 'animal'),
(14, 'naga');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `akun`
--
ALTER TABLE `akun`
  ADD PRIMARY KEY (`uid`);

--
-- Indeks untuk tabel `gambar`
--
ALTER TABLE `gambar`
  ADD PRIMARY KEY (`id_gambar`),
  ADD KEY `fk_id_tags` (`id_tags`),
  ADD KEY `fk_id_kategori` (`id_kategori`);

--
-- Indeks untuk tabel `kategori`
--
ALTER TABLE `kategori`
  ADD PRIMARY KEY (`id_kategori`);

--
-- Indeks untuk tabel `resep`
--
ALTER TABLE `resep`
  ADD PRIMARY KEY (`id_resep`),
  ADD KEY `property` (`id_user`);

--
-- Indeks untuk tabel `tags`
--
ALTER TABLE `tags`
  ADD PRIMARY KEY (`id_tags`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `akun`
--
ALTER TABLE `akun`
  MODIFY `uid` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT untuk tabel `gambar`
--
ALTER TABLE `gambar`
  MODIFY `id_gambar` int(15) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=93;

--
-- AUTO_INCREMENT untuk tabel `kategori`
--
ALTER TABLE `kategori`
  MODIFY `id_kategori` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT untuk tabel `resep`
--
ALTER TABLE `resep`
  MODIFY `id_resep` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `tags`
--
ALTER TABLE `tags`
  MODIFY `id_tags` int(15) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `gambar`
--
ALTER TABLE `gambar`
  ADD CONSTRAINT `fk_id_kategori` FOREIGN KEY (`id_kategori`) REFERENCES `kategori` (`id_kategori`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `gambar_ibfk_2` FOREIGN KEY (`id_tags`) REFERENCES `tags` (`id_tags`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ketidakleluasaan untuk tabel `resep`
--
ALTER TABLE `resep`
  ADD CONSTRAINT `property` FOREIGN KEY (`id_user`) REFERENCES `akun` (`uid`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
