// ============================================================
//  File    : Course.java
//  Project : COS 201 Mid-Semester Lab Assessment
//  Student : NWOKOHU DUPE SARAH (2025/B/CSC/0639)
//  Role    : Model class - represents ONE course.
//            Holds data + validates it, and handles the
//            string <-> file (CSV) conversion.
// ============================================================

public class Course {

    private final String code;
    private final String title;
    private final int units;

    public Course(String code, String title, int units) {
        // --- String processing: trim + uppercase so that
        //     'cos201' and '  cos201 ' become the same code.
        String normalised = normaliseCode(code);

        // --- Validation: business rules enforced with exceptions.
        if (!normalised.matches("[A-Z]{3}[0-9]{3}")) {
            throw new IllegalArgumentException(
                "Invalid course code '" + code +
                "'. Expected 3 letters followed by 3 digits, e.g. COS201.");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Course title must not be empty.");
        }
        if (units < 1 || units > 6) {
            throw new IllegalArgumentException("Credit units must be between 1 and 6.");
        }

        this.code = normalised;
        this.title = title.trim();
        this.units = units;
    }

    /** String processing helper: trims whitespace and upper-cases. */
    public static String normaliseCode(String code) {
        return code == null ? "" : code.trim().toUpperCase();
    }

    public String getCode()  { return code;  }
    public String getTitle() { return title; }
    public int    getUnits() { return units; }

    /** Converts this course into one CSV line for the text file. */
    public String toFileString() {
        // commas inside a title would break the CSV, so replace them
        return code + "," + title.replace(",", " ") + "," + units;
    }

    /** Rebuilds a Course object from one CSV line read from file. */
    public static Course fromFileString(String line) {
        String[] parts = line.split(",", -1);   // keep empty fields
        if (parts.length != 3) {
            throw new IllegalArgumentException("Malformed line: " + line);
        }
        return new Course(parts[0], parts[1], Integer.parseInt(parts[2].trim()));
    }

    /** Formats the course as one aligned table row. */
    @Override
    public String toString() {
        return String.format("%-8s %-38s %d", code, title, units);
    }
}