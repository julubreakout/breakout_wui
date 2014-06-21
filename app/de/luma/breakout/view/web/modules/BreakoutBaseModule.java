package de.luma.breakout.view.web.modules;

import java.util.Set;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Names;

import de.luma.breakout.view.web.controllers.Application;
import de.luma.breakout.view.web.controllers.UserController;
import de.luma.breakout.view.web.datalayer.UserDAO;
import de.luma.breakout.view.web.helpers.HighscorePoster;

public class BreakoutBaseModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(UserController.class).in(Singleton.class);
		bind(Application.class).in(Singleton.class);
		bind(HighscorePoster.class);
		bind(DbList.class);
		bind(String.class).annotatedWith(Names.named("HighscoreServerUrl")).toInstance("http://de-htwg-sa-highscores.herokuapp.com/");
	}
	
	public static class DbList {
		@Inject
		public Set<UserDAO> daos;
	}

}
