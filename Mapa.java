import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Mapa {
    private List<String> mapa;
    private Map<Character, ElementoMapa> elementos;
    private int x = 50; // Posição inicial X do personagem
    private int y = 50; // Posição inicial Y do personagem
    private final int TAMANHO_CELULA = 10; // Tamanho de cada célula do mapa
    private boolean[][] areaRevelada; // Rastreia quais partes do mapa foram reveladas
    private final Color brickColor = new Color(153, 76, 0); // Cor marrom para tijolos
    private final Color vegetationColor = new Color(34, 139, 34); // Cor verde para vegetação
    private final Color yeColor = new Color(73, 73, 73); // Cor cinza para kanye west
    private final Color waterColor = new Color(20, 191, 213); // Cor azul para agua
    private final int RAIO_VISAO = 5; // Raio de visão do personagem


    public Mapa(String arquivoMapa) {
        mapa = new ArrayList<>();
        elementos = new HashMap<>();
        registraElementos();
        carregaMapa(arquivoMapa);
        areaRevelada = new boolean[mapa.size()+1000][mapa.get(0).length()+1000];
        atualizaCelulasReveladas();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getTamanhoCelula() {
        return TAMANHO_CELULA;
    }

    public int getNumLinhas() {
        return mapa.size();
    }

    public int getNumColunas() {
        return mapa.get(0).length();
    }

    public ElementoMapa getElemento(int x, int y) {
        Character id = mapa.get(y).charAt(x);
        return elementos.get(id);
    }

    public boolean estaRevelado(int x, int y) {
        return areaRevelada[y][x];
    }

    // Move conforme enum Direcao
    public boolean move(Direcao direcao) {
        int dx = 0, dy = 0;

        switch (direcao) {
            case CIMA:
                dy = -TAMANHO_CELULA;
                break;
            case BAIXO:
                dy = TAMANHO_CELULA;
                break;
            case ESQUERDA:
                dx = -TAMANHO_CELULA;
                break;
            case DIREITA:
                dx = TAMANHO_CELULA;
                break;
            default:
                return false;
        }

        if (!podeMover(x + dx, y + dy)) {
            System.out.println("Não pode mover");
            return false;
        }

        x += dx;
        y += dy;

        // Atualiza as células reveladas
        atualizaCelulasReveladas();
        return true;
    }

    // Verifica se o personagem pode se mover para a próxima posição
    private boolean podeMover(int nextX, int nextY) {
        int mapX = nextX / TAMANHO_CELULA;
        int mapY = nextY / TAMANHO_CELULA - 1;

        if (mapa == null)
            return false;

        if (mapX >= 0 && mapX < mapa.get(0).length() && mapY >= 1 && mapY <= mapa.size()) {
            char id;

            try {
               id = mapa.get(mapY).charAt(mapX);
            } catch (StringIndexOutOfBoundsException e) {
                return false;
            }

            if (id == ' ')
                return true;

            ElementoMapa elemento = elementos.get(id);
            if (elemento != null) {
                //System.out.println("Elemento: " + elemento.getSimbolo() + " " + elemento.getCor());
                return elemento.podeSerAtravessado();
            }
        }

        return false;
    }


    public String interage() {
        ElementoMapa elementoMaisProximo = null;
        double menorDistancia = Double.MAX_VALUE;

        // Verifica elementos nas células adjacentes
        for (int i = Math.max(0, y / TAMANHO_CELULA - 2); i <= Math.min(mapa.size() - 1, y / TAMANHO_CELULA + 2); i++) {
            for (int j = Math.max(0, x / TAMANHO_CELULA - 2); j <= Math.min(mapa.get(i).length() - 1, x / TAMANHO_CELULA + 2); j++) {
                // Ignora a posição do próprio personagem


                char id = mapa.get(i).charAt(j);
                ElementoMapa elemento = elementos.get(id);

                // Verifica se há um elemento no local e se ele pode interagir
                if (elemento != null && elemento.podeInteragir()) {
                    // Calcula a distância usando o teorema de Pitágoras
                    double distancia = Math.sqrt(Math.pow(i - y / TAMANHO_CELULA, 2) + Math.pow(j - x / TAMANHO_CELULA, 2));

                    // Verifica se a distância é menor do que a menor distância registrada até agora
                    if (distancia < menorDistancia) {
                        menorDistancia = distancia;
                        elementoMaisProximo = elemento;
                    } else if (distancia == menorDistancia) {
                        // Se a distância é a mesma, fazemos o desempate
                        Direcao direcaoAtual = calcularDirecao(y / TAMANHO_CELULA, x / TAMANHO_CELULA, i, j);
                        Direcao direcaoAnterior = calcularDirecao(y / TAMANHO_CELULA, x / TAMANHO_CELULA,
                                elementoMaisProximo != null ? i : 0,
                                elementoMaisProximo != null ? j : 0);

                        // Se a direção atual tem uma prioridade maior, atualiza o elemento mais próximo
                        if (calcularPrioridadeVisual(direcaoAtual) > calcularPrioridadeVisual(direcaoAnterior)) {
                            elementoMaisProximo = elemento;
                        }


                    }
                }
            }
        }

        // Se encontrou um elemento para interagir
        if (elementoMaisProximo != null) {
            System.out.println("Interagindo com elemento: " + elementoMaisProximo.getClass().getSimpleName());
            return elementoMaisProximo.interage();
        } else {
            // Se não houver nenhum elemento para interagir nas células adjacentes
            System.out.println("Nada para interagir");
            return "Nada para interagir";
        }
    }



    private Direcao calcularDirecao(int personagemY, int personagemX, int elementoY, int elementoX) {
        int diferencaY = elementoY - personagemY;
        int diferencaX = elementoX - personagemX;

        if (diferencaY < 0) {
            if (diferencaX < 0) {
                return Direcao.SUPERIOR_ESQUERDA;
            } else if (diferencaX > 0) {
                return Direcao.SUPERIOR_DIREITA;
            } else {
                return Direcao.CIMA;
            }
        } else if (diferencaY > 0) {
            if (diferencaX < 0) {
                return Direcao.INFERIOR_ESQUERDA;
            } else if (diferencaX > 0) {
                return Direcao.INFERIOR_DIREITA;
            } else {
                return Direcao.BAIXO;
            }
        } else {
            if (diferencaX < 0) {
                return Direcao.ESQUERDA;
            } else {
                return Direcao.DIREITA;
            }
        }
    }

    private int calcularPrioridadeVisual(Direcao direcao) {
        switch (direcao) {
            case DIREITA:
                return 0;
            case INFERIOR_DIREITA:
                return 1;
            case BAIXO:
                return 2;
            case INFERIOR_ESQUERDA:
                return 3;
            case ESQUERDA:
                return 4;
            case SUPERIOR_ESQUERDA:
                return 5;
            case CIMA:
                return 6;
            case SUPERIOR_DIREITA:
                return 7;
            default:
                return 8; // Prioridade padrão para casos não esperados
        }
    }



    public String ataca() {
        //TODO: Implementar
        return "Ataca";
    }

    private void carregaMapa(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                mapa.add(line);
                // Se character 'P' está contido na linha atual, então define a posição inicial do personagem
                if (line.contains("P")) {
                    x = line.indexOf('P') * TAMANHO_CELULA;
                    y = mapa.size() * TAMANHO_CELULA;
                    // Remove o personagem da linha para evitar que seja desenhado
                    mapa.set(mapa.size() - 1, line.replace('P', ' '));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para atualizar as células reveladas
    private void atualizaCelulasReveladas() {
        if (mapa == null)
            return;
        for (int i = Math.max(0, y / TAMANHO_CELULA - RAIO_VISAO); i < Math.min(mapa.size(), y / TAMANHO_CELULA + RAIO_VISAO + 1); i++) {
            for (int j = Math.max(0, x / TAMANHO_CELULA - RAIO_VISAO); j < Math.min(mapa.get(i).length(), x / TAMANHO_CELULA + RAIO_VISAO + 1); j++) {
                areaRevelada[i][j] = true;
            }
        }
    }

    // Registra os elementos do mapa
    private void registraElementos() {
        // Parede
        elementos.put('#', new Parede('▣', brickColor));
        // Vegetação
        elementos.put('V', new Vegetacao('♣', vegetationColor));
        // Ye
        elementos.put('Y', new Ye('¥', yeColor));
        // Agua
        elementos.put('A', new Agua('▨', waterColor));
    }
}
