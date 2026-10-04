# Chapter 1 Exercises

## Exercise 1: Dining Philosophers with chopsticks

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

## Ex. 2: Safety or Liveness?

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


## Ex. 3: Myopic Alice and Bob

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

## Ex. 4: Poor prisoners

### Part 1: Switch is known to be initially off
Let one prisoner be the consumer, and the other P-1 prisoners the producers. Here's the protocol:

Every other prisoner:
1. If the switch is off and you have never turned it on, turn it on.
2. Otherwise, leave it unchanged.

The consumer:
1. If the switch is on, turn it off and increment its local counter by 1.
2. Otherwise, do nothing.
2. When the count = P-1, declare to the warden that everyone has visited the switch at least once.

**Note:** "For any N, everyone eventually visits at least N times" means everyone visits **infinitely often**: no prisoner can ever stop for good. So even if the counter makes many visits before anyone else does, they just find the switch off and wait; they are guaranteed to come back after the others start signaling.

### Part 2: Switch initial state unknown

**Problem:** If the switch starts on, the counter may count one **fake** signal. Ignoring it isn't safe either: it might be a real signal from a prisoner who visited before the counter.

**Idea:** Each non-counter signals **twice**, so one fake signal can't fool the counter.

Every other prisoner:
1. If the switch is off and you have turned it on **fewer than twice**,
   turn it on.
2. Otherwise, leave it unchanged.

The consumer
1. If the switch is on, turn it off and increment `count`.
2. When `count = 2(P − 1)`, declare that everyone has visited.

**Why `2(P − 1)`:** Check the worst case on each side of the target.
- *Safety* (claim false, count as high as possible): one prisoner
  missing, switch started on. Max count = `2(P − 2) + 1 = 2P − 3`,
  which is below the target, so no wrong declaration.
- *Liveness* (claim true, count as low as possible): switch started off.
  The counter still receives all `2(P − 1)` signals, so it reaches the
  target.

## Ex. 5: Poor prisoners with colorful hats

**Idea:** Prisoner 1 (at the back) sacrifices himself to announce the **parity of the blue hats** in front. Everyone else then knows every hat except their own, plus the parity information of blues (from hearing Prisoner 1), so they can work out their own hat.

**Agreed beforehand:** Prisoner 1 saying "red" means odd, "blue" means even. (vice versa is symmetric.)

Prisoner 1:
1. Count the blue hats on prisoners 2..P.
2. Say "red" if the count is odd, "blue" if it's even.

Prisoner n > 1:
1. Count the blue hats **seen** in front (prisoners n+1..P) plus the "blue" answers **heard** from prisoners 2..n−1.
2. If this count's parity matches the announced parity, say "red"; otherwise, say "blue".

Prisoners 2..n−1 answer correctly, so prisoner n knows every hat among 2..P except his own. A blue hat flips the parity and a red one doesn't, so the mismatch reveals his color. Thus, prisoners 2..P are always freed; prisoner 1 survives with probability 1/2.

```
Prisoner 1:
    if blues on 2..P is odd
        say "red"
    else
        say "blue"

Prisoner n > 1:
    announcedOdd = (prisoner 1 said "red")
    known = blues on n+1..P + "blue" answers from 2..n-1
    if (known is odd) == announcedOdd
        say "red"
    else
        say "blue"
```

## Ex. 6: Optimal number of multiprocessors

Operation costs depend on N. First compute the operation costs for the sequential and parallel part. Then compute the execution time with N multiprocessor using the formula `t_N = sequential_cost + parallel_cost / N`.

Average cost per operation (80% non-memory ops with cost 1, 20% memory ops):
- Sequential part: `0.8 x 1 unit + 0.2 x 14 unit = 3.6 unit`
- Parallel part:  `0.8 x 1 unit + 0.2 x (N/(N+10) x (3N + 11) unit + 10/(N+10) x 1 unit) unit`

Total time:
`t_N = 0.15 x sequential_cost/operation + (0.85/N) x parallel_cost/operation`

Minimize t_N with `dT/dN = 0` which gives **N = 10**.
