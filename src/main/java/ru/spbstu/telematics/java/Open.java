package ru.spbstu.telematics.java;

public class Open implements Runnable
{
    private Window window;

    Open(Window window)
    {
        this.window = window;
    }

    public void run()
    {
        while (window.actions.size() != 0)
        {
            window.start_open();
        }
    }
}
