package tests;

import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import dominio.PlanoContas;
import facade.GerenciadorFacade;
import util.CategoriaMes;

public class AgenteRealizadoMensalTest {

	private GerenciadorFacade gerenciador;
	private PlanoContas planoContas;
	
	@Before
	public void createPlanoContasAndAgents(){
		 planoContas = PlanoContas.getInstance();
		 gerenciador = new GerenciadorFacade(planoContas);
	}
	//===================interface-based tests:	===================//

	/**
	 * Partição: Filename ser nulo? 
	 * Opções de resposta: 
	 * Sim
	 * Não
	 */
	@Rule
	public ExpectedException expectedEx1 = ExpectedException.none();
	
	@Test(expected = java.lang.NullPointerException.class)
	public void nullFilename() throws FileNotFoundException{	
		gerenciador.leRealizadoMensal(null, CategoriaMes.JANEIRO);
		expectedEx1.expectMessage("Expected exception:  java.lang.NullPointerException");
	}


	
	//===================functionality-based tests:	===================//
	/**
	 * Partição:  Agente relaizado mensal está lendo bem o plano relaizado mensal? 
	 * Opções de resposta: 
	 * Sim
	 * Não
	 */

	@Test(expected = Test.None.class)
	public void isReadingPlan() throws FileNotFoundException {	
		gerenciador.leRealizadoMensal("TemplateJaneiroCompletado.xls", CategoriaMes.JANEIRO);

	}
	

	/**
	 * Partição:  Exceção ao ler plano base quando tipo de arquivo é errado, ocorre? 
	 * Opções de resposta: 
	 * Sim
	 * Não
	 */
	@Rule
	public ExpectedException expectedEx2 = ExpectedException.none();

	@Test(expected = java.io.FileNotFoundException.class)
	public void isThrowningExpectionWhenReadingPlan() throws FileNotFoundException{	
		gerenciador.leRealizadoMensal("NomeQueNaoExiste.xls", CategoriaMes.JANEIRO);
		expectedEx2.expectMessage("Expected exception: java.io.FileNotFoundException");
	}
	

	/**
	 * Partição:  Exceção ao ler plano base quando nome de arquivo é errado, ocorre? 
	 * Opções de resposta: 
	 * Sim
	 * Não
	 */
	@Rule
	public ExpectedException expectedEx3 = ExpectedException.none();

	@Test(expected = org.apache.poi.openxml4j.exceptions.InvalidFormatException.class)
	public void isThrowningExpectionWhenWrongType() throws FileNotFoundException{	
		gerenciador.leRealizadoMensal("Modelo_Controle_Orcamentario_Completo.csv", CategoriaMes.JANEIRO);
		expectedEx3.expectMessage("Expected exception: org.apache.poi.openxml4j.exceptions.InvalidFormatException");
	}


}
