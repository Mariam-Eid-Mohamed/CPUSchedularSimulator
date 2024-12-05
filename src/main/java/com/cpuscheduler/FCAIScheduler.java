package com.cpuscheduler;
import java.util.List;
import java.util.*;


// Not the final version
public class FCAIScheduler {
    private int startTime, curTime, executionTime;
    private final List<FCAIProcess> processes;
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
        // Remaining burst time if no then it mean the process has been completed
        boolean noRBT = p.isCompleted;


        System.out.print(startTime + "-" + curTime + "  " + p.getName() + "  " + executionTime + "  " +
                p.getRemainingBurstTime() + "  " + (noRBT ? "Completed" : p.prvQuantum + "->" + p.getQuantum()) + "  "
                + p.getPriority() + "  " + (noRBT ? "Completed" : p.prvFCAIFactor + "->" + p.getFCAIFactor()) + "  ");

        if (!preempted || prvP == null)
            System.out.print(p.getName() + " starts execution, runs for " + executionTime +
                    " units, remaining burst = " + p.getRemainingBurstTime());
        else
            System.out.print(p.getName() + " preempts " + prvP.getName() + " starts execution, runs for " +
                    executionTime + " units, remaining burst = " + p.getRemainingBurstTime());
        System.out.println("\n______________________________________________________________________________________");
    }
    public void schedule() {
        FCAIProcess curProcess, prvProcess = null, prvPrev = null;

        int quantum, tempQuantum = 0;

        System.out.println("Time Process - Executed Time - Remaining Burst Time - "
                + "Updated Quantum - Priority FCAI Factor"+ "  " + "Action Details");

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
                    prvProcess = prvPrev;
                    curProcess.decrementRBT(1);
                    executionTime++; tempQuantum--; curTime++;
                }
                else {
                    prvProcess.prvFCAIFactor = prvProcess.getFCAIFactor();
                    prvProcess.updateQuantum(executionTime);
                    prvProcess.updateFCAIFactor(V1, V2);
                    displayExecutedProcess(prvProcess, prvPrev, true);
                    startTime = curTime;
                    prvProcess = null;
                    tempP = curProcess;
                    continue;
                }
            }
            else {
                if (prvProcess != null) {
                    boolean isPreempted = tempQuantum > 0 || !prvProcess.isCompleted;
                    prvProcess.prvFCAIFactor = prvProcess.getFCAIFactor();
                    prvProcess.updateQuantum(executionTime);
                    prvProcess.updateFCAIFactor(V1, V2);
                    displayExecutedProcess(prvProcess, prvPrev, isPreempted);
                }
                startTime = curTime;
                quantum = curProcess.getQuantum();
                tempQuantum = quantum;
                executionTime = (int) Math.ceil(0.4 * quantum);

                // if the remaining burst time less than the amount 40% of quantum then we will execute the remaining burst time
                if (curProcess.getRemainingBurstTime() < executionTime) {
                    executionTime = curProcess.getRemainingBurstTime();
                    curTime += executionTime;
                    curProcess.decrementRBT(curProcess.getRemainingBurstTime());
                    displayExecutedProcess(curProcess, prvProcess, false);
                    continue;  // Continue to the next process
                }
                tempQuantum -= executionTime;
                curTime += executionTime;
                curProcess.decrementRBT(executionTime);
            }

            readyQueue.add(curProcess);

            prvPrev = prvProcess;
            prvProcess = curProcess;
        }
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

}
