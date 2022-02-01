import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader


object LocalAuthorities {
    private val authorityToRegion = mutableMapOf<String?, String?>()


    private const val fileName = "/home/dave/ukgov/District_to_Region.csv"

    init {
        processCsvLineByLine(File(fileName))
        { row ->
            authorityToRegion[row["LAD19NM"]] = row["RGN19NM"]
        }
    }

    fun region(authority: String) : String? {
        var result = authorityToRegion[authority]
        if (result==null && authority.contains(" and ")) {
            val firstAuthority = authority.substring(0,authority.indexOf(" and "))
            result = authorityToRegion[firstAuthority]
            authorityToRegion[authority] = result
        }
        return result
    }

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
}