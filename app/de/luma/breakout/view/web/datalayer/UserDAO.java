package de.luma.breakout.view.web.datalayer;

import de.luma.breakout.view.web.models.User;

public interface UserDAO {

	void create(User user);
	void update(User user);
	void delete(User user);
	
	User getByEmail(String email);
	
	void openConnection();
	void closeConnection();
		
}
