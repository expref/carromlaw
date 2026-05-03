package com.vjn.carromlawscompanion

import android.content.Context
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonArray

class RulesRepository(private val context: Context) {

    private var cachedRulesData: CarromRulesData? = null
    private var cachedFoulData: FoulCheckerData? = null
    private var cachedScoringScenarios: ScoringScenarios? = null

    private val json = Json { ignoreUnknownKeys = true }

    fun getRulesData(): CarromRulesData {
        cachedRulesData?.let { return it }

        val jsonString = context.assets
            .open("carrom_rules.json")
            .bufferedReader()
            .use { it.readText() }

        val data = json.decodeFromString<CarromRulesData>(jsonString)
        cachedRulesData = data
        return data
    }

    fun getSections(): List<Section> {
        return getRulesData().sections
    }

    fun getFoulCheckerData(): FoulCheckerData {
        cachedFoulData?.let { return it }

        val jsonString = context.assets
            .open("foul_checker.json")
            .bufferedReader()
            .use { it.readText() }

        val data = json.decodeFromString<FoulCheckerData>(jsonString)
        cachedFoulData = data
        return data
    }

    fun getFoulCategories(): List<FoulCategory> {
        return getFoulCheckerData().categories
    }

    fun getScoringScenarios(): ScoringScenarios {
        cachedScoringScenarios?.let { return it }

        val jsonString = context.assets
            .open("foul_checker.json")
            .bufferedReader()
            .use { it.readText() }

        // Parse the JSON to extract just the scoring_scenarios part
        val rootElement = json.parseToJsonElement(jsonString).jsonObject
        val scoringElement = rootElement["scoring_scenarios"]!!
        val scoring = json.decodeFromJsonElement(ScoringScenarios.serializer(), scoringElement)

        cachedScoringScenarios = scoring
        return scoring
    }
}