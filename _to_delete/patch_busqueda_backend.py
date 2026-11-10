import io

def read(p):
    with io.open(p, "r", encoding="utf-8") as f:
        return f.read()

def write(p, c):
    with io.open(p, "w", encoding="utf-8") as f:
        f.write(c)

base = "src/main/java/elementary/Rutix/"

# 1) BuscarViajesRequestConsultaDto.java: fechaSalida -> fechaDesde/fechaHasta
p = base + "Viajes/dto/BuscarViajesRequestConsultaDto.java"
c = read(p)
old = """public class BuscarViajesRequestConsultaDto {
    private LocalDate fechaSalida;
    private String ciudadPartida;
"""
assert c.count(old) == 1, ("dto class body", c.count(old))
new = """public class BuscarViajesRequestConsultaDto {
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;
    private String ciudadPartida;
"""
write(p, c.replace(old, new))
print("1) BuscarViajesRequestConsultaDto.java OK")

# 2) ViajeRepository.java: range query
p = base + "Viajes/repositorios/ViajeRepository.java"
c = read(p)
old = '''    @Query("""
    SELECT m FROM Viaje m
    WHERE (
        (:fechaSalida IS NULL AND m.fechaSalida >= CURRENT_DATE)
        OR m.fechaSalida = :fechaSalida
    )
    AND ((:ciudadPartida IS NULL or :ciudadPartida = '')OR m.ciudadPartida = :ciudadPartida )
    AND ((:ciudadDestino IS NULL or :ciudadDestino = '') OR m.ciudadDestino = :ciudadDestino)
    AND (:estado IS NULL OR m.estado = :estado)
""")
    Page<Viaje> buscarViajes(
            @Param("fechaSalida") LocalDate fechaSalida,
            @Param("ciudadPartida") String ciudadPartida,
            @Param("ciudadDestino") String ciudadDestino,
            @Param("estado") EstadoViajeEnum estado,
            Pageable pageable);
'''
assert c.count(old) == 1, ("ViajeRepository query", c.count(old))
new = '''    @Query("""
    SELECT m FROM Viaje m
    WHERE (
        (:fechaDesde IS NULL AND :fechaHasta IS NULL AND m.fechaSalida >= CURRENT_DATE)
        OR (:fechaDesde IS NOT NULL AND :fechaHasta IS NOT NULL AND m.fechaSalida BETWEEN :fechaDesde AND :fechaHasta)
        OR (:fechaDesde IS NOT NULL AND :fechaHasta IS NULL AND m.fechaSalida >= :fechaDesde)
        OR (:fechaDesde IS NULL AND :fechaHasta IS NOT NULL AND m.fechaSalida <= :fechaHasta)
    )
    AND ((:ciudadPartida IS NULL or :ciudadPartida = '')OR m.ciudadPartida = :ciudadPartida )
    AND ((:ciudadDestino IS NULL or :ciudadDestino = '') OR m.ciudadDestino = :ciudadDestino)
    AND (:estado IS NULL OR m.estado = :estado)
""")
    Page<Viaje> buscarViajes(
            @Param("fechaDesde") LocalDate fechaDesde,
            @Param("fechaHasta") LocalDate fechaHasta,
            @Param("ciudadPartida") String ciudadPartida,
            @Param("ciudadDestino") String ciudadDestino,
            @Param("estado") EstadoViajeEnum estado,
            Pageable pageable);
'''
write(p, c.replace(old, new))
print("2) ViajeRepository.java OK")

# 3) BuscarViajesConsulta.java: validate range + pass new params
p = base + "Viajes/consultas/BuscarViajesConsulta.java"
c = read(p)

old_import = "import java.util.ArrayList;\n"
assert c.count(old_import) == 1, ("consulta import", c.count(old_import))
c = c.replace(old_import, old_import + "import java.time.temporal.ChronoUnit;\n")

old_execute_head = '''    @Override
    public PageResponseDto<ViajeResumidoDto> execute(BuscarViajesRequestConsultaDto input) {
        Pageable pageable = PageRequest.of(
                input.getOffset().intValue(),   // page
                input.getLimit(),               // size
                Sort.by(Sort.Direction.DESC, "id")
        );

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long uid = Long.parseLong(auth.getName());
        this.perfilLogueado = repoPerfil.findByUsuarioId(uid).orElseThrow(() -> new ReglaNegocioException("No se encontró el perfil"));

        Page<Viaje> result = this.repo.buscarViajes(input.getFechaSalida(), input.getCiudadPartida(), input.getCiudadDestino(), input.getEstado(), pageable);
'''
assert c.count(old_execute_head) == 1, ("consulta execute head", c.count(old_execute_head))
new_execute_head = '''    private static final long RANGO_FECHAS_MAXIMO_DIAS = 31;

    @Override
    public PageResponseDto<ViajeResumidoDto> execute(BuscarViajesRequestConsultaDto input) {

        if (input.getFechaDesde() != null && input.getFechaHasta() != null) {
            if (input.getFechaHasta().isBefore(input.getFechaDesde()))
                throw new ReglaNegocioException("La fecha hasta no puede ser anterior a la fecha desde");

            long dias = ChronoUnit.DAYS.between(input.getFechaDesde(), input.getFechaHasta());
            if (dias > RANGO_FECHAS_MAXIMO_DIAS)
                throw new ReglaNegocioException("El rango entre fecha desde y fecha hasta no puede superar los " + RANGO_FECHAS_MAXIMO_DIAS + " dias");
        }

        Pageable pageable = PageRequest.of(
                input.getOffset().intValue(),   // page
                input.getLimit(),               // size
                Sort.by(Sort.Direction.DESC, "id")
        );

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long uid = Long.parseLong(auth.getName());
        this.perfilLogueado = repoPerfil.findByUsuarioId(uid).orElseThrow(() -> new ReglaNegocioException("No se encontró el perfil"));

        Page<Viaje> result = this.repo.buscarViajes(input.getFechaDesde(), input.getFechaHasta(), input.getCiudadPartida(), input.getCiudadDestino(), input.getEstado(), pageable);
'''
c = c.replace(old_execute_head, new_execute_head)
write(p, c)
print("3) BuscarViajesConsulta.java OK")
