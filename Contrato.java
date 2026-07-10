package contrato;

import modelo.Cliente;
import modelo.Imovel;


public class Contrato {

    private Cliente cliente;
    private Imovel imovel;
    private String tipoContrato;
    private double valorAcordado;

    public Contrato(Cliente cliente, Imovel imovel, String tipoContrato, double valorAcordado) {
        this.cliente = cliente;
        this.imovel = imovel;
        this.tipoContrato = tipoContrato;
        this.valorAcordado = valorAcordado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Imovel getImovel() {
        return imovel;
    }

    public void setImovel(Imovel imovel) {
        this.imovel = imovel;
    }

    public String getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(String tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    public double getValorAcordado() {
        return valorAcordado;
    }

    public void setValorAcordado(double valorAcordado) {
        this.valorAcordado = valorAcordado;
    }

    
    public String emitirContrato() {
        return "===== CONTRATO DE " + tipoContrato.toUpperCase() + " =====\n"
                + "Cliente: " + cliente.getNome() + " (CPF: " + cliente.getCpf() + ")\n"
                + "Imovel: cod " + imovel.getCod() + " - " + imovel.getEndereco() + "\n"
                + "Valor acordado: R$ " + String.format("%.2f", valorAcordado) + "\n"
                + "================================";
    }

    @Override
    public String toString() {
        return "Contrato{tipoContrato='" + tipoContrato + "'"
                + ", cliente=" + cliente.getNome()
                + ", imovel(cod)=" + imovel.getCod()
                + ", valorAcordado=" + valorAcordado
                + "}";
    }
}
