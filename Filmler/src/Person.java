import java.util.ArrayList;
import java.util.List;

public class Person {
	private String name;

	private List<String> localURLs = new ArrayList<>();

	private List<String> images = new ArrayList<>();

	public Person(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getLocalURLs() {
		return localURLs;
	}

	public void setLocalURLs(List<String> localURLs) {
		this.localURLs = localURLs;
	}

	public boolean addLocalURL(String localURL) {
		return this.localURLs.add(localURL);
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}
}
