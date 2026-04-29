package src.service;

import java.util.List;

import src.model.Applicant;



public class ApplicantService {

    public static String generatedId() {
        return "APP-" + System.currentTimeMillis();
    }

    public static boolean isDuplicateEmail(String email) {
        List<Applicant> list = FileService.loadApplicants();
        for (Applicant a : list) {
            if(a.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    public static Applicant findApplicantByEmail(String email) {
        List<Applicant> list = FileService.loadApplicants();
        for (Applicant a : list) {
            if(a.getEmail().equals(email)) {
                return a;
            }
        }
        return null;
    }
}