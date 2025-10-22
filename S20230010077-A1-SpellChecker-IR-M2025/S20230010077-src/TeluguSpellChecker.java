import java.util.*;

public class TeluguSpellChecker {
    private LanguageModel languageModel;
    private CandidateGenerator candidateGenerator;
    private FileHandler fileHandler;
    
    // Main memory storage for current document and candidates
    private List<String> sourceDocument;
    private Map<String, List<String>> candidateMap;
    
    public TeluguSpellChecker() {
        this.languageModel = new LanguageModel();
        this.candidateGenerator = new CandidateGenerator();
        this.fileHandler = new FileHandler();
        this.sourceDocument = new ArrayList<>();
        this.candidateMap = new HashMap<>();
    }
    
    public void initializeSpellChecker(String indexPath) {
        System.out.println("Initializing Telugu Spell Checker...");
        languageModel.loadModel(indexPath);
        System.out.println("Spell checker initialized successfully!");
    }
    
    public void initializeWithModel(LanguageModel model) {
        this.languageModel = model;
    }
    
    public LanguageModel getLanguageModel() {
        return languageModel;
    }
    
    public void processDocument(String documentPath) {
        System.out.println("Processing document: " + documentPath);
        sourceDocument = fileHandler.readDocument(documentPath);
        if (sourceDocument.isEmpty()) {
            System.out.println("Warning: No content loaded from document");
            return;
        }
        identifyMisspelledWords();
        System.out.println("Document processing completed!");
    }
    
    public void processText(String text) {
        sourceDocument = Arrays.asList(text.split("\\s+"));
        identifyMisspelledWords();
    }
    
    private void identifyMisspelledWords() {
        candidateMap.clear();
        
        for (String word : sourceDocument) {
            if (!languageModel.isValidWord(word) && !word.trim().isEmpty()) {
                List<String> candidates = candidateGenerator.generateCandidates(word, languageModel);
                candidateMap.put(word, candidates);
            }
        }
    }
    
    public void displayMisspelledWords() {
        System.out.println("\n=== MISSPELLED WORDS AND CANDIDATES ===");
        
        if (candidateMap.isEmpty()) {
            System.out.println("No misspelled words found!");
            return;
        }
        
        for (Map.Entry<String, List<String>> entry : candidateMap.entrySet()) {
            System.out.println("Misspelled: " + entry.getKey());
            
            // Show top 5 candidates
            List<String> candidates = entry.getValue();
            int limit = Math.min(5, candidates.size());
            List<String> top5 = candidates.subList(0, limit);
            
            System.out.println("Top " + limit + " Candidates: " + top5);
            System.out.println("Recommended: " + 
                (candidates.isEmpty() ? "No suggestions" : candidates.get(0)));
            System.out.println("---");
        }
    }
    
    public String getCorrectedText() {
        List<String> correctedDocument = new ArrayList<>();
        
        for (String word : sourceDocument) {
            if (candidateMap.containsKey(word) && !candidateMap.get(word).isEmpty()) {
                correctedDocument.add(candidateMap.get(word).get(0));
            } else {
                correctedDocument.add(word);
            }
        }
        
        return String.join(" ", correctedDocument);
    }
    
    public Map<String, List<String>> getCandidateMap() {
        return candidateMap;
    }
    
    public void saveIndex(String indexPath) {
        languageModel.saveModel(indexPath);
        System.out.println("Index saved to: " + indexPath);
    }
}