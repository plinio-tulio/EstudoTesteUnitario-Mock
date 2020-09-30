package br.com.automacao.servicos;

import org.junit.Assert;
import org.junit.Test;

import br.com.automacao.exceptions.NaoPodeDividirPorZeroException;
import br.com.automacao.servicos.Calculadora;

public class CalculadorraTest {

	@Test
	public void deveSomarDoisValores() {
		//cenario
		int a = 5;
		int b = 3;
		Calculadora calc = new Calculadora();
		
		//acao
		int resultado = calc.somar(a,b);
		
		//verificacao
		Assert.assertEquals(8, resultado);
	}
	
	@Test
	public void deveSubtrairDoisValores() {
		//cenario
		int a = 8;
		int b = 5;
		Calculadora calc = new Calculadora();
		
		//acao
		int resultado = calc.subtrair(a,b);
		
		//verificacao
		Assert.assertEquals(3, resultado);
	}
	
	@Test
	public void deveDividirDoisValores() throws NaoPodeDividirPorZeroException {
		//cenario
		int a = 10;
		int b = 5;
		Calculadora calc = new Calculadora();
		
		//acao
		int resultado = calc.divide(a,b);
		
		//verificacao
		Assert.assertEquals(2, resultado);
	}
	
	@Test(expected = NaoPodeDividirPorZeroException.class)
	public void deveLancarEcecaoAoDividirPorZero() throws NaoPodeDividirPorZeroException {
		//cenario
		int a = 10;
		int b = 0;
		Calculadora calc = new Calculadora();
		
		//acao
		int resultado = calc.divide(a,b);
		
		//verificacao
		Assert.assertEquals(2, resultado);
	}
}
