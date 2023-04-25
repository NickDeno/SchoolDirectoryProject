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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import model.Name;
import model.Person;
import model.PersonBag;
import model.Student;
import util.Backup;

public class StudentView {
	private PersonBag personBag;
	private File selectedFile;
	
	private VBox studentPane;
	private TextField firstNameField;
	private TextField lastNameField;
	private TextField gpaField;
	private TextField majorField;
	private TextField idField;
	private TextField outputField;
	
	private ChoiceBox<String> choiceBox;
	private ListView<Person> listView;
	
	private static final String[] validMajors = {"ACC", "BUS", "MKT", "RET", "AUT", "CYB", "COT", "DRF", "ELT", "ENS", "FPT", "TYT", "CHI", "CIN", "COM", "DNC", "DMA", 
			"ENG", "FRE", "GER", "GRD", "HUM", "INT", "ITL", "JPN", "LAT", "MUS", "MTR", "PHL", "ART", "SPN", "THR", "WST", "HIS", "SOC", "ASL", "CDC", "DTE", "PAR", "PFS",
			"HSC", "MED", "HIT", "HUS", "NUR", "OTA", "PED", "PTA", "PNU", "AST", "BIO", "CHE", "ESC", "ENV", "MAR", "MAT", "MET", "PHY", "ANT", "ECO", "GEO", "POL", "PSY",
			"COL", "CSE", "CRJ", "CUL", "EDU", "ESL", "HVA", "HRM", "CST", "IND", "LAW", "LIB", "MFT", "POA", "RTV", "RDG", "VST"};

	public StudentView(PersonBag personBag, File selectedFile) {
		this.personBag = personBag;
		this.selectedFile = selectedFile;
		Text title = new Text("Student View");
		title.setFill(Paint.valueOf("#ffffff"));
		title.setFont(Font.font("Baskerville Old Face",FontWeight.BOLD, 60));
		
		firstNameField = new TextField();
		firstNameField.setPrefSize(100, 30);
		firstNameField.setPromptText("FIRST NAME");
		
		lastNameField = new TextField();
		lastNameField.setPrefSize(100, 30);
		lastNameField.setPromptText("LAST NAME");
		
		gpaField = new TextField();
		gpaField.setPrefSize(100, 30);
		gpaField.setPromptText("GPA");
		
		majorField = new TextField();
		majorField.setPrefSize(100, 30);
		majorField.setPromptText("MAJOR");
		
		idField = new TextField();
		idField.setPrefSize(100, 30);
		idField.setPromptText("ID");
		idField.setEditable(false);

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
		inputBox.getChildren().addAll(firstNameField, lastNameField, gpaField, majorField, idField);
		
		HBox btnBox = new HBox(20);
		btnBox.setAlignment(Pos.CENTER);
		btnBox.getChildren().addAll( searchBtn, removeBtn, insertBtn, updateBtn);
		
		choiceBox = new ChoiceBox<>();
		choiceBox.getItems().addAll("ID", "FIRST NAME" , "LAST NAME", "GPA", "MAJOR");
		choiceBox.setValue("Search Parameter:");
		choiceBox.setOnAction(e -> {	
			if(choiceBox.getValue().equals("ID")) {
				idField.setEditable(true);
			} else {
				idField.setEditable(false);
			}
		});
		
		VBox outputBox = new VBox(30);
		outputBox.setAlignment(Pos.CENTER);
		outputBox.getChildren().addAll(outputField, listView);
		
		searchBtn.setOnAction(e -> {		
			outputField.clear();
			listView.getItems().clear();
			Person[] predicateSearch = this.personBag.search(s -> {
				if(s instanceof Student) {
					if(choiceBox.getValue().equals("ID")) {
						return s.getId().equals(idField.getText());
					}
					
					if(choiceBox.getValue().equals("FIRST NAME")) {
						return s.getName().getFirstName().equals(firstNameField.getText());
					}
					
					if(choiceBox.getValue().equals("LAST NAME")) {
						return s.getName().getLastName().equals(lastNameField.getText());
					}
					
					if(choiceBox.getValue().equals("MAJOR")) {
						return ((Student) s).getMajor().equalsIgnoreCase(majorField.getText());
					}
					
					if(choiceBox.getValue().equals("GPA")) {
						return ((Student) s).getGpa() == Double.parseDouble(gpaField.getText());
					}
				}
				return false;
			});
			
			if(choiceBox.getValue().equals("ID") && predicateSearch.length > 0) { // If user searches by ID and gets a match, 
				outputField.appendText("Student found with id " + idField.getText() + "!");
				setTextFields(predicateSearch[0]);	
				
			} else if(predicateSearch.length > 0) { // If users searches by anything else and gets a match,
				outputField.appendText("Search results:");
				ObservableList<Person> results = FXCollections.observableArrayList(predicateSearch);
				listView.getItems().addAll(results);
				listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Person>() {

					@Override
					public void changed(ObservableValue<? extends Person> observable, Person oldPerson, Person newPerson) {
						if(newPerson != null) {
							setTextFields(newPerson);
						}
					}	
				});
			} else { // If user searches and gets no matches,
				outputField.appendText("No student matches found.");
				clearTextFields();
			}
			choiceBox.setValue("Search Parameter:");	
		});
		
		removeBtn.setOnAction(e -> {
			outputField.clear();	
			if(checkTextFieldsAreValid() == true) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setHeaderText(null);
				alert.setContentText("Are you sure you want to remove student with id " + idField.getText() + "?");
				Optional<ButtonType> action = alert.showAndWait();
				
				if(action.get() == ButtonType.OK) {
					outputField.appendText("Removed student with id " + idField.getText() + "!");
					Person[] predicateDelete = this.personBag.delete(s -> s.getId().equals(idField.getText()));		
					listView.getItems().remove(predicateDelete[0]);
					listView.getSelectionModel().clearSelection();
					clearTextFields();
					Backup.backupPersonBag(this.personBag, this.selectedFile);
				}	
			}
		});
		
		insertBtn.setOnAction(e -> {
			outputField.clear();
			listView.getItems().clear();	
			if(checkTextFieldsAreValid() == true && idField.getText().isEmpty()) {
				Student s = new Student(new Name(firstNameField.getText(), lastNameField.getText()),  Double.parseDouble(gpaField.getText()), majorField.getText());
				this.personBag.insert(s);
				outputField.appendText("Inserted Student!");
				clearTextFields();
				Backup.backupPersonBag(this.personBag, this.selectedFile);	
			} 	
		});
		
		updateBtn.setOnAction(e -> {
			outputField.clear();
			Person[] studentToUpdate = this.personBag.search(s -> s.getId().equals(idField.getText()));	
			if(checkTextFieldsAreValid() == true) {
				studentToUpdate[0].setName(new Name(firstNameField.getText(), lastNameField.getText()));
				((Student)studentToUpdate[0]).setMajor(majorField.getText());
				((Student)studentToUpdate[0]).setGpa(Double.parseDouble(gpaField.getText()));
				listView.getSelectionModel().clearSelection();
				outputField.appendText("Student with id " + idField.getText() + " was updated!");
				clearTextFields();
				Backup.backupPersonBag(this.personBag, this.selectedFile);
			}
		});
		
		studentPane = new VBox(35);
		studentPane.setAlignment(Pos.CENTER);
		studentPane.setPadding(new Insets(30));
		studentPane.getChildren().addAll(title, inputBox, btnBox, choiceBox, outputBox);
		studentPane.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #1d67a3, #232526 )");
	}
	
	//StudentView Methods
	public VBox getStudentPane() {
		return studentPane;
	}
	
	public void setBag(PersonBag personBag) {
		this.personBag = personBag;
	}
	
	public void setSelectedFile(File selectedFile) {
		this.selectedFile = selectedFile;
	}

	private boolean checkTextFieldsAreValid() {
		if(firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty() || gpaField.getText().isEmpty() || majorField.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Blank Text Fields");
			alert.setContentText("One or more text fields may have been left blank. Please recheck text fields and try again.");
			alert.showAndWait();
			return false;
			
		} else if(Double.parseDouble(gpaField.getText()) > 4.0 || Double.parseDouble(gpaField.getText()) < 0.0 ) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Invalid Gpa");
			alert.setContentText("Please enter a gpa between 0.0 and 4.0 then try again.");
			alert.showAndWait();
			return false;
		} else {
			boolean foundMajor = false;
			for(int i = 0; i < validMajors.length; i++) {
				if(majorField.getText().equals(validMajors[i])) {
					foundMajor = true;
				}
			}
			if(foundMajor == false) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("Invalid Major");
				alert.setContentText("Please enter a valid major then try again.");
				alert.showAndWait();
				return false;
			}
		}
		return true;	
	}	
	
	public void setTextFields(Person p) {
		firstNameField.setText(p.getName().getFirstName());
		lastNameField.setText(p.getName().getLastName());
		gpaField.setText(String.valueOf(((Student)p).getGpa()));
		majorField.setText(((Student)p).getMajor());
		idField.setText(p.getId());
	}
	
	public void clearTextFields() {
		firstNameField.clear();
		lastNameField.clear();
		gpaField.clear();
		majorField.clear();
		idField.clear();
	}
	
	public void clearAllFields() {
		clearTextFields();
		outputField.clear();
		choiceBox.setValue("Search Parameter");
		listView.getItems().clear();
	}
}
