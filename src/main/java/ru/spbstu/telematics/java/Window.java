package ru.spbstu.telematics.java;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Window
{
    private boolean full_open;
    private boolean full_close;
    private int percent;
    private int time_passed;
    private int hold_button_time;
    private int delta_time;
    private int t;

    public ArrayList<Integer> actions;


    Window(ArrayList<Integer> actions, int R, int t)
    {
        full_close = true;
        full_open = false;
        percent = 0;
        time_passed = 0;
        delta_time = R/100;
        hold_button_time = 0;
        this.actions = actions;
        this.t = t;
    }

    void AddOpened_percent()
    {
        if (!full_open)
        {
            time_passed += delta_time;
            percent += 1;
            CheckWindowStatus();
            System.out.println(percent + " открывается");
        } else
        {
            System.out.println("Ошибка! Это окно уже открыто!");
        }
    }

    void SubOpened_percent()
    {
        if (!full_close)
        {
            time_passed += delta_time;
            percent -= 1;
            CheckWindowStatus();
            String str;
            System.out.println(percent + " закрывается");
        } else
        {
            System.out.println("Ошибка! Это окно уже закрыто!");
        }
    }

    void CheckWindowStatus()
    {
        if (percent == 0)
        {
            full_close = true;
            full_open = false;
        } else if (percent == 100)
        {
            full_close = false;
            full_open = true;
        } else
        {
            full_close = false;
            full_open = false;
        }
    }

    synchronized void start_close()
    {
        try
        {
            while (actions.size() != 0 && actions.get(0) < 0)
                wait();
            if (actions.size() != 0)
            {
                hold_button_time = actions.get(0);
                actions.remove(0);
                if (hold_button_time < t * delta_time)
                    hold_button_time = 100 * delta_time;
                time_passed = 0;

                System.out.println("Start close");
                while (time_passed < hold_button_time)
                {
                    if (full_close)
                    {
                        System.out.println("Окно полностью закрыто");
                        break;
                    }
                    try
                    {
                        Thread.sleep(delta_time);
                        SubOpened_percent();
                    } catch (InterruptedException e)
                    {
                        System.out.println("Поток был прерван");
                    }
                }
                System.out.println("End close");
            }
            notify();
        } catch (InterruptedException e)
        {
            System.out.println(e.getMessage());
        }
    }

    synchronized void start_open()
    {
        try
        {
            while (actions.size() != 0 && actions.get(0) > 0)
                wait();
            if (actions.size() != 0)
            {
                hold_button_time = actions.get(0) * -1;
                actions.remove(0);
                if (hold_button_time < t * delta_time)
                    hold_button_time = 100 * delta_time;
                time_passed = 0;

                System.out.println("Start open");
                while (time_passed < hold_button_time)
                {
                    if (full_open)
                    {
                        System.out.println("Окно полностью открыто");
                        break;
                    }
                    try
                    {
                        Thread.sleep(delta_time);
                        AddOpened_percent();
                    } catch (InterruptedException e)
                    {
                        System.out.println("Поток был прерван");
                    }
                }
                System.out.println("End open");
            }
            notify();
        } catch (InterruptedException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
