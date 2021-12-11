package day1

import BaseDayObjectTest
import DayRetriever
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

class Day1Test : BaseDayObjectTest(Day1(), partOneExpected = 1139, partTwoExpected = 1103)