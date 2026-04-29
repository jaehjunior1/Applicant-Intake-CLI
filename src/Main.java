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
            while (true) {
                System.out.println("\n--- Reviewer Mode ---");
                System.out.println("1. Search by Email");
                System.out.println("2. Search by ID");
                System.out.println("3. Back to Main Menu");
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1":
                        searchByEmail();
                        break;
                    case "2":
                        searchByID();
                        break;
                    case "3":
                        return;
                    default:
                        System.out.println("Invalid option.");
                }
            }
        }

        

        private static void searchByID() {
            System.out.println("Enter ID to search: ");
            String id = scanner.nextLine();
            List<Applicant> list = FileService.loadApplicants();
            for (Applicant a : list) {
                if(a.getId().equals(id)) {
                    showApplicantAndUpdate(a, list);
                    return;
                }
            }
            System.out.println("Applicant not found.");
         }

         private static void searchByEmail() {
            System.out.println("Enter email to search: ");
            String email = scanner.nextLine();

            Applicant applicant = ApplicantService.findApplicantByEmail(email);

            if (applicant == null) {
                System.out.println("Applicant not found.");
                return;
                
            }
            List<Applicant> list = FileService.loadApplicants();
            showApplicantAndUpdate(applicant, list);
         }

         private static void showApplicantAndUpdate(Applicant a, List<Applicant> list) {
            System.out.println("\n--- Applicant Details ---");
            System.out.println("ID: " + a.getId());
            System.out.println("Name: " + a.getName());
            System.out.println("Email: " + a.getEmail());
            System.out.println("Course: " + a.getCourse());
            System.out.println("Guardian: " + a.getGuardianName());
            System.out.println("Status: " + a.getStatus());

            if (!a.getStatus().equals("Pending")) {
                System.out.println("This application has already been processed.");
                return;
                
            }

            System.out.println("\n1. Accept");
            System.out.println("2. Reject");
            System.out.println("3. Cancel");

            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                a.setStatus("Accepted");
            } else if (choice.equals("2")) {
                a.setStatus("Rejected");
            } else {
                System.out.println("Action cancelled.");
                return;
            }
            FileService.overwriteAll(list);
            System.out.println("Application status updated to: " + a.getStatus());
         }
        }
