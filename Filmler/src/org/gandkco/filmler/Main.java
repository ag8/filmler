package org.gandkco.filmler;

import Utils.FaceRec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

public class Main {
    String imageURL = "http://blog.cleveland.com/nationworld_impact/2009/09/large_barack-obama-cabinet-091009.jpg";

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

        FaceRec.faceAnalyze(imageURL, false);

        System.out.println(faceNum);
        success = true;
        return success;
    }
}
