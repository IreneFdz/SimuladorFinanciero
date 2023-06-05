package ontology.conceptos;


public class Carta_acciones extends Concepto {
    // tipo = 1 -> carta de acciones de empresas
    // tipo = 2 -> carta de operaciones
    // tipo = 3 -> carta de pago de comisiones
	private int tipo;
	private int id;
    private int valor;
    private int cantidad;
    private int a_pagar;
	private boolean doblada;
    private String descripcion;
    private String empresa;
	
	public Carta_acciones() {}

	public int getTipo() {
		return tipo;
	}
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

    //Acciones
    public String getEmpresa() {
		return empresa;
	}
	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}
    public int getValor() {
		return valor;
	}
	public void setValor(int valor) {
		this.valor = valor;
	}
	public boolean getDoblada() {
		return doblada;
	}
	public void setDoblada(boolean doblada) {
		this.doblada = doblada;
	}

    //carta de operaciones
    public int getTipoOperaciones() {
		return cantidad;
	}
    public void setTipoOperaciones(int cantidad) {
		this.cantidad = cantidad;
	}

    //carta de comisiones
    public int getA_pagar() {
		return a_pagar;
	}

	public void setA_pagar(int a_pagar) {
		this.a_pagar = a_pagar;
	}
}