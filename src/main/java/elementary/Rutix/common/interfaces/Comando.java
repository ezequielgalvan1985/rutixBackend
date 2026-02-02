package elementary.Rutix.common.interfaces;

public interface Comando<I,O>{
    O execute(I value);
}
