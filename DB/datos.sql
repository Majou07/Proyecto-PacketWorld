USE packet_world;

-- =========================
-- Tabla: Rol
-- =========================
INSERT INTO rol (nombre_rol) VALUES
('Administrador'),
('Conductor'),
('Ejecutivo de tienda');

-- =========================
-- Tabla: Tipo Unidad
-- =========================
INSERT INTO tipo_unidad (nombre) VALUES
('Gasolina'),
('Diesel'),
('Eléctrica'),
('Híbrida');

-- =========================
-- Tabla: Estatus Unidad
-- =========================
INSERT INTO estatus_unidad (nombre) VALUES
('Activo'),
('Inactivo'),
('En mantenimiento');

-- =========================
-- Tabla: Estatus Envío
-- =========================
INSERT INTO estatus_envio (nombre) VALUES
('recibido en sucursal'),
('procesado'),
('en transito'),
('detenido'),
('entregado'),
('cancelado');

-- =========================
-- Tabla: Sucursal
-- =========================
INSERT INTO sucursal (codigo_sucursal, nombre_corto, estatus, calle, numero, colonia, codigo_postal, ciudad, estado)
VALUES
('SUC001', 'Sucursal Centro', 'activa', 'Av. Reforma', '101', 'Centro', '91000', 'Xalapa', 'Veracruz'),
('SUC002', 'Sucursal Norte', 'activa', 'Calle Hidalgo', '202', 'Industrial', '91100', 'Xalapa', 'Veracruz'),
('SUC003', 'Sucursal Sur', 'activa', 'Juárez', '303', 'Benito Juárez', '91200', 'Xalapa', 'Veracruz');

-- =========================
-- Tabla: Colaborador
-- =========================
INSERT INTO colaborador (nombre, apellido_paterno, apellido_materno, curp, correo_electronico, numero_personal, contrasena, idrol, codigo_sucursal, numero_licencia)
VALUES
('Juan', 'Pérez', 'López', 'PEPJ800101HDFRRN09', 'juan.perez@correo.com', 'EMP001', 'Password123', 2, 'SUC001', 'LIC123456'),
('María', 'García', 'Hernández', 'GAHM920202MDFRRN08', 'maria.garcia@correo.com', 'EMP002', 'ClaveSegura89', 1, 'SUC002', NULL),
('Carlos', 'Ramírez', 'Torres', 'RATC850505HDFRRN07', 'carlos.ramirez@correo.com', 'EMP003', 'MiPass2026', 2, 'SUC001', 'ABC-987654');

-- =========================
-- Tabla: Cliente
-- =========================
INSERT INTO cliente (nombre, apellido_paterno, apellido_materno, calle, numero, colonia, codigo_postal, telefono, correo_electronico)
VALUES
('Luis', 'Martínez', 'Sánchez', 'Av. Reforma', '123', 'Centro', '91000', '2281234567', 'luis.martinez@correo.com'),
('Ana', 'Fernández', 'Ruiz', 'Calle Hidalgo', '45', 'Industrial', '91100', '2287654321', 'ana.fernandez@correo.com'),
('Pedro', 'López', 'Morales', 'Juárez', '67', 'Benito Juárez', '91200', '2285551111', 'pedro.lopez@correo.com');

-- =========================
-- Tabla: Unidad
-- =========================
INSERT INTO unidad (vin, nii, marca, modelo, anio, id_tipo_unidad, id_estatus_unidad)
VALUES
('1HGCM82633A004352', '2020-1HGC', 'Nissan', 'Versa', 2020, 1, 1),
('2HGCM82633A004353', '2022-2HGC', 'Toyota', 'Hilux', 2022, 2, 1),
('3HGCM82633A004354', '2019-3HGC', 'Ford', 'Transit', 2019, 3, 3);

-- =========================
-- Tabla: Envío
-- =========================
INSERT INTO envio (numero_guia, id_cliente_remitente, codigo_sucursal_origen, destinatario_nombre, destinatario_ap_paterno, destinatario_ap_materno, destino_calle, destino_numero, destino_colonia, destino_codigo_postal, destino_ciudad, destino_estado, costo_total, id_estatus_envio)
VALUES
('GUIA001', 1, 'SUC001', 'Roberto', 'Hernández', 'Gómez', 'Av. México', '55', 'Centro', '91000', 'Xalapa', 'Veracruz', 0.0, 1),
('GUIA002', 2, 'SUC002', 'Laura', 'Ramírez', NULL, 'Calle Morelos', '12', 'Industrial', '91100', 'Xalapa', 'Veracruz', 0.0, 1),
('GUIA003', 3, 'SUC003', 'Miguel', 'Torres', 'Pérez', 'Juárez', '89', 'Benito Juárez', '91200', 'Xalapa', 'Veracruz', 0.0, 1);

-- =========================
-- Tabla: Paquete (2 por cada envío)
-- =========================
INSERT INTO paquete (id_envio, descripcion, peso_kg, alto_cm, ancho_cm, profundidad_cm)
VALUES
(1, 'Caja pequeña', 2.5, 30.0, 20.0, 15.0),
(1, 'Sobre documentos', 0.5, 25.0, 15.0, 2.0),
(2, 'Caja mediana', 5.0, 40.0, 30.0, 25.0),
(2, 'Paquete ropa', 3.2, 50.0, 40.0, 20.0),
(3, 'Electrónicos', 7.5, 60.0, 40.0, 30.0),
(3, 'Accesorios', 1.8, 20.0, 15.0, 10.0);
