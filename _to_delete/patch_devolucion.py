import io

def read(p):
    with io.open(p, "r", encoding="utf-8") as f:
        return f.read()

def write(p, c):
    with io.open(p, "w", encoding="utf-8") as f:
        f.write(c)

base = "src/main/java/elementary/Rutix/"

# 1) EstadoPagoEnum: add DEVUELTO
p = base + "common/Enum/EstadoPagoEnum.java"
c = read(p)
old = """public enum EstadoPagoEnum {
    PENDIENTE,
    CONFIRMADO,
    ERROR
}
"""
assert c.count(old) == 1, ("EstadoPagoEnum", c.count(old))
new = """public enum EstadoPagoEnum {
    PENDIENTE,
    CONFIRMADO,
    ERROR,
    DEVUELTO
}
"""
write(p, c.replace(old, new))
print("1) EstadoPagoEnum.java OK")

# 2) Pago.java: import + devolver()
p = base + "Viajes/dominio/Pago.java"
c = read(p)
old_import = "import elementary.Rutix.common.Enum.MetodoPagoEnum;\n"
assert c.count(old_import) == 1, ("Pago import", c.count(old_import))
c = c.replace(old_import, old_import + "import elementary.Rutix.common.excepciones.ReglaNegocioException;\n")

old_tail = """    private String externalId;

}
"""
assert c.count(old_tail) == 1, ("Pago tail", c.count(old_tail))
new_tail = """    private String externalId;

    public void devolver(){
        if (this.getEstado() != EstadoPagoEnum.PENDIENTE && this.getEstado() != EstadoPagoEnum.CONFIRMADO)
            throw new ReglaNegocioException("Solo se puede Devolver un pago en estado PENDIENTE o CONFIRMADO");
        this.setEstado(EstadoPagoEnum.DEVUELTO);
    }

}
"""
c = c.replace(old_tail, new_tail)
write(p, c)
print("2) Pago.java OK")

# 3) Reserva.java: cancelarPorViajeCancelado()
p = base + "Viajes/dominio/Reserva.java"
c = read(p)
old = """    public void cancelar(){
        if(this.getEstado()!=EstadoReserva.PENDIENTE) throw new ReglaNegocioException("Solo se puede Cancelar una reserva pendiente");
        this.setEstado(EstadoReserva.CANCELADA);
    }
}
"""
assert c.count(old) == 1, ("Reserva cancelar", c.count(old))
new = """    public void cancelar(){
        if(this.getEstado()!=EstadoReserva.PENDIENTE) throw new ReglaNegocioException("Solo se puede Cancelar una reserva pendiente");
        this.setEstado(EstadoReserva.CANCELADA);
    }

    /**
     * Cancelacion en cascada por cancelacion del Viaje: a diferencia de cancelar(),
     * aqui la reserva puede venir de PENDIENTE, CONFIRMADA o PAGADA (el pasajero no
     * elige cancelar, el viaje ya no se va a realizar). Si ya estaba en un estado
     * terminal (CANCELADA/RECHAZADA) no hace nada.
     */
    public void cancelarPorViajeCancelado(){
        if (this.getEstado() == EstadoReserva.CANCELADA || this.getEstado() == EstadoReserva.RECHAZADA)
            return;
        this.setEstado(EstadoReserva.CANCELADA);
    }
}
"""
c = c.replace(old, new)
write(p, c)
print("3) Reserva.java OK")

# 4) Viaje.java: cascade in cancelar()
p = base + "Viajes/dominio/Viaje.java"
c = read(p)
old = """    public void cancelar(){
        if (this.getEstado() != EstadoViajeEnum.CREADO && this.getEstado() != EstadoViajeEnum.COMPLETO)
            throw new ReglaNegocioException("Solo se puede Cancelar un viaje en estado CREADO o COMPLETO");
        this.setEstado(EstadoViajeEnum.CANCELADO);
    }
"""
assert c.count(old) == 1, ("Viaje cancelar", c.count(old))
new = """    public void cancelar(){
        if (this.getEstado() != EstadoViajeEnum.CREADO && this.getEstado() != EstadoViajeEnum.COMPLETO)
            throw new ReglaNegocioException("Solo se puede Cancelar un viaje en estado CREADO o COMPLETO");
        this.setEstado(EstadoViajeEnum.CANCELADO);
        for (Reserva r : this.listaReservas) {
            r.cancelarPorViajeCancelado();
        }
    }
"""
c = c.replace(old, new)
write(p, c)
print("4) Viaje.java OK")
