package de.luma.breakout.view.web.datalayer;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

public final class HibernateUtil {
	private SessionFactory sessionFactory;
	
	public HibernateUtil() {
		AnnotationConfiguration cfg = new AnnotationConfiguration();
		cfg.configure();
		
		this.sessionFactory = cfg.buildSessionFactory();
	}
	
	public SessionFactory getInstance() {
		return this.sessionFactory;
	}
	
}
