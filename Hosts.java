public class Hosts extends Person {
    public Hosts(String firstName, String lastName) {
        super(firstName, lastName);
    }
    public void randomizeNum() {
        Numbers numbers = new Numbers();
        numbers.generateNumber();
    }
}
