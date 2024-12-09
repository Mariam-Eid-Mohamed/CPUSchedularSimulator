package com.cpuscheduler;

public class FCAIProcess extends Process {
    private int quantum;
    private int FCAIFactor;
    public int prvFCAIFactor;
    public int prvQuantum;
    public boolean isCompleted = false;
    public FCAIProcess(String name, String color,int burstTime, int arrivalTime, int priority, int quantum) {
        super(name, color,arrivalTime, burstTime, priority); // Call to superclass constructor
        this.quantum = quantum;
        this.FCAIFactor = 0; // Initialize FCAIFactor to a default value
    }

    public int getQuantum() {return quantum;}
    public int getFCAIFactor() {return FCAIFactor;}
    public void updateQuantum(int executedTime) {
        if (executedTime == quantum && getRemainingBurstTime() > 0)
            quantum += 2;
        else
            quantum += (quantum - executedTime);
    }
    public void updateFCAIFactor(double V1, double V2) {
        FCAIFactor = (int) Math.ceil((10 - getPriority()) + ((double) getArrivalTime() / V1) + ((double) getRemainingBurstTime() / V2));
    }

    public void decrementRBT(int val) {
        setRemainingBurstTime((getRemainingBurstTime() - val));
        isCompleted = getRemainingBurstTime() <= 0;
    }

}