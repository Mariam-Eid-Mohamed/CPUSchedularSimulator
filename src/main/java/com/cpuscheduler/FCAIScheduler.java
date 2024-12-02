package com.cpuscheduler;
import java.util.List;

public class FCAIScheduler {

    
    public double[] calculateV1V2(List<FCAIProcess> processes) {
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
        double V1 = (double) maxArrivalTime / 10;
        double V2 = (double) maxRemainingBurstTime / 10;

        // Return both values as an array
        return new double[]{V1, V2};
    }

    public static void updateFCAIFactor(double V1, double V2, List<FCAIProcess> processes) {
        // set V1 & V2 and then updating the FCAI factor
        for (FCAIProcess process: processes) {
            process.setV1(V1);
            process.setV2(V2);
            process.updateFCAIFactor();
        }
    }

    public static void displayFCAIFactor(List<FCAIProcess> processes) {
        for (FCAIProcess process : processes) {
            System.out.println("Process " + process.getName() + ": " + process.getFCAIFactor());
        }
        System.out.println("______________________________________________________________________________________");
    }
    // schedule processes using the FCAI scheduling algorithm
    public void scheduleProcesses(List<Process> processList);

    // allow preemption after 40% execution of quantum
    public boolean allowPreemption(Process process);

    // testing the scheduler
    public void testScheduler(List<Process> processList);
}
