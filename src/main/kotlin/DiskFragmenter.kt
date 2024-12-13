/**
 * Solves the Disk Fragmenter puzzle from Advent of Code Day 9
 */
class DiskFragmenter {
    /**
     * Represents a block on the disk
     * @property id The identifier of the file (-1 for free space)
     * @property size The size of the block
     */
    data class Block(val id: Int, val size: Int)

    /**
     * Parses a disk map string into a list of blocks (files and spaces)
     * @param input The disk map string where alternating digits represent file/space lengths
     * @return List of Block objects with their sizes and IDs (-1 for spaces)
     * @throws IllegalArgumentException if the input string is invalid
     */
    fun parseDiskMap(input: String): List<Block> {
        require(input.all { it.isDigit() }) { "Input must contain only digits" }
        return input.chunked(1)
            .mapIndexed { index, size -> 
                Block(
                    id = if (index % 2 == 0) index / 2 else -1,
                    size = size.toInt()
                )
            }
    }

    /**
     * Expands blocks into individual positions
     * @param blocks List of blocks to expand
     * @return List of file IDs at each position (-1 for free space)
     */
    private fun expandBlocks(blocks: List<Block>): List<Int> {
        return blocks.flatMap { block -> 
            List(block.size) { block.id }
        }
    }

    /**
     * Calculates checksum by multiplying each file block's position by its ID
     * @param positions List of file IDs at each position (-1 for free space)
     * @return The calculated checksum
     */
    private fun calculateChecksum(positions: List<Int>): Long {
        return positions.mapIndexed { pos, id ->
            if (id >= 0) pos.toLong() * id.toLong() else 0L
        }.sum()
    }

    /**
     * Moves a single block from the rightmost position to the leftmost free space
     * @param positions Current positions of blocks
     * @return Updated positions after moving one block
     */
    private fun moveOneBlock(positions: List<Int>): List<Int> {
        val mutable = positions.toMutableList()
        // Find rightmost file block
        val rightmostIndex = mutable.indexOfLast { it >= 0 }
        if (rightmostIndex < 0) return positions
        
        // Find leftmost free space
        val leftmostSpace = mutable.indexOfFirst { it == -1 }
        if (leftmostSpace < 0 || leftmostSpace >= rightmostIndex) return positions
        
        // Move the block
        mutable[leftmostSpace] = mutable[rightmostIndex]
        mutable[rightmostIndex] = -1
        
        return mutable
    }

    /**
     * Finds the leftmost position where a file of given size can fit
     * @param positions Current positions list
     * @param startPos Position to start searching from
     * @param size Required contiguous space size
     * @return The starting position of the found space, or -1 if no suitable space found
     */
    private fun findLeftmostSpace(positions: List<Int>, startPos: Int, size: Int): Int {
        var currentStart = -1
        var consecutiveSpaces = 0
        
        for (i in 0 until startPos) {
            if (positions[i] == -1) {
                if (currentStart == -1) currentStart = i
                consecutiveSpaces++
                if (consecutiveSpaces >= size) return currentStart
            } else {
                currentStart = -1
                consecutiveSpaces = 0
            }
        }
        return -1
    }

    /**
     * Calculates checksum for Part 1 - moving blocks right-to-left one at a time
     * @param input The disk map string
     * @return The calculated checksum
     */
    fun calculateChecksumPart1(input: String): Long {
        val blocks = parseDiskMap(input)
        var positions = expandBlocks(blocks)
        
        // Keep moving blocks until no more moves are possible
        while (true) {
            val newPositions = moveOneBlock(positions)
            if (newPositions == positions) break
            positions = newPositions
        }
        
        return calculateChecksum(positions)
    }

    /**
     * Calculates checksum for Part 2 - moving whole files to leftmost position in decreasing ID order
     * @param input The disk map string
     * @return The calculated checksum
     */
    fun calculateChecksumPart2(input: String): Long {
        val blocks = parseDiskMap(input)
        var positions = expandBlocks(blocks)
        
        // Process files in decreasing ID order
        val fileIds = positions.filter { it >= 0 }.distinct().sortedDescending()
        
        for (fileId in fileIds) {
            // Find the current positions of this file
            val filePositions = positions.withIndex()
                .filter { it.value == fileId }
                .map { it.index }
            
            if (filePositions.isEmpty()) continue
            
            // Calculate file size
            val fileSize = filePositions.size
            
            // Find leftmost position where this file can fit
            val leftmostSpace = findLeftmostSpace(positions, filePositions.first(), fileSize)
            
            // If we found a suitable space and it's to the left of the current position
            if (leftmostSpace >= 0 && leftmostSpace < filePositions.first()) {
                // Move the file
                val mutablePositions = positions.toMutableList()
                // Clear old positions
                filePositions.forEach { mutablePositions[it] = -1 }
                // Set new positions
                for (i in 0 until fileSize) {
                    mutablePositions[leftmostSpace + i] = fileId
                }
                positions = mutablePositions
            }
        }
        
        return calculateChecksum(positions)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val fragmenter = DiskFragmenter()
            
            // Read puzzle input
            val input = DiskFragmenter::class.java.getResourceAsStream("/2.txt")?.bufferedReader()?.readText()?.trim()
                ?: throw IllegalStateException("Could not read puzzle input file")
            
            // Solve Part 1
            val part1Answer = fragmenter.calculateChecksumPart1(input)
            println("\n==========================================")
            println("Part 1 Answer: $part1Answer")
            println("==========================================\n")
            
            // Solve Part 2
            val part2Answer = fragmenter.calculateChecksumPart2(input)
            println("\n==========================================")
            println("Part 2 Answer: $part2Answer")
            println("==========================================\n")
        }
    }
}
