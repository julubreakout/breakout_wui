package de.luma.breakout.view.web.modules;

import com.google.inject.Singleton;

import de.luma.breakout.view.web.datalayer.HibernateUserDAO;
import de.luma.breakout.view.web.datalayer.HibernateUtil;
import de.luma.breakout.view.web.datalayer.UserDAO;

public class HibernateModule extends BreakoutBaseModule {

	@Override
	protected void configure() {
		super.configure();
		
		bind(HibernateUtil.class).in(Singleton.class);
		bind(UserDAO.class).to(HibernateUserDAO.class).in(Singleton.class);
	}
	
}
