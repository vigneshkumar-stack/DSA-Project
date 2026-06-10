import java.util.*;

public class WikipediaBFS {

    static void addEdge(Map<String, List<String>> graph,
                        String u, String v) {

        graph.putIfAbsent(u, new ArrayList<>());
        graph.putIfAbsent(v, new ArrayList<>());

        graph.get(u).add(v);
        graph.get(v).add(u);
    }

    public static void main(String[] args) {

        Map<String, List<String>> graph = new HashMap<>();

        addEdge(graph, "Cricket", "India");
        addEdge(graph, "Cricket", "Sachin");

        addEdge(graph, "India", "Mumbai");
        addEdge(graph, "India", "Tendulkar");

        addEdge(graph, "Mumbai", "Wankhede");
        addEdge(graph, "Mumbai", "Bandra");
        addEdge(graph, "Mumbai", "MumbaiCity");

        addEdge(graph, "Sachin", "Tendulkar");
        addEdge(graph, "Sachin", "Wankhede");

        addEdge(graph, "Tendulkar", "Wankhede");

        addEdge(graph, "Bandra", "MumbaiCity");

        String source = "Cricket";

        Queue<String> queue = new LinkedList<>();
        Map<String, Integer> distance = new HashMap<>();

        queue.add(source);
        distance.put(source, 0);

        while (!queue.isEmpty()) {

            String current = queue.poll();

            for (String neighbor : graph.get(current)) {

                if (!distance.containsKey(neighbor)) {

                    distance.put(neighbor,
                            distance.get(current) + 1);

                    queue.add(neighbor);
                }
            }
        }

        System.out.println("Starting Article: " + source);
        System.out.println();

        for (String article : distance.keySet()) {

            System.out.println(article +
                    " -> Distance " +
                    distance.get(article));
        }
    }
}