package modelo;


public class Terreno extends Imovel {

    private static final double TAXA_ADMINISTRATIVA = 0.08;

    private TipoTerreno tipo;

    public Terreno(int cod, Endereco endereco, double valor, double area, TipoTerreno tipo) {
        super(cod, endereco, valor, area);
        this.tipo = tipo;
    }

    public TipoTerreno getTipo() {
        return tipo;
    }

    public void setTipo(TipoTerreno tipo) {
        this.tipo = tipo;
    }

   
    @Override
    public double calcularValorFinal() {
        return getValor() + (getValor() * TAXA_ADMINISTRATIVA);
    }

    @Override
    public String toString() {
        return "Terreno{" + super.toString() + ", tipo=" + tipo + ", valorFinal=" + calcularValorFinal()+ "}";
    }
}
