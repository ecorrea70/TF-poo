import java.awt.*;
import java.util.Random;

public class Ye  implements ElementoMapa{
    private Color cor;
    private Character simbolo;

    public Ye(Character simbolo, Color cor) {
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
        String[] frases={"Yeezy season approaching", "you'll never find nobody better than me", "some day, some day",
                          "boooouund to fall in love", "Modern day MJ with a off-the-wall flow", "encontre o elemento misterioso antes que seja tarde",
                          "in the night", "acho que o símbolo dele parece com isso: Ϡ", "tente procurar pela floresta"};
        Random random = new Random();

        int aleatorio = random.nextInt(frases.length);
        return frases[aleatorio];
    }
}
