package de.luma.breakout.view.web;
import java.util.Set;

import play.Application;
import play.GlobalSettings;
import akka.actor.ActorSystem;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

import de.luma.breakout.view.web.datalayer.UserDAO;
import de.luma.breakout.view.web.modules.BreakoutBaseModule.DbList;
import de.luma.breakout.view.web.modules.CouchDbModule;
import de.luma.breakout.view.web.modules.Db4oModule;
import de.luma.breakout.view.web.modules.HibernateModule;

public class AppGlobal extends GlobalSettings {

	private final static Injector guice = Guice.createInjector(new CouchDbModule(),new HibernateModule(), new Db4oModule());  //  Db4oModule CouchDbModule
	private static ActorSystem actorSystem = null;
	public final static String GameName = "Breakout2";
	
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
		
		System.out.println("Creating actor system.");
		setActorSystem(ActorSystem.create("Breakout2ActorSystem"));
		
		
		
		for (UserDAO db : getInjector().getInstance(DbList.class).daos) {
			System.out.println("Opening database connection to "+db.getClass().getName());
			db.openConnection();
		}
		
	}
	
	
	
	@Override
	public void onStop(Application arg0) {
		super.onStop(arg0);
		
		
		for (UserDAO db : getInjector().getInstance(DbList.class).daos) {
			System.out.println("Closing database connection to "+db.getClass().getName());
			db.closeConnection();
		}
	}

	public static ActorSystem getActorSystem() {
		return actorSystem;
	}

	private static void setActorSystem(ActorSystem actorSystem) {
		AppGlobal.actorSystem = actorSystem;
	}

}
