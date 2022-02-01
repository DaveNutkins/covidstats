import java.time.temporal.ChronoUnit


fun main() {
    val oldFileMap = CovidFileReader.readFileToMap(CovidFileReader.fileName(today.minusDays(1)))
    val newFileMap = CovidFileReader.readFileToMap(CovidFileReader.fileName(today))
    val delays = IntArray(1000)
    for (areaName in oldFileMap.keys) {
        val oldMap = oldFileMap[areaName]
        val newMap = newFileMap[areaName]
        var specimenDate = baseDate;
        while (specimenDate.isBefore(today)) {
            val oldCount = oldMap?.get(specimenDate) ?: 0
            val newCount = newMap?.get(specimenDate) ?: 0
            val difference = newCount - oldCount
            if (difference < 0) {
                val dateDifference = ChronoUnit.DAYS.between(specimenDate, today).toInt()
                delays[dateDifference] += difference
            }
            specimenDate = specimenDate.plusDays(1)
        }
    }

    val totalNewCases = delays.sum()
    var cumulativeCases = 0
    delays.forEachIndexed { i, delay ->
        if (delay < 0) {
            cumulativeCases += delay
            println("$i,$delay,${delay * 100.0 / totalNewCases},${cumulativeCases * 100.0 / totalNewCases},${totalNewCases - cumulativeCases}")
        }
    }
}

