Public class Applicant {
    private String id;
    private String name;
    private String email;
    private String course;
    private String guardianName;
    private String status;

    //Constructor
    public Applicant(String id, String name, String email, String course, String guardianName, String status;) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.course = course;
        this.guardianName = guardianName;
        this.status = status;
    }

    @Override
    public String toString() {
        return id + "," + name + "," + email + "," + course + "," + guardianName + "," + status;
    }

    public static Applicant fromCSV(String line) {
        String[] parts = line.split(",");
        return new Applicant(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
    }
}