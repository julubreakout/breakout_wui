package de.luma.breakout.view.web.datalayer;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Query;
import com.db4o.reflect.jdk.JdkReflector;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.luma.breakout.view.web.models.User;

/**
 * Provides acces to User objects stored in a Db4o database.
 * @author Lukas
 *
 */
public class Db4oUserDAO implements UserDAO {

	private ObjectContainer db;
	private String dbFile;
	
	@Inject
	public Db4oUserDAO(@Named("Db4oDbPath") String dbFile) {
		this.dbFile = dbFile;
	}
	
	@Override
	public void create(User user) {
		update(user);
	}

	@Override
	public void update(User user) {
		db.store(user);
		db.commit();
	}

	@Override
	public void delete(User user) {
		db.delete(user);
		db.commit();
	}

	@Override
	public User getByEmail(String email) {
		Query query = db.query();
		query.constrain(User.class);
		query.descend("email").constrain(email).equal();

		ObjectSet<User> results = query.execute();
		if(results.size() == 0)
			return null;
		
		return results.get(0);
	}

	@Override
	public void openConnection() {
		EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
		configuration.common().reflectWith(
		        new JdkReflector(Thread.currentThread().getContextClassLoader()));
		
		db = Db4oEmbedded.openFile(configuration, dbFile);
	}

	@Override
	public void closeConnection() {
		if (db != null) {
			db.commit();
			db.close();
		}
	}

	
	
}
