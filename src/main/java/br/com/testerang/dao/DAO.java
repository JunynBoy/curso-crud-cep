package br.com.testerang.dao;

import java.io.Serializable;
import java.util.List;

import br.com.testerang.model.Base;
import br.com.testerang.model.UnidadeDeSaude;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

public class DAO<T extends Base> implements Serializable {

	private static final long serialVersionUID = 1L;
	private static EntityManager manager = ConnectionFactory.getEntityManager();

	public T buscarPorId(Class<T> clazz, Long id) {
		System.out.println("buscou");
		return manager.find(clazz, id);
	}

	public void salvar(T t) {
		System.out.println("salvou");
		try {
			manager.getTransaction().begin();

			if (t.getId() == null) {
				manager.persist(t);
			} else {
				manager.merge(t);
			}

			manager.getTransaction().commit();
		} catch (Exception e) {
			manager.getTransaction().rollback();
		}
	}

	public void remover(Class<T> clazz, Long id) {
		System.out.println("removeu");
		T t = buscarPorId(clazz, id);
		try {
			manager.getTransaction().begin();
			manager.remove(t);
			manager.getTransaction().commit();
		} catch (Exception e) {
			manager.getTransaction().rollback();
		}
	}

	@SuppressWarnings("unchecked") 
	public List<T> buscarTodos(String jpql) {
		Query query = manager.createQuery(jpql);
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<T> buscarUmPorNome(String nomeEstabelecimento) {
		Query query = manager.createQuery("SELECT e FROM UnidadeDeSaude e WHERE e.nomeEstabelecimento LIKE :nomeEstabelecimento")
	        .setParameter("nomeEstabelecimento", nomeEstabelecimento + "%");
	    return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<T> buscar(String textoBusca) {
		Query query = manager.createQuery("SELECT e FROM UnidadeDeSaude e WHERE e.nomeEstabelecimento LIKE :textoBusca OR e.cnes LIKE :textoBusca OR e.cepInicio LIKE :textoBusca OR e.cepInicio LIKE :textoBusca OR e.cepFinal LIKE :textoBusca")
	        .setParameter("textoBusca", "%" + textoBusca + "%");
	    return query.getResultList();
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	public List<T> buscarUpdate(UnidadeDeSaude update) {
		Query query = manager.createQuery("select u from UnidadeDeSaude u where u.id <> :id order by u.id desc ")
	        .setParameter("id", update.getId());
	    return query.getResultList();
	}
	
	
	

}
