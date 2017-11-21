package com.dotterbear.repository;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

@NoRepositoryBean
public class BaseRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements BaseRepository<T, ID> {

	private int batchSize = 20;

	private EntityManager entityManager;

	public BaseRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.entityManager = entityManager;
	}

	@Override
	@Transactional
	public Collection<T> batchSave(Collection<T> entities) {
		int count = 0;
		for (T entity : entities) {
			save(entity);
			if (++count % batchSize == 0) {
				entityManager.flush();
				entityManager.clear();
			}
		}
		return entities;
	}

	@Override
	@Transactional
	public T msave(T entity) {
		return entityManager.merge(entity);
	}

}
