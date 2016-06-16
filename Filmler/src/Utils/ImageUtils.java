package Utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtils {
	final static String urlBase = System.getProperty("user.dir") + "/images/";

	public static BufferedImage cropImage(BufferedImage src, Rectangle rect) {
		return src.getSubimage(rect.x, rect.y, rect.width, rect.height);
	}

	public static Face cropAndSave(BufferedImage src, Rectangle rect, String ID, String filename) throws IOException {
		File outputFile = new File(urlBase + filename + ".jpg");
		ImageIO.write(cropImage(src, rect), "jpg", outputFile);
		return new Face(outputFile, ID);
	}


}
