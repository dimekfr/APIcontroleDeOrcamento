package tests;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import dominio.PlanoContas;
import facade.GerenciadorFacade;
import negocios.AgentePrevisao;
import util.CategoriaMes;

public class AgentePrevisaoTest {
	private GerenciadorFacade gerenciador;
	private PlanoContas planoContas;
	
	@Before
	public void createPlanoContasAndAgents() throws FileNotFoundException{
		 planoContas = PlanoContas.getInstance();
		 gerenciador = new GerenciadorFacade(planoContas);
		 gerenciador.lerOrcamentoInicial("Modelo_Controle_Orcamentario_Completo.csv");
		 planoContas.setDataCongelamento(LocalDate.now().plusYears(1));
	}
	
	//===================Interface-based Tests:	===================//
	/**
	 * Partição:  Valor para prever ser nulo gera exceção? 
	 * Opções de resposta: 
	 * Sim
	 * Não
	 */
	@Rule
	public ExpectedException expectedEx1 = ExpectedException.none();
	
	@Test(expected = java.lang.NullPointerException.class)
	public void nullValue() throws FileNotFoundException{	
		gerenciador.geraPrevisao(1, 103, (Double) null, CategoriaMes.JANEIRO.toInt());
		expectedEx1.expectMessage("Expected exception:  java.lang.NullPointerException");
	}

	/**
	 * Partição:   Valor de código da rubrica ser nulo gera exceção? 
	 * Opções de resposta: 
	 * Sim
	 * Não
	 */
	@Rule
	public ExpectedException expectedEx2 = ExpectedException.none();
	
	@Test(expected = java.lang.NullPointerException.class)
	public void nullRubricaCode() throws FileNotFoundException{	
		gerenciador.geraPrevisao(1, (Integer) null, 100000, CategoriaMes.JANEIRO.toInt());
		expectedEx1.expectMessage("Expected exception:  java.lang.NullPointerException");
	}
	/**
	 * Partição:   valor negativo para previsao mantém valor do ano anterior? 
	 * Opções de resposta: 
	 * Sim, mantém a do ano anterior
	 * Não, atualiza
	 */

	@Test(expected = java.lang.NullPointerException.class)
	public void negativeValue() throws FileNotFoundException{
		gerenciador.geraPrevisao(1, 103, -10, CategoriaMes.JANEIRO.toInt());
		assertEquals(planoContas.getRubricas().get(103).getValorPrevisto(02),(Double)null, 0.00001);
	}
	
	//===================functionality-based tests:	===================//
	/**
	 * Partição:  Correto valor de saída da previsão manter ano anterior? 
	 * Opções de resposta: 
	 * Sim
	 * Não
	 */

	@Test
	public void manterAnoAnterior() {
		double valuePreviousYear = 	PlanoContas.getInstance().getRubricas().get(103).getvalorAnoPassado(CategoriaMes.JANEIRO.toInt());
		gerenciador.geraPrevisao(AgentePrevisao.PREVISAO_VALORANOANTERIOR, 103, 0, CategoriaMes.JANEIRO.toInt());
		double predictedValue = PlanoContas.getInstance().getRubricas().get(103).getValorPrevisto(CategoriaMes.JANEIRO.toInt());
		assertEquals(predictedValue,valuePreviousYear, 0.00001);
	}	
	/**
	 * Partição:  Correto valor de saída da previsão porcentagem? 
	 * Opções de resposta: 
	 * Sim
	 * Não
	 */

	@Test
	public void percentagePrediction(){
		double valuePreviousYear = 	PlanoContas.getInstance().getRubricas().get(106).getvalorAnoPassado(CategoriaMes.JANEIRO.toInt());
		gerenciador.geraPrevisao(2, 106, 1.10, CategoriaMes.JANEIRO.toInt());
		double predictedValue = PlanoContas.getInstance().getRubricas().get(106).getValorPrevisto(CategoriaMes.JANEIRO.toInt());
		assertEquals(predictedValue,valuePreviousYear*1.10, 0.00001);
	}	
	/**
	 * Partição:   Correto valor de saída da previsão valor fixo? 
	 * Opções de resposta: 
	 * Sim
	 * Não
	 */
	@Test
	public void fixedPrediction(){
		double fixedValue = 19050;
		double valuePreviousYear = 	PlanoContas.getInstance().getRubricas().get(106).getvalorAnoPassado(CategoriaMes.FEVEREIRO.toInt());
		gerenciador.geraPrevisao(1, 106, fixedValue, CategoriaMes.FEVEREIRO.toInt());
		double predictedValue = PlanoContas.getInstance().getRubricas().get(106).getValorPrevisto(CategoriaMes.FEVEREIRO.toInt());
		assertEquals(fixedValue,predictedValue, 0.00001);
	}
	/**
	 * Partição:  Data alteração atingida bloqueia modificações? 
	 * Opções de resposta: 
	 * Sim
	 * Não
	 */
	@Test(expected = java.lang.NullPointerException.class)
	public void freezeDate() {
		planoContas.setDataCongelamento(LocalDate.now());
		double fixedValue = 19050;
		double valuePreviousYear = 	PlanoContas.getInstance().getRubricas().get(106).getvalorAnoPassado(CategoriaMes.FEVEREIRO.toInt());
		gerenciador.geraPrevisao(0, 106, fixedValue, CategoriaMes.FEVEREIRO.toInt());
		double predictedValue = PlanoContas.getInstance().getRubricas().get(106).getValorPrevisto(CategoriaMes.FEVEREIRO.toInt());
		assertEquals(valuePreviousYear,(Double)null, 0.00001);
		expectedEx1.expectMessage("Expected exception:  java.lang.NullPointerException");
	}

}
