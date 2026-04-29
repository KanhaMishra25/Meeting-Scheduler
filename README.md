🚀 Meeting Scheduler (Java Swing)

A desktop-based Meeting Scheduler application built using Java Swing. This project provides an intuitive and responsive interface to manage meetings efficiently with features like conflict detection, filtering, sorting, and detailed viewing.

📌 Features
✅ Add, view, and delete meetings
⏰ Start and end time support (24-hour format)
⚠️ Conflict detection (prevents overlapping meetings)
🔍 Filter meetings:
All
Today
This Week
Upcoming
🔃 Sort meetings:
Date & Time
Start Time
Title
📋 Detailed meeting view panel
🎨 Modern dark-themed responsive UI
✔️ Input validation (strict date & time formats)
🛠️ Tech Stack
Language: Java
UI Framework: Java Swing
Concepts Used:
Object-Oriented Programming (OOP)
Event-Driven Programming
Collections (ArrayList)
Date & Time API (LocalDate, LocalTime)
📄 Project Structure

Single-file implementation:

Main.java
Components:
Meeting (Model): Represents meeting data
Service (Logic): Handles storage, sorting, and conflict detection
UI Layer: Swing-based interface and event handling
⚙️ How to Run
1. Compile
javac Main.java
2. Run
java Main
🧠 Key Logic
🔥 Conflict Detection

Prevents overlapping meetings using interval logic:

newStart < existingEnd && existingStart < newEnd
📸 UI Overview
Left panel → Input form
Top bar → Filter & Sort dropdowns
Right panel → Meeting list + details
Fully resizable layout
🚀 Future Improvements
📆 Calendar-based UI (monthly view)
🔔 Reminder/notification system
💾 Database integration (SQLite)
✏️ Edit meeting functionality
🌐 Multi-user support
💡 Use Cases
Personal scheduling
Academic project
Interview demonstration
Basic calendar system prototype
👨‍💻 Author

Developed as a Java Swing project demonstrating UI design, data handling, and scheduling logic.

📜 License

This project is open-source and available for educational and personal use.
