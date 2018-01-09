package tests;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import dominio.PlanoContas;
import facade.GerenciadorFacade;
import negocios.GerenciadorArquivos;
import util.CategoriaMes;

public class GerenciadorArquivosTest {
	private GerenciadorArquivos gerenciador;
	private PlanoContas planoContas;
	
	@Before
	public void createPlanoContasAndAgents(){
		 planoContas = PlanoContas.getInstance();
		 gerenciador = new GerenciadorArquivos();
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
	public void nullFilenameOrcamentoInicial() throws FileNotFoundException{	
		gerenciador.lerOrcamentoInicial(null);
		expectedEx1.expectMessage("Expected exception:  java.lang.NullPointerException");
	}
	@Rule
	public ExpectedException expectedEx2 = ExpectedException.none();
	
	@Test(expected = java.lang.NullPointerException.class)
	public void nullFilenameRealizadoMensal() throws FileNotFoundException{	
		gerenciador.lerRealizadoMensal(null);
		expectedEx1.expectMessage("Expected exception:  java.lang.NullPointerException");
	}


	/**
	 * Partição: Filename ser do tipo errado? 
	 * Opções de resposta: 
	 * Sim
	 * Não
	 */
	@Rule
	public ExpectedException expectedEx3 = ExpectedException.none();

	@Test(expected = org.apache.poi.openxml4j.exceptions.InvalidFormatException.class)
	public void isThrowningExpectionWhenWrongType() throws FileNotFoundException{	
		gerenciador.lerRealizadoMensal("Modelo_Controle_Orcamentario_Completo.csv");
		expectedEx3.expectMessage("Expected exception: org.apache.poi.openxml4j.exceptions.InvalidFormatException");
	}

	

}
