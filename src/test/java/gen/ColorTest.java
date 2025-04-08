package gen;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ColorTest {

	public static void main(String[] args) throws Exception {
		File in = new File("./temp/reimu_cloth.png");
		File bf = new File("./temp/reimu.png");
		var base = ImageIO.read(bf);
		var img = ImageIO.read(in);
		var ans = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				int color = img.getRGB(x, y);
				int a = (color >> 24) & 0xff;
				int r = (color >> 16) & 0xff;
				int g = (color >> 8) & 0xff;
				int b = (color) & 0xff;
				int gb = (g + b) / 2;
				//if (a > 0)
					ans.setRGB(x, y, a << 24 | 0 << 16 | 0 << 8 | r);
				//else ans.setRGB(x, y, base.getRGB(x, y));
			}
		}
		File out = new File("./temp/reimu_blue_test.png");
		if (!out.exists()) out.createNewFile();
		ImageIO.write(ans, "PNG", out);
	}

}
