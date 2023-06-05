package ontology.conceptos;


public class Empresa extends Concepto {
	private int identificador;
	private int valor_acciones;
    private String nombre;
	private boolean bancarrota;
	private boolean maximo;
	
	public Empresa() {}

	public int getIdentificador() {
		return identificador;
	}

	public void setIdentificador(int identificador) {
		this.identificador = identificador;
	}
	public int getValorAcciones() {
		return valor_acciones;
	}

	public void setValorAcciones(int valor_acciones) {
		this.valor_acciones = valor_acciones;
	}
    public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public boolean getBancarrota() {
		return bancarrota;
	}

	public void setBancarrota(boolean bancarrota) {
		this.bancarrota = bancarrota;
	}
	public boolean getMaximo() {
		return maximo;
	}

	public void setMaximo(boolean maximo) {
		this.maximo = maximo;
	}

}
