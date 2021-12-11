package day3

import BaseDayObjectTest
import DayRetriever
import org.junit.Test
import kotlin.test.assertEquals

class Day3Test : BaseDayObjectTest(
    Day3(),
    partOneSimpleExpected = 198,
    partOneExpected = 3277364,
    partTwoSimpleExpected = 230,
    partTwoExpected = 5736383
)