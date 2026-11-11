-- Bug: al completar un viaje (estado -> COMPLETO) la base devuelve "Data
-- truncated for column 'estado'". La columna real quedo creada (antes de
-- que Flyway empezara a versionar, ver baseline-on-migrate + baseline-version
-- 1 en application-local.yml) como
-- ENUM('CANCELADO','CREADO','EN_CURSO','FINALIZADO'), sin 'COMPLETO', que es
-- el valor que usa Viaje.completar(). Se redefine el ENUM agregando el valor
-- faltante y dejando los 5 estados de EstadoViajeEnum (orden sin
-- importancia, MySQL/MariaDB lo valida por nombre).
ALTER TABLE viajes MODIFY COLUMN estado
    ENUM('CREADO','COMPLETO','EN_CURSO','FINALIZADO','CANCELADO');
