package elementary.Rutix.Viajes.IntegracionPagoExterno;

import elementary.Rutix.Viajes.dto.PagoDto;
import elementary.Rutix.common.Enum.MetodoPagoEnum;
import org.springframework.stereotype.Component;

@Component
public class EfectivoCobroStrategy implements GeneradorCobroStrategy{
    @Override
    public MetodoPagoEnum getMetodoPago() {
        return null;
    }

    @Override
    public PagoDto generar(PagoDto pagoDto) {
        return null;
    }
}
