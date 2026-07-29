package com.babynumbers

import org.junit.Test
import org.junit.Assert.*

/**
 * 简单示例测试
 *
 * 用于验证测试环境配置是否正确
 */
class ExampleTest {

    @Test
    fun `addition is correct`() {
        // Given
        val a = 2
        val b = 3

        // When
        val result = a + b

        // Then
        assertEquals(5, result)
    }

    @Test
    fun `string concatenation`() {
        // Given
        val hello = "Hello"
        val world = "World"

        // When
        val result = "$hello $world"

        // Then
        assertEquals("Hello World", result)
    }

    @Test
    fun `list operations`() {
        // Given
        val numbers = listOf(1, 2, 3, 4, 5)

        // When
        val sum = numbers.sum()
        val filtered = numbers.filter { it > 3 }

        // Then
        assertEquals(15, sum)
        assertEquals(listOf(4, 5), filtered)
    }
}
