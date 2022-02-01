import java.io.File
import java.time.LocalDate
import java.time.temporal.ChronoUnit

fun main() {
    val startDate = LocalDate.of(2020, 7 , 2)
    val endDate = LocalDate.of(2020, 8 , 28)
    val entries = (ChronoUnit.DAYS.between(startDate, endDate)+1).toInt()
    val result : MutableMap<String?, MutableMap<LocalDate, IntArray>> = mutableMapOf<String?, MutableMap<LocalDate, IntArray>>()

    for (index in 0..entries-1) {
        val fileDate = startDate.plusDays(index.toLong())
        val fileMap = CovidFileReader.readFileToMap(CovidFileReader.fileName(fileDate))
        for (areaName in fileMap.keys) {
            val areaMap =  fileMap[areaName]
            var resultAreaMap = result[areaName]
            if (resultAreaMap == null) {
                resultAreaMap = mutableMapOf<LocalDate,IntArray>()
                result[areaName] = resultAreaMap
            }
            for (specimenDate in areaMap?.keys!!) {
                var countArray = resultAreaMap[specimenDate]
                if (countArray == null) {
                    countArray = IntArray(entries)
                    resultAreaMap[specimenDate] = countArray
                }
                countArray[index] = areaMap[specimenDate]!!
            }
        }
    }
    val writer = File("/home/dave/changescovid${endDate}.csv").writer()
    writer.write("Area,Specimen Date")
    for (index in 0..entries-1) {
        val fileDate =startDate.plusDays(index.toLong())
        writer.write(",$fileDate")
    }
    writer.write("\n")
    for(areaName in result.keys) {
        val areaMap = result[areaName]
        for (specimenDate in areaMap?.keys!!) {
            val countArray = areaMap.get(specimenDate)
            if (countArray != null) {
                if (!isArrayIncreasing(countArray)) {
                    writer.write("${CovidFileReader.sanitiseAreaName(areaName)},$specimenDate")
                    for (value in countArray) {
                        writer.write(",$value")
                    }
                    writer.write("\n")
                }
            }
        }
    }
    writer.close()

}

fun isArrayTheSame(a: IntArray): Boolean {
    for (v in a) {
        if (v != a[0]) {
            return false
        }
    }
    return true
}

fun isArrayIncreasing(a: IntArray) : Boolean {
    for (i in 0..a.lastIndex) {
        if (i>0)
            if (a[i]<a[i-1]){
                return false
            }
    }
    return true
}