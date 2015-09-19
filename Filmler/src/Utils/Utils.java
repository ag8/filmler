package Utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class Utils {
    public static BufferedImage cropImage(BufferedImage src, Rectangle rect) throws IOException {
        BufferedImage dest = src.getSubimage(rect.x, rect.y, rect.width, rect.height);
        return dest;
    }

    public static void cropAndSave(BufferedImage src, Rectangle rect, String filename) throws IOException {
        File outputfile = new File(System.getProperty("user.dir") + "\\images\\" + filename + ".jpg");
        ImageIO.write(cropImage(src, rect), "jpg", outputfile);
    }
}
