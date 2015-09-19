package Utils;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class FaceRec {
    /**
     * Creates a group
     * @param groupTag - the group tag
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
     * @param tag - the person's tag
     * @param personName - the person's name
     * @param groupName - the group name
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
     * Creates a faceset.
     * @param tag - the faceset's tag
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
