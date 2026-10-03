# Chapter 1 Exercises

## Exercise 1

### Part 1

We need to ensure a safety property: mutual exclusion of the chopsticks.
The idea is simple. Each philosopher first attempts to grab the chopstick on their left, if successful then on their right. I

_Why correct_: A chopstick is held only by the thread that acquired its lock, so no two philosophers hold the same chopstick.

_Limitation_: The problem is that deadlock is possible. If all philosophers pick up their left chopsticks at the same time, each waits forever for the right chopsticks, causing circular wait.

_Implementation_: [DiningPhilosophers.java](DiningPhilosophers.java).

### Part 2

Now we also need to ensure a liveness property: deadlock-freedom.
Deadlock was caused by a circular-wait. In order to break the wait being circular, we force some philosophers to first reach for their right, others to first reach for their left chopstick.

_Why correct_: Philosophers reach for the same chopstick, resulting in one of them waiting without holding any chopstick. Thus, that philosopher waiting does not cause circular-wait.

_Limitation_: A slow philosopher with her luck not on her side, may starve.

_Implementation_: [DiningPhilosophersNoDeadlock](DiningPhilosophersNoDeadlock.java)

### Part 3

Now we also need to ensure a liveness property: starvation-freedom.
Starvation was caused by the fact that when a chopstick is released, any philosopher can grab it, even the philosopher who just released it.

The idea is to allow the lock to keep a queue of the thread waiting for the lock to be released. When the lock is free, only the thread who is the first in that queue can acquire it. The others must wait in the line. For our convenience, `ReentrantLock(true)` with its fairness argument `true` implements this efficiently.

_Implementation_: [DiningPhilosophersNoDeadlockNoStarvation](DiningPhilosophersNoDeadlockNoStarvation.java)

## Ex. 2

| # |Type | the Bad thing that never happens / the Good thing eventually happens|
|:--|:---------|:---------------------------------------------------------------|
| 1 | Safety   | A patron is served before someone who arrived earlier.|
| 2 | Liveness | each thing that can go wrong eventually does.|
| 3 | Safety  | Someone wants to die. |
| 4 | Liveness | Death or tax happens eventually. |
| 5 | Safety| One borns and within t times no one dies. |
| 6 | Safety| No message printed within one second after an interrupt. |
| 7 | Liveness | A message is eventually printed. |
| 8 | Liveness | The work is eventually finished. |
| 9 | Safety | The cost of living decreases. |
| 10 | Safety | You meet a Harvard man, yet you can't tell he's from Harvard. |


## Ex. 3

Required properties:
- mutual exclusion: Bob and the pets are never in the yard simultaneously.
- producer-consumer: The pets don't go into the yard if no food, and Bob doesn't bring food if there's still food in the yard.
- starvation-freedom: If Bob is willing to feed and the pets are always hungry, the pets can eat infinitely often.

Two cans, one per windowsill. Each person watches only their own can; the other knocks it down with a string. A dropped can signals "your turn."

_Initially_: both cans up. Bob puts food in the yard, leaves, and drops Alice's can.

**Alice** (loop):
1. Wait until her can is down.
2. Release the pets.
3. Wait until the food is eaten and the pets are back inside.
4. Reset her can.
5. Drop Bob's can.

**Bob** (loop):
1. Wait until his can is down.
2. Put food in the yard and leave.
3. Reset his can.
4. Drop Alice's can.

**Order matters:** reset your own can *before* dropping the other's; otherwise the other's signal could be erased, and both wait forever.
