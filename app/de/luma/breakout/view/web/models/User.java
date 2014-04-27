package de.luma.breakout.view.web.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonProperty;

@Entity
@Table(name="USER")
public class User implements IUser {

	@Id @Column(name="EMAIL")
	private String email;

	@Column(name="NAME")
	private String name;

	@Column(name="PASSWORD")
	private String password;

	private String revision;


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
