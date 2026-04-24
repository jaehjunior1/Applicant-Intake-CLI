import java.util.*;
public class Main {
    

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Applicant Intake System ===");
            System.out.println("1. New Application");
            System.out.println("2. Reviewer Mode");
            System.out.println("3. Exit");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    newApplication();
                    break;
                case "2":
                    reviewerMode();
                    break;
                case "3":
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }

        
    }
}