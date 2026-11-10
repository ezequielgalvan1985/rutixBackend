import io

path = "src/main/java/elementary/Rutix/Viajes/ReservaController.java"
with io.open(path, "r", encoding="utf-8") as f:
    content = f.read()

# 1) import
old_import = "import elementary.Rutix.Viajes.comandos.pagos.RegistrarPagoDeReservaComando;\n"
assert content.count(old_import) == 1, content.count(old_import)
content = content.replace(old_import, old_import + "import elementary.Rutix.Viajes.comandos.pagos.DevolucionPagoDeReservaComando;\n")

# 2) field
old_field = "        private final EliminarPagoDeReservaComando eliminarPago;\n"
assert content.count(old_field) == 1, content.count(old_field)
content = content.replace(old_field, old_field + "        private final DevolucionPagoDeReservaComando devolucionPago;\n")

# 3) constructor param
old_ctor_param = "                             EliminarPagoDeReservaComando eliminarPago,\n"
assert content.count(old_ctor_param) == 1, content.count(old_ctor_param)
content = content.replace(old_ctor_param, old_ctor_param + "                             DevolucionPagoDeReservaComando devolucionPago,\n")

# 4) constructor assignment
old_assign = "        this.eliminarPago = eliminarPago;\n"
assert content.count(old_assign) == 1, content.count(old_assign)
content = content.replace(old_assign, old_assign + "        this.devolucionPago = devolucionPago;\n")

# 5) endpoint after eliminar-pago
old_endpoint = """    @DeleteMapping("/eliminar-pago")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestBody @Valid EliminarPagoDeReservaComandoDto dto){
        eliminarPago.execute(dto);
    }

}
"""
assert content.count(old_endpoint) == 1, content.count(old_endpoint)
new_endpoint = """    @DeleteMapping("/eliminar-pago")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestBody @Valid EliminarPagoDeReservaComandoDto dto){
        eliminarPago.execute(dto);
    }

    @PutMapping("/devolver-pago")
    @PreAuthorize("hasRole('ADMIN')")
    public void devolverPago(@RequestBody @Valid DevolucionPagoDeReservaComandoDto dto){
        devolucionPago.execute(dto);
    }

}
"""
content = content.replace(old_endpoint, new_endpoint)

with io.open(path, "w", encoding="utf-8") as f:
    f.write(content)

print("ReservaController.java patched OK")
