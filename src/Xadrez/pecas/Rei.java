package Xadrez.pecas;

import TabuleiroDoJogo.Tabuleiro;
import Xadrez.Cor;
import Xadrez.PecaDeXadrez;

public class Rei extends PecaDeXadrez {

    public Rei(Tabuleiro tabuleiro, Cor cor) {
        super(tabuleiro, cor);
    }

    @Override
    public String toString() {
        return "R";
    }
}
