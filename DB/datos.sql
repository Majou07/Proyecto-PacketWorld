use packet_world;
-- =========================
-- Tabla: Colaborador
-- =========================
INSERT INTO colaborador (nombre, apellido_paterno, apellido_materno, curp, correo_electronico, numero_personal, contrasena, id_rol, codigo_sucursal, numero_licencia)
VALUES
('Juan', 'Pérez', 'López', 'PEPJ800101HDFRRN09', 'juan.perez@correo.com', 'EMP001', 'Password123', 1, 'SUC001', 'LIC123456'),
('María', 'García', 'Hernández', 'GAHM920202MDFRRN08', 'maria.garcia@correo.com', 'EMP002', 'ClaveSegura89', 2, 'SUC002', NULL),
('Carlos', 'Ramírez', 'Torres', 'RATC850505HDFRRN07', 'carlos.ramirez@correo.com', 'EMP003', 'MiPass2026', 1, 'SUC001', 'ABC-987654');

-- =========================
-- Tabla: Cliente
-- =========================
INSERT INTO cliente (nombre, apellido_paterno, apellido_materno, calle, numero, colonia, telefono, codigo_postal, correo_electronico)
VALUES
('Luis', 'Martínez', 'Sánchez', 'Av. Reforma', '123', 'Centro', '2281234567', '91000', 'luis.martinez@correo.com'),
('Ana', 'Fernández', 'Ruiz', 'Calle Hidalgo', '45', 'Industrial', '2287654321', '91100', 'ana.fernandez@correo.com'),
('Pedro', 'López', 'Morales', 'Juárez', '67', 'Benito Juárez', '2285551111', '91200', 'pedro.lopez@correo.com');

-- =========================
-- Tabla: Sucursal
-- =========================
INSERT INTO sucursal (codigo_sucursal, nombre, numero, codigo_postal)
VALUES
('SUC001', 'Sucursal Centro', '101', '91000'),
('SUC002', 'Sucursal Norte', '202', '91100'),
('SUC003', 'Sucursal Sur', '303', '91200');

-- =========================
-- Tabla: Envio
-- =========================
INSERT INTO envio (id_cliente_remitente, codigo_sucursal_origen, destinatario_nombre, destinatario_ap_paterno, destinatario_ap_materno, destino_calle, destino_numero, destino_colonia, destino_codigo_postal, destino_ciudad, destino_estado, costo_total, id_estatus_envio)
VALUES
(1, 'SUC001', 'Roberto', 'Hernández', 'Gómez', 'Av. México', '55', 'Centro', '91000', 'Xalapa', 'Veracruz', 0.0, 1),
(2, 'SUC002', 'Laura', 'Ramírez', NULL, 'Calle Morelos', '12', 'Industrial', '91100', 'Xalapa', 'Veracruz', 0.0, 1),
(3, 'SUC003', 'Miguel', 'Torres', 'Pérez', 'Juárez', '89', 'Benito Juárez', '91200', 'Xalapa', 'Veracruz', 0.0, 1);

-- =========================
-- Tabla: Paquete (2 por cada envío)
-- =========================
INSERT INTO paquete (id_envio, descripcion, peso, alto, ancho, profundidad)
VALUES
(1, 'Caja pequeña', 2.5, 30.0, 20.0, 15.0),
(1, 'Sobre documentos', 0.5, NULL, NULL, NULL),
(2, 'Caja mediana', 5.0, 40.0, 30.0, 25.0),
(2, 'Paquete ropa', 3.2, 50.0, 40.0, 20.0),
(3, 'Electrónicos', 7.5, 60.0, 40.0, 30.0),
(3, 'Accesorios', 1.8, NULL, NULL, NULL);

-- =========================
-- Tabla: Unidad
-- =========================
INSERT INTO unidad (marca, modelo, anio, vin, nii, id_tipo_unidad, id_estatus_unidad)
VALUES
('Nissan', 'Versa', 2020, '1HGCM82633A004352', 'NII001', 1, 1),
('Toyota', 'Hilux', 2022, '2HGCM82633A004353', NULL, 2, 1),
('Ford', 'Transit', 2019, '3HGCM82633A004354', 'NII002', 3, 3);
