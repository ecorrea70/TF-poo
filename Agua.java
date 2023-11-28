import java.awt.*;

public class Agua  implements ElementoMapa{
    private Color cor;
    private Character simbolo;

    public Agua(Character simbolo, Color cor) {
        this.simbolo = simbolo;
        this.cor = cor;
    }

    @Override
    public Character getSimbolo() {
        return simbolo;
    }

    @Override
    public Color getCor() {
        return cor;
    }

    @Override
    public boolean podeSerAtravessado() {
        return true;
    }

    @Override
    public boolean podeInteragir() {
        return true;
    }

    @Override
    public String interage() {
        return "glub glub glub";
    }
}
