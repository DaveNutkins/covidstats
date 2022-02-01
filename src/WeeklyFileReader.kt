import java.io.File
import java.time.LocalDate
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.FileNotFoundException
import java.io.FileReader

class WeeklyFileReader {

    companion object {
        fun readFileToMap(fileName: String): MutableMap<LocalDate, Int> {
            var dateMap = mutableMapOf<LocalDate, Int>()
            processCsvLineByLine(File(fileName)) { row ->
                val date = LocalDate.parse(row["Date"])
                val weeklyCases = row["Weekly"]?.toInt() ?: 0
                dateMap[date] = weeklyCases
            }
            return dateMap
        }

        private fun processCsvLineByLine(csv: File, processor: (Map<String, String>) -> Unit) {
            try {
                val fileReader = FileReader(csv)
                val parser = CSVParser(fileReader, CSVFormat.EXCEL.withHeader())
                for (record in parser) {
                    processor(record.toMap())
                }
                parser.close()
                fileReader.close()
            } catch (e: FileNotFoundException) {
                // ignore empty file
            }
        }
    }
}