package com.vjn.carromlawscompanion

import kotlinx.serialization.Serializable

@Serializable
data class CarromRulesData(
    val metadata: Metadata,
    val sections: List<Section>
)

@Serializable
data class Metadata(
    val title: String,
    val publisher: String,
    val year_adopted: Int,
    val total_sections: Int,
    val total_rules: Int
)

@Serializable
data class Section(
    val id: String,
    val number: Int,
    val title: String,
    val description: String,
    val rules: List<Rule>
)

@Serializable
data class Rule(
    val id: String,
    val title: String,
    val text: String,
    val tags: List<String> = emptyList()
)

@Serializable
data class FoulCheckerData(
    val metadata: FoulMetadata,
    val categories: List<FoulCategory>
)

@Serializable
data class FoulMetadata(
    val title: String,
    val description: String,
    val version: String
)

@Serializable
data class FoulCategory(
    val id: String,
    val title: String,
    val icon: String,
    val scenarios: List<FoulScenario>
)

@Serializable
data class FoulScenario(
    val id: String,
    val question: String,
    val consequence: String,
    val details: String,
    val rule_refs: List<String>
)
@Serializable
data class ScoringScenarios(
    val title: String,
    val description: String,
    val scenarios: List<ScoringScenario>
)

@Serializable
data class ScoringScenario(
    val id: String,
    val situation: String,
    val proper_stroke: String,
    val improper_stroke: String,
    val rule_refs: List<String>
)
