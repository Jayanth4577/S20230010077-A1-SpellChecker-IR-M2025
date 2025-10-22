import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import javax.xml.stream.*;

public class WikipediaDumpProcessor {
    private static final Set<String> TELUGU_STOP_WORDS = Set.of(
        "మరియు", "కానీ", "అయితే", "కాబట్టి", "అని", "గా", "ను", "ను", "కు", "లో", "నుండి"
    );

    public Map<String, Integer> processDump(String dumpFilePath, String outputTextPath) {
        Map<String, Integer> wordFrequencies = new HashMap<>();
        AtomicInteger pageCount = new AtomicInteger(0);
        
        System.out.println("Starting Wikipedia dump processing...");
        
        // Check if file is compressed
        if (dumpFilePath.toLowerCase().endsWith(".bz2") || dumpFilePath.toLowerCase().endsWith(".gz")) {
            System.err.println("ERROR: Compressed file detected!");
            System.err.println("Please extract the file first using 7-Zip or similar tool.");
            System.err.println("Windows: Right-click file -> 7-Zip -> Extract Here");
            System.err.println("Or use command: 7z e " + dumpFilePath);
            System.err.println("Then provide the extracted .xml file path.");
            return wordFrequencies;
        }
        
        try {
            FileInputStream fileInputStream = new FileInputStream(dumpFilePath);
            
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader reader = factory.createXMLStreamReader(fileInputStream, "UTF-8");
            
            BufferedWriter textWriter = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(outputTextPath), StandardCharsets.UTF_8));
            String currentElement = "";
            StringBuilder pageContent = new StringBuilder();
            boolean inText = false;
            boolean inTitle = false;
            String currentTitle = "";
            
            while (reader.hasNext()) {
                int event = reader.next();
                
                switch (event) {
                    case XMLStreamReader.START_ELEMENT:
                        currentElement = reader.getLocalName();
                        if ("text".equals(currentElement)) {
                            inText = true;
                            pageContent.setLength(0);
                        } else if ("title".equals(currentElement)) {
                            inTitle = true;
                            currentTitle = "";
                        }
                        break;
                        
                    case XMLStreamReader.CHARACTERS:
                        if (inText) {
                            pageContent.append(reader.getText());
                        } else if (inTitle) {
                            currentTitle += reader.getText();
                        }
                        break;
                        
                    case XMLStreamReader.END_ELEMENT:
                        String elementName = reader.getLocalName();
                        if ("text".equals(elementName)) {
                            inText = false;
                            processPageContent(pageContent.toString(), wordFrequencies, textWriter);
                            pageCount.incrementAndGet();
                            
                            if (pageCount.get() % 1000 == 0) {
                                System.out.println("Processed " + pageCount.get() + " pages...");
                            }
                        } else if ("title".equals(elementName)) {
                            inTitle = false;
                        } else if ("page".equals(elementName)) {
                            currentTitle = "";
                            pageContent.setLength(0);
                        }
                        break;
                }
            }
            
            reader.close();
            fileInputStream.close();
            textWriter.close();
            
            System.out.println("Completed processing " + pageCount.get() + " pages");
            
        } catch (Exception e) {
            System.err.println("Error processing dump: " + e.getMessage());
            e.printStackTrace();
        }
        
        return wordFrequencies;
    }
    
    private void processPageContent(String content, Map<String, Integer> wordFrequencies, 
                                  BufferedWriter writer) throws IOException {
        if (content == null || content.trim().isEmpty()) {
            return;
        }
        
        String cleanText = extractCleanText(content);
        
        if (!cleanText.trim().isEmpty()) {
            writer.write(cleanText);
            writer.newLine();
            updateWordFrequencies(cleanText, wordFrequencies);
        }
    }
    
    private String extractCleanText(String wikiText) {
        if (wikiText == null) return "";
        
        String text = wikiText;
        text = text.replaceAll("\\{\\{[^}]*\\}\\}", "");
        text = text.replaceAll("\\[\\[[^]]*?\\|([^]]*?)]]", "$1");
        text = text.replaceAll("\\[\\[([^]|]*?)]]", "$1");
        text = text.replaceAll("<[^>]+>", "");
        text = text.replaceAll("<ref[^>]*>.*?</ref>", "");
        text = text.replaceAll("&[a-z]+;", "");
        text = text.replaceAll("\\[https?://[^] ]*]", "");
        text = text.replaceAll("\\[\\[File:[^]]*]]", "");
        text = text.replaceAll("\\[\\[Image:[^]]*]]", "");
        text = text.replaceAll("\\s+", " ").trim();
        
        return text;
    }
    
    private void updateWordFrequencies(String text, Map<String, Integer> wordFrequencies) {
        String[] words = text.split("[\\s\\p{Punct}]+");
        
        for (String word : words) {
            if (isValidTeluguWord(word)) {
                wordFrequencies.merge(word, 1, Integer::sum);
            }
        }
    }
    
    private boolean isValidTeluguWord(String word) {
        if (word == null || word.trim().isEmpty() || word.length() < 2) {
            return false;
        }
        
        boolean hasTeluguChars = word.codePoints()
            .anyMatch(cp -> (cp >= 0x0C00 && cp <= 0x0C7F));
        
        boolean notStopWord = !TELUGU_STOP_WORDS.contains(word);
        
        return hasTeluguChars && notStopWord;
    }
    
    public void saveWordFrequencies(Map<String, Integer> wordFrequencies, String outputPath) {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(outputPath), StandardCharsets.UTF_8))) {
            
            wordFrequencies.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(entry -> {
                    try {
                        writer.write(entry.getKey() + "|" + entry.getValue());
                        writer.newLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            
            System.out.println("Word frequencies saved to: " + outputPath);
            System.out.println("Total unique words: " + wordFrequencies.size());
            
        } catch (IOException e) {
            System.err.println("Error saving word frequencies: " + e.getMessage());
        }
    }
}