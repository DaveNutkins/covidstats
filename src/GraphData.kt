import java.time.LocalDate

fun main(args:Array<String>) {
    val areaName = args[0]
    val fileMap = CovidFileReader.readFileToMap(CovidFileReader.fileName(today))
    val areaMap = fileMap[areaName]
    val populations = CovidFileReader.extractPopulations(CovidFileReader.fileName(populationDate))
    println("$areaName,${populations[areaName]}")

    val firstDate = baseDate
    val lastDate = today
    var sampleDate = firstDate
    while (sampleDate!!.isBefore(lastDate)) {
        val count = areaMap?.get(sampleDate) ?: 0
        println("$sampleDate,$count")
        sampleDate = sampleDate?.plusDays(1)
    }


}

