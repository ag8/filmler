package org.gandkco.filmler;

import Utils.FaceRec;
import Utils.Utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Utils.*;

public class Main {
    public static Map<String, String> imagesAndPeople = new HashMap<>();

    static String[] imageURLs = {
            "http://sigmacamp.org/sites/default/files/galleries/301/20140817-A96A8206.jpg", //Dashka and Christina
            "http://sigmacamp.org/sites/default/files/galleries/297/20140815-A96A7804.jpg", //Shmoo
            "http://sigmacamp.org/sites/default/files/galleries/303/20140818-A96A9568.jpg", //Dashka and Christina
            "http://sigmacamp.org/sites/default/files/galleries/303/20140818-A96A9638.jpg",
            "http://sigmacamp.org/sites/default/files/galleries/303/20140818-A96A9667.jpg",
            "http://sigmacamp.org/sites/default/files/galleries/303/20140818-A96A9633.jpg"  //Dashka, Lena,
    };

    public static List<Person> officialListOfPeople = new ArrayList<>();

    public static void main(String[] args) {
        TagFaces.run(args);

        System.out.println(imagesAndPeople);

        boolean success = false;
        try {
            success = new Main().analyze();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
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
        boolean success = false;

        for ( Person p : officialListOfPeople ) {
            for (String localURL : p.getLocalURLs()) {
                System.out.println("Person " + p.getName() + " has local URL " + localURL);

                BufferedImage img = null;
                try {
                    img = ImageIO.read(new File(localURL));
                } catch (IOException e) {

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

//        FaceRec.groupCreate("test1", "sigmapeople1");
//        FaceRec.personCreate()

        success = true;
        return success;
    }

    public boolean analyze() throws IOException, URISyntaxException {
        boolean success = false;

        for (int i = 0; i < imageURLs.length; i++) {
            String imageURL = imageURLs[i];
            System.out.println("Doing " + imageURL);
            FaceRec.faceAnalyze(imageURL, false);
        }

        success = true;
        return success;
    }
}
