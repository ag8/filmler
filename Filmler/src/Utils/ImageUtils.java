package Utils;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class ImageUtils {
	final static String urlBase = System.getProperty("user.dir") + "/images/";

	public static BufferedImage cropImage(BufferedImage src, Rectangle rect) throws IOException {
		return src.getSubimage(rect.x, rect.y, rect.width, rect.height);
	}

	public static void cropAndSave(BufferedImage src, Rectangle rect, String filename) throws IOException {
		File outputfile = new File(urlBase + filename + ".jpg");
		ImageIO.write(cropImage(src, rect), "jpg", outputfile);
	}

	public static String encodeToString(BufferedImage image, String type) {
		String imageString = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		try {
			ImageIO.write(image, type, bos);
			byte[] imageBytes = bos.toByteArray();

			BASE64Encoder encoder = new BASE64Encoder();
			imageString = encoder.encode(imageBytes);

			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return imageString;
	}

	public static String uploadImage(String image) throws IOException {
		HttpClient httpclient;
		HttpPost httppost;
		ArrayList<NameValuePair> postParameters;
		httpclient = new DefaultHttpClient();
		httppost = new HttpPost("http://electronneutrino.com/lexhack/api.php");


		postParameters = new ArrayList<>();
		postParameters.add(new BasicNameValuePair("image", image));

		httppost.setEntity(new UrlEncodedFormEntity(postParameters));

		HttpResponse response = httpclient.execute(httppost);
		OutputStream outputStream = new ByteArrayOutputStream();
		response.getEntity().writeTo(outputStream);
		return outputStream.toString();
	}
}
