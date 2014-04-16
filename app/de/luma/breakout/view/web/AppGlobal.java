package de.luma.breakout.view.web;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.luma.breakout.view.web.modules.Db4oModule;
import play.GlobalSettings;

public class AppGlobal extends GlobalSettings {

	 private final static Injector guice = Guice.createInjector(new Db4oModule());
	 
	 public static Injector getInjector() {
		 return guice;
	 }
	
}
