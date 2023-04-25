package view;

import java.io.File;
import java.util.Optional;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import model.Name;
import model.PersonBag;
import model.Textbook;
import model.TextbookBag;
import util.Backup;

public class TextbookView {
	private TextbookBag textbookBag;
	private File selectedFile;
	
	private VBox textbookPane;
	private TextField titleField;
	private TextField isbnField;
	private TextField authorFirstNameField;
	private TextField authorLastNameField;
	private TextField priceField;
	private TextField outputField;
	
	private ChoiceBox<String> choiceBox;
	private ListView<Textbook> listView;
	
	public TextbookView(TextbookBag textbookBag, File selectedFile) {
		this.textbookBag = textbookBag;
		this.selectedFile = selectedFile;
		Text title = new Text("Textbook View");
		title.setFill(Paint.valueOf("#e1e8eb"));
		title.setFont(Font.font("Baskerville Old Face",FontWeight.BOLD, 60));
		
		titleField = new TextField();
		titleField.setPrefSize(120, 30);
		titleField.setPromptText("TITLE");

		authorFirstNameField = new TextField();
		authorFirstNameField.setPrefSize(120, 30);
		authorFirstNameField.setPromptText("AUTHOR FIRST NAME");
		
		authorLastNameField = new TextField();
		authorLastNameField.setPrefSize(120, 30);
		authorLastNameField.setPromptText("AUTHOR LAST NAME");
		
		priceField = new TextField();
		priceField.setPrefSize(120, 30);
		priceField.setPromptText("PRICE");
		
		isbnField = new TextField();
		isbnField.setPrefSize(120, 30);
		isbnField.setPromptText("ISBN");
		isbnField.setEditable(false);
		
		Button searchBtn = new Button("SEARCH");
		searchBtn.setPrefSize(80, 30);
		
		Button removeBtn = new Button("REMOVE");
		removeBtn.setPrefSize(80, 30);
		
		Button insertBtn = new Button("INSERT");
		insertBtn.setPrefSize(80, 30);
		
		Button updateBtn = new Button("UPDATE");
		updateBtn.setPrefSize(80, 30);
		
		outputField = new TextField();
		outputField.setPromptText("OUTPUT");
		outputField.setMaxSize(300, 50);
		
		listView = new ListView<>();
		listView.setMaxSize(700, 200);
		
		
		HBox inputBox = new HBox(20);
		inputBox.setAlignment(Pos.CENTER);
		inputBox.getChildren().addAll(titleField, authorFirstNameField, authorLastNameField, priceField, isbnField);
		
		HBox btnBox = new HBox(20);
		btnBox.setAlignment(Pos.CENTER);
		btnBox.getChildren().addAll( searchBtn, removeBtn, insertBtn, updateBtn);
		
		choiceBox = new ChoiceBox<>();
		choiceBox.getItems().addAll("TITLE", "ISBN", "AUTHOR FIRST NAME", "AUTHOR LAST NAME", "PRICE");
		choiceBox.setValue("Search Parameter:");
		choiceBox.setOnAction(e -> {	
			if(choiceBox.getValue().equals("ID")) {
				isbnField.setEditable(true);
			} else {
				isbnField.setEditable(false);
			}
		});
		
		VBox outputBox = new VBox(30);
		outputBox.setAlignment(Pos.CENTER);
		outputBox.getChildren().addAll(outputField, listView);
		
		searchBtn.setOnAction(e -> {
			outputField.clear();
			String userChoice = choiceBox.getValue();
			Textbook[] predicateSearch = this.textbookBag.search(t -> {
					if(userChoice.equals("TITLE")) {
						return t.getTitle().equals(titleField.getText());
					}
					
					if(userChoice.equals("ISBN")) {
						return t.getIsbn().equals(isbnField.getText());
					}
					
					if(userChoice.equals("AUTHOR FIRST NAME")) {
						return t.getAuthor().getFirstName().equals(authorFirstNameField.getText());
					}
					
					if(userChoice.equals("AUTHOR LAST NAME")) {
						return t.getAuthor().getLastName().equals(authorLastNameField.getText());
					}
					
					if(userChoice.equals("PRICE")){
						return t.getPrice() == Double.parseDouble(priceField.getText());
					}
					return false;
			});
			
			if(userChoice.equals("ISBN") && predicateSearch.length > 0) { // If users searches by Isbn and gets a match,
				outputField.appendText("Textbook found with id " + isbnField.getText() + "!");
				setTextFields(predicateSearch[0]);
				
			} else if(predicateSearch.length > 0) { // If user searches by anything else and gets a match,
				outputField.appendText("Search results:");
				ObservableList<Textbook> results = FXCollections.observableArrayList(predicateSearch);
				listView.getItems().addAll(results);
				listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Textbook>() {

					@Override
					public void changed(ObservableValue<? extends Textbook> observable, Textbook oldTextbook, Textbook newTextbook) {
						if(newTextbook != null) {
							setTextFields(newTextbook);
						}
					}	
				});
			
			} else { // If user searches and gets no matches, 
				outputField.appendText("No textbook matches found.");
				clearTextFields();
			}
			choiceBox.setValue("Search Parameter:");
		});
		
		removeBtn.setOnAction(e -> {
			if(checkTextFieldsAreValid() == true) {
				outputField.clear();
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setHeaderText(null);
				alert.setContentText("Are you sure you want to remove textbook with ISBN " + isbnField.getText() + "?");
				Optional<ButtonType> action = alert.showAndWait();
			
				if(action.get() == ButtonType.OK) {
					outputField.appendText("Removed textbook with isbn " + isbnField.getText() + "!");
					Textbook[] predicateDelete = this.textbookBag.delete(t -> t.getIsbn().equals(isbnField.getText()));		
					listView.getItems().remove(predicateDelete[0]);
					listView.getSelectionModel().clearSelection();
					clearTextFields();
					Backup.backupTextbookBag(this.textbookBag, this.selectedFile);
				}
			}
		});
		
		insertBtn.setOnAction(e -> {
			outputField.clear();
			listView.getItems().clear();
			if(checkTextFieldsAreValid() == true && isbnField.getText().isEmpty()) {
				Textbook t = new Textbook(titleField.getText(), isbnField.getText(), new Name(authorFirstNameField.getText(), authorLastNameField.getText()), Double.parseDouble(priceField.getText()));
				textbookBag.insert(t);
				outputField.appendText("Inserted Textbook");
				clearTextFields();
				Backup.backupTextbookBag(this.textbookBag, this.selectedFile);	
			} 
		});
		
		updateBtn.setOnAction(e -> {
			outputField.clear();
			Textbook[] textbookToUpdate = this.textbookBag.search(t -> t.getIsbn().equals(isbnField.getText()));
			if(checkTextFieldsAreValid() == true) {
				textbookToUpdate[0].setTitle(titleField.getText());
				textbookToUpdate[0].setIsbn(isbnField.getText());
				textbookToUpdate[0].setAuthor(new Name(authorFirstNameField.getText(), authorLastNameField.getText()));
				textbookToUpdate[0].setPrice(Double.parseDouble(priceField.getText()));
				listView.getSelectionModel().clearSelection();
				outputField.appendText("Textbook with isbn " + isbnField.getText() + " was updated!");
				clearTextFields();
				Backup.backupTextbookBag(this.textbookBag, this.selectedFile);
			}
		});
			
		textbookPane = new VBox(35);
		textbookPane.setAlignment(Pos.CENTER);
		textbookPane.setPadding(new Insets(30));
		textbookPane.getChildren().addAll(title, inputBox, btnBox, choiceBox, outputBox);
		textbookPane.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #5b5bba, #232526 )");
		
	}
	
	public VBox getTextbookPane() {
		return textbookPane;
	}
	
	public void setTextbookBag(TextbookBag textbookBag) {
		this.textbookBag = textbookBag;
	}
	
	public void setSelectedFile(File selectedFile) {
		this.selectedFile = selectedFile;
	}
	
	private boolean checkTextFieldsAreValid() {
		if(titleField.getText().isEmpty() || authorFirstNameField.getText().isEmpty() || authorLastNameField.getText().isEmpty() || priceField.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Blank Text Fields");
			alert.setContentText("One or more text fields may have been left blank. Please recheck text fields and try again.");
			alert.showAndWait();
			return false;	
		} 
		return true;	
	}	
	
	public void setTextFields(Textbook t) {
		titleField.setText(t.getTitle());
		authorFirstNameField.setText(t.getAuthor().getFirstName());
		authorLastNameField.setText(t.getAuthor().getLastName());
		priceField.setText(String.valueOf(t.getPrice()));
		isbnField.setText(t.getIsbn());
		
	}
	
	public void clearTextFields() {
		titleField.clear();
		authorFirstNameField.clear();
		authorLastNameField.clear();
		priceField.clear();
		isbnField.clear();
	}
	
	public void clearAllFields() {
		clearTextFields();
		outputField.clear();
		choiceBox.setValue("Search Parameter");
		listView.getItems().clear();		
	}	
}
