package com.cpuscheduler;
import java.util.*;
//               input example
// List<FCAIProcess> processes = new ArrayList<>(Arrays.asList(
//             new FCAIProcess("P1", "R",17, 0, 4, 4),
//             new FCAIProcess("P2", "R",6, 3, 9, 3),
//             new FCAIProcess("P3", "R",10, 4, 3, 5),
//             new FCAIProcess("P4", "R",4, 29, 8, 2)
//         ));

public class FCAIScheduler {
    private int startTime, curTime, executionTime;
    private final List<FCAIProcess> processes;
    private final List<FCAIProcess> completedProcesses = new ArrayList<>();

    private FCAIProcess tempP = null;
    private double V1, V2;
    private final PriorityQueue<FCAIProcess> readyQueue = new PriorityQueue<>(
            Comparator.comparingDouble(FCAIProcess::getFCAIFactor)
                    .thenComparing(Comparator.comparingInt(FCAIProcess::getPriority).reversed())
    );

    public FCAIScheduler(List<FCAIProcess> processes) {
        this.processes = processes;
        startTime = 0; curTime = 0; executionTime = 0;
        calculateV1V2();
        updateFCAIFactor();
    }

    public void updateReadyQueue() {
        // Add processes that have arrived to the queue
        for (FCAIProcess p : new ArrayList<>(processes)) {
            if (p.getArrivalTime() <= curTime) {
                readyQueue.add(p);
                processes.remove(p);
            }
        }
    }
    public void displayExecutedProcess(FCAIProcess p, FCAIProcess prvP, boolean preempted) {
        boolean noRBT = p.isCompleted;
        if (noRBT) {
            p.setCompletionTime(curTime);
            completedProcesses.add(p);
        }
        // Print the process details
        System.out.printf("%-6s %-7s %-14d %-21d %-15s %-10d %-12s", startTime + "-" + curTime, p.getName(),
                executionTime, p.getRemainingBurstTime(), noRBT ? "Completed" : p.prvQuantum + "->" + p.getQuantum(),
                p.getPriority(), noRBT ? "Completed" : p.prvFCAIFactor + "->" + p.getFCAIFactor());

        // Print the action details directly below
        if (!preempted || prvP == null) {
            System.out.printf("%s starts execution, runs for %d units, remaining burst = %d\n\n",
                    p.getName(), executionTime, p.getRemainingBurstTime());
        } else {
            System.out.printf("%s preempts %s, starts execution, runs for %d units, remaining burst = %d\n\n",
                    p.getName(), prvP.getName(), executionTime, p.getRemainingBurstTime());
        }
    }

    public void schedule() {
        FCAIProcess curProcess, prvProcess = null, prvPrev = null;

        int quantum, tempQuantum = 0;

        System.out.printf("%-6s %-7s %-14s %-21s %-15s %-10s %-12s\n",
                "Time", "Process", "Executed Time", "Remaining Burst Time",
                "Updated Quantum", "Priority", "FCAI Factor");


        while (!processes.isEmpty() || !readyQueue.isEmpty() || tempP != null) {
            updateReadyQueue();
            if (readyQueue.isEmpty()) {
                if (tempP == null) {
                    curTime++;
                    continue;
                }
                else checkTemp();
            }

            curProcess = readyQueue.poll();
            if (curProcess == null) break;
            if (tempP != null && !tempP.isCompleted) {
                readyQueue.add(tempP);
                tempP = null;
            }

            // if the process still executed
            if (curProcess == prvProcess) {
                if (tempQuantum > 0 && !curProcess.isCompleted) {
                    prvProcess = prvPrev; curProcess.decrementRBT(1);
                    executionTime++; tempQuantum--; curTime++;
                }
                else {
                    finalizePreviousProcess(prvProcess, prvPrev, tempQuantum);
                    startTime = curTime;
                    prvProcess = null;
                    tempP = curProcess;
                    continue;
                }
            }
            else {
                if (prvProcess != null) {
                    finalizePreviousProcess(prvProcess, prvPrev, tempQuantum);
                }

                startTime = curTime;
                quantum = curProcess.getQuantum();
                tempQuantum = quantum;
                executionTime = Math.min((int) Math.ceil(0.4 * quantum), curProcess.getRemainingBurstTime());

                curTime += executionTime;
                curProcess.decrementRBT(executionTime);

                if (curProcess.getRemainingBurstTime() == 0) {
                    finalizePreviousProcess(curProcess, prvProcess, 0);
                    continue;  // Skip to the next process
                }

                tempQuantum -= executionTime;
            }


            readyQueue.add(curProcess);

            prvPrev = prvProcess;
            prvProcess = curProcess;
        }

        displayTimes();
    }
    public void checkTemp() {
        if (tempP != null && !tempP.isCompleted) {
            readyQueue.add(tempP);
            tempP = null;
        }
    }
    public void calculateV1V2() {
        // Find the maximum arrival time and maximum burst time among all processes
        int maxArrivalTime = 0;
        int maxRemainingBurstTime = 0;
        for (FCAIProcess process: processes) {
            if (process.getArrivalTime() > maxArrivalTime)
                maxArrivalTime = process.getArrivalTime();
            if (process.getRemainingBurstTime() > maxRemainingBurstTime)
                maxRemainingBurstTime = process.getRemainingBurstTime();
        }
        // Calculate V1 and V2 according to the formula
        V1 = (double) maxArrivalTime / 10;
        V2 = (double) maxRemainingBurstTime / 10;
    }
    public void updateFCAIFactor() {
        for (FCAIProcess process: processes)
            process.updateFCAIFactor(V1, V2);
    }
    public static void displayFCAIFactor(List<FCAIProcess> processes) {
        for (FCAIProcess process : processes)
            System.out.println("Process " + process.getName() + ": " + process.getFCAIFactor());
        System.out.println("______________________________________________________________________________________");
    }

    private void finalizePreviousProcess(FCAIProcess prvProcess, FCAIProcess prvPrev, int tempQuantum) {
        boolean isPreempted = tempQuantum > 0 || !prvProcess.isCompleted;

        prvProcess.prvFCAIFactor = prvProcess.getFCAIFactor();
        prvProcess.prvQuantum = prvProcess.getQuantum();
        prvProcess.updateQuantum(executionTime);
        prvProcess.updateFCAIFactor(V1, V2);
        displayExecutedProcess(prvProcess, prvPrev, isPreempted);
    }

    private void displayTimes() {
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;
        int processCount = completedProcesses.size();

        System.out.println("\nTime details for each process:");
        System.out.printf("%-10s %-15s %-15s %-15s %-15s\n",
                "Process", "Arrival Time", "Completion Time", "Turnaround Time", "Waiting Time");

        for (FCAIProcess process : completedProcesses) {
            // Calculate Turnaround Time
            int turnaroundTime = process.getCompletionTime() - process.getArrivalTime();
            process.setTurnaroundTime(turnaroundTime);

            // Calculate Waiting Time
            int waitingTime = turnaroundTime - process.getBurstTime();
            process.setWaitingTime(waitingTime);

            // Accumulate for averages
            totalWaitingTime += waitingTime;
            totalTurnaroundTime += turnaroundTime;

            // Display process details
            System.out.printf("%-10s %-15d %-15d %-15d %-15d\n",
                    process.getName(),
                    process.getArrivalTime(),
                    process.getCompletionTime(),
                    turnaroundTime,
                    waitingTime);
        }

        // Calculate and display average Waiting Time and Turnaround Time
        double avgWaitingTime = (double) totalWaitingTime / processCount;
        double avgTurnaroundTime = (double) totalTurnaroundTime / processCount;

        System.out.println("\nAverage Waiting Time: " + avgWaitingTime);
        System.out.println("Average Turnaround Time: " + avgTurnaroundTime);
    }

}