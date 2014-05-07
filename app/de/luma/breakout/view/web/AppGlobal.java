package de.luma.breakout.view.web;
import com.google.inject.Guice;
import com.google.inject.Injector;

import de.luma.breakout.view.web.datalayer.UserDAO;
import de.luma.breakout.view.web.modules.CouchDbModule;
import de.luma.breakout.view.web.modules.Db4oModule;
import de.luma.breakout.view.web.modules.HibernateModule;
import play.Application;
import play.GlobalSettings;

public class AppGlobal extends GlobalSettings {

	private final static Injector guice = Guice.createInjector(new HibernateModule());  //  Db4oModule CouchDbModule

	public static Injector getInjector() {
		return guice;
	}

	@Override
	public <A> A getControllerInstance(Class<A> controllerClass)
			throws Exception {
		
		return getInjector().getInstance(controllerClass);
	}
	
	@Override
	public void onStart(Application arg0) {
		super.onStart(arg0);
		System.out.println("Opening database connection.");
		getInjector().getInstance(UserDAO.class).openConnection();
	}
	
	@Override
	public void onStop(Application arg0) {
		super.onStop(arg0);
		System.out.println("Closing database connection.");
		getInjector().getInstance(UserDAO.class).closeConnection();
	}

}
