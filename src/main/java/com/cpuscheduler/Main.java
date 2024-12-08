package com.cpuscheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final List<Process> processes = new ArrayList<>();
    private static final List<FCAIProcess> FCAIProcesses = new ArrayList<>();

    public static void main(String[] args) {
    // Create a Scanner object to read user input
    Scanner sc = new Scanner(System.in);

    // Prompt the user to select a scheduling algorithm and store the choice
    int choice = getSchedulingChoice(sc);

    // Prompt the user to enter the number of processes and store the input
    int numProcesses = getNumberOfProcesses(sc);

    // Collect process details based on the user's choice
    // If the choice is 4 (FCAI Scheduling), pass 'true' to indicate quantum input is needed
    getProcessesInput(sc, numProcesses, choice == 4);

    // Handle the chosen scheduling algorithm
    // This method processes the logic for the selected scheduling type
    handleSchedulingChoice(choice);

    // Close the Scanner to release resources
    sc.close();
}


    private static int getNumberOfProcesses(Scanner sc) {
        System.out.println("Enter number of processes: ");
        return sc.nextInt();
    }

    private static int getSchedulingChoice(Scanner sc) {
        System.out.println("Choose a scheduling algorithm:");
        System.out.println("1. Priority Scheduling");
        System.out.println("2. Non-Preemptive Shortest Job First (SJF)");
        System.out.println("3. SRTF Scheduling");
        System.out.println("4. FCAI Scheduling");

        return sc.nextInt();
    }

    private static void getProcessesInput(Scanner sc, int numProcesses, boolean isFCAI) {
        sc.nextLine(); // Consume newline

        for (int i = 0; i < numProcesses; i++) {
            System.out.println("Enter details for Process " + (i + 1));

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


            if (isFCAI) {
                System.out.print("Enter quantum time: ");
                int quantum =  sc.nextInt();
                FCAIProcesses.add(new FCAIProcess(name, color, arrivalTime, burstTime, priority, quantum));
            } else {
                processes.add(new Process(name, color, arrivalTime, burstTime, priority));
            }

            sc.nextLine(); // Consume newline
        }
    }

    private static void handleSchedulingChoice(int choice) {
        switch (choice) {
            case 1:
                System.out.println("Priority Scheduling selected.");
                PriorityScheduler priorityScheduler = new PriorityScheduler();
                priorityScheduler.schedule(processes.toArray(new Process[0])); // Adjust based on your PriorityScheduler method signature
                break;
            case 2:
                System.out.println("Non-Preemptive Shortest Job First (SJF) selected.");
                SJF sjfScheduler = new SJF(processes);
                sjfScheduler.schedule();
                sjfScheduler.printSchedule();
                break;
            case 3:
                System.out.println("SRTF Scheduling selected.");
                SRTFScheduler srtfScheduler = new SRTFScheduler();
                srtfScheduler.schedule(processes.toArray(new Process[0]));
                break;
            case 4:
                System.out.println("FCAI Scheduling selected.");
                FCAIScheduler fcaiScheduler = new FCAIScheduler(FCAIProcesses);
                fcaiScheduler.schedule();
                break;
            default:
                System.out.println("Invalid choice!");
        }
    }
}
