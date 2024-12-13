import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DiskFragmenterTest {
    private val fragmenter = DiskFragmenter()

    @Test
    fun `test parse disk map`() {
        val input = "12345"
        val blocks = fragmenter.parseDiskMap(input)
        assertEquals(5, blocks.size)
        assertEquals(DiskFragmenter.Block(0, 1), blocks[0]) // First file
        assertEquals(DiskFragmenter.Block(-1, 2), blocks[1]) // Space
        assertEquals(DiskFragmenter.Block(1, 3), blocks[2]) // Second file
        assertEquals(DiskFragmenter.Block(-1, 4), blocks[3]) // Space
        assertEquals(DiskFragmenter.Block(2, 5), blocks[4]) // Third file
    }

    @Test
    fun `test invalid input throws exception`() {
        assertThrows<IllegalArgumentException> {
            fragmenter.parseDiskMap("123a45")
        }
    }

    @Test
    fun `test part 1 checksum with example input`() {
        val input = "2333133121414131402"
        val checksum = fragmenter.calculateChecksumPart1(input)
        assertEquals(1928, checksum)
    }

    @Test
    fun `test part 1 checksum with large example`() {
        val input = javaClass.getResourceAsStream("/1.txt")?.bufferedReader()?.readText()?.trim()
            ?: throw IllegalStateException("Could not read 1.txt")
        val checksum = fragmenter.calculateChecksumPart1(input)
        assertEquals(6330095022244L, checksum)
    }

    @Test
    fun `test part 1 checksum with puzzle input`() {
        val input = javaClass.getResourceAsStream("/2.txt")?.bufferedReader()?.readText()?.trim()
            ?: throw IllegalStateException("Could not read 2.txt")
        val answer = fragmenter.calculateChecksumPart1(input)
        println("==========================================")
        println("Part 1 Answer: $answer")
        println("==========================================")
        assertTrue(answer > 0)
    }

    @Test
    fun `test part 2 checksum with example input`() {
        val input = "2333133121414131402"
        val checksum = fragmenter.calculateChecksumPart2(input)
        assertEquals(2858, checksum)
    }

    @Test
    fun `test part 2 checksum with puzzle input`() {
        val input = javaClass.getResourceAsStream("/2.txt")?.bufferedReader()?.readText()?.trim()
            ?: throw IllegalStateException("Could not read 2.txt")
        val answer = fragmenter.calculateChecksumPart2(input)
        println("\n==========================================")
        println("Part 2 Answer: $answer")
        println("==========================================\n")
        assertTrue(answer > 0)
    }
}
