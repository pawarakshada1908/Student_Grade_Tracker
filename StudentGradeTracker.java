package project;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class StudentGradeTracker extends JFrame {

    JTextField nameField, subjectField, marksField;
    JTable table;
    DefaultTableModel model;

    ArrayList<GradeRecord> records = new ArrayList<>();

    // Student Record Class
    class GradeRecord {

        String name;
        String subject;
        int marks;

        GradeRecord(String name, String subject, int marks) {

            this.name = name;
            this.subject = subject;
            this.marks = marks;
        }
    }

    StudentGradeTracker() {

        setTitle("Student Grade Tracker");
        setSize(850, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10, 10));

        // Heading
        JLabel heading = new JLabel(
                "STUDENT GRADE TRACKER", JLabel.CENTER);

        heading.setFont(new Font("Arial", Font.BOLD, 24));

        add(heading, BorderLayout.NORTH);

        // Input Panel
        JPanel inputPanel = new JPanel();

        inputPanel.add(new JLabel("Student Name:"));

        nameField = new JTextField(10);
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Subject:"));

        subjectField = new JTextField(10);
        inputPanel.add(subjectField);

        inputPanel.add(new JLabel("Marks:"));

        marksField = new JTextField(5);
        inputPanel.add(marksField);

        // Buttons
        JButton addButton = new JButton("Add Record");
        JButton deleteButton = new JButton("Delete Record");
        JButton reportButton = new JButton("Subject Report");

        inputPanel.add(addButton);
        inputPanel.add(deleteButton);
        inputPanel.add(reportButton);

        add(inputPanel, BorderLayout.CENTER);

        // Table
        model = new DefaultTableModel(
                new String[]{"Student Name", "Subject", "Marks"}, 0);

        table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.SOUTH);

        // Button Actions
        addButton.addActionListener(e -> addRecord());

        deleteButton.addActionListener(e -> deleteRecord());

        reportButton.addActionListener(e -> showReport());

        setVisible(true);
    }

    // Add Student Record
    void addRecord() {

        String name = nameField.getText().trim();
        String subject = subjectField.getText().trim();
        String marksText = marksField.getText().trim();

        if (name.isEmpty() || subject.isEmpty()
                || marksText.isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Please enter all details!");

            return;
        }

        try {

            int marks = Integer.parseInt(marksText);

            if (marks < 0 || marks > 100) {

                JOptionPane.showMessageDialog(this,
                        "Marks must be between 0 and 100!");

                return;
            }

            GradeRecord record =
                    new GradeRecord(name, subject, marks);

            records.add(record);

            model.addRow(new Object[]{name, subject, marks});

            nameField.setText("");
            subjectField.setText("");
            marksField.setText("");

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(this,
                    "Please enter valid numeric marks!");
        }
    }

    // Delete Selected Record
    void deleteRecord() {

        int row = table.getSelectedRow();

        if (row >= 0) {

            records.remove(row);
            model.removeRow(row);

        } else {

            JOptionPane.showMessageDialog(this,
                    "Please select a record to delete!");
        }
    }

    // Subject-wise Summary Report
    void showReport() {

        if (records.isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "No student records available!");

            return;
        }

        LinkedHashMap<String, ArrayList<Integer>> subjectMarks =
                new LinkedHashMap<>();

        for (GradeRecord record : records) {

            String subject = record.subject;

            if (!subjectMarks.containsKey(subject)) {

                subjectMarks.put(subject, new ArrayList<>());
            }

            subjectMarks.get(subject).add(record.marks);
        }

        StringBuilder report = new StringBuilder();

        report.append("SUBJECT-WISE SUMMARY REPORT\n\n");

        for (String subject : subjectMarks.keySet()) {

            ArrayList<Integer> marks = subjectMarks.get(subject);

            int total = 0;
            int highest = marks.get(0);
            int lowest = marks.get(0);

            for (int m : marks) {

                total = total + m;

                if (m > highest) {
                    highest = m;
                }

                if (m < lowest) {
                    lowest = m;
                }
            }

            double average = (double) total / marks.size();

            report.append("Subject: ").append(subject).append("\n");

            report.append("Students: ").append(marks.size()).append("\n");

            report.append("Average Marks: ").append(average).append("\n");

            report.append("Highest Marks: ").append(highest).append("\n");

            report.append("Lowest Marks: ").append(lowest).append("\n\n");
        }

        JTextArea textArea = new JTextArea(report.toString());

        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JOptionPane.showMessageDialog(this,
                new JScrollPane(textArea),
                "Subject-wise Summary Report",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // Main Method
    public static void main(String[] args) {

        new StudentGradeTracker();
    }
}

