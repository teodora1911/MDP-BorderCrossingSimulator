package tesi.chat.model;

import java.util.Objects;

public class User {
	
	public static final String POLICE = "P";
	public static final String CUSTOMS = "C";
	
	private String username;
	private String terminal;
	private int entry;
	private String control;
	
	public User(String username, String terminal, int entry, String control) {
		this.username = username;
		this.terminal = terminal;
		this.entry = entry;
		this.control = control;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	public int getEntry() {
		return entry;
	}

	public void setEntry(int entry) {
		this.entry = entry;
	}

	public String getControl() {
		return control;
	}

	public void setControl(String control) {
		this.control = control;
	}

	@Override
	public int hashCode() {
		return Objects.hash(control, entry, terminal);
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
		return Objects.equals(control, other.control) && entry == other.entry
				&& Objects.equals(terminal, other.terminal);
	}
}
