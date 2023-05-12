package br.com.testerang.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.SerializationUtils;

import br.com.testerang.model.UnidadeDeSaude;
import br.com.testerang.service.UnidadeDeSaudeService;
import br.com.testerang.utility.Message;
import br.com.testerang.utility.NegocioException;

@Named
@ViewScoped
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
		unidadeSaudeUpdate = new UnidadeDeSaude();
		this.unidadeSaudeUpdate = (UnidadeDeSaude) SerializationUtils.clone(unidadeScreen);
	}
	

	public void adicionar() {
		if (!validar()) {
			try {
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
			var testeLista = service.buscar(nomeEstabelecimento);
			setResultados(testeLista);
		} catch (Exception e) {
			Message.erro(e.getMessage());
		}

	}

	public boolean validar() {

		boolean encontrouUnidade = false;
		resultados = service.todasAsUnidades();
		Integer unidadeCepInicio = Integer.parseInt(unidadeSaude.getCepInicio().replace("-", ""));
		Integer unidadeCepFinal = Integer.parseInt(unidadeSaude.getCepFinal().replace("-", ""));

		for (UnidadeDeSaude unidade : resultados) {
			Integer cepInicio = Integer.parseInt(unidade.getCepInicio().replace("-", ""));
			Integer cepFinal = Integer.parseInt(unidade.getCepFinal().replace("-", ""));

			if (unidadeCepInicio >= cepInicio && unidadeCepInicio <= cepFinal
					|| unidadeCepFinal <= cepFinal && unidadeCepFinal >= cepInicio) {
				Message.warn("Já existe uma unidade que atende neste Intervalo de Cep");
				return encontrouUnidade = true;
			} else if (unidadeSaude.getCnes().equals(unidade.getCnes())) {
				Message.warn("Já existe uma unidade com este CNES cadastrado");
				return encontrouUnidade = true;
			}

		}
		return encontrouUnidade;
	}

	public void alterarUnidade() {

		try {
			
			boolean encontrouUnidade = false;
			resultados = service.todasMenosUm(unidadeSaudeUpdate);
			Integer unidadeCepInicio = Integer.parseInt(unidadeSaudeUpdate.getCepInicio().replace("-", ""));
			Integer unidadeCepFinal = Integer.parseInt(unidadeSaudeUpdate.getCepFinal().replace("-", ""));
			System.out.println(unidadeCepInicio);
			for (var unidade : resultados) {
				Integer cepInicio = Integer.parseInt(unidade.getCepInicio().replace("-", ""));
				Integer cepFinal = Integer.parseInt(unidade.getCepFinal().replace("-", ""));

				if (unidadeCepInicio >= cepInicio && unidadeCepInicio <= cepFinal
						|| unidadeCepFinal <= cepFinal && unidadeCepFinal >= cepInicio) {

					Message.warn("Já existe uma unidade que atende neste Intervalo de Cep");
					encontrouUnidade = true;
					break;
				} else if (unidadeSaudeUpdate.getCnes().equals(unidade.getCnes())) {
					Message.warn("Já existe uma unidade com este CNES cadastrado");
					encontrouUnidade = true;
					break;
				}

			}

			if (!encontrouUnidade) {

				service.salvar(unidadeSaudeUpdate);
				Message.info(unidadeSaudeUpdate.getNomeEstabelecimento() + " foi alterado com sucesso");
				unidadeSaudeUpdate = new UnidadeDeSaude();
				carregar2();

			} else {
				Message.warn("Não foi possível salvar a sua alteração");
			}
		} catch (Exception e) {
			Message.erro(e.getMessage());
		}
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
