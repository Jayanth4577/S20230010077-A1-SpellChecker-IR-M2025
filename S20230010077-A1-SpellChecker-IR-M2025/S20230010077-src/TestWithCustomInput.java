import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class TestWithCustomInput {
    public static void main(String[] args) {
        System.out.println("=== TESTING WITH CUSTOM INPUT FROM input.txt ===\n");
        
        // Initialize spell checker
        TeluguSpellChecker spellChecker = new TeluguSpellChecker();
        
        // Read text from input.txt
        String inputText = readInputFile("../data/input.txt");
        if (inputText == null || inputText.trim().isEmpty()) {
            System.out.println("Error: Could not read input.txt or file is empty");
            return;
        }
        
        System.out.println("Original Text:");
        System.out.println(inputText);
        System.out.println("\n" + "=".repeat(60) + "\n");
        
        // Process the text
        spellChecker.processText(inputText);
        
        System.out.println("Corrected Text:");
        System.out.println(spellChecker.getCorrectedText());
        System.out.println("\n" + "=".repeat(60) + "\n");
        
        // Show misspelled words and candidates
        Map<String, List<String>> candidates = spellChecker.getCandidateMap();
        if (candidates.isEmpty()) {
            System.out.println("✓ No spelling errors found! All words are correct.");
        } else {
            System.out.println("SPELLING ERRORS DETECTED:\n");
            int count = 1;
            for (Map.Entry<String, List<String>> entry : candidates.entrySet()) {
                System.out.println(count + ". Misspelled Word: " + entry.getKey());
                List<String> suggestions = entry.getValue();
                if (!suggestions.isEmpty()) {
                    System.out.println("   Top Suggestions:");
                    for (int i = 0; i < Math.min(5, suggestions.size()); i++) {
                        System.out.println("      → " + suggestions.get(i));
                    }
                } else {
                    System.out.println("   No suggestions available");
                }
                System.out.println();
                count++;
            }
        }
    }
    
    private static String readInputFile(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                System.out.println("Warning: " + filePath + " not found");
                return null;
            }
            
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)
            );
            
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(" ");
            }
            reader.close();
            
            return content.toString().trim();
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return null;
        }
    }
}
