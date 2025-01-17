package Xadrez;

import TabuleiroDoJogo.Peca;
import TabuleiroDoJogo.Posicao;
import TabuleiroDoJogo.Tabuleiro;
import Xadrez.pecas.*;

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
        PecaDeXadrez p = (PecaDeXadrez) tabuleiro.removerPeca(origem);
        p.incrementarQuantidadeDeMovimentos();
        Peca pecaCapturada = tabuleiro.removerPeca(destino);
        tabuleiro.colocarPeca(p, destino);

        if (pecaCapturada != null) {
            pecasNoTabuleiro.remove(pecaCapturada);
            pecasCapturadas.add(pecaCapturada);
        }

        if (p instanceof Rei && destino.getColuna() == origem.getColuna() + 2) {
            Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() + 3);
            Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() + 1);
            PecaDeXadrez torre = (PecaDeXadrez) tabuleiro.removerPeca(origemT);
            tabuleiro.colocarPeca(torre, destinoT);
            torre.incrementarQuantidadeDeMovimentos();
        }

        if (p instanceof Rei && destino.getColuna() == origem.getColuna() - 2) {
            Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() - 4);
            Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() - 1);
            PecaDeXadrez torre = (PecaDeXadrez) tabuleiro.removerPeca(origemT);
            tabuleiro.colocarPeca(torre, destinoT);
            torre.incrementarQuantidadeDeMovimentos();
        }

        return pecaCapturada;
    }

    private void desvazerMovimento(Posicao origem, Posicao destino, Peca pecaCapturada) {
        PecaDeXadrez p = (PecaDeXadrez) tabuleiro.removerPeca(destino);
        p.decrementarQuantidadeDeMovimentos();
        tabuleiro.colocarPeca(p, origem);

        if (pecaCapturada != null) {
            tabuleiro.colocarPeca(pecaCapturada, destino);
            pecasCapturadas.remove(pecaCapturada);
            pecasNoTabuleiro.add(pecaCapturada);
        }

        if (p instanceof Rei && destino.getColuna() == origem.getColuna() + 2) {
            Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() + 3);
            Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() + 1);
            PecaDeXadrez torre = (PecaDeXadrez) tabuleiro.removerPeca(destinoT);
            tabuleiro.colocarPeca(torre, origemT);
            torre.decrementarQuantidadeDeMovimentos();
        }

        if (p instanceof Rei && destino.getColuna() == origem.getColuna() - 2) {
            Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() - 4);
            Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() - 1);
            PecaDeXadrez torre = (PecaDeXadrez) tabuleiro.removerPeca(destinoT);
            tabuleiro.colocarPeca(torre, origemT);
            torre.decrementarQuantidadeDeMovimentos();
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
        colocarNovaPeca('a', 1, new Torre(tabuleiro, Cor.BRANCAS));
        colocarNovaPeca('b', 1, new Cavalo(tabuleiro, Cor.BRANCAS));
        colocarNovaPeca('c', 1, new Bispo(tabuleiro, Cor.BRANCAS));
        colocarNovaPeca('d', 1, new Rainha(tabuleiro, Cor.BRANCAS));
        colocarNovaPeca('e', 1, new Rei(tabuleiro, Cor.BRANCAS, this));
        colocarNovaPeca('f', 1, new Bispo(tabuleiro, Cor.BRANCAS));
        colocarNovaPeca('g', 1, new Cavalo(tabuleiro, Cor.BRANCAS));
        colocarNovaPeca('h', 1, new Torre(tabuleiro, Cor.BRANCAS));
        colocarNovaPeca('a', 2, new Peao(tabuleiro, Cor.BRANCAS));
        colocarNovaPeca('b', 2, new Peao(tabuleiro, Cor.BRANCAS));
        colocarNovaPeca('c', 2, new Peao(tabuleiro, Cor.BRANCAS));
        colocarNovaPeca('d', 2, new Peao(tabuleiro, Cor.BRANCAS));
        colocarNovaPeca('e', 2, new Peao(tabuleiro, Cor.BRANCAS));
        colocarNovaPeca('f', 2, new Peao(tabuleiro, Cor.BRANCAS));
        colocarNovaPeca('g', 2, new Peao(tabuleiro, Cor.BRANCAS));
        colocarNovaPeca('h', 2, new Peao(tabuleiro, Cor.BRANCAS));

        colocarNovaPeca('a', 8, new Torre(tabuleiro, Cor.PRETAS));
        colocarNovaPeca('b', 8, new Cavalo(tabuleiro, Cor.PRETAS));
        colocarNovaPeca('c', 8, new Bispo(tabuleiro, Cor.PRETAS));
        colocarNovaPeca('d', 8, new Rainha(tabuleiro, Cor.PRETAS));
        colocarNovaPeca('e', 8, new Rei(tabuleiro, Cor.PRETAS, this));
        colocarNovaPeca('f', 8, new Bispo(tabuleiro, Cor.PRETAS));
        colocarNovaPeca('g', 8, new Cavalo(tabuleiro, Cor.PRETAS));
        colocarNovaPeca('h', 8, new Torre(tabuleiro, Cor.PRETAS));
        colocarNovaPeca('a', 7, new Peao(tabuleiro, Cor.PRETAS));
        colocarNovaPeca('b', 7, new Peao(tabuleiro, Cor.PRETAS));
        colocarNovaPeca('c', 7, new Peao(tabuleiro, Cor.PRETAS));
        colocarNovaPeca('d', 7, new Peao(tabuleiro, Cor.PRETAS));
        colocarNovaPeca('e', 7, new Peao(tabuleiro, Cor.PRETAS));
        colocarNovaPeca('f', 7, new Peao(tabuleiro, Cor.PRETAS));
        colocarNovaPeca('g', 7, new Peao(tabuleiro, Cor.PRETAS));
        colocarNovaPeca('h', 7, new Peao(tabuleiro, Cor.PRETAS));

    }
}
