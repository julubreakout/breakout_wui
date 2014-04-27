package de.luma.breakout.view.web.modules;

import com.google.inject.Singleton;
import com.google.inject.name.Names;

import de.luma.breakout.view.web.datalayer.Db4oUserDAO;
import de.luma.breakout.view.web.datalayer.UserDAO;

public class Db4oModule extends BreakoutBaseModule {

	@Override
	protected void configure() {
		super.configure();
		
		bind(String.class).annotatedWith(Names.named("Db4oDbPath")).toInstance("db4o.db");
		bind(UserDAO.class).to(Db4oUserDAO.class).in(Singleton.class);
	}

}
