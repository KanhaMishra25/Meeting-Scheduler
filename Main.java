import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Main {

    // ===== MODEL =====
    static class Meeting {
        private static int COUNTER = 1;

        private final int id;
        private final String title;
        private final String description;
        private final LocalDate date;
        private final LocalTime startTime;
        private final LocalTime endTime;

        public Meeting(String title, String description, LocalDate date, LocalTime startTime, LocalTime endTime) {
            this.id = COUNTER++;
            this.title = title;
            this.description = description;
            this.date = date;
            this.startTime = startTime;
            this.endTime = endTime;
        }

        public int getId() {
            return this.id;
        }

        public String getTitle() {
            return this.title;
        }

        public String getDescription() {
            return this.description;
        }

        public LocalDate getDate() {
            return this.date;
        }

        public LocalTime getStartTime() {
            return this.startTime;
        }

        public LocalTime getEndTime() {
            return this.endTime;
        }

        public String getDayName() {
            return this.date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        }

        public String getDateDisplay() {
            return this.date.format(DateTimeFormatter.ofPattern("dd-MM-uuuu"));
        }

        public String getFullDisplay() {
            return getDayName() + ", " + getDateDisplay() + " | " +
                    this.startTime + " - " + this.endTime + " | " + this.title;
        }

        @Override
        public String toString() {
            return getDayName() + ", " + getDateDisplay() + " | " +
                    this.startTime + " - " + this.endTime + " | " + this.title;
        }
    }

    // ===== SERVICE =====
    static class MeetingService {
        private final ArrayList<Meeting> meetings = new ArrayList<>();

        public void addMeeting(Meeting meeting) {
            this.meetings.add(meeting);
        }

        public void deleteMeeting(Meeting meeting) {
            this.meetings.remove(meeting);
        }

        public boolean hasConflict(Meeting newMeeting) {
            for (Meeting existing : this.meetings) {
                if (existing.getDate().equals(newMeeting.getDate())) {
                    boolean overlap =
                            newMeeting.getStartTime().isBefore(existing.getEndTime()) &&
                            existing.getStartTime().isBefore(newMeeting.getEndTime());

                    if (overlap) {
                        return true;
                    }
                }
            }
            return false;
        }

        public ArrayList<Meeting> getSortedMeetings() {
            ArrayList<Meeting> sorted = new ArrayList<>(this.meetings);
            sorted.sort(Comparator
                    .comparing(Meeting::getDate)
                    .thenComparing(Meeting::getStartTime)
                    .thenComparing(Meeting::getEndTime));
            return sorted;
        }
    }

    // ===== UI =====
    private JFrame frame;
    private JTextField titleField;
    private JTextField dateField;
    private JTextField startTimeField;
    private JTextField endTimeField;
    private JTextArea descriptionArea;

    private DefaultListModel<Meeting> listModel;
    private JList<Meeting> meetingList;
    private JTextArea detailsArea;

    private final MeetingService service = new MeetingService();

    private final Color bgColor = new Color(26, 26, 35);
    private final Color cardColor = new Color(38, 38, 52);
    private final Color accentBlue = new Color(0, 153, 255);
    private final Color accentGreen = new Color(46, 204, 113);
    private final Color accentRed = new Color(231, 76, 60);
    private final Color textWhite = Color.WHITE;
    private final Color mutedText = new Color(220, 220, 220);

    private final DateTimeFormatter dateFormatter =
            DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);

    private final DateTimeFormatter timeFormatter =
            DateTimeFormatter.ofPattern("HH:mm").withResolverStyle(ResolverStyle.STRICT);

    public Main() {
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Meeting Scheduler");
        frame.setSize(980, 620);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(15, 15));
        frame.getContentPane().setBackground(bgColor);

        JLabel header = new JLabel("Meeting Scheduler", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.setForeground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10));
        frame.add(header, BorderLayout.NORTH);

        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setBackground(bgColor);
        leftPanel.setPreferredSize(new Dimension(360, 0));

        JPanel formPanel = new JPanel(new GridLayout(9, 1, 8, 8));
        formPanel.setBackground(cardColor);
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        titleField = createField();
        dateField = createField();
        startTimeField = createField();
        endTimeField = createField();
        descriptionArea = new JTextArea(4, 20);
        styleTextArea(descriptionArea);

        formPanel.add(createLabel("Title"));
        formPanel.add(titleField);

        formPanel.add(createLabel("Date (yyyy-MM-dd)"));
        formPanel.add(dateField);

        formPanel.add(createLabel("Start Time (HH:mm)"));
        formPanel.add(startTimeField);

        formPanel.add(createLabel("End Time (HH:mm)"));
        formPanel.add(endTimeField);

        formPanel.add(createLabel("Description"));
        formPanel.add(new JScrollPane(descriptionArea));

        JButton addButton = createButton("Add Meeting", accentGreen);
        JButton deleteButton = createButton("Delete Selected", accentRed);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        buttonPanel.setBackground(cardColor);
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);

        leftPanel.add(formPanel, BorderLayout.CENTER);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(leftPanel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setBackground(bgColor);

        listModel = new DefaultListModel<>();
        meetingList = new JList<>(listModel);
        meetingList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        meetingList.setBackground(cardColor);
        meetingList.setForeground(textWhite);
        meetingList.setSelectionBackground(accentBlue);
        meetingList.setSelectionForeground(Color.WHITE);
        meetingList.setFixedCellHeight(32);

        JScrollPane listScroll = new JScrollPane(meetingList);
        listScroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(accentBlue),
                "Scheduled Meetings (sorted by date and time)",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 14),
                Color.BLACK
        ));

        detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        styleTextArea(detailsArea);
        detailsArea.setText("Select a meeting to view full details here.");

        JScrollPane detailsScroll = new JScrollPane(detailsArea);
        detailsScroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(accentGreen),
                "Meeting Details",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 14),
                Color.BLACK
        ));

        rightPanel.add(listScroll, BorderLayout.CENTER);
        rightPanel.add(detailsScroll, BorderLayout.SOUTH);

        frame.add(rightPanel, BorderLayout.CENTER);

        addButton.addActionListener(e -> handleAddMeeting());
        deleteButton.addActionListener(e -> handleDeleteMeeting());

        meetingList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    updateDetailsPanel();
                }
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void handleAddMeeting() {
        String title = titleField.getText().trim();
        String dateText = dateField.getText().trim();
        String startText = startTimeField.getText().trim();
        String endText = endTimeField.getText().trim();
        String description = descriptionArea.getText().trim();

        if (title.isEmpty() || dateText.isEmpty() || startText.isEmpty() || endText.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Title, date, start time, and end time are required.");
            return;
        }

        LocalDate date;
        LocalTime startTime;
        LocalTime endTime;

        try {
            date = LocalDate.parse(dateText, dateFormatter);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(frame, "Invalid date format. Use yyyy-MM-dd.");
            return;
        }

        try {
            startTime = LocalTime.parse(startText, timeFormatter);
            endTime = LocalTime.parse(endText, timeFormatter);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(frame, "Invalid time format. Use HH:mm in 24-hour format.");
            return;
        }

        if (!startTime.isBefore(endTime)) {
            JOptionPane.showMessageDialog(frame, "Start time must be earlier than end time.");
            return;
        }

        Meeting newMeeting = new Meeting(title, description, date, startTime, endTime);

        if (service.hasConflict(newMeeting)) {
            JOptionPane.showMessageDialog(
                    frame,
                    "⚠️ Conflict detected!\nAnother meeting already exists in this time range."
            );
            return;
        }

        service.addMeeting(newMeeting);
        refreshMeetingList();

        titleField.setText("");
        dateField.setText("");
        startTimeField.setText("");
        endTimeField.setText("");
        descriptionArea.setText("");

        JOptionPane.showMessageDialog(frame, "Meeting added successfully.");
    }

    private void handleDeleteMeeting() {
        Meeting selected = meetingList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(frame, "Please select a meeting to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                frame,
                "Delete selected meeting?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            service.deleteMeeting(selected);
            refreshMeetingList();
            detailsArea.setText("Select a meeting to view full details here.");
        }
    }

    private void refreshMeetingList() {
        listModel.clear();
        for (Meeting meeting : service.getSortedMeetings()) {
            listModel.addElement(meeting);
        }
    }

    private void updateDetailsPanel() {
        Meeting selected = meetingList.getSelectedValue();
        if (selected == null) {
            detailsArea.setText("Select a meeting to view full details here.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Title: ").append(selected.getTitle()).append("\n");
        sb.append("Day: ").append(selected.getDayName()).append("\n");
        sb.append("Date: ").append(selected.getDateDisplay()).append("\n");
        sb.append("Time: ").append(selected.getStartTime()).append(" - ").append(selected.getEndTime()).append("\n");
        sb.append("Description: ").append(selected.getDescription().isEmpty() ? "-" : selected.getDescription()).append("\n");

        detailsArea.setText(sb.toString());
    }

    private JTextField createField() {
        JTextField field = new JTextField();
        field.setBackground(new Color(58, 58, 76));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return field;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(mutedText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return label;
    }

    private JButton createButton(String text, Color bg) {
        JButton button = new JButton(text);
        button.setBackground(bg);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        return button;
    }

    private void styleTextArea(JTextArea area) {
        area.setBackground(new Color(58, 58, 76));
        area.setForeground(Color.WHITE);
        area.setCaretColor(Color.WHITE);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}
