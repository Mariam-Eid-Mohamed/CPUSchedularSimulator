package com.cpuscheduler;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FCAIGUI extends JFrame {
    private JTextField nameField, colorField, arrivalField, burstField, priorityField, quantumField;
    private JTextArea outputArea;
    private JPanel ganttChartPanel;
    private List<FCAIProcess> processes = new ArrayList<>();

    public FCAIGUI() {
        setTitle("FCAI Scheduler");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(8, 2));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add Process"));

        inputPanel.add(new JLabel("Process Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Color:"));
        colorField = new JTextField();
        inputPanel.add(colorField);

        inputPanel.add(new JLabel("Arrival Time:"));
        arrivalField = new JTextField();
        inputPanel.add(arrivalField);

        inputPanel.add(new JLabel("Burst Time:"));
        burstField = new JTextField();
        inputPanel.add(burstField);

        inputPanel.add(new JLabel("Priority:"));
        priorityField = new JTextField();
        inputPanel.add(priorityField);

        inputPanel.add(new JLabel("Quantum:"));
        quantumField = new JTextField();
        inputPanel.add(quantumField);

        JButton addProcessButton = new JButton("Add Process");
        addProcessButton.addActionListener(e -> addProcess());
        inputPanel.add(addProcessButton);

        JButton runSchedulerButton = new JButton("Run Scheduler");
        runSchedulerButton.addActionListener(e -> runScheduler());
        inputPanel.add(runSchedulerButton);

        add(inputPanel, BorderLayout.NORTH);

        // Gantt Chart Panel
        ganttChartPanel = new JPanel();
        ganttChartPanel.setPreferredSize(new Dimension(800, 200));
        ganttChartPanel.setBorder(BorderFactory.createTitledBorder("Gantt Chart"));
        add(ganttChartPanel, BorderLayout.CENTER);

        // Output Panel
        outputArea = new JTextArea(15, 70);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Scheduler Output"));
        add(scrollPane, BorderLayout.SOUTH);
    }

    private void addProcess() {
        try {
            String name = nameField.getText();
            String color = colorField.getText();
            int arrivalTime = Integer.parseInt(arrivalField.getText());
            int burstTime = Integer.parseInt(burstField.getText());
            int priority = Integer.parseInt(priorityField.getText());
            int quantum = Integer.parseInt(quantumField.getText());

            FCAIProcess process = new FCAIProcess(name, color, burstTime, arrivalTime, priority, quantum);
            processes.add(process);
            outputArea.append("Added Process: " + name + "\n");

            // Clear input fields
            nameField.setText("");
            colorField.setText("");
            arrivalField.setText("");
            burstField.setText("");
            priorityField.setText("");
            quantumField.setText("");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid inputs.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void runScheduler() {
        if (processes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No processes to schedule.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        FCAIScheduler scheduler = new FCAIScheduler(processes);
        scheduler.schedule();

        // Update output area
        outputArea.append("\nScheduling Completed.\n");
        scheduler.displayFCAIFactor(processes);

        // Simulate Gantt Chart (simple example)
        ganttChartPanel.removeAll();
        ganttChartPanel.setLayout(new FlowLayout());
        for (FCAIProcess process : processes) {
            JLabel processLabel = new JLabel(process.getName());
            processLabel.setOpaque(true);
            processLabel.setBackground(Color.decode(process.getColor()));
            processLabel.setPreferredSize(new Dimension(100, 50));
            ganttChartPanel.add(processLabel);
        }
        ganttChartPanel.revalidate();
        ganttChartPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FCAIGUI().setVisible(true));
    }
}
