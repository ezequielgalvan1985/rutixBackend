-- Ampliacion del seed de ciudades (V2 se cargo a mano y quedo con un
-- subconjunto chico por provincia; esta migracion agrega mas localidades
-- por provincia, cargadas tambien a mano desde conocimiento propio, para
-- tener mejor cobertura en el autocomplete de origen/destino). No toca
-- V2 (ya aplicada): Flyway no permite modificar una migracion ya corrida
-- sin romper el checksum, asi que esto va en un archivo nuevo.
-- id_georef queda NULL igual que en V2, por la misma razon (permitir un
-- import real contra Georef mas adelante sin duplicar lo cargado a mano).

INSERT INTO ciudades (nombre, latitud, longitud, provincia_id) VALUES
-- Buenos Aires
('Olavarría', -36.8927, -60.3225, (SELECT id FROM provincias WHERE nombre = 'Buenos Aires')),
('Azul', -36.7769, -59.8586, (SELECT id FROM provincias WHERE nombre = 'Buenos Aires')),
('Chivilcoy', -34.8964, -60.0156, (SELECT id FROM provincias WHERE nombre = 'Buenos Aires')),
('Zárate', -34.0974, -59.0243, (SELECT id FROM provincias WHERE nombre = 'Buenos Aires')),
('Campana', -34.1858, -58.9577, (SELECT id FROM provincias WHERE nombre = 'Buenos Aires')),
('Luján', -34.5703, -59.1051, (SELECT id FROM provincias WHERE nombre = 'Buenos Aires')),
('San Pedro', -33.6786, -59.6667, (SELECT id FROM provincias WHERE nombre = 'Buenos Aires')),
('Dolores', -36.3132, -57.6792, (SELECT id FROM provincias WHERE nombre = 'Buenos Aires')),
('Tres Arroyos', -38.3739, -60.2767, (SELECT id FROM provincias WHERE nombre = 'Buenos Aires')),
('Pehuajó', -35.8106, -61.8906, (SELECT id FROM provincias WHERE nombre = 'Buenos Aires')),
('Trenque Lauquen', -35.9711, -62.7327, (SELECT id FROM provincias WHERE nombre = 'Buenos Aires')),
('Chascomús', -35.5716, -58.0138, (SELECT id FROM provincias WHERE nombre = 'Buenos Aires')),
('Pinamar', -37.1075, -56.8609, (SELECT id FROM provincias WHERE nombre = 'Buenos Aires')),
('Villa Gesell', -37.2633, -56.9733, (SELECT id FROM provincias WHERE nombre = 'Buenos Aires')),
('San Clemente del Tuyú', -36.3539, -56.7256, (SELECT id FROM provincias WHERE nombre = 'Buenos Aires')),
-- Córdoba
('Alta Gracia', -31.6534, -64.4281, (SELECT id FROM provincias WHERE nombre = 'Córdoba')),
('Bell Ville', -32.6281, -62.6894, (SELECT id FROM provincias WHERE nombre = 'Córdoba')),
('San Francisco', -31.4273, -62.0836, (SELECT id FROM provincias WHERE nombre = 'Córdoba')),
('Jesús María', -30.9808, -64.0956, (SELECT id FROM provincias WHERE nombre = 'Córdoba')),
('Cosquín', -31.2436, -64.4692, (SELECT id FROM provincias WHERE nombre = 'Córdoba')),
('Marcos Juárez', -32.6953, -62.1067, (SELECT id FROM provincias WHERE nombre = 'Córdoba')),
('Deán Funes', -30.4234, -64.3608, (SELECT id FROM provincias WHERE nombre = 'Córdoba')),
('Villa Dolores', -31.9436, -65.1889, (SELECT id FROM provincias WHERE nombre = 'Córdoba')),
-- Santa Fe
('Reconquista', -29.1500, -59.6500, (SELECT id FROM provincias WHERE nombre = 'Santa Fe')),
('San Lorenzo', -32.7453, -60.7417, (SELECT id FROM provincias WHERE nombre = 'Santa Fe')),
('Casilda', -33.0458, -61.1667, (SELECT id FROM provincias WHERE nombre = 'Santa Fe')),
('Villa Constitución', -33.2333, -60.3333, (SELECT id FROM provincias WHERE nombre = 'Santa Fe')),
('Esperanza', -31.4572, -60.9306, (SELECT id FROM provincias WHERE nombre = 'Santa Fe')),
('Santo Tomé', -31.6756, -60.7781, (SELECT id FROM provincias WHERE nombre = 'Santa Fe')),
('Rufino', -34.2667, -62.7167, (SELECT id FROM provincias WHERE nombre = 'Santa Fe')),
-- Mendoza
('Malargüe', -35.4761, -69.5831, (SELECT id FROM provincias WHERE nombre = 'Mendoza')),
('Tunuyán', -33.5781, -69.0181, (SELECT id FROM provincias WHERE nombre = 'Mendoza')),
('San Martín', -33.0806, -68.4706, (SELECT id FROM provincias WHERE nombre = 'Mendoza')),
('Luján de Cuyo', -33.0333, -68.8833, (SELECT id FROM provincias WHERE nombre = 'Mendoza')),
('General Alvear', -34.9833, -67.7000, (SELECT id FROM provincias WHERE nombre = 'Mendoza')),
-- Tucumán
('Tafí Viejo', -26.7333, -65.2500, (SELECT id FROM provincias WHERE nombre = 'Tucumán')),
('Monteros', -27.1667, -65.5000, (SELECT id FROM provincias WHERE nombre = 'Tucumán')),
('Aguilares', -27.4333, -65.6167, (SELECT id FROM provincias WHERE nombre = 'Tucumán')),
('Bella Vista', -27.0667, -65.3000, (SELECT id FROM provincias WHERE nombre = 'Tucumán')),
-- Salta
('Tartagal', -22.5333, -63.8167, (SELECT id FROM provincias WHERE nombre = 'Salta')),
('Metán', -25.4833, -64.9667, (SELECT id FROM provincias WHERE nombre = 'Salta')),
('Cafayate', -26.0725, -65.9747, (SELECT id FROM provincias WHERE nombre = 'Salta')),
('Rosario de la Frontera', -25.8000, -64.9667, (SELECT id FROM provincias WHERE nombre = 'Salta')),
-- Jujuy
('Palpalá', -24.2500, -65.2167, (SELECT id FROM provincias WHERE nombre = 'Jujuy')),
('Libertador General San Martín', -23.8000, -64.7833, (SELECT id FROM provincias WHERE nombre = 'Jujuy')),
('Perico', -24.3833, -65.1000, (SELECT id FROM provincias WHERE nombre = 'Jujuy')),
('Humahuaca', -23.2069, -65.3511, (SELECT id FROM provincias WHERE nombre = 'Jujuy')),
('Tilcara', -23.5667, -65.4000, (SELECT id FROM provincias WHERE nombre = 'Jujuy')),
('La Quiaca', -22.1067, -65.6033, (SELECT id FROM provincias WHERE nombre = 'Jujuy')),
-- Entre Ríos
('Gualeguay', -33.1419, -59.3094, (SELECT id FROM provincias WHERE nombre = 'Entre Ríos')),
('Chajarí', -30.7500, -57.9833, (SELECT id FROM provincias WHERE nombre = 'Entre Ríos')),
('Concepción del Uruguay', -32.4833, -58.2333, (SELECT id FROM provincias WHERE nombre = 'Entre Ríos')),
('Colón', -32.2242, -58.1458, (SELECT id FROM provincias WHERE nombre = 'Entre Ríos')),
('Villaguay', -31.8667, -59.0167, (SELECT id FROM provincias WHERE nombre = 'Entre Ríos')),
('Victoria', -32.6167, -60.1500, (SELECT id FROM provincias WHERE nombre = 'Entre Ríos')),
('Nogoyá', -32.3833, -59.7833, (SELECT id FROM provincias WHERE nombre = 'Entre Ríos')),
('San Salvador', -31.6167, -58.5167, (SELECT id FROM provincias WHERE nombre = 'Entre Ríos')),
('La Paz', -30.7333, -59.6333, (SELECT id FROM provincias WHERE nombre = 'Entre Ríos')),
-- Chaco
('Presidencia Roque Sáenz Peña', -26.7833, -60.4333, (SELECT id FROM provincias WHERE nombre = 'Chaco')),
('Villa Ángela', -27.5833, -60.7167, (SELECT id FROM provincias WHERE nombre = 'Chaco')),
('Charata', -27.2167, -61.2000, (SELECT id FROM provincias WHERE nombre = 'Chaco')),
('General José de San Martín', -26.5500, -59.3333, (SELECT id FROM provincias WHERE nombre = 'Chaco')),
-- Corrientes
('Goya', -29.1333, -59.2667, (SELECT id FROM provincias WHERE nombre = 'Corrientes')),
('Mercedes', -29.2000, -58.0833, (SELECT id FROM provincias WHERE nombre = 'Corrientes')),
('Paso de los Libres', -29.7167, -57.0833, (SELECT id FROM provincias WHERE nombre = 'Corrientes')),
('Curuzú Cuatiá', -29.7833, -58.0500, (SELECT id FROM provincias WHERE nombre = 'Corrientes')),
('Santo Tomé', -28.5558, -56.0558, (SELECT id FROM provincias WHERE nombre = 'Corrientes')),
-- Misiones
('Oberá', -27.4833, -55.1167, (SELECT id FROM provincias WHERE nombre = 'Misiones')),
('Eldorado', -26.4000, -54.6167, (SELECT id FROM provincias WHERE nombre = 'Misiones')),
('Apóstoles', -27.9000, -55.7500, (SELECT id FROM provincias WHERE nombre = 'Misiones')),
('Jardín América', -27.0333, -55.2167, (SELECT id FROM provincias WHERE nombre = 'Misiones')),
-- Formosa
('Clorinda', -25.2833, -57.7167, (SELECT id FROM provincias WHERE nombre = 'Formosa')),
('Pirané', -25.7333, -59.1167, (SELECT id FROM provincias WHERE nombre = 'Formosa')),
('El Colorado', -26.3000, -59.3500, (SELECT id FROM provincias WHERE nombre = 'Formosa')),
-- Santiago del Estero
('La Banda', -27.7333, -64.2333, (SELECT id FROM provincias WHERE nombre = 'Santiago del Estero')),
('Termas de Río Hondo', -27.4833, -64.8500, (SELECT id FROM provincias WHERE nombre = 'Santiago del Estero')),
('Frías', -28.6333, -65.1500, (SELECT id FROM provincias WHERE nombre = 'Santiago del Estero')),
-- Catamarca
('Andalgalá', -27.6000, -66.3167, (SELECT id FROM provincias WHERE nombre = 'Catamarca')),
('Belén', -27.6500, -67.0333, (SELECT id FROM provincias WHERE nombre = 'Catamarca')),
('Recreo', -29.2667, -65.0667, (SELECT id FROM provincias WHERE nombre = 'Catamarca')),
('Santa María', -26.7000, -66.0500, (SELECT id FROM provincias WHERE nombre = 'Catamarca')),
-- La Rioja
('Chilecito', -29.1667, -67.5000, (SELECT id FROM provincias WHERE nombre = 'La Rioja')),
('Chamical', -30.3500, -66.3167, (SELECT id FROM provincias WHERE nombre = 'La Rioja')),
('Aimogasta', -28.5667, -66.8167, (SELECT id FROM provincias WHERE nombre = 'La Rioja')),
-- San Juan
('Chimbas', -31.5167, -68.5500, (SELECT id FROM provincias WHERE nombre = 'San Juan')),
('Rivadavia', -31.5333, -68.6167, (SELECT id FROM provincias WHERE nombre = 'San Juan')),
('Caucete', -31.6500, -68.2667, (SELECT id FROM provincias WHERE nombre = 'San Juan')),
('Jáchal', -30.2500, -68.7500, (SELECT id FROM provincias WHERE nombre = 'San Juan')),
('Pocito', -31.6500, -68.6167, (SELECT id FROM provincias WHERE nombre = 'San Juan')),
-- San Luis
('Villa Mercedes', -33.6667, -65.4500, (SELECT id FROM provincias WHERE nombre = 'San Luis')),
('Merlo', -32.3333, -65.0167, (SELECT id FROM provincias WHERE nombre = 'San Luis')),
('Concarán', -32.5167, -65.3333, (SELECT id FROM provincias WHERE nombre = 'San Luis')),
-- La Pampa
('General Pico', -35.6667, -63.7500, (SELECT id FROM provincias WHERE nombre = 'La Pampa')),
('Realicó', -35.0333, -64.2500, (SELECT id FROM provincias WHERE nombre = 'La Pampa')),
('Victorica', -36.2167, -65.4333, (SELECT id FROM provincias WHERE nombre = 'La Pampa')),
-- Neuquén
('Cutral Có', -38.9333, -69.2167, (SELECT id FROM provincias WHERE nombre = 'Neuquén')),
('Plaza Huincul', -38.9167, -69.2000, (SELECT id FROM provincias WHERE nombre = 'Neuquén')),
('Zapala', -38.9000, -70.0667, (SELECT id FROM provincias WHERE nombre = 'Neuquén')),
('Villa La Angostura', -40.7667, -71.6500, (SELECT id FROM provincias WHERE nombre = 'Neuquén')),
('Chos Malal', -37.3833, -70.2667, (SELECT id FROM provincias WHERE nombre = 'Neuquén')),
-- Río Negro
('Cipolletti', -38.9333, -67.9833, (SELECT id FROM provincias WHERE nombre = 'Río Negro')),
('Villa Regina', -39.1000, -67.0667, (SELECT id FROM provincias WHERE nombre = 'Río Negro')),
('El Bolsón', -41.9667, -71.5333, (SELECT id FROM provincias WHERE nombre = 'Río Negro')),
('San Antonio Oeste', -40.7333, -64.9500, (SELECT id FROM provincias WHERE nombre = 'Río Negro')),
-- Chubut
('Esquel', -42.9167, -71.3167, (SELECT id FROM provincias WHERE nombre = 'Chubut')),
('Sarmiento', -45.5833, -69.0667, (SELECT id FROM provincias WHERE nombre = 'Chubut')),
('Gaiman', -43.2833, -65.4833, (SELECT id FROM provincias WHERE nombre = 'Chubut')),
-- Santa Cruz
('Caleta Olivia', -46.4333, -67.5333, (SELECT id FROM provincias WHERE nombre = 'Santa Cruz')),
('Pico Truncado', -46.8000, -67.9667, (SELECT id FROM provincias WHERE nombre = 'Santa Cruz')),
('Perito Moreno', -46.6000, -70.9167, (SELECT id FROM provincias WHERE nombre = 'Santa Cruz')),
('Puerto San Julián', -49.3067, -67.7181, (SELECT id FROM provincias WHERE nombre = 'Santa Cruz')),
-- Tierra del Fuego
('Tolhuin', -54.5167, -67.2000, (SELECT id FROM provincias WHERE nombre = 'Tierra del Fuego, Antártida e Islas del Atlántico Sur'));
