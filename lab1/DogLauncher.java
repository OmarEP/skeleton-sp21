public class DogLauncher {
    public static void main(String[] args) {
        Dog d = new Dog(15);
        d.makeNoise();
        Dog d2 = new Dog(151);
        Dog bigger = Dog.maxDog(d, d2);
        bigger.makeNoise();
    }
}
