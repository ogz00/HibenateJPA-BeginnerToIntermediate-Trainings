package org.oguz.orm.data.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.oguz.orm.data.HibernateUtil;
import org.oguz.orm.data.dao.interfaces.Dao;

public class AbstractDao<T, ID extends Serializable> implements Dao<T, ID> {

	private Class<T> persistenceClass;
	private Session session;

	@SuppressWarnings("unchecked")
	public AbstractDao() {
		this.persistenceClass = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	public void setSession(Session session) {
		this.session = session;

	}

	protected Session getSession() {
		if (this.session == null) {
			this.session = HibernateUtil.getSessionFactory()
					.getCurrentSession();
		}
		return this.session;
	}

	public Class<T> getPersistentClass() {
		return persistenceClass;
	}

	@SuppressWarnings("unchecked")
	public T findById(ID id) {
		return (T) getSession().load(this.persistenceClass, id);
	}

	public List<T> findAll() {
		return this.findByCriteria();
	}

	@SuppressWarnings("unchecked")
	protected List<T> findByCriteria(Criterion... criterion) {
		Criteria crit = this.getSession().createCriteria(
				this.getPersistentClass());

		for (Criterion c : criterion) {
			crit.add(c);
		}
		return (List<T>) crit.list();
	}

	public T save(T entity) {
		this.getSession().saveOrUpdate(entity);
		return entity;
	}

	public void delete(T entity) {
		this.getSession().delete(entity);

	}

	public void flush() {
		this.getSession().flush();

	}

	public void clear() {
		this.getSession().clear();

	}

}
