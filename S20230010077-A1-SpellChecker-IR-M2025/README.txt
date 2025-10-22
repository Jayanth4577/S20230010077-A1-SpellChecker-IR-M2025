TELUGU SPELL CHECKER SYSTEM
Roll No: S20230010077
Language: Java
Target Language: Telugu

DESCRIPTION:
------------
This is a comprehensive spell checker for Telugu language based on statistical language modeling and edit distance algorithms. The system can identify misspelled words, generate correction candidates, and provide semantic ranking.

MODULES:
--------
1. TeluguSpellChecker (Main Class)
   - Coordinates the entire spell checking process
   - Handles user input and output
   - Manages the spell checking workflow

2. LanguageModel
   - Builds and manages the word frequency dictionary
   - Loads/saves the language model from/to secondary storage
   - Provides word probability calculations

3. CandidateGenerator
   - Generates correction candidates using edit operations
   - Implements insertion, deletion, substitution, and transposition
   - Ranks candidates based on edit distance and frequency

4. FileHandler
   - Handles file I/O operations
   - Manages data persistence on secondary storage

5. WikipediaDumpProcessor
   - Processes Wikipedia XML dumps to extract Telugu text
   - Builds word frequency models from large corpora

6. TestSpellChecker
   - Contains 5 predefined test cases for demonstration
   - Hardcoded input for consistent grading

7. WikipediaSpellChecker
   - Main application with user interface
   - Interactive menu system for all operations

8. TestWithCustomInput
   - Utility for testing with custom Telugu text
   - Reads from data/input.txt
   - Displays detailed spell checking results

EDIT OPERATIONS:
----------------
1. Insertion: Add one Telugu character
2. Deletion: Remove one Telugu character  
3. Substitution: Replace one Telugu character with another
4. Transposition: Swap two adjacent Telugu characters

DATA SOURCES:
-------------
The system can use Telugu Wikipedia dumps from:
https://dumps.wikimedia.org/tewiki/latest/

Required file: tewiki-latest-pages-articles.xml.bz2 (~214 MB compressed, ~2 GB uncompressed)

SETUP - DOWNLOAD WIKIPEDIA DATA:
--------------------------------
OPTION A - Automated (Recommended):
   Run the provided PowerShell script:
   1. Open PowerShell in project directory
   2. Run: .\download-wikipedia.ps1
   3. The script will:
      - Download tewiki-latest-pages-articles.xml.bz2
      - Verify checksum
      - Extract to XML using 7-Zip
      - Clean up and prepare for processing
   
   Note: Requires 7-Zip installed (https://www.7-zip.org/)

OPTION B - Manual:
   1. Download from: https://dumps.wikimedia.org/tewiki/latest/tewiki-latest-pages-articles.xml.bz2
   2. Verify with checksums: tewiki-latest-sha1sums.txt
   3. Extract using 7-Zip: Right-click -> 7-Zip -> Extract Here
   4. Place extracted XML in data\ folder

HOW TO RUN:
-----------
OPTION 1 - Run Test Cases (Recommended for Grading):
   
   For Windows:
   1. Open Command Prompt or PowerShell
   2. Navigate to: cd S20230010077-src
   3. Compile: javac -encoding UTF-8 *.java
   4. Run tests: java TestSpellChecker
   
   For Mac/Linux:
   1. Open Terminal
   2. Navigate to: cd S20230010077-src
   3. Make script executable: chmod +x run.sh
   4. Run: ./run.sh
   
   This will run all 5 test cases and display results.

OPTION 2 - Test with Custom Input:
   
   For Windows:
   1. Edit data\input.txt with your Telugu text
   2. cd S20230010077-src
   3. javac -encoding UTF-8 TestWithCustomInput.java
   4. java TestWithCustomInput
   
   For Mac/Linux:
   1. Edit data/input.txt with your Telugu text
   2. cd S20230010077-src
   3. javac -encoding UTF-8 TestWithCustomInput.java
   4. java TestWithCustomInput

OPTION 3 - Interactive Mode:
   
   For Windows:
   1. cd S20230010077-src
   2. javac -encoding UTF-8 *.java
   3. java WikipediaSpellChecker
   
   For Mac/Linux:
   1. cd S20230010077-src
   2. javac -encoding UTF-8 *.java
   3. java WikipediaSpellChecker

PROCESSING WIKIPEDIA DUMP TO BUILD LANGUAGE MODEL:
---------------------------------------------------
After downloading and extracting the Wikipedia dump:

1. Run the main program (compile.bat then run-main.bat)
2. Choose option 1: "Build model from Wikipedia dump"
3. When prompted:
   - Wikipedia dump path: .\data\tewiki-latest-pages-articles.xml
   - Output directory: .\data\processed
4. Processing time: 10-30 minutes depending on your system
5. This creates:
   - telugu_corpus.txt (extracted Telugu text)
   - telugu_frequencies.dat (word frequency model)

Note: The extracted XML file must be uncompressed (not .bz2 or .gz)
If you provide a compressed file, you'll get an error with extraction instructions.

NOTE: For proper Telugu character display in Windows console, use chcp 65001 before running

REQUIREMENTS:
-------------
- Java 8 or higher
- Minimum 2GB RAM recommended
- 500MB free disk space for indices

STORAGE ARCHITECTURE:
---------------------
- Main Memory: Source document and candidate sets
- Secondary Storage: Complete spell checker index and language model

INPUT METHODS:
--------------
The spell checker accepts input in three ways:

1. HARDCODED TEST CASES (Recommended for demonstration):
   - TestSpellChecker.java contains 5 predefined test cases
   - Input is hardcoded as String variables in the code
   - No external input required
   - Best for quick evaluation and grading
   
2. INTERACTIVE INPUT:
   - WikipediaSpellChecker.java provides menu-driven interface
   - User can type Telugu sentences directly
   - Requires Telugu input capability on keyboard
   
3. FILE INPUT:
   - Sample file: data/input.txt (Telugu text in UTF-8)
   - Can check entire files for misspellings
   - Use option 2 in WikipediaSpellChecker menu

TESTING WITH CUSTOM TELUGU TEXT:
---------------------------------
To test with your own Telugu text from websites or online sources:

METHOD 1 - Using File Input (Easiest):
   1. Copy Telugu text from any website (Wikipedia, news, blogs, etc.)
   2. Open data\input.txt in a text editor
   3. Paste the Telugu text and save (ensure UTF-8 encoding)
   4. Compile: javac -encoding UTF-8 TestWithCustomInput.java
   5. Run: java TestWithCustomInput
   6. The spell checker will process your custom text and show results
   
METHOD 2 - Modify Test Code:
   1. Open S20230010077-src\TestSpellChecker.java
   2. Replace the String text values in any test case
   3. Save and recompile: javac -encoding UTF-8 *.java
   4. Run: java TestSpellChecker
   
METHOD 3 - Interactive Mode:
   1. Run WikipediaSpellChecker.java
   2. Choose option 3: "Test spell checker"
   3. Choose option 2: "Check text interactively"
   4. Type or paste your Telugu text directly
   5. Press Enter to see spell checking results

Note: TestWithCustomInput.java is included for easy testing with custom input.
It automatically reads from data\input.txt and displays detailed spell checking results.

INPUT FORMAT:
-------------
Input text files in UTF-8 encoding containing Telugu text.

OUTPUT FORMAT:
--------------
- Identified misspelled words
- Ranked list of correction candidates (up to 5 per word)
- Suggested corrections with confidence scores

CANDIDATE GENERATION:
---------------------
The system generates up to 5 correction candidates per misspelled word:
- Code: Math.min(5, candidates.size()) in TeluguSpellChecker.java line 75
- Current output shows 1-2 candidates due to limited sample dictionary (~70 words)
- With full Wikipedia corpus (50,000+ words), would show 5 candidates consistently
- All candidates are ranked by frequency (semantic ranking)

NOTE:
-----
The actual Telugu corpus data is not included in this submission due to size constraints. Please download Telugu Wikipedia dumps from https://dumps.wikimedia.org/tewiki/latest/ and place them in a 'data' folder.