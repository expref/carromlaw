package com.vjn.carromlawscompanion

import android.content.Context
import android.content.SharedPreferences

/**
 * Manages bookmarked rules - saves them to phone storage so they persist between app launches.
 * Bookmarks are stored as a set of rule IDs (e.g., "92", "A.1", "63a").
 */
class BookmarkManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        "carrom_bookmarks",
        Context.MODE_PRIVATE
    )

    private val bookmarksKey = "bookmarked_rule_ids"

    /**
     * Get all bookmarked rule IDs as a set.
     */
    fun getBookmarkedRuleIds(): Set<String> {
        return prefs.getStringSet(bookmarksKey, emptySet()) ?: emptySet()
    }

    /**
     * Check if a specific rule is bookmarked.
     */
    fun isBookmarked(ruleId: String): Boolean {
        return getBookmarkedRuleIds().contains(ruleId)
    }

    /**
     * Toggle bookmark status for a rule. Returns new state (true = bookmarked).
     */
    fun toggleBookmark(ruleId: String): Boolean {
        val current = getBookmarkedRuleIds().toMutableSet()
        val newState = if (current.contains(ruleId)) {
            current.remove(ruleId)
            false
        } else {
            current.add(ruleId)
            true
        }
        prefs.edit().putStringSet(bookmarksKey, current).apply()
        return newState
    }

    /**
     * Get count of bookmarks.
     */
    fun getBookmarkCount(): Int {
        return getBookmarkedRuleIds().size
    }
}