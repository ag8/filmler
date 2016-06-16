package Utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.*;

public class FaceRec {

	static int imageNumber = 0;

	/* GETs the text of a webpage */
	private static String get(String url) {
		String results = "";
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				results += inputLine + "\n";
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return results;
	}


	/* Face methods */
	public static String detectFaces(String imageURL) {
		//https://apius.faceplusplus.com/v2/detection/detect?url=http://sigmacamp.org/sites/default/files/galleries/301/20140817-A96A8117.jpg&api_key=aa8b4b83d3c198dab0cb90e915f1cdf1&api_secret=bg71y3KbnCUUYQjxqX6pWyH0JNFVsv-L&attribute=none
		return get("https://apius.faceplusplus.com/v2/detection/detect?api_key=" + Keys.API_KEY + "&api_secret=" + Keys.SECRET_KEY + "&url=" + imageURL + "&attribute=none");
	}

	public static String getFaceInfo(Face face) {
		return get("https://apius.faceplusplus.com/v2/info/get_face?api_key=" + Keys.API_KEY + "&api_secret=" + Keys.SECRET_KEY + "&face_id=" + face.ID);
	}

	public static List<Face> createFaces(String imageURL) {
		String results = detectFaces(imageURL);
		JSONObject json = new JSONObject(results);

		int imageHeight = json.getInt("img_height");
		int imageWidth = json.getInt("img_width");
		System.out.println("Image Height: " + imageHeight);
		System.out.println("Image Width: " + imageWidth);

		JSONArray faceArray = json.getJSONArray("face");
		int faceNum = faceArray.length();


		// Get face heights, centers, and IDs.
		// We don't need widths because we crop to squares.

		int[] faceHeights = new int[faceNum];
		int[] faceCenterXs = new int[faceNum];
		int[] faceCenterYs = new int[faceNum];
		String[] faceIDs = new String[faceNum];

		for (int i = 0; i < faceNum; i++) {
			JSONObject face = faceArray.getJSONObject(i);
			faceIDs[i] = face.getString("face_id");

			JSONObject position = face.getJSONObject("position");
			faceHeights[i] = (int) (position.getDouble("height") / 100 * imageHeight); // Convert percent to pixel

			JSONObject center = position.getJSONObject("center");
			faceCenterXs[i] = (int) (center.getDouble("x") / 100 * imageWidth);
			faceCenterYs[i] = (int) (center.getDouble("y") / 100 * imageHeight);

		}



		// Now, we crop the faces

		int[] topCornerXs = new int[faceNum];
		for (int i = 0; i < faceNum; i++) {
			topCornerXs[i] = faceCenterXs[i] - faceHeights[i] / 2;
		}

		int[] topCornerYs = new int[faceNum];
		for (int i = 0; i < faceNum; i++) {
			topCornerYs[i] = faceCenterYs[i] - faceHeights[i] / 2;
		}

		List<Face> faceList = new ArrayList<>(faceNum);
		for (int i = 0; i < faceNum; i++) {
			try {
				Rectangle rect;
				URL url = new URL(imageURL);
				BufferedImage img = ImageIO.read(url);
				System.out.println("Cropping a face from " + imageURL);
				rect = new Rectangle(topCornerXs[i], topCornerYs[i], faceHeights[i], faceHeights[i]);
				String ID = faceIDs[i];

				faceList.add(ImageUtils.cropAndSave(img, rect, ID, "image" + imageNumber++));
			} catch (IOException e) {
				System.out.println("Can't make cropped image from " + imageURL);
				e.printStackTrace();
			}

		}

		return faceList;
	}


	/* Group methods */
	/**
	 * Creates a group
	 *
	 * @param groupName - the group name
	 * @return - JSON response
	 */
	public static String createGroup(String groupName) {
		//https://apius.faceplusplus.com/v2/group/create?api_key=YOUR_API_KEY&api_secret=YOUR_API_SECRET&tag=created_by_Alice&group_name=Family
		return get("https://apius.faceplusplus.com/v2/group/create?api_key=" + Keys.API_KEY + "&api_secret=" + Keys.SECRET_KEY +  "&group_name=" + groupName);
	}

	public static String getGroupInfo(String groupName) {
		//https://apius.faceplusplus.com/v2/group/get_info?api_key=YOUR_API_KEY&api_secret=YOUR_API_SECRET&group_name=Family
		return get("https://apius.faceplusplus.com/v2/group/get_info?api_key=" + Keys.API_KEY + "&api_secret=" + Keys.SECRET_KEY + "&group_name=" + groupName);
	}


	/* Person methods */
	/**
	 * Creates a person
	 *
	 * @param personName - the person's name
	 * @param groupName  - the group name
	 * @return - the JSON response
	 */
	public static String createPerson(String personName, String groupName) {
		//https://apius.faceplusplus.com/v2/person/create?api_key=YOUR_API_KEY&api_secret=YOUR_API_SECRET&tag=demotest&person_name=NicolasCage&group_name=SuperStars
		return get("https://apius.faceplusplus.com/v2/person/create?api_key=" + Keys.API_KEY + "&api_secret=" + Keys.SECRET_KEY + "&person_name=" + personName + "&group_name=" + groupName);
	}

	public static String associateFace(String personName, String faceID) {
		return get("https://apius.faceplusplus.com/v2/person/add_face?api_key=" + Keys.API_KEY + "&api_secret=" + Keys.SECRET_KEY + "&person_name=" + personName + "&face_id=" + faceID);
	}

	/**
	 * Trains the identification model for a group
	 *
	 * @param groupName - the group name
	 * @return - The session ID
	 */
	public static String trainIdentify(String groupName) {
		return get("https://apius.faceplusplus.com/v2/train/identify?api_key=" + Keys.API_KEY + "&api_secret=" + Keys.SECRET_KEY + "&group_name=" + groupName);
	}


	public static List<String> identifyFaces(String groupName, String imageURL) {
		JSONObject json = new JSONObject(get("https://apius.faceplusplus.com/v2/recognition/identify?api_key=" + Keys.API_KEY + "&api_secret=" + Keys.SECRET_KEY + "&group_name=" + groupName + "&url=" + imageURL));
		JSONArray faceArray = json.getJSONArray("face");

		List<String> identifiedPeople = new ArrayList<>(); // List of person names of people in photo

		for (int i = 0; i < faceArray.length(); i++) {
			JSONArray candidateArray = faceArray.getJSONArray(i);
			for (int j = 0; j < candidateArray.length(); j++) {
				if (candidateArray.getJSONObject(i).getDouble("confidence") > 90) {
					identifiedPeople.add(candidateArray.getJSONObject(i).getString("person_name"));
				}
			}

		}
		return identifiedPeople;
	}

	/* Faceset methods */
	/*public static String createFaceset(String facesetName) {
		return get("https://apius.faceplusplus.com/v2/faceset/create?api_key=" + Keys.API_KEY + "&api_secret=" + Keys.SECRET_KEY + "&faceset_name=" + facesetName);
	}*/


}
