package servico;

import java.util.ArrayList;
import java.util.List;

import contrato.Contrato;
import excecoes.CpfDuplicadoException;
import excecoes.ImovelIndisponivelException;
import excecoes.ValidacaoException;
import excecoes.ValorInvalidoException;
import modelo.Cliente;
import modelo.Imovel;
import modelo.StatusImovel;


public class ServicoImobiliaria {

    private List<Cliente> clientes;
    private List<Imovel> imoveis;
    private List<Contrato> contratos;

    // Contador para geracao automatica do codigo do imovel (regra do enunciado).
    // Comeca em 1 e e incrementado a cada cadastro bem-sucedido.
    private int proximoCodigo = 1;

    public ServicoImobiliaria() {
        this.clientes = new ArrayList<>();
        this.imoveis = new ArrayList<>();
        this.contratos = new ArrayList<>();
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public List<Imovel> getImoveis() {
        return imoveis;
    }

    public List<Contrato> getContratos() {
        return contratos;
    }

    // ---------------------------------------------------------------
    // Cadastro de cliente: valida os dados e impede CPF duplicado.
    // As excecoes sao propagadas (nao capturadas aqui) para que quem
    // chamou o metodo decida como reagir - nesse projeto, quem trata
    // e a classe de menu (GererenciarAgenda).
    // ---------------------------------------------------------------
    public void cadastrarCliente(Cliente c) throws CpfDuplicadoException, ValidacaoException {
        if (c.getNome() == null || c.getNome().trim().isEmpty()) {
            throw new ValidacaoException("Nome do cliente nao pode ser vazio.");
        }
        if (c.getEmail() == null || !c.getEmail().contains("@")) {
            throw new ValidacaoException("E-mail invalido: deve conter '@'.");
        }
        if (!cpfValido(c.getCpf())) {
            throw new ValidacaoException("CPF invalido: " + c.getCpf());
        }

        // clientes.contains() usa o equals() de Cliente (definido por CPF),
        // entao esse if ja cobre a regra de "nao permitir CPF duplicado".
        if (clientes.contains(c)) {
            throw new CpfDuplicadoException(
                    "Ja existe um cliente cadastrado com o CPF " + c.getCpf());
        }

        clientes.add(c);
    }

    /**
     * Validacao de CPF pelo algoritmo oficial dos digitos verificadores
     * (nao e so checar 11 caracteres). Regra:
     * 1) precisa ter 11 digitos e nao pode ser uma sequencia repetida
     *    (ex: 111.111.111-11), que "passaria" na conta mas nao existe;
     * 2) o 10o digito e calculado a partir dos 9 primeiros;
     * 3) o 11o digito e calculado a partir dos 10 primeiros.
     */
    private boolean cpfValido(String cpf) {
        if (cpf == null) {
            return false;
        }
        String numeros = cpf.replaceAll("[^0-9]", "");
        if (numeros.length() != 11 || numeros.chars().distinct().count() == 1) {
            return false;
        }

        int[] digito = new int[11];
        for (int i = 0; i < 11; i++) {
            digito[i] = numeros.charAt(i) - '0';
        }

        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += digito[i] * (10 - i);
        }
        int primeiroVerificador = (soma * 10) % 11;
        if (primeiroVerificador == 10) {
            primeiroVerificador = 0;
        }
        if (primeiroVerificador != digito[9]) {
            return false;
        }

        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += digito[i] * (11 - i);
        }
        int segundoVerificador = (soma * 10) % 11;
        if (segundoVerificador == 10) {
            segundoVerificador = 0;
        }
        return segundoVerificador == digito[10];
    }

    // ---------------------------------------------------------------
    // Cadastro de imovel: valida o valor e atribui o codigo automatico.
    // O objeto chega aqui com um cod "provisorio" (ex: 0), que e
    // sobrescrito pelo contador interno antes de entrar na lista.
    // ---------------------------------------------------------------
    public void cadastrarImovel(Imovel i) throws ValorInvalidoException {
        if (i.getValor() <= 0) {
            throw new ValorInvalidoException("Valor do imovel deve ser maior que zero.");
        }

        i.setCod(proximoCodigo);
        proximoCodigo++;

        imoveis.add(i);
    }

    // ---------------------------------------------------------------
    // Buscas: usam o nome da classe (Casa, Apartamento, Terreno) e o
    // enum StatusImovel para filtrar a lista de imoveis cadastrados.
    // ---------------------------------------------------------------
    public List<Imovel> buscarImovelPorTipo(String tipo) {
        List<Imovel> resultado = new ArrayList<>();
        for (Imovel i : imoveis) {
            if (i.getClass().getSimpleName().equalsIgnoreCase(tipo)) {
                resultado.add(i);
            }
        }
        return resultado;
    }

    public List<Imovel> buscarImovelPorStatus(StatusImovel status) {
        List<Imovel> resultado = new ArrayList<>();
        for (Imovel i : imoveis) {
            if (i.getStatus() == status) {
                resultado.add(i);
            }
        }
        return resultado;
    }

    
    public void venderImovel(Imovel imovel, Cliente comprador) {
        try {
            if (imovel.getStatus() != StatusImovel.DISPONIVEL) {
                throw new ImovelIndisponivelException(
                        "Imovel de codigo " + imovel.getCod() + " nao esta disponivel para venda.");
            }
            if (imovel.getValor() <= 0) {
                throw new ValorInvalidoException(
                        "Valor do imovel deve ser maior que zero para realizar a venda.");
            }

            imovel.setStatus(StatusImovel.VENDIDO);

            Contrato contrato = new Contrato(comprador, imovel, "Venda", imovel.calcularValorFinal());
            contratos.add(contrato);

            System.out.println("Venda realizada com sucesso!");
            System.out.println(contrato.emitirContrato());

        } catch (ImovelIndisponivelException | ValorInvalidoException e) {
            System.out.println("Erro ao vender imovel: " + e.getMessage());
        }
    }

   
    public void alugarImovel(Imovel imovel, Cliente locatario, double valorMensal) {
        try {
            if (imovel.getStatus() != StatusImovel.DISPONIVEL) {
                throw new ImovelIndisponivelException(
                        "Imovel de codigo " + imovel.getCod() + " nao esta disponivel para aluguel.");
            }
            if (valorMensal <= 0) {
                throw new ValorInvalidoException(
                        "Valor mensal do aluguel deve ser maior que zero.");
            }

            imovel.setStatus(StatusImovel.ALUGADO);

            Contrato contrato = new Contrato(locatario, imovel, "Aluguel", valorMensal);
            contratos.add(contrato);

            System.out.println("Aluguel realizado com sucesso!");
            System.out.println(contrato.emitirContrato());

        } catch (ImovelIndisponivelException | ValorInvalidoException e) {
            System.out.println("Erro ao alugar imovel: " + e.getMessage());
        }
    }

   
    public void gerarRelatorios() {
        long qtdDisponiveis = imoveis.stream()
                .filter(i -> i.getStatus() == StatusImovel.DISPONIVEL)
                .count();

        long qtdVendidos = imoveis.stream()
                .filter(i -> i.getStatus() == StatusImovel.VENDIDO)
                .count();

        double totalVendas = contratos.stream()
                .filter(c -> "Venda".equalsIgnoreCase(c.getTipoContrato()))
                .mapToDouble(Contrato::getValorAcordado)
                .sum();

        long qtdAlugados = imoveis.stream()
                .filter(i -> i.getStatus() == StatusImovel.ALUGADO)
                .count();

        double totalAlugueis = contratos.stream()
                .filter(c -> "Aluguel".equalsIgnoreCase(c.getTipoContrato()))
                .mapToDouble(Contrato::getValorAcordado)
                .sum();

        Imovel imovelMaisCaro = imoveis.stream()
                .max((a, b) -> Double.compare(a.calcularValorFinal(), b.calcularValorFinal()))
                .orElse(null);

        System.out.println("========== RELATORIO DA IMOBILIARIA ==========");
        System.out.println("Quantidade de imoveis disponiveis: " + qtdDisponiveis);
        System.out.println("Quantidade de imoveis vendidos: " + qtdVendidos);
        System.out.println("Total arrecadado com imoveis vendidos: R$ " + String.format("%.2f", totalVendas));
        System.out.println("Quantidade de imoveis alugados: " + qtdAlugados);
        System.out.println("Total arrecadado com alugueis: R$ " + String.format("%.2f", totalAlugueis));
        System.out.println("Imovel mais caro: " + (imovelMaisCaro != null ? imovelMaisCaro : "Nenhum imovel cadastrado"));
        System.out.println("===============================================");
    }
}
