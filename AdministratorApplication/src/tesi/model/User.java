package tesi.model;

import java.util.Objects;

public class User {
	
	private String username;
	private String password;
	private String newPassword;
	
	public User() {
		super();
	}
	
	public User(String username, String password, String newPassword) {
		this.username = username;
		this.password = password;
		this.newPassword = newPassword;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(username, other.username);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(username);
	}
}
