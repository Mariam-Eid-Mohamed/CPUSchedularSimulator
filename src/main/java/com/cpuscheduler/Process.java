package com.cpuscheduler;
//Process class to store process data
public class Process {
    private final String name;
    private final String color;
    private final int arrivalTime;
    private final int burstTime;
    private final int priority;
    private int remainingBurstTime;
    private int completionTime;
    private int startTime;
    private int waitingTime;
    private int turnaroundTime;
    private boolean isScheduled = false;


    // Constructor, getters and setters
    public Process(String name, String color, int arrivalTime, int burstTime, int priority) {
        this.name = name;
        this.color = color;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.remainingBurstTime = burstTime;
    }

    public String getName() { return name; }
    public int getArrivalTime() { return arrivalTime; }
    public int getCompletionTime() { return completionTime; }
    public int getStartTime() { return startTime; }
    public int getBurstTime() { return burstTime; }
    public int getPriority() { return priority; }
    public int getRemainingBurstTime() { return remainingBurstTime; }
//for priority scheduler
    public void setCompletionTime(int completionTime) {
        this.completionTime = completionTime;
    }
    public void setStartTime(int startTime) {this.startTime = startTime; }

    public void setRemainingBurstTime(int remainingBurstTime) { this.remainingBurstTime = remainingBurstTime; }
    public void setWaitingTime(int waitingTime) { this.waitingTime = waitingTime; }
    public void setTurnaroundTime(int turnaroundTime) { this.turnaroundTime = turnaroundTime; }
    public int getWaitingTime() { return waitingTime; }
    public int getTurnaroundTime() { return turnaroundTime; }
    public String getColor() { return color; }
    public boolean isScheduled() { return isScheduled; }
    public void setScheduled(boolean scheduled) { isScheduled = scheduled; }
}
