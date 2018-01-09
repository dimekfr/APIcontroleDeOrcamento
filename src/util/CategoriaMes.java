package util;

public enum CategoriaMes {

	JANEIRO(1, "Janeiro"), FEVEREIRO(2, "Fevereiro"), MARCO(3, "Março"), ABRIL(4, "Abril"), MAIO(5, "Maio"), JUNHO(6, "Junho"),   
    JULHO(7, "Julho"), AGOSTO(8, "Agosto"), SETEMBRO(9, "Setembro"), OUTUBRO(10, "Outubro"), NOVEMBRO(11, "Novembro"), DEZEMBRO(12, "Dezembro");
	
    private int numMes;
    private String stringMes;
    
    CategoriaMes(int numMes, String stringMes){
       this.numMes = numMes;
       this.stringMes = stringMes;
    }
    
    CategoriaMes(int numMes){
    	switch(numMes) {
    	
    	case 1:
    		this.numMes = numMes;
    		this.stringMes = "Janeiro";
			break;
    	case 2:
    		this.numMes = numMes;
    		this.stringMes = "Fevereiro";
			break;
    	case 3:
    		this.numMes = numMes;
    		this.stringMes = "Março";
			break;
    	case 4:
    		this.numMes = numMes;
    		this.stringMes = "Abril";
			break;
    	case 5:
    		this.numMes = numMes;
    		this.stringMes = "Maio";
			break;
    	case 6:
    		this.numMes = numMes;
    		this.stringMes = "Junho";
			break;
    	case 7:
    		this.numMes = numMes;
    		this.stringMes = "Julho";
			break;
    	case 8:
    		this.numMes = numMes;
    		this.stringMes = "Agosto";
			break;
    	case 9:
    		this.numMes = numMes;
    		this.stringMes = "Setembro";
			break;
    	case 10:
    		this.numMes = numMes;
    		this.stringMes = "Outubro";
			break;
    	case 11:
    		this.numMes = numMes;
    		this.stringMes = "Novembro";
			break;
    	case 12:
    		this.numMes = numMes;
    		this.stringMes = "Dezembro";
			break;
		default:
			this.numMes = 0;
			this.stringMes = "";
			break;
	    	
    	}
    }
    
    public static CategoriaMes getMes(int numMes) {
		switch(numMes) {
		    	
		    	case 1:
		    		return JANEIRO;
		    	case 2:
		    		return FEVEREIRO;
		    	case 3:
		    		return MARCO;
		    	case 4:
		    		return ABRIL;
		    	case 5:
		    		return MAIO;
		    	case 6:
		    		return JUNHO;
		    	case 7:
		    		return JULHO;
		    	case 8:
		    		return AGOSTO;
		    	case 9:
		    		return SETEMBRO;
		    	case 10:
		    		return OUTUBRO;
		    	case 11:
		    		return NOVEMBRO;
		    	case 12:
		    		return DEZEMBRO;
				default:
					return null;
			    	
		}
    }
    
    public int toInt(){
       return this.numMes;
    }
    
    public String toString() {
    	return this.stringMes;
    }

}
