import io

p = "src/main/resources/db/migration/V2__ubicaciones.sql"
with io.open(p, "r", encoding="utf-8") as f:
    c = f.read()

old = '''CREATE TABLE paises (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    codigo_iso VARCHAR(2),
    CONSTRAINT uk_paises_nombre UNIQUE (nombre)
);

CREATE TABLE provincias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    id_georef VARCHAR(20),
    pais_id BIGINT NOT NULL,
    CONSTRAINT fk_provincias_pais FOREIGN KEY (pais_id) REFERENCES paises (id)
);

CREATE TABLE ciudades (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    id_georef VARCHAR(20),
    latitud DOUBLE,
    longitud DOUBLE,
    provincia_id BIGINT NOT NULL,
    CONSTRAINT fk_ciudades_provincia FOREIGN KEY (provincia_id) REFERENCES provincias (id)
);
'''
assert c.count(old) == 1, c.count(old)
new = '''CREATE TABLE paises (
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
'''
c = c.replace(old, new)

with io.open(p, "w", encoding="utf-8") as f:
    f.write(c)
print("V2 charset fix OK")
