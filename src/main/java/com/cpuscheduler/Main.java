package com.cpuscheduler;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Get user inputs for processes
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter number of processes: ");
        int numProcesses = sc.nextInt();

        // Create an array to store processes
        Process[] processes = new Process[numProcesses];

        // Collect information for each process
        for (int i = 0; i < numProcesses; i++) {
            System.out.println("Enter details for Process " + (i + 1));

            // Create a new Process object and gather attributes like name, burst time, arrival time, etc.
            sc.nextLine(); // Consume the newline character
            System.out.print("Process Name: ");
            String name = sc.nextLine();
            System.out.print("Process Color (Graphical Representation): ");
            String color = sc.nextLine();
            System.out.print("Arrival Time: ");
            int arrivalTime = sc.nextInt();
            System.out.print("Burst Time: ");
            int burstTime = sc.nextInt();
            System.out.print("Priority: ");
            int priority = sc.nextInt();

            processes[i] = new Process(name, color, arrivalTime, burstTime, priority);
        }

        // After collecting processes, call the scheduling algorithms
        // Example: Call Priority Scheduling
        PriorityScheduler scheduler = new PriorityScheduler();
//        scheduler.schedule(processes);
    }
}
