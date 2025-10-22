import java.util.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class LanguageModel {
    private Map<String, Integer> wordFrequencies;
    private Set<String> validWords;
    private int totalWords;
    
    // Telugu character set for edit operations
    private static final String TELUGU_CHARS = 
        "అఆఇఈఉఊఋఌఎఏఐఒఓఔకఖగఘఙచఛజఝఞటఠడఢణతథదధనపఫబభమయరఱలళవశషసహఽాిీుూృౄెేైొోౌ్ౕౖౠౡ";
    
    public LanguageModel() {
        this.wordFrequencies = new HashMap<>();
        this.validWords = new HashSet<>();
        this.totalWords = 0;
    }
    
    @SuppressWarnings("unchecked")
    public void loadModel(String filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            wordFrequencies = (Map<String, Integer>) ois.readObject();
            validWords = new HashSet<>(wordFrequencies.keySet());
            totalWords = wordFrequencies.values().stream().mapToInt(Integer::intValue).sum();
            System.out.println("Language model loaded with " + validWords.size() + " words");
        } catch (FileNotFoundException e) {
            System.out.println("No existing index found. Creating new language model...");
            initializeWithSampleData();
        } catch (Exception e) {
            System.out.println("Error loading model: " + e.getMessage());
            initializeWithSampleData();
        }
    }
    
    public void saveModel(String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(wordFrequencies);
            System.out.println("Language model saved with " + validWords.size() + " words");
        } catch (Exception e) {
            System.out.println("Error saving model: " + e.getMessage());
        }
    }
    
    private void initializeWithSampleData() {
        // Sample Telugu words with frequencies - Only CORRECT spellings
        String[] sampleWords = {
            // Common words (CORRECT versions only)
            "తెలుగు", "భాష", "దేశం", "ప్రపంచ", "విద్యార్థి", "విద్య", "విద్యా",
            "ఉపాధ్యాయ", "ఉపాధ్యాయుడు", "ఉపాధ్యాయురాలు", "పాఠశాల", "పాఠం", "పాఠశాలా",
            "కళాశాల", "కళ", "విశ్వవిద్యాలయం", "విశ్వవిద్యాలయ",  
            "పుస్తకం", "పుస్తకము", "లేఖన", "లేఖనం", "పరీక్ష", "పరీక్షలు", "ఫలితాలు", "ఫలితం",
            // Technology words
            "కంప్యూటర్", "కంప్యూటరు", "ప్రోగ్రామ్", "ప్రోగ్రామ", "ప్రోగ్రామింగ",
            "సాఫ్ట్‌వేర్", "హార్డ్‌వేర్", "ఇంటర్నెట్", "మొబైల్",
            // Places
            "భారతదేశం", "భారతం", "తెలంగాణ", "తెలంగాణా", "ఆంధ్రప్రదేశ్", "ఆంధ్రప్రదేశం",
            "హైదరాబాద్", "హైదరాబాదు", "చెన్నై", "చెన్నైయ్", "బెంగళూరు", "బెంగుళూరు", 
            "ముంబై", "ముంబయి", "దిల్లీ", "కోల్కతా", "కోల్‌కతా",
            // Verbs and common words
            "చదువు", "చదివింది", "రాయి", "వ్రాయు", "మాట్లాడు", "వినుము", "చూడు", "తినుము"
        };
        
        Random rand = new Random(42); // Fixed seed for consistency
        for (String word : sampleWords) {
            wordFrequencies.put(word, 100 + rand.nextInt(900)); // Random frequency 100-1000
        }
        validWords.addAll(wordFrequencies.keySet());
        totalWords = wordFrequencies.values().stream().mapToInt(Integer::intValue).sum();
    }
    
    public void buildFromCorpus(String corpusPath) {
        System.out.println("Building language model from corpus...");
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(corpusPath), StandardCharsets.UTF_8))) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split("\\s+");
                for (String word : words) {
                    word = word.trim();
                    if (!word.isEmpty() && isTeluguWord(word)) {
                        wordFrequencies.put(word, wordFrequencies.getOrDefault(word, 0) + 1);
                    }
                }
            }
            validWords = new HashSet<>(wordFrequencies.keySet());
            totalWords = wordFrequencies.values().stream().mapToInt(Integer::intValue).sum();
            System.out.println("Language model built with " + validWords.size() + " unique words");
        } catch (IOException e) {
            System.out.println("Error building from corpus: " + e.getMessage());
            initializeWithSampleData();
        }
    }
    
    private boolean isTeluguWord(String word) {
        return word.codePoints().anyMatch(cp -> (cp >= 0x0C00 && cp <= 0x0C7F));
    }
    
    public boolean isValidWord(String word) {
        return validWords.contains(word) || word.trim().isEmpty();
    }
    
    public double getWordProbability(String word) {
        if (totalWords == 0) return 0.0;
        int freq = wordFrequencies.getOrDefault(word, 0);
        return (double) freq / totalWords;
    }
    
    public Set<String> getValidWords() {
        return validWords;
    }
    
    public String getTeluguCharacters() {
        return TELUGU_CHARS;
    }
    
    public Map<String, Integer> getWordFrequencies() {
        return wordFrequencies;
    }
}