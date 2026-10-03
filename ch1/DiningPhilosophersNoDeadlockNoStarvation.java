import java.util.concurrent.locks.ReentrantLock;

public class DiningPhilosophersNoDeadlockNoStarvation {
    static int N = 5;
    static ReentrantLock[] chopsticks = new ReentrantLock[N];

    // Main method must be public because JVM calls it outside the class to start
    // the program.
    public static void main(String[] args) {
        for (int i = 0; i < N; i++)
            chopsticks[i] = new ReentrantLock(true); // FAIRNESS PARAMETER TURNED ON
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
            if (i % 2 == 0) {
                // If philosopher id is EVEN, then first reach for the RIGHT chopstick
                right.lock();
                left.lock();
            } else {
                // If philosopher id is ODD, then first reach for the LEFT chopstick
                left.lock();
                right.lock();
            }
            System.out.println("Philosopher " + i + " eats...");
            sleep(); // eat

            left.unlock();
            right.unlock(); // Again, the order of unlocks doesn't matter
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
