package gen;

import javax.imageio.ImageIO;
import java.io.File;

public class WaterColorizor {

	public static void main(String[] args) throws Exception {
		File file = new File("./src/test/resources/generated/water_still.png");
		File out = new File("./src/test/resources/generated/out/water_still.png");
		var image = ImageIO.read(file);
		int alpha = 0;
		for (var i = 0; i < image.getWidth(); i++) {
			for (var j = 0; j < image.getHeight(); j++) {
				int a = (image.getRGB(i, j) >> 24) & 0xff;
				if (a > alpha) {
					alpha = a;
				}
			}
		}
		for (var i = 0; i < image.getWidth(); i++) {
			for (var j = 0; j < image.getHeight(); j++) {
				int co = image.getRGB(i, j);
				int a = (co >> 24) & 0xff;
				int na = (a * 255 / alpha) & 0xff;
				int c = co & 0xffffff | (na << 24);
				image.setRGB(i, j, c);
			}
		}
		ImageIO.write(image, "PNG", out);
		System.out.println(alpha);
	}

}
