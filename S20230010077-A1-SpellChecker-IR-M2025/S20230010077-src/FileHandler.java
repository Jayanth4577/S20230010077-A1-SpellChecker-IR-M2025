import java.util.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileHandler {
    
    public List<String> readDocument(String filePath) {
        List<String> words = new ArrayList<>();
        
        // Check if file exists first
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("File not found: " + filePath);
            System.out.println("Using sample data for demonstration...");
            return Arrays.asList("తెలుగు", "భాషా", "దేశం", "ప్రపంచ", "విధ్యార్థి", "పరీక్షా", "ఫలితాలు");
        }
        
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                String[] lineWords = line.split("\\s+");
                for (String word : lineWords) {
                    if (!word.trim().isEmpty()) {
                        words.add(word.trim());
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading document: " + e.getMessage());
            // Return sample data for demonstration
            return Arrays.asList("తెలుగు", "భాషా", "దేశం", "ప్రపంచ", "విధ్యార్థి", "పరీక్షా", "ఫలితాలు");
        }
        return words;
    }
    
    public void writeDocument(String filePath, String content) {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8))) {
            writer.write(content);
        } catch (IOException e) {
            System.out.println("Error writing document: " + e.getMessage());
        }
    }
}