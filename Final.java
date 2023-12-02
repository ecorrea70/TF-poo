import java.awt.Color;

public class Final implements ElementoMapa {
    private Color cor;
    private Character simbolo;


    public Final(Character simbolo, Color cor) {
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
        return false;
    }

    @Override
    public boolean podeInteragir() {
        return true;
    }

    @Override
    public String interage() {
        return "Parabéns! Você encontrou o objeto misterioso!";
    }

}
