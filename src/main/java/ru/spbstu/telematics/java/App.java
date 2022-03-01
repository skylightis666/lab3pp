package ru.spbstu.telematics.java;
import java.util.ArrayList;

public class App {

    public static void main(String[] args)
    {
        ArrayList<Integer> actions = new ArrayList<>();
        int R = 2500;
        int t = 5;
        int deltaTime = R/100;

        actions.add(-15 * deltaTime);
        actions.add(20 * deltaTime);
        actions.add(5 * deltaTime);
        actions.add(-25 * deltaTime);
        actions.add(123 * deltaTime);

        Window window = new Window(actions, R, t);
        Open open = new Open(window);
        Close close = new Close(window);
        new Thread(open).start();
        new Thread(close).start();

    }
}
