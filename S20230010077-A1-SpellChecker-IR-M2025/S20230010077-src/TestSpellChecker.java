public class TestSpellChecker {
    
    public static void main(String[] args) {
        System.out.println("=== TELUGU SPELL CHECKER TEST CASES ===");
        
        TeluguSpellChecker spellChecker = new TeluguSpellChecker();
        spellChecker.initializeSpellChecker("data/telugu_index.dat");
        
        // Test cases
        testCase1(spellChecker);
        testCase2(spellChecker);
        testCase3(spellChecker);
        testCase4(spellChecker);
        testCase5(spellChecker);
    }
    
    private static void testCase1(TeluguSpellChecker spellChecker) {
        System.out.println("\n--- Test Case 1: Single misspelled word ---");
        String text = "తెలుగు భాషా దేశం";
        spellChecker.processText(text);
        
        System.out.println("Original: " + text);
        System.out.println("Corrected: " + spellChecker.getCorrectedText());
        spellChecker.displayMisspelledWords();
    }
    
    private static void testCase2(TeluguSpellChecker spellChecker) {
        System.out.println("\n--- Test Case 2: Multiple misspelled words ---");
        String text = "విధ్యార్థి ఉపాధ్యాయుడు పాఠశాల";
        spellChecker.processText(text);
        
        System.out.println("Original: " + text);
        System.out.println("Corrected: " + spellChecker.getCorrectedText());
        spellChecker.displayMisspelledWords();
    }
    
    private static void testCase3(TeluguSpellChecker spellChecker) {
        System.out.println("\n--- Test Case 3: Completely correct text ---");
        String text = "తెలుగు భాష దేశం ప్రపంచ";
        spellChecker.processText(text);
        
        System.out.println("Original: " + text);
        System.out.println("Corrected: " + spellChecker.getCorrectedText());
        spellChecker.displayMisspelledWords();
    }
    
    private static void testCase4(TeluguSpellChecker spellChecker) {
        System.out.println("\n--- Test Case 4: Mixed correct and incorrect ---");
        String text = "కంప్యూటర్ ప్రోగ్రామింగ్ భాష తెలుగు";
        spellChecker.processText(text);
        
        System.out.println("Original: " + text);
        System.out.println("Corrected: " + spellChecker.getCorrectedText());
        spellChecker.displayMisspelledWords();
    }
    
    private static void testCase5(TeluguSpellChecker spellChecker) {
        System.out.println("\n--- Test Case 5: Complex words ---");
        String text = "విశ్వవిధ్యాలయ పరీక్షా ఫలితాలు";
        spellChecker.processText(text);
        
        System.out.println("Original: " + text);
        System.out.println("Corrected: " + spellChecker.getCorrectedText());
        spellChecker.displayMisspelledWords();
    }
}