package dev.xkmc.youkaishomecoming.init.food;

public class InitializationMarker {

	private static int step = 0;

	public static synchronized void expectAndAdvance(int old) {
		if (step != old) {
			throw new IllegalStateException("Initialization was carried out at wrong order");
		} else {
			step++;
		}
	}

}
