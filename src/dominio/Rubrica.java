package dominio;
import static org.junit.Assert.assertNotNull;

import java.util.*;

import util.CategoriaRubrica;

/**
 *  Classe que representa uma Rubrica de um Plano de Contas
 *
 */
public class Rubrica {
	
	private Rubrica pai;
	private String nome;
	private int codigo;
	private CategoriaRubrica categoria;
	private Double[] valoresAnoPassado;
	private Double[] valoresPrevistos;
	private Double[] valoresRealizados;
	private List<Rubrica> subRubricas;
	
	public Rubrica(Rubrica pai, String nome, int codigo, CategoriaRubrica categoria, Double[] valoresAnoPassado) {
		this.pai = pai;
		this.nome = nome;
		this.codigo = codigo;
		this.categoria = categoria;
		this.subRubricas = new ArrayList<>();
		this.valoresAnoPassado = valoresAnoPassado; 
		this.valoresPrevistos = new Double[12]; 
		this.valoresRealizados = new Double[12]; 
	}


	public Rubrica getPai() {
		return this.pai;
	}
	public int getCodigo() {
		return codigo;
	}
	public List<Rubrica> getSubRubricas(){
		return this.subRubricas;
	
	}
	
	public void addSubRubrica(Rubrica subRubrica) {
		this.subRubricas.add(subRubrica);
	}
	
	public void setValorRealizado(int mes, double valor) {
		try {
			this.valoresRealizados[mes - 1] = valor;
			
		}catch(ArrayIndexOutOfBoundsException e) {
			 System.out.println("Mês inválido!");
		}
	}
	
	public void setValorPrevisto(int mes, double valor) {
		try {
			this.valoresPrevistos[mes - 1] = valor;
			
		}catch(ArrayIndexOutOfBoundsException e) {
			 System.out.println("Mês inválido!");
		}
	}
	
	public String getValorPrevisto(int mes, int codigo) {
		String out = this.nome + "  " +  String.valueOf(this.codigo) + " " + this.valoresPrevistos[mes - 1];
		return out;
	}
	
	public Double getValorPrevisto(int mes) {
		
		return this.valoresPrevistos[mes - 1];
	}
	
	
	public double getvalorAnoPassado(int mes) {
		return this.valoresAnoPassado[mes - 1];
	}
	
	public String getNome() {
		return this.nome;
	}
	
	public CategoriaRubrica getCategoria () {
		return this.categoria;
	}
	public Double getValorRealizado(int mes) {
		return this.valoresRealizados[mes - 1];
	}

	/**
	 * Dado um mes retorna a soma das previões de todas as suas subrúbricas, não incluindo ela mesma. 
	 * @param mes Mês da previsão
	 * @return Somatório das pŕevisões das subrubricas
	 */
	public Double somaValoresPrevistosSubrubricas(int mes)  {
		if(this.getSubRubricas().isEmpty() ) {
			if(this.getValorPrevisto(mes) == null) {
				return 0.0;
			}
			else {
				return this.getValorPrevisto(mes);	
			}
		
		}
		
		Double count = 0.0;
		for (Rubrica subRubrica : this.getSubRubricas()) {
			count = count + subRubrica.somaValoresPrevistosSubrubricas(mes);
		}
		
		return count;
	}

	/**
	 * Dado um mes retorna a soma dos valores realizados de todas as suas subrúbricas, não incluindo ela mesma. 
	 * @param mes Mês do Realizado Mensal
	 * @return Somatório dos valores realizados das subrubricas
	 */
	public Double somaValoresRealizadosSubrubricas(int mes)  {
		if (this.getSubRubricas().isEmpty() ) {
			try { 
				if(this.getValorRealizado(mes) == null) {
					return 0.0;
				}
				else {
					return this.getValorRealizado(mes);
				}
			}
			catch (NullPointerException ex){
				return 0.0;
			}
		}
		
		Double count = 0.0;
		for (Rubrica subRubrica : this.getSubRubricas()) {
			count = count + subRubrica.somaValoresRealizadosSubrubricas(mes);
		}
		return count;
	}
	
	
	public String toString() {
		
		assertNotNull(this.nome);
		
		String out = this.nome + "  " +  String.valueOf(this.codigo);
		double total = 0;
		for(int i = 0; i < 12; i++) {
			if(this.valoresAnoPassado[i] == null) System.out.println(this.nome +"\n");
			total += this.valoresAnoPassado[i];
			
		}
		out += "  " + String.valueOf(total);
		return out;
	}
}
