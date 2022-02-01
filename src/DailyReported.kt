import java.time.LocalDate

fun main() {

    val endFileMap = CovidFileReader.readFileToMap(CovidFileReader.fileName(today))
    val previousFileMap = CovidFileReader.readFileToMap(CovidFileReader.fileName(today.minusDays(1)))
    val populations = CovidFileReader.extractPopulations(CovidFileReader.fileName(today))
    println("Area,Region,ReportedToday,Rate")
    for (areaName in previousFileMap.keys) {
        val todays = calculateChangeForArea(areaName, previousFileMap, endFileMap, today)
        val region = areaName?.let { LocalAuthorities.region(it) }
        val population = populations[areaName] ?: 1.0
        val rate = todays / population
        println("${CovidFileReader.sanitiseAreaName(areaName)},$region,$todays,$rate")
    }
}

private fun calculateChangeForArea(
    areaName: String?,
    oldFileMap: MutableMap<String?, MutableMap<LocalDate, Int>>,
    newFileMap: MutableMap<String?, MutableMap<LocalDate, Int>>,
    lastDate : LocalDate
) : Int {
    val oldMap = oldFileMap[areaName]
    val newMap = newFileMap[areaName]
    var specimenDate = baseDate
    var difference = 0
    while (specimenDate.isBefore(lastDate)) {
        val oldCount = oldMap?.get(specimenDate) ?: 0
        val newCount = newMap?.get(specimenDate) ?: 0
        difference += newCount - oldCount
        specimenDate = specimenDate.plusDays(1)
    }
    return difference
}

