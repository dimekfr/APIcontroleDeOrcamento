package tests;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.LinkedHashMap;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import dominio.PlanoContas;
import dominio.Rubrica;
import facade.GerenciadorFacade;
import negocios.GerenciadorArquivos;

public class AgenteOrcamentoInicialTest {

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
		gerenciador.lerOrcamentoInicial(null);
		expectedEx1.expectMessage("Expected exception:  java.lang.NullPointerException");
	}
	/**
	 * Partição:  Gera exceção se não tem o código que a pessoa digitou?
	 * Opções de resposta: 
	 * Sim
	 * Não
	 */

	@Test(expected = java.lang.NullPointerException.class)
	public void isReadingBasePlanCorrectlyNOCODE() throws FileNotFoundException {	
		LinkedHashMap<Integer, Rubrica> map;
		GerenciadorArquivos le = new GerenciadorArquivos();
		map  = le.lerOrcamentoInicial("Modelo_Controle_Orcamentario_Completo.csv");
		assertEquals(map.get(1090).getCodigo(), null);
	}

	
	//===================functionality-based tests:	===================//
	/**
	 * Partição:  Agente orçamento inicial está lendo bem o plano base? 
	 * Opções de resposta: 
	 * Sim
	 * Não
	 */
	
	@Test(expected = Test.None.class)
	public void isReadingBasePlan() throws FileNotFoundException {	
		gerenciador.lerOrcamentoInicial("Modelo_Controle_Orcamentario_Completo.csv");

	}

	/**
	 * Partição:  Exceção ao ler plano base quando nome de arquivo é errado, ocorre? 
	 * Opções de resposta: 
	 * Sim
	 * Não
	 */

	@Rule
	public ExpectedException expectedEx2 = ExpectedException.none();

	@Test(expected = java.io.FileNotFoundException.class)
	public void isThrowningExpectionWhenReadingBasePlan() throws FileNotFoundException{	
		gerenciador.lerOrcamentoInicial("NomeQueNaoExiste.csv");
		expectedEx2.expectMessage("Expected exception: java.io.FileNotFoundException");
	}
	/**
	 * Partição: Rúbricas estão sendo armazenadas corretamente dada leitura do plano base (teste se o nome está correto)?
	 * Opções de resposta: 
	 * Sim
	 * Não
	 */

	@Test
	public void isReadingBasePlanCorrectlyTESTBYNAME() throws FileNotFoundException {	
		LinkedHashMap<Integer, Rubrica> map;
		GerenciadorArquivos le = new GerenciadorArquivos();
		map  = le.lerOrcamentoInicial("Modelo_Controle_Orcamentario_Completo.csv");
		assertEquals(map.get(103).getNome(), "Receita Bruta");
	}
	/**
	 * Partição:   Rúbricas estão sendo armazenadas corretamente dada leitura do plano base (teste se o código está correto)?
	 * Opções de resposta: 
	 * Sim
	 * Não
	 */
	@Test
	public void isReadingBasePlanCorrectlyTESTBYCODE() throws FileNotFoundException {	
		LinkedHashMap<Integer, Rubrica> map;
		GerenciadorArquivos le = new GerenciadorArquivos();
		map  = le.lerOrcamentoInicial("Modelo_Controle_Orcamentario_Completo.csv");
		assertEquals(map.get(103).getCodigo(), 103);
	}
}
