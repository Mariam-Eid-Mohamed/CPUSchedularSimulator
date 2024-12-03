package com.cpuscheduler;
import java.util.List;

public class FCAIScheduler {
    // Initialize Processes
    private final List<FCAIProcess> processes;
    private double V1; // Last arrival time / 10
    private double V2; // Max burst time / 10

    public FCAIScheduler(List<FCAIProcess> processes) {
        this.processes = processes;
        calculateV1V2();
    }
    public void schedule() {
        int currentTime = 0;
        PriorityQueue<FCAIProcess> readyQueue = new PriorityQueue<>(Comparator.comparingDouble(FCAIProcess::getFCAIFactor));

        for (FCAIProcess process : processes) {
            process.updateFCAIFactor(V1, V2);
        }

        while (!processes.isEmpty() || !readyQueue.isEmpty()) {
            // Add processes that have arrived to the queue
            Iterator<FCAIProcess> iterator = processes.iterator();
            while (iterator.hasNext()) {
                FCAIProcess process = iterator.next();
                if (process.getArrivalTime() <= currentTime) {
                    readyQueue.add(process);
                    iterator.remove();
                }
            }

            if (readyQueue.isEmpty()) {
                currentTime++; 
                continue;
            }

            FCAIProcess currentProcess = readyQueue.poll();
            int quantum = currentProcess.getQuantum();
            int executionTime = (int) Math.ceil(0.4 * quantum);
            // if the remaining burst time less than the amount 40% of quantum then we will execute the remaining burst time
            if (currentProcess.getRemainingBurstTime() < executionTime)
                executionTime = currentProcess.getRemainingBurstTime();
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
    public static void updateFCAIFactor(double V1, double V2, List<FCAIProcess> processes) {
        for (FCAIProcess process: processes)
            process.updateFCAIFactor(V1, V2);
    }
    public static void displayFCAIFactor(List<FCAIProcess> processes) {
        for (FCAIProcess process : processes)
            System.out.println("Process " + process.getName() + ": " + process.getFCAIFactor());
        System.out.println("______________________________________________________________________________________");
    }
}
