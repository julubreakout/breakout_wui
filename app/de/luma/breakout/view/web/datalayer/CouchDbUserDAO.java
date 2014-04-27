package de.luma.breakout.view.web.datalayer;

import org.ektorp.CouchDbConnector;
import org.ektorp.DocumentNotFoundException;
import org.ektorp.support.CouchDbRepositorySupport;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.luma.breakout.view.web.models.User;

/**
 * Provides acces to User objects stored in a CouchDb.
 * @author Lukas
 *
 */
public class CouchDbUserDAO extends CouchDbRepositorySupport<User> implements UserDAO {

	@Inject
	protected CouchDbUserDAO(@Named("CouchDbUserConnector") CouchDbConnector db) {
		super(User.class, db);
		initStandardDesignDocument();
	}

	@Override
	public void create(User user) {
		db.create(user);
	}

	@Override
	public void update(User user) {
		db.update(user);
	}

	@Override
	public void delete(User user) {
		db.delete(user);
	}

	@Override
	public User getByEmail(String email) {
		try {
			return db.get(User.class, email);
		} catch (DocumentNotFoundException e) {
			return null;
		}
	}

	@Override
	public void openConnection() {}

	@Override
	public void closeConnection() {}

}
