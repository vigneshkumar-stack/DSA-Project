public class CO2SET {
    public static void main(String[] args) {

        double[] surge = new double[16];

        for (int i = 0; i < 16; i++) {
            surge[i] = 1.0;
        }

        // Update [3,9] += 0.5
        for (int i = 3; i <= 9; i++) {
            surge[i] += 0.5;
        }

        // Update [7,14] += 0.3
        for (int i = 7; i <= 14; i++) {
            surge[i] += 0.3;
        }

        double max = surge[0];
        for (int i = 1; i < 16; i++) {
            if (surge[i] > max) {
                max = surge[i];
            }
        }

        System.out.println("Total Zones : 16");
        System.out.println("Maximum Surge : " + max);
        System.out.println("Range Updates : Successful");
    }
}