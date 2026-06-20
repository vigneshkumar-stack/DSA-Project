import java.util.*;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class DeliveryNetwork {

    // ---------- 1. Graph (Non-Linear Data Structure) ----------
    // Adjacency list: node -> list of {neighbor, weight}
    static Map<Integer, List<int[]>> graph = new HashMap<>();

    static void addEdge(int u, int v, int w) {
        graph.computeIfAbsent(u, k -> new ArrayList<>()).add(new int[]{v, w});
        graph.computeIfAbsent(v, k -> new ArrayList<>()).add(new int[]{u, w});
    }

    // ---------- 2. Dijkstra's Greedy Algorithm (uses a min-heap) ----------
    // Time: O((V + E) log V) | Greedy choice: always expand the nearest unvisited hub
    static int[] dijkstra(int src, int n) {
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[src] = 0;

        PriorityQueue<int[]> minHeap = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        minHeap.offer(new int[]{0, src});            // {distance, node}

        while (!minHeap.isEmpty()) {
            int[] top = minHeap.poll();
            int d = top[0], u = top[1];
            if (d > dist[u]) continue;

            for (int[] edge : graph.getOrDefault(u, Collections.emptyList())) {
                int v = edge[0], w = edge[1];
                if (dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    minHeap.offer(new int[]{dist[v], v});
                }
            }
        }
        return dist;
    }

    // ---------- 3. 0/1 Knapsack (Dynamic Programming) ----------
    // Time: O(n * W) | Required instead of Greedy: items are indivisible
    static int knapsack(int[] weights, int[] values, int capacity, List<Integer> selected) {
        int n = weights.length;
        int[][] dp = new int[n + 1][capacity + 1];

        for (int i = 1; i <= n; i++) {
            for (int w = 0; w <= capacity; w++) {
                dp[i][w] = dp[i - 1][w];
                if (weights[i - 1] <= w) {
                    dp[i][w] = Math.max(dp[i][w], dp[i - 1][w - weights[i - 1]] + values[i - 1]);
                }
            }
        }

        // Backtrack through the DP table to find which packages were selected
        int w = capacity;
        for (int i = n; i >= 1; i--) {
            if (dp[i][w] != dp[i - 1][w]) {
                selected.add(i - 1);
                w -= weights[i - 1];
            }
        }
        Collections.reverse(selected);
        return dp[n][capacity];
    }

    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        // ---- Build the delivery-hub graph: 0=Depot, 1=HubB, 2=HubC, 3=HubD, 4=HubE ----
        addEdge(0, 1, 4);
        addEdge(0, 2, 1);
        addEdge(1, 2, 2);
        addEdge(1, 3, 1);
        addEdge(2, 3, 5);
        addEdge(3, 4, 3);

        String[] hubNames = {"Depot", "HubB", "HubC", "HubD", "HubE"};
        int[] dist = dijkstra(0, hubNames.length);

        System.out.println("Graph (Non-Linear DS) \u2014 Shortest Distances from Depot (Dijkstra, Greedy):");
        StringBuilder distLine = new StringBuilder();
        for (int i = 0; i < hubNames.length; i++) {
            distLine.append(hubNames[i]).append("=").append(dist[i]).append("km");
            if (i != hubNames.length - 1) distLine.append("  ");
        }
        System.out.println(distLine);
        System.out.println();

        // ---- 0/1 Knapsack: truck payload optimization ----
        String[] packageNames = {"P1", "P2", "P3", "P4"};
        int[] weights = {12, 7, 5, 3};
        int[] values = {60, 50, 30, 20};
        int capacity = 15;

        List<Integer> selected = new ArrayList<>();
        int maxValue = knapsack(weights, values, capacity, selected);

        System.out.println("0/1 Knapsack (DP) \u2014 Truck Capacity = " + capacity + " kg:");
        StringBuilder available = new StringBuilder("Packages available: ");
        for (int i = 0; i < packageNames.length; i++) {
            available.append(packageNames[i]).append("(").append(weights[i]).append("kg,").append(values[i]).append(")");
            if (i != packageNames.length - 1) available.append(" ");
        }
        System.out.println(available);

        StringBuilder chosen = new StringBuilder();
        int totalWeight = 0;
        for (int idx : selected) {
            if (chosen.length() > 0) chosen.append(" + ");
            chosen.append(packageNames[idx]);
            totalWeight += weights[idx];
        }
        System.out.println("Packages selected : " + chosen + "  (weight = " + totalWeight + " kg)");
        System.out.println();
        System.out.println("Maximum Delivery Value: " + maxValue);
    }
}