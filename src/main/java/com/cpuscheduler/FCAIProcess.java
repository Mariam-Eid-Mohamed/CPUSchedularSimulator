package com.cpuscheduler;

public class FCAIProcess extends Process {
    private int quantum;
    private double FCAIFactor;

    public FCAIProcess(String name, String color, int arrivalTime, int burstTime, int priority, int quantum) {
        super(name, color, arrivalTime, burstTime, priority); // Call to superclass constructor
        this.quantum = quantum;
        this.FCAIFactor = 0.0; // Initialize FCAIFactor to a default value
    }

    public int getQuantum() {return quantum;}
    public void setQuantum(int quantum) {this.quantum = quantum;}
    public double getFCAIFactor() {return FCAIFactor;}

    public void updateQuantum(int executedTime) {
        if (getRemainingBurstTime() > 0) {
            if (executedTime < quantum)
                quantum += quantum - executedTime;
            else
                quantum += 2;
        }
    }
    public void updateFCAIFactor(double V1, double V2) {
        FCAIFactor = (10 - getPriority()) + (getArrivalTime() / V1) + (getRemainingBurstTime() / V2);
    }
}
