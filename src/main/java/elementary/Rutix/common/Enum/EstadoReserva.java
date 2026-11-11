package elementary.Rutix.common.Enum;

public enum EstadoReserva {
    CANCELADA,
    PENDIENTE,
    CONFIRMADA,
    // Se setean al elegir la forma de pago (ver Reserva.iniciarPago): el
    // pago todavia no esta confirmado, asi que la reserva no pasa directo a
    // PAGADA. EFECTIVO lo confirma el conductor al recibirlo; MERCADOPAGO lo
    // confirma el webhook de Mercado Pago cuando se implemente la
    // integracion real (ver IntegracionPagoExterno).
    PENDIENTE_CONFIRMACION_EFECTIVO,
    PENDIENTE_CONFIRMACION_MERCADOPAGO,
    PAGADA,
    RECHAZADA
}
