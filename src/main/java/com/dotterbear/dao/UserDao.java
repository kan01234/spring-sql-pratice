package com.dotterbear.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dotterbear.model.User;

@Transactional(rollbackFor = Exception.class)
@Repository
public class UserDao {

	private final int pageSize = 10;
	private Logger logger = Logger.getLogger(this.getClass());

	@PersistenceContext
	protected EntityManager em;

	public User saveException(User user) throws Exception {
		em.persist(user);
		throw new Exception();
		// TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		// return userB;
	}

	public User save(User user) {
		em.persist(user);
		return user;
	}

	@Cacheable(value = "findAll", key = "#page")
	public List<User> findAll(int page) {
		logger.info("find All, page: " + page);
		return em.createQuery("from User").setFirstResult(--page * pageSize).setMaxResults(pageSize).getResultList();
	}

	public List<User> findAllOrderByName(int page) {
		return em.createQuery("from User order by name asc").setFirstResult(--page * pageSize).setMaxResults(pageSize).getResultList();
	}

	public List<User> findUserByEmailAndName(String email, String name) {
		return em.createNamedQuery("User.findUserByEmailAndName").setParameter(1, email).setParameter(2, name).getResultList();
	}

	public List<User> findUserByEmailIn(List<String> emails) {
		Criteria criteria = em.unwrap(Session.class).createCriteria(User.class);
		return criteria.add(Restrictions.in("email", emails)).addOrder(Order.asc("id")).list();
	}

	@CacheEvict(value = "findAll", key = "#page")
	public void evictFindAllByPage(int page) {
		logger.info("evict find all by page");
	}

	@CacheEvict(value = "findAll", allEntries = true)
	public void evictFindAll() {
		logger.info("evict find all");
	}

}
