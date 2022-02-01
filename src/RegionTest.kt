import java.time.LocalDate


    fun main() {
        val populations = CovidFileReader.extractPopulations(CovidFileReader.fileName(LocalDate.of(2021,4, 29)))
        val regions = Regions(populations)
        println(regions.population("London"))
        println(regions.population("South East"))
        println(regions.population("North East"))
        println(regions.population("North West"))
        println(regions.population("Scotland"))
        println(regions.population("Northern Ireland"))
        println(regions.population("Wales"))

    }

