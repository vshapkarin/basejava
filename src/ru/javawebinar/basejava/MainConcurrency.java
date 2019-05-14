package ru.javawebinar.basejava;

public class MainConcurrency {
    private static int staticId;
    private int id;

    public MainConcurrency() {
        id = ++staticId;
    }

    public static void main(String[] args) {
        MainConcurrency mainConcurrency1 = new MainConcurrency();
        MainConcurrency mainConcurrency2 = new MainConcurrency();

        Thread thread0 = new Thread(() -> test(mainConcurrency1, mainConcurrency2));
        Thread thread1 = new Thread(() -> test(mainConcurrency2, mainConcurrency1));
        thread0.start();
        thread1.start();
    }

    private static void test(MainConcurrency a, MainConcurrency b) {
        System.out.println(Thread.currentThread().getName() + " is trying to lock " + a);
        synchronized (a) {
            try {
                System.out.println("Locked!");
                Thread.sleep(10);
                System.out.println(Thread.currentThread().getName() + " is trying to lock " + b);
                synchronized (b) {
                    System.out.println("Locked!");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        return "MainConcurrency with id " + id;
    }
}
