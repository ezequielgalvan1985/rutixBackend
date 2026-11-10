import io

def read(p):
    with io.open(p, "r", encoding="utf-8") as f:
        return f.read()

def write(p, c):
    with io.open(p, "w", encoding="utf-8") as f:
        f.write(c)

base = "src/main/java/elementary/Rutix/"

# 1) SecurityConfig.java: permitir POST /v1/viajes/buscar sin autenticacion
p = base + "Security/SecurityConfig.java"
c = read(p)
old = '''                .authorizeHttpRequests(auth -> auth
                        // El preflight CORS (OPTIONS) nunca manda Authorization: si no se
                        // permite explicitamente, cae en anyRequest().authenticated() y el
                        // navegador ve un 401 sin headers CORS (bloqueado por CORS, sin serlo).
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .anyRequest().authenticated()
                )
'''
assert c.count(old) == 1, ("authorizeHttpRequests", c.count(old))
new = '''                .authorizeHttpRequests(auth -> auth
                        // El preflight CORS (OPTIONS) nunca manda Authorization: si no se
                        // permite explicitamente, cae en anyRequest().authenticated() y el
                        // navegador ve un 401 sin headers CORS (bloqueado por CORS, sin serlo).
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        // La busqueda/listado de viajes es publica: cualquiera puede explorar
                        // viajes sin loguearse. BuscarViajesConsulta ya maneja el caso anonimo
                        // (sin perfil logueado, accionesDisponibles queda vacio).
                        .requestMatchers(HttpMethod.POST, "/v1/viajes/buscar").permitAll()
                        .anyRequest().authenticated()
                )
'''
c = c.replace(old, new)
write(p, c)
print("1) SecurityConfig.java OK")

# 2) BuscarViajesConsulta.java: no asumir usuario autenticado
p = base + "Viajes/consultas/BuscarViajesConsulta.java"
c = read(p)

old_import = "import org.springframework.security.core.Authentication;\n"
assert c.count(old_import) == 1, ("consulta import auth", c.count(old_import))
c = c.replace(old_import, old_import + "import org.springframework.security.authentication.AnonymousAuthenticationToken;\n")

old_auth_block = '''        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long uid = Long.parseLong(auth.getName());
        this.perfilLogueado = repoPerfil.findByUsuarioId(uid).orElseThrow(() -> new ReglaNegocioException("No se encontró el perfil"));

        Page<Viaje> result = this.repo.buscarViajes(input.getFechaDesde(), input.getFechaHasta(), input.getCiudadPartida(), input.getCiudadDestino(), input.getEstado(), pageable);
'''
assert c.count(old_auth_block) == 1, ("consulta auth block", c.count(old_auth_block))
new_auth_block = '''        // La busqueda es publica (ver SecurityConfig): sin token, auth es un
        // AnonymousAuthenticationToken, no null. Si hay un usuario real logueado
        // usamos su perfil para armar accionesDisponibles; si no, queda sin acciones
        // (debe iniciar sesion para reservar/gestionar).
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        this.perfilLogueado = null;
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            Long uid = Long.parseLong(auth.getName());
            this.perfilLogueado = repoPerfil.findByUsuarioId(uid).orElse(null);
        }

        Page<Viaje> result = this.repo.buscarViajes(input.getFechaDesde(), input.getFechaHasta(), input.getCiudadPartida(), input.getCiudadDestino(), input.getEstado(), pageable);
'''
c = c.replace(old_auth_block, new_auth_block)

old_acciones = '''    private List<String>getListaAcciones(Viaje v, PerfilRutix p){
        List<String> lista = new ArrayList<>();
        //si EL conductor esta viendo el detalle del viaje
        if (v.getConductor().getId() == p.getId()){
'''
assert c.count(old_acciones) == 1, ("consulta getListaAcciones", c.count(old_acciones))
new_acciones = '''    private List<String>getListaAcciones(Viaje v, PerfilRutix p){
        List<String> lista = new ArrayList<>();
        // Sin sesion (busqueda publica/anonima) no hay ninguna accion disponible:
        // hace falta iniciar sesion para reservar o gestionar el viaje.
        if (p == null) {
            return lista;
        }
        //si EL conductor esta viendo el detalle del viaje
        if (v.getConductor().getId() == p.getId()){
'''
c = c.replace(old_acciones, new_acciones)
write(p, c)
print("2) BuscarViajesConsulta.java OK")
