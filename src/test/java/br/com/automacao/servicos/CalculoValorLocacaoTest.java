package br.com.automacao.servicos;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.automacao.daos.LocacaoDAO;
import br.com.automacao.entidades.Filme;
import br.com.automacao.entidades.Locacao;
import br.com.automacao.entidades.Usuario;
import br.com.automacao.exceptions.FilmeSemEstoqueException;
import br.com.automacao.exceptions.LocadoraException;
import br.com.automacao.servicos.LocacaoService;
import br.com.automacao.servicos.SPCService;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {
	
	@InjectMocks
	private LocacaoService service;
	
	@Mock
	private LocacaoDAO dao;
	@Mock
	private SPCService spc ;
	
	@Parameter
	public List<Filme> filmes;
	
	@Parameter(value=1)
	public Double valorLocacao;
	
	@Parameter(value=2)
	public String cenario;
	
	@Before
	public void setUp() {	
		MockitoAnnotations.initMocks(this);
	}
	
	private static Filme filme1 = new Filme("Filme 1", 2, 4.0);
	private static Filme filme2 = new Filme("Filme 2", 2, 4.0);
	private static Filme filme3 = new Filme("Filme 3", 3, 4.0);
	private static Filme filme4 = new Filme("Filme 4", 3, 4.0);
	private static Filme filme5 = new Filme("Filme 5", 3, 4.0);
	private static Filme filme6 = new Filme("Filme 6", 3, 4.0);
	private static Filme filme7 = new Filme("Filme 7", 3, 4.0);
	
	@Parameters(name="{2}")
	public static Collection<Object[]> getPatametros(){
		return Arrays.asList(new Object [][] {
			{Arrays.asList(filme1,filme2),8.0, "2 Filmes: Sem desconto"},
			{Arrays.asList(filme1,filme2,filme3),11.0, "3 Filmes: 25%"},
			{Arrays.asList(filme1,filme2,filme3,filme4),13.0, "4 Filmes: 50%"},
			{Arrays.asList(filme1,filme2,filme3, filme4, filme5),14.0, "5 Filmes: 75%"},
			{Arrays.asList(filme1,filme2,filme3, filme4, filme5, filme6),14.0, "6 Filmes: 100%"},
			{Arrays.asList(filme1,filme2,filme3, filme4, filme5, filme6, filme7),18.0, "7 Filmes: Sem desconto"}
		});
	}
	
	@Test
	public void deveCalcularValorLocacaoConsiderandoDescontos() throws FilmeSemEstoqueException, LocadoraException {
		// cenario
		Usuario usuario = new Usuario("Usuario 1");
		// ação	
		Locacao resultado = service.alugarFilme(usuario, filmes);
		//verificacao
		assertThat(resultado.getValor(), is(valorLocacao));	
	}
	
}