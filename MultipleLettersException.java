public class MultipleLettersException extends Exception {
    public MultipleLettersException(String s) {
    }

    @Override
    public String getMessage() {
        return "More than one letter was entered";
    }
}
