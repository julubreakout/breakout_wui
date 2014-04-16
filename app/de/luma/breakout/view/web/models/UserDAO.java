package de.luma.breakout.view.web.models;

public interface UserDAO {

	public void create(User user);
	public void update(User user);
	public void delete(User user);
	
	public User getByEmail(String email);
		
}
