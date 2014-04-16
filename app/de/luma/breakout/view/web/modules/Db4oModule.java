package de.luma.breakout.view.web.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;

import de.luma.breakout.view.web.models.Db4oUserDAO;
import de.luma.breakout.view.web.models.UserDAO;

public class Db4oModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(String.class).annotatedWith(Names.named("Db4oDbPath")).toInstance("db4o.db");
		bind(UserDAO.class).to(Db4oUserDAO.class).in(Singleton.class);
	}

}
