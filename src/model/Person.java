package model;

import java.io.Serializable;

public abstract class Person implements Serializable {
	private Name name;
	private String id;
	
	private static int idCount = 0;

	public Person(Name name) {
		this.name = name;
		id = String.valueOf(idCount++);
	}
	
	public Person(String firstName, String lastName) {
		this.name = new Name(firstName, lastName);
		id = String.valueOf(idCount++);
	}

	public Name getName() {
		return name;
	}

	public void setName(Name name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}
	
	public static void setIdCount(int idCount) {
		Person.idCount = idCount;
	}

	@Override
	public String toString() {
		return "Person [name=" + name + ", id=" + id + "]";
	}


}
