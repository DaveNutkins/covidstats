import java.time.LocalDate

fun main() {

    val fileMap = CovidFileReader.readFileToMap(CovidFileReader.fileName(today))
    val recentDate = today.minusDays(5)

    println("Area,Region,LastBetterDate")
    for (areaName in fileMap.keys) {
        if (areaName != null) {
            val areaMap = fileMap[areaName]
            val currentTotal = weeklyTotal(areaMap, recentDate)
            var currentDate = recentDate.minusDays(5)
            var total = currentTotal
            while (total>=currentTotal && currentDate.isAfter(baseDate)) {
                total = weeklyTotal(areaMap, currentDate)
                currentDate = currentDate.minusDays(1)
            }
            println("${CovidFileReader.sanitiseAreaName(areaName)},${LocalAuthorities.region(areaName)},${currentDate.plusDays(1)}")
        }

    }
}


