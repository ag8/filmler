package Utils;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

public class Utils {
    public static BufferedImage cropImage(BufferedImage src, Rectangle rect) throws IOException {
        BufferedImage dest = src.getSubimage(rect.x, rect.y, rect.width, rect.height);
        return dest;
    }

    public static void cropAndSave(BufferedImage src, Rectangle rect, String filename) throws IOException {
        File outputfile = new File(System.getProperty("user.dir") + "\\images\\" + filename + ".jpg");
        ImageIO.write(cropImage(src, rect), "jpg", outputfile);
    }

    public static BufferedImage decodeToImage(String imageString) {

        BufferedImage image = null;
        byte[] imageByte;
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            imageByte = decoder.decodeBuffer(imageString);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
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


        postParameters = new ArrayList<NameValuePair>();
        postParameters.add(new BasicNameValuePair("image", image));
//        postParameters.add(new BasicNameValuePair("param2", "param2_value"));

        httppost.setEntity(new UrlEncodedFormEntity(postParameters));

        HttpResponse response = httpclient.execute(httppost);
        OutputStream outputStream = new ByteArrayOutputStream();
        response.getEntity().writeTo(outputStream);
        return outputStream.toString();
    }
}
