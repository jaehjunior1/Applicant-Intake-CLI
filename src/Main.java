package src;
import java.io.File;
import java.util.List;
import java.util.Scanner;
import src.util.*;
import src.service.*;
import src.model.Applicant;

public class Main {
    
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        

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
    private static void newApplication() {
        System.out.println("\n--- New Application ---");

        // collect applicant information.
        String name;
        do {
             System.out.println("Enter Name: ");
                name = scanner.nextLine();
        } while (!ValidationUtil.isNotEmpty(name));

        String email;
        do {
            System.out.print("Enter email: ");
            email = scanner.nextLine();

            if (!ValidationUtil.isValidEmail(email)) {
                System.out.println("Invalid email format. please try again."); 
            } else if (ApplicantService.isDuplicateEmail(email)) {
                System.out.println("Email already exists. please try again.");
                email = "";
            }
            } while (!ValidationUtil.isValidEmail(email));

            System.out.println("Enter course: ");
            String course = scanner.nextLine();

            System.out.println("Enter guardian name: ");
            String guardian = scanner.nextLine();

            String id = ApplicantService.generatedId();

            Applicant applicant = new Applicant(id, name, email, course, guardian, "Pending");

            FileService.saveApplicant(applicant);

            System.out.println("Application saved! ID: " + id);
        }
        private static void reviewerMode() {
            System.out.println("Enter Application ID: ");
            String id = scanner.nextLine();
            
            List<Applicant> list = FileService.loadApplicants();
            boolean found = false;
            
        for (Applicant a : list) {
            if (a.getId().equals(id)) {
                found = true;

                System.out.println("Found: " + a.getEmail());
                System.out.println("1. Accept");
                System.out.println("2. Reject");

                String choice = scanner.nextLine();

                if (choice.equals("1")) {
                    a.setStatus("Accepted");
                } else if (choice.equals("2")) {
                    a.setStatus("Rejected");
                }
                break;
            }
        }
        if (!found) {
            System.out.println("Applicant not found.");
        } else {
            FileService.overwriteAll(list);
            System.out.println("Status updated.");
        }
    }
}