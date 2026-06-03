public class CO3SET {

    static int n = 15;
    static int[] bit = new int[n + 1];

    static void update(int index, int value) {
        while (index <= n) {
            bit[index] += value;
            index += index & (-index);
        }
    }

    static int prefixSum(int index) {
        int sum = 0;
        while (index > 0) {
            sum += bit[index];
            index -= index & (-index);
        }
        return sum;
    }

    static int rangeSum(int left, int right) {
        return prefixSum(right) - prefixSum(left - 1);
    }

    public static void main(String[] args) {

        int[] spend = {0, 1200, 800, 0, 2400, 1500, 600, 0, 0,
                       3500, 0, 1100, 950, 700, 0};

        for (int i = 1; i <= n; i++) {
            update(i, spend[i - 1]);
        }

        int totalSpend = rangeSum(5, 12);

        System.out.println("HDFC NetBanking - Daily Spend Analysis");
        System.out.println();

        System.out.println("Days Recorded : 15");
        System.out.println("Data Structure : Binary Indexed Tree (Fenwick Tree)");
        System.out.println();

        System.out.println("Daily Spend Data:");
        for (int i = 1; i <= n; i++) {
            System.out.print(spend[i - 1] + " ");
        }

        System.out.println("\n");
        System.out.println("Query Spend [5,12]");
        System.out.println("Total Spend : " + totalSpend);
        System.out.println();

        System.out.println("Operations Performed:");
        System.out.println("Point Updates : Successful");
        System.out.println("Range Sum Query : Successful");
        System.out.println();

        System.out.println("Time Complexity:");
        System.out.println("Update : O(log n)");
        System.out.println("Range Query : O(log n)");
    }
}