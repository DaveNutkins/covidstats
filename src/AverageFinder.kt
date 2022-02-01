import java.time.LocalDate

val today = LocalDate.of(2022, 1, 30)!!
val baseDate = LocalDate.of(2020, 3, 1)!!
val populationDate = LocalDate.of(2021, 10, 22)!!

fun main() {

    val fileMap = CovidFileReader.readFileToMap(CovidFileReader.fileName(today))
    //  val populations = CovidFileReader.extractPopulations(CovidFileReader.fileName(populationDate))
    val recentDate = today.minusDays(5)
    val previousWeek = recentDate.minusWeeks(1)
    val week2previous = recentDate.minusWeeks(2)
    val week3previous = recentDate.minusWeeks(3)
    var areaCount = 0
    println("Area,Weekly Cases,Rate")
    for (areaName in fileMap.keys) {
        if (areaName != null) {
            val areaMap = fileMap[areaName]
            val currentTotal = weeklyTotal(areaMap, recentDate)
            val previousTotal = weeklyTotal(areaMap, previousWeek)
            val previous2Total = weeklyTotal(areaMap, week2previous)
            val previous3Total = weeklyTotal(areaMap, week3previous)
            val weeklyIncrease = currentTotal - previousTotal
            //               println("${CovidFileReader.sanitiseAreaName(areaName)},$currentTotal,${currentTotal.toDouble()/ populations[areaName]!!}")
            if (isNowGettingWorse(previousTotal, previous2Total, previous3Total, weeklyIncrease)) {
                println("${CovidFileReader.sanitiseAreaName(areaName)},$previous3Total,$previous2Total,$previousTotal,$currentTotal")
                areaCount++
            }
        }
    }
    println(areaCount)
}

private fun isAllGettingBetter(
    previousTotal: Int,
    previous2Total: Int,
    previous3Total: Int,
    weeklyIncrease: Int
): Boolean = weeklyIncrease <= 0  && previous2Total in previousTotal..previous3Total

private fun isAllGettingWorse(
    previousTotal: Int,
    previous2Total: Int,
    previous3Total: Int,
    weeklyIncrease: Int
): Boolean = weeklyIncrease >= 0  && previous2Total in previous3Total..previousTotal

private fun isNowGettingBetter(
    previousTotal: Int,
    previous2Total: Int,
    previous3Total: Int,
    weeklyIncrease: Int
) :Boolean = weeklyIncrease <= 0  && previous2Total in previous3Total..previousTotal

private fun isNowGettingWorse(
    previousTotal: Int,
    previous2Total: Int,
    previous3Total: Int,
    weeklyIncrease: Int
) :Boolean = weeklyIncrease >= 0  && previous2Total in previousTotal..previous3Total

fun weeklyTotal(areaMap : MutableMap<LocalDate, Int>?, averageDate: LocalDate) : Int {
    var total  = 0
    var sampleDate = averageDate.minusDays(6)
    while (!sampleDate.isAfter(averageDate)) {
        total += areaMap[sampleDate] ?: 0
        sampleDate = sampleDate.plusDays(1)
    }
    return total
}

fun addToTotal(totalMap : MutableMap<String,Int>, areaName: String, extra: Int) {
    val region = LocalAuthorities.region(areaName)
    if (region !=null) {
        if (!totalMap.containsKey(region)) {
            totalMap[region] = extra
        } else {
            totalMap[region] = totalMap[region]?.plus(extra)!!
        }
    }
}

fun displayRegionalTotals(totalMap: MutableMap<String, Int>, regions: Regions) {
    for (region in totalMap.keys) {
        val current = totalMap[region]
        val rate = current!! / regions.population(region)!!
        println("$region,$current,$rate")
    }
}
