package src.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.Buffer;
import java.util.List;
import 

public class FileService {

    private static final String FILE_NAME = "applicants.csv";

    public static void saveApplicant(Applicant applicant) {
        try (BufferedWrite writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(applicant.toString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving file.");
        }
    }

    public static List<Applicant> loadApplicants() {
        List<Applicant> list = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readerLine()) != null) {
                list.add(Applicant.fromCSV(line));
            }
        } catch (IOException e) {

        }
        return list;
    }

    public static void overwriteAll(List<Applicant> applicants) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Applicants a : applicants) {
                writer.write(a.toString());
                writer.newLine();
            }
        }catch (IOException e) {
            System.out.println("Error writing file.");
        }
    }
}