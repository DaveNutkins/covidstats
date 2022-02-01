fun main() {
        val weeklyMap = WeeklyFileReader.readFileToMap("/home/dave/Documents/elstow.csv")
        val lastWeeklyDate =  weeklyMap.keys.max()!!

        val dailyMap = DailyImplier.implyDailyCases(weeklyMap)
        var sampleDate = weeklyMap.keys.min()!!.minusDays(6)
        while (!sampleDate!!.isAfter(lastWeeklyDate)) {
            val count = dailyMap?.get(sampleDate) ?: 0
            println("$sampleDate,$count")
            sampleDate = sampleDate?.plusDays(1)
        }
    }


