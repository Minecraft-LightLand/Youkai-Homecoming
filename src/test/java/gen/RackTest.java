package gen;

public class RackTest {

	public static void main(String[] args) {
		double total = 1;
		int n = 14;
		double[] heat = new double[n];
		for (int i = 0; i < n; i++) {
			heat[i] += total * 0.05;
			total *= 0.95;
		}
		for (int i = n - 1; i >= 0; i--) {
			heat[i] += total * 0.2;
			total *= 0.8;
		}
		StringBuilder sb = new StringBuilder("Racks: ");
		for (int i = 0; i < n; i++) {
			sb.append((int) Math.ceil(20 / heat[i]));
			if (i < n - 1) sb.append(", ");
		}
		System.out.println(sb);
		System.out.println("Remain: " + Math.round(total * 10000) / 100d);
	}

}
