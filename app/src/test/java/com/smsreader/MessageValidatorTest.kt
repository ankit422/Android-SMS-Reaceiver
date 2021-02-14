package com.smsreader

import junit.framework.TestCase
import org.junit.Test
import java.util.regex.Pattern


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class MessageValidatorTest : TestCase() {

    val testString = "This Message contains a Date: 20-02-2021 and price: $233 for something."

    @Test
    fun test_validate_regex1() {
        var p: Pattern = Pattern.compile("\\d{2}-\\d{2}-\\d{4}")
        var m = p.matcher(testString)
        assertTrue("Found Match", m.find())
        assertEquals("20-02-2021", m.group(0))
    }

    @Test
    fun test_validate_regex2() {
        var p: Pattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}")
        var m = p.matcher(testString)
        assertFalse("Found Not Match", m.find())
    }

    @Test
    fun test_validate_regex3() {
        /* Make sure matches() doesn't change after calls to find() */
        var p: Pattern = Pattern.compile("\\$(\\d*)")
        var m = p.matcher(testString)
        assertTrue("Found Match", m.find())
        assertEquals("$233", m.group(0))
    }

}

