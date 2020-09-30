package br.com.automacao.servicos;
import br.com.automacao.entidades.Usuario;

public interface SPCService {

	public boolean possuiNegativacao(Usuario usuario) throws Exception;
}