package tesi.model;

import java.util.Objects;

public class User {
	
	private String username;
	private String password;
	private String newPassword;
	private Terminal terminal;
	
	public User(String username, String password, String newPassword, Terminal terminal) {
		this.username = username;
		this.password = password;
		this.newPassword = newPassword;
		this.terminal = terminal;
	}
	
	public User(String username, String password, String newPassword) {
		this(username, password, newPassword, null);
	}
	
	public User(String username, String password, Terminal terminal) {
		this(username, password, null, terminal);
	}

	public User(String username, String password) {
		this(username, password, null, null);
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

	public Terminal getTerminal() {
		return terminal;
	}

	public void setTerminal(Terminal terminal) {
		this.terminal = terminal;
	}

	@Override
	public int hashCode() {
		return Objects.hash(username);
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
}
