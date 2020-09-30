package br.com.automacao.servicos;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import br.com.automacao.entidades.Locacao;
import br.com.automacao.servicos.Calculadora;
import junit.framework.Assert;

public class CalculadoraMockTest {
	
	@Mock
	private Calculadora calcMock;
	
	@Spy
	private Calculadora calcSpy;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void devoMostrarDiferencaEntreMockSpy() {
		Mockito.when(calcMock.somar(1, 2)).thenReturn(5);
		//Mockito.when(calcSpy.somar(1, 2)).thenReturn(5);
		Mockito.doReturn(5).when(calcSpy).somar(1,2);
		Mockito.doNothing().when(calcSpy).imprime();
		
		System.out.println("Mock:" + calcMock.somar(1, 2));
		System.out.println("Spy:" + calcSpy.somar(1, 2));

		System.out.println("Mock");
		calcMock.imprime();
		System.out.println("Spy");
		calcSpy.imprime();
	}
	
	@Test
	public void testeSomaCalculadoraMock() {
		//O calc se torna um mock da classe de calculadora
		Calculadora calc = Mockito.mock(Calculadora.class);	
		//Definimos um comportamento para o mock, ou seja, quando passamos o parametro 1 e 2 retorna 5
		//sem entrar na implementação do método de soma de fato
			//Mockito.when(calc.somar(1,2)).thenReturn(5);
		//somando valores aleatoros para os parametros deve sempre retonar 5
			//Mockito.when(calc.somar(Mockito.anyInt(),Mockito.anyInt())).thenReturn(5);
		//Definindo o comportameto quando primeiro parametro deve ser fixo em 1 e o segundo qualquer inteiro
		Mockito.when(calc.somar(Mockito.eq(1),Mockito.anyInt())).thenReturn(5);
		Assert.assertEquals(5, calc.somar(1, 2000));
		System.out.println(calc.somar(1, 2));
		System.out.println("Passando valor diferente do definido no mock - retorna valor padrão"
				+ "de acordo com o tipo "+calc.somar(1, 5));		
	}
	
	@Test
	public void testeCalculadoraCapturandoArgumentos() {
		Calculadora calc = Mockito.mock(Calculadora.class);
		ArgumentCaptor<Integer> argCapt = ArgumentCaptor.forClass(Integer.class);
		Mockito.when(calc.somar(argCapt.capture(),argCapt.capture())).thenReturn(5);
		Assert.assertEquals(5, calc.somar(1, 3564));
		System.out.println(argCapt.getAllValues());
	}
}