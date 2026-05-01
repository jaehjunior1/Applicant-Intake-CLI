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
        System.out.println("1. Search by ID");
        System.out.println("2. Search by Email");
        System.out.println("3. View All Applicants");
        System.out.println("4. Print Summary Report");
        System.out.println("5. Back");

        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                searchById();
                break;
            case "2":
                searchByEmail();
                break;
            case "3":
                viewAllApplicants(); // NEW
                break;
            case "4":
                printSummary();
                break;
            case "5":
                return;
            default:
                System.out.println("Invalid option.");
        }
    }
}

private static void viewAllApplicants() {
    List<Applicant> list = FileService.loadApplicants();

    if (list.isEmpty()) {
        System.out.println("No applicants found.");
        return;
    }

    int pageSize = 5;
    int page = 0;

    while (true) {
        int start = page * pageSize;
        int end = Math.min(start + pageSize, list.size());

        System.out.println("\n--- Applicants (Page " + (page + 1) + ") ---");

        for (int i = start; i < end; i++) {
            Applicant a = list.get(i);
            System.out.println((i + 1) + ". " + a.getId() + " | " + a.getName() + " | " + a.getStatus());
        }

        System.out.println("\nN - Next | P - Previous | E - Exit");
        

        String input = scanner.nextLine();

if (input.equalsIgnoreCase("N")) {
    if (end >= list.size()) {
        System.out.println("You are on the last page.");
    } else {
        page++;
    }

} else if (input.equalsIgnoreCase("P")) {
    if (page == 0) {
        System.out.println("You are on the first page.");
    } else {
        page--;
    }

} else if (input.equalsIgnoreCase("E")) {
    return;

} else {
    // Try to interpret input as a number
    try {
        int userChoice = Integer.parseInt(input);

        if (userChoice < 1 || userChoice > (end - start)) {
            System.out.println("Invalid selection.");
        } else {
            int actualIndex = start + (userChoice - 1);
            Applicant selected = list.get(actualIndex);

            showApplicantAndUpdate(selected, list);
        }

    } catch (NumberFormatException e) {
        System.out.println("Invalid input.");
    }
}
 }
}

       

        

        private static void searchById() {
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

         private static void printSummary() {
                List<Applicant> list = FileService.loadApplicants();

                if (list.isEmpty()) {
                    System.out.println("No applicants found.");
                    return;
                }

                int total = 0;
                int pending = 0;
                int accepted = 0;
                int rejected = 0;

                for (Applicant a : list) {
                    total++;

                    if (a.getStatus().equalsIgnoreCase("Pending")) {
                        pending++;
                    } else if (a.getStatus().equalsIgnoreCase("Accepted")) {
                        accepted++;
                    } else if (a.getStatus().equalsIgnoreCase("Rejected")) {
                        rejected++;
                    }
                }

                System.out.println("\n--- Summary Report ---");
                System.out.println("Total Applicants: " + total);
                System.out.println("Pending: " + pending);
                System.out.println("Accepted: " + accepted);
                System.out.println("Rejected: " + rejected);
            }

}
