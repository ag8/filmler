package org.gandkco.filmler;

import Utils.FaceRec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

public class Main {
    String imageURL = "http://sigmacamp.org/sites/default/files/galleries/301/20140817-A96A8206.jpg";

    public static void main(String[] args) {
        boolean success = false;
        try {
            success = new Main().analyze();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        System.out.println(success ? "Success." : "Fail.");
    }

    public boolean analyze() throws IOException, URISyntaxException {
        boolean success = false;

        int faceNum = FaceRec.getFaceNum(imageURL);

        FaceRec.faceAnalyze(imageURL);

        System.out.println(faceNum);

        success = true;
        return success;
    }
}
