package modelo;


public class Apartamento extends Imovel {

    private int andar;
    private int numeroApt;
    private double vlrCondominio;
    private double iptu;

    public Apartamento(int cod, Endereco endereco, double valor, double area,
                        int andar, int numeroApt, double vlrCondominio, double iptu) {
        super(cod, endereco, valor, area);
        this.andar = andar;
        this.numeroApt = numeroApt;
        this.vlrCondominio = vlrCondominio;
        this.iptu = iptu;
    }

    public int getAndar() {
        return andar;
    }

    public void setAndar(int andar) {
        this.andar = andar;
    }

    public int getNumeroApt() {
        return numeroApt;
    }

    public void setNumeroApt(int numeroApt) {
        this.numeroApt = numeroApt;
    }

    public double getVlrCondominio() {
        return vlrCondominio;
    }

    public void setVlrCondominio(double vlrCondominio) {
        this.vlrCondominio = vlrCondominio;
    }

    public double getIptu() {
        return iptu;
    }

    public void setIptu(double iptu) {
        this.iptu = iptu;
    }

    
    @Override
    public double calcularValorFinal() {
        return getValor() + iptu + vlrCondominio;
    }

    @Override
    public String toString() {
        return "Apartamento{" + super.toString()
                + ", andar=" + andar
                + ", numeroApt=" + numeroApt
                + ", vlrCondominio=" + vlrCondominio
                + ", iptu=" + iptu
                + ", valorFinal=" + calcularValorFinal()
                + "}";
    }
}
