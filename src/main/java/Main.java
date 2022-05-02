import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner keyBoard = new Scanner(System.in);
        UserInterface.startUp(keyBoard);

        keyBoard.close();
    }
}