package negocios;

import java.util.ArrayList;

import dominio.PlanoContas;
import dominio.Rubrica;
import util.CategoriaMes;
import util.CategoriaRubrica;
import static org.junit.Assert.*;

/**
 *  Classe responsável por gerenciar a geração de análise comparativa do
 *  planejando de orçamento e do que foi efetivamente realizada durante um
 *  determinado período em uma empresa.
 * 
 */
public class AgenteAnaliseComparativa extends AgenteAbstract{

	
	public AgenteAnaliseComparativa(PlanoContas plano) {
		super(plano);
	}
	

	public void geraAnaliseComparativa(CategoriaMes mesInicial, CategoriaMes mesFinal) {
		
		int contadorRubrica = 1;
		
		GerenciadorArquivos gerenciadorArquivo = new GerenciadorArquivos();
		gerenciadorArquivo.geraArquivoAnaliseComparativa();
		
        for (int codigoRubrica : getPlanoContas().getRubricas().keySet()) {
        		Rubrica rubrica = getPlanoContas().getRubricas().get(codigoRubrica);
        		gerenciadorArquivo.preencheLinhaAnaliseComparativa(geraValoresRubrica(rubrica, mesInicial, mesFinal), contadorRubrica);
        		contadorRubrica += 1;
        }
		
		gerenciadorArquivo.finalizaArquivoAnaliseComparativa();
	}
	
	/**
	 * gera um array de 7 caracteristicas de uma rubrica
	 * 1- codigo
	 * 2- nome
	 * 3- valor previsto em um intervalo de tempo
	 * 4- valor realizado em um intervalo de tempo
	 * 5- variacao (3-4)
	 * 6- porcentagem (5*100/3)
	 * 7- :) ou :(
	 * @param rubrica
	 * @param mesInicial
	 * @param mesFinal
	 * @return
	 */
	public ArrayList<String> geraValoresRubrica(Rubrica rubrica, CategoriaMes mesInicial, CategoriaMes mesFinal) {
		
		ArrayList<String> valores = new ArrayList<String>();
		Double previstos = 0.0;
		Double realizados = 0.0;
		Double[] previstosERealizados = {0.0, 0.0};
		
		assertNotNull(rubrica.getCodigo());
		assertNotNull(rubrica.getNome());

		valores.add(String.valueOf(rubrica.getCodigo()));
		valores.add(String.valueOf(rubrica.getNome()));
	
		if (PlanoContas.getInstance().getRubricasEspeciais().containsKey(rubrica.getCodigo())) {
			previstosERealizados = getValoresPrevistosRealizadosRubricaEspecial(rubrica.getCodigo(), mesInicial, mesFinal);
			
		}
		else {
			previstosERealizados = iteraESomaValoresRubricas(rubrica, mesInicial, mesFinal);
		}
		previstos = previstosERealizados[0];
		realizados = previstosERealizados[1];
		valores.add(previstos.toString());
		valores.add(realizados.toString());
		Double variacao = previstos - realizados;
		valores.add(String.valueOf(variacao));
		valores.add(calculaPorcentagem(previstos, variacao).toString() + "%");
		valores.add(String.valueOf(geraAvaliacao(rubrica.getCategoria(), variacao)));
	   
		return valores;
		
	}
	/**
	 *  gera um array de 2 valores, o primeiro o valor previsto e o segundo o valor realizado
	 *  ambos referentes a um intervalo de tempo dado pelos paramentros
	 * @param rubrica
	 * @param mesInicial
	 * @param mesFinal
	 * @return
	 */
	public static Double[] iteraESomaValoresRubricas(Rubrica rubrica, CategoriaMes mesInicial, CategoriaMes mesFinal) {
		
		Double somaValoresPrevistos = 0.0;
		Double somaValoresRealizados = 0.0;
		
		for (int mounthCounter = mesInicial.toInt(); mounthCounter <= mesFinal.toInt(); mounthCounter ++) {
			somaValoresPrevistos += rubrica.somaValoresPrevistosSubrubricas(mounthCounter);
			somaValoresRealizados += rubrica.somaValoresRealizadosSubrubricas(mounthCounter);
		}
		return new Double[] {somaValoresPrevistos, somaValoresRealizados};

	}
	/**
	 * gera um array de 2 valores, o primeiro o valor previsto e o segundo o valor realizado
	 * ambos referentes a uma rubrica especial
	 * faz um parser da fórmula da rubrica especial
	 * caso 1: a rubrica depende de apenas uma outra
	 * caso 2: a rubrica depende do inverso de outra
	 * caso 3: a rubrica depende da soma de varias outras ou depende da subtracao de varias outras
	 * @param RubricaCode
	 * @param mesInicial
	 * @param mesFinal
	 * @return
	 */
	
	public static Double[] getValoresPrevistosRealizadosRubricaEspecial(int RubricaCode, CategoriaMes mesInicial, CategoriaMes mesFinal) {
		String formula = PlanoContas.getInstance().getRubricasEspeciais().get(RubricaCode);
		
	
		String[] formulaArray = formula.split(" ");
		
		Double acumuladorPrevistos = 0.0;
		Double acumuladorRealizados = 0.0;
		
		
		//se o comprimento é um, é porque a fórmula é uma rubrica; sem operacao
		
		if (formulaArray.length == 1) {
			
			Rubrica rubrica =  PlanoContas.getInstance().getRubricas().get(Integer.valueOf(formulaArray[0]));
			return iteraESomaValoresRubricas(rubrica, mesInicial, mesFinal);
		}
		
		//se é dois, é porque é -x. sim, sempre.
		if (formulaArray.length == 2) {
			Rubrica rubrica =  PlanoContas.getInstance().getRubricas().get(Integer.valueOf(formulaArray[1]));
			Double[] result = iteraESomaValoresRubricas(rubrica, mesInicial, mesFinal);
			
			for (Double element : result) {
				element *= -1;
			}
			return result;
		}
		//se é maior. é pq é ou x + x + ... ou x - x - x... nunca terá - e + misturados.
	
		if (formulaArray[1].equals("+")) {

			for (int cont=0; cont < formulaArray.length; cont += 2) {
				Rubrica rubrica =  PlanoContas.getInstance().getRubricas().get(Integer.valueOf(formulaArray[cont]));
				
				
				Double[] result = iteraESomaValoresRubricas(rubrica, mesInicial, mesFinal);
				acumuladorPrevistos += result[0];
				acumuladorRealizados += result[1];
			}
			return new Double[] {acumuladorPrevistos, acumuladorRealizados};
		}
		else {
			
			for (int cont = 0; cont < formulaArray.length; cont += 2) {
				Rubrica rubrica =  PlanoContas.getInstance().getRubricas().get(Integer.valueOf(formulaArray[cont]));
				Double[] result = iteraESomaValoresRubricas(rubrica, mesInicial, mesFinal);
				if (cont == 0) {
					acumuladorPrevistos = result[0];
					acumuladorRealizados = result[1];
				}
				else {
					acumuladorPrevistos -= result[0];
					acumuladorRealizados -= result[1];
				}
			}
			return new Double[] {acumuladorPrevistos, acumuladorRealizados};
			
		}
	}
	/**
	 * dada a categoria que uma rubrica pertence, avaliar a variacao obtida em um intervalo de tempo 
	 * atribui carinha feliz ou triste
	 * @param categoriaRubrica
	 * @param valorVariacao
	 * @return
	 */

	public static String geraAvaliacao (CategoriaRubrica categoriaRubrica, Double valorVariacao) {
		switch (categoriaRubrica) {
		case DESPESA:
			if (valorVariacao < 0) {
				return ":(";
			}
			else {
				return ":)";
			}
		case RECEITA:
			if (valorVariacao <= 0) {
				return ":)";
			}
			else {
				return ":(";
			} 
		}
		return ":/";
	}
	
	/**
	 * calcula a porcentagem a partir de um valor previto e da variacao
	 * @param valorPrevisto
	 * @param variacao
	 * @return
	 */
	private static Double calculaPorcentagem (Double valorPrevisto, Double variacao) {
	
		if (valorPrevisto != 0.0) {
			Double porcentagem = variacao * 100 / valorPrevisto;
			return porcentagem;
		}
		else {
			return 0.0;
		}
			
	}
	
	
}
