// ============================================================
//  File    : CourseManager.java
//  Project : COS 201 Mid-Semester Lab Assessment
//  Student : NWOKOHU DUPE SARAH (2025/B/CSC/0639)
//  Role    : Logic layer - stores ALL courses in an ArrayList
//            and performs every operation (add, view, search,
//            total units, save, load). The console front-end
//            only talks to this class.
// ============================================================

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class CourseManager {

    /** The internal structure that stores all courses. */
    private final List<Course> courses = new ArrayList<>();

    /** Adds a course after validation and a duplicate check. */
    public void addCourse(String code, String title, int units) {
        String normalised = Course.normaliseCode(code);
        if (search(normalised) != null) {
            throw new IllegalArgumentException(
                "Course with code " + normalised + " already exists.");
        }
        courses.add(new Course(code, title, units));  // Course validates too
    }

    public List<Course> getCourses() {
        return courses;
    }

    /** Public entry point for the recursive search. */
    public Course search(String code) {
        return searchRecursive(Course.normaliseCode(code), 0);
    }

    // ---------------------------------------------------------
    //  RECURSIVE FEATURE #1: search by course code
    // ---------------------------------------------------------
    private Course searchRecursive(String code, int index) {
        if (index >= courses.size()) {                 // BASE CASE 1: end of list -> not found
            return null;
        }
        if (courses.get(index).getCode().equalsIgnoreCase(code)) {
            return courses.get(index);                 // BASE CASE 2: course found
        }
        return searchRecursive(code, index + 1);       // RECURSIVE CASE: check next index
    }

    /** Public entry point for the recursive total-units count. */
    public int totalUnits() {
        return totalUnitsRecursive(0);
    }

    // ---------------------------------------------------------
    //  RECURSIVE FEATURE #2: sum of all credit units
    // ---------------------------------------------------------
    private int totalUnitsRecursive(int index) {
        if (index >= courses.size()) {                 // BASE CASE: past last element
            return 0;
        }
        return courses.get(index).getUnits()
             + totalUnitsRecursive(index + 1);         // RECURSIVE CASE
    }

    /** Writes every course as one CSV line into the text file.
     *  try-with-resources closes the file automatically. */
    public void saveToFile(String filename) throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(filename))) {
            for (Course c : courses) {                 // ITERATION
                out.println(c.toFileString());
            }
        }
    }

    /** Reads the file back and rebuilds the course list.
     *  Malformed lines are reported and SKIPPED (not fatal),
     *  duplicates already in memory are ignored. */
    public int loadFromFile(String filename) throws IOException {
        int loaded = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int lineNo = 0;
            while ((line = reader.readLine()) != null) {   // ITERATION
                lineNo++;
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                try {
                    Course c = Course.fromFileString(line);
                    if (search(c.getCode()) == null) {   // ignore duplicates
                        courses.add(c);
                        loaded++;
                    }
                } catch (IllegalArgumentException e) {
                    // graceful error handling: report + skip the bad line
                    System.err.println("Skipping malformed line " + lineNo
                            + " in " + filename + ": " + line);
                }
            }
        }
        return loaded;
    }
}