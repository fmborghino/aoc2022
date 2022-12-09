@file:Suppress("unused")

import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("res", name)
    .readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * Generate Pair<Int, Int> for range N x N where N is 0 to (n-1)
 * Handy for generating keys for a square grid, say.
 * `generatePairs(2).joinToString() // (0, 0), (0, 1), (1, 0), (1, 1)`
 */
fun generatePairs(n: Int) = sequence {
    (0 until n).forEach { x ->
        (0 until n).forEach { y ->
            yield(Pair(x, y))
        }
    }
}