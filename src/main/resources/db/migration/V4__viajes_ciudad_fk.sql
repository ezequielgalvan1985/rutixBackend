-- Paso 4 de la reingenieria origen/destino: Viaje pasa de guardar
-- ciudad_partida/ciudad_destino como texto libre a referenciar la entidad
-- Ciudad por FK (igual que ya hace con conductor/vehiculo).
--
-- A pedido: en vez de migrar los datos existentes (viajes/reservas/pagos de
-- prueba), se borran directo y se arranca de cero con las columnas FK ya
-- obligatorias. Mas simple que un backfill y, al no ser datos reales, no hay
-- nada que preservar.

DELETE FROM pagos;
DELETE FROM reservas;
DELETE FROM viajes;

ALTER TABLE viajes DROP COLUMN ciudad_partida;
ALTER TABLE viajes DROP COLUMN ciudad_destino;

ALTER TABLE viajes ADD COLUMN ciudad_partida_id BIGINT NOT NULL;
ALTER TABLE viajes ADD COLUMN ciudad_destino_id BIGINT NOT NULL;

ALTER TABLE viajes ADD CONSTRAINT fk_viajes_ciudad_partida
    FOREIGN KEY (ciudad_partida_id) REFERENCES ciudades (id);
ALTER TABLE viajes ADD CONSTRAINT fk_viajes_ciudad_destino
    FOREIGN KEY (ciudad_destino_id) REFERENCES ciudades (id);

CREATE INDEX idx_viajes_ciudad_partida ON viajes (ciudad_partida_id);
CREATE INDEX idx_viajes_ciudad_destino ON viajes (ciudad_destino_id);
