package gen;

public class RackTest {

	public static void main(String[] args) {
		test(20, 2);
		test(20, 6);
		test(20, 10);
		test(20, 14);
		test(20, 18);
	}

	private static void test(int max, int n) {
		double total = max;
		double[] heat = new double[n];
		for (int i = 0; i < n; i++) {
			heat[i] += total * 0.04;
			total *= 0.96;
		}
		for (int i = n - 1; i >= 0; i--) {
			heat[i] += total * 0.2;
			total *= 0.8;
		}
		StringBuilder sb = new StringBuilder("Racks: ");
		for (int i = 0; i < n; i++) {
			sb.append((int) Math.ceil(40 / heat[i]));
			if (i < n - 1) sb.append(", ");
		}
		System.out.println("Layer: " + n);
		System.out.println(sb);
		System.out.println("Remain: " + Math.round(total * 10000d / max) / 100d + "%");
	}

}
