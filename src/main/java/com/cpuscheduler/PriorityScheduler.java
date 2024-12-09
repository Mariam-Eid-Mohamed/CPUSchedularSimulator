package com.cpuscheduler;
import java.util.*;

// Priority Scheduling Algorithm
//steps of implementation
//1.Sort processes by arrival time initially (to ensure processes are checked in arrival order).
//2.Use a ready queue to keep track of processes that have arrived but are waiting to execute.
//3.At each unit of time:
//  Check for processes that have arrived.
//  Select the process with the highest priority from the ready queue.
//   Execute the process to completion (non-preemptive).
//4.Repeat until all processes are executed.
public class PriorityScheduler {
    public ArrayList<Process>RESULT = new ArrayList<>();

    public void schedule (Process[] processes){

        boolean isFirstProcess = true; // Add this variable at the start of the method
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter context switching if any: ");
        int contextSwitching = sc.nextInt();
        // Sort processes by arrival time initially
        Arrays.sort(processes, Comparator.comparingInt(Process::getArrivalTime));
        // Priority Queue to simulate the ready queue (sorted by priority and arrival time)
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(Comparator
                .comparingInt(Process::getPriority)
                .thenComparingInt(Process::getArrivalTime));
        ArrayList<Process>scheduledProcesses = new ArrayList<>();
        int currentTime = 0;
        int CompletedProcess = 0;
        while(CompletedProcess <processes.length){
            for (Process process : processes) {
                if (!process.isScheduled() && process.getArrivalTime() <= currentTime) {
                    readyQueue.add(process);
                    process.setScheduled(true); // Mark the process as added to the ready queue
                }
            }
            if (readyQueue.isEmpty()) {
                currentTime++;
                continue;
            }
            //poll one with the highest priority
            Process currentProcess = readyQueue.poll();
            // Set start time and calculate completion time

            currentProcess.setStartTime(currentTime);
            currentTime += currentProcess.getBurstTime(); // Process execution time
            currentProcess.setCompletionTime(currentTime);
            // Calculate Turnaround Time (TAT) and Waiting Time (WT)
            currentProcess.setTurnaroundTime((currentProcess.getCompletionTime() + contextSwitching) - currentProcess.getArrivalTime());
            // Special case: Adjust WT calculation for the first handled process
            if (isFirstProcess) {
                currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - contextSwitching - currentProcess.getBurstTime());
                isFirstProcess = false; // Reset flag after handling the first process
            } else {
                currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());
            }
            currentTime += contextSwitching;
            scheduledProcesses.add(currentProcess);
            RESULT.add(currentProcess);
            CompletedProcess++;


        }

        // Display the results
        System.out.println("\nScheduled Processes:");
        System.out.printf("%-10s%-10s%-10s%-10s%-10s%-15s%-10s%-10s\n",
                "Process", "Arrival", "Burst", "Priority", "Start", "Completion", "TAT", "WT");

        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;

        for (Process process : scheduledProcesses) {
            System.out.printf("%-10s%-10d%-10d%-10d%-10d%-15d%-10d%-10d\n",
                    process.getName(), process.getArrivalTime(), process.getBurstTime(), process.getPriority(),
                    process.getStartTime(), process.getCompletionTime(), process.getTurnaroundTime(), process.getWaitingTime());

            // Accumulate TAT and WT
            totalWaitingTime += process.getWaitingTime();
            totalTurnaroundTime += process.getTurnaroundTime();
        }

// Calculate averages
        double averageWaitingTime = (double) totalWaitingTime / scheduledProcesses.size();
        double averageTurnaroundTime = (double) totalTurnaroundTime / scheduledProcesses.size();

        System.out.printf("\nAverage Waiting Time : %.2f\n", averageWaitingTime);
        System.out.printf("Average Turnaround Time : %.2f\n", averageTurnaroundTime);
       for (Process process : RESULT) {
           System.out.println(process.getName());
       }



    }}
