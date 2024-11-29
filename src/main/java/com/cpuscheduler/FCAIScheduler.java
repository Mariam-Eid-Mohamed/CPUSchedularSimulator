package com.cpuscheduler;
import java.util.List;

public class FCAIScheduler {

    // calculate V1 and V2 from the list of processes
    public void calculateFactors(List<Process> processList);

    // calculate FCAI Factor for a given process
    public int calculateFCAIFactor(Process process, double V1, double V2);

    // update quantum dynamically after a process is preempted or completes
    public void updateQuantum(Process process, boolean isPreempted, int unusedQuantum);

    // schedule processes using the FCAI scheduling algorithm
    public void scheduleProcesses(List<Process> processList);

    // allow preemption after 40% execution of quantum
    public boolean allowPreemption(Process process);

    // testing the scheduler
    public void testScheduler(List<Process> processList);
}
