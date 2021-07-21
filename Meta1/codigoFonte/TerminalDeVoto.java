
import java.io.Serializable;

public class TerminalDeVoto implements Serializable {

    private final int id;
    private boolean active;

    public TerminalDeVoto(int id) {
        this.id=id;
        this.active=true;
    }

    public int getId() {
        return id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
