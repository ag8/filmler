import Utils.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
	public final static String urlBase = System.getProperty("user.dir");

	public static List<Face> facePool; // List of all faces recognized by Face++. Each face has an ID and cropped URL.
	public static List<Person> people;

	public static void main(String[] args) {
		/* Set up LIST OF TRAINING URLS from training.txt */
		List<String> trainingURLs = loadURLs(new File(urlBase + "/files/training.txt"));

		/* CREATE FACEPOOL using FaceRec */
		facePool = new ArrayList<>();
		for (String url : trainingURLs) {
			System.out.println("Creating faces from " + url);
			facePool.addAll(FaceRec.createFaces(url));
		}
		System.out.println("FacePool =======================");
		facePool.forEach(System.out::println);

		/* CREATE LIST OF PEOPLE using TagFaces */
		people = TagFaces.run(facePool);
		System.out.println("People =========================");
		people.forEach(System.out::println);

		analyzeInteractions();
	}

	public static void analyzeInteractions() {
		String groupName = "sigmapeople";
		FaceRec.createGroup(groupName);
		for (Person p : people) {
			FaceRec.createPerson(p.getName(), groupName);
			for (Face f : p.getFaces()) {
				FaceRec.associateFace(p.getName(), f.ID);
			}
		}

		System.out.println(FaceRec.getGroupInfo(groupName));

		FaceRec.trainIdentify(groupName);
		try {
			Thread.sleep(1000); // Wait for the training. TODO: Check the HTTP status to see when it's done
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		List<String> eventURLs = loadURLs(new File(urlBase + "/files/event.txt")); // URLs of all photos from the event
		for (String url : eventURLs) {
			List<String> names = FaceRec.identifyFaces(groupName, url);
			List<Person> peopleInPhoto = new ArrayList<>(names.size());
			for (Person p : people) {
				for (String name : names) {
					if (p.getName().equals(name)) {
						peopleInPhoto.add(p);
					}
				}
			}
			for (Person p : peopleInPhoto) {
				//for (Person )
			}
		}

	}

	public static List<String> loadURLs(File urlsFile) {
		Scanner urlsIn = null;
		try {
			urlsIn = new Scanner(urlsFile);
		} catch (FileNotFoundException e) {
			System.out.println("URLs file not found. Put it in " + urlsFile.toPath());
			e.printStackTrace();
		}

		List<String> imageURLs = new ArrayList<>();
		while (urlsIn != null && urlsIn.hasNextLine()) {
			imageURLs.add(urlsIn.nextLine());
		}
		if (urlsIn != null) urlsIn.close();
		return imageURLs;
	}
}
