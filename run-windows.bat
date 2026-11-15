@echo off
where java >nul 2>nul || (echo Java JDK 17+ is required. & pause & exit /b 1)
where mvn >nul 2>nul || (echo Maven is required. Open this folder in VS Code with the Java Extension Pack or install Apache Maven. & pause & exit /b 1)
mvn clean compile exec:java
pause
