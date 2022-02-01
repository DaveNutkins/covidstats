import java.time.LocalDate

fun main() {

    val endFileMap = CovidFileReader.readFileToMap(CovidFileReader.fileName(today))
    val weekAgoMap = CovidFileReader.readFileToMap(CovidFileReader.fileName(today.minusDays(7)))
    val fortnightAgoMap = CovidFileReader.readFileToMap(CovidFileReader.fileName(today.minusDays(14)))

    val populations = CovidFileReader.extractPopulations(CovidFileReader.fileName(populationDate))
    val lastWeekTotalMyRegion = mutableMapOf<String, Int>()
    println("Area,Region,Increase Percentage,Last Weekly Cases,Rate,Population")
    for (areaName in weekAgoMap.keys) {
        val lastWeek = calculateChangeForArea(areaName, weekAgoMap, endFileMap, today)
        val previousWeek = calculateChangeForArea(areaName, fortnightAgoMap, weekAgoMap, today)
        val increasePercent = lastWeek * 100.0  /previousWeek - 100.0
        val region = areaName?.let { LocalAuthorities.region(it) }
        if (region !=null) {
            if (!lastWeekTotalMyRegion.containsKey(region)) {
                lastWeekTotalMyRegion[region] = lastWeek
            } else {
                lastWeekTotalMyRegion[region] = lastWeekTotalMyRegion[region]?.plus(lastWeek)!!
            }
        }
        val population = populations[areaName] ?: 0.0
        println("${CovidFileReader.sanitiseAreaName(areaName)},$region,$increasePercent,$lastWeek,${lastWeek/population},$population")
    }
    val regions = Regions(populations)
    for (region in lastWeekTotalMyRegion.keys) {
          val current = lastWeekTotalMyRegion[region]
          val rate = current!! / regions.population(region)!!
          println("$region,$current,$rate")
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
    var specimenDate = baseDate;
    var difference = 0
    while (specimenDate.isBefore(lastDate)) {
        val oldCount = oldMap?.get(specimenDate) ?: 0
        val newCount = newMap?.get(specimenDate) ?: 0
        difference += newCount - oldCount
        specimenDate = specimenDate.plusDays(1)
    }
    return difference
}

