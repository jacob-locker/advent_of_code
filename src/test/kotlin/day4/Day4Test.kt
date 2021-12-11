package day4

import BaseDayObjectTest
import DayRetriever
import org.junit.Test
import kotlin.test.assertEquals

class Day4Test : BaseDayObjectTest(
    Day4(),
    partOneSimpleExpected = 4512,
    partOneExpected = 28082,
    partTwoSimpleExpected = 1924,
    partTwoExpected = 8224
)