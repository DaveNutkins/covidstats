import java.time.LocalDate

fun main(args: Array<String>) {
    val oldFileMap = CovidFileReader.readFileToMap(CovidFileReader.fileName(today.minusDays(1)))
    val newFileMap = CovidFileReader.readFileToMap(CovidFileReader.fileName(today))
    println("Area Name,Specimen Date,Increase,Cases")
    val targetAreas = setOf("Bedford", "Central Bedfordshire", "Luton", "Milton Keynes"/*,"Hackney and City of London" */)
    // was oldFileMap.keys
    for (areaName in targetAreas) {
        showDifferencesForArea(areaName, oldFileMap, newFileMap, today)
    }
//    println("Area Name,Increase")
//    for (areaName in  oldFileMap.keys) {
//        showTotalChangeForArea(areaName, oldFileMap, newFileMap, today)
//    }

//    val populations = CovidFileReader.extractPopulations(CovidFileReader.fileName(today))
//    for (area in populations.keys) {
//        println("$area,${populations[area]}")
//    }

}

private fun showDifferencesForArea(
    areaName: String?,
    oldFileMap: MutableMap<String?, MutableMap<LocalDate, Int>>,
    newFileMap: MutableMap<String?, MutableMap<LocalDate, Int>>,
    lastDate: LocalDate?
) {
    val oldMap = oldFileMap[areaName]
    val newMap = newFileMap[areaName]
    var specimenDate = LocalDate.of(2020, 3, 1)
    while (specimenDate.isBefore(lastDate)) {
        val oldCount = oldMap?.get(specimenDate) ?: 0
        val newCount = newMap?.get(specimenDate) ?: 0
        val difference = newCount - oldCount
        if (difference != 0) {
            println("${CovidFileReader.sanitiseAreaName(areaName)},$specimenDate,$difference,$newCount")
        }
        specimenDate = specimenDate.plusDays(1)
    }
}

private fun showTotalChangeForArea(
    areaName: String?,
    oldFileMap: MutableMap<String?, MutableMap<LocalDate, Int>>,
    newFileMap: MutableMap<String?, MutableMap<LocalDate, Int>>,
    lastDate: LocalDate?
) {
    val oldMap = oldFileMap[areaName]
    val newMap = newFileMap[areaName]
    var specimenDate = LocalDate.of(2020, 3, 1)
    var difference = 0
    while (specimenDate.isBefore(lastDate)) {
        val oldCount = oldMap?.get(specimenDate) ?: 0
        val newCount = newMap?.get(specimenDate) ?: 0
        difference += newCount - oldCount
        specimenDate = specimenDate.plusDays(1)
    }
   if (difference != 0) {
        println("${CovidFileReader.sanitiseAreaName(areaName)},$difference")
   }
}