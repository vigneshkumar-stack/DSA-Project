
public class FlashSaleOrderDispatchSystem {

    // ---------- 1. Heap Sort (Price field — unbounded range) ----------
    // Time: O(n log n) | Space: O(1) | Stable: No

    static void heapify(int[] arr, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n && arr[left] > arr[largest]) {
            largest = left;
        }

        if (right < n && arr[right] > arr[largest]) {
            largest = right;
        }

        if (largest != i) {
            int temp = arr[i];
            arr[i] = arr[largest];
            arr[largest] = temp;

            heapify(arr, n, largest);
        }
    }

    static void heapSort(int[] arr) {
        int n = arr.length;

        // Build Max Heap
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i);
        }

        // Extract elements one by one
        for (int i = n - 1; i > 0; i--) {
            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;

            heapify(arr, i, 0);
        }
    }

    // ---------- 2. Counting Sort (Priority field — bounded range 1-10) ----------
    // Time: O(n + k) | Space: O(k) | Stable: Yes

    static int[] countingSort(int[] arr, int k) {
        int[] count = new int[k + 1];

        for (int val : arr) {
            count[val]++;
        }

        // Prefix Sum
        for (int i = 1; i <= k; i++) {
            count[i] += count[i - 1];
        }

        int[] output = new int[arr.length];

        // Stable placement
        for (int i = arr.length - 1; i >= 0; i--) {
            int val = arr[i];
            count[val]--;
            output[count[val]] = val;
        }

        return output;
    }

    // ---------- 3. Radix Sort (PIN-code field — fixed-width multi-digit) ----------
    // Time: O(d(n + k)) | Space: O(n + k) | Stable: Yes

    static void countingSortByDigit(int[] arr, int exp) {
        int n = arr.length;
        int[] output = new int[n];
        int[] count = new int[10];

        for (int num : arr) {
            int digit = (num / exp) % 10;
            count[digit]++;
        }

        for (int i = 1; i < 10; i++) {
            count[i] += count[i - 1];
        }

        for (int i = n - 1; i >= 0; i--) {
            int digit = (arr[i] / exp) % 10;
            count[digit]--;
            output[count[digit]] = arr[i];
        }

        System.arraycopy(output, 0, arr, 0, n);
    }

    static void radixSort(int[] arr) {
        int max = arr[0];

        for (int num : arr) {
            if (num > max) {
                max = num;
            }
        }

        for (int exp = 1; max / exp > 0; exp *= 10) {
            countingSortByDigit(arr, exp);
        }
    }

    // ---------- Main Method ----------

    public static void main(String[] args) {

        // Sample order records
        int[] prices = {4999, 899, 12499, 499, 7999, 1499, 3499, 2499};
        int[] priorities = {5, 9, 1, 7, 2, 10, 1, 3, 2};
        int[] pinCodes = {682001, 100456, 500001, 100089, 245112, 100023};

        heapSort(prices);

        int[] sortedPriorities = countingSort(priorities, 10);

        radixSort(pinCodes);

        System.out.println("Heap Sort (Price):");
        for (int price : prices) {
            System.out.print(price + " ");
        }

        System.out.println("\n");

        System.out.println("Counting Sort (Priority 1-10):");
        for (int priority : sortedPriorities) {
            System.out.print(priority + " ");
        }

        System.out.println("\n");

        System.out.println("Radix Sort (PIN Code):");
        for (int pin : pinCodes) {
            System.out.print(pin + " ");
        }

        System.out.println("\n");

        System.out.println(
                "Highest-priority order to dispatch first: Priority "
                        + sortedPriorities[0]);
    }
}