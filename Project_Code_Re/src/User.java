import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
	String username, password, role;

	public User(String username, String password, String role) {
		super();
		this.username = username;
		this.password = password;
		this.role = role;
	}

	String getUsername() {
		return username;
	}

	void setUsername(String username) {
		this.username = username;
	}

	String getPassword() {
		return password;
	}

	void setPassword(String password) {
		this.password = password;
	}

	String getRole() {
		return role;
	}

	void setRole(String role) {
		this.role = role;
	}

	public static User fromResultSet(ResultSet rs) {
		try {
			String username = rs.getString("Username");
			String password = rs.getString("Password");
			String role = rs.getString("Role");
			return new User(username, password, role);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
