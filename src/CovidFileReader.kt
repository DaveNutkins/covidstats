import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.time.LocalDate

class CovidFileReader {

    companion object {

        fun readFileToMap(fileName: String): MutableMap<String?, MutableMap<LocalDate, Int>> {
            val areaMap: MutableMap<String?, MutableMap<LocalDate, Int>> =
                mutableMapOf<String?, MutableMap<LocalDate, Int>>()
            processCsvLineByLine(File(fileName)) { row ->
                if (isLowerTier(row)) {
                    val areaName = row["Area name"] ?: row["areaName"]
                    if (areaName != null) {

                        val specimenDate = LocalDate.parse(row["Specimen date"] ?: row["date"])
                        val dailyCasesAsString = row["Daily lab-confirmed cases"] ?: row["newCasesBySpecimenDate"]
                        var dailyCasesAsFloat = 0.0f
                        if (dailyCasesAsString != null && !dailyCasesAsString.isNullOrBlank()) {
                            dailyCasesAsFloat = dailyCasesAsString.toFloat()
                        }
                        val dailyCases = dailyCasesAsFloat.toInt()

                        var dateMap: MutableMap<LocalDate, Int>? = areaMap[areaName]
                        if (dateMap == null) {
                            dateMap = mutableMapOf<LocalDate, Int>()
                            areaMap[areaName] = dateMap
                        }

                        dateMap[specimenDate] = dailyCases
                    }
                }
            }
            return areaMap
        }

        fun extractPopulations(fileName: String): MutableMap<String?, Double> {
            val resultMap = mutableMapOf<String?, Double>()
            processCsvLineByLine(File(fileName)) { row ->
                if (isLowerTier(row)) {
                    val areaName = row["Area name"] ?: row["areaName"]
                    if (!resultMap.containsKey(areaName)){
                        val rateAsString = row["Cumulative lab-confirmed cases rate"]  ?: row["cumCasesBySpecimenDateRate"]
                        var rateAsDouble = 0.0
                        if (rateAsString != null && !rateAsString.isNullOrBlank()) {
                            rateAsDouble = rateAsString.toDouble()
                        }
                        val casesAsString = row["Cumulative lab-confirmed cases"] ?: row["cumCasesBySpecimenDate"]
                        var casesAsDouble = 0.0
                        if (casesAsString != null && !casesAsString.isNullOrBlank()) {
                            casesAsDouble = casesAsString.toDouble()
                        }
                        if (casesAsDouble!= 0.0 && rateAsDouble!= 0.0) {
                            val population =  casesAsDouble / rateAsDouble
                            resultMap[areaName] = population
                        }
                    }
                }
            }
            return resultMap
        }

        private fun isLowerTier(row: Map<String, String>) =
                row["Area type"].equals("Lower tier local authority") || row["Area type"].equals("ltla") || row["areaType"].equals("ltla")

        fun processCsvLineByLine(csv: File, processor: (Map<String, String>) -> Unit) {
            try {
                val fileReader = FileReader(csv)
                val parser = CSVParser(fileReader, CSVFormat.EXCEL.withHeader())
                for ( record in parser) {
                    processor(record.toMap())
                }
                parser.close()
                fileReader.close()}
            catch ( e: FileNotFoundException) {
                // ignore empty file
            }
        }

        fun fileName(date: LocalDate) =
                "/home/dave/ukgov/l${date.year.toString()}${date.monthValue.toString().padStart(2, '0')}${date.dayOfMonth.toString()
                        .padStart(2, '0')}.csv"


        fun sanitiseAreaName(raw : String?): String? = raw?.replace(",","")

    }

}