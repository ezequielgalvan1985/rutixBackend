package elementary.Rutix.Viajes.IntegracionPagoExterno;

import elementary.Rutix.common.Enum.MetodoPagoEnum;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Component
public class GeneradorCobroFactory {

    private final Map<MetodoPagoEnum, GeneradorCobroStrategy> strategies;

    public GeneradorCobroFactory(
            List<GeneradorCobroStrategy> strategies) {

        this.strategies = strategies.stream()
                .collect(Collectors.toMap(
                        GeneradorCobroStrategy::getMetodoPago,
                        strategy -> strategy
                ));
    }

    public GeneradorCobroStrategy obtener(MetodoPagoEnum tipo) {
        return strategies.get(tipo);
    }
}