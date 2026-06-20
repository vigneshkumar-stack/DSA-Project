import java.util.*;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class MessagingApp {

    // ---------- Node used while building the Huffman Tree ----------
    // Each node carries the partially-built codes for every symbol merged into it so far.
    static class HuffNode {
        int freq;
        int order;                                  // insertion order, breaks ties like a stable heap
        List<Object[]> pairs;                        // {symbol(String), codeSoFar(StringBuilder)}

        HuffNode(int freq, int order, List<Object[]> pairs) {
            this.freq = freq;
            this.order = order;
            this.pairs = pairs;
        }
    }

    // ---------- 1. Huffman Coding: Greedy merging using a Min-Heap, builds a Binary Tree ----------
    // Time: O(n log n) | Greedy choice: always merge the two lowest-frequency nodes
    static Map<String, String> buildHuffmanCodes(String[] symbols, int[] freqs) {
        PriorityQueue<HuffNode> minHeap = new PriorityQueue<>(
            Comparator.<HuffNode>comparingInt(a -> a.freq).thenComparingInt(a -> a.order)
        );

        int order = 0;
        for (int i = 0; i < symbols.length; i++) {
            List<Object[]> pairs = new ArrayList<>();
            pairs.add(new Object[]{symbols[i], new StringBuilder()});
            minHeap.offer(new HuffNode(freqs[i], order++, pairs));
        }

        while (minHeap.size() > 1) {
            HuffNode n1 = minHeap.poll();             // two least-frequent nodes
            HuffNode n2 = minHeap.poll();

            for (Object[] p : n1.pairs) ((StringBuilder) p[1]).insert(0, '0');
            for (Object[] p : n2.pairs) ((StringBuilder) p[1]).insert(0, '1');

            List<Object[]> merged = new ArrayList<>(n1.pairs);
            merged.addAll(n2.pairs);                  // new internal node (Non-Linear Binary Tree)
            minHeap.offer(new HuffNode(n1.freq + n2.freq, order++, merged));
        }

        Map<String, String> codes = new LinkedHashMap<>();
        for (Object[] p : minHeap.poll().pairs) {
            codes.put((String) p[0], ((StringBuilder) p[1]).toString());
        }
        return codes;
    }

    // ---------- 2. Edit Distance (Dynamic Programming) ----------
    // Time: O(m*n) | DP is required: no greedy character-matching rule guarantees the minimum
    static int editDistance(String a, String b) {
        int m = a.length(), n = b.length();
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) dp[i][0] = i;
        for (int j = 0; j <= n; j++) dp[0][j] = j;

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j - 1],
                                  Math.min(dp[i - 1][j], dp[i][j - 1]));
                }
            }
        }
        return dp[m][n];
    }

    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        // ---- Huffman Coding ----
        String[] symbols = {"a", "b", "c", "d", "e", "f"};
        int[] freqs = {45, 13, 12, 16, 9, 5};

        Map<String, String> codes = buildHuffmanCodes(symbols, freqs);

        int totalBits = 0;
        Map<String, Integer> freqMap = new LinkedHashMap<>();
        for (int i = 0; i < symbols.length; i++) freqMap.put(symbols[i], freqs[i]);
        for (String s : codes.keySet()) totalBits += freqMap.get(s) * codes.get(s).length();

        int totalFreq = Arrays.stream(freqs).sum();
        int fixedLengthBits = 3 * totalFreq;          // 6 symbols need ceil(log2 6) = 3 bits each

        System.out.println("Huffman Coding (Greedy + Binary Tree):");

        StringBuilder freqLine = new StringBuilder("Symbol Frequencies : ");
        for (int i = 0; i < symbols.length; i++) {
            freqLine.append(symbols[i]).append("=").append(freqs[i]);
            if (i != symbols.length - 1) freqLine.append(" ");
        }
        System.out.println(freqLine);

        StringBuilder codeLine = new StringBuilder("Huffman Codes      : ");
        boolean first = true;
        for (Map.Entry<String, String> e : codes.entrySet()) {
            if (!first) codeLine.append(" ");
            codeLine.append(e.getKey()).append("=").append(e.getValue());
            first = false;
        }
        System.out.println(codeLine);

        System.out.println("Total Encoded Bits : " + totalBits + " bits   (Fixed-Length Encoding: " + fixedLengthBits + " bits)");
        System.out.println();

        // ---- Edit Distance Auto-Correct ----
        String typed = "kitten";
        String suggested = "sitting";
        int distance = editDistance(typed, suggested);

        System.out.println("Edit Distance (DP) \u2014 Auto-Correct:");
        System.out.println("Typed Word : " + typed);
        System.out.println("Suggested  : " + suggested);
        System.out.println("Minimum Edit Operations: " + distance);
    }
}