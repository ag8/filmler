import Utils.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
	public final static String urlBase = System.getProperty("user.dir");

	public static Map<String, String> imagesToPeople;

	public static List<String> imageURLs;

	public static List<Person> listOfPeople;

	public static void main(String[] args) throws FileNotFoundException {
		// Set up main image urls from urls.txt
		Scanner urlsFile = new Scanner(new File(urlBase + "/files/urls.txt"));
		imageURLs = new ArrayList<>();
		while (urlsFile.hasNextLine()) {
			imageURLs.add(urlsFile.nextLine());
		}
		urlsFile.close();


		imagesToPeople = TagFaces.run(args);

		System.out.println("Images and People: " + imagesToPeople);

		boolean success = false;
		/*try {
			success = new Main().analyze();
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}*/

		System.out.println(success ? "Success." : "Fail.");

		// Create list of people
		listOfPeople = new ArrayList<>();
		for (String image : imagesToPeople.keySet()) {
			System.out.println("Now analyzing " + image + " and " + imagesToPeople.get(image) + "______________________");

			String personName = imagesToPeople.get(image);
			int indexOfPersonInOfficialListOfPeople = -1;
			for (Person p : listOfPeople) {
				if (p.getName().equals(personName)) {
					indexOfPersonInOfficialListOfPeople = listOfPeople.indexOf(p);
				}
			}

			if (indexOfPersonInOfficialListOfPeople > -1) { // Person found in list
				listOfPeople.get(indexOfPersonInOfficialListOfPeople).addLocalURL(urlBase + "/images/" + image);
			} else {
				System.out.println("Adding image " + image);
				Person newPerson = new Person(imagesToPeople.get(image));

				newPerson.addLocalURL(urlBase + "/images/" + image);
				listOfPeople.add(newPerson);
			}
		}

		boolean success2 = false;
		/*try {
			success2 = new Main().analyzeInteractions();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		System.out.println(success2 ? "Success2." : "Fail2.");
	}

	/*public boolean analyzeInteractions() throws IOException {
		for ( Person p : listOfPeople) {
			for (String localURL : p.getLocalURLs()) {
				System.out.println("Person " + p.getName() + " has local URL " + localURL);

				BufferedImage img = null;
				try {
					img = ImageIO.read(new File(localURL));
				} catch (IOException e) {
					e.printStackTrace();
				}

				String base64string = ImageUtils.encodeToString(img, "jpg");

				String g = ImageUtils.uploadImage(base64string);

				List<String> images = p.getImages();
				images.add(g);
				p.setImages(images);
			}
		}

		for ( Person p : listOfPeople) {
			System.out.println(p.getName() + "::::::::::::::::::::::::::::::::::::::::::::::");
			for (String url : p.getImages()) {
				System.out.println(url);
			}
		}

		FaceRec.groupCreate("test1", "sigmapeople1");
		for (Person p : listOfPeople) {
			FaceRec.personCreate("test1", p.getName(), "sigmapeople1");
		}

		System.out.println(FaceRec.groupGetInfo("sigmapeople1"));

		return true;
	}


	public boolean analyze() throws IOException, URISyntaxException {
		for (String imageURL : imageURLs) {
			System.out.println("Doing " + imageURL);
			FaceRec.faceAnalyze(imageURL, false);
		}

		return true;
	}*/
}
