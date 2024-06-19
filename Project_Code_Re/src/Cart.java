import java.sql.ResultSet;
import java.sql.SQLException;

public class Cart {
	String username, juiceId;
	int quantity;

	public Cart(String username, String juiceId, int quantity) {
		this.username = username;
		this.juiceId = juiceId;
		this.quantity = quantity;
	}

	String getUsername() {
		return username;
	}

	void setUsername(String username) {
		this.username = username;
	}

	String getJuiceId() {
		return juiceId;
	}

	void setJuiceId(String juiceId) {
		this.juiceId = juiceId;
	}

	int getQuantity() {
		return quantity;
	}

	void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public static Cart fromResultSet(ResultSet rs) {
		try {
			String name = rs.getString("Username");
			String juiceId = rs.getString("JuiceId");
			int quantity = Integer.parseInt(rs.getString("Quantity"));
			return new Cart(name, juiceId, quantity);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
