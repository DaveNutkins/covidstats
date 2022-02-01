
import java.time.LocalDate

fun main() {

    val fileMap = CovidFileReader.readFileToMap(CovidFileReader.fileName(today))
    val recentDate = today.minusDays(3)

    println("Area,Region,LastGettingWorseDate")
    for (areaName in fileMap.keys) {
        if (areaName != null) {
            val areaMap = fileMap[areaName]
            var currentDate = recentDate
            var currentTotal = weeklyTotal(areaMap, currentDate)
            currentDate = currentDate.minusDays(1)
            var previousTotal = weeklyTotal(areaMap, currentDate)
            while (previousTotal>=currentTotal && currentDate.isAfter(baseDate)) {
                currentDate = currentDate.minusDays(1)
                currentTotal = previousTotal
                previousTotal = weeklyTotal(areaMap, currentDate)
            }
            println("${CovidFileReader.sanitiseAreaName(areaName)},${LocalAuthorities.region(areaName)},${currentDate.plusDays(1)}")
        }

    }
    }




