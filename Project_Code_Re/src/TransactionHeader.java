import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionHeader {
	private String transactionId, username, paymentType;

	public TransactionHeader(String transactionId, String username, String paymentType) {
		this.transactionId = transactionId;
		this.username = username;
		this.paymentType = paymentType;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public static TransactionHeader fromResultSet(ResultSet rs) {
		try {
			String username = rs.getString("Username");
			String id = rs.getString("TransactionId");
			String paymentType = rs.getString("PaymentType");
			return new TransactionHeader(id, username, paymentType);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
