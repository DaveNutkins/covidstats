import java.time.LocalDate

fun main() {

    val fileMap = CovidFileReader.readFileToMap(CovidFileReader.fileName(today))
    val populations = CovidFileReader.extractPopulations(CovidFileReader.fileName(populationDate))

    println("Area,WorstWeeklyDate,WeeklyCases,WeeklyRate,WorstDate,DailyCases,DailyRate")
    for (areaName in fileMap.keys)
        if (areaName != null) {
            val areaMap = fileMap[areaName]
            var currentDate = baseDate
            var worstWeeklyDate = currentDate
            var worstWeeklyCount = 0
            var worstDate = currentDate
            var worstDailyCount = 0
            while (!currentDate.isAfter(today)) {
                val currentDay = areaMap?.get(currentDate)
                if (currentDay != null) {
                    if (currentDay > worstDailyCount) {
                        worstDailyCount = currentDay
                        worstDate = currentDate
                    }
                }
                val currentTotal = weeklyTotal(areaMap, currentDate)
                if (currentTotal > worstWeeklyCount) {
                    worstWeeklyCount = currentTotal
                    worstWeeklyDate = currentDate
                }

                currentDate = currentDate.plusDays(1)
            }
            println("${CovidFileReader.sanitiseAreaName(areaName)},$worstWeeklyDate,$worstWeeklyCount,${worstWeeklyCount.toDouble() / populations[areaName]!!},$worstDate,$worstDailyCount,${worstDailyCount.toDouble() / populations[areaName]!!} ")
    }
}





