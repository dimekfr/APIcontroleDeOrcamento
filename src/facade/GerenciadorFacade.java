package facade;

import java.io.FileNotFoundException;
import java.time.LocalDate;

import dominio.PlanoContas;
import negocios.AgenteAnaliseComparativa;
import negocios.AgenteOrcamentoInicial;
import negocios.AgentePrevisao;
import negocios.AgenteRealizadoMensal;
import util.CategoriaMes;

/**
 *  Clase fachada para todos os agentes que operam sobre o Plano de Contas
 *
 */
public class GerenciadorFacade {
	
	private AgenteAnaliseComparativa agenteAnaliseComparativa;
	private AgenteRealizadoMensal agenteRealizadoMensal;
	private AgentePrevisao agentePrevisao;
	private AgenteOrcamentoInicial agenteOrcamentoInicial;


	public GerenciadorFacade(PlanoContas plano) {
		agenteAnaliseComparativa = new AgenteAnaliseComparativa(plano);
		agenteRealizadoMensal = new AgenteRealizadoMensal(plano);
		agentePrevisao = new AgentePrevisao(plano);
		agenteOrcamentoInicial = new AgenteOrcamentoInicial(plano);
		
	}
	
	/**
	 * Executa o agente de previsão.
	 * @param option 1 - Valor Fixo, 2 - Valor em Porcentual, 3 - Valor Ano Anterior
	 * @param codigo Código da Rúbrica
	 * @param valor Valor de previsão
	 * @param mes Mês da previsão
	 */
	public void geraPrevisao(int option, int codigo, double valor, int mes) {
		
		if(LocalDate.now().isBefore(agentePrevisao.getPlanoContas().getDataCongelamento()) && valor >= 0) {
			
			switch(option) {
				case AgentePrevisao.PREVISAO_VALORPORCENTAGEM:
					agentePrevisao.previsaoPorcentagem(codigo, valor, mes);
					break;
				
				case AgentePrevisao.PREVISAO_VALORFIXO:
					agentePrevisao.previsaoValorFixo(codigo, valor, mes);
					break;
				
				case AgentePrevisao.PREVISAO_VALORANOANTERIOR:
					agentePrevisao.previsaoManterAnoAnterior(codigo, mes);
					break;
				
				default: 
					System.out.println("Opção não existente!");
			}
		}
		else {
			if(valor <= 0) {
				System.out.println("Valor não pode ser negativo");
			}
			else {
				System.out.println("Data de congelamento atingida, desculpe, mas alterações nas previsões não podem mais ser feitas");	
			}
		}
	}
	
	/**
	 * Executa o agente de Orçamento Inicial.
	 * @param filename Arquivo de Orçamento Inicial 
	 * @throws FileNotFoundException
	 */
	public void lerOrcamentoInicial(String filename) throws FileNotFoundException {
		agenteOrcamentoInicial.lerOrcamentoAnterior(filename);
	}
	
	/**
	 * Executa o agente de Realizado Mensal para gerar um template
	 * a ser preenchido pelo usuário.
	 * @param mes
	 */
	public void geraTemplateRealizadoMensal(CategoriaMes mes) {
		agenteRealizadoMensal.geraTemplateOrcamentoMensal(mes);
	}
	
	/**
	 * Executa o agente de Realizado Mensal para ler o
	 * arquivo preenchido pelo usuário.
	 * @param filename
	 * @param mes
	 */
	public void leRealizadoMensal(String filename, CategoriaMes mes) {
		agenteRealizadoMensal.leRealizadoMensal(filename, mes);
	}
	
	/**
	 * Executa o agente de Analise Comparativa para gerar analise
	 * no período entre os meses fornecidos.
	 * @param mesInicial
	 * @param mesFinal
	 */
	public void geraAnalise(CategoriaMes mesInicial, CategoriaMes mesFinal) {

		agenteAnaliseComparativa.geraAnaliseComparativa(mesInicial, mesFinal);
	}

	
}
