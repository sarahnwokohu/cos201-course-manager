// ============================================================
//  File    : CourseManagerConsole.java
//  Project : COS 201 Mid-Semester Lab Assessment
//  Student : NWOKOHU DUPE SARAH (2025/B/CSC/0639)
//  Role    : Console front-end - text menu, user input and
//            friendly error messages. Contains RECURSIVE
//            FEATURE #3: the menu re-displays itself.
// ============================================================

import java.util.Scanner;

public class CourseManagerConsole {

    private static final String DATA_FILE = "courses.txt";

    private final CourseManager manager = new CourseManager();  // shared logic layer
    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("  STUDENT COURSE MANAGEMENT SYSTEM (CONSOLE)  ");
        System.out.println("  COS 201 - Programming I Lab Assessment      ");
        System.out.println("  Student: NWOKOHU DUPE SARAH                  ");
        System.out.println("  Matric : 2025/B/CSC/0639                     ");
        System.out.println("==============================================");
        new CourseManagerConsole().showMenu();
        System.out.println("Thank you for using the system. Goodbye!");
    }

    // ---------------------------------------------------------
    //  RECURSIVE FEATURE #3: the menu calls itself after
    //  every action. Choosing 7 simply RETURNS - that is the
    //  base case that ends the recursion.
    // ---------------------------------------------------------
    private void showMenu() {
        System.out.println();
        System.out.println("=========== MAIN MENU ===========");
        System.out.println("1. Add Course");
        System.out.println("2. View All Courses");
        System.out.println("3. Search Course by Code");
        System.out.println("4. Compute Total Units");
        System.out.println("5. Save to File");
        System.out.println("6. Load from File");
        System.out.println("7. Exit Program");
        System.out.println("=================================");

        int choice = readInt("Choose an option (1-7): ");

        // Logical decision-making with a switch statement
        switch (choice) {
            case 1 -> addCourse();
            case 2 -> viewAllCourses();
            case 3 -> searchCourse();
            case 4 -> computeTotalUnits();
            case 5 -> saveToFile();
            case 6 -> loadFromFile();
            case 7 -> { return; }   // BASE CASE: stops the recursion
            default -> System.out.println("Invalid option. Please choose a number from 1 to 7.");
        }

        showMenu();   // RECURSIVE CASE: menu reappears after every action
    }

    /** Safely reads a whole number. Non-numeric input produces a
     *  friendly message instead of a crashing exception. */
    private int readInt(String prompt) {
        System.out.print(prompt);
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Error: please enter a valid whole number.");
            return Integer.MIN_VALUE;   // sentinel: treated as invalid below
        }
    }

    private void addCourse() {
        System.out.println("\n--- Add Course ---");
        System.out.print("Enter course code (e.g. COS201): ");
        String code = scanner.nextLine();
        System.out.print("Enter course title: ");
        String title = scanner.nextLine();
        int units = readInt("Enter credit units (1-6): ");
        if (units == Integer.MIN_VALUE) {
            return;   // non-numeric input already reported by readInt
        }
        try {
            manager.addCourse(code, title, units);
            System.out.println("SUCCESS: course added.");
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR: " + e.getMessage());  // friendly error message
        }
    }

    private void viewAllCourses() {
        System.out.println("\n--- All Recorded Courses ---");
        if (manager.getCourses().isEmpty()) {
            System.out.println("No courses recorded yet.");
            return;
        }
        System.out.println("CODE     TITLE                                    UNITS");
        System.out.println("--------------------------------------------------------");
        for (Course c : manager.getCourses()) {   // ITERATION with a for-loop
            System.out.println(c);
        }
        System.out.println("--------------------------------------------------------");
        System.out.println("Total courses: " + manager.getCourses().size());
    }

    private void searchCourse() {
        System.out.println("\n--- Search Course by Code ---");
        System.out.print("Enter course code to search: ");
        String code = scanner.nextLine();
        Course found = manager.search(code);      // recursive search
        if (found == null) {
            System.out.println("Course '" + Course.normaliseCode(code) + "' was not found.");
        } else {
            System.out.println("Course found:");
            System.out.println("CODE     TITLE                                    UNITS");
            System.out.println("--------------------------------------------------------");
            System.out.println(found);
        }
    }

    private void computeTotalUnits() {
        int total = manager.totalUnits();         // recursive accumulator
        System.out.println("\n--- Compute Total Units ---");
        if (manager.getCourses().isEmpty()) {
            System.out.println("No courses recorded yet.");
        } else {
            System.out.println("Total credit units of all recorded courses = " + total);
        }
    }

    private void saveToFile() {
        System.out.println("\n--- Save to File ---");
        try {
            manager.saveToFile(DATA_FILE);
            System.out.println("SUCCESS: " + manager.getCourses().size()
                    + " course(s) saved to " + DATA_FILE);
        } catch (Exception e) {
            System.out.println("ERROR: save failed - " + e.getMessage());
        }
    }

    private void loadFromFile() {
        System.out.println("\n--- Load from File ---");
        try {
            int loaded = manager.loadFromFile(DATA_FILE);
            System.out.println("SUCCESS: " + loaded + " new course(s) loaded from "
                    + DATA_FILE + ".");
        } catch (java.io.FileNotFoundException e) {
            System.out.println("ERROR: '" + DATA_FILE
                    + "' not found. Use option 5 to save first.");
        } catch (Exception e) {
            System.out.println("ERROR: load failed - " + e.getMessage());
        }
    }
}