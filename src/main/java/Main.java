import java.io.IOException;
import java.util.Scanner;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.terminal.Attributes;

public class Main {

    static double saldoDigitado;
    static String inputDigitados;
    static int numeroMenu = 0;
    static boolean menuLigado = true;
    static ContaBancaria pessoaLogada;

    static String[] opcoes = {"Depositar","Sacar","Pix", "Trocar de conta","Sair"};

    static Scanner input = new Scanner(System.in);

    static Terminal terminal;
    static Attributes modoNormal;

    static class ContaBancaria  {
        String nome;
        String numeroConta;
        int senha;
        String chavePix;
        double saldo;
    }

    static ContaBancaria  contaMaillon = new ContaBancaria ();
    static ContaBancaria  contaAntony = new ContaBancaria ();

    static ContaBancaria [] listaContas = {contaMaillon, contaAntony};

    public static void main(String[] args) throws IOException {
        contas();

        login();

        terminal = TerminalBuilder.builder()
                .system(true)
                .build();

        modoNormal = terminal.enterRawMode();

        while (menuLigado) {
            menu();
        }
    }


    static void depositar() {
        System.out.println();
        System.out.print("Digite o valor do depósito (R$): ");
        saldoDigitado = input.nextDouble();
        System.out.println();

        if ( saldoDigitado > 0) {
            pessoaLogada.saldo += saldoDigitado;
            System.out.println("Depósito realizado com sucesso! Valor: R$ " + saldoDigitado);
            return;
        }
        System.out.println("Depósito não realizado. O valor deve ser maior que zero.");
        System.out.println("Retornando ao menu principal...");
    }

    static void sacar() {
        System.out.println();
        System.out.print("Digite o valor do saque (R$): ");
        saldoDigitado = input.nextDouble();
        System.out.println();

        if (saldoDigitado > 0 && saldoDigitado <= pessoaLogada.saldo ) {
            pessoaLogada.saldo -= saldoDigitado;
            System.out.println("Saque realizado com sucesso! Valor: R$ " + saldoDigitado);
            return;
        }
        System.out.println("Saque não realizado. O valor deve ser maior que zero e não pode ultrapassar o saldo disponível.");
        System.out.println("Retornando ao menu principal...");
    }

    static void pix() {
        System.out.println();
        System.out.print("Digite a chave Pix do destinatário: ");
        inputDigitados = input.next();




        if (inputDigitados.equals(pessoaLogada.chavePix)) {
            System.out.println();
            System.out.println("Não é possível enviar um Pix para a sua própria chave.");
            System.out.println("Retornando ao menu principal...");
            return;
        }

        for (ContaBancaria chaveAtual: listaContas) {

            if ( chaveAtual.chavePix.equals(inputDigitados.trim())) {

                System.out.println();
                System.out.print("Digite o valor do Pix (R$): ");
                saldoDigitado = input.nextDouble();
                System.out.println();

                if (saldoDigitado <= 0 ) {
                    System.out.println("Pix não realizado. O valor deve ser maior que zero.");
                    System.out.println("Retornando ao menu principal...");
                    return;
                }

                if (saldoDigitado > pessoaLogada.saldo ) {
                    System.out.println("Saldo insuficiente para realizar este Pix.");
                    System.out.println("Retornando ao menu principal...");
                    return;
                }

                pessoaLogada.saldo -= saldoDigitado;
                chaveAtual.saldo += saldoDigitado;
                System.out.println("Pix enviado com sucesso para " + chaveAtual.nome);
                System.out.println("Valor enviado: R$ " + saldoDigitado);
                return;
            }
        }
        System.out.println();
        System.out.println("Chave Pix não encontrada. Transferência cancelada.");
        System.out.println("Retornando ao menu principal...");
    }

    static void menu() throws IOException {


        System.out.println();
        System.out.println("=== Menu principal ===");
        System.out.println("Saldo disponível: R$ " + pessoaLogada.saldo);
        System.out.println("Use as setas para cima e para baixo para navegar. Pressione Enter para confirmar.");
        System.out.println();

        for (int i = 0; i < opcoes.length; i++) {

            if (i == numeroMenu) {
                terminal.writer().println("> " + opcoes[i]);
            } else {
                terminal.writer().println("  " + opcoes[i]);
            }

        }

        terminal.writer().flush();
        leitorDeTecla();


    }

    static void acoesMenu() {
        switch (numeroMenu) {
            case 0:
                depositar();
                break;

            case 1:
                sacar();
                break;

            case 2:
                pix();
                break;

            case 3:
                login();
                break;

            case 4:
                menuLigado = false;
                System.out.println();
                System.out.println("Sessão encerrada. Obrigado por usar o banco!");
                break;
        }
    }

    static void login() {
        int tentativas = 3;
        String numeroContaDigitado;
        int senhaDigitada;
        System.out.println();


        while (tentativas > 0) {

            System.out.print("Digite o número da sua conta: ");
            numeroContaDigitado = input.next();
            System.out.print("Digite a senha da sua conta: ");
            senhaDigitada = input.nextInt();

            for (ContaBancaria contaAtual : listaContas) {

                if (numeroContaDigitado.equals(contaAtual.numeroConta) && senhaDigitada == contaAtual.senha) {

                    System.out.println();
                    pessoaLogada = contaAtual;
                    System.out.println("Bem-vindo(a), " + pessoaLogada.nome);
                    return;

                }
            }

                    System.out.println();
                    System.out.println("Número da conta ou senha incorretos.");

                    tentativas--;

                    if (tentativas <= 0) {
                        System.out.println("Limite de tentativas atingido. Sessão encerrada.");
                        menuLigado = false;
                        return;
                    }
                    System.out.println("Tentativas de acesso restantes: " + tentativas);
                    System.out.println();





        }

    }

    static void leitorDeTecla() throws IOException {



        int tecla1 = terminal.reader().read();

        if (tecla1 == 27) {

            int tecla2 = terminal.reader().read();
            int tecla3 = terminal.reader().read();

            if (tecla2 == 'O' || tecla2 == '[') {

                if (tecla3 == 'A') {
                    numeroMenu--;

                    if (numeroMenu < 0) {
                        numeroMenu = 0;
                    }
                }

                if (tecla3 == 'B') {
                    numeroMenu++;

                    if (numeroMenu >= opcoes.length) {
                        numeroMenu = opcoes.length - 1;
                    }
                }
            }
        }

        if (tecla1 == '\r' || tecla1 == '\n') {
            terminal.setAttributes(modoNormal);
            acoesMenu();

            if ( menuLigado) {
                modoNormal = terminal.enterRawMode();
            }
        }

    }

    static void contas() {
        contaMaillon.nome = "Maillon";
        contaMaillon.numeroConta = "001";
        contaMaillon.senha = 123;
        contaMaillon.chavePix = "121212";
        contaMaillon.saldo = 0;

        contaAntony.nome = "Antony";
        contaAntony.numeroConta = "002";
        contaAntony.senha = 456;
        contaAntony.chavePix = "565656";
        contaAntony.saldo = 0;
    }
}


