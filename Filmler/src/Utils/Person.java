package Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Person {
	private String name;
	private List<Face> faces = new ArrayList<>();
	private Map<Person, Integer> connections = new HashMap<>();

	public Person(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Face> getFaces() {
		return faces;
	}

	public void setFaces(List<Face> faces) {
		this.faces = faces;
	}

	public void addFace(Face face) {
		faces.add(face);
	}

	public Map<Person, Integer> getConnections() {
		return connections;
	}

	public void addConnection(Person person) {
		if (connections.containsKey(person)) {
			connections.replace(person, connections.get(person) + 1); // Increment number of connections with this person
		} else {
			connections.put(person, 1);
		}
	}

	@Override
	public String toString() {
		return name + faces;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Person person = (Person) o;

		return name.equals(person.name);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}
}
