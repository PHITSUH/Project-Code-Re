import java.sql.ResultSet;
import java.sql.SQLException;

public class Juice {
	private String juiceId, name, desc;
	private int price;

	public Juice(String juiceId, String name, String description, int price) {
		this.juiceId = juiceId;
		this.name = name;
		this.desc = description;
		this.price = price;
	}

	public String getJuiceId() {
		return juiceId;
	}

	public void setJuiceId(String juiceId) {
		this.juiceId = juiceId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public static Juice fromResultSet(ResultSet rs) {
		try {
			String name = rs.getString("JuiceName");
			String juiceId = rs.getString("JuiceId");
			String desc = rs.getString("JuiceDescription");
			int price = Integer.parseInt(rs.getString("Price"));
			return new Juice(juiceId, name, desc, price);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
