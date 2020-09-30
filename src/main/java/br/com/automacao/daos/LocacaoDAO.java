package br.com.automacao.daos;

import java.util.List;

import br.com.automacao.entidades.Locacao;

public interface LocacaoDAO {

	public void salvar(Locacao locacao);

	public List<Locacao> obterLocacoesPendentes();
}