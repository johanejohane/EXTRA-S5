
-- ============================================
-- BASE DE DONNEES CLINIQUE
-- MySQL 8
-- Execution : mysql -u root -p < sql/schema.sql
-- ============================================

CREATE DATABASE IF NOT EXISTS clinique;
USE clinique;

-- ============================================
-- TABLE ROLE
-- ============================================

CREATE TABLE IF NOT EXISTS role (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(30) NOT NULL UNIQUE
);

-- ============================================
-- TABLE UTILISATEUR
-- ============================================

CREATE TABLE IF NOT EXISTS utilisateur (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    mot_de_passe VARCHAR(100) NOT NULL,
    role_id INT NOT NULL,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (role_id) REFERENCES role(id)
);

-- ============================================
-- TABLE PATIENT
-- ============================================

CREATE TABLE IF NOT EXISTS patient (
    id INT AUTO_INCREMENT PRIMARY KEY,
    utilisateur_id INT NOT NULL UNIQUE,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (utilisateur_id)
        REFERENCES utilisateur(id)
        ON DELETE CASCADE
);

-- ============================================
-- INSERTION DES ROLES
-- ============================================

INSERT IGNORE INTO role (nom) VALUES
('medecin'),
('patient');

-- ============================================
-- COMPTES DE TEST
-- ============================================

-- Medecin
INSERT IGNORE INTO utilisateur
(nom, email, mot_de_passe, role_id)
VALUES (
    'Dr Rabe',
    'medecin@clinique.mg',
    'medecin123',
    (SELECT id FROM role WHERE nom = 'medecin')
);

-- Patient
INSERT IGNORE INTO utilisateur
(nom, email, mot_de_passe, role_id)
VALUES (
    'Soa Rasoa',
    'patient@clinique.mg',
    'patient123',
    (SELECT id FROM role WHERE nom = 'patient')
);

-- ============================================
-- CREATION DE LA FICHE PATIENT
-- ============================================

INSERT IGNORE INTO patient (utilisateur_id)
SELECT id
FROM utilisateur
WHERE email = 'patient@clinique.mg';
