import java.sql.ResultSet;
import java.sql.SQLException;

public class Join {
	private String transactionId, juiceId, juiceName;
	private int quantity;

	public Join(String transactionId, String juiceId, String juiceName, int quantity) {
		super();
		this.transactionId = transactionId;
		this.juiceId = juiceId;
		this.juiceName = juiceName;
		this.quantity = quantity;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getJuiceId() {
		return juiceId;
	}

	public void setJuiceId(String juiceId) {
		this.juiceId = juiceId;
	}

	public String getJuiceName() {
		return juiceName;
	}

	public void setJuiceName(String juiceName) {
		this.juiceName = juiceName;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public static Join fromResultSet(ResultSet rs) {
		try {
			String transactionId = rs.getString("TransactionId");
			String juiceId = rs.getString("JuiceId");
			String name = rs.getString("JuiceName");
			int quantity = Integer.parseInt(rs.getString("Quantity"));
			return new Join(transactionId, juiceId, name, quantity);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

}
