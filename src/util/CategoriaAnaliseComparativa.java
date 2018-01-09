package util;

public enum CategoriaAnaliseComparativa {
	
	CODIGO(0), NOME(1), PREVISAO(2), REALIZADO(3), VARIACAO(4), PORCENTAGEM(5), AVALIACAO(6);
	
	private int numColuna;
	CategoriaAnaliseComparativa(int numColuna){
		this.numColuna = numColuna;
	 }
	    
	public int toInt(){
		return this.numColuna;
	}
}
