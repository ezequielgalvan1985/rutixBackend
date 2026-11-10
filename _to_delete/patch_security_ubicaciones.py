import io

p = "src/main/java/elementary/Rutix/Security/SecurityConfig.java"
with io.open(p, "r", encoding="utf-8") as f:
    c = f.read()

old = '''                        // La busqueda/listado de viajes es publica: cualquiera puede explorar
                        // viajes sin loguearse. BuscarViajesConsulta ya maneja el caso anonimo
                        // (sin perfil logueado, accionesDisponibles queda vacio).
                        .requestMatchers(HttpMethod.POST, "/v1/viajes/buscar").permitAll()
                        .anyRequest().authenticated()
'''
assert c.count(old) == 1, c.count(old)
new = '''                        // La busqueda/listado de viajes es publica: cualquiera puede explorar
                        // viajes sin loguearse. BuscarViajesConsulta ya maneja el caso anonimo
                        // (sin perfil logueado, accionesDisponibles queda vacio).
                        .requestMatchers(HttpMethod.POST, "/v1/viajes/buscar").permitAll()
                        // El autocompletado de ciudades (combo origen/destino) tambien es
                        // publico: hace falta antes incluso de loguearse, para armar la busqueda.
                        .requestMatchers(HttpMethod.GET, "/v1/ubicaciones/**").permitAll()
                        .anyRequest().authenticated()
'''
c = c.replace(old, new)

with io.open(p, "w", encoding="utf-8") as f:
    f.write(c)
print("SecurityConfig.java patched OK")
