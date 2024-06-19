import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import util.Connect;

public class CustomerPage {
	Stage primaryStage;
	User user;
	private Connect connect = Connect.getInstance();
	ArrayList<TransactionHeader> thList = new ArrayList<>();
	ArrayList<TransactionDetail> tdList = new ArrayList<>();
	ArrayList<Juice> juiceList = new ArrayList<>();
	ArrayList<Cart> cartList = new ArrayList<>();

	public CustomerPage(Stage primaryStage, User user) {
		super();
		this.primaryStage = primaryStage;
		this.user = user;
		getTransactionHeader();
		getTransactionDetail();
		getJuice();
		getCart();
	}

	public void getTransactionHeader() {

		String query = "SELECT * FROM transactionheader";
		connect.rs = connect.execQuery(query);
		try {
			while (connect.rs.next()) {
				thList.add(TransactionHeader.fromResultSet(connect.rs));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getTransactionDetail() {
		String query = "SELECT * FROM transactiondetail";
		connect.rs = connect.execQuery(query);
		try {
			while (connect.rs.next()) {
				tdList.add(TransactionDetail.fromResultSet(connect.rs));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getJuice() {
		String query = "SELECT * FROM msjuice";
		connect.rs = connect.execQuery(query);
		try {
			while (connect.rs.next()) {
				juiceList.add(Juice.fromResultSet(connect.rs));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getCart() {
		String query = "SELECT * FROM cartdetail";
		connect.rs = connect.execQuery(query);
		try {
			while (connect.rs.next()) {

				Cart cart = Cart.fromResultSet(connect.rs);
				if (cart.getUsername().equals(user.getUsername())) {
					cartList.add(cart);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addCart(String id, int quantity) {
		String query = "INSERT INTO cartdetail VALUES(?, ?, ?)";
		PreparedStatement ps = connect.addQuery(query);
		try {
			ps.setString(1, user.getUsername());
			ps.setString(2, id);
			ps.setInt(3, quantity);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addTransactionHeader(String id, String paymentType) {
		String query = "INSERT INTO transactionheader VALUES(?, ?, ?)";
		PreparedStatement ps = connect.addQuery(query);
		try {
			ps.setString(2, user.getUsername());
			ps.setString(1, id);
			ps.setString(3, paymentType);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addTransactionDetail(String transactionId, String juiceId, int quantity) {
		String query = "INSERT INTO transactiondetail VALUES(?, ?, ?)";
		PreparedStatement ps = connect.addQuery(query);
		try {
			ps.setString(1, transactionId);
			ps.setString(2, juiceId);
			ps.setInt(3, quantity);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void deleteCart(String id) {
		String query = "DELETE FROM cartdetail WHERE JuiceId = ? AND Username = ?";
		PreparedStatement ps = connect.addQuery(query);
		try {
			ps.setString(1, id);
			ps.setString(2, user.getUsername());
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void deleteAllCart() {
		String query = "DELETE FROM cartdetail WHERE Username = ?";
		PreparedStatement ps = connect.addQuery(query);
		try {
			ps.setString(1, user.getUsername());
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void updateCart(String id, int quantity) {
		String query = "UPDATE cartdetail SET quantity = quantity + ? WHERE Username = ? AND JuiceId = ?";
		PreparedStatement ps = connect.addQuery(query);
		try {
			ps.setInt(1, quantity);
			ps.setString(2, user.getUsername());
			ps.setString(3, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	int juiceIndex = -1;
	String juiceName;

	public void ShowCustomerPage() {
		BorderPane parent = new BorderPane();
		Button logout = new Button("Logout");
		CustomMenuItem logoutItem = new CustomMenuItem(logout);
		Factory f = new Factory();
		Text text = f.createText(String.format("Hi, %s", user.getUsername()));
		Region region = new Region();
		ToolBar menuBar = new ToolBar(logout, region, text);

		HBox hbox1 = new HBox(menuBar);
		VBox vbox1 = new VBox();
		Label cart = new Label("Your Cart");
		cart.setFont(Font.font("Arial", FontWeight.BOLD, 60));
		vbox1.getChildren().addAll(cart);
		vbox1.setAlignment(Pos.TOP_CENTER);
		vbox1.setMargin(cart, new Insets(100, 0, 0, 0));
		hbox1.getChildren().addAll(text);
		hbox1.setHgrow(menuBar, Priority.ALWAYS);
		hbox1.setHgrow(region, Priority.ALWAYS);
		parent.setTop(hbox1);
		parent.setCenter(vbox1);
		Text emptyCart = f.createText("Your cart is empty, try adding items!");

		int check = 0;
		int totalPrice = 0;
		ArrayList<String> list = new ArrayList<>();
		for (int i = 0; i < cartList.size(); i++) {
			if (cartList.get(i).getUsername().equals(user.getUsername())) {
				check++;
				int price = 0;
				String juiceName = "";
				for (int j = 0; j < juiceList.size(); j++) {
					if (cartList.get(i).getJuiceId().equals(juiceList.get(j).getJuiceId())) {
						juiceName = juiceList.get(j).getName();
						price = juiceList.get(j).getPrice() * cartList.get(i).getQuantity();
						list.add(String.format("%dx %s - [Rp. %d]", cartList.get(i).getQuantity(), juiceName, price));
						totalPrice += price;
					}
				}
			}
		}
		ListView<String> listView = new ListView<>();
		ObservableList<String> obs;

		if (check == 0) {
			vbox1.getChildren().add(emptyCart);
			vbox1.setMargin(emptyCart, new Insets(20, 0, 20, 0));
		} else {
			obs = FXCollections.observableArrayList(list);
			listView.setItems(obs);
			listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			vbox1.getChildren().add(listView);
			vbox1.setMargin(listView, new Insets(20, 0, 0, 0));
			listView.setMaxWidth(400);
			listView.setMaxHeight(150);
			Text total = f.createText(String.format("Total Price: %d", totalPrice));
			total.setFont(Font.font("Arial", 30));
			vbox1.getChildren().add(total);
			vbox1.setMargin(total, new Insets(20, 0, 20, 0));
		}

		HBox buttonBox = new HBox();
		Button addCart = new Button("Add New Item to Cart");
		Button deleteCart = new Button("Delete Item from Cart");
		Button checkout = new Button("Checkout");
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.getChildren().addAll(addCart, deleteCart, checkout);
		buttonBox.setMargin(addCart, new Insets(0, 10, 0, 0));
		buttonBox.setMargin(deleteCart, new Insets(0, 10, 0, 0));
		buttonBox.setMargin(checkout, new Insets(0, 10, 0, 0));
		vbox1.getChildren().add(buttonBox);
		addCart.setPrefWidth(140);
		deleteCart.setPrefWidth(140);
		checkout.setPrefWidth(140);

		Scene scene = new Scene(parent, 800, 600);
		primaryStage.setScene(scene);
		primaryStage.show();

		logout.setOnAction(e -> {
			LoginMenu login = new LoginMenu(primaryStage);
			login.loadLogin();
			return;
		});

		String radioInput;

		addCart.setOnAction(e -> {
			Window addWindow = new Stage();
			((Stage) addWindow).initModality(Modality.APPLICATION_MODAL);
			((Stage) addWindow).initOwner(primaryStage);
			BorderPane addWindowPane = new BorderPane();
			VBox mainBox = new VBox();
			HBox topBox = new HBox();
			Text title = f.createText("Add New Item");
			title.setFill(Color.WHITE);
			title.setFont(Font.font("Arial", FontWeight.BOLD, 30));
			topBox.getChildren().addAll(title);
			HBox.setHgrow(topBox, Priority.ALWAYS);
			topBox.setAlignment(Pos.CENTER);
			title.setTextAlignment(TextAlignment.CENTER);
			title.setFont(Font.font("Arial", 15));
			topBox.setPadding(new Insets(10, 100, 10, 100));
			BackgroundFill bgFill = new BackgroundFill(Color.GRAY, null, null);
			Background bg = new Background(bgFill);
			topBox.setBackground(bg);

			mainBox.setAlignment(Pos.CENTER);
			ComboBox<String> comboBox = new ComboBox<>();
			for (int i = 0; i < juiceList.size(); i++) {
				comboBox.getItems().add(juiceList.get(i).getName());
			}
			Label juicePrice = f.createLabel("Juice Price:-");
			Label juice = f.createLabel("Juice: ");
			HBox comboBoxSet = new HBox();
			comboBoxSet.getChildren().addAll(comboBox, juicePrice);
			comboBoxSet.setAlignment(Pos.CENTER);
			comboBoxSet.setMargin(comboBox, new Insets(0, 15, 15, 0));
			Label desc = f.createLabel("");
			desc.setVisible(false);
			desc.setWrapText(true);
			Label quantity = f.createLabel("Quantity: ");
			SpinnerValueFactory<Integer> spinnerFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000,
					1);
			Label totalJuicePrice = f.createLabel("Total Price:-");
			Spinner<Integer> quantitySpinner = new Spinner<>(spinnerFactory);
			Button addItem = new Button("Add Item");

			mainBox.getChildren().addAll(juice, comboBoxSet, desc, quantitySpinner, totalJuicePrice, addItem);
			mainBox.setMaxWidth(300);
			mainBox.setMargin(juice, new Insets(-100, 0, 10, 0));
			mainBox.setMargin(comboBoxSet, new Insets(0, 0, 10, 0));
			mainBox.setMargin(desc, new Insets(0, 0, 10, 0));
			mainBox.setMargin(quantitySpinner, new Insets(0, 0, 10, 0));
			mainBox.setMargin(totalJuicePrice, new Insets(0, 0, 10, 0));
			mainBox.setMargin(addItem, new Insets(0, 0, 10, 0));

			addWindowPane.setTop(topBox);
			addWindowPane.setCenter(mainBox);
			Scene popUpScene = new Scene(addWindowPane, 450, 450);
			((Stage) addWindow).setScene(popUpScene);
			((Stage) addWindow).show();

			comboBox.setOnAction(s -> {
				String comboPick = comboBox.getValue();
				int price = 0;
				for (int i = 0; i < juiceList.size(); i++) {
					if (juiceList.get(i).getName().equals(comboPick)) {
						price = juiceList.get(i).getPrice();
						juicePrice.setText(String.format("Juice Price: %d", price));
						desc.setText(juiceList.get(i).getDesc());
						desc.setVisible(true);
						totalJuicePrice.setText(String.format("Total Price: %d",
								quantitySpinner.getValue() * juiceList.get(i).getPrice()));
						juiceIndex = i;
						break;
					}
				}
			});

			quantitySpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
				if (juiceIndex != -1) {
					totalJuicePrice
							.setText(String.format("Total Price: %d", juiceList.get(juiceIndex).getPrice() * newValue));
				}
			});

			addItem.setOnAction(s -> {
				if (comboBox.getSelectionModel().getSelectedItem() == null) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setContentText("Juice not picked yet");
					alert.show();
				} else {
					int checkUnique = 0;
					int cartIndex = 0;
					for (int i = 0; i < cartList.size(); i++) {
						if (cartList.get(i).getJuiceId().equals(juiceList.get(juiceIndex).getJuiceId())) {
							checkUnique++;
							cartIndex = i;
							break;
						}
					}
					if (checkUnique == 0) {
						addCart(juiceList.get(juiceIndex).getJuiceId(), quantitySpinner.getValue());
						cartList.add(new Cart(user.getUsername(), juiceList.get(juiceIndex).getJuiceId(),
								quantitySpinner.getValue()));
					} else {
						updateCart(juiceList.get(juiceIndex).getJuiceId(), quantitySpinner.getValue());
						cartList.get(cartIndex)
								.setQuantity(cartList.get(cartIndex).getQuantity() + quantitySpinner.getValue());
					}
					((Stage) addWindow).close();
					CustomerPage cp = new CustomerPage(primaryStage, user);
					cp.ShowCustomerPage();
				}
			});

		});

		listView.getSelectionModel().selectedItemProperty().addListener((observable, oldVal, newVal) -> {
			juiceName = newVal.split("-")[0];
			juiceName.replaceFirst("^\\S+\\s", "").trim();
			juiceName = juiceName.split("x ")[1].trim();
		});

		deleteCart.setOnAction(e -> {
			if (cartList.isEmpty()) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText("Your cart is empty");
				alert.show();
			} else if (listView.getSelectionModel().getSelectedItems().isEmpty()) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText("Please choose which juice to delete");
				alert.show();
			} else {
				int index = 0;
				String juiceId = "";
				for (int i = 0; i < juiceList.size(); i++) {
					if (juiceList.get(i).getName().equals(juiceName)) {
						index = i;
						juiceId = juiceList.get(i).getJuiceId();
						break;
					}
				}
				deleteCart(juiceId);
				for (int i = 0; i < cartList.size(); i++) {
					if (cartList.get(i).getJuiceId().equals(juiceId)) {
						cartList.remove(i);
						break;
					}
				}
				CustomerPage cp = new CustomerPage(primaryStage, user);
				cp.ShowCustomerPage();
			}
		});

		checkout.setOnAction(e -> {
			if (cartList.isEmpty()) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText("You don't have any cart");
				alert.show();
			} else {
				showCheckoutPage();
			}
		});
	}

	public void showCheckoutPage() {
		BorderPane parent = new BorderPane();
		Button logout = new Button("Logout");
		CustomMenuItem logoutItem = new CustomMenuItem(logout);
		Factory f = new Factory();
		Text text = f.createText(String.format("Hi, %s", user.getUsername()));
		Region region = new Region();
		ToolBar menuBar = new ToolBar(logout, region, text);

		HBox hbox1 = new HBox(menuBar);
		VBox vbox1 = new VBox();
		Label checkoutLabel = new Label("Checkout");
		checkoutLabel.setFont(Font.font("Arial", FontWeight.BOLD, 60));
		vbox1.getChildren().addAll(checkoutLabel);
		vbox1.setAlignment(Pos.TOP_CENTER);
		vbox1.setMargin(checkoutLabel, new Insets(100, 0, 30, 0));
		hbox1.getChildren().addAll(text);
		hbox1.setHgrow(menuBar, Priority.ALWAYS);
		hbox1.setHgrow(region, Priority.ALWAYS);
		parent.setTop(hbox1);
		parent.setCenter(vbox1);
		VBox checkoutBox = new VBox();
		checkoutBox.setAlignment(Pos.CENTER_LEFT);

		Label[] lists = new Label[cartList.size()];
		int totalPrice = 0;
		for (int i = 0; i < cartList.size(); i++) {
			int index = 0;
			for (int j = 0; j < juiceList.size(); j++) {
				if (juiceList.get(j).getJuiceId().equals(cartList.get(i).getJuiceId())) {
					index = j;
					break;
				}
			}
			int tempQuan = cartList.get(i).getQuantity();
			String tempName = juiceList.get(index).getName();
			int tempPrice = juiceList.get(index).getPrice();
			lists[i] = f.createLabel(String.format("%dx %s    [%d x Rp. %d,- = Rp. %d,-]", tempQuan, tempName, tempQuan,
					tempPrice, (tempQuan * tempPrice)));
			totalPrice += (tempQuan * tempPrice);
			lists[i].setFont(Font.font("Arial", 15));
			lists[i].setTextAlignment(TextAlignment.LEFT);
			checkoutBox.getChildren().add(lists[i]);
			checkoutBox.setMargin(lists[i], new Insets(0, 0, 10, 200));
		}

		Label totalLabel = f.createLabel(String.format("Total Price: Rp. %d,-", totalPrice));
		checkoutBox.getChildren().add(totalLabel);
		checkoutBox.setMargin(totalLabel, new Insets(0, 0, 20, 200));

		Label paymentLabel = f.createLabel("Payment Type: ");
		paymentLabel.setFont(Font.font("Arial", 18));
		checkoutBox.getChildren().add(paymentLabel);
		checkoutBox.setMargin(paymentLabel, new Insets(0, 0, 10, 200));
		ToggleGroup toggleGroup = new ToggleGroup();
		RadioButton cashButton = new RadioButton("Cash");
		RadioButton debitButton = new RadioButton("Debit");
		RadioButton creditButton = new RadioButton("Credit");
		cashButton.setToggleGroup(toggleGroup);
		debitButton.setToggleGroup(toggleGroup);
		creditButton.setToggleGroup(toggleGroup);

		HBox radioButtonBox = new HBox();
		radioButtonBox.getChildren().addAll(cashButton, debitButton, creditButton);
		checkoutBox.getChildren().add(radioButtonBox);
		checkoutBox.setMargin(radioButtonBox, new Insets(0, 0, 20, 200));
		radioButtonBox.setMargin(cashButton, new Insets(0, 30, 0, 0));
		radioButtonBox.setMargin(creditButton, new Insets(0, 30, 0, 0));
		radioButtonBox.setMargin(debitButton, new Insets(0, 30, 0, 0));
		Button cancelButton = new Button("Cancel");
		Button checkoutButton = new Button("Checkout");

		cancelButton.setPrefWidth(100);
		checkoutButton.setPrefWidth(130);
		cancelButton.setPrefHeight(50);
		checkoutButton.setPrefHeight(50);

		HBox choiceBox = new HBox();
		choiceBox.getChildren().addAll(cancelButton, checkoutButton);
		choiceBox.setAlignment(Pos.CENTER);
		choiceBox.setMargin(cancelButton, new Insets(0, 15, 0, 0));

		vbox1.getChildren().addAll(checkoutBox, choiceBox);

		Scene scene = new Scene(parent, 800, 600);
		primaryStage.setScene(scene);
		primaryStage.show();

		cancelButton.setOnAction(e -> {
			ShowCustomerPage();
			return;
		});

		checkoutButton.setOnAction(e -> {
			if (toggleGroup.getSelectedToggle() == null) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText("Please select payment type");
				alert.show();
			} else {
				RadioButton selected = (RadioButton) toggleGroup.getSelectedToggle();
				String transactionId = String.format("TR%03d", thList.size() + 1);
				String paymentType = selected.getText();
				addTransactionHeader(transactionId, paymentType);
				for (int i = 0; i < cartList.size(); i++) {
					addTransactionDetail(transactionId, cartList.get(i).getJuiceId(), cartList.get(i).getQuantity());
				}
				cartList.removeAll(cartList);
				deleteAllCart();
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setContentText("All items checked out successfully, please proceed your payment");
				alert.show();
				ShowCustomerPage();
			}
		});
	}
}
