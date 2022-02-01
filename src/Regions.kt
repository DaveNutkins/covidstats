class Regions (authorityToPopulation: MutableMap<String?, Double> ) {
    var regionToPopulation = mutableMapOf<String,Double>()
    init {
        for (authority in authorityToPopulation.keys) {
            val population : Double = authorityToPopulation[authority]!!
            val region = authority?.let { LocalAuthorities.region(it) }
            if (region!=null) {
                val existingPopulation = regionToPopulation[region] ?: 0.0
                regionToPopulation[region] = existingPopulation + population
            }
        }
    }
    fun population(region:String) : Double? = regionToPopulation[region]
}