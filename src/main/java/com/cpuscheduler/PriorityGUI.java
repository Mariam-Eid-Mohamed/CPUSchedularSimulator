package com.cpuscheduler;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PriorityGUI extends JFrame {
    private JTextField nameField, priorityField, arrivalField, burstField, colorField, contextSwitchingField;
    private JTextArea outputArea;
    private JPanel ganttChartPanel;
    private List<Process> processes = new ArrayList<>();

    public PriorityGUI() {
        setTitle("Priority Scheduler");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Top Panel for Process Input
        JPanel inputPanel = new JPanel(new GridLayout(7, 2));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add Process"));

        inputPanel.add(new JLabel("Process Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Priority:"));
        priorityField = new JTextField();
        inputPanel.add(priorityField);

        inputPanel.add(new JLabel("Arrival Time:"));
        arrivalField = new JTextField();
        inputPanel.add(arrivalField);

        inputPanel.add(new JLabel("Burst Time:"));
        burstField = new JTextField();
        inputPanel.add(burstField);

        inputPanel.add(new JLabel("Color"));
        colorField = new JTextField();
        inputPanel.add(colorField);


        JButton addProcessButton = new JButton("Add Process");
        addProcessButton.addActionListener(e -> addProcess());
        inputPanel.add(addProcessButton);

        JButton runSchedulerButton = new JButton("Run Scheduler");
        runSchedulerButton.addActionListener(e -> runScheduler());
        inputPanel.add(runSchedulerButton);

        add(inputPanel, BorderLayout.NORTH);

        // Center Panel for Gantt Chart
        ganttChartPanel = new JPanel();
        ganttChartPanel.setPreferredSize(new Dimension(800, 200));
        ganttChartPanel.setBorder(BorderFactory.createTitledBorder("Gantt Chart"));
        add(ganttChartPanel, BorderLayout.CENTER);

        // Bottom Panel for Output Area
        outputArea = new JTextArea(10, 50);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Scheduler Output"));
        add(scrollPane, BorderLayout.SOUTH);
    }

    private void addProcess() {
        try {
            String name = nameField.getText();
            int priority = Integer.parseInt(priorityField.getText());
            int arrivalTime = Integer.parseInt(arrivalField.getText());
            int burstTime = Integer.parseInt(burstField.getText());
            String color = colorField.getText();

            processes.add(new Process(name, color, arrivalTime, burstTime, priority));
            outputArea.append("Added Process: " + name + "\n");

            // Clear input fields
            nameField.setText("");
            priorityField.setText("");
            arrivalField.setText("");
            burstField.setText("");
            colorField.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid Input! Please check the fields.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void runScheduler() {
        try {


            // Run the Priority Scheduler
            PriorityScheduler scheduler = new PriorityScheduler();
            scheduler.schedule(processes.toArray(new Process[0]));

            // Generate Gantt Chart
            generateGanttChart(scheduler.RESULT);

            // Display the scheduler output
            outputArea.append("\nScheduler Output:\n");
            for (Process process : processes) {
                outputArea.append("Process: " + process.getName() +
                        ", Arrival: " + process.getArrivalTime() +
                        ", Burst: " + process.getBurstTime() +
                        ", Priority: " + process.getPriority() +
                        ", Start: " + process.getStartTime() +
                        ", Completion: " + process.getCompletionTime() +
                        "\n");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error running scheduler: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void generateGanttChart(List<Process> RESULT) {
        ganttChartPanel.removeAll();
        ganttChartPanel.setLayout(null); // Use manual layout for precise positioning

        int currentX = 10; // Starting position for the Gantt chart
        int processHeight = 50; // Fixed height for all process blocks
        int scaleFactor = 10; // Scale factor for burst time to pixel width

        for (Process process : RESULT) {
            int width = process.getBurstTime() * scaleFactor;

            JLabel label = new JLabel(process.getName(), SwingConstants.CENTER);
            label.setOpaque(true);
            label.setBackground(getColorByName(process.getColor())); // Use color mapping
            label.setForeground(Color.BLACK); // Text color
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            label.setBounds(currentX, 20, width, processHeight);

            ganttChartPanel.add(label);
            currentX += width; // Move to the next position
        }

        ganttChartPanel.revalidate();
        ganttChartPanel.repaint();
    }
    private Color getColorByName(String colorName) {
        switch (colorName.toLowerCase()) {
            case "red":
                return Color.RED;
            case "blue":
                return Color.BLUE;
            case "green":
                return Color.GREEN;
            case "yellow":
                return Color.YELLOW;
            case "cyan":
                return Color.CYAN;
            case "magenta":
                return Color.MAGENTA;
            case "orange":
                return Color.ORANGE;
            case "pink":
                return Color.PINK;
            case "gray":
                return Color.GRAY;
            case "black":
                return Color.BLACK;
            case "white":
                return Color.WHITE;
            default:
                try {
                    // Try decoding custom hex colors like #FF5733
                    return Color.decode(colorName);
                } catch (NumberFormatException e) {
                    // Fallback to a default color if input is invalid
                    return Color.LIGHT_GRAY;
                }
        }
    }




    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PriorityGUI gui = new PriorityGUI();
            gui.setVisible(true);
        });
    }
}
