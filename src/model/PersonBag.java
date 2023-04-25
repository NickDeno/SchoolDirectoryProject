package model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.function.Predicate;

public class PersonBag implements Serializable {
	private Person[] arr;
	private int nElems;

	public PersonBag(int maxSize) {
		arr = new Person[maxSize];
	}
	
	public void insert(Person person) {
		arr[nElems++] = person;
	}
	
	public void display() {
		for(int i = 0; i < nElems; i++) {
			System.out.println(arr[i]);
		}
		System.out.println();
	}

	public Person[] search(Predicate<Person> predicate) {
		Person[] temp = new Person[nElems];
		int count = 0;
		for (int i = 0; i < nElems; i++) {
			if (predicate.test(arr[i])) {
				temp[count++] = arr[i];
			}
		}
		return Arrays.copyOf(temp, count);
	}

	public Person[] delete(Predicate<Person> predicate) {
		Person[] temp = new Person[nElems];
		int count = 0;
		for(int i = 0; i <nElems; i++) {
			if(predicate.test(arr[i])) {
				temp[count++] = arr[i];
				
				for(int j = i; j < nElems-1; j++) {
					arr[j] = arr[j+1];
				}
				nElems--;
				i--;
			}
		}
		return Arrays.copyOf(temp, count);
	}

	public int getnElems() {
		return nElems;
	}
}
