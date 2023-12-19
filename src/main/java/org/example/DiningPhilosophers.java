package org.example;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

public class DiningPhilosophers {
    private static final Lock[] forks = new ReentrantLock[5];

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            forks[i] = new ReentrantLock();
        }

        Philosopher[] philosophers = new Philosopher[5];
        Thread[] threads = new Thread[5];

        for (int i = 0; i < 5; i++) {
            philosophers[i] = new Philosopher(i);
            threads[i] = new Thread(philosophers[i]);
            threads[i].start();
        }

        try {
            for (int i = 0; i < 5; i++) {
                threads[i].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class Philosopher implements Runnable {
        private final int id;

        public Philosopher(int id) {
            this.id = id;
        }

        private void think() {
            System.out.println("Философ " + id + " размышляет.");
            try {
                sleep((long)(Math.random()*2000));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        private void eat() {
            System.out.println("Философ " + id + " ест.");
            try {
                sleep((long)(Math.random()*2000));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                think();
                pickUpForks();
                eat();
                putDownForks();
            }
        }

        private void pickUpForks() {
            int leftFork = id;
            int rightFork = (id + 1) % 5;

            forks[leftFork].lock();
            System.out.println("Философ " + id + " поднял левую вилку");
            forks[rightFork].lock();
            System.out.println("Философ " + id + " поднял правую вилку.");
        }

        private void putDownForks() {
            int leftFork = id;
            int rightFork = (id + 1) % 5;

            forks[leftFork].unlock();
            forks[rightFork].unlock();

            System.out.println("Философ " + id + " положил вилки.");
        }
    }
}
