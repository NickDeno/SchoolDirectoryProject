package model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.function.Predicate;

public class TextbookBag implements Serializable {
	private Textbook[] bookArr;
	private int nElems;

	public TextbookBag(int maxSize) {
		bookArr = new Textbook[maxSize];
	}

	public void insert(Textbook textbook) {
		bookArr[nElems++] = textbook;
	}
	
	public void display() {
		for(int i = 0; i < nElems; i++) {
			System.out.println(bookArr[i]);
			System.out.println(i);
		}
		System.out.println();
	}

	public Textbook[] search(Predicate<Textbook> predicate) {
		Textbook[] temp = new Textbook[nElems];
		int count = 0;
		for (int i = 0; i < nElems; i++) {
			if (predicate.test(bookArr[i])) {
				temp[count++] = bookArr[i];
			}
		}
		return Arrays.copyOf(temp, count);
	}

	public Textbook[] delete(Predicate<Textbook> predicate) {
		Textbook[] temp = new Textbook[nElems];
		int count = 0;
		for(int i = 0; i <nElems; i++) {
			if(predicate.test(bookArr[i])) {
				temp[count++] = bookArr[i];
				
				for(int j = i; j < nElems-1; j++) {
					bookArr[j] = bookArr[j+1];
				}
				nElems--;
				i--;
			}
		}
		return Arrays.copyOf(temp, count);
	}

}
