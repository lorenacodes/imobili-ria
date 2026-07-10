package app;

import java.util.List;
import java.util.Scanner;

import excecoes.CpfDuplicadoException;
import excecoes.ValidacaoException;
import excecoes.ValorInvalidoException;
import modelo.Apartamento;
import modelo.Casa;
import modelo.Cliente;
import modelo.Endereco;
import modelo.Imovel;
import modelo.StatusImovel;
import modelo.Terreno;
import modelo.TipoTerreno;
import servico.ServicoImobiliaria;

/**
 * Classe de execucao do sistema: exibe o menu, le a opcao escolhida
 * pelo usuario (teclado) e repassa a acao para a ServicoImobiliaria,
 * que concentra as regras de negocio. O nome da classe segue o
 * enunciado da avaliacao.
 */
public class GererenciarAgenda {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final ServicoImobiliaria SERVICO = new ServicoImobiliaria();

    public static void main(String[] args) {
        int opcao;

        do {
            exibirMenu();
            opcao = lerInteiro("Digite a sua opcao: ");

            switch (opcao) {
                case 1:
                    cadastrarCliente();
                    break;
                case 2:
                    cadastrarImovel();
                    break;
                case 3:
                    listarImoveis();
                    break;
                case 4:
                    venderImovel();
                    break;
                case 5:
                    alugarImovel();
                    break;
                case 6:
                    buscarImovel();
                    break;
                case 7:
                    SERVICO.gerarRelatorios();
                    break;
                case 8:
                    System.out.println("\nEncerrando o sistema. Ate logo!");
                    break;
                default:
                    System.out.println("\nOpcao invalida. Escolha um numero de 1 a 8.");
            }

        } while (opcao != 8);

        SCANNER.close();
    }

    private static void exibirMenu() {
        System.out.println("\n===== SISTEMA DE GESTAO IMOBILIARIA =====");
        System.out.println("1 - Cadastrar Cliente");
        System.out.println("2 - Cadastrar Imovel");
        System.out.println("3 - Listar Imoveis");
        System.out.println("4 - Vender Imovel");
        System.out.println("5 - Alugar Imovel");
        System.out.println("6 - Buscar Imovel");
        System.out.println("7 - Relatorios");
        System.out.println("8 - Sair do Sistema");
    }

    // =================================================================
    // Opcao 1 - Cadastrar Cliente
    // =================================================================
    private static void cadastrarCliente() {
        System.out.println("\n--- Cadastro de Cliente ---");
        System.out.print("Nome: ");
        String nome = SCANNER.nextLine();
        System.out.print("CPF (somente numeros ou com pontuacao): ");
        String cpf = SCANNER.nextLine();
        System.out.print("Telefone: ");
        String telefone = SCANNER.nextLine();
        System.out.print("E-mail: ");
        String email = SCANNER.nextLine();

        Cliente cliente = new Cliente(nome, cpf, telefone, email);

        try {
            SERVICO.cadastrarCliente(cliente);
            System.out.println("Cliente cadastrado com sucesso!");
        } catch (CpfDuplicadoException | ValidacaoException e) {
            System.out.println("Erro ao cadastrar cliente: " + e.getMessage());
        }
    }

    // =================================================================
    // Opcao 2 - Cadastrar Imovel
    // =================================================================
    private static void cadastrarImovel() {
        System.out.println("\n--- Cadastro de Imovel ---");
        System.out.println("Tipo do imovel:");
        System.out.println("1 - Casa");
        System.out.println("2 - Apartamento");
        System.out.println("3 - Terreno");
        int tipo = lerInteiro("Opcao: ");

        Endereco endereco = lerEndereco();
        double valor = lerDouble("Valor do imovel: ");
        double area = lerDouble("Area (m2): ");

        // O codigo passado aqui e apenas um placeholder: quem gera o
        // codigo definitivo e a ServicoImobiliaria.cadastrarImovel().
        Imovel imovel = null;

        switch (tipo) {
            case 1: {
                int nrQuartos = lerInteiro("Numero de quartos: ");
                boolean garagem = lerBooleano("Possui garagem? (s/n): ");
                double iptu = lerDouble("Valor do IPTU: ");
                imovel = new Casa(0, endereco, valor, area, nrQuartos, garagem, iptu);
                break;
            }
            case 2: {
                int andar = lerInteiro("Andar: ");
                int numeroApt = lerInteiro("Numero do apartamento: ");
                double vlrCondominio = lerDouble("Valor do condominio: ");
                double iptu = lerDouble("Valor do IPTU: ");
                imovel = new Apartamento(0, endereco, valor, area, andar, numeroApt, vlrCondominio, iptu);
                break;
            }
            case 3: {
                TipoTerreno tipoTerreno = lerTipoTerreno();
                imovel = new Terreno(0, endereco, valor, area, tipoTerreno);
                break;
            }
            default:
                System.out.println("Tipo de imovel invalido.");
                return;
        }

        try {
            SERVICO.cadastrarImovel(imovel);
            System.out.println("Imovel cadastrado com sucesso! Codigo gerado: " + imovel.getCod());
        } catch (ValorInvalidoException e) {
            System.out.println("Erro ao cadastrar imovel: " + e.getMessage());
        }
    }

    private static Endereco lerEndereco() {
        System.out.print("Logradouro: ");
        String logradouro = SCANNER.nextLine();
        int numero = lerInteiro("Numero: ");
        System.out.print("Bairro: ");
        String bairro = SCANNER.nextLine();
        System.out.print("Cidade: ");
        String cidade = SCANNER.nextLine();
        return new Endereco(logradouro, numero, bairro, cidade);
    }

    private static TipoTerreno lerTipoTerreno() {
        System.out.println("Tipo do terreno:");
        System.out.println("1 - Residencial");
        System.out.println("2 - Comercial");
        int opcao = lerInteiro("Opcao: ");
        return opcao == 2 ? TipoTerreno.COMERCIAL : TipoTerreno.RESIDENCIAL;
    }

    // =================================================================
    // Opcao 3 - Listar Imoveis
    // =================================================================
    private static void listarImoveis() {
        System.out.println("\n--- Lista de Imoveis Cadastrados ---");
        List<Imovel> imoveis = SERVICO.getImoveis();

        if (imoveis.isEmpty()) {
            System.out.println("Nenhum imovel cadastrado ainda.");
            return;
        }

        for (Imovel i : imoveis) {
            System.out.println(i);
        }
    }

    // =================================================================
    // Opcao 4 - Vender Imovel
    // =================================================================
    private static void venderImovel() {
        System.out.println("\n--- Venda de Imovel ---");
        Imovel imovel = selecionarImovelPorCodigo();
        if (imovel == null) {
            return;
        }

        Cliente comprador = selecionarClientePorCpf();
        if (comprador == null) {
            return;
        }

        SERVICO.venderImovel(imovel, comprador);
    }

    // =================================================================
    // Opcao 5 - Alugar Imovel
    // =================================================================
    private static void alugarImovel() {
        System.out.println("\n--- Aluguel de Imovel ---");
        Imovel imovel = selecionarImovelPorCodigo();
        if (imovel == null) {
            return;
        }

        Cliente locatario = selecionarClientePorCpf();
        if (locatario == null) {
            return;
        }

        double valorMensal = lerDouble("Valor mensal do aluguel: ");
        SERVICO.alugarImovel(imovel, locatario, valorMensal);
    }

    // =================================================================
    // Opcao 6 - Buscar Imovel
    // =================================================================
    private static void buscarImovel() {
        System.out.println("\n--- Buscar Imovel ---");
        System.out.println("1 - Buscar por tipo (Casa, Apartamento, Terreno)");
        System.out.println("2 - Buscar por status (DISPONIVEL, ALUGADO, VENDIDO)");
        int opcao = lerInteiro("Opcao: ");

        List<Imovel> resultado;

        if (opcao == 1) {
            System.out.print("Digite o tipo (Casa, Apartamento ou Terreno): ");
            String tipo = SCANNER.nextLine();
            resultado = SERVICO.buscarImovelPorTipo(tipo);
        } else if (opcao == 2) {
            System.out.print("Digite o status (DISPONIVEL, ALUGADO ou VENDIDO): ");
            String status = SCANNER.nextLine();
            try {
                resultado = SERVICO.buscarImovelPorStatus(StatusImovel.valueOf(status.toUpperCase()));
            } catch (IllegalArgumentException e) {
                System.out.println("Status invalido.");
                return;
            }
        } else {
            System.out.println("Opcao invalida.");
            return;
        }

        if (resultado.isEmpty()) {
            System.out.println("Nenhum imovel encontrado para esse criterio.");
        } else {
            resultado.forEach(System.out::println);
        }
    }

    // =================================================================
    // Metodos auxiliares (selecao de objetos ja cadastrados e leitura
    // segura de entrada do teclado)
    // =================================================================

    private static Imovel selecionarImovelPorCodigo() {
        int codigo = lerInteiro("Codigo do imovel: ");
        for (Imovel i : SERVICO.getImoveis()) {
            if (i.getCod() == codigo) {
                return i;
            }
        }
        System.out.println("Nenhum imovel encontrado com o codigo " + codigo + ".");
        return null;
    }

    private static Cliente selecionarClientePorCpf() {
        System.out.print("CPF do cliente: ");
        String cpf = SCANNER.nextLine();
        for (Cliente c : SERVICO.getClientes()) {
            if (c.getCpf().equals(cpf)) {
                return c;
            }
        }
        System.out.println("Nenhum cliente encontrado com o CPF " + cpf + ".");
        return null;
    }

    private static int lerInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = SCANNER.nextLine();
            try {
                return Integer.parseInt(entrada.trim());
            } catch (NumberFormatException e) {
                System.out.println("Valor invalido. Digite um numero inteiro.");
            }
        }
    }

    private static double lerDouble(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = SCANNER.nextLine();
            try {
                return Double.parseDouble(entrada.trim().replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.println("Valor invalido. Digite um numero (ex: 1500.50).");
            }
        }
    }

    private static boolean lerBooleano(String mensagem) {
        System.out.print(mensagem);
        String entrada = SCANNER.nextLine().trim().toLowerCase();
        return entrada.equals("s") || entrada.equals("sim");
    }
}
