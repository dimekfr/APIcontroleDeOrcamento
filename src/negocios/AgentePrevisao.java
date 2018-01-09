package negocios;

import dominio.PlanoContas;

/**
 *   Classe que gerencia a previsão para o próximo ano do 
 *   plano de contas.
 */
public class AgentePrevisao extends AgenteAbstract {

	public static final int PREVISAO_VALORFIXO = 1;
	public static final int PREVISAO_VALORPORCENTAGEM = 2;
	public static final int PREVISAO_VALORANOANTERIOR = 3;

	
	public AgentePrevisao(PlanoContas plano) {
		super(plano);
	}

	/**
	 * Prevê o valor de uma rúbrica como sendo um percentual do valor da mesma no ano passado.
	 * @param codigo Código da rúbrica
	 * @param porcentagem Taxa do valor comparado ao ano passado em percentual
	 * @param mes Mês da previsão
	 */
	public void previsaoPorcentagem(int codigo, double porcentagem, int mes) {
		
		try {
			double valorAnoPassado = getPlanoContas().getRubricas().get(codigo).getvalorAnoPassado(mes);
			
			getPlanoContas().getRubricas().get(codigo).setValorPrevisto(mes, (valorAnoPassado*porcentagem));
		}
		catch (NullPointerException npe) {
			 System.out.println("O código digitado não existe!");
		}
	}

	/**
	 * Prevê o valor de uma rúbrica com um valor dado
	 * @param codigo Código da rúbrica
	 * @param valor Valor de previsão
	 * @param mes Mês da previsão
	 */
	public void previsaoValorFixo(int codigo, double valor, int mes) {
		
		try {
			super.getPlanoContas().getRubricas().get(codigo).setValorPrevisto(mes, valor);
		}
		catch(NullPointerException npe) {
			 System.out.println("O código digitado não existe!");
		}
	}
	
	/**
	 * Prevê o valor da rúbrica como sendo o mesmo do ano anterios.
	 * @param codigo Código da rúbrica
	 * @param mes Mês da previsão
	 */
	public void previsaoManterAnoAnterior(int codigo, int mes) {
		
		try {
			double valorAnoPassado = getPlanoContas().getRubricas().get(codigo).getvalorAnoPassado(mes);
			super.getPlanoContas().getRubricas().get(codigo).setValorPrevisto(mes, valorAnoPassado);
		}
		catch (NullPointerException npe) {
			 System.out.println("O código digitado não existe!");
		}
	}
	
	
	public String toString(int codigo, int mes) {
		
		return super.getPlanoContas().getRubricas().get(codigo).getValorPrevisto(mes, codigo) + " mes: " + mes;
	}
	

}