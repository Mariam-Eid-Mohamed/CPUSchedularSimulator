package com.cpuscheduler;
import java.util.List;

public class FCAIScheduler {

    // schedule processes using the FCAI scheduling algorithm
    public void scheduleProcesses(List<Process> processList);

    // allow preemption after 40% execution of quantum
    public boolean allowPreemption(Process process);

    // testing the scheduler
    public void testScheduler(List<Process> processList);
}
