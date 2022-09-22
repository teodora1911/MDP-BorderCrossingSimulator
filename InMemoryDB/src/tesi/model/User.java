package tesi.model;

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
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
	public String getNewPassword() {
		return this.newPassword;
	}
	
	@Override
	public boolean equals(Object object) {
		if(this == object)
			return true;
		if(object == null)
			return false;
		if(getClass() != object.getClass())
			return false;
		
		User other = (User) object;
		return this.username.equals(other.username);
	}
	
	@Override
	public int hashCode() {
		return username != null ? username.hashCode() : 13;
	}
}
