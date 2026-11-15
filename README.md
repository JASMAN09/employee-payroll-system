# Employee Payroll Management System

A simple desktop payroll application built with Java Swing, JDBC and SQLite.

## Features
- Register employees and store their name, email, department and designation.
- Store basic salary, allowances and deductions.
- Automatically calculate net salary: **basic salary + allowances - deductions**.
- View, edit and delete employee records.
- Generate department-wise staff counts and payroll totals.
- Persist records in a local SQLite database (`payroll.db`).

## What you need
- **JDK 17 or newer**
- **Visual Studio Code**
- VS Code extension: **Extension Pack for Java** (by Microsoft)
- Maven (the VS Code Java/Maven extensions can help manage Maven projects).

The SQLite JDBC driver is declared in `pom.xml`; Maven downloads it the first time the project is built. No database server is needed. The app creates `payroll.db` automatically in the project root.

> Important: I couldn't bundle the JDBC driver jar in this project because external downloads are unavailable in the build environment. So the first Maven build needs an internet connection to fetch that one dependency. After it is downloaded, the app can run offline.

## Open and run in VS Code
1. Extract the ZIP file.
2. In VS Code, choose **File → Open Folder** and select `employee-payroll-system`.
3. If prompted, install the recommended **Extension Pack for Java**.
4. Wait for the Java project/Maven import to finish.
5. Open `src/main/java/com/payroll/app/Main.java` and click **Run** above `main`, or open the VS Code terminal and run:

   ```bash
   mvn clean compile exec:java
   ```

You can also run `run-mac-linux.sh` (macOS/Linux) or `run-windows.bat` (Windows), provided Maven is available on PATH.

## Project structure

```text
employee-payroll-system/
├── .vscode/                 # VS Code recommendations and task
├── src/main/java/com/payroll/
│   ├── app/Main.java        # Application entry point
│   ├── db/Database.java     # JDBC connection and schema setup
│   ├── model/Employee.java  # Employee data model
│   ├── service/PayrollService.java # CRUD and department report logic
│   └── ui/PayrollFrame.java # Swing frontend
├── pom.xml                  # Maven project and SQLite JDBC dependency
├── run-mac-linux.sh
└── run-windows.bat
```

## Database
SQLite stores data locally in `payroll.db`. To reset the sample application data, close the app and delete that file; it will be recreated on next launch.

## Notes
- Email addresses must be unique.
- Salary values must be non-negative.
- Deductions cannot exceed basic salary plus allowances.
- This is a student project/demo, not a production payroll or tax-compliance system.
