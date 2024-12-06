package com.cpuscheduler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SJF {

    private final List<Process> processes;

    public SJF(List<Process> processes) {
        this.processes = processes;
    }

    public void schedule() {
        int currentTime = 0; // Tracks the current time
        int completed = 0;   // Tracks the number of completed processes
        int n = processes.size();

        while (completed < n) {
            Process nextProcess = null;

            // Find the shortest job process that has arrived and is not yet scheduled
            for (Process process : processes) {
                if (!process.isScheduled() && process.getArrivalTime() <= currentTime) {
                    if (nextProcess == null || process.getBurstTime() < nextProcess.getBurstTime()) {
                        nextProcess = process;
                    }
                }
            }

            if (nextProcess != null) {
                // Schedule the selected process
                nextProcess.setStartTime(currentTime);
                currentTime += nextProcess.getBurstTime();
                nextProcess.setCompletionTime(currentTime);
                nextProcess.setTurnaroundTime(currentTime - nextProcess.getArrivalTime());
                nextProcess.setWaitingTime(nextProcess.getTurnaroundTime() - nextProcess.getBurstTime());
                nextProcess.setScheduled(true);

                completed++;
            } else {
                // If no process is ready, advance the time
                currentTime++;
            }
        }
    }

    public void printSchedule() {
        System.out.println("\nScheduled Processes:");
        System.out.printf("%-10s%-10s%-10s%-10s%-10s%-15s%-10s%-10s\n",
                "Process", "Arrival", "Burst", "Priority", "Start", "Completion", "TAT", "WT");


        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;

        for (Process process : processes) {
            totalWaitingTime += process.getWaitingTime();
            totalTurnaroundTime += process.getTurnaroundTime();

            System.out.printf("%-10s%-10d%-10d%-10d%-10d%-15d%-10d%-10d\n",
                    process.getName(),
                    process.getArrivalTime(),
                    process.getBurstTime(),
                    process.getPriority(),
                    process.getStartTime(),
                    process.getCompletionTime(),
                    process.getTurnaroundTime(),
                    process.getWaitingTime()
            );

            totalWaitingTime += process.getWaitingTime();
            totalTurnaroundTime += process.getTurnaroundTime();
        }

        System.out.printf("\nAverage Waiting Time: %.2f\n", totalWaitingTime / processes.size());
        System.out.printf("Average Turnaround Time: %.2f\n", totalTurnaroundTime / processes.size());
    }
}
