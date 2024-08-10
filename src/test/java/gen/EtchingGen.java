package gen;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

public class EtchingGen {

	public static void main(String[] args) throws Exception {
		// 0 black 1 adj 2 white
		int n = 4;
		int w = 128;
		int h = 16;
		int len = w * h;
		long seed = 31483465135L;
		Random r = new Random(seed);
		int[][] type = new int[len][n];
		for (int i = 0; i < len; i++) {
			if (i == 0) {
				for (int j = 0; j < n; j++) {
					type[i][j] = 1;
				}
			} else {
				for (int j = 0; j < n; j++) {
					int left = type[i - 1][j];
					int top = 0;
					int tl = 0;
					int dl = 0;
					if (j > 0) {
						top = type[i][j - 1];
						tl = type[i - 1][j - 1];
					}
					if (j + 1 < n) {
						dl = type[i - 1][j + 1];
					}
					float chance = 0;
					int ans = 0;
					if (left == 2 && top == 2) {
						chance = 0.8f;
						ans = 1;
						if (tl == 2 || dl == 2) chance = 0;
					} else if (left == 2 || top == 2) {
						chance = left == 0 ? 0.8f : 0.5f;
						ans = 1;
						if (tl == 2 || dl == 2) chance = 0.3f;
					} else {
						chance = left == 0 ? 0.8f : 0.4f;
						if (tl == 2 || dl == 2) chance = 0;
					}
					if (chance > 0) {
						if (r.nextFloat() < chance) {
							ans = 2;
						}
					}
					type[i][j] = ans;
				}
			}
		}
		var img = new BufferedImage(w, (n + 4) * h, BufferedImage.TYPE_4BYTE_ABGR);
		int white = 0xffffffff;
		int black = 0x00ffffff;
		for (int i = 0; i < len; i++) {
			int x = i % w;
			int y = i / w * (n + 4);
			img.setRGB(x, y + 0, white);
			img.setRGB(x, y + 1, black);
			for (int j = 0; j < n; j++) {
				img.setRGB(x, y + j + 2, type[i][j] == 2 ? white : black);
			}
			img.setRGB(x, y + n + 2, black);
			img.setRGB(x, y + n + 3, white);
		}
		File file = new File("./src/test/resources/generated/spell_circle_" + n + ".png");
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		if (!file.exists()) {
			file.createNewFile();
		}
		ImageIO.write(img, "PNG", file);
	}

}
