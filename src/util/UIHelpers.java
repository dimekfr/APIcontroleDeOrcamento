package util;

import java.time.LocalDate;
import java.util.Calendar;

import dominio.PlanoContas;
import facade.GerenciadorFacade;
import negocios.AgentePrevisao;

/**
 *  Classe responsável por conter os métodos que a UI utiliza, porém somente métodos que não interagem com usuário. Por esse motivo é uma classe de "Helpers" e está no pacote util
 *
 */
public class UIHelpers {
 
	static UIHelpers instance;
	private GerenciadorFacade facade;
	
	public UIHelpers(GerenciadorFacade facade) {
		this.facade = facade;
	}
	
	public static UIHelpers getInstance(GerenciadorFacade facade) {
		if(instance == null){
			instance = new UIHelpers(facade);
		}		
		return(instance);	
	}
	
	private boolean validateDate(int day, int month, int year) {
		
		Calendar calendar = Calendar.getInstance();
		boolean monthIsValid = month <= 12 && month >= 1;
		
		int yearLowerLimit = calendar.get(Calendar.YEAR);
		boolean yearIsValid = year > yearLowerLimit;
		boolean yearIsLeapYear = year % 400 == 0 || (year % 100 != 0 && year % 4 == 0);
		
		int dayUpperLimit, dayLowerLimit=1;
		
		if(month % 2 == 0) dayUpperLimit = 30;
		else dayUpperLimit = 31;
		
		if(month == 2) {
			if(yearIsLeapYear)dayUpperLimit = 29;
			else dayUpperLimit = 28;
		}
		
		boolean dayIsValid = day >= dayLowerLimit && day <= dayUpperLimit; 
		
		return dayIsValid && monthIsValid && yearIsValid;
	}
	
	
	public boolean returnIfIsAValidDate(int day, int month, int year) {
		
		if(validateDate(day, month, year)) {
			PlanoContas planoContas = PlanoContas.getInstance();
			planoContas.setDataCongelamento(LocalDate.of(Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day)));
			return true;
		}
		else {
			System.out.println("Data inválida!\n");
			return false;
		}
	}
	public void	generatePrediction(int predicitionOp, int rubricaCode, int predictionValue, int predictionMonth) {
		facade.geraPrevisao(predicitionOp, rubricaCode, predictionValue, predictionMonth);
	}
	
	
	
}
