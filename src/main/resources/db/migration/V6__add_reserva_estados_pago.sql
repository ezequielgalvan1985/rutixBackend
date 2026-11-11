-- Nuevos estados intermedios de Reserva (ver Reserva.iniciarPago): al elegir
-- la forma de pago, la reserva pasa a PENDIENTE_CONFIRMACION_EFECTIVO o
-- PENDIENTE_CONFIRMACION_MERCADOPAGO en vez de ir directo a PAGADA.
--
-- Igual que con viajes.estado (V5): la columna real de reservas puede ser
-- un ENUM mas viejo que el EstadoReserva de Java actual (la tabla se creo
-- antes de que Flyway empezara a versionar, ver baseline-on-migrate +
-- baseline-version 1), asi que se redefine agregando los valores nuevos
-- para no repetir el mismo "Data truncated for column 'estado'".
ALTER TABLE reservas MODIFY COLUMN estado
    ENUM('PENDIENTE','CONFIRMADA','PAGADA','RECHAZADA','CANCELADA',
         'PENDIENTE_CONFIRMACION_EFECTIVO','PENDIENTE_CONFIRMACION_MERCADOPAGO')
    NOT NULL;
