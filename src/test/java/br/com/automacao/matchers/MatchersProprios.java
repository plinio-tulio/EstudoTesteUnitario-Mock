package br.com.automacao.matchers;

import java.util.Calendar;

public class MatchersProprios {
	
	public static DiasSemanaMatcher caiEm(Integer diaSemana) {
		return new DiasSemanaMatcher(diaSemana);
	}
	
	public static DiasSemanaMatcher caiNumaSegunda() {
		return new DiasSemanaMatcher(Calendar.MONDAY);
	}
	
	public static DataDiferencaDiasMatcher ehHojeComDiferenciaDias(Integer qtdDias) {
		return new DataDiferencaDiasMatcher(qtdDias);
	}
	
	public static DataDiferencaDiasMatcher ehHoje() {
		return new DataDiferencaDiasMatcher(0);
	}
}
