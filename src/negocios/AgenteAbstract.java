package negocios;

import dominio.PlanoContas;

/**
 * Classe abstrata para uma classe que pode agir sobre o Plano de Contas.
 *
 */
public abstract class AgenteAbstract {
	
	private PlanoContas planoContas;
	
	public AgenteAbstract(PlanoContas plano) {
	
		this.planoContas = plano;
	}
	
	public PlanoContas getPlanoContas() {
		return this.planoContas;
	}
	
}