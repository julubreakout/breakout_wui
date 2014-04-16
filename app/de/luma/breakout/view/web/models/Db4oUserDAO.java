package de.luma.breakout.view.web.models;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class Db4oUserDAO implements UserDAO {

	private static ObjectContainer db;
	
	@Inject
	public Db4oUserDAO(@Named("Db4oDbPath") String dbFile) {
		if (db == null) {
			db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), dbFile);
		}
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
		User prototype = new User();
		prototype.setEmail(email);
		ObjectSet<User> results = db.queryByExample(email);
		if(results.size() == 0)
			return null;
		
		return results.get(0);
	}

	
	
}
