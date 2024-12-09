package com.cpuscheduler;
import java.util.*;

public class SRTFScheduler {
    public void schedule(Process[] processes) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter context switching if any: ");
        int contextSwitching = sc.nextInt();
        // Sort processes by arrival time initially
        Arrays.sort(processes, Comparator.comparingInt(Process::getArrivalTime));

        // Priority Queue to manage the ready queue (shortest remaining time first)
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(Comparator
                .comparingInt(Process::getRemainingBurstTime)
                .thenComparingInt(Process::getArrivalTime));

        ArrayList<Process> scheduledProcesses = new ArrayList<>();
        List<String> contextSwitchLog = new ArrayList<>();

        int currentTime = 0;
        int completedProcesses = 0;

        int previousTime = 0;
        Process previousProcess = null;

        while (completedProcesses < processes.length) {
            // Add all processes that have arrived at the current time to the ready queue
            for (Process process : processes) {
                if (!process.isScheduled() && process.getArrivalTime() <= currentTime) {
                    readyQueue.add(process);
                    process.setScheduled(true); // Mark as added to the ready queue
                }
            }

            // If the ready queue is empty, increase the current time (idle state)
            if (readyQueue.isEmpty()) {
                currentTime++;
                continue;
            }

            // Poll the process with the shortest remaining time
            Process currentProcess = readyQueue.poll();

            if(previousProcess != null && previousProcess != currentProcess){
                contextSwitchLog.add("Process: " + previousProcess.getName() + " started at time " + previousTime);
                contextSwitchLog.add("Process: " + previousProcess.getName() + " was switched at time " + currentTime);
                previousTime = currentTime;
            }

            if (currentProcess.getStartTime() == -1) {
                currentProcess.setStartTime(currentTime);
            }

            // Execute the process for one time unit
            currentProcess.setRemainingBurstTime(currentProcess.getRemainingBurstTime() - 1);
            currentTime++;

            // If the process completes execution
            if (currentProcess.getRemainingBurstTime() == 0) {
                currentProcess.setCompletionTime(currentTime);
                currentProcess.setTurnaroundTime((currentProcess.getCompletionTime() + contextSwitching) - currentProcess.getArrivalTime());
                currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());
                currentTime += contextSwitching;
                scheduledProcesses.add(currentProcess);
                completedProcesses++;

                contextSwitchLog.add("Process: " + currentProcess.getName() + " started at time " + previousTime);
                contextSwitchLog.add("Process: " + currentProcess.getName() + " finished at time " + currentTime);
                previousTime = currentTime;
                currentProcess = null;
            } else {
                // Re-add the process to the ready queue if it's not finished
                readyQueue.add(currentProcess);
            }

            previousProcess = currentProcess;
        }

        displayContextSwitchLog(contextSwitchLog);

        // Display results
        displayResults(scheduledProcesses);
    }

    private void displayResults(ArrayList<Process> scheduledProcesses) {
        System.out.println("\nScheduled Processes:");
        System.out.printf("%-10s%-10s%-10s%-7s%-13s%-10s%-10s\n",
                "Process", "Arrival", "Burst", "Start", "Completion", "TAT", "WT");

        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;

        for (Process process : scheduledProcesses) {
            System.out.printf("%-10s%-10d%-10d%-10d%-10d%-10d%-10d\n",
                    process.getName(), process.getArrivalTime(), process.getBurstTime(),
                    process.getStartTime(), process.getCompletionTime(),
                    process.getTurnaroundTime(), process.getWaitingTime());

            totalWaitingTime += process.getWaitingTime();
            totalTurnaroundTime += process.getTurnaroundTime();
        }

        double averageWaitingTime = (double) totalWaitingTime / scheduledProcesses.size();
        double averageTurnaroundTime = (double) totalTurnaroundTime / scheduledProcesses.size();

        System.out.printf("\nAverage Waiting Time: %.2f\n", averageWaitingTime);
        System.out.printf("Average Turnaround Time: %.2f\n", averageTurnaroundTime);
    }

    private void displayContextSwitchLog(List<String> contextSwitchLog) {
        System.out.println("\nContext Switch Log:");
        for (String logState : contextSwitchLog) {
            System.out.println(logState);
        }
    }
}
