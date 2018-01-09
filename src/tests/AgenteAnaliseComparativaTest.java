package tests;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import dominio.PlanoContas;
import dominio.Rubrica;
import facade.GerenciadorFacade;
import negocios.AgenteAnaliseComparativa;
import util.CategoriaMes;
import util.CategoriaRubrica;

public class AgenteAnaliseComparativaTest {

	Double PREV_VALUE = 100.0;
	Double REL_VALUE = 200.0;
	
	private GerenciadorFacade gerenciador;
	private PlanoContas planoContas;
	private AgenteAnaliseComparativa analiseComp;

	@Before
	public void createPlanoContasAndAgents() throws FileNotFoundException {
		planoContas = PlanoContas.getInstance();
		gerenciador = new GerenciadorFacade(planoContas);
		gerenciador.lerOrcamentoInicial("Modelo_Controle_Orcamentario_Completo.csv");
		planoContas.setDataCongelamento(LocalDate.now().plusYears(1));
		analiseComp = new AgenteAnaliseComparativa(planoContas);
	}
	
	// ============================================interface-based tests:==============================================

	/**
	 * Domínio:Todas possíveis sáidas do método geraValoresRubrica() do AgenteAnáliseComparativa, jutamente com todas rúbricas
	 * na empresa.
	 * Partição: Valor para prever ser nulo no código da rubrica, gera erro(rubrica
	 * filha como dominio)? Opções de resposta: Sim Não
	 */
	@Rule
	public ExpectedException expectedEx1 = ExpectedException.none();

	@Test(expected = java.lang.NullPointerException.class)
	public void geraValoresRubricaSemCodigoNaFilha() {
		Rubrica mae = new Rubrica(null, "mae", 0, CategoriaRubrica.DESPESA, null);
		Rubrica filha = new Rubrica(null, "filha", (Integer) null, CategoriaRubrica.DESPESA, null);
		mae.addSubRubrica(filha);
		filha.setValorPrevisto(0, 100);
		filha.setValorRealizado(0, 200);
		filha.setValorPrevisto(1, 100);
		filha.setValorRealizado(1, 200);
		ArrayList<String> valores = analiseComp.geraValoresRubrica(mae, CategoriaMes.JANEIRO, CategoriaMes.FEVEREIRO);
		expectedEx1.expectMessage("Expected exception:  java.lang.NullPointerException");

	}

	/**
	 * Domínio: Todas possíveis sáidas do método geraValoresRubrica() do AgenteAnáliseComparativa, jutamente com todas rúbricas
	 * na empresa.
	 *  Partição: Valor para prever ser nulo no código da rubrica, gera erro(rubrica
	 * mae como dominio)? Opções de resposta: Sim Não
	 */
	@Rule
	public ExpectedException expectedEx2 = ExpectedException.none();

	@Test(expected = java.lang.NullPointerException.class)
	public void geraValoresRubricaSemCodigoNaMae() {
		Rubrica mae = new Rubrica(null, "mae", 0, CategoriaRubrica.DESPESA, null);
		Rubrica filha = new Rubrica(null, "filha", (Integer) null, CategoriaRubrica.DESPESA, null);
		mae.addSubRubrica(filha);
		filha.setValorPrevisto(0, 100);
		filha.setValorRealizado(0, 200);
		filha.setValorPrevisto(1, 100);
		filha.setValorRealizado(1, 200);
		ArrayList<String> valores = analiseComp.geraValoresRubrica(mae, CategoriaMes.JANEIRO, CategoriaMes.FEVEREIRO);
		expectedEx2.expectMessage("Expected exception:  java.lang.NullPointerException");

	}

	/**
	 * Partição: Sem Valor previsto é devolvido zero? Opções de resposta: Sim Não
	 */
	@Test
	public void geraValoresRubricaSemValorPrevisto() {
		Rubrica mae = new Rubrica(null, "mae", 0, CategoriaRubrica.DESPESA, null);
		Rubrica filha = new Rubrica(null, "filha", 1, CategoriaRubrica.DESPESA, null);
		mae.addSubRubrica(filha);
		ArrayList<String> valoresEsperados = new ArrayList<String>(
				Arrays.asList("0", "mae", "0.0", "0.0", "0.0", "0.0%", ":)"));
		ArrayList<String> valores = analiseComp.geraValoresRubrica(mae, CategoriaMes.JANEIRO, CategoriaMes.FEVEREIRO);
		assertEquals(valoresEsperados, valores);

	}

	// ===================functionality-based tests: ===================//

	/**
	 * Domínio: Todas possíveis saídas do método geraValoresRubrica() do AgenteAnáliseComparativa, juntamente com todas rúbricas
	 * na empresa.
	 * Partição: Valor para prever ser nulo no código da rubrica, gera erro(rubrica
	 * filha como dominio)? Opções de resposta: Sim Não
	 */
	@Test
	public void geraValoresRubricaComSubrubrica() {
		ArrayList<String> correctAnswer = new ArrayList<String>(Arrays.asList("0", "mae", "200.0", "400.0", "-200.0", "-100.0%", ":("));
		Rubrica mae = new Rubrica(null, "mae", 0, CategoriaRubrica.DESPESA, null);
		Rubrica filha = new Rubrica(null, "filha", 1, CategoriaRubrica.DESPESA, null);
		mae.addSubRubrica(filha);
		filha.setValorPrevisto(1, 100);
		filha.setValorRealizado(1, 200);
		filha.setValorPrevisto(2, 100);
		filha.setValorRealizado(2, 200);
		ArrayList<String> valores = analiseComp.geraValoresRubrica(mae, CategoriaMes.JANEIRO, CategoriaMes.FEVEREIRO);
		assertEquals(correctAnswer, valores);

	}

	
	/**
	 * Domínio:  Todas possíveis saídas do método método geraValoresRubrica() do AgenteAnáliseComparativa, jutamente com todas rúbricas
	 * na empresa.
	 * Partição: Rúbrica sem subrubrica no método geraValoresRubrica? Opções de
	 * resposta: Sim Não
	 */
	@Test
	public void geraValoresRubricaSemSubrubrica() {
		ArrayList<String> correctAnswer = new ArrayList<String>(
				Arrays.asList("100", "mae", "200.0", "400.0", "-200.0", "-100.0%", ":("));
		Rubrica mae = new Rubrica(null, "mae", 100, CategoriaRubrica.DESPESA, null);
		mae.setValorPrevisto(1, 100);
		mae.setValorRealizado(1, 200);
		mae.setValorPrevisto(2, 100);
		mae.setValorRealizado(2, 200);
		ArrayList<String> valores = analiseComp.geraValoresRubrica(mae, CategoriaMes.JANEIRO, CategoriaMes.FEVEREIRO);
		assertEquals(correctAnswer, valores);

	}

	/**
	 * Domínio:  Todas possíveis saídas do método método geraValoresRubrica() do AgenteAnáliseComparativa, jutamente com todas rúbricas
	 * na empresa.
	 *  * Partição: Rúbrica sem com valoresAnoPassado não nulo funciona corretamente?
	 * Opções de resposta: Sim Não
	 */
	@Test
	public void geraValoresRubricaComValoresAnoPassado() {
		ArrayList<String> correctAnswer = new ArrayList<String>(
				Arrays.asList("100", "mae", "200.0", "400.0", "-200.0", "-100.0%", ":("));
		Double[] valoresAnoPassado = { 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0 };
		Rubrica mae = new Rubrica(null, "mae", 100, CategoriaRubrica.DESPESA, valoresAnoPassado);
		mae.setValorPrevisto(1, 100);
		mae.setValorRealizado(1, 200);
		mae.setValorPrevisto(2, 100);
		mae.setValorRealizado(2, 200);
		ArrayList<String> valores = analiseComp.geraValoresRubrica(mae, CategoriaMes.JANEIRO, CategoriaMes.FEVEREIRO);
		System.out.println(valores);
		System.out.println(correctAnswer);
		assertEquals(correctAnswer, valores);

	}
	/**
	 * Domínio:  Todas possíveis saídas do método  somaValoresPrevistosSubrubricas() do AgenteAnáliseComparativa, jutamente com todas rúbricas
	 * na empresa.
	 *Partição: soma retorna resultado correto quando nao tem subricas e valor
	 * previsto? Opções de resposta: Sim Não
	 */

	@Test
	public void somaValoresPrevistosSubrubricasSemSubrubricasSemValorPrevisto() {
		ArrayList<String> correctAnswer = new ArrayList<String>(
				Arrays.asList("1", "mae", "200.0", "400.0", "-200.0", "-100.0%", ":("));
		Double[] valoresAnoPassado = { 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0 };
		Rubrica mae = new Rubrica(null, "mae", 1, CategoriaRubrica.DESPESA, valoresAnoPassado);
		mae.setValorRealizado(0, 200);
		mae.setValorRealizado(1, 200);
		Double[] valores = AgenteAnaliseComparativa.iteraESomaValoresRubricas(mae, CategoriaMes.JANEIRO, CategoriaMes.FEVEREIRO);
		assertEquals(0.0, valores[0], 0.0000000001);
	}
	/**
	 * Domínio:  Todas possíveis saídas do método  somaValoresPrevistosSubrubricas() do AgenteAnáliseComparativa, jutamente com todas rúbricas
	 * na empresa.
	 * 	Partição: Soma retorna resultado correto quando nao tem subricas, mas tem
	 * valor previsto? Opções de resposta: Sim Não
	 */

	@Test
	public void somaValoresPrevistosSubrubricasSemSubrubricasComValorPrevisto() {
		ArrayList<String> correctAnswer = new ArrayList<String>(
				Arrays.asList("1", "mae", "200.0", "400.0", "-200.0", "-100.0%", ":("));
		Double[] valoresAnoPassado = { 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0 };
		Rubrica mae = new Rubrica(null, "mae", 1, CategoriaRubrica.DESPESA, valoresAnoPassado);
		mae.setValorPrevisto(1, 100);
		mae.setValorRealizado(1, 200);
		mae.setValorPrevisto(6, 100);
		mae.setValorRealizado(6, 200);
		Double[] valores = AgenteAnaliseComparativa.iteraESomaValoresRubricas(mae, CategoriaMes.JANEIRO, CategoriaMes.FEVEREIRO);
		assertEquals(100.0, valores[0], 0.0000000001);
	}
	/**
	 * Domínio:  Todas possíveis saídas do método  somaValoresPrevistosSubrubricas() do AgenteAnáliseComparativa, jutamente com todas rúbricas
	 * na empresa.
	 * 	Partição: Soma retorna resultado correto quando  tem subricas e  tem
	 * valor previsto? Opções de resposta: Sim Não
	 */

	@Test
	public void somaValoresPrevistosSubrubricasComSubrubricasComValorPrevisto() {
		Rubrica mae = new Rubrica(null, "mae", 0, CategoriaRubrica.DESPESA, null);
		Rubrica filha = new Rubrica(null, "filha", 1, CategoriaRubrica.DESPESA, null);
		mae.addSubRubrica(filha);
		filha.setValorPrevisto(1, 100);
		filha.setValorRealizado(0, 200);
		filha.setValorPrevisto(2, 300);
		filha.setValorRealizado(1, 200);
		Double[] valores = AgenteAnaliseComparativa.iteraESomaValoresRubricas(mae, CategoriaMes.JANEIRO, CategoriaMes.MARCO);
		assertEquals(400.0, valores[0], 0.0000000001);
	}
	/**
	 * Domínio:  Todas possíveis saídas do método  somaValoresPrevistosSubrubricas() do AgenteAnáliseComparativa, jutamente com todas rúbricas
	 * na empresa.
	 * 	Partição: Soma retorna resultado correto quando  tem subricas, mas não tem
	 * valor previsto? Opções de resposta: Sim Não
	 */

	@Test
	public void somaValoresPrevistosSubrubricasComSubrubricassemValorPrevisto() {
		Rubrica mae = new Rubrica(null, "mae", 0, CategoriaRubrica.DESPESA, null);
		Rubrica filha = new Rubrica(null, "filha", 1, CategoriaRubrica.DESPESA, null);
		mae.addSubRubrica(filha);
		filha.setValorRealizado(0, 200);
		filha.setValorRealizado(1, 200);
		Double[] valores = AgenteAnaliseComparativa.iteraESomaValoresRubricas(mae, CategoriaMes.JANEIRO, CategoriaMes.FEVEREIRO);
		assertEquals(0.0, valores[0], 0.0000000001);
	}
	/**
	 * Domínio:  Todas possíveis saídas do método somaValoresRealizadosSubrubricas() do AgenteAnáliseComparativa, jutamente com todas rúbricas
	 * na empresa.
	 * Partição: Soma retorna resultado correto quando nao tem subricas e valor
	 * realizado? Opções de resposta: Sim Não
	 */
	@Test
	public void somaValoresRealizadosSubrubricasSemSubrubricasSemValorPrevisto() {
		ArrayList<String> correctAnswer = new ArrayList<String>(
				Arrays.asList("1", "mae", "200.0", "400.0", "-200.0", "-100.0%", ":("));
		Double[] valoresAnoPassado = { 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0 };
		Rubrica mae = new Rubrica(null, "mae", 1, CategoriaRubrica.DESPESA, valoresAnoPassado);
		mae.setValorPrevisto(0, 100);
		mae.setValorPrevisto(1, 100);
		Double[] valores = AgenteAnaliseComparativa.iteraESomaValoresRubricas(mae, CategoriaMes.JANEIRO, CategoriaMes.FEVEREIRO);
		assertEquals(0.0, valores[1], 0.0000000001);
	}

	/**
	 * Domínio:  Todas possíveis saídas do método somaValoresRealizadosSubrubricas() do AgenteAnáliseComparativa, jutamente com todas rúbricas
	 * na empresa.
	 * Partição: Soma retorna resultado correto quando nao tem subricas e tem valor
	 * realizado? Opções de resposta: Sim Não
	 */
	@Test
	public void somaValoresRealizadosSubrubricasSemSubrubricasComValorPrevisto() {
		ArrayList<String> correctAnswer = new ArrayList<String>(
				Arrays.asList("100", "mae", "200.0", "400.0", "-200.0", "-100.0%", ":("));
		Double[] valoresAnoPassado = { 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0 };
		Rubrica mae = new Rubrica(null, "mae", 100, CategoriaRubrica.DESPESA, valoresAnoPassado);
		mae.setValorRealizado(1, 200);
		mae.setValorRealizado(2, 200);
		Double[] valores = AgenteAnaliseComparativa.iteraESomaValoresRubricas(mae, CategoriaMes.JANEIRO, CategoriaMes.FEVEREIRO);
		assertEquals(400.0, valores[1], 0.0000000001);
	
	}

	/**
	 * Domínio:  Todas possíveis saídas do método somaValoresRealizadosSubrubricas() do AgenteAnáliseComparativa, jutamente com todas rúbricas
	 * na empresa.
	 * Partição: Soma retorna resultado correto quando tem subricas e tem valor
	 * realizado? Opções de resposta: Sim Não
	 */
	@Test
	public void somaValoresRealizadosSubrubricasComSubrubricasComValorPrevisto() {
		Rubrica mae = new Rubrica(null, "mae", 0, CategoriaRubrica.DESPESA, null);
		Rubrica filha = new Rubrica(null, "filha", 1, CategoriaRubrica.DESPESA, null);
		mae.addSubRubrica(filha);
		filha.setValorRealizado(1, 200);
		filha.setValorRealizado(2, 200);
		Double[] valores = AgenteAnaliseComparativa.iteraESomaValoresRubricas(mae, CategoriaMes.JANEIRO, CategoriaMes.FEVEREIRO);
		assertEquals(400.0, valores[1], 0.0000000001);
	}

	/**
	 * Domínio:  Todas possíveis saídas do método somaValoresRealizadosSubrubricas() do AgenteAnáliseComparativa, jutamente com todas rúbricas
	 * na empresa.
	 * Partição: Soma retorna resultado correto quando  tem subricas e não tem valor
	 * realizado? Opções de resposta: Sim Não
	 */
	@Test
	public void somaValoresRealizadosSubrubricasComSubrubricassemValorPrevisto() {
		Rubrica mae = new Rubrica(null, "mae", 0, CategoriaRubrica.DESPESA, null);
		Rubrica filha = new Rubrica(null, "filha", 1, CategoriaRubrica.DESPESA, null);
		mae.addSubRubrica(filha);
		Double[] valores = AgenteAnaliseComparativa.iteraESomaValoresRubricas(mae, CategoriaMes.JANEIRO, CategoriaMes.FEVEREIRO);
		assertEquals(0.0, valores[1], 0.0000000001);
	}

	/**
	 * Domínio:  Todas possíveis saídas do método geraAvaliacao() do AgenteAnáliseComparativa, jutamente com todas rúbricas e valores ja estimados da comparação
	 * na empresa.
	 */
	
	// CATEGORIA DESPESA
	
	/*
	 * Partição: Cara negativa é correta para despesas com variação negativa? Opções
	 * de resposta: Sim Não
	 */
	@Test
	public void geraAvaliacaoDespesaVaricaoNegativa() {
		String resultado = analiseComp.geraAvaliacao(CategoriaRubrica.DESPESA, -10.0);
		assertEquals(":(", resultado);
	}
	/*
	 * Partição: Cara positiva é correta para despesas com variação positiva? Opções
	 * de resposta: Sim Não
	 */
	@Test
	public void geraAvaliacaoDespesaVaricaoPositiva() {
		String resultado = analiseComp.geraAvaliacao(CategoriaRubrica.DESPESA, 1000.0);
		assertEquals(":)", resultado);
	}
	/*
	 * Partição: Cara positiva é correta para despesas com boundary values? Opções
	 * de resposta: Sim Não
	 */

	// teste no valor limite para cara postiva!
	@Test
	public void geraAvaliacaoDespesaVaricaoPositivaaBoundaryValues() {
		String resultado = analiseComp.geraAvaliacao(CategoriaRubrica.DESPESA, 0.0);
		assertEquals(":)", resultado);
	}
	// teste no valor limite para cara negativa!
	@Test
	public void geraAvaliacaoDespesaVaricaoNegativaBoundaryValues() {
		String resultado = analiseComp.geraAvaliacao(CategoriaRubrica.DESPESA, -0.000000001);
		assertEquals(":(", resultado);
	}
	// CATEGORIA RECEITA
	/*
	 * Partição: Cara positiva é correta para receitas com variação negativa? Opções
	 * de resposta: Sim Não
	 */
	@Test
	public void geraAvaliacaoReceitaVaricaoNegativa() {
		String resultado = analiseComp.geraAvaliacao(CategoriaRubrica.RECEITA, -10.0);
		assertEquals(":)", resultado);
	}
	/*
	 * Partição: Cara negativa é correta para receitas com variação positiva? Opções
	 * de resposta: Sim Não
	 */
	@Test
	public void geraAvaliacaoReceitaVaricaoPositiva() {
		String resultado = analiseComp.geraAvaliacao(CategoriaRubrica.RECEITA, 1000.0);
		assertEquals(":(", resultado);
	}
	/*
	 * Partição: Cara negativa é correta para receitas com boundary values? Opções
	 * de resposta: Sim Não
	 */

	// teste no valor limite para cara negativa!
	@Test
	public void geraAvaliacaoReceitaVaricaoPositivaaBoundaryValues() {
		String resultado = analiseComp.geraAvaliacao(CategoriaRubrica.RECEITA, 0.00000001);
		assertEquals(":(", resultado);
	}
	// teste no valor limite para cara positiva!
	@Test
	public void geraAvaliacaoReceitaVaricaoNegativaBoundaryValues() {
		String resultado = analiseComp.geraAvaliacao(CategoriaRubrica.RECEITA, -0.000000001);
		assertEquals(":)", resultado);
	}
	
	/**
	 * metodo que testa o metodo 'geraValoresRubrica' para a rubrica 17, que é especial
	 */

	@Test
	public void calculaValoresDeRubricaEspecial17() {
		//formula da 17 = -187
		//187 = 187 - 193
		//193 = 193
		
		Rubrica rubricaCode17 = planoContas.getRubricas().get(17);
		Rubrica rubricaCode187 = planoContas.getRubricas().get(187);
		Rubrica rubricaCode193 = planoContas.getRubricas().get(193);
		
		setaValoresPrevistosERealizadosSubrubricas(193);
		setaValoresPrevistosERealizadosSubrubricas(187);
			
		ArrayList<String> correctAnswer193 = new ArrayList<String>(
				Arrays.asList("193", "Receitas Financeiras", "800.0", "1600.0", "-800.0", "-100.0" + "%", ":("));
		ArrayList<String> obtido193 = analiseComp.geraValoresRubrica(rubricaCode193, CategoriaMes.JANEIRO, CategoriaMes.FEVEREIRO);
		assertEquals(correctAnswer193, obtido193);
		
		assertEquals(13, planoContas.getRubricas().get(187).getSubRubricas().size());
		
		ArrayList<String> correctAnswer187 = new ArrayList<String>(
				Arrays.asList("187", "Resultado Financeiro", "1800.0", "3600.0", "-1800.0", "-100.0" + "%", ":("));
		
		ArrayList<String> obtido187 = analiseComp.geraValoresRubrica(rubricaCode187, CategoriaMes.JANEIRO, CategoriaMes.FEVEREIRO);
		assertEquals(correctAnswer187, obtido187);
		
		ArrayList<String> correctAnswer17 = new ArrayList<String>(
				Arrays.asList("17", "Resultado Financeiro", "2600.0", "5200.0", "-2600.0", "-100.0" + "%", ":("));
		
		ArrayList<String> obtido17 = analiseComp.geraValoresRubrica(rubricaCode17, CategoriaMes.JANEIRO, CategoriaMes.FEVEREIRO);
		assertEquals(correctAnswer17, obtido17);
		
		
			
	}
	/**
	 * metodo que testa o metodo 'geraValoresRubrica' para a rubrica 110, que é especial
	 */
	@Test
	public void testaRubrica110() {
		//110 = 2 + 120
		//2 tem 9 filhos
		//120 tem 13 filhos
		Rubrica rubrica110 = planoContas.getRubricas().get(110);
		Rubrica rubrica2 = planoContas.getRubricas().get(2);
		Rubrica rubrica120 = planoContas.getRubricas().get(120);
		
		assertEquals(9, rubrica2.getSubRubricas().size());
		assertEquals(13, rubrica120.getSubRubricas().size());

		
		setaValoresPrevistosERealizadosSubrubricas(2);
		setaValoresPrevistosERealizadosSubrubricas(120);
		
		assertEquals(9, rubrica2.getSubRubricas().size());
		
		ArrayList<String> correctAnswer2 = new ArrayList<String>(
				Arrays.asList("2", "Custos operacionais", "1800.0", "3600.0", "-1800.0", "-100.0" + "%", ":("));
		
		ArrayList<String> obtido2 = analiseComp.geraValoresRubrica(rubrica2, CategoriaMes.JANEIRO, CategoriaMes.FEVEREIRO);
		assertEquals(correctAnswer2, obtido2);
		
		ArrayList<String> correctAnswer120 = new ArrayList<String>(
				Arrays.asList("120", "Custos com Pessoal", "2600.0", "5200.0", "-2600.0", "-100.0" + "%", ":("));
		
		ArrayList<String> obtido120 = analiseComp.geraValoresRubrica(rubrica120, CategoriaMes.JANEIRO, CategoriaMes.FEVEREIRO);
		assertEquals(correctAnswer120, obtido120);
		
		ArrayList<String> correctAnswer110 = new ArrayList<String>(
				Arrays.asList("110", "( - ) Custo dos produtos/servi�os vendidos", "4400.0", "8800.0", "-4400.0", "-100.0" + "%", ":("));
		
		ArrayList<String> obtido110 = analiseComp.geraValoresRubrica(rubrica110, CategoriaMes.JANEIRO, CategoriaMes.FEVEREIRO);
		assertEquals(correctAnswer110, obtido110);
		
		
	}
	/**
	 * metodo que testa o metodo 'geraValoresRubrica' para a rubrica 1, que é especial
	 */
	@Test
	public void calculaValoresDeRubricaEspecial1() {
		//formula da 1 = 103 - 2396
		
		Rubrica rubricaCode1 = planoContas.getRubricas().get(1);
		Rubrica rubricaCode2396 = planoContas.getRubricas().get(2396);
		Rubrica rubricaCode103 = planoContas.getRubricas().get(103);
		
		this.setaValoresPrevistosERealizadosSubrubricas(103);
		this.setaValoresPrevistosERealizadosSubrubricas(2396);
	
		ArrayList<String> correctAnswer103 = new ArrayList<String>(
				Arrays.asList("103", "Receita Bruta", "400.0", "800.0", "-400.0", "-100.0" + "%", ":("));
		
		
		ArrayList<String> correctAnswer2396 = new ArrayList<String>(
				Arrays.asList( "2396", "( - ) Dedu��es", "1200.0", "2400.0", "-1200.0", "-100.0%", ":("));
		
		
		ArrayList<String> obtido103 = analiseComp.geraValoresRubrica(rubricaCode103, CategoriaMes.JANEIRO, CategoriaMes.FEVEREIRO);
		assertEquals(correctAnswer103, obtido103);
		
		
		ArrayList<String> obtido2396 = analiseComp.geraValoresRubrica(rubricaCode2396, CategoriaMes.JANEIRO, CategoriaMes.FEVEREIRO);
		assertEquals(correctAnswer2396, obtido2396);
		
		
	
		ArrayList<String> correctAnswer1 = new ArrayList<String>(
				Arrays.asList("1", "Receita L�quida de Vendas", "-800.0", "-1600.0", "800.0", "-100.0" + "%", ":)"));
		ArrayList<String> obtido1 = analiseComp.geraValoresRubrica(rubricaCode1, CategoriaMes.JANEIRO, CategoriaMes.FEVEREIRO);

		
		assertEquals(correctAnswer1, obtido1);
			
	}
	/**
	 * Método seta, para todas as rubricas filhas da entregue como paramentro:
	 * valor previsto = 100
	 * valor realizado = 200
	 * ambas as atribuicoes sao para os meses janeiro e fevereiro
	 * é chamada pelos testadores de rubrica especial
	 * @param RubricaCode
	 */
	public void setaValoresPrevistosERealizadosSubrubricas(int RubricaCode) {
		for (Rubrica filha : PlanoContas.getInstance().getRubricas().get(RubricaCode).getSubRubricas()) {
			filha.setValorPrevisto(1, 100);
			filha.setValorPrevisto(2, 100);
			filha.setValorRealizado(1, 200);
			filha.setValorRealizado(2, 200);
		}
	}

	
	
	
	
}
