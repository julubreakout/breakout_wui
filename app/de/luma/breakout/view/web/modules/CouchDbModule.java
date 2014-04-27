package de.luma.breakout.view.web.modules;

import java.net.MalformedURLException;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;

import com.google.inject.Singleton;
import com.google.inject.name.Names;

import de.luma.breakout.view.web.datalayer.CouchDbUserDAO;
import de.luma.breakout.view.web.datalayer.UserDAO;

public class CouchDbModule extends BreakoutBaseModule {

	@Override
	protected void configure() {
		super.configure();

		try {
			// bind users database
			HttpClient httpClient = new StdHttpClient.Builder()
			.url("http://lenny2.in.htwg-konstanz.de:5984")
			.build();

			CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
			CouchDbConnector userDb = dbInstance.createConnector("breakout2_user_db", true);
			bind(CouchDbConnector.class).annotatedWith(Names.named("CouchDbUserConnector")).toInstance(userDb);

		} catch (MalformedURLException e) {
			throw new RuntimeException(e.getMessage());
		}

		bind(UserDAO.class).to(CouchDbUserDAO.class).in(Singleton.class);
	}

}
