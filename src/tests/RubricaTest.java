package tests;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import dominio.PlanoContas;
import dominio.Rubrica;
import facade.GerenciadorFacade;
import util.CategoriaRubrica;

public class RubricaTest {


	private GerenciadorFacade gerenciador;
	private PlanoContas planoContas;
	
	@Before
	public void createPlanoContasAndAgents() throws FileNotFoundException{
		 planoContas = PlanoContas.getInstance();
		 gerenciador = new GerenciadorFacade(planoContas);
		 gerenciador.lerOrcamentoInicial("Modelo_Controle_Orcamentario_Completo.csv");
		 planoContas.setDataCongelamento(LocalDate.now().plusYears(1));
	}
	
	//===================functionality-based tests:	===================//
	/**
	 * Partição:  Subrúbricas estão sendo setadas corretas? 
	 * Opções de resposta: 
	 * Sim
	 * Não
	 */
	//=NAO SEI COMO FAZER FUNCIONAR ESSA PORRA, (será que precisa desse teste? é só um getter -> eu acho q precisa sim pq n to testando o get mas sim a logica tlg, de se a subrubricas tao salvando direitinho)
	@Test
	public void getSubRubricas() {
		String correctAnswer = "[Devolucoes  105  169199.0"+
		                 ", ICMS  106  7749.0" +
		                 ", ISSQN s/ Servicos  107  160131.0" +
		                 ", Pis s/ Faturamento  108  70412.0" +
		                 ", Cofins  109  324972.0" +
		                 ", Remessa de Mercadoria  936  0.0]";
		List<Rubrica> subrubricasDa2396 = null;
		Map<Integer, Rubrica> rubrica =  planoContas.getRubricas();
		for (Map.Entry<Integer, Rubrica> entry : rubrica.entrySet())
		{
			if(entry.getValue().getCodigo() == 2396) {
				subrubricasDa2396 = entry.getValue().getSubRubricas();	
			}
		}
		
		assertEquals(subrubricasDa2396.toString(), correctAnswer.toString());
	}
	
	/**
	 * Partição: Adicionar uma subrúbrica funciona corretamente? 
	 * Opções de resposta: 
	 * Sim
	 * Não
	 */

	@Test
	public void addSubRubricas() {
		Map<Integer, Rubrica> rubricas =  planoContas.getRubricas();
		
		Double[] valoresAnoPassado = {1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0};
		Rubrica subrubricaAdicionada = new Rubrica( rubricas.get(103), "Teste", -1, CategoriaRubrica.RECEITA, valoresAnoPassado);

		List<Rubrica> subrubricasDa103 = null;
		
		for (Map.Entry<Integer, Rubrica> entry : rubricas.entrySet())
		{
			if(entry.getValue().getCodigo() == 103) {
				entry.getValue().addSubRubrica(subrubricaAdicionada);	
				subrubricasDa103 = entry.getValue().getSubRubricas();
			}
		}
		

		assertEquals(subrubricasDa103.contains(subrubricaAdicionada), true);

	}
}
