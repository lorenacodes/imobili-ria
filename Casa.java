package modelo;


public class Casa extends Imovel {

    private int nrQuartos;
    private boolean garagem;
    private double iptu;

    public Casa(int cod, Endereco endereco, double valor, double area,
                int nrQuartos, boolean garagem, double iptu) {
        super(cod, endereco, valor, area);
        this.nrQuartos = nrQuartos;
        this.garagem = garagem;
        this.iptu = iptu;
    }

    public int getNrQuartos() {
        return nrQuartos;
    }

    public void setNrQuartos(int nrQuartos) {
        this.nrQuartos = nrQuartos;
    }

    public boolean isGaragem() {
        return garagem;
    }

    public void setGaragem(boolean garagem) {
        this.garagem = garagem;
    }

    public double getIptu() {
        return iptu;
    }

    public void setIptu(double iptu) {
        this.iptu = iptu;
    }

    
    @Override
    public double calcularValorFinal() {
        return getValor() + iptu;
    }

    @Override
    public String toString() {
        return "Casa{" + super.toString()
                + ", nrQuartos=" + nrQuartos
                + ", garagem=" + garagem
                + ", iptu=" + iptu
                + ", valorFinal=" + calcularValorFinal()
                + "}";
    }
}
