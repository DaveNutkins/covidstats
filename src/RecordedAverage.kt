import java.time.LocalDate

fun main() {

    val endDate = LocalDate.of(2020, 9, 24)
    val endFileMap = CovidFileReader.readFileToMap(CovidFileReader.fileName(endDate))
    val startDate = endDate.minusDays(7)
    val oldFileMap = CovidFileReader.readFileToMap(CovidFileReader.fileName(startDate))
    val populations = CovidFileReader.extractPopulations(CovidFileReader.fileName(populationDate))
    println("Area,Region,Weekly Cases,Rate")
    for (areaName in oldFileMap.keys) {
      val  difference = calculateChangeForArea(areaName, oldFileMap, endFileMap, endDate)
      val region = areaName?.let { LocalAuthorities.region(it) }
      println("${CovidFileReader.sanitiseAreaName(areaName)},$region,$difference,${difference/ populations[areaName]!!}")
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
     var specimenDate = LocalDate.of(2020, 3, 1);
     var difference = 0
     while (specimenDate.isBefore(lastDate)) {
         val oldCount = oldMap?.get(specimenDate) ?: 0
         val newCount = newMap?.get(specimenDate) ?: 0
         difference += newCount - oldCount
         specimenDate = specimenDate.plusDays(1)
     }
     return difference
 }

