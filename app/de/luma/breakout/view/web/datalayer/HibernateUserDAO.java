package de.luma.breakout.view.web.datalayer;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.google.inject.Inject;

import de.luma.breakout.view.web.models.User;

public class HibernateUserDAO implements UserDAO {

	@Inject
	private HibernateUtil hibernate;
	
	@Override
	public void create(User user) {
		Session session;
		Transaction tx = null;
		try {
			session = hibernate.getInstance().getCurrentSession();
			tx = session.beginTransaction();
			session.save(user);
			tx.commit();
		} catch (HibernateException ex) {
			if (tx != null) {
				try {
					tx.rollback();
				} catch (HibernateException exRb) { }
			}

			throw new RuntimeException(ex.getMessage());
		}
	}

	@Override
	public void update(User user) {
		Session session;
		Transaction tx = null;
		try {
			session = hibernate.getInstance().getCurrentSession();
			tx = session.beginTransaction();
			session.update(user);
			tx.commit();
		} catch (HibernateException ex) {
			if (tx != null) {
				try {
					tx.rollback();
				} catch (HibernateException exRb) { }
			}

			throw new RuntimeException(ex.getMessage());
		}
	}

	@Override
	public void delete(User user) {
		Session session;
		Transaction tx = null;
		try {
			session = hibernate.getInstance().getCurrentSession();
			tx = session.beginTransaction();
			session.delete(user);
			tx.commit();
		} catch (HibernateException ex) {
			if (tx != null) {
				try {
					tx.rollback();
				} catch (HibernateException exRb) { }
			}

			throw new RuntimeException(ex.getMessage());
		}
	}

	@Override
	public User getByEmail(String email) {
		Session session;
		Transaction tx = null;
		try {
			session = hibernate.getInstance().getCurrentSession();
			tx = session.beginTransaction();
			List<User> result = session.createCriteria(User.class).add(Restrictions.eq("email", email)).list();
			tx.commit();
			if (result.size() == 1) {
				return result.get(0);
			}
		} catch (HibernateException ex) {
			throw new RuntimeException(ex.getMessage());
		}

		return null;
	}

	@Override
	public void openConnection() { }

	@Override
	public void closeConnection() {
		hibernate.getInstance().getCurrentSession().close();
	}

}
