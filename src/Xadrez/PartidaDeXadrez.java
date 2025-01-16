package Xadrez;

import TabuleiroDoJogo.ExcecaoTabuleiro;
import TabuleiroDoJogo.Peca;
import TabuleiroDoJogo.Posicao;
import TabuleiroDoJogo.Tabuleiro;
import Xadrez.pecas.Rei;
import Xadrez.pecas.Torre;

public class PartidaDeXadrez {


    private int turno;
    private Cor jogadorAtual;
    private Tabuleiro tabuleiro;

    public PartidaDeXadrez() {
        tabuleiro = new Tabuleiro(8, 8);
        turno = 1;
        jogadorAtual = Cor.BRANCAS;
        setupInicial();
    }

    public int getTurno() {
        return turno;
    }

    public Cor getJogadorAtual() {
        return jogadorAtual;
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

    public boolean[][] possiveisMovimentos(PosicaoXadrez posicaoOrigem) {
        Posicao posicao = posicaoOrigem.toPosicao();
        validarPosicaoOrigem(posicao);
        return tabuleiro.peca(posicao).possiveisMovimentos();
    }

    public PecaDeXadrez executarMovimentoXadrez(PosicaoXadrez posicaoOrigem, PosicaoXadrez posicaoDestino) {
        Posicao origem = posicaoOrigem.toPosicao();
        Posicao destino = posicaoDestino.toPosicao();
        validarPosicaoOrigem(origem);
        validarPosicaoDestino(origem, destino);
        Peca pecaCapturada = realizarMovimento(origem, destino);
        proximoTurno();
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
        if (jogadorAtual != ((PecaDeXadrez)tabuleiro.peca(posicao)).getCor()) {
            throw new ExcecaoXadrez("A peça escolhida não é sua");
        }
        if (!tabuleiro.peca(posicao).existeAlgumMovimentoPossivel()) {
            throw new ExcecaoXadrez("Não existe movimentos possíveis para a peça escolhida");
        }
    }

    private void validarPosicaoDestino(Posicao origem, Posicao destino) {
        if (!tabuleiro.peca(origem).possivelMovimento(destino)) {
            throw new ExcecaoXadrez("A peça escolhida não pode se mover para a posição de destino");
        }
    }

    private void proximoTurno() {
        turno++;
        jogadorAtual = (jogadorAtual == Cor.BRANCAS) ? Cor.PRETAS : Cor.BRANCAS;
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
