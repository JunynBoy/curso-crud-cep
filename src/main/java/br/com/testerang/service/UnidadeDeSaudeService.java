package br.com.testerang.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import br.com.testerang.dao.DAO;
import br.com.testerang.model.UnidadeDeSaude;
import br.com.testerang.utility.NegocioException;

public class UnidadeDeSaudeService implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private DAO<UnidadeDeSaude> dao;
	
	public void salvar (UnidadeDeSaude u) throws NegocioException {
		

		dao.salvar(u);
	}
	
	public void remover(UnidadeDeSaude u)throws NegocioException{
		dao.remover(UnidadeDeSaude.class, u.getId());
	}
	
	public List<UnidadeDeSaude> todasAsUnidades(){
		return dao.buscarTodos("select u from UnidadeDeSaude u order by u.id desc");
	}
	
	public List<UnidadeDeSaude>  buscarPorNome(String nomeEstabelecimento) {
	    return dao.buscarUmPorNome(nomeEstabelecimento);
	}
	
	public List<UnidadeDeSaude> todasMenosUm(UnidadeDeSaude update){
		return dao.buscarUpdate(update);
	}
	
	

	

}
