# Ex. 1.
## Part 1
We need to ensure a safety property: mutual exclusion of the chopsticks. 
The idea is simple. Each philosopher first attempts to grab the chopstick on their left, if successful then on their right. I
_Why correct_: A chopstick is held only by the thread that acquired its lock, so no two philosophers hold the same chopstick. 
_Limitation_: The problem is that deadlock is possible. If all philosophers pick up their left chopsticks at the same time, each waits forever for the right chopsticks, causing circular wait.
_Implementation_: [DiningPhilosophers.java](DiningPhilosophers.java).
## Part 2
Now we also need to ensure a liveness property: deadlock-freedom.
Deadlock was caused by a circular-wait. In order to break the wait being circular, we force some philosophers to first reach for their right, others to first reach for their left chopstick.
_Why correct_: Philosophers reach for the same chopstick, resulting in one of them waiting without holding any chopstick. Thus, that philosopher waiting does not cause circular-wait.
_Limitation_: A slow philosopher with her luck not on her side, may starve.
_Implementation_: [DiningPhilosophersNoDeadlock](DiningPhilosophersNoDeadlock.java)
## Part 3
Now we also need to ensure a liveness property: starvation-freedom.
Starvation was caused by the fact that when a chopstick is released, any philosopher can grab it, even the philosopher who just released it.
The idea is to allow the lock to keep a queue of the thread waiting for the lock to be released. When the lock is free, only the thread who is the first in that queue can acquire it. The others must wait in the line. For our convenience, `ReentrantLock(true)` with its fairness argument `true` implements this efficiently.
_Implementation_: [DiningPhilosophersNoDeadlockNoStarvation](DiningPhilosophersNoDeadlockNoStarvation.java)

# Ex. 2
/| No. | Property type | Bad thing that never happens | Good thing that eventually happens |
| ------------- | -------------- | -------------- | --------------- |
| Item1 | Item1 | Item1 | Item 1 |

1. Safety
Bad thing that never happens: 
