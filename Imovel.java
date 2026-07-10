package modelo;

import java.util.Objects;


public abstract class Imovel implements Calculavel {

    private int cod;
    private Endereco endereco;
    private double valor;
    private double area;
    private StatusImovel status;

    protected Imovel(int cod, Endereco endereco, double valor, double area) {
        this.cod = cod;
        this.endereco = endereco;
        this.valor = valor;
        this.area = area;
        
        this.status = StatusImovel.DISPONIVEL;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public StatusImovel getStatus() {
        return status;
    }

    public void setStatus(StatusImovel status) {
        this.status = status;
    }

    /**
     * Cada subclasse concreta (Casa, Apartamento, Terreno) deve
     * sobrescrever este método com sua própria regra de cálculo,
     * conforme especificado no enunciado.
     */
    @Override
    public abstract double calcularValorFinal();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Imovel)) return false;
        Imovel imovel = (Imovel) o;
        return cod == imovel.cod;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cod);
    }

    @Override
    public String toString() {
        return "cod=" + cod
                + ", endereco=" + endereco
                + ", valor=" + valor
                + ", area=" + area
                + ", status=" + status;
    }
}
