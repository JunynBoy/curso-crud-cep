package br.com.testerang.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.testerang.model.UnidadeDeSaude;
import br.com.testerang.repository.UnidadeDeSaudeRepository;
import br.com.testerang.utility.NegocioException;

class UnidadeDeSaudeServiceTest {

	private FakeUnidadeDeSaudeRepository repository;
	private UnidadeDeSaudeService service;

	@BeforeEach
	void setUp() {
		repository = new FakeUnidadeDeSaudeRepository();
		service = new UnidadeDeSaudeService(repository);
	}

	@Test
	void salvaUnidadeNormalizandoDados() throws NegocioException {
		UnidadeDeSaude unidade = unidade(null, "  Unidade   Central  ", "12.34567", "12345678", "12345999");

		UnidadeDeSaude saved = service.salvar(unidade);

		assertEquals("Unidade Central", saved.getNomeEstabelecimento());
		assertEquals("1234567", saved.getCnes());
		assertEquals("12345-678", saved.getCepInicio());
		assertEquals("12345-999", saved.getCepFinal());
	}

	@Test
	void rejeitaCnesJaCadastrado() {
		repository.unidades.add(unidade(1L, "Unidade A", "1234567", "10000-000", "10000-100"));
		UnidadeDeSaude novaUnidade = unidade(null, "Unidade B", "1234567", "20000-000", "20000-100");

		assertThrows(NegocioException.class, () -> service.salvar(novaUnidade));
	}

	@Test
	void rejeitaIntervaloDeCepSobreposto() {
		repository.unidades.add(unidade(1L, "Unidade A", "1234567", "10000-000", "10000-100"));
		UnidadeDeSaude novaUnidade = unidade(null, "Unidade B", "7654321", "10000-050", "10000-150");

		assertThrows(NegocioException.class, () -> service.salvar(novaUnidade));
	}

	@Test
	void permiteEditarMantendoOCnesDaPropriaUnidade() throws NegocioException {
		repository.unidades.add(unidade(1L, "Unidade A", "1234567", "10000-000", "10000-100"));
		UnidadeDeSaude unidadeEditada = unidade(1L, "Unidade A Editada", "1234567", "10000-000", "10000-100");

		UnidadeDeSaude saved = service.salvar(unidadeEditada);

		assertEquals("Unidade A Editada", saved.getNomeEstabelecimento());
	}

	private static UnidadeDeSaude unidade(Long id, String nome, String cnes, String cepInicio, String cepFinal) {
		UnidadeDeSaude unidade = new UnidadeDeSaude();
		unidade.setId(id);
		unidade.setNomeEstabelecimento(nome);
		unidade.setCnes(cnes);
		unidade.setCepInicio(cepInicio);
		unidade.setCepFinal(cepFinal);
		return unidade;
	}

	private static final class FakeUnidadeDeSaudeRepository extends UnidadeDeSaudeRepository {

		private final List<UnidadeDeSaude> unidades = new ArrayList<>();

		@Override
		public boolean existeCnes(String cnes, Long idIgnorado) {
			return unidades.stream()
					.anyMatch(unidade -> Objects.equals(unidade.getCnes(), cnes)
							&& !Objects.equals(unidade.getId(), idIgnorado));
		}

		@Override
		public List<UnidadeDeSaude> listarTodasExceto(Long id) {
			return unidades.stream()
					.filter(unidade -> !Objects.equals(unidade.getId(), id))
					.collect(Collectors.toList());
		}

		@Override
		public UnidadeDeSaude salvar(UnidadeDeSaude unidade) {
			return unidade;
		}
	}
}
