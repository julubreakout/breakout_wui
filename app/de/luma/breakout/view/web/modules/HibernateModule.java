package de.luma.breakout.view.web.modules;

import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;

import de.luma.breakout.view.web.datalayer.CouchDbUserDAO;
import de.luma.breakout.view.web.datalayer.HibernateUserDAO;
import de.luma.breakout.view.web.datalayer.HibernateUtil;
import de.luma.breakout.view.web.datalayer.UserDAO;

public class HibernateModule extends BreakoutBaseModule {

	@Override
	protected void configure() {
		super.configure();
		
		bind(HibernateUtil.class).in(Singleton.class);
		//bind(UserDAO.class).to(HibernateUserDAO.class).in(Singleton.class);
		Multibinder<UserDAO> mb = Multibinder.newSetBinder(binder(), UserDAO.class);
		mb.addBinding().to(HibernateUserDAO.class).in(Singleton.class);
	}
	
}
