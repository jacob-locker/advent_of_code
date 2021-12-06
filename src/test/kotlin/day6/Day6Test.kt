package day6

import BaseDayTest

class Day6Test : BaseDayTest<Long>(
    6,
    partOneMethod = ::getNumberOfLanternFish,
    partOneSimpleExpected = 5934,
    partOneExpected = 353079,
    partTwoMethod = { getNumberOfLanternFish(it, numberOfDays = 256) },
    partTwoSimpleExpected = 26984457539,
    partTwoExpected = 1605400130036
)