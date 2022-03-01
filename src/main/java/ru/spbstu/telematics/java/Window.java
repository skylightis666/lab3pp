package ru.spbstu.telematics.java;

import java.util.ArrayList;

public class Window
{
    private boolean fullOpen;
    private boolean fullClose;
    private int percent;
    private int timePassed;
    private int holdButtonTime;
    private final int deltaTime;
    private final int t;

    public ArrayList<Integer> actions;


    Window(ArrayList<Integer> actions, int R, int t)
    {
        fullClose = true;
        fullOpen = false;
        percent = 0;
        timePassed = 0;
        deltaTime = R/100;
        holdButtonTime = 0;
        this.actions = actions;
        this.t = t;
    }

    void addOpenedPercent()
    {
        if (!fullOpen)
        {
            timePassed += deltaTime;
            percent += 1;
            checkWindowStatus();
            System.out.println(percent + " открывается");
        } else
        {
            System.out.println("Ошибка! Это окно уже открыто!");
        }
    }

    void subOpenedPercent()
    {
        if (!fullClose)
        {
            timePassed += deltaTime;
            percent -= 1;
            checkWindowStatus();
            String str;
            System.out.println(percent + " закрывается");
        } else
        {
            System.out.println("Ошибка! Это окно уже закрыто!");
        }
    }

    void checkWindowStatus()
    {
        if (percent == 0)
        {
            fullClose = true;
            fullOpen = false;
        } else if (percent == 100)
        {
            fullClose = false;
            fullOpen = true;
        } else
        {
            fullClose = false;
            fullOpen = false;
        }
    }

    synchronized void startClose()
    {
        try
        {
            while (actions.size() != 0 && actions.get(0) < 0)
                wait();
            if (actions.size() != 0)
            {
                holdButtonTime = actions.get(0);
                actions.remove(0);
                if (holdButtonTime < t * deltaTime)
                    holdButtonTime = 100 * deltaTime;
                timePassed = 0;

                System.out.println("Start close");
                while (timePassed < holdButtonTime)
                {
                    if (fullClose)
                    {
                        System.out.println("Окно полностью закрыто");
                        break;
                    }
                    try
                    {
                        Thread.sleep(deltaTime);
                        subOpenedPercent();
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

    synchronized void startOpen()
    {
        try
        {
            while (actions.size() != 0 && actions.get(0) > 0)
                wait();
            if (actions.size() != 0)
            {
                holdButtonTime = actions.get(0) * -1;
                actions.remove(0);
                if (holdButtonTime < t * deltaTime)
                    holdButtonTime = 100 * deltaTime;
                timePassed = 0;

                System.out.println("Start open");
                while (timePassed < holdButtonTime)
                {
                    if (fullOpen)
                    {
                        System.out.println("Окно полностью открыто");
                        break;
                    }
                    try
                    {
                        Thread.sleep(deltaTime);
                        addOpenedPercent();
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
