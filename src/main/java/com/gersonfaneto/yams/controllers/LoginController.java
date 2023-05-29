package com.gersonfaneto.yams.controllers;

import com.gersonfaneto.yams.dao.DAO;
import com.gersonfaneto.yams.models.entities.admnistrator.Administrator;
import com.gersonfaneto.yams.models.entities.user.User;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class LoginController {

  @FXML private ImageView closeButton;

  @FXML private TextField emailField;

  @FXML private Button loginButton;

  @FXML private PasswordField passwordField;

  @FXML private TextField passwordText;

  @FXML private CheckBox showPassword;

  @FXML private Label visualFeedback;

  @FXML
  public void initialize() {
    User sysAdm = Administrator.retrieveInstance("admin@gmail.com", "admin", "John Smith");
    DAO.fromUsers().createOne(sysAdm);

    revealSecrets(null);
  }

  @FXML
  public void closeWindow(MouseEvent mouseClick) {
    System.exit(0);
  }

  @FXML
  public void revealSecrets(MouseEvent mouseClick) {
    if (showPassword.isSelected()) {
      passwordText.setText(passwordField.getText());
      passwordText.setVisible(true);
      passwordField.setVisible(false);
      return;
    }
    passwordField.setText(passwordText.getText());
    passwordField.setVisible(true);
    passwordText.setVisible(false);
  }

  @FXML
  public void validateEntries(MouseEvent mouseClick) {
    String passwordText = passwordValue();
    String emailText = emailField.getText();
    User foundUser = DAO.fromUsers().findByEmail(emailText);

    if (passwordText.length() == 0 || emailText.length() == 0) {
      visualFeedback.setText("Insira o seus dados!");
      visualFeedback.setTextFill(Color.RED);
      return;
    } else if (foundUser == null) {
      visualFeedback.setText("Usuário não encontrado!");
      visualFeedback.setTextFill(Color.RED);
      return;
    } else if (!foundUser.getUserPassword().equals(passwordText)) {
      visualFeedback.setText("Senha incorreta!");
      visualFeedback.setTextFill(Color.RED);
      return;
    }

    visualFeedback.setText("");
    System.out.println("Welcome back!");
  }

  private String passwordValue() {
    return showPassword.isSelected() ? passwordText.getText() : passwordField.getText();
  }
}
