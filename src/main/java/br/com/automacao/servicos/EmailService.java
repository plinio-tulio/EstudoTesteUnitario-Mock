package br.com.automacao.servicos;

import br.com.automacao.entidades.Usuario;

public interface EmailService {
	
	public void notificarAtraso(Usuario usuario);

}