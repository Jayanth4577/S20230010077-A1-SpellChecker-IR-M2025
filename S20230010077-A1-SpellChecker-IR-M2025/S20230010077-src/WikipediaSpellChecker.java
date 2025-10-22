import java.util.Map;
import java.util.Scanner;

public class WikipediaSpellChecker {
    
    public static void main(String[] args) {
        TeluguSpellChecker spellChecker = new TeluguSpellChecker();
        LanguageModel languageModel = new LanguageModel();
        Scanner scanner = new Scanner(System.in);
        
        try {
            System.out.println("=== Telugu Wikipedia Spell Checker ===");
            System.out.println("1. Build model from Wikipedia dump");
            System.out.println("2. Load existing model");
            System.out.println("3. Test spell checker");
            System.out.print("Choose option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    buildFromWikipediaDump(languageModel, scanner);
                    break;
                case 2:
                    languageModel.loadModel("data/telugu_index.dat");
                    break;
                case 3:
                    languageModel.loadModel("data/telugu_index.dat");
                    break;
                default:
                    System.out.println("Invalid choice. Using sample data.");
                    languageModel.loadModel("data/telugu_index.dat");
            }
            
            spellChecker.initializeWithModel(languageModel);
            runSpellChecker(spellChecker, scanner);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
    
    private static void buildFromWikipediaDump(LanguageModel model, Scanner scanner) {
        System.out.print("Enter path to Wikipedia dump (.bz2 file): ");
        String dumpPath = scanner.nextLine();
        
        System.out.print("Enter output directory for processed data: ");
        String outputDir = scanner.nextLine();
        
        System.out.println("Starting Wikipedia dump processing...");
        System.out.println("This may take several minutes for large dumps...");
        
        long startTime = System.currentTimeMillis();
        
        WikipediaDumpProcessor processor = new WikipediaDumpProcessor();
        String textOutputPath = outputDir + "/telugu_corpus.txt";
        String freqOutputPath = outputDir + "/telugu_frequencies.dat";
        
        Map<String, Integer> frequencies = processor.processDump(dumpPath, textOutputPath);
        processor.saveWordFrequencies(frequencies, freqOutputPath);
        
        long endTime = System.currentTimeMillis();
        
        System.out.println("Processing completed in " + (endTime - startTime) / 1000 + " seconds");
        
        model.loadModel("data/telugu_index.dat");
    }
    
    private static void runSpellChecker(TeluguSpellChecker spellChecker, Scanner scanner) {
        System.out.println("\n=== Spell Checker Ready ===");
        
        while (true) {
            System.out.println("\nOptions:");
            System.out.println("1. Check a sentence");
            System.out.println("2. Check a file");
            System.out.println("3. Exit");
            System.out.print("Choose option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    checkSentence(spellChecker, scanner);
                    break;
                case 2:
                    checkFile(spellChecker, scanner);
                    break;
                case 3:
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
    
    private static void checkSentence(TeluguSpellChecker spellChecker, Scanner scanner) {
        System.out.print("Enter Telugu sentence to check: ");
        String sentence = scanner.nextLine();
        
        spellChecker.processText(sentence);
        
        System.out.println("\n=== Spell Check Results ===");
        System.out.println("Original: " + sentence);
        System.out.println("Corrected: " + spellChecker.getCorrectedText());
        spellChecker.displayMisspelledWords();
    }
    
    private static void checkFile(TeluguSpellChecker spellChecker, Scanner scanner) {
        System.out.print("Enter path to text file: ");
        String filePath = scanner.nextLine();
        
        spellChecker.processDocument(filePath);
        spellChecker.displayMisspelledWords();
        
        System.out.println("\n=== Corrected Text ===");
        System.out.println(spellChecker.getCorrectedText());
    }
}