package br.com.testerang.controller;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.testerang.model.UnidadeDeSaude;
import br.com.testerang.service.UnidadeDeSaudeService;
import br.com.testerang.utility.Message;
import br.com.testerang.utility.NegocioException;

@Named
@ViewScoped
public class UnidadeDeSaudeMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private UnidadeDeSaude unidadeSaude;
	private UnidadeDeSaude unidadeSaudeUpdate;
	private List<UnidadeDeSaude> unidades;
	private String termoBusca;
	private List<UnidadeDeSaude> resultados;
	private boolean buscaAtiva;

	@Inject
	private UnidadeDeSaudeService service;

	@Inject
	private GeradorRelatorio geradorRelatorio;

	@PostConstruct
	public void init() {
		unidadeSaude = new UnidadeDeSaude();
		unidadeSaudeUpdate = new UnidadeDeSaude();
		resultados = Collections.emptyList();
		buscaAtiva = false;
		carregarUnidades();
	}

	public void carregarUnidades() {
		unidades = service.todasAsUnidades();
	}

	public void carregaUnidade(UnidadeDeSaude unidadeScreen) {
		unidadeSaudeUpdate = unidadeScreen == null
				? new UnidadeDeSaude()
				: new UnidadeDeSaude(unidadeScreen);
	}

	public void adicionar() {
		try {
			service.salvar(unidadeSaude);
			unidadeSaude = new UnidadeDeSaude();
			carregarUnidades();
			Message.info("Unidade de saúde salva com sucesso.");
		} catch (NegocioException e) {
			Message.erro(e.getMessage());
		}
	}

	public void excluir(UnidadeDeSaude unidadeDelete) {
		try {
			String nomeEstabelecimento = unidadeDelete.getNomeEstabelecimento();
			service.remover(unidadeDelete);
			carregarUnidades();
			Message.info(nomeEstabelecimento + " foi removido.");
		} catch (NegocioException e) {
			Message.erro(e.getMessage());
		}
	}

	public void buscar() {
		try {
			if (isBlank(termoBusca)) {
				limparBusca();
				return;
			}

			resultados = service.buscar(termoBusca);
			buscaAtiva = true;
		} catch (RuntimeException e) {
			Message.erro("Não foi possível realizar a busca.");
		}
	}

	public void limparBusca() {
		termoBusca = null;
		resultados = Collections.emptyList();
		buscaAtiva = false;
		carregarUnidades();
	}

	public void alterarUnidade() {
		try {
			service.salvar(unidadeSaudeUpdate);
			Message.info(unidadeSaudeUpdate.getNomeEstabelecimento() + " foi alterado com sucesso.");
			unidadeSaudeUpdate = new UnidadeDeSaude();
			carregarUnidades();
		} catch (NegocioException e) {
			Message.erro(e.getMessage());
		}
	}

	public void gerarRelatorioAction() {
		try {
			geradorRelatorio.gerar(buscaAtiva ? resultados : unidades);
		} catch (NegocioException e) {
			Message.erro(e.getMessage());
		}
	}

	public UnidadeDeSaude getUnidadeSaude() {
		return unidadeSaude;
	}

	public void setUnidadeSaude(UnidadeDeSaude unidadeSaude) {
		this.unidadeSaude = unidadeSaude;
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

	public boolean isBuscaAtiva() {
		return buscaAtiva;
	}

	private static boolean isBlank(String value) {
		return value == null || value.trim().isEmpty();
	}
}
