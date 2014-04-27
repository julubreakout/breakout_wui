package de.luma.breakout.view.web.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.luma.breakout.view.web.controllers.Application;
import de.luma.breakout.view.web.controllers.UserController;

public class BreakoutBaseModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(UserController.class).in(Singleton.class);
		bind(Application.class).in(Singleton.class);
	}

}
