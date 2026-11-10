import io

path = "src/main/java/elementary/Rutix/Viajes/ViajeController.java"
with io.open(path, "r", encoding="utf-8") as f:
    content = f.read()

# 1) import
old_import = "import elementary.Rutix.Viajes.comandos.CompletarViajeComando;\n"
assert content.count(old_import) == 1, content.count(old_import)
content = content.replace(old_import, old_import + "import elementary.Rutix.Viajes.comandos.CancelarViajeComando;\n")

# 2) field
old_field = "    private final CompletarViajeComando completarViajeUseCase;\n"
assert content.count(old_field) == 1, content.count(old_field)
content = content.replace(old_field, old_field + "    private final CancelarViajeComando cancelarViajeUseCase;\n")

# 3) constructor param
old_ctor_param = "                           CompletarViajeComando completarViajeUseCase,\n"
assert content.count(old_ctor_param) == 1, content.count(old_ctor_param)
content = content.replace(old_ctor_param, old_ctor_param + "                           CancelarViajeComando cancelarViajeUseCase,\n")

# 4) constructor assignment
old_assign = "        this.completarViajeUseCase = completarViajeUseCase;\n"
assert content.count(old_assign) == 1, content.count(old_assign)
content = content.replace(old_assign, old_assign + "        this.cancelarViajeUseCase = cancelarViajeUseCase;\n")

# 5) endpoint, right after /completar mapping
old_endpoint = """    @PutMapping(value="/{id}/completar")
    public void completar(@PathVariable("id") Long id){
        completarViajeUseCase.execute(id);
    }
"""
assert content.count(old_endpoint) == 1, content.count(old_endpoint)
new_endpoint = old_endpoint + """
    @PutMapping(value="/{id}/cancelar")
    @PreAuthorize("hasRole('ADMIN') or @viajeSecurity.esConductor(#id, authentication)")
    public void cancelar(@PathVariable("id") Long id){
        cancelarViajeUseCase.execute(id);
    }
"""
content = content.replace(old_endpoint, new_endpoint)

with io.open(path, "w", encoding="utf-8") as f:
    f.write(content)

print("ViajeController.java patched OK")
