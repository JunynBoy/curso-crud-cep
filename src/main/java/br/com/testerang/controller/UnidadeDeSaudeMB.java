package br.com.testerang.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;


import br.com.testerang.model.UnidadeDeSaude;
import br.com.testerang.service.UnidadeDeSaudeService;
import br.com.testerang.utility.Message;
import br.com.testerang.utility.NegocioException;

@Named
@SessionScoped
public class UnidadeDeSaudeMB implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private UnidadeDeSaude unidadeSaude;

	private UnidadeDeSaude unidadeSaudeUpdate;

	private String nomeEstabelecimento;

	public String getNomeEstabelecimento() {
		return nomeEstabelecimento;
	}

	public void setNomeEstabelecimento(String nomeEstabelecimento) {
		this.nomeEstabelecimento = nomeEstabelecimento;
	}

	@Inject
	private UnidadeDeSaudeService service;

	private List<UnidadeDeSaude> unidades;

	private String termoBusca;
	private List<UnidadeDeSaude> resultados;

	@PostConstruct
	public void carregar() {
		carregar2();
	}

	public void carregar2() {
		unidades = service.todasAsUnidades();
	}
	
	public void carregaUnidade(UnidadeDeSaude unidadeScreen) {
		this.unidadeSaudeUpdate = unidadeScreen;
		System.out.println("BOTAO DO FRONT: " + unidadeSaudeUpdate);
	}

	public void adicionar() {

	    
	    if (!validar()) {
	        try {
	            unidadeSaude.setCepInicio(unidadeSaude.getCepInicio().replace("-", ""));
	            unidadeSaude.setCepFinal(unidadeSaude.getCepFinal().replace("-", ""));
	            service.salvar(unidadeSaude);

	            unidadeSaude = new UnidadeDeSaude();
	            carregar2();

	            Message.info("Unidade de saúde salva com sucesso!");
	        } catch (NegocioException e) {
	            Message.erro("Erro ao salvar unidade de saúde: " + e.getMessage());
	        }
	    }
	}

	

	public void excluir(UnidadeDeSaude unidadeDelete) {
		
		try {
			String nomeEstab = unidadeDelete.getNomeEstabelecimento();
			service.remover(unidadeDelete);
			carregar2();
			Message.info(nomeEstab + " foi removido");
			nomeEstab = "";
		} catch (NegocioException e) {
			Message.erro(e.getMessage());
		}

	}

	public void buscar() {
		
		try {
			var testeLista = service.buscarPorNome(nomeEstabelecimento);
			setResultados(testeLista);
		} catch (Exception e) {
			Message.erro(e.getMessage());
		}

	}

	
	public boolean validar() {
		
		
		boolean encontrouUnidade = false;
	    resultados = service.todasAsUnidades();
	    for (UnidadeDeSaude unidade : resultados) {
	        int cepInicio = Integer.parseInt(unidade.getCepInicio().replace("-", ""));
	        int cepFinal = Integer.parseInt(unidade.getCepFinal().replace("-", ""));
	        int unidadeCepInicio = Integer.parseInt(unidadeSaude.getCepInicio().replace("-", ""));
	        int unidadeCepFinal = Integer.parseInt(unidadeSaude.getCepFinal().replace("-", ""));
	        
	       
	        
	        if (unidadeCepInicio >= cepInicio && unidadeCepInicio <= cepFinal || unidadeCepFinal <= cepFinal && unidadeCepFinal >= cepInicio ) {
	            Message.warn("Já existe uma unidade que atende neste Intervalo de Cep");
	            return encontrouUnidade = true;
	        }else if(unidadeSaude.getCnes().equals(unidade.getCnes())){
	        	Message.warn("Já existe uma unidade com este CNES cadastrado");
	        	 return encontrouUnidade = true;
	        }
	        
	        
	    }
	    return encontrouUnidade;
	}
	

		
	
	public void alterarUnidade() {
//		var teste = unidadeSaudeUpdate;
//		boolean encontrouUnidade = false;
	    resultados = service.todasMenosUm(unidadeSaudeUpdate);
	    
	    
//	    for (UnidadeDeSaude unidade : resultados) {
//	        Integer cepInicio = Integer.parseInt(unidade.getCepInicio().replace("-", ""));
//	        Integer cepFinal = Integer.parseInt(unidade.getCepFinal().replace("-", ""));
//	        Integer unidadeCepInicio = Integer.parseInt(unidadeSaudeUpdate.getCepInicio().replace("-", ""));
//	        Integer unidadeCepFinal = Integer.parseInt(unidadeSaudeUpdate.getCepFinal().replace("-", ""));
//	        System.out.println(unidadeSaudeUpdate.getNomeEstabelecimento());
//	        System.out.println("Unidade ------ " + unidade.toString());
//	       
//	        
//	        if (unidadeCepInicio >= cepInicio && unidadeCepInicio <= cepFinal || unidadeCepFinal <= cepFinal && unidadeCepFinal >= cepInicio ) {
//	            Message.warn("Já existe uma unidade que atende neste Intervalo de Cep");
//	            encontrouUnidade = true;
//	        }else if(unidadeSaudeUpdate.getCnes().equals(unidade.getCnes())){
//	        	Message.warn("Já existe uma unidade com este CNES cadastrado");
//	        	encontrouUnidade = true;
//	        }
//	        
//	        
//	    }
	    
	
		
//		if(encontrouUnidade == false) {
			try {
				service.salvar(unidadeSaudeUpdate);
				carregar2();

				Message.info(unidadeSaudeUpdate.getNomeEstabelecimento() + " foi alterado com sucesso");
				unidadeSaudeUpdate = new UnidadeDeSaude();
			} catch (NegocioException e) {
				Message.erro(e.getMessage());
			}
//		}else {
//			Message.warn("Não foi possível atualizar o cadastro");
//		}
	
		
//	}
	
	}
	


	public UnidadeDeSaude getUnidadeSaude() {
		return unidadeSaude;
	}

	public void setUnidadeSaude(UnidadeDeSaude unidadeSaude) {
		this.unidadeSaude = unidadeSaude;
	}

	public UnidadeDeSaudeService getService() {
		return service;
	}

	public void setService(UnidadeDeSaudeService service) {
		this.service = service;
	}

	public List<UnidadeDeSaude> getUnidades() {
		return unidades;
	}

	public void setUnidades(List<UnidadeDeSaude> unidades) {
		this.unidades = unidades;
	}

	public UnidadeDeSaude getUnidadeSaudeUpdate() {
		return unidadeSaudeUpdate;
	}

	public void setUnidadeSaudeUpdate(UnidadeDeSaude unidadeSaudeUpdate) {
		this.unidadeSaudeUpdate = unidadeSaudeUpdate;
	}

	public String getTermoBusca() {
		return termoBusca;
	}

	public void setTermoBusca(String termoBusca) {
		this.termoBusca = termoBusca;
	}

	public List<UnidadeDeSaude> getResultados() {
		return resultados;
	}

	public void setResultados(List<UnidadeDeSaude> resultados) {
		this.resultados = resultados;
	}

}
