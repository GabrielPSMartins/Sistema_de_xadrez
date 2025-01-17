package Xadrez;

import TabuleiroDoJogo.Peca;
import TabuleiroDoJogo.Posicao;
import TabuleiroDoJogo.Tabuleiro;
import Xadrez.pecas.Rei;
import Xadrez.pecas.Torre;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PartidaDeXadrez {


    private int turno;
    private Cor jogadorAtual;
    private Tabuleiro tabuleiro;
    private boolean xeque;
    private boolean xequeMate;

    private List<Peca> pecasNoTabuleiro = new ArrayList<>();
    private List<Peca> pecasCapturadas = new ArrayList<>();

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

    public boolean getXeque() {
        return xeque;
    }

    public boolean getXequeMate() {
        return xequeMate;
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

        if (testeXeque(jogadorAtual)) {
            desvazerMovimento(origem, destino, pecaCapturada);
            throw new ExcecaoXadrez("Você não pode se colocar em xeque.");
        }

        xeque = (testeXeque(oponente(jogadorAtual))) ? true : false;

        if (testeXequeMate(oponente(jogadorAtual))) {
            xequeMate = true;
        }
        else {
            proximoTurno();
        }
        return (PecaDeXadrez) pecaCapturada;
    }

    private Peca realizarMovimento(Posicao origem, Posicao destino) {
        Peca p = tabuleiro.removerPeca(origem);
        Peca pecaCapturada = tabuleiro.removerPeca(destino);
        tabuleiro.colocarPeca(p, destino);

        if (pecaCapturada != null) {
            pecasNoTabuleiro.remove(pecaCapturada);
            pecasCapturadas.add(pecaCapturada);
        }

        return pecaCapturada;
    }

    private void desvazerMovimento(Posicao origem, Posicao destino, Peca pecaCapturada) {
        Peca p = tabuleiro.removerPeca(destino);
        tabuleiro.colocarPeca(p, origem);

        if (pecaCapturada != null) {
            tabuleiro.colocarPeca(pecaCapturada, destino);
            pecasCapturadas.remove(pecaCapturada);
            pecasNoTabuleiro.add(pecaCapturada);
        }
    }

    private void validarPosicaoOrigem(Posicao posicao) {
        if(!tabuleiro.pecaExiste(posicao)) {
            throw new ExcecaoXadrez("Não existe peça na posição de origem.");
        }
        if (jogadorAtual != ((PecaDeXadrez)tabuleiro.peca(posicao)).getCor()) {
            throw new ExcecaoXadrez("A peça escolhida não é sua.");
        }
        if (!tabuleiro.peca(posicao).existeAlgumMovimentoPossivel()) {
            throw new ExcecaoXadrez("Não existe movimentos possíveis para a peça escolhida.");
        }
    }

    private void validarPosicaoDestino(Posicao origem, Posicao destino) {
        if (!tabuleiro.peca(origem).possivelMovimento(destino)) {
            throw new ExcecaoXadrez("A peça escolhida não pode se mover para a posição de destino.");
        }
    }

    private void proximoTurno() {
        turno++;
        jogadorAtual = (jogadorAtual == Cor.BRANCAS) ? Cor.PRETAS : Cor.BRANCAS;
    }

    private Cor oponente(Cor cor) {
        return (cor == Cor.BRANCAS) ? Cor.PRETAS : Cor.BRANCAS;
    }

    private PecaDeXadrez rei(Cor cor) {
        List<Peca> list = pecasNoTabuleiro.stream().filter(x -> ((PecaDeXadrez)x).getCor() == cor).collect(Collectors.toList());
        for (Peca p : list) {
            if (p instanceof Rei) {
                return (PecaDeXadrez) p;
            }
        }
        throw new IllegalStateException("Não existe o rei da cor " + cor + " no tabuleiro.");
    }

    private boolean testeXeque(Cor cor) {
        Posicao posicaoRei = rei(cor).getPosicaoXadrez().toPosicao();
        List<Peca> pecasOponente = pecasNoTabuleiro.stream().filter(x -> ((PecaDeXadrez)x).getCor() == oponente(cor)).collect(Collectors.toList());
        for (Peca p : pecasOponente) {
            boolean [][] mat = p.possiveisMovimentos();
            if (mat[posicaoRei.getLinha()][posicaoRei.getColuna()]) {
                return true;
            }
        }
        return false;
    }

    private boolean testeXequeMate (Cor cor) {
        if (!testeXeque(cor)) {
            return false;
        }
        List<Peca> list = pecasNoTabuleiro.stream().filter(x -> ((PecaDeXadrez)x).getCor() == cor).collect(Collectors.toList());
        for (Peca p : list) {
            boolean [][] mat = p.possiveisMovimentos();
            for (int i=0; i< tabuleiro.getLinhas(); i++) {
                for (int j=0; j< tabuleiro.getColunas(); j++) {
                    if(mat[i][j]) {
                        Posicao origem = ((PecaDeXadrez)p).getPosicaoXadrez().toPosicao();
                        Posicao destino = new Posicao(i, j);
                        Peca pecaCapturada = realizarMovimento(origem, destino);
                        boolean testeXeque = testeXeque(cor);
                        desvazerMovimento(origem, destino, pecaCapturada);
                        if (!testeXeque) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private void colocarNovaPeca(char coluna, int linha, PecaDeXadrez peca) {
        tabuleiro.colocarPeca(peca, new PosicaoXadrez(coluna, linha).toPosicao());
        pecasNoTabuleiro.add(peca);
    }

    private void setupInicial() {
        colocarNovaPeca('h', 7, new Torre(tabuleiro, Cor.BRANCAS));
        colocarNovaPeca('d', 1, new Torre(tabuleiro, Cor.BRANCAS));
        colocarNovaPeca('e', 1, new Rei(tabuleiro, Cor.BRANCAS));

        colocarNovaPeca('b', 8, new Torre(tabuleiro, Cor.PRETAS));
        colocarNovaPeca('a', 8, new Rei(tabuleiro, Cor.PRETAS));

    }
}
