import io

base = "Viajes_dominio_Viaje.java".replace("Viajes_dominio_", "")
path = "src/main/java/elementary/Rutix/Viajes/dominio/Viaje.java"

with io.open(path, "r", encoding="utf-8") as f:
    content = f.read()

old = """    public void completar(){
        if (this.getEstado() != EstadoViajeEnum.CREADO)
            throw new ReglaNegocioException("Solo se puede Completar un viaje en estado CREADO");
        this.setEstado(EstadoViajeEnum.COMPLETO);
    }
"""
assert content.count(old) == 1, f"old completar() block occurrences: {content.count(old)}"

new = old + """
    public void cancelar(){
        if (this.getEstado() != EstadoViajeEnum.CREADO && this.getEstado() != EstadoViajeEnum.COMPLETO)
            throw new ReglaNegocioException("Solo se puede Cancelar un viaje en estado CREADO o COMPLETO");
        this.setEstado(EstadoViajeEnum.CANCELADO);
    }
"""

content = content.replace(old, new)

with io.open(path, "w", encoding="utf-8") as f:
    f.write(content)

print("Viaje.java patched OK")
