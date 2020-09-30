package br.com.automacao.servicos;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.com.automacao.builders.FilmeBuilder;
import br.com.automacao.builders.LocacaoBuilder;
import br.com.automacao.builders.UsuarioBuilder;
import br.com.automacao.daos.LocacaoDAO;
import br.com.automacao.daos.LocacaoDAOFake;
import br.com.automacao.entidades.Filme;
import br.com.automacao.entidades.Locacao;
import br.com.automacao.entidades.Usuario;
import br.com.automacao.exceptions.FilmeSemEstoqueException;
import br.com.automacao.exceptions.LocadoraException;
import br.com.automacao.matchers.MatchersProprios;
import br.com.automacao.servicos.EmailService;
import br.com.automacao.servicos.LocacaoService;
import br.com.automacao.servicos.SPCService;
import br.com.automacao.utils.DataUtils;
import javafx.beans.binding.When;

public class LocacaoServiceTest {

	@InjectMocks
	private LocacaoService service;
	@Mock
	private SPCService spc;
	@Mock
	private LocacaoDAO dao;
	@Mock
	private EmailService email;
	private int contador = 1 ;
	
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Before
	public void setUp() {
		System.out.println("Before= Executado antes cada teste, após o BeforeClass");
		MockitoAnnotations.initMocks(this);
		
		//Mockito.mock(LocacaoDAO.class);
		System.out.println("Contador:"+ contador + "As variaveis são sempre destruidas a cada novo teste"
				+ ", que é o recomendável, caso precise reutilizar usar variável estática");
		contador++;
	}
	
	@After
	public void tearDown() {
		System.out.println("Before= Executado após cada teste executado");
	}
	
	@BeforeClass
	public static void setUpClass() {
		System.out.println("Before Class= Executado antes de todos os testes");
	}
	
	@AfterClass
	public static void tearDownClass() {
		System.out.println("After Class");
	}
	
	@Test
	public void testeLocacao() throws Exception {
		
		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		// Cenário
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 5.0));

		// ação
		Locacao locacao = service.alugarFilme(usuario, filmes);

		// validação Junit
		assertEquals(5.0, locacao.getValor(), 0.01);
		//assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
		//assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));

		// Hamcrast
		assertThat(locacao.getValor(), is(5.0));
		assertThat(locacao.getValor(), is(equalTo(5.0)));
		assertThat(locacao.getValor(), is(CoreMatchers.not(6.0))); // pode fazer o importe estatico para facilitar a
																	// leitura
		assertThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		assertThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(true));
		assertThat(locacao.getValor(), is(equalTo(5.0)));
		assertThat(locacao.getValor(), is(CoreMatchers.not(6.0))); // pode fazer o importe estatico para facilitar a
																	// leitura

		// ErrorCollector Desta forma consegue capturar as duas falhas simultaneamente
		// na verificação
		error.checkThat(locacao.getValor(), is(equalTo(5.0)));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)),
				is(true));

	}
	
	@Test
	public void deveAlugaFilme() throws Exception {	
		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		// Cenário
		//Usuario usuario = new Usuario("Usuario 1");
		Usuario usuario = UsuarioBuilder.umUsuario().agora();

		//List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 5.0));
		List<Filme> filmes =  Arrays.asList(FilmeBuilder.umFilme().agora());

		// ação
		Locacao locacao = service.alugarFilme(usuario, filmes);

		// validação Junit
		// ErrorCollector Desta forma consegue capturar as duas falhas simultaneamente
		error.checkThat(locacao.getValor(), is(equalTo(4.0)));
		error.checkThat(locacao.getDataLocacao(), MatchersProprios.ehHoje());
		error.checkThat(locacao.getDataRetorno(), MatchersProprios.ehHojeComDiferenciaDias(1));
	}
	
	@Test(expected = FilmeSemEstoqueException.class)
	public void testeLocacao_filmeSemEstoque() throws Exception {
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 0, 5.0));
		// ação
		Locacao locacao = service.alugarFilme(usuario, filmes);
	}

	@Test
	public void testeLocacao_filmeSemEstoque02() throws LocadoraException {
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 0, 5.0));
		// ação
			try {
				Locacao locacao = service.alugarFilme(usuario, filmes);
				Assert.fail("Deveria ter lancado uma excecao");
			} catch (FilmeSemEstoqueException e) {
				Assert.assertEquals(e.getClass(), FilmeSemEstoqueException.class);
			}
			
	}

	@Test(expected = FilmeSemEstoqueException.class)
	public void testeLocacao_filmeSemEstoque03() throws Exception {
		// cenario
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 0, 5.0));
		/*exception.expect(Exception.class);
		exception.expectMessage("Filme sem estoque");*/
		// ação
		Locacao locacao = service.alugarFilme(usuario, filmes);
	}

	@Test
	public void testeLocacao_usuarioVazio() throws FilmeSemEstoqueException {
		// cenario
		List<Filme> filmes = Arrays.asList(new Filme("Filme 2", 1, 4.0));
		// ação
		
		try {
			Locacao locacao = service.alugarFilme(null, filmes);
			Assert.fail();
		} catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), is("Usuario vazio"));
		}

		System.out.println("forma robusta");
	}

	@Test
	public void testeLocacao_filmeVazio() throws FilmeSemEstoqueException, LocadoraException {
		// cenario
		Usuario usuario = new Usuario("Usuario 1");
		
		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");
		
		// ação	
		Locacao locacao = service.alugarFilme(usuario, null);
	}
	
	@Test
	public void devePagar75PctNoFilme3() throws FilmeSemEstoqueException, LocadoraException {
		// cenario
		Usuario usuario = new Usuario("Usuario 1");
		
		List<Filme> filmes = Arrays.asList(
				new Filme("Filme 1", 2, 4.0),
				new Filme("Filme 2", 2, 4.0),
				new Filme("Filme 3", 3, 4.0),
				new Filme("Filme 4", 3, 4.0),
				new Filme("Filme 5", 3, 4.0),
				new Filme("Filme 6", 3, 4.0)
				);
		
		// ação	
		Locacao locacao = service.alugarFilme(usuario, filmes);
		
		//verifificacao
		// valor do filme 4 + 4 + 3 +2 + 1 + 0=14
		assertThat(locacao.getValor(), is(14.0));	
	}
	
	@Test
	public void devePagar50PctNoFilme4() throws FilmeSemEstoqueException, LocadoraException {
		// cenario
		Usuario usuario = new Usuario("Usuario 1");
		
		List<Filme> filmes = Arrays.asList(
				new Filme("Filme 1", 2, 4.0),
				new Filme("Filme 2", 2, 4.0),
				new Filme("Filme 3", 3, 4.0),
				new Filme("Filme 4", 3, 4.0)
				);
		
		// ação	
		Locacao locacao = service.alugarFilme(usuario, filmes);
		
		//verifificacao
		// valor do filme 4 + 4 + 3 +2 + 1=13
		assertThat(locacao.getValor(), is(13.0));	
	}
	
	@Test
	public void devePagar25PctNoFilme5() throws FilmeSemEstoqueException, LocadoraException {
		// cenario
		Usuario usuario = new Usuario("Usuario 1");
		
		List<Filme> filmes = Arrays.asList(
				new Filme("Filme 1", 2, 4.0),
				new Filme("Filme 2", 2, 4.0),
				new Filme("Filme 3", 3, 4.0),
				new Filme("Filme 4", 3, 4.0),
				new Filme("Filme 5", 3, 4.0)
				);
		
		// ação	
		Locacao locacao = service.alugarFilme(usuario, filmes);
		
		//verifificacao
		// valor do filme 4 + 4 + 3 +2 =13
		assertThat(locacao.getValor(), is(14.0));	
	}
	
	@Test
	public void devePagar0PctNoFilme6() throws FilmeSemEstoqueException, LocadoraException {
		// cenario
		Usuario usuario = new Usuario("Usuario 1");
		
		List<Filme> filmes = Arrays.asList(
				new Filme("Filme 1", 2, 4.0),
				new Filme("Filme 2", 2, 4.0),
				new Filme("Filme 3", 3, 4.0),
				new Filme("Filme 4", 3, 4.0),
				new Filme("Filme 5", 3, 4.0),
				new Filme("Filme 6", 3, 4.0)
				);
		
		// ação	
		Locacao locacao = service.alugarFilme(usuario, filmes);
		
		//verifificacao
		assertThat(locacao.getValor(), is(14.0));	
	}
	
	@Test
	public void deveDevolverNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException {
		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 2", 1, 4.0));
		
		Locacao locacao = service.alugarFilme(usuario, filmes);
		
		//verificacao
		System.out.println(locacao.getDataRetorno());
		boolean ehSegunda = DataUtils.verificarDiaSemana(locacao.getDataRetorno(), Calendar.MONDAY);
		Assert.assertTrue(ehSegunda);
	}
	
	@Test
	public void deveDevolverNaSegundaAoAlugarNoSabado_MatchCustomizavel() throws FilmeSemEstoqueException, LocadoraException {	
		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 2", 1, 4.0));
		
		Locacao locacao = service.alugarFilme(usuario, filmes);
		
		//verificacao
		System.out.println(locacao.getDataRetorno());
		boolean ehSegunda = DataUtils.verificarDiaSemana(locacao.getDataRetorno(), Calendar.MONDAY);
		assertThat(locacao.getDataRetorno(), MatchersProprios.caiNumaSegunda());
	}	
	
	@Test
	public void naoDeveAlugarFilmeParaNegativadoSPC() throws Exception{
		//CENARIO
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		Usuario usuario2 = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes =  Arrays.asList(FilmeBuilder.umFilme().agora());
		
		Mockito.when(spc.possuiNegativacao(usuario)).thenReturn(true);
		
		exception.expect(LocadoraException.class);
		exception.expectMessage("Usuário Negativado");
		
		//acao - se assar o usuario 2 o teste falha
		service.alugarFilme(usuario, filmes);	
	}
	
	@Test
	public void deveEnviarEmailParaLocacoesAtrasadas() {
		//cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Locacao> locacoes = Arrays.asList(
				LocacaoBuilder.umLocacao()
					.comUsuario(usuario)
					.comDataRetorno(DataUtils.obterDataComDiferencaDias(-2))
					.agora());
		Mockito.when(dao.obterLocacoesPendentes()).thenReturn(locacoes);
		
		//acao
		service.notificarAtrasos();
		
		//verificacao
		Mockito.verify(email).notificarAtraso(usuario);
	}

	@Test
	public void deveTratarErronoSPC() throws Exception {
		//cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes =  Arrays.asList(FilmeBuilder.umFilme().agora());
		
		//Expectativa Mock
		Mockito.when(spc.possuiNegativacao(usuario)).thenThrow(new Exception("Falha na comunicaçao com SPC"));
		
		//verificacao
		exception.expect(LocadoraException.class);
		exception.expectMessage("Falha na comunicaçao com SPC");
		
		//acao
		service.alugarFilme(usuario, filmes);
	}
	
	@Test
	public void deveProrrogarUmaLocacao() throws Exception {
		//cenario
		Locacao locacao = LocacaoBuilder.umLocacao().agora();
		
		//acao
		service.prorrogarLocacao(locacao, 3);
		
		//verificacao
		ArgumentCaptor<Locacao> argCapt = ArgumentCaptor.forClass(Locacao.class);
		Mockito.verify(dao).salvar(argCapt.capture());
		Locacao locacaoRetornada = argCapt.getValue();
		
		//Apenas a primeira falha é exibida
		Assert.assertThat(locacaoRetornada.getValor(), is(12.0));
		Assert.assertThat(locacaoRetornada.getDataLocacao(), is(MatchersProprios.ehHoje()));
		Assert.assertThat(locacaoRetornada.getDataRetorno(), (MatchersProprios.ehHojeComDiferenciaDias(3)));
		
		//Desta forma caso der erro em duas verificações as duas serão exibidas como falha
		error.checkThat(locacaoRetornada.getValor(), is(12.0));
		error.checkThat(locacaoRetornada.getDataLocacao(), is(MatchersProprios.ehHoje()));
		error.checkThat(locacaoRetornada.getDataRetorno(), (MatchersProprios.ehHojeComDiferenciaDias(3)));
	}
}