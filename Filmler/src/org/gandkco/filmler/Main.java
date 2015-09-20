package org.gandkco.filmler;

import Utils.FaceRec;
import Utils.Utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import Utils.*;

public class Main {
    public static Map<String, String> imagesAndPeople = new HashMap<>();

    static String[] imageURLs;

    public static List<Person> officialListOfPeople = new ArrayList<>();

    public static void main(String[] args) throws FileNotFoundException {
        //Set up main image urls from urls.gak

        AtomicReference<String> token1 = new AtomicReference<>();
        Scanner inFile1 = new Scanner(new File("C:\\Users\\Irochka\\Documents\\GitHub\\filmler\\Filmler\\files\\urls.gak")).useDelimiter(",\\s*");
        List<String> temps = new ArrayList<>();
        while (inFile1.hasNext()) {
            token1.set(inFile1.next());
            temps.add(token1.get());
        }
        inFile1.close();
        imageURLs = temps.toArray(new String[temps.size()]);


        TagFaces.run(args);

        System.out.println(imagesAndPeople);

        boolean success = false;
        try {
            success = new Main().analyze();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        System.out.println(success ? "Success." : "Fail.");

        //CREATE OFFICIAL LIST OF PEOPLE
        for (String image : imagesAndPeople.keySet()) {
            System.out.println("Now analyzing " + image + " and " + imagesAndPeople.get(image) + "______________________");

            String personName = imagesAndPeople.get(image);
            boolean officialListOfPeopleContainsPerson = false;
            int indexOfPersonInOfficialListOfPeople = -1;
            for (Person p : officialListOfPeople) {
                if (p.getName().equals(personName)) {
                    officialListOfPeopleContainsPerson = true;
                    indexOfPersonInOfficialListOfPeople = officialListOfPeople.indexOf(p);
                }
            }

            if (officialListOfPeopleContainsPerson) {
                officialListOfPeople.get(indexOfPersonInOfficialListOfPeople).addLocalURL("C:\\Users\\Irochka\\Documents\\GitHub\\filmler\\Filmler\\images\\" + image);
            } else {
                System.out.println("Adding image " + image);
                Person newPerson = new Person(imagesAndPeople.get(image));

                newPerson.addLocalURL("C:\\Users\\Irochka\\Documents\\GitHub\\filmler\\Filmler\\images\\" + image);
                officialListOfPeople.add(newPerson);
            }
        }

        boolean success2 = false;
        try {
            success2 = new Main().analyzeInteractions();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(success2 ? "Success2." : "Fail2.");
    }

    public boolean analyzeInteractions() throws IOException {
        for ( Person p : officialListOfPeople ) {
            for (String localURL : p.getLocalURLs()) {
                System.out.println("Person " + p.getName() + " has local URL " + localURL);

                BufferedImage img = null;
                try {
                    img = ImageIO.read(new File(localURL));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String base64string = Utils.encodeToString(img, "jpg");

                String g = Utils.uploadImage(base64string);

                List<String> images = p.getImages();
                images.add(g);
                p.setImages(images);
            }
        }

        for ( Person p : officialListOfPeople ) {
            System.out.println(p.getName() + "::::::::::::::::::::::::::::::::::::::::::::::");
            for (String url : p.getImages()) {
                System.out.println(url);
            }
        }

        FaceRec.groupCreate("test1", "sigmapeople1");
        for (Person p : officialListOfPeople) {
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
    }
}
