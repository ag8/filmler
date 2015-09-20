package Utils;

import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class FaceRec {
    /**
     * Creates a group
     *
     * @param groupTag  - the group tag
     * @param groupName - the group name
     * @return - JSON response
     * @throws IOException - Yeah, well, sucks.
     */
    public static String groupCreate(String groupTag, String groupName) throws IOException {
        String results = "";

        //https://apius.faceplusplus.com/v2/group/create?api_key=YOUR_API_KEY&api_secret=YOUR_API_SECRET&tag=created_by_Alice&group_name=Family
        URL url = new URL("https://apius.faceplusplus.com/v2/group/create?api_key=" + Constants.API_KEY + "&api_secret=" + Constants.SECRET_KEY + "&tag=" + groupTag + "&group_name=" + groupName);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(url.openStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null)
            results += inputLine + "\n";
        in.close();

        return results;
    }


    /**
     * Creates a person.
     *
     * @param tag        - the person's tag
     * @param personName - the person's name
     * @param groupName  - the group name
     * @return - the JSON response
     * @throws IOException - sucks
     */
    public static String personCreate(String tag, String personName, String groupName) throws IOException {
        String results = "";

        //https://apius.faceplusplus.com/v2/person/create?api_key=YOUR_API_KEY&api_secret=YOUR_API_SECRET&tag=demotest&person_name=NicolasCage&group_name=SuperStars
        URL url = new URL("https://apius.faceplusplus.com/v2/person/create?api_key=" + Constants.API_KEY + "&api_secret=" + Constants.SECRET_KEY + "&tag=" + tag + "&person_name=" + personName + "&group_name=" + groupName);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(url.openStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null)
            results += inputLine + "\n";
        in.close();

        return results;
    }


    /**
     * Adds a face to the Face++ database.
     *
     * @param imageURL - the url of the image
     * @return - the unique ID of the image
     */
    public static String addFace(String imageURL) throws IOException {
        String results = "";

        //https://apius.faceplusplus.com/v2/detection/detect?url=http%3A%2F%2Ffaceplusplus.com%2Fstatic%2Fimg%2Fdemo%2F1.jpg&api_secret=YOUR_API_SECRET&api_key=YOUR_API_KEY&attribute=glass,pose,gender,age,race,smiling
        URL url = new URL("https://apius.faceplusplus.com/v2/detection/detect?url=" + URLEncoder.encode(imageURL) + "&api_secret=" + Constants.SECRET_KEY + "&api_key=" + Constants.API_KEY + "&attribute=glass,pose,gender,age,race,smiling");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(url.openStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null)
            results += inputLine + "\n";
        in.close();

        String[] parts = results.split("\"face_id\": \"");
        String[] subparts = parts[1].split("\",\n" +
                "            \"position\": \\{\n" +
                "                \"center\": \\{\n" +
                "                    \"x\": ");
        String id = subparts[0];

        return id;
    }

    /**
     * Creates a faceset.
     *
     * @param tag         - the faceset's tag
     * @param facesetName - the faceset's name
     * @return - the JSON response
     * @throws IOException - I/O Exception
     */
    public static String facesetCreate(String tag, String facesetName) throws IOException {
        String results = "";

        //https://apius.faceplusplus.com/v2/faceset/create?api_key=YOUR_API_KEY&api_secret=YOUR_API_SECRET&tag=demotest&faceset_name=NicolasCage
        URL url = new URL("https://apius.faceplusplus.com/v2/faceset/create?api_key=" + Constants.API_KEY + "&api_secret=" + Constants.SECRET_KEY + "&tag=" + tag + "&faceset_name=" + facesetName);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(url.openStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null)
            results += inputLine + "\n";
        in.close();

        return results;
    }

    public static int getFaceNum(String imageURL) throws IOException {
        String result = getFaces(imageURL);

        int faces = StringUtils.countMatches(result, "attribute");

        return faces;
    }

    public static String getFaces(String imageURL) throws IOException {
        String results = "";

        //https://apius.faceplusplus.com/v2/detection/detect?url=http://sigmacamp.org/sites/default/files/galleries/301/20140817-A96A8117.jpg&api_secret=bg71y3KbnCUUYQjxqX6pWyH0JNFVsv-L&api_key=aa8b4b83d3c198dab0cb90e915f1cdf1&attribute=glass,pose,gender,age,race,smiling
        URL url = new URL("https://apius.faceplusplus.com/v2/detection/detect?url=" + imageURL + "&api_secret=" + Constants.SECRET_KEY + "&api_key=" + Constants.API_KEY + "&attribute=glass,pose,gender,age,race,smiling");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(url.openStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null)
            results += inputLine + "\n";
        in.close();

        return results;
    }

    public static void faceAnalyze(String imageURL, boolean riskEdges) throws IOException {
        List<Float> faceHeights = new ArrayList<Float>();
        List<Float> faceCenterXs = new ArrayList<Float>();
        List<Float> faceCenterYs = new ArrayList<Float>();

        String results = getFaces(imageURL);


        /*
        GET HEIGHTS
         */

        String[] parts = results.split("}, \n" +
                "                \"height\": ");

        for (int i = 1; i < parts.length; i++) { //Start with 1, because 0 is trash
            String p = parts[i];

            String[] subParts = p.split(", \n" +
                    "                \"mouth_left\":");
            float height = Float.parseFloat(subParts[0]);
            faceHeights.add(height);
        }


        /*
        GET CENTER XS
         */

        String[] parts2 = results.split("\"center\": \\{\n" +
                "                    \"x\": ");

        for (int i = 1; i < parts2.length; i++) { //Start with 1, because 0 is trash
            String p = parts2[i];

            String[] subParts = p.split(", \n" +
                    "                    \"y\": ");
            float faceCenterX = Float.parseFloat(subParts[0]);
            faceCenterXs.add(faceCenterX);
        }


        /*
        GET CENTER YS
         */

        String[] parts3 = results.split("\\}");

        for (int i = 1; i < parts3.length; i++) { //Start with 1, because 0 is trash
            if (parts3[i - 1].contains("center")) {
                String[] partsOfThat = parts3[i - 1].split(", \n" +
                        "                    \"y\": ");

                String[] subParts = partsOfThat[1].split("\n" +
                        "                ");

                float faceCenterY = Float.parseFloat(subParts[0]);
                faceCenterYs.add(faceCenterY);
            }
        }


        //Now, we crop the faces

        //Get absolute pixels

        //First, get the image width and the image height
        //h = height
        //w = width

        String[] getHeight0 = results.split("\"img_height\": ");
        String[] getHeight1 = getHeight0[1].split(", \n" +
                "    \"img_id\": \"");
        float h = Float.parseFloat(getHeight1[0]);
        System.out.println("Height: " + h);

        String[] getWidth0 = results.split("\", \n" +
                "    \"img_width\": ");
        String[] getWidth1 = getWidth0[1].split(", \n" +
                "    \"session_id\": \"");
        float w = Float.parseFloat(getWidth1[0]);
        System.out.println("Width: " + w);


        //Get the actual face heights
        List<Float> actualFaceHeights = new ArrayList<Float>();
        for (float faceHeight : faceHeights) {
            actualFaceHeights.add(faceHeight * h / 100);
        }

        //Get the actual x-values TODO: Make sure this isn't switched with the y-values
        List<Float> actualXValues = new ArrayList<Float>();
        for (float xValue : faceCenterXs) {
            actualXValues.add(xValue * w / 100);
        }

        for (float actualXValue : actualXValues) {
            System.out.println("aXV:" + actualXValue);
        }

        //Get the actual y-values TODO: Make sure this isn't switched with the x-values
        List<Float> actualYValues = new ArrayList<Float>();
        for (float yValue : faceCenterYs) {
            actualYValues.add(yValue * h / 100);
        }

        List<Float> topCornerXs = new ArrayList<Float>();
        for (int i = 0; i < actualFaceHeights.size(); i++) {
            topCornerXs.add(actualXValues.get(i) - actualFaceHeights.get(i) / (float) 2.0);
        }

        List<Float> topCornerYs = new ArrayList<Float>();
        for (int i = 0; i < actualFaceHeights.size(); i++) {
            topCornerYs.add(actualYValues.get(i) - actualFaceHeights.get(i) / (float) 2.0);
        }

        for (int i = 0; i < actualFaceHeights.size(); i++) {
            Rectangle rect;

            BufferedImage img = ImageIO.read(new URL(imageURL));
            if (riskEdges) {
                rect = new Rectangle(0, 0, 1, 1); //Improve this @derikk
            } else {
                rect = new Rectangle((int) Math.floor(topCornerXs.get(i)), (int) Math.floor(topCornerYs.get(i)), (int) Math.floor(actualFaceHeights.get(i)), (int) Math.floor(actualFaceHeights.get(i)));
            }

            Utils.cropAndSave(img, rect, "image" + Variables.imageNumber + "");
            Variables.imageNumber++;
        }
    }


    public static List<String> getFaceJSON(String imageURL) throws IOException, URISyntaxException {
        List<String> results = new ArrayList<String>();

        //Url: http://apius.faceplusplus.com/v2/detection/detect?api_key=aa8b4b83d3c198dab0cb90e915f1cdf1&api_secret=bg71y3KbnCUUYQjxqX6pWyH0JNFVsv-L&url=https://electronneutrino.com/stalking/stalkb.jpg&attribute=age%2Cgender%2Crace%2Csmiling%2Cpose%2Cglass

        URL url = new URL("http://apius.faceplusplus.com/v2/detection/detect?api_key=" + Constants.API_KEY + "&api_secret=" + Constants.SECRET_KEY + "&url=" + imageURL + "&attribute=age%2Cgender%2Crace%2Csmiling%2Cpose%2Cglass");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(url.openStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null)
            results.add(inputLine);
        in.close();

        return results;
    }
}
