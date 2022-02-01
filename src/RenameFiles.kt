import java.io.File
import java.time.LocalDate

fun main() {
    println("renaming")
    var date = LocalDate.of(2020, 7, 3)
    val endDate = LocalDate.of (2021, 6, 16)
    while (!date.isAfter(endDate)) {
        renameFileForDate(date)
        date = date.plusDays(1)
    }
}

private fun renameFileForDate(date: LocalDate) {
  //  println("renaming " + oldFileName(date) + " to " + newFileName(date))
    var sourceFile = File(oldFileName(date))
    if (sourceFile.exists()) {
        var destFile = File(newFileName(date))
        sourceFile.renameTo(destFile)
    } else {
        println(oldFileName(date) + " does not exist - skipping")
    }
}

fun oldFileName(date: LocalDate) =
        "/home/dave/ukgovtest/latest${date.monthValue.toString().padStart(2, '0')}${date.dayOfMonth.toString()
                .padStart(2, '0')}.csv"

fun newFileName(date: LocalDate) =
        "/home/dave/ukgovtest/l${date.year.toString()}${date.monthValue.toString().padStart(2, '0')}${date.dayOfMonth.toString()
                .padStart(2, '0')}.csv"