public class ColumnMappingDemo {

    public static void main(String[] args) throws Exception {

        String[] header1 = {"roll_no", "name", "marks", "city"};
        String[] row1 = {"101", "Dhruvi", "87.5", "Surat"};
        show("Case 1: all columns present", header1, row1);

        String[] header2 = {"roll_no", "name", "marks"};
        String[] row2 = {"102", "Amit", "74"};
        show("Case 2: optional column missing", header2, row2);

        String[] header3 = {"marks", "name", "roll_no"};
        String[] row3 = {"90.5", "Riya", "103"};
        show("Case 3: different column order", header3, row3);

        String[] header4 = {"roll_no", "name", "city"};
        String[] row4 = {"104", "Neha", "Surat"};
        show("Case 4: required column missing", header4, row4);
    }

    static void show(String title, String[] header, String[] row) {
        System.out.println("--- " + title + " ---");
        try {
            Student student = RowMapper.map(Student.class, header, row);
            System.out.println(student);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}