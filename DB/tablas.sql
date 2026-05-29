-- ======================================
-- CREACIÓN DE BASE DE DATOS
-- ======================================
CREATE DATABASE IF NOT EXISTS packet_world;

USE packet_world;

-- ======================================
-- CATALOGOS
-- ======================================

CREATE TABLE rol (
  idrol INT NOT NULL AUTO_INCREMENT,
  nombre_rol VARCHAR(50) NOT NULL,
  PRIMARY KEY (idrol),
  UNIQUE KEY uq_nombre_rol (nombre_rol)
);

CREATE TABLE sucursal (
  codigo_sucursal VARCHAR(20) NOT NULL,
  nombre_corto VARCHAR(100) NOT NULL,
  estatus ENUM('activa', 'inactiva') NOT NULL DEFAULT 'activa',
  calle VARCHAR(255) NOT NULL,
  numero VARCHAR(50) NOT NULL,
  colonia VARCHAR(100) NOT NULL,
  codigo_postal VARCHAR(5) NOT NULL,
  ciudad VARCHAR(100) NOT NULL,
  estado VARCHAR(100) NOT NULL,
  PRIMARY KEY (codigo_sucursal)
);

CREATE TABLE tipo_unidad (
  id_tipo_unidad INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(50) NOT NULL,
  PRIMARY KEY (id_tipo_unidad)
);

CREATE TABLE estatus_unidad (
  id_estatus_unidad INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(50) NOT NULL,
  PRIMARY KEY (id_estatus_unidad)
);

CREATE TABLE estatus_envio (
  id_estatus_envio INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(50) NOT NULL,
  PRIMARY KEY (id_estatus_envio)
);

-- ======================================
-- TABLAS PRINCIPALES
-- ======================================

CREATE TABLE unidad (
  id_unidad INT NOT NULL AUTO_INCREMENT,
  vin VARCHAR(17) NOT NULL,
  nii VARCHAR(20) NOT NULL,
  marca VARCHAR(50) NOT NULL,
  modelo VARCHAR(50) NOT NULL,
  anio INT NOT NULL,
  motivo_baja TEXT NULL,
  id_tipo_unidad INT NOT NULL,
  id_estatus_unidad INT NOT NULL,

  PRIMARY KEY (id_unidad),
  UNIQUE KEY uq_vin (vin),
  UNIQUE KEY uq_nii (nii),

  FOREIGN KEY (id_tipo_unidad) REFERENCES tipo_unidad(id_tipo_unidad),
  FOREIGN KEY (id_estatus_unidad) REFERENCES estatus_unidad(id_estatus_unidad)
);

CREATE TABLE cliente (
  id_cliente INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL,
  apellido_paterno VARCHAR(100) NOT NULL,
  apellido_materno VARCHAR(100) NULL,
  calle VARCHAR(255) NOT NULL,
  numero VARCHAR(50) NOT NULL,
  colonia VARCHAR(100) NOT NULL,
  codigo_postal VARCHAR(5) NOT NULL,
  telefono VARCHAR(15) NOT NULL,
  correo_electronico VARCHAR(100) NOT NULL,

  PRIMARY KEY (id_cliente),
  UNIQUE KEY uq_telefono_cliente (telefono),
  UNIQUE KEY uq_correo_cliente (correo_electronico)
);

CREATE TABLE colaborador (
  id_colaborador INT NOT NULL AUTO_INCREMENT,
  numero_personal VARCHAR(50) NOT NULL,
  nombre VARCHAR(100) NOT NULL,
  apellido_paterno VARCHAR(100) NOT NULL,
  apellido_materno VARCHAR(100) NULL,
  curp VARCHAR(18) NOT NULL,
  correo_electronico VARCHAR(100) NOT NULL,
  contrasena VARCHAR(255) NOT NULL,
  fotografia LONGBLOB NULL,
  numero_licencia VARCHAR(50) NULL,

  idrol INT NOT NULL,
  codigo_sucursal VARCHAR(20) NOT NULL,
  id_unidad_asignada INT NULL,

  PRIMARY KEY (id_colaborador),

  UNIQUE KEY uq_numero_personal (numero_personal),
  UNIQUE KEY uq_curp (curp),
  UNIQUE KEY uq_correo_colaborador (correo_electronico),
  UNIQUE KEY uq_id_unidad_asignada (id_unidad_asignada),
  UNIQUE KEY uq_numero_licencia (numero_licencia),

  FOREIGN KEY (idrol) REFERENCES rol(idrol),
  FOREIGN KEY (codigo_sucursal) REFERENCES sucursal(codigo_sucursal),
  FOREIGN KEY (id_unidad_asignada) REFERENCES unidad(id_unidad)
    ON DELETE SET NULL
    ON UPDATE CASCADE
);

CREATE TABLE envio (
  id_envio INT NOT NULL AUTO_INCREMENT,
  numero_guia VARCHAR(50) NOT NULL,
  destinatario_nombre VARCHAR(100) NOT NULL,
  destinatario_ap_paterno VARCHAR(100) NOT NULL,
  destinatario_ap_materno VARCHAR(100),
  destino_calle VARCHAR(255) NOT NULL,
  destino_numero VARCHAR(50) NOT NULL,
  destino_colonia VARCHAR(100) NOT NULL,
  destino_codigo_postal VARCHAR(5) NOT NULL,
  destino_ciudad VARCHAR(100) NOT NULL,
  destino_estado VARCHAR(100) NOT NULL,
  costo_total DECIMAL(10,2) NOT NULL DEFAULT 0.00,

  id_cliente_remitente INT NOT NULL,
  codigo_sucursal_origen VARCHAR(20) NOT NULL,
  id_conductor_asignado INT NULL,
  id_estatus_envio INT NOT NULL,

  PRIMARY KEY (id_envio),
  UNIQUE KEY uq_numero_guia (numero_guia),

  FOREIGN KEY (id_cliente_remitente) REFERENCES cliente(id_cliente),
  FOREIGN KEY (codigo_sucursal_origen) REFERENCES sucursal(codigo_sucursal),
  FOREIGN KEY (id_conductor_asignado) REFERENCES colaborador(id_colaborador)
    ON DELETE SET NULL,
  FOREIGN KEY (id_estatus_envio) REFERENCES estatus_envio(id_estatus_envio)
);

CREATE TABLE paquete (
  id_paquete INT NOT NULL AUTO_INCREMENT,
  id_envio INT NOT NULL,
  descripcion TEXT,
  peso_kg DECIMAL(10, 2) NOT NULL,
  alto_cm DECIMAL(10, 2) NOT NULL,
  ancho_cm DECIMAL(10, 2) NOT NULL,
  profundidad_cm DECIMAL(10, 2) NOT NULL,

  PRIMARY KEY (id_paquete),
  FOREIGN KEY (id_envio) REFERENCES envio(id_envio)
    ON DELETE CASCADE
);

CREATE TABLE historial_estatus (
  id_historial INT NOT NULL AUTO_INCREMENT,
  id_envio INT NOT NULL,
  id_colaborador_actualiza INT NOT NULL,
  id_estatus_envio INT NOT NULL,
  fecha_hora_cambio DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  comentario TEXT NULL,

  PRIMARY KEY (id_historial),

  FOREIGN KEY (id_envio) REFERENCES envio(id_envio)
    ON DELETE CASCADE,
  FOREIGN KEY (id_colaborador_actualiza) REFERENCES colaborador(id_colaborador),
  FOREIGN KEY (id_estatus_envio) REFERENCES estatus_envio(id_estatus_envio)
);