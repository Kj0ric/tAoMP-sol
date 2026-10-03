import java.util.concurrent.locks.ReentrantLock;

public class DiningPhilosophers {
    static int N = 5;
    static ReentrantLock[] chopsticks = new ReentrantLock[N];

    // Main method must be public because JVM calls it outside the class to start
    // the program.
    public static void main(String[] args) {
        for (int i = 0; i < N; i++)
            chopsticks[i] = new ReentrantLock();
        for (int i = 0; i < N; i++) {
            int id = i;
            Thread th = new Thread(() -> philosopher(id));
            th.start();
        }
    }

    // Static means the method belongs to the class, not an object of the class.
    static void philosopher(int i) {
        // Compute the left and right chopstick the philosopher attempts to grap
        ReentrantLock left = chopsticks[i];
        ReentrantLock right = chopsticks[(i + 1) % N];
        while (true) {
            sleep(); // think

            left.lock();
            try { // immediately follow by a try_finally block
                sleep(); // to make it more probable to see the deadlock scenario
                right.lock();
                try {
                    System.out.println("Philosopher " + i + " eats...");
                    sleep(); // eat
                } finally {
                    right.unlock();
                }
            } finally {
                left.unlock(); // first line in finally is to unlock

            }
        }
    }

    static void sleep() {
        long sleep_amount = (long) (Math.random() * 100);
        try {
            Thread.sleep(sleep_amount);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
