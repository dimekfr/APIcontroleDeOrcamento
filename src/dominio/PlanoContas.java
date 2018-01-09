package dominio;

import java.time.LocalDate;
import java.util.*;


/**
 *  Classe que representa um Plano de Contas de uma empresa.
 *  Usado o Design-Pattern de Singleton
 *
 */
public class PlanoContas {
	
	private Map<Integer, Rubrica> rubricas;
	private LocalDate dataCongelamento;
	
	static PlanoContas instance;
	
	private Map<Integer, String> rubricasEspeciais;
	
	/**
	 * Implementação do Singleton Plano de Contas
	 * @return A única instância de Plano de Contas do Sistema
	 */
	public static PlanoContas getInstance() {
		
		if(instance == null){
			instance = new PlanoContas();
			instance.inicializaMapRubricasEspeciais();
		}		
		return(instance);	
	}
	
	/**
	 *  Inicializa as rubricas especiais que possuem fórmula especial para o somatório.
	 *  A chave do map é o código da rúbrica, e a String é a fórmula dos códigos que devem ser
	 *  somados ou subtraídos.
	 *  Ex:
	 *  1 -> "103 - 2396"    
	 *  Rubrica 1 possui o valor da rubrica 103 subtraído da rúbrica 2396
	 */
	private void inicializaMapRubricasEspeciais() {
		
		rubricasEspeciais = new HashMap<Integer, String>();
		
		rubricasEspeciais.put(103, "103"); 
		rubricasEspeciais.put(2396, "2396"); 
		rubricasEspeciais.put(1, "103 - 2396"); 
		rubricasEspeciais.put(110, "2 + 120"); 
		rubricasEspeciais.put(2, "2"); 
		rubricasEspeciais.put(120, "120"); 
		rubricasEspeciais.put(3, "1 - 110"); 
		rubricasEspeciais.put(2398, "133 + 156 + 312");
		rubricasEspeciais.put(133, "133 + 142");
		rubricasEspeciais.put(4, "133"); 
		rubricasEspeciais.put(142, "142"); 
		rubricasEspeciais.put(156, "156 + 2401"); 
		rubricasEspeciais.put(5, "156"); 
		rubricasEspeciais.put(2401, "156"); 
		rubricasEspeciais.put(312, "312 + 338"); 
		rubricasEspeciais.put(6, "312");
		rubricasEspeciais.put(338, "338"); 
		rubricasEspeciais.put(7, "103 - 2398"); 
		rubricasEspeciais.put(187, "187 - 193");  
		rubricasEspeciais.put(10, "187"); 
		rubricasEspeciais.put(193, "193"); 
		rubricasEspeciais.put(11, "9 - 187"); 
		rubricasEspeciais.put(197, "352 + 201");  
		rubricasEspeciais.put(352, "352");  
		rubricasEspeciais.put(201, "201"); 
		rubricasEspeciais.put(12, "11 - 197"); 
		rubricasEspeciais.put(13, "12 - 914 - 205"); 
		rubricasEspeciais.put(14, "11");  
		rubricasEspeciais.put(15, "172");  
		rubricasEspeciais.put(16, "184"); 
		rubricasEspeciais.put(17, "- 187"); 
		rubricasEspeciais.put(18, "13 + 15 + 16 + 17");
		rubricasEspeciais.put(19, "2398");
		rubricasEspeciais.put(20, "21 + 22 + 23");		
	}
	
	public PlanoContas(){
		rubricas = new LinkedHashMap<Integer, Rubrica>();
		dataCongelamento = LocalDate.of(2020, 1, 11);
	}
	
	public void setRubricas(Map<Integer, Rubrica> map) {
		this.rubricas = map;
	}
	
	public Map<Integer, Rubrica> getRubricas(){
		return rubricas;
	}
	
	/**
	 * Determina a data de congelamento de previsões,
	 * data a partir do qual não poderá mais haver previsões.
	 * @param date Data de congelamento
	 */
	public void setDataCongelamento(LocalDate date) {
		this.dataCongelamento = date;
	}
	
	public LocalDate getDataCongelamento() {
		return this.dataCongelamento;
	}

	public Map<Integer, String> getRubricasEspeciais() {
		return this.rubricasEspeciais;
	}
	

}
