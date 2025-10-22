import java.util.*;

public class CandidateGenerator {
    
    public List<String> generateCandidates(String word, LanguageModel languageModel) {
        if (word == null || word.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        Set<String> candidates = new HashSet<>();
        
        // Generate candidates with single edit operations
        candidates.addAll(generateDeletions(word));
        candidates.addAll(generateInsertions(word, languageModel.getTeluguCharacters()));
        candidates.addAll(generateSubstitutions(word, languageModel.getTeluguCharacters()));
        candidates.addAll(generateTranspositions(word));
        
        // Filter valid words and rank by probability
        List<String> validCandidates = new ArrayList<>();
        for (String candidate : candidates) {
            if (languageModel.isValidWord(candidate)) {
                validCandidates.add(candidate);
            }
        }
        
        // Rank candidates by probability (semantic ranking)
        validCandidates.sort((c1, c2) -> 
            Double.compare(languageModel.getWordProbability(c2), 
                          languageModel.getWordProbability(c1)));
        
        return validCandidates;
    }
    
    private List<String> generateDeletions(String word) {
        List<String> deletions = new ArrayList<>();
        for (int i = 0; i < word.length(); i++) {
            StringBuilder sb = new StringBuilder(word);
            deletions.add(sb.deleteCharAt(i).toString());
        }
        return deletions;
    }
    
    private List<String> generateInsertions(String word, String teluguChars) {
        List<String> insertions = new ArrayList<>();
        for (int i = 0; i <= word.length(); i++) {
            for (char c : teluguChars.toCharArray()) {
                StringBuilder sb = new StringBuilder(word);
                insertions.add(sb.insert(i, c).toString());
            }
        }
        return insertions;
    }
    
    private List<String> generateSubstitutions(String word, String teluguChars) {
        List<String> substitutions = new ArrayList<>();
        for (int i = 0; i < word.length(); i++) {
            for (char c : teluguChars.toCharArray()) {
                char[] chars = word.toCharArray();
                if (chars[i] != c) { // Avoid same character substitution
                    chars[i] = c;
                    substitutions.add(new String(chars));
                }
            }
        }
        return substitutions;
    }
    
    private List<String> generateTranspositions(String word) {
        List<String> transpositions = new ArrayList<>();
        for (int i = 0; i < word.length() - 1; i++) {
            char[] chars = word.toCharArray();
            char temp = chars[i];
            chars[i] = chars[i + 1];
            chars[i + 1] = temp;
            transpositions.add(new String(chars));
        }
        return transpositions;
    }
}