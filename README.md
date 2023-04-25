
# School Directory Project

Simulates a school directory. This project was written in Java, and used JavaFX (without SceneBuilder) to build the GUI. The GUI allows the user to insert, search, remove, and update textbooks, students, and instructors. This was my final project submission for my CSE148-Object Oriented Programming Suffolk County Community College class in Spring 2022.


## VSCode: How to Run

* Download Project
* Launch VSCode
* Ensure "Extension Pack for Java" Extension is installed for VSCode
* Open Project in Workspace
* Run AppDemo file
* If the "JavaFX runtime components are missing, and are required to run this application" error appears in console when trying to run, navigate to the .vscode folder, then the launch.json file. Open it, and add the following command directly under the "projectName" line. "vmArgs": "--module-path "lib/javafx-sdk-19/lib" --add-modules javafx.controls,javafx.fxml". Finally, save the launch.json file and rerun AppDemo
* NOTE: Since all jar dependencies are contained within lib folder, and are already referenced in the classpath, there is no need to download and install JavaFX before running this program. To check this, after opening project in VSCode, under the "Referenced Libraries" folder, the jars should all appear automatically



## Project Requirements

* Implement the following classes and methods:
    * Name Class:
        * firstName field: String type
        * lastName field: String type
    * Person (abstract super class):
        * name field: Name type
        * id field: String type (unique)
    * Student (Person subclass):
        * gpa field: double type
        * major field: String type
    * Instructor (Person subclass):
        * rank field: String type(Instructor, Assistant Professor, Associate Professor, Professor)
        * salary field: double type
    * PersonBag (Stores multiple Student objects):
        * personArr field: Person[] type
        * nElems field: int type
        * public void insert(Person person) method: Allows insertion of Person objects into personArr
        * public Person[] search(Predicate predicate): Allows personArr to be searched based on predicate
        * public Person delete(Predicate predicate): Allows Person objects to be deleted from personArr based on predicate
    * Textbook class:
        * title field: String type
        * isbn field: String type
        * author field: Name type
        * price field: double type
    * TextbookBag class(Stores multiple Textbook objects):
        * bookArr field: Textbook[] type
        * nElems: int type
        * public void insert(Textbook textbook): Allows insertion of Textbook objects into bookArr
        * public Textbook[] search(Predicate predicate): allows bookArr to be searcheed based on predicate
        * public Textbook[] delete(Predicate predicate): Allows Textbook objects to be delted from bookArr based on predicate
    * Utilities class:
        * public static Name emitName(): Returns a Name object consisting of a random first name and a random last name from the two files Firstnames.txt and Lastnames.txt
        * public static String[] emitTitleAndISBN(): Read the two text files textbook_isbns.txt and textbook_titles.txt using the Scanner class. Since each title in the title file matches the isbn at the same location in the isbn file, the method should return one dimensional array that contains one pair of title and isbn.
        * public static double emitPrice(): Returns a random price (double type) between 0.0 and 200.0
        * public static String emitMajor(): Returns a random major based on Majors.txt file
        * public static void importBooks(): Reads all the information from the four files(textbook_isbns.txt, textbook_titles.txt, Firstnames.txt and Lastnames.txt) and use the aforementioned methods in the same class to import all the textbooks into the TextbookBag
        * public static void importStudents(): Returns an array of 1000 Student objects based on the 3 text files (Firstnames.txt, Lastnames.txt, and Majors.txt) and random GPA between 0.0 and 4.0
        * viii.	public static void importInstructors(): Return an array of 500 Instructor objects based on the 2 text files(Firstnames.txt and Lastnames.txt) and a random salary between 10,000.00 and 100,000.00
* Implement GUI using JavaFX to work with classes listed above
* When program starts the very first time, all the Textbook objects and Person objects, including both Student(1000) and Instructor(500) objects, should be loaded into the TextbookBag and PersonBag respectively, by using the corresponding methods in the Utilities class.
* Once these objects are imported in the bags, the bags must be saved onto the hard drive as Textbooks.dat and Persons.dat in a separate Data folder (backupFolder) under project folder. From this point on, no large scale imports will be used again
* Data persistence should be implemented. This means your project should be able to backup the two bags whenever there are changes to them and restore the two bags whenever the program restarts