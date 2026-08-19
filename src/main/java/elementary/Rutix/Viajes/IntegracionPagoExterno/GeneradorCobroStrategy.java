package elementary.Rutix.Viajes.IntegracionPagoExterno;

import elementary.Rutix.Viajes.dto.PagoDto;
import elementary.Rutix.common.Enum.MetodoPagoEnum;

public interface GeneradorCobroStrategy {

    MetodoPagoEnum getMetodoPago();
    PagoDto generar(PagoDto pagoDto);
}
