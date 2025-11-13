package fishing.control;

//WelcomeController.jav

import fishing.model.Player;
import util.DataManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.annotation.processing.Generated;
import java.io.InputStream;


/**
* Controller for the Welcome (Login/Register) screen
*/
public class Welcome {

    @FXML
 private ImageView gifBackground;
    @FXML
 private TabPane tabPane;
 
 // Login fields
    @FXML
 private TextField loginUsernameField;
    @FXML
 private PasswordField loginPasswordField;
    @FXML
  private Label loginErrorLabel;
 
 // Register fields
    @FXML
 private TextField registerUsernameField;
    @FXML
  private PasswordField registerPasswordField;
    @FXML
 private PasswordField confirmPasswordField;
    @FXML
  private Label registerErrorLabel;
 
@FXML
 public void initialize() {
     // Load GIF from resources
     try {
         InputStream stream = getClass().getResourceAsStream("/assets/background/fishing_dock_login.gif");
         if (stream == null) {
             System.err.println("GIF background not found!");
         } else {
             Image gifImage = new Image(stream);
             gifBackground.setImage(gifImage);
         }
     } catch (Exception e) {
         System.err.println("Error loading GIF: " + e.getMessage());
     }

     // Clear error labels initially
     loginErrorLabel.setText("");
     registerErrorLabel.setText("");
     
     // Add Enter key listener for login
     loginPasswordField.setOnAction(event -> handleLogin());
     
     // Add Enter key listener for register
     confirmPasswordField.setOnAction(event -> handleRegister());
 }
 
 /**
  * Handle login button click
  */
 @FXML
 private void handleLogin() {
     String username = loginUsernameField.getText().trim();
     String password = loginPasswordField.getText();
     
     // Clear previous error
     loginErrorLabel.setText("");
     
     // Validation
     if (username.isEmpty() || password.isEmpty()) {
         showLoginError("Please fill in all fields.");
         return;
     }
     
     // Authenticate player
     Player player = DataManager.authenticatePlayer(username, password);
     
     if (player == null) {
         showLoginError("Invalid username or password.");
         return;
     }
     
     // Login successful
     System.out.println("Login successful: " + username);
     SceneManager.setPlayer(player);
     SceneManager.switchScene("dashboard");
 }
 
 /**
  * Handle register button click
  */
 @FXML
 private void handleRegister() {
     String username = registerUsernameField.getText().trim();
     String password = registerPasswordField.getText();
     String confirmPassword = confirmPasswordField.getText();
     
     // Clear previous error
     registerErrorLabel.setText("");
     
     // Validation
     if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
         showRegisterError("Please fill in all fields.");
         return;
     }
     
     if (username.length() < 3) {
         showRegisterError("Username must be at least 3 characters.");
         return;
     }
     
     if (password.length() < 4) {
         showRegisterError("Password must be at least 4 characters.");
         return;
     }
     
     if (!password.equals(confirmPassword)) {
         showRegisterError("Passwords do not match.");
         return;
     }
     
     // Check if username already exists
     if (DataManager.playerExists(username)) {
         showRegisterError("Username already taken.");
         return;
     }
     
     // Register new player
     boolean success = DataManager.registerPlayer(username, password);
     
     if (!success) {
         showRegisterError("Registration failed. Please try again.");
         return;
     }
     
     // Registration successful - auto login
     System.out.println("Registration successful: " + username);
     Player newPlayer = DataManager.loadPlayer(username);
     SceneManager.setPlayer(newPlayer);
     SceneManager.switchScene("dashboard");
 }
 
 /**
  * Display login error message
  */
 private void showLoginError(String message) {
     loginErrorLabel.setText(message);
 }
 
 /**
  * Display register error message
  */
 private void showRegisterError(String message) {
     registerErrorLabel.setText(message);
 }
}