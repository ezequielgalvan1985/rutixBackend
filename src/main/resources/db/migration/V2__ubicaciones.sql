-- Entidades geograficas para el autocomplete de origen/destino (reingenieria
-- Pais/Provincia/Ciudad, tipo el buscador de Despegar). Seed cargado a mano:
-- no se pudo importar en vivo desde la API de Georef (apis.datos.gob.ar)
-- porque este entorno no tiene salida de red hacia ese dominio.
--
-- Las provincias son el listado oficial completo (24: 23 provincias + CABA).
-- Las ciudades son un subconjunto curado de las principales localidades por
-- provincia -- pensado para rutas de larga distancia, no es exhaustivo -- con
-- coordenadas aproximadas. id_georef queda NULL en todas las filas cargadas
-- asi; el campo existe justamente para poder correr despues un import real
-- contra Georef (cuando haya salida de red) sin duplicar lo ya cargado: el
-- import deberia hacer upsert por id_georef, dejando intactas las filas
-- cargadas a mano (id_georef NULL) salvo que matcheen por nombre+provincia.

CREATE TABLE paises (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    codigo_iso VARCHAR(2),
    CONSTRAINT uk_paises_nombre UNIQUE (nombre)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE provincias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    id_georef VARCHAR(20),
    pais_id BIGINT NOT NULL,
    CONSTRAINT fk_provincias_pais FOREIGN KEY (pais_id) REFERENCES paises (id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE ciudades (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    id_georef VARCHAR(20),
    latitud DOUBLE,
    longitud DOUBLE,
    provincia_id BIGINT NOT NULL,
    CONSTRAINT fk_ciudades_provincia FOREIGN KEY (provincia_id) REFERENCES provincias (id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE INDEX idx_ciudades_nombre ON ciudades (nombre);
CREATE INDEX idx_provincias_pais ON provincias (pais_id);
CREATE INDEX idx_ciudades_provincia ON ciudades (provincia_id);

INSERT INTO paises (nombre, codigo_iso) VALUES ('Argentina', 'AR');

INSERT INTO provincias (nombre, pais_id) VALUES
('Buenos Aires', (SELECT id FROM paises WHERE nombre = 'Argentina')),
('Ciudad Autónoma de Buenos Aires', (SELECT id FROM paises WHERE nombre = 'Argentina')),
('Catamarca', (SELECT id FROM paises WHERE nombre = 'Argentina')),
('Chaco', (SELECT id FROM paises WHERE nombre = 'Argentina')),
('Chubut', (SELECT id FROM paises WHERE nombre = 'Argentina')),
('Córdoba', (SELECT id FROM paises WHERE nombre = 'Argentina')),
('Corrientes', (SELECT id FROM paises WHERE nombre = 'Argentina')),
('Entre Ríos', (SELECT id FROM paises WHERE nombre = 'Argentina')),
('Formosa', (SELECT id FROM paises WHERE nombre = 'Argentina')),
('Jujuy', (SELECT id FROM paises WHERE nombre = 'Argentina')),
('La Pampa', (SELECT id FROM paises WHERE nombre = 'Argentina')),
('La Rioja', (SELECT id FROM paises WHERE nombre = 'Argentina')),
('Mendoza', (SELECT id FROM paises WHERE nombre = 'Argentina')),
('Misiones', (SELECT id FROM paises WHERE nombre = 'Argentina')),
('Neuquén', (SELECT id FROM paises WHERE nombre = 'Argentina')),
('Río Negro', (SELECT id FROM paises WHERE nombre = 'Argentina')),
('Salta', (SELECT id FROM paises WHERE nombre = 'Argentina')),
('San Juan', (SELECT id FROM paises WHERE nombre = 'Argentina')),
('San Luis', (SELECT id FROM paises WHERE nombre = 'Argentina')),
('Santa Cruz', (SELECT id FROM paises WHERE nombre = 'Argentina')),
('Santa Fe', (SELECT id FROM paises WHERE nombre = 'Argentina')),
('Santiago del Estero', (SELECT id FROM paises WHERE nombre = 'Argentina')),
('Tierra del Fuego, Antártida e Islas del Atlántico Sur', (SELECT id FROM paises WHERE nombre = 'Argentina')),
('Tucumán', (SELECT id FROM paises WHERE nombre = 'Argentina'));

INSERT INTO ciudades (nombre, latitud, longitud, provincia_id) VALUES
-- CABA
('Ciudad Autónoma de Buenos Aires', -34.6037, -58.3816, (SELECT id FROM provincias WHERE nombre = 'Ciudad Autónoma de Buenos Aires')),
-- Buenos Aires
('La Plata', -34.9205, -57.9536, (SELECT id FROM provincias WHERE nombre = 'Buenos Aires')),
('Mar del Plata', -38.0055, -57.5426, (SELECT id FROM provincias WHERE nombre = 'Buenos Aires')),
('Bahía Blanca', -38.7183, -62.2663, (SELECT id FROM provincias WHERE nombre = 'Buenos Aires')),
('Tandil', -37.3217, -59.1332, (SELECT id FROM provincias WHERE nombre = 'Buenos Aires')),
('Mercedes', -34.6514, -59.4306, (SELECT id FROM provincias WHERE nombre = 'Buenos Aires')),
('San Nicolás de los Arroyos', -33.3333, -60.2167, (SELECT id FROM provincias WHERE nombre = 'Buenos Aires')),
('Pergamino', -33.8958, -60.5736, (SELECT id FROM provincias WHERE nombre = 'Buenos Aires')),
('Junín', -34.5842, -60.9433, (SELECT id FROM provincias WHERE nombre = 'Buenos Aires')),
('Necochea', -38.5545, -58.7390, (SELECT id FROM provincias WHERE nombre = 'Buenos Aires')),
-- Córdoba
('Córdoba', -31.4201, -64.1888, (SELECT id FROM provincias WHERE nombre = 'Córdoba')),
('Villa María', -32.4075, -63.2402, (SELECT id FROM provincias WHERE nombre = 'Córdoba')),
('Río Cuarto', -33.1232, -64.3498, (SELECT id FROM provincias WHERE nombre = 'Córdoba')),
('Villa Carlos Paz', -31.4241, -64.4978, (SELECT id FROM provincias WHERE nombre = 'Córdoba')),
-- Santa Fe
('Santa Fe', -31.6333, -60.7000, (SELECT id FROM provincias WHERE nombre = 'Santa Fe')),
('Rosario', -32.9442, -60.6505, (SELECT id FROM provincias WHERE nombre = 'Santa Fe')),
('Rafaela', -31.2503, -61.4867, (SELECT id FROM provincias WHERE nombre = 'Santa Fe')),
('Venado Tuerto', -33.7466, -61.9688, (SELECT id FROM provincias WHERE nombre = 'Santa Fe')),
-- Mendoza
('Mendoza', -32.8908, -68.8272, (SELECT id FROM provincias WHERE nombre = 'Mendoza')),
('San Rafael', -34.6177, -68.3301, (SELECT id FROM provincias WHERE nombre = 'Mendoza')),
('Godoy Cruz', -32.9264, -68.8372, (SELECT id FROM provincias WHERE nombre = 'Mendoza')),
-- Tucumán
('San Miguel de Tucumán', -26.8083, -65.2176, (SELECT id FROM provincias WHERE nombre = 'Tucumán')),
('Concepción', -27.3333, -65.5833, (SELECT id FROM provincias WHERE nombre = 'Tucumán')),
-- Salta
('Salta', -24.7859, -65.4117, (SELECT id FROM provincias WHERE nombre = 'Salta')),
('San Ramón de la Nueva Orán', -23.1372, -64.3261, (SELECT id FROM provincias WHERE nombre = 'Salta')),
-- Jujuy
('San Salvador de Jujuy', -24.1858, -65.2995, (SELECT id FROM provincias WHERE nombre = 'Jujuy')),
-- Entre Ríos
('Paraná', -31.7319, -60.5238, (SELECT id FROM provincias WHERE nombre = 'Entre Ríos')),
('Concordia', -31.3929, -58.0209, (SELECT id FROM provincias WHERE nombre = 'Entre Ríos')),
('Gualeguaychú', -33.0094, -58.5159, (SELECT id FROM provincias WHERE nombre = 'Entre Ríos')),
-- Chaco
('Resistencia', -27.4512, -58.9867, (SELECT id FROM provincias WHERE nombre = 'Chaco')),
-- Corrientes
('Corrientes', -27.4698, -58.8341, (SELECT id FROM provincias WHERE nombre = 'Corrientes')),
-- Misiones
('Posadas', -27.3671, -55.8961, (SELECT id FROM provincias WHERE nombre = 'Misiones')),
('Puerto Iguazú', -25.5953, -54.5733, (SELECT id FROM provincias WHERE nombre = 'Misiones')),
-- Formosa
('Formosa', -26.1849, -58.1731, (SELECT id FROM provincias WHERE nombre = 'Formosa')),
-- Santiago del Estero
('Santiago del Estero', -27.7951, -64.2615, (SELECT id FROM provincias WHERE nombre = 'Santiago del Estero')),
-- Catamarca
('San Fernando del Valle de Catamarca', -28.4696, -65.7795, (SELECT id FROM provincias WHERE nombre = 'Catamarca')),
-- La Rioja
('La Rioja', -29.4131, -66.8558, (SELECT id FROM provincias WHERE nombre = 'La Rioja')),
-- San Juan
('San Juan', -31.5375, -68.5364, (SELECT id FROM provincias WHERE nombre = 'San Juan')),
-- San Luis
('San Luis', -33.3017, -66.3378, (SELECT id FROM provincias WHERE nombre = 'San Luis')),
-- La Pampa
('Santa Rosa', -36.6167, -64.2833, (SELECT id FROM provincias WHERE nombre = 'La Pampa')),
-- Neuquén
('Neuquén', -38.9516, -68.0591, (SELECT id FROM provincias WHERE nombre = 'Neuquén')),
('San Martín de los Andes', -40.1576, -71.3519, (SELECT id FROM provincias WHERE nombre = 'Neuquén')),
-- Río Negro
('Viedma', -40.8135, -62.9967, (SELECT id FROM provincias WHERE nombre = 'Río Negro')),
('San Carlos de Bariloche', -41.1335, -71.3103, (SELECT id FROM provincias WHERE nombre = 'Río Negro')),
('General Roca', -39.0333, -67.5833, (SELECT id FROM provincias WHERE nombre = 'Río Negro')),
-- Chubut
('Rawson', -43.3002, -65.1023, (SELECT id FROM provincias WHERE nombre = 'Chubut')),
('Comodoro Rivadavia', -45.8647, -67.4822, (SELECT id FROM provincias WHERE nombre = 'Chubut')),
('Trelew', -43.2489, -65.3051, (SELECT id FROM provincias WHERE nombre = 'Chubut')),
('Puerto Madryn', -42.7692, -65.0385, (SELECT id FROM provincias WHERE nombre = 'Chubut')),
-- Santa Cruz
('Río Gallegos', -51.6230, -69.2168, (SELECT id FROM provincias WHERE nombre = 'Santa Cruz')),
('El Calafate', -50.3379, -72.2648, (SELECT id FROM provincias WHERE nombre = 'Santa Cruz')),
-- Tierra del Fuego
('Ushuaia', -54.8019, -68.3030, (SELECT id FROM provincias WHERE nombre = 'Tierra del Fuego, Antártida e Islas del Atlántico Sur')),
('Río Grande', -53.7879, -67.7059, (SELECT id FROM provincias WHERE nombre = 'Tierra del Fuego, Antártida e Islas del Atlántico Sur'));
