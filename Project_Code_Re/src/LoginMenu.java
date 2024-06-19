
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import util.Connect;

public class LoginMenu {
	private Stage primaryStage;
	ArrayList<User> users = new ArrayList<>();

	public LoginMenu(Stage primaryStage) {
		// TODO Auto-generated constructor stub
		getData();
		this.primaryStage = primaryStage;
	}

	private Connect connect = Connect.getInstance();

	public void getData() {
		users.removeAll(users);

		String query = "SELECT * FROM msuser";
		connect.rs = connect.execQuery(query);
		try {
			while (connect.rs.next()) {

				users.add(User.fromResultSet(connect.rs));
			}
			System.out.println(connect.rsm.getColumnCount());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addUser(String username, String password) {
		String query = "INSERT INTO msuser VALUES(?, ?, ?)";
		PreparedStatement ps = connect.addQuery(query);
		try {
			ps.setString(1, username);
			ps.setString(2, password);
			ps.setString(3, "Customer");
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void loadLogin() {
		BorderPane parent = new BorderPane();
		MenuBar menuBar = new MenuBar();
		Menu menu = new Menu("Dashboard");
		MenuItem loginMenu = new MenuItem("Login");
		MenuItem registerMenu = new MenuItem("Register");
		menu.getItems().addAll(loginMenu, registerMenu);
		menuBar.getMenus().add(menu);
		HBox hbox1 = new HBox(menuBar);
		VBox vbox1 = new VBox();
		parent.setTop(hbox1);
		hbox1.setAlignment(Pos.TOP_LEFT);
		vbox1.setAlignment(Pos.TOP_CENTER);
		hbox1.setHgrow(menuBar, Priority.ALWAYS);
		hbox1.setMaxWidth(1000);
		Factory factory = new Factory();
		Text login = factory.createText("Login");
		login.setStyle("-fx-font: 60 arial;");
		login.setFont(Font.font("Arial", FontWeight.BOLD, 50));
		Text name = factory.createText("NJuice");
		name.setStyle("-fx-font: 25 arial;");
		vbox1.getChildren().addAll(login, name);
		vbox1.setSpacing(15);

		VBox insertBox = new VBox();
		HBox usernameBox = new HBox();
		HBox usernameTextBox = new HBox();
		Label usernameLabel = factory.createLabel("Username");
		usernameTextBox.getChildren().add(usernameLabel);
		usernameTextBox.setMargin(usernameLabel, new Insets(0, 0, 0, 250));
		TextField username = new TextField();
		username.setPromptText("Enter Username");
		usernameBox.setHgrow(username, Priority.ALWAYS);
		username.setMaxWidth(300);
		usernameBox.getChildren().add(username);
		usernameBox.setMargin(username, new Insets(0, 0, 25, 250));
		HBox passwordBox = new HBox();
		PasswordField password = new PasswordField();
		password.setPromptText("Enter Password");
		password.setMaxWidth(300);
		passwordBox.getChildren().add(password);
		passwordBox.setHgrow(password, Priority.ALWAYS);
		passwordBox.setMargin(password, new Insets(0, 0, 10, 250));
		HBox passwordTextBox = new HBox();
		Label passwordLabel = factory.createLabel("Password");
		passwordTextBox.getChildren().add(passwordLabel);
		passwordTextBox.setMargin(passwordLabel, new Insets(0, 0, 0, 250));
		Label alert = new Label("Credentials Failed!");
		alert.setVisible(false);
		alert.setTextFill(Color.RED);
		Button loginButton = new Button("Login");
		insertBox.getChildren().addAll(usernameTextBox, usernameBox, passwordTextBox, passwordBox, alert);
		insertBox.setMargin(alert, new Insets(0, 0, 30, 250));
		parent.setCenter(vbox1);
		vbox1.getChildren().addAll(insertBox, loginButton);
		vbox1.setMargin(login, new Insets(100, 0, 0, 0));

		Scene scene = new Scene(parent, 800, 600);
		primaryStage.setScene(scene);
		primaryStage.show();

		registerMenu.setOnAction(e -> {
			loadRegister();
		});

		loginButton.setOnAction(e -> {
			if (username.getText().isEmpty() || password.getText().isEmpty()) {
				alert.setVisible(true);
			} else {
				String usernameInput = username.getText();
				String passwordInput = password.getText();

				int checkUsername = 0, checkPassword = 0;
				int index = 0;
				for (int i = 0; i < users.size(); i++) {
					if (users.get(i).getUsername().equals(usernameInput)) {
						checkUsername++;
						index = i;
						break;
					}
				}
				if (checkUsername == 0) {
					alert.setVisible(true);
				} else {
					// TODO Message Chain
					User user = users.get(index);
					if (user.getPassword().equals(passwordInput)) {
						checkPassword++;
					}
					if (checkPassword == 0) {
						alert.setVisible(true);
					} else {
						if (user.getRole().equals("Admin")) {
							AdminPage admin = new AdminPage(user, primaryStage);
							admin.showViewTransaction();
						} else {
							CustomerPage customer = new CustomerPage(primaryStage, user);
							customer.ShowCustomerPage();
						}
					}
				}
			}
		});
	}

	public void loadRegister() {
		BorderPane parent = new BorderPane();
		MenuBar menuBar = new MenuBar();
		Menu menu = new Menu("Dashboard");
		MenuItem loginMenu = new MenuItem("Login");
		MenuItem registerMenu = new MenuItem("Register");
		menu.getItems().addAll(loginMenu, registerMenu);
		menuBar.getMenus().add(menu);
		HBox hbox1 = new HBox(menuBar);
		VBox vbox1 = new VBox();
		parent.setTop(hbox1);
		hbox1.setAlignment(Pos.TOP_LEFT);
		vbox1.setAlignment(Pos.TOP_CENTER);
		hbox1.setHgrow(menuBar, Priority.ALWAYS);
		hbox1.setMaxWidth(1000);
		Factory factory = new Factory();
		Text login = factory.createText("Register");
		login.setStyle("-fx-font: 60 arial;");
		login.setFont(Font.font("Arial", FontWeight.BOLD, 50));
		Text name = factory.createText("NJuice");
		name.setStyle("-fx-font: 25 arial;");
		vbox1.getChildren().addAll(login, name);
		vbox1.setSpacing(15);

		VBox insertBox = new VBox();
		HBox usernameBox = new HBox();
		HBox usernameTextBox = new HBox();
		Label usernameLabel = factory.createLabel("Username");
		usernameTextBox.getChildren().add(usernameLabel);
		usernameTextBox.setMargin(usernameLabel, new Insets(0, 0, 0, 250));
		TextField username = new TextField();
		username.setPromptText("Enter Username");
		usernameBox.setHgrow(username, Priority.ALWAYS);
		username.setMaxWidth(300);
		usernameBox.getChildren().add(username);
		usernameBox.setMargin(username, new Insets(0, 0, 25, 250));
		HBox passwordBox = new HBox();
		PasswordField password = new PasswordField();
		password.setPromptText("Enter Password");
		password.setMaxWidth(300);
		passwordBox.getChildren().add(password);
		passwordBox.setHgrow(password, Priority.ALWAYS);
		passwordBox.setMargin(password, new Insets(0, 0, 10, 250));
		HBox passwordTextBox = new HBox();
		Label passwordLabel = factory.createLabel("Password");
		passwordTextBox.getChildren().add(passwordLabel);
		passwordTextBox.setMargin(passwordLabel, new Insets(0, 0, 0, 250));
		CheckBox agree = new CheckBox();
		Text terms = factory.createText("I agree to the terms and conditions of NJuice!");
		HBox termsBox = new HBox();
		termsBox.getChildren().addAll(agree, terms);
		termsBox.setHgrow(terms, Priority.ALWAYS);
		termsBox.setMargin(agree, new Insets(0, 10, 0, 0));
		Label alert = new Label("Credentials Failed!");
		alert.setVisible(false);
		alert.setTextFill(Color.RED);
		Button registerButton = new Button("Register");
		insertBox.getChildren().addAll(usernameTextBox, usernameBox, passwordTextBox, passwordBox, termsBox, alert);
		insertBox.setMargin(alert, new Insets(0, 0, 30, 250));
		insertBox.setMargin(termsBox, new Insets(0, 0, 0, 250));
		parent.setCenter(vbox1);
		vbox1.getChildren().addAll(insertBox, registerButton);
		vbox1.setMargin(login, new Insets(100, 0, 0, 0));

		Scene scene = new Scene(parent, 800, 600);
		primaryStage.setScene(scene);
		primaryStage.show();
		loginMenu.setOnAction(e -> {
			loadLogin();
		});

		registerButton.setOnAction(e -> {
			if (username.getText().isEmpty() || password.getText().isEmpty() || !agree.isSelected()) {
				alert.setText("Please Input all the field");
				alert.setVisible(true);
			} else {
				String usernameInput = username.getText();
				String passwordInput = password.getText();
				int checkUsername = 0;
				for (int i = 0; i < users.size(); i++) {
					if (users.get(i).getUsername().equals(usernameInput)) {
						checkUsername++;
						break;
					}
				}
				if (checkUsername != 0) {
					alert.setText("Username is already taken");
					alert.setVisible(true);
				} else {
					users.add(new User(usernameInput, passwordInput, "Customer"));
					addUser(usernameInput, passwordInput);
					CustomerPage customer = new CustomerPage(primaryStage, users.get(users.size() - 1));
					customer.ShowCustomerPage();
				}
			}
		});

	}
}
