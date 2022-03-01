package ru.spbstu.telematics.java;
import java.util.ArrayList;

public class App {

    public static void main(String[] args)
    {
        ArrayList<Integer> actions = new ArrayList<>();
        int R = 2500;
        int t = 5;
        int delta_time = R/100;

        actions.add(-15 * delta_time);
        actions.add(20 * delta_time);
        actions.add(5 * delta_time);
        actions.add(-25 * delta_time);
        actions.add(123 * delta_time);

        Window window = new Window(actions, R, t);
        Open open = new Open(window);
        Close close = new Close(window);
        new Thread(open).start();
        new Thread(close).start();

    }
}
