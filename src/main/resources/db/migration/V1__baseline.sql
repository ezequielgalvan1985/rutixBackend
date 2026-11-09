-- Baseline del esquema de Rutix tal como lo dejo Hibernate con ddl-auto=update.
-- Generado a partir del mapeo JPA real de las entidades (PerfilRutix, Vehiculo,
-- Tarjeta, CuentaBancaria, Viaje, Reserva, Pago), no de un dump de la base.
-- No se ejecuta contra una base que ya tiene estas tablas (Flyway la marca
-- "baseline" en V1 y arranca a versionar desde V2); solo se ejecuta tal cual
-- en una base nueva (por ejemplo, un entorno de test o la maquina de otro
-- desarrollador). Antes de depender de este script para levantar un entorno
-- nuevo, conviene diffearlo contra el esquema real (`SHOW CREATE TABLE`) y
-- corregir lo que no coincida.

CREATE TABLE perfil_rutix (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT,
    nombre VARCHAR(255),
    valoracion INT,
    direccion VARCHAR(100),
    telefono VARCHAR(20),
    email VARCHAR(50),
    ciudad VARCHAR(50),
    fecha_alta DATETIME
);

CREATE TABLE vehiculos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(255),
    marca VARCHAR(50),
    modelo VARCHAR(100),
    patente VARCHAR(10),
    anio VARCHAR(4),
    tiene_aire BIT NOT NULL,
    activo BIT NOT NULL,
    perfil_id BIGINT NOT NULL,
    asientos INT,
    CONSTRAINT fk_vehiculos_perfil FOREIGN KEY (perfil_id) REFERENCES perfil_rutix (id)
);

CREATE TABLE tarjetas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo VARCHAR(255) NOT NULL,
    marca VARCHAR(255) NOT NULL,
    ultimos_4 VARCHAR(4) NOT NULL,
    titular VARCHAR(100) NOT NULL,
    vencimiento_mes INT NOT NULL,
    vencimiento_anio INT NOT NULL,
    token VARCHAR(255) NOT NULL,
    activa BIT NOT NULL,
    perfil_id BIGINT NOT NULL,
    fecha_alta DATETIME,
    CONSTRAINT uk_tarjetas_token UNIQUE (token),
    CONSTRAINT fk_tarjetas_perfil FOREIGN KEY (perfil_id) REFERENCES perfil_rutix (id)
);

CREATE TABLE cuenta_bancaria (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre_titular VARCHAR(50),
    cbu VARCHAR(22),
    alias VARCHAR(100),
    numero VARCHAR(50),
    nombre_entidad VARCHAR(50),
    codigo_entidad INT,
    fecha_alta DATETIME,
    activo BIT NOT NULL,
    perfil_id BIGINT NOT NULL,
    CONSTRAINT uk_cuenta_bancaria_cbu UNIQUE (cbu),
    CONSTRAINT fk_cuenta_bancaria_perfil FOREIGN KEY (perfil_id) REFERENCES perfil_rutix (id)
);

CREATE TABLE viajes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha_salida DATE,
    hora_salida TIME NOT NULL,
    ciudad_partida VARCHAR(100) NOT NULL,
    ciudad_destino VARCHAR(100) NOT NULL,
    hora_llegada TIME NOT NULL,
    perfil_id BIGINT NOT NULL,
    vehiculo_id BIGINT NOT NULL,
    asientos INT NOT NULL,
    paga_senia BIT,
    porcentaje_senia INT,
    valor DECIMAL(38,2),
    estado VARCHAR(255),
    asientos_disponibles INT,
    asientos_reservados INT,
    fecha_alta DATETIME,
    CONSTRAINT fk_viajes_conductor FOREIGN KEY (perfil_id) REFERENCES perfil_rutix (id),
    CONSTRAINT fk_viajes_vehiculo FOREIGN KEY (vehiculo_id) REFERENCES vehiculos (id)
);

CREATE TABLE reservas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    perfil_id BIGINT NOT NULL,
    viaje_id BIGINT NOT NULL,
    asientos INT,
    estado VARCHAR(255) NOT NULL,
    fecha_alta DATETIME,
    fecha_actualizacion DATETIME,
    fecha_puntaje DATETIME,
    puntaje INT,
    proveedor VARCHAR(255),
    external_id VARCHAR(255),
    payment_id VARCHAR(255),
    CONSTRAINT fk_reservas_pasajero FOREIGN KEY (perfil_id) REFERENCES perfil_rutix (id),
    CONSTRAINT fk_reservas_viaje FOREIGN KEY (viaje_id) REFERENCES viajes (id)
);

CREATE TABLE pagos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reserva_id BIGINT NOT NULL,
    importe DECIMAL(38,2),
    estado VARCHAR(255),
    forma VARCHAR(255) NOT NULL,
    fecha_alta DATETIME,
    external_id VARCHAR(255),
    CONSTRAINT fk_pagos_reserva FOREIGN KEY (reserva_id) REFERENCES reservas (id)
);
