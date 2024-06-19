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
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import util.Connect;

public class AdminPage {
	User user;
	Stage primaryStage;
	ArrayList<TransactionDetail> tdList = new ArrayList<>();
	ArrayList<TransactionHeader> thList = new ArrayList<>();
	ArrayList<Juice> juiceList = new ArrayList<>();
	ArrayList<Join> joinList = new ArrayList<>();

	public AdminPage(User user, Stage primaryStage) {
		this.user = user;
		this.primaryStage = primaryStage;
		getTransactionHeader();
		getTransactionDetail();
		getJuice();
		getJoinList();
	}

	private Connect connect = Connect.getInstance();

	public void getTransactionHeader() {

		String query = "SELECT * FROM transactionheader";
		connect.rs = connect.execQuery(query);
		try {
			while (connect.rs.next()) {
				// TODO feature envy
				thList.add(TransactionHeader.fromResultSet(connect.rs));
			}
		} catch (SQLException e) {
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

	public void getJoinList() {
		String query = "SELECT TransactionId, mj.JuiceId, JuiceName, Quantity FROM transactiondetail td JOIN msjuice mj ON mj.JuiceId = td.JuiceId ";
		connect.rs = connect.execQuery(query);
		try {
			while (connect.rs.next()) {

				joinList.add(Join.fromResultSet(connect.rs));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addJuice(String id, String name, String desc, int price) {
		String query = "INSERT INTO msjuice VALUES(?, ?, ?, ?)";
		PreparedStatement ps = connect.addQuery(query);
		try {
			ps.setString(1, id);
			ps.setString(2, name);
			ps.setInt(3, price);
			ps.setString(4, desc);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void updateJuice(String id, int price) {
		String query = "UPDATE msjuice SET Price = ? WHERE JuiceId = ?";
		PreparedStatement ps = connect.addQuery(query);
		try {
			ps.setInt(1, price);
			ps.setString(2, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void deleteJuice(String id) {
		String query = "DELETE FROM msjuice WHERE JuiceId = ?";
		PreparedStatement ps = connect.addQuery(query);
		try {
			ps.setString(1, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void deleteTransactionDetail(String id) {
		String query = "DELETE FROM transactiondetail WHERE JuiceId = ?";
		PreparedStatement ps = connect.addQuery(query);
		try {
			ps.setString(1, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	ObservableList<Join> joinObs;

	public void showViewTransaction() {
		BorderPane parent = new BorderPane();
		MenuBar menuBar = new MenuBar();
		Menu dashboard = new Menu("Admin's Dashboard");
		Menu logout = new Menu("Logout");
		MenuItem logoutMenu = new MenuItem("Logout from admin");
		MenuItem viewTransaction = new MenuItem("View Transaction");
		MenuItem manageProducts = new MenuItem("Manage Products");
		dashboard.getItems().addAll(viewTransaction, manageProducts);
		logout.getItems().add(logoutMenu);
		menuBar.getMenus().addAll(dashboard, logout);
		HBox hbox1 = new HBox(menuBar);
		VBox vbox1 = new VBox();
		parent.setTop(hbox1);
		parent.setCenter(vbox1);
		hbox1.setAlignment(Pos.TOP_LEFT);
		vbox1.setAlignment(Pos.TOP_CENTER);
		hbox1.setHgrow(menuBar, Priority.ALWAYS);
		hbox1.setMaxWidth(1000);

		Factory f = new Factory();
		Label viewLabel = f.createLabel("View Transaction");
		viewLabel.setFont(Font.font("Arial", FontWeight.BOLD, 25));
		TableView<TransactionHeader> tableTH = new TableView<>();
		ObservableList<TransactionHeader> thObs = FXCollections.observableArrayList(thList);

		TableColumn<TransactionHeader, String> idCol = new TableColumn<>("Transaction Id");
		idCol.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
		idCol.setMinWidth(100);

		TableColumn<TransactionHeader, String> paymentTypeCol = new TableColumn<>("Payment Type");
		paymentTypeCol.setCellValueFactory(new PropertyValueFactory<>("paymentType"));
		paymentTypeCol.setMinWidth(100);

		TableColumn<TransactionHeader, String> usernameCol = new TableColumn<>("Username");
		usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
		usernameCol.setMinWidth(100);

		tableTH.getColumns().addAll(idCol, paymentTypeCol, usernameCol);
		tableTH.setMaxHeight(200);
		tableTH.setMaxWidth(300);
		tableTH.setItems(thObs);
		tableTH.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		tableTH.setEditable(false);

		TableView<Join> tableJoin = new TableView<>();

		TableColumn<Join, String> transactionIdCol = new TableColumn<>("Transaction Id");
		transactionIdCol.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
		transactionIdCol.setMinWidth(100);

		TableColumn<Join, String> juiceIdCol = new TableColumn<>("Juice Id");
		juiceIdCol.setCellValueFactory(new PropertyValueFactory<>("juiceId"));
		juiceIdCol.setMinWidth(100);

		TableColumn<Join, String> juiceNameCol = new TableColumn<>("Juice Name");
		juiceNameCol.setCellValueFactory(new PropertyValueFactory<>("juiceName"));
		juiceNameCol.setMinWidth(100);

		TableColumn<Join, Integer> quantityCol = new TableColumn<>("Quantity");
		quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		quantityCol.setMinWidth(100);

		tableJoin.getColumns().addAll(transactionIdCol, juiceIdCol, juiceNameCol, quantityCol);
		tableJoin.setMaxHeight(200);
		tableJoin.setMaxWidth(400);

		vbox1.getChildren().addAll(viewLabel, tableTH, tableJoin);
		vbox1.setMargin(viewLabel, new Insets(100, 0, 20, 0));
		vbox1.setMargin(tableTH, new Insets(0, 0, 20, 0));
		tableJoin.setPlaceholder(f.createLabel("Click on a transation header to view the transaction detail"));

		Scene scene = new Scene(parent, 800, 600);
		primaryStage.setScene(scene);
		primaryStage.show();

		tableTH.setOnMouseClicked(e -> {
			int index = tableTH.getSelectionModel().getSelectedIndex();
			TransactionHeader th = tableTH.getItems().get(index);
//			System.out.println(th.getTransactionId());
			ArrayList<Join> result = new ArrayList<>();
			for (int i = 0; i < joinList.size(); i++) {
				if (joinList.get(i).getTransactionId().equals(th.getTransactionId())) {
					result.add(joinList.get(i));
				}
			}
			joinObs = FXCollections.observableArrayList(result);
			tableJoin.setItems(joinObs);
		});

		logoutMenu.setOnAction(e -> {
			LoginMenu lm = new LoginMenu(primaryStage);
			lm.loadLogin();
			return;
		});

		manageProducts.setOnAction(e -> {
			showManageProducts();
		});
	}

	public void showManageProducts() {
		BorderPane parent = new BorderPane();
		MenuBar menuBar = new MenuBar();
		Menu dashboard = new Menu("Admin's Dashboard");
		Menu logout = new Menu("Logout");
		MenuItem logoutMenu = new MenuItem("Logout from admin");
		MenuItem viewTransaction = new MenuItem("View Transaction");
		MenuItem manageProducts = new MenuItem("Manage Products");
		dashboard.getItems().addAll(viewTransaction, manageProducts);
		logout.getItems().add(logoutMenu);
		menuBar.getMenus().addAll(dashboard, logout);
		HBox hbox1 = new HBox(menuBar);
		VBox vbox1 = new VBox();
		parent.setTop(hbox1);
		hbox1.setAlignment(Pos.TOP_LEFT);
		vbox1.setAlignment(Pos.TOP_CENTER);
		hbox1.setHgrow(menuBar, Priority.ALWAYS);
		hbox1.setMaxWidth(1000);
		parent.setCenter(vbox1);

		Factory f = new Factory();
		Label manageLabel = f.createLabel("Manage Products");
		manageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));
		TableView<Juice> tableJuice = new TableView<>();
		ObservableList<Juice> juiceObs = FXCollections.observableArrayList(juiceList);

		TableColumn<Juice, String> idCol = new TableColumn<>("Juice Id");
		idCol.setCellValueFactory(new PropertyValueFactory<>("juiceId"));
		idCol.setMinWidth(150);

		TableColumn<Juice, String> juiceNameCol = new TableColumn<>("Juice Name");
		juiceNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		juiceNameCol.setMinWidth(150);

		TableColumn<Juice, Integer> priceCol = new TableColumn<>("Price");
		priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
		priceCol.setMinWidth(150);

		TableColumn<Juice, String> descCol = new TableColumn<>("Juice Description");
		descCol.setCellValueFactory(new PropertyValueFactory<>("desc"));
		descCol.setMinWidth(150);

		tableJuice.getColumns().addAll(idCol, juiceNameCol, priceCol, descCol);
		tableJuice.setMaxHeight(200);
		tableJuice.setMaxWidth(600);
		tableJuice.setItems(juiceObs);

		HBox editBox = new HBox();
		VBox textBox = new VBox();
		textBox.setMaxWidth(200);
		Label comboLabel = new Label("Product ID to delete/update: ");
		comboLabel.setWrapText(true);
		Label priceLabel = new Label("Price: ");
		Label productLabel = new Label("Product Name");
		textBox.getChildren().addAll(comboLabel, priceLabel, productLabel);
		textBox.setMargin(comboLabel, new Insets(0, 0, 20, 0));
		textBox.setMargin(priceLabel, new Insets(0, 0, 20, 0));
		textBox.setMargin(productLabel, new Insets(0, 0, 20, 0));

		VBox inputBox = new VBox();
		ComboBox<String> juiceCombo = new ComboBox<>();
		for (int i = 0; i < juiceList.size(); i++) {
			juiceCombo.getItems().add(juiceList.get(i).getJuiceId());
		}
		Spinner<Integer> priceSpinner = new Spinner<>(0, 100000, 10000, 100);
		TextField nameField = new TextField();
		nameField.setPromptText("Insert product name to be created");
		TextArea descField = new TextArea();
		descField.setPromptText("Insert the new product's text description, min. 10 & max. 100");
		nameField.setPrefWidth(300);
		descField.setPrefWidth(300);
		descField.setPrefHeight(100);
		descField.setWrapText(true);
		Region space = new Region();
		inputBox.getChildren().addAll(juiceCombo, priceSpinner, nameField, descField, space);
		inputBox.setMargin(juiceCombo, new Insets(0, 0, 10, 0));
		inputBox.setMargin(priceSpinner, new Insets(0, 0, 10, 0));
		inputBox.setMargin(nameField, new Insets(0, 0, 10, 0));

		VBox buttonBox = new VBox();
		Button insertButton = new Button("Insert Juice");
		Button updateButton = new Button("Update Price");
		Button deleteButton = new Button("Remove Juice");
		insertButton.setPrefHeight(30);
		updateButton.setPrefHeight(30);
		deleteButton.setPrefHeight(30);
		buttonBox.getChildren().addAll(insertButton, updateButton, deleteButton);
		buttonBox.setMargin(insertButton, new Insets(0, 0, 10, 0));
		buttonBox.setMargin(updateButton, new Insets(0, 0, 10, 0));

		editBox.getChildren().addAll(textBox, inputBox, buttonBox);
		editBox.setMargin(textBox, new Insets(0, 20, 0, 0));
		editBox.setMargin(inputBox, new Insets(0, 20, 0, 0));
		editBox.setAlignment(Pos.CENTER);

		vbox1.getChildren().addAll(manageLabel, tableJuice, editBox);
		vbox1.setMargin(manageLabel, new Insets(100, 0, 10, 0));
		vbox1.setMargin(tableJuice, new Insets(0, 0, 10, 0));

		Scene scene = new Scene(parent, 800, 600);
		primaryStage.setScene(scene);
		primaryStage.show();

		deleteButton.setOnAction(e -> {
			if (juiceCombo.getSelectionModel().getSelectedItem() == null) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText("Please fill all the required field");
				alert.show();
			} else {
				String id = juiceCombo.getValue();
				for (int i = 0; i < juiceList.size(); i++) {
					Juice juice = juiceList.get(i);
					if (juice.getJuiceId().equals(id)) {
						deleteTransactionDetail(id);
						deleteJuice(id);
						juiceList.remove(i);
						AdminPage ap = new AdminPage(user, primaryStage);
						ap.showManageProducts();
						break;
					}
				}
			}
		});

		updateButton.setOnAction(e -> {
			if (juiceCombo.getValue().isEmpty()) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText("Please fill all the required field");
				alert.show();
			} else {
				String id = juiceCombo.getValue();
				int price = priceSpinner.getValue();
				updateJuice(id, price);
				for (int i = 0; i < juiceList.size(); i++) {
					if (juiceList.get(i).getJuiceId().equals(id)) {
						juiceList.get(i).setPrice(price);
						break;
					}
				}
				AdminPage ap = new AdminPage(user, primaryStage);
				ap.showManageProducts();
			}
		});

		insertButton.setOnAction(e -> {
			if (nameField.getText().isEmpty() || descField.getText().isEmpty()) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText("Please fill all the required field");
				alert.show();
			} else if (descField.getText().length() < 10 || descField.getText().length() > 100) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText("Description is not right");
				alert.show();
			} else {
				String name = nameField.getText();
				String desc = descField.getText();
				int price = priceSpinner.getValue();
				String id = String.format("JU%03d", juiceList.size() + 1);
				addJuice(id, name, desc, price);
				juiceList.add(new Juice(id, name, desc, price));
				AdminPage ap = new AdminPage(user, primaryStage);
				ap.showManageProducts();
			}
		});

		logoutMenu.setOnAction(e -> {
			LoginMenu lm = new LoginMenu(primaryStage);
			lm.loadLogin();
			return;
		});

		viewTransaction.setOnAction(e -> {
			showViewTransaction();
		});
	}
}
