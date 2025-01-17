package Xadrez;

import TabuleiroDoJogo.Peca;
import TabuleiroDoJogo.Posicao;
import TabuleiroDoJogo.Tabuleiro;

public abstract class PecaDeXadrez extends Peca {

    private Cor cor;
    private int quantidadeDeMovimentos;

    public PecaDeXadrez(Tabuleiro tabuleiro, Cor cor) {
        super(tabuleiro);
        this.cor = cor;
    }

    public Cor getCor() {
        return cor;
    }

    public int getQuantidadeDeMovimentos() {
        return quantidadeDeMovimentos;
    }

    public void incrementarQuantidadeDeMovimentos () {
        quantidadeDeMovimentos++;
    }

    public void decrementarQuantidadeDeMovimentos () {
        quantidadeDeMovimentos--;
    }

    public PosicaoXadrez getPosicaoXadrez() {
        return PosicaoXadrez.dePosicao(posicao);
    }

    protected boolean exisePecaOponente(Posicao posicao) {
        PecaDeXadrez p = (PecaDeXadrez) getTabuleiro().peca(posicao);
        return p != null && p.getCor() != cor;
    }
}
