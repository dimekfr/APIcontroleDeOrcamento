package negocios;

import java.io.FileNotFoundException;
import java.util.LinkedHashMap;

import dominio.PlanoContas;
import dominio.Rubrica;

/**
 *  Classe responsável por gerenciar a leitura do plano orçamentário do ano anterior da empresa.
 * 
 */
public class AgenteOrcamentoInicial extends AgenteAbstract {

	
	public AgenteOrcamentoInicial(PlanoContas plano) {
		super(plano);
	}
	
	/**
	 * Lê o arquivo CSV com o Orçamento do ano passado da empresa.
	 * @param filename Arquivo csv
	 * @throws FileNotFoundException
	 */
	public void lerOrcamentoAnterior(String filename) throws FileNotFoundException{
		
		GerenciadorArquivos ler = new GerenciadorArquivos();
		LinkedHashMap<Integer, Rubrica> rubricasIniciais = ler.lerOrcamentoInicial(filename);
		
		getPlanoContas().setRubricas(rubricasIniciais);

	}
	
}