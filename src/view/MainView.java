package view;

import java.io.File;
import java.util.Optional;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import model.PersonBag;
import model.TextbookBag;
import util.Backup;
import util.Restore;
import util.Utilities;

public class MainView {
	private PersonBag personBag;
	private TextbookBag textbookBag;
	private StudentView studentView;
	private InstructorView instructorView;
	private TextbookView textbookView;
	private BorderPane root;
	
	private File selectedPersonsFile;
	private File selectedTextbooksFile;
	
	public MainView() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		fileChooser.setInitialDirectory(new File("BackupFolder/"));
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter(".dat", "*.dat"));
		
		studentView = new StudentView(personBag, selectedPersonsFile);
		instructorView = new InstructorView(personBag, selectedPersonsFile);
		textbookView = new TextbookView(textbookBag, selectedTextbooksFile);
		root = new BorderPane();		
		MenuBar menuBar = new MenuBar();	
		VBox startView = makeStartingView();	
		root.setTop(menuBar);
		root.setCenter(startView);
			
		Menu fileMenu = new Menu("File");
		Menu importMenu = new Menu("Import");
		Menu importPersonsMenu = new Menu("Persons");
		Menu importTextbooksMenu = new Menu("Textbooks");
		Menu backupMenu = new Menu("Backup");
		Menu viewMenu = new Menu("View");
		Menu clearMenu = new Menu("Clear");	
		MenuItem importExistingPersonsItem = new MenuItem("Import Exisiting PersonBag");
		MenuItem importNewPersonsItem = new MenuItem("Import New PersonBag");
		MenuItem importExisitingTextbooksItem = new MenuItem("Import Exisiting TextbookBag");
		MenuItem importNewTextbooksItem = new MenuItem("Import New TextbookBag");
		MenuItem backupPersonsItem = new MenuItem("Backup Persons");
		MenuItem backupTextbooksItem = new MenuItem("Backup Textbooks");
		MenuItem exitItem = new MenuItem("Exit");		
		MenuItem studentViewItem = new MenuItem("Student View");
		MenuItem instructorViewItem = new MenuItem("Instructor View");
		MenuItem textbookViewItem = new MenuItem("Textbook View");	
		MenuItem mainMenuViewItem = new MenuItem("Return to Menu");
		MenuItem clearTextFieldsItem = new MenuItem("Clear Text Fields");
		MenuItem clearAllFieldsItem = new MenuItem("Clear All Fields");
		SeparatorMenuItem separator = new SeparatorMenuItem();
		SeparatorMenuItem separator2 = new SeparatorMenuItem();
		
		fileMenu.getItems().addAll(importMenu, backupMenu, separator, exitItem);
		importMenu.getItems().addAll(importPersonsMenu, importTextbooksMenu);
		importPersonsMenu.getItems().addAll(importExistingPersonsItem, importNewPersonsItem);
		importTextbooksMenu.getItems().addAll(importExisitingTextbooksItem, importNewTextbooksItem);
		backupMenu.getItems().addAll(backupPersonsItem, backupTextbooksItem);
		viewMenu.getItems().addAll(studentViewItem, instructorViewItem, textbookViewItem, separator2, mainMenuViewItem);
		clearMenu.getItems().addAll(clearTextFieldsItem, clearAllFieldsItem);
		menuBar.getMenus().addAll(fileMenu, viewMenu, clearMenu);
		
		importExistingPersonsItem.setOnAction(e -> {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Importing Exisiting File");
			alert.setHeaderText(null);
			alert.setContentText("To import an exisiting PersonBag, please select an existing .dat file that contains students/instructors.");
			alert.showAndWait();
			selectedPersonsFile = fileChooser.showOpenDialog(null);
			if(selectedPersonsFile != null) {
				personBag = Restore.restorePersonBag(selectedPersonsFile);
				studentView.setBag(personBag);
				studentView.setSelectedFile(selectedPersonsFile);
				instructorView.setPersonBag(personBag);
				instructorView.setSelectedFile(selectedPersonsFile);
				Backup.backupPersonBag(personBag, selectedPersonsFile);
				
				alert.setTitle("Import Sucessful");
				alert.setContentText("The selected PersonBag file was sucessfully imported!");
				alert.showAndWait();	
			}
		});
	
		importNewPersonsItem.setOnAction(e -> {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Creating New File");
			alert.setHeaderText(null);
			alert.setContentText("To import a new PersonBag, please choose a location and create a new .dat file. "
					+ "This file will then be loaded with 1000 random students and 500 random instructors.");
			alert.showAndWait();
			selectedPersonsFile = fileChooser.showSaveDialog(null);	
			if(selectedPersonsFile != null) {
				personBag = new PersonBag(2000);
				Utilities.importStudents(personBag);
				Utilities.importInstructors(personBag);
				Backup.backupPersonBag(personBag, selectedPersonsFile);
				studentView.setBag(personBag);
				studentView.setSelectedFile(selectedPersonsFile);
				instructorView.setPersonBag(personBag);
				instructorView.setSelectedFile(selectedPersonsFile);
				
				alert.setTitle("New PersonBag Created");
				alert.setContentText("A new PersonBag file was successfuly created with 1000 random students and 500 random instructors at " + selectedPersonsFile );
				alert.setWidth(500);
				alert.setHeight(175);
				alert.showAndWait();
			}
		});
	
		importExisitingTextbooksItem.setOnAction(e -> {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Importing Exisiting File");
			alert.setHeaderText(null);
			alert.setContentText("To import an exisiting TextbookBag, please select an existing .dat file that contains textbooks.");
			alert.showAndWait();
			selectedTextbooksFile = fileChooser.showOpenDialog(null);
			if(selectedTextbooksFile != null) {
				textbookBag = Restore.restoreTextbookBag(selectedTextbooksFile);
				textbookView.setTextbookBag(textbookBag);
				textbookView.setSelectedFile(selectedTextbooksFile);
				Backup.backupTextbookBag(textbookBag, selectedTextbooksFile);
				
				alert.setTitle("Import Sucessful");
				alert.setContentText("The selected TextbookBag file was sucessfully imported!");
				alert.showAndWait();
			}
		});
	
		importNewTextbooksItem.setOnAction(e -> {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Creating New File");
			alert.setHeaderText(null);
			alert.setContentText("To import a new TextbookBag, please choose a location and create a new .dat file.");
			alert.showAndWait();
			selectedTextbooksFile = fileChooser.showSaveDialog(null);
			if(selectedTextbooksFile != null) {
				textbookBag = new TextbookBag(40000);
				Utilities.importTextbooks(textbookBag);
				Backup.backupTextbookBag(textbookBag, selectedTextbooksFile);
				textbookView.setTextbookBag(textbookBag);
				textbookView.setSelectedFile(selectedTextbooksFile);
				
				alert.setTitle("New TextbookBag Created");
				alert.setWidth(500);
				alert.setHeight(175);
				alert.setContentText("A new TextbookBag file was successfuly created at " + selectedTextbooksFile);
				alert.showAndWait();
			}
		});
	
		backupPersonsItem.setOnAction(e -> {
			//This if-else statement is done to prevent a NullPointerException when trying to backup.
			if(selectedPersonsFile != null) {
				Backup.backupPersonBag(personBag, selectedPersonsFile);	
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Backup Successful");
				alert.setHeaderText(null);
				alert.setContentText("PersonBag has been backed up to " + selectedPersonsFile);
				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText(null);
				alert.setContentText("Cannot backup students and instructors since no PersonBag file was ever imported.");
				alert.showAndWait();
			}
		});	
	
		backupTextbooksItem.setOnAction(e -> {
			//This if-else statement is done to prevent a NullPointerException when trying to backup.
			if(selectedTextbooksFile != null) {
				Backup.backupTextbookBag(textbookBag, selectedTextbooksFile);
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Backup Successful");
				alert.setHeaderText(null);
				alert.setContentText("Textbooks have been backed up to " + selectedTextbooksFile);
				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText(null);
				alert.setContentText("Cannot backup textbooks since no TextbookBag file was ever imported.");
				alert.showAndWait();
			}
		});
	
		exitItem.setOnAction(e -> {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setHeaderText(null);
			alert.setContentText("Are you sure you want to exit? The imported files will be backed up on exit.");
			Optional<ButtonType> action = alert.showAndWait();
			if(action.get() == ButtonType.OK) {
				//This if-else statement is done to prevent a NullPointerException when trying to backup.
				if(selectedPersonsFile != null && selectedTextbooksFile!= null) {
					Backup.backupPersonBag(personBag, selectedPersonsFile);
					Backup.backupTextbookBag(textbookBag, selectedTextbooksFile);
					Platform.exit();
				} else if(selectedPersonsFile != null) {
					Backup.backupPersonBag(personBag, selectedPersonsFile);
					Platform.exit();
				} else if(selectedTextbooksFile != null) {
					Backup.backupTextbookBag(textbookBag, selectedTextbooksFile);
					Platform.exit();
				} else {
					Platform.exit();
				}
			}
		});
	
		studentViewItem.setOnAction(e -> {
			root.setCenter(studentView.getStudentPane());
			instructorView.clearAllFields();
			textbookView.clearAllFields();
		});
	
		instructorViewItem.setOnAction(e -> {
			root.setCenter(instructorView.getInstructorPane());
			studentView.clearAllFields();
			textbookView.clearAllFields();
		});
	
		textbookViewItem.setOnAction(e -> {
			root.setCenter(textbookView.getTextbookPane());
			studentView.clearAllFields();
			instructorView.clearAllFields();
		});	
	
		mainMenuViewItem.setOnAction(e -> {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setHeaderText(null);
			alert.setContentText("Are you sure you want to return to main menu?");
			Optional<ButtonType> action = alert.showAndWait();
			if(action.get() == ButtonType.OK) {
				root.setCenter(startView);
				studentView.clearAllFields();
				instructorView.clearAllFields();
				textbookView.clearAllFields();
			}
		});

		clearTextFieldsItem.setOnAction(e -> {
			if(root.getCenter().equals(studentView.getStudentPane())) {
				studentView.clearTextFields();	
			}else if(root.getCenter().equals(instructorView.getInstructorPane())){
				instructorView.clearTextFields();	
			}else {
				textbookView.clearTextFields();
			}
		});
	
		clearAllFieldsItem.setOnAction(e -> {
			if(root.getCenter().equals(studentView.getStudentPane())) {
				studentView.clearAllFields();	
			}else if(root.getCenter().equals(instructorView.getInstructorPane())){
				instructorView.clearAllFields();	
			}else {
				textbookView.clearAllFields();
			}
		});
	}

	public BorderPane getRoot() {
		return root;
	}
	
	public VBox makeStartingView() {
		Text title = new Text("Main Menu");
		title.setFill(Paint.valueOf("#ffffff"));
		title.setFont(Font.font("Baskerville Old Face",FontWeight.BOLD, 100));
	
		Button studentViewBtn = new Button("Student View");
		studentViewBtn.setPrefSize(140, 50);
		studentViewBtn.setFont(new Font("Bookman Old Style", 14));
		studentViewBtn.setOnAction(e -> root.setCenter(studentView.getStudentPane()));
	
		Button instructorViewBtn = new Button("Instructor View");
		instructorViewBtn.setPrefSize(140, 50);
		instructorViewBtn.setFont(new Font("Bookman Old Style", 14));
		instructorViewBtn.setOnAction(e -> root.setCenter(instructorView.getInstructorPane()));
	
		Button textbookViewBtn = new Button("Textbook View");
		textbookViewBtn.setPrefSize(140, 50);
		textbookViewBtn.setFont(new Font("Bookman Old Style", 14));
		textbookViewBtn.setOnAction(e -> root.setCenter(textbookView.getTextbookPane()));
	
		HBox buttonBox = new HBox(40);
		buttonBox.getChildren().addAll(studentViewBtn, instructorViewBtn, textbookViewBtn);
		buttonBox.setAlignment(Pos.CENTER);
	
		VBox startView = new VBox(50);
		startView.getChildren().addAll(title, buttonBox);
		startView.setAlignment(Pos.CENTER);
		startView.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #9e374a, #232526 )");
	
		return startView;
	}	
}