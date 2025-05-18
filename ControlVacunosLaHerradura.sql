CREATE DATABASE sistema_ganadero;
USE sistema_ganadero;
CREATE TABLE Animal (
    Caravana VARCHAR(20) PRIMARY KEY,
    Nombre VARCHAR(50),
    FechaNacimiento DATE,
    Sexo ENUM('Macho', 'Hembra'),
    Raza VARCHAR(30),
    PesoActual DECIMAL(10,2),
    EstadoReproductivo ENUM('Vaca', 'Preñada', 'Lista para Inseminación', 'Ternero')
);


CREATE TABLE Vacuna (
    IdVacuna INT AUTO_INCREMENT PRIMARY KEY,
    NombreVacuna VARCHAR(100),
    Descripcion TEXT
);

CREATE TABLE AplicacionVacuna (
    IdAplicacion INT AUTO_INCREMENT PRIMARY KEY,
    Caravana VARCHAR(20),
    IdVacuna INT,
    FechaAplicacion DATE,
    ProximaDosis DATE,
    FOREIGN KEY (Caravana) REFERENCES Animal(Caravana),
    FOREIGN KEY (IdVacuna) REFERENCES Vacuna(IdVacuna)
);


CREATE TABLE PesoRegistro (
    IdRegistro INT AUTO_INCREMENT PRIMARY KEY,
    Caravana VARCHAR(20),
    Fecha DATE,
    Peso DECIMAL(10,2),
    FOREIGN KEY (Caravana) REFERENCES Animal(Caravana)
);

CREATE TABLE Prenez (
    IdPrenez INT AUTO_INCREMENT PRIMARY KEY,
    Caravana VARCHAR(20),
    FechaConfirmacion DATE,
    FechaPartoEstimada DATE,
    Confirmada BOOLEAN,
    FOREIGN KEY (Caravana) REFERENCES Animal(Caravana)
);
 