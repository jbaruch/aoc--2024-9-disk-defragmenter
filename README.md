# Advent of Code Day 9 - Disk Fragmenter

This is a Kotlin solution for Day 9 of Advent of Code, implementing a disk defragmentation system that calculates checksums based on different file movement strategies.

## Problem Description

The problem involves moving files on a disk according to specific rules and calculating checksums based on their final positions.

### Part 1
- Move individual blocks from right to left
- Each block moves to the leftmost available free space
- Calculate checksum based on final positions

### Part 2
- Move entire files as units
- Process files in decreasing ID order
- Only move a file if there's enough contiguous space to its left
- Calculate checksum based on final positions

## Project Structure

- `src/main/kotlin/DiskFragmenter.kt` - Main implementation
- `src/main/resources/2.txt` - Puzzle input
- `src/test/kotlin/DiskFragmenterTest.kt` - Test cases
- `src/test/resources/` - Test input files

## Running the Solution

```bash
./gradlew run
```

This will run both parts of the solution and print the answers.

## Running Tests

```bash
./gradlew test
```

## Requirements

- Java 17 or higher
- Gradle (wrapper included)
