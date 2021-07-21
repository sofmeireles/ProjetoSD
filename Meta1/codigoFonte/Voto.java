
import java.io.Serializable;
import java.util.GregorianCalendar;

public class Voto implements Serializable {
    private final MesaDeVoto mesa;
    private final GregorianCalendar data;
    private final Eleicao eleicao;

    public Voto(MesaDeVoto mesa, GregorianCalendar data, Eleicao eleicao) {
        this.mesa = mesa;
        this.data = data;
        this.eleicao = eleicao;
    }

    public MesaDeVoto getMesa() {
        return mesa;
    }

    public GregorianCalendar getData() {
        return data;
    }

    public Eleicao getEleicao() {
        return eleicao;
    }
}
