package br.com.testerang.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import br.com.testerang.model.UnidadeDeSaude;
import br.com.testerang.persistence.JpaUtil;

@ApplicationScoped
public class UnidadeDeSaudeRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	public Optional<UnidadeDeSaude> buscarPorId(Long id) {
		if (id == null) {
			return Optional.empty();
		}

		return execute(manager -> Optional.ofNullable(manager.find(UnidadeDeSaude.class, id)));
	}

	public List<UnidadeDeSaude> listarTodas() {
		return execute(manager -> manager
				.createQuery("select u from UnidadeDeSaude u order by u.id desc", UnidadeDeSaude.class)
				.getResultList());
	}

	public List<UnidadeDeSaude> listarTodasExceto(Long id) {
		if (id == null) {
			return listarTodas();
		}

		return execute(manager -> manager
				.createQuery("select u from UnidadeDeSaude u where u.id <> :id order by u.id desc", UnidadeDeSaude.class)
				.setParameter("id", id)
				.getResultList());
	}

	public List<UnidadeDeSaude> buscar(String termo) {
		String termoNormalizado = "%" + normalizeSearchTerm(termo) + "%";

		return execute(manager -> manager
				.createQuery(
						"select u from UnidadeDeSaude u "
								+ "where lower(u.nomeEstabelecimento) like :termo "
								+ "or lower(u.cnes) like :termo "
								+ "or lower(u.cepInicio) like :termo "
								+ "or lower(u.cepFinal) like :termo "
								+ "order by u.id desc",
						UnidadeDeSaude.class)
				.setParameter("termo", termoNormalizado)
				.getResultList());
	}

	public boolean existeCnes(String cnes, Long idIgnorado) {
		return execute(manager -> {
			String jpql = "select count(u) from UnidadeDeSaude u where u.cnes = :cnes";
			if (idIgnorado != null) {
				jpql += " and u.id <> :id";
			}

			TypedQuery<Long> query = manager.createQuery(jpql, Long.class)
					.setParameter("cnes", cnes);
			if (idIgnorado != null) {
				query.setParameter("id", idIgnorado);
			}

			return query.getSingleResult() > 0;
		});
	}

	public UnidadeDeSaude salvar(UnidadeDeSaude unidade) {
		EntityManager manager = JpaUtil.createEntityManager();
		EntityTransaction transaction = manager.getTransaction();

		try {
			transaction.begin();

			UnidadeDeSaude saved = unidade.getId() == null
					? persist(manager, unidade)
					: manager.merge(unidade);

			transaction.commit();
			return saved;
		} catch (RuntimeException e) {
			rollback(transaction);
			throw e;
		} finally {
			manager.close();
		}
	}

	public void remover(UnidadeDeSaude unidade) {
		if (unidade == null || unidade.getId() == null) {
			return;
		}

		EntityManager manager = JpaUtil.createEntityManager();
		EntityTransaction transaction = manager.getTransaction();

		try {
			transaction.begin();
			UnidadeDeSaude managed = manager.find(UnidadeDeSaude.class, unidade.getId());
			if (managed != null) {
				manager.remove(managed);
			}
			transaction.commit();
		} catch (RuntimeException e) {
			rollback(transaction);
			throw e;
		} finally {
			manager.close();
		}
	}

	private UnidadeDeSaude persist(EntityManager manager, UnidadeDeSaude unidade) {
		manager.persist(unidade);
		return unidade;
	}

	private <T> T execute(Function<EntityManager, T> operation) {
		EntityManager manager = JpaUtil.createEntityManager();
		try {
			return operation.apply(manager);
		} finally {
			manager.close();
		}
	}

	private static void rollback(EntityTransaction transaction) {
		if (transaction != null && transaction.isActive()) {
			transaction.rollback();
		}
	}

	private static String normalizeSearchTerm(String termo) {
		return termo == null ? "" : termo.trim().toLowerCase();
	}
}
