public class TestSort {

    // Test the Sort.sort method.
    public static void testSort() {
        String[] input = {"I", "have", "an", "egg."};
        String[] expected = {"an", "egg", "have", "i"};

        Sort.sort(input);
    }

    public static void main(String[] args) {
        testSort();
    }
}
