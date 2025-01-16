package Xadrez;

import TabuleiroDoJogo.ExcecaoTabuleiro;
import TabuleiroDoJogo.Peca;
import TabuleiroDoJogo.Posicao;
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

    public PecaDeXadrez executarMovimentoXadrez(PosicaoXadrez posicaoOrigem, PosicaoXadrez posicaoDestino) {
        Posicao origem = posicaoOrigem.toPosicao();
        Posicao destino = posicaoDestino.toPosicao();
        validarPosicaoOrigem(origem);
        Peca pecaCapturada = realizarMovimento(origem, destino);
        return (PecaDeXadrez) pecaCapturada;
    }

    private Peca realizarMovimento(Posicao origem, Posicao destino) {
        Peca p = tabuleiro.removerPeca(origem);
        Peca pecaCapturada = tabuleiro.removerPeca(destino);
        tabuleiro.colocarPeca(p, destino);
        return pecaCapturada;
    }

    private void validarPosicaoOrigem(Posicao posicao) {
        if(!tabuleiro.pecaExiste(posicao)) {
            throw new ExcecaoXadrez("Não existe peça na posição de origem");
        }
    }

    private void colocarNovaPeca(char coluna, int linha, PecaDeXadrez peca) {
        tabuleiro.colocarPeca(peca, new PosicaoXadrez(coluna, linha).toPosicao());
    }

    private void setupInicial() {
        colocarNovaPeca('c', 1, new Torre(tabuleiro, Cor.BRANCAS));
        colocarNovaPeca('c', 2, new Torre(tabuleiro, Cor.BRANCAS));
        colocarNovaPeca('d', 2, new Torre(tabuleiro, Cor.BRANCAS));
        colocarNovaPeca('e', 2, new Torre(tabuleiro, Cor.BRANCAS));
        colocarNovaPeca('e', 1, new Torre(tabuleiro, Cor.BRANCAS));
        colocarNovaPeca('d', 1, new Rei(tabuleiro, Cor.BRANCAS));

        colocarNovaPeca('c', 7, new Torre(tabuleiro, Cor.PRETAS));
        colocarNovaPeca('c', 8, new Torre(tabuleiro, Cor.PRETAS));
        colocarNovaPeca('d', 7, new Torre(tabuleiro, Cor.PRETAS));
        colocarNovaPeca('e', 7, new Torre(tabuleiro, Cor.PRETAS));
        colocarNovaPeca('e', 8, new Torre(tabuleiro, Cor.PRETAS));
        colocarNovaPeca('d', 8, new Rei(tabuleiro, Cor.PRETAS));
    }
}
