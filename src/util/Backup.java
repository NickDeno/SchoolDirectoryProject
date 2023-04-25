package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import model.PersonBag;
import model.TextbookBag;

public class Backup {
	public static void backupPersonBag(PersonBag personBag, File selectedPersonsFile) {
		try {
			FileOutputStream fos = new FileOutputStream(selectedPersonsFile);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(personBag);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void backupTextbookBag(TextbookBag textbookBag, File selectedTextbooksFile) {
		try {
			FileOutputStream fos = new FileOutputStream(selectedTextbooksFile);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(textbookBag);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
