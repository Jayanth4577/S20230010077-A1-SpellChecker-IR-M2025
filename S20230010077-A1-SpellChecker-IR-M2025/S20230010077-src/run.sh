#!/bin/bash
# Telugu Spell Checker - Run Script for Mac/Linux
# This script compiles and runs the spell checker

echo "=== Telugu Spell Checker - Compilation and Test ==="
echo ""

# Compile all Java files
echo "Compiling Java files..."
javac -encoding UTF-8 *.java

if [ $? -eq 0 ]; then
    echo "✓ Compilation successful!"
    echo ""
    
    # Run test cases
    echo "Running test cases..."
    echo ""
    java TestSpellChecker
else
    echo "✗ Compilation failed!"
    exit 1
fi
