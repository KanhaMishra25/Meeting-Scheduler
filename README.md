# 🚀 Meeting Scheduler (Java Swing)

A desktop-based **Meeting Scheduler application** built using Java Swing. It provides a simple and responsive interface to manage meetings with features like conflict detection, filtering, sorting, and detailed viewing.

---

## 📌 Features

* Add, view, and delete meetings
* Start and end time support (24-hour format)
* Conflict detection to prevent overlapping meetings
* Filter meetings: All, Today, This Week, Upcoming
* Sort meetings: Date & Time, Start Time, Title
* Detailed meeting view panel
* Responsive and modern dark-themed UI
* Input validation for date and time formats

---

## 🛠️ Tech Stack

* Java
* Java Swing
* Object-Oriented Programming (OOP)
* Java Date & Time API (LocalDate, LocalTime)

---

## 📄 Project Structure

Main.java contains the complete application:

* Meeting class → Represents meeting data
* Service class → Handles logic, sorting, and conflict detection
* UI → Built using Swing components and event handling

---

## ⚙️ How to Run

1. Compile the code
   javac Main.java

2. Run the program
   java Main

---

## 🧠 Key Logic

Conflict detection ensures no overlapping meetings:

newStart < existingEnd && existingStart < newEnd

---

## 📸 UI Overview

* Left panel: Input form
* Top bar: Filter and sort options
* Right panel: Meeting list and details
* Fully resizable layout

---

## 🚀 Future Improvements

* Calendar-based UI
* Reminder notifications
* Database integration (SQLite)
* Edit meeting functionality
* Multi-user support

---

## 💡 Use Cases

* Personal scheduling
* Academic projects
* Interview demonstrations
* Basic calendar system prototype

---

## 📜 License

Open-source project for educational and personal use.
