package Aplicativo;

import Xadrez.ExcecaoXadrez;
import Xadrez.PartidaDeXadrez;
import Xadrez.PecaDeXadrez;
import Xadrez.PosicaoXadrez;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Programa {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        PartidaDeXadrez partidaDeXadrez = new PartidaDeXadrez();
        List<PecaDeXadrez> capturadas = new ArrayList<>();

        while (!partidaDeXadrez.getXequeMate()) {
            try {
                UI.limparTela();
                UI.printPartida(partidaDeXadrez, capturadas);
                System.out.println();
                System.out.print("Origem: ");
                PosicaoXadrez origem = UI.lerPosicaoXadrez(sc);

                boolean [][] possiveisMovimentos = partidaDeXadrez.possiveisMovimentos(origem);
                UI.limparTela();
                UI.printTabuleiro(partidaDeXadrez.getPecas(), possiveisMovimentos);
                System.out.println();
                System.out.print("Destino: ");
                PosicaoXadrez destino = UI.lerPosicaoXadrez(sc);

                PecaDeXadrez pecaCapturada = partidaDeXadrez.executarMovimentoXadrez(origem, destino);

                if (pecaCapturada != null) {
                    capturadas.add(pecaCapturada);
                }
            }
            catch (ExcecaoXadrez e) {
                System.out.println(e.getMessage());
                sc.nextLine();
            }
            catch (InputMismatchException e) {
                System.out.println(e.getMessage());
                sc.nextLine();
            }
        }
        UI.limparTela();
        UI.printPartida(partidaDeXadrez, capturadas);
    }
}