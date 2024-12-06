package com.cpuscheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Get user inputs for processes
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter number of processes: ");
        int numProcesses = sc.nextInt();

        // Create a list to store processes
        List<Process> processes = new ArrayList<>();

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

            processes.add(new Process(name, color, arrivalTime, burstTime, priority));
        }
        System.out.println("Choose a scheduling algorithm:");
        System.out.println("1. Priority Scheduling");
        System.out.println("2. Non-Preemptive Shortest Job First (SJF)");

        int choice = sc.nextInt();

        switch (choice) {
            case 1:
                PriorityScheduler priorityScheduler = new PriorityScheduler();
                priorityScheduler.schedule(processes.toArray(new Process[0])); // Adjust based on your PriorityScheduler method signature
                break;

            case 2:
                SJF sjfScheduler = new SJF(processes);
                sjfScheduler.schedule();
                sjfScheduler.printSchedule();
                break;

            default:
                System.out.println("Invalid choice!");
                break;
        }

        sc.close();
    }
}
