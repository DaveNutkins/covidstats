import java.time.LocalDate

class DailyImplier {

    companion object {

         fun implyDailyCases(weeklyMap: Map<LocalDate, Int>): Map<LocalDate, Int> {
            val firstWeeklyDate = weeklyMap.keys.min()!!
            var dailyMap = trySinglePoints(firstWeeklyDate, weeklyMap)
            if (dailyMap.isEmpty()) {
                dailyMap = try2Points(firstWeeklyDate, weeklyMap)
            }
             return dailyMap
        }
        private fun implyMap(seedMap: Map<LocalDate, Int>, weeklyMap: Map<LocalDate, Int>): Map<LocalDate, Int> {
            val resultMap = mutableMapOf<LocalDate, Int>()
            resultMap.putAll(seedMap)
            val lastDate = weeklyMap.keys.max()
            var currentDate = weeklyMap.keys.min()!!
            while (!currentDate.isAfter(lastDate)) {
                val newDaily = weeklyMap[currentDate]!! - partialWeeklyTotal(resultMap, currentDate)
                if (newDaily < 0 ) {
                    println(resultMap)
                    return mutableMapOf<LocalDate, Int>()
                }
                resultMap [currentDate]  = newDaily
                currentDate = currentDate.plusDays(1)
            }
            println(resultMap)
            return resultMap
        }
        private fun partialWeeklyTotal (map : Map<LocalDate,Int>, endDate : LocalDate) : Int {
            var sampleDate = endDate.minusDays( 6)
            var weekly = 0
            while (sampleDate.isBefore(endDate)) {
                weekly += map[sampleDate] ?: 0
                sampleDate = sampleDate.plusDays(1)
            }
            return weekly
        }
        private fun trySinglePointMap(pointDate: LocalDate, weeklyMap: Map<LocalDate, Int>): Map<LocalDate, Int> {
            val seedMap = mutableMapOf<LocalDate, Int>()
            seedMap[pointDate] = 2
            return implyMap(seedMap, weeklyMap)
        }

        private fun trySinglePoints(firstWeeklyDate: LocalDate, weeklyMap: Map<LocalDate, Int>) : Map<LocalDate,Int> {
            for (daysBefore in 6 downTo 0) {
                val baseDate = firstWeeklyDate.minusDays(daysBefore.toLong())
                val dailyMap = trySinglePointMap(baseDate, weeklyMap)
                if (dailyMap.isNotEmpty()) {
                    return dailyMap
                }
            }
            return mutableMapOf()
        }

        private fun try2Points(firstWeeklyDate: LocalDate, weeklyMap: Map<LocalDate, Int>) : Map<LocalDate,Int> {
            for (daysBefore1 in 6 downTo 1) {
                val pointDate1 = firstWeeklyDate.minusDays(daysBefore1.toLong())
                for (daysBefore2 in daysBefore1 - 1 downTo 0) {
                    val pointDate2 = firstWeeklyDate.minusDays(daysBefore2.toLong())
                    val dailyMap = try2PointMap(pointDate1, pointDate2, weeklyMap)
                    if (dailyMap.isNotEmpty()) {
                        return dailyMap
                    }
                }
            }
            return mutableMapOf()
        }

        private fun try2PointMap(pointDate1: LocalDate, pointDate2: LocalDate, weeklyMap: Map<LocalDate, Int>): Map<LocalDate, Int> {
            val seedMap = mutableMapOf<LocalDate, Int>()
            seedMap[pointDate1] = 1
            seedMap[pointDate2] = 1
            return implyMap(seedMap, weeklyMap)
        }
    }
}

