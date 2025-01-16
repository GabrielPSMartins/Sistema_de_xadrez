package Xadrez;

import TabuleiroDoJogo.Tabuleiro;
import Xadrez.pecas.Rei;
import Xadrez.pecas.Torre;

public class PartidaDeXadrez {

    private Tabuleiro tabuleiro;

    public PartidaDeXadrez() {
        tabuleiro = new Tabuleiro(8, 8);
        setupInicial();
    }

    public PecaDeXadrez[][] getPecas() {
        PecaDeXadrez[][] mat = new PecaDeXadrez[tabuleiro.getLinhas()][tabuleiro.getColunas()];
        for (int i=0; i< tabuleiro.getLinhas(); i++) {
            for (int j=0; j< tabuleiro.getColunas(); j++) {
                mat[i][j] = (PecaDeXadrez) tabuleiro.peca(i, j);
            }
        }
        return mat;
    }

    private void colocarNovaPeca(char coluna, int linha, PecaDeXadrez peca) {
        tabuleiro.colocarPeca(peca, new PosicaoXadrez(coluna, linha).toPosicao());
    }

    private void setupInicial() {
        colocarNovaPeca('b', 6, new Torre(tabuleiro, Cor.BRANCAS));
        colocarNovaPeca('e', 8, new Rei(tabuleiro, Cor.PRETAS));
        colocarNovaPeca('e', 1, new Rei(tabuleiro, Cor.BRANCAS));
    }
}
