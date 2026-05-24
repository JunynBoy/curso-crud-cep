package br.com.testerang.service;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import br.com.testerang.model.UnidadeDeSaude;
import br.com.testerang.repository.UnidadeDeSaudeRepository;
import br.com.testerang.utility.CepUtils;
import br.com.testerang.utility.NegocioException;

@ApplicationScoped
public class UnidadeDeSaudeService implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final int CNES_LENGTH = 7;

	private UnidadeDeSaudeRepository repository;

	public UnidadeDeSaudeService() {
	}

	@Inject
	public UnidadeDeSaudeService(UnidadeDeSaudeRepository repository) {
		this.repository = repository;
	}

	public UnidadeDeSaude salvar(UnidadeDeSaude unidade) throws NegocioException {
		validar(unidade);

		try {
			return repository.salvar(unidade);
		} catch (RuntimeException e) {
			throw new NegocioException("Não foi possível salvar a unidade de saúde.", e);
		}
	}

	public void remover(UnidadeDeSaude unidade) throws NegocioException {
		if (unidade == null || unidade.getId() == null) {
			throw new NegocioException("Selecione uma unidade de saúde válida para remover.");
		}

		try {
			repository.remover(unidade);
		} catch (RuntimeException e) {
			throw new NegocioException("Não foi possível remover a unidade de saúde.", e);
		}
	}

	public List<UnidadeDeSaude> todasAsUnidades() {
		return repository.listarTodas();
	}

	public List<UnidadeDeSaude> buscar(String termoBusca) {
		if (termoBusca == null || termoBusca.trim().isEmpty()) {
			return todasAsUnidades();
		}

		return repository.buscar(termoBusca);
	}

	private void validar(UnidadeDeSaude unidade) throws NegocioException {
		if (unidade == null) {
			throw new NegocioException("Informe os dados da unidade de saúde.");
		}

		String nome = normalizarTexto(unidade.getNomeEstabelecimento());
		if (nome.isEmpty()) {
			throw new NegocioException("Nome da unidade é obrigatório.");
		}

		String cnes = somenteDigitos(unidade.getCnes());
		if (cnes.length() != CNES_LENGTH) {
			throw new NegocioException("CNES deve conter 7 dígitos.");
		}

		String cepInicio = CepUtils.normalizar(unidade.getCepInicio());
		String cepFinal = CepUtils.normalizar(unidade.getCepFinal());
		int cepInicioNumero = CepUtils.paraNumero(cepInicio);
		int cepFinalNumero = CepUtils.paraNumero(cepFinal);

		if (cepInicioNumero > cepFinalNumero) {
			throw new NegocioException("CEP inicial não pode ser maior que o CEP final.");
		}

		unidade.setNomeEstabelecimento(nome);
		unidade.setCnes(cnes);
		unidade.setCepInicio(cepInicio);
		unidade.setCepFinal(cepFinal);

		Long idAtual = unidade.getId();
		if (repository.existeCnes(cnes, idAtual)) {
			throw new NegocioException("Já existe uma unidade com este CNES cadastrado.");
		}

		validarIntervaloCepDisponivel(idAtual, cepInicioNumero, cepFinalNumero);
	}

	private void validarIntervaloCepDisponivel(Long idAtual, int cepInicioNumero, int cepFinalNumero)
			throws NegocioException {
		for (UnidadeDeSaude unidade : repository.listarTodasExceto(idAtual)) {
			int inicioCadastrado = CepUtils.paraNumero(unidade.getCepInicio());
			int finalCadastrado = CepUtils.paraNumero(unidade.getCepFinal());

			if (CepUtils.intervalosSobrepostos(cepInicioNumero, cepFinalNumero, inicioCadastrado, finalCadastrado)) {
				throw new NegocioException("Já existe uma unidade que atende este intervalo de CEP.");
			}
		}
	}

	private static String normalizarTexto(String texto) {
		if (texto == null) {
			return "";
		}

		return texto.trim().replaceAll("\\s+", " ");
	}

	private static String somenteDigitos(String value) {
		if (value == null) {
			return "";
		}

		return value.replaceAll("\\D", "");
	}
}
