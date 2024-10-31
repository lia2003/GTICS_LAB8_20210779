CREATE DATABASE unlp_students;

USE unlp_students;

CREATE TABLE students (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    gpa DECIMAL(3, 2) NOT NULL CHECK (gpa >= 0.0 AND gpa <= 4.0),
    facultad VARCHAR(100) NOT NULL,
    creditos_completados INT NOT NULL CHECK (creditos_completados >= 0),
    posicion_clasificacion INT NOT NULL
);

INSERT INTO students (nombre, gpa, facultad, creditos_completados, posicion_clasificacion) VALUES
('Juan Pérez', 3.9, 'Ingeniería', 120, 1),
('Ana Gómez', 3.7, 'Ingeniería', 110, 2),
('Luis Rodríguez', 3.8, 'Medicina', 130, 1),
('María López', 3.6, 'Ciencias Sociales', 100, 1),
('Carlos Sánchez', 3.5, 'Artes', 90, 1);
