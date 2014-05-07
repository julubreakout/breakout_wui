package de.luma.breakout.view.web.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonProperty;

@Entity
@Table(name="breakout2_players")
public class User implements IUser {

	@Id @Column(name="email")
	private String email;

	@Column(name="name")
	private String name;

	@Column(name="password")
	private String password;
	
	@Column(name="highscore")
	private int highscore;

	@Transient
	private String revision;

	public User() {
		setHighscore(0);
	}
	

	public int getHighscore() {
		return highscore;
	}

	public void setHighscore(int highscore) {
		this.highscore = highscore;
	}

	@JsonProperty("_id")
	public String getEmail() {
		return email;
	}

	@JsonProperty("_id")
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Transient @JsonProperty("_rev")
	public String getRevision() {
		return revision;
	}
	@Transient @JsonProperty("_rev")
	public void setRevision(String s) {
		this.revision = s;
	}

}
