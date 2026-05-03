package com.vjn.carromlawscompanion

import android.content.Context

/**
 * Generates quiz questions from rules data and tracks stats and high scores.
 */
class QuizManager(
    private val context: Context,
    private val repository: RulesRepository
) {

    private val prefs = context.getSharedPreferences("carrom_quiz", Context.MODE_PRIVATE)

    private val highScoreKey = "high_score"
    private val totalQuizzesKey = "total_quizzes"
    private val totalQuestionsKey = "total_questions"
    private val totalCorrectKey = "total_correct"

    fun getHighScore(): Int = prefs.getInt(highScoreKey, 0)

    fun getTotalQuizzes(): Int = prefs.getInt(totalQuizzesKey, 0)

    fun getTotalQuestions(): Int = prefs.getInt(totalQuestionsKey, 0)

    fun getTotalCorrect(): Int = prefs.getInt(totalCorrectKey, 0)

    fun getAccuracyPercentage(): Int {
        val total = getTotalQuestions()
        if (total == 0) return 0
        return (getTotalCorrect() * 100) / total
    }

    /**
     * Saves results from a completed quiz - updates all stats.
     */
    fun saveQuizResult(score: Int, totalQuestions: Int) {
        val editor = prefs.edit()

        // Update high score if needed
        if (score > getHighScore()) {
            editor.putInt(highScoreKey, score)
        }

        // Update cumulative stats
        editor.putInt(totalQuizzesKey, getTotalQuizzes() + 1)
        editor.putInt(totalQuestionsKey, getTotalQuestions() + totalQuestions)
        editor.putInt(totalCorrectKey, getTotalCorrect() + score)

        editor.apply()
    }

    /**
     * Reset all stats (for "clear stats" feature if needed later).
     */
    fun resetStats() {
        prefs.edit().clear().apply()
    }

    /**
     * Generate a list of quiz questions.
     */
    fun generateQuestions(count: Int = 10): List<QuizQuestion> {
        val sections = repository.getSections()
        val allRules = sections.flatMap { section ->
            section.rules.map { rule -> RuleWithSection(rule, section) }
        }

        val goodRules = allRules.filter { it.rule.text.length >= 50 }

        val questionPool = mutableListOf<QuizQuestion>()

        // Type 1: "Which section does this rule belong to?"
        goodRules.forEach { ruleWithSection ->
            val correctSection = ruleWithSection.section
            val wrongSections = sections.filter { it.id != correctSection.id }.shuffled().take(3)
            val options = (listOf(correctSection) + wrongSections).shuffled()

            questionPool.add(
                QuizQuestion(
                    questionText = "Which section contains this rule?\n\n\"${ruleWithSection.rule.title}\"",
                    options = options.map { "${it.id}. ${it.title}" },
                    correctAnswerIndex = options.indexOf(correctSection),
                    explanation = "Rule ${ruleWithSection.rule.id} (${ruleWithSection.rule.title}) is in Section ${correctSection.id}: ${correctSection.title}",
                    ruleRef = ruleWithSection.rule.id
                )
            )
        }

        // Type 2: "What is rule X about?"
        goodRules.forEach { ruleWithSection ->
            val correctRule = ruleWithSection.rule
            val wrongRules = goodRules
                .filter { it.rule.id != correctRule.id }
                .shuffled()
                .take(3)
                .map { it.rule }

            val options = (listOf(correctRule) + wrongRules).shuffled()

            questionPool.add(
                QuizQuestion(
                    questionText = "Which of these is the title of Rule ${correctRule.id}?",
                    options = options.map { it.title },
                    correctAnswerIndex = options.indexOf(correctRule),
                    explanation = "Rule ${correctRule.id} is titled \"${correctRule.title}\". ${correctRule.text.take(150)}${if (correctRule.text.length > 150) "..." else ""}".expandAbbreviations(),
                    ruleRef = correctRule.id
                )
            )
        }

        // Type 3: Foul category questions
        try {
            val foulCategories = repository.getFoulCategories()
            foulCategories.forEach { category ->
                category.scenarios.forEach { scenario ->
                    val wrongCategories = foulCategories
                        .filter { it.id != category.id }
                        .shuffled()
                        .take(3)

                    val options = (listOf(category) + wrongCategories).shuffled()

                    questionPool.add(
                        QuizQuestion(
                            questionText = "What category does this foul belong to?\n\n\"${scenario.question.expandAbbreviations()}\"",
                            options = options.map { it.title },
                            correctAnswerIndex = options.indexOf(category),
                            explanation = "This is in the \"${category.title}\" category. ${scenario.consequence.expandAbbreviations()}",
                            ruleRef = scenario.rule_refs.firstOrNull() ?: ""
                        )
                    )
                }
            }
        } catch (e: Exception) {
            // skip
        }

        return questionPool.shuffled().take(count)
    }
}

data class RuleWithSection(
    val rule: Rule,
    val section: Section
)

data class QuizQuestion(
    val questionText: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String,
    val ruleRef: String
)