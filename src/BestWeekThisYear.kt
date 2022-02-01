import java.time.LocalDate

fun main() {

    val fileMap = CovidFileReader.readFileToMap(CovidFileReader.fileName(today))
    val populations = CovidFileReader.extractPopulations(CovidFileReader.fileName(populationDate))

    println("Area,BestDate,WeeklyCases,WeeklyRate")
    for (areaName in fileMap.keys)
        if (areaName != null) {
            val areaMap = fileMap[areaName]
            val startOfYear = LocalDate.of(2021,1,1)
            var currentDate = today.minusDays(5)
            var bestWeeklyCount = weeklyTotal(areaMap, currentDate)
            var bestDate = currentDate

            while (currentDate.isAfter(startOfYear)) {
               val currentTotal = weeklyTotal(areaMap, currentDate)
                if (currentTotal < bestWeeklyCount) {
                    bestWeeklyCount = currentTotal
                    bestDate = currentDate
                }
                currentDate = currentDate.minusDays(1)
            }
            println("${CovidFileReader.sanitiseAreaName(areaName)},$bestDate,$bestWeeklyCount,${bestWeeklyCount.toDouble() / populations[areaName]!!}")
        }
}





