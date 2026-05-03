package com.vjn.carromlawscompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items as gridItems
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Brightness1
import androidx.compose.material.icons.filled.Cached
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Chair
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.ManageSearch
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Scoreboard
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vjn.carromlawscompanion.ui.animation.RuleVisuals
import com.vjn.carromlawscompanion.ui.animation.VisualFor
import com.vjn.carromlawscompanion.ui.theme.CarromGold
import com.vjn.carromlawscompanion.ui.theme.CarromLawsCompanionTheme
import com.vjn.carromlawscompanion.ui.theme.CarromLineBlack
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CarromLawsCompanionTheme {
                CarromApp()
            }
        }
    }
}

data class SearchResult(
    val section: Section,
    val rule: Rule
)

@Composable
fun CarromApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                onBrowseRulesClick = { navController.navigate("rules") },
                onFoulCheckerClick = { navController.navigate("foul_checker") },
                onScenarioClick = { navController.navigate("scenarios") },
                onBookmarksClick = { navController.navigate("bookmarks") },
                onQuizClick = { navController.navigate("quiz") },
                onSearchClick = { navController.navigate("search") },
                onSettingsClick = { navController.navigate("settings") }
            )
        }
        composable("rules") {
            RulesListScreen(
                onSectionClick = { sectionId ->
                    navController.navigate("section/$sectionId")
                },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable("section/{sectionId}") { backStackEntry ->
            val sectionId = backStackEntry.arguments?.getString("sectionId") ?: ""
            SectionDetailScreen(
                sectionId = sectionId,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable("search") {
            SearchScreen(
                onBackClick = { navController.popBackStack() },
                onResultClick = { sectionId ->
                    navController.navigate("section/$sectionId")
                }
            )
        }
        composable("foul_checker") {
            FoulCheckerScreen(
                onCategoryClick = { categoryId ->
                    navController.navigate("foul_category/$categoryId")
                },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable("foul_category/{categoryId}") { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
            FoulCategoryScreen(
                categoryId = categoryId,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable("scenarios") {
            ScenarioSimulatorScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        composable("bookmarks") {
            BookmarksScreen(
                onBackClick = { navController.popBackStack() },
                onRuleClick = { sectionId ->
                    navController.navigate("section/$sectionId")
                }
            )
        }
        composable("quiz") {
            QuizScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        composable("settings") {
            SettingsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onBrowseRulesClick: () -> Unit,
    onFoulCheckerClick: () -> Unit,
    onScenarioClick: () -> Unit,
    onBookmarksClick: () -> Unit,
    onQuizClick: () -> Unit,
    onSearchClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val context = LocalContext.current
    val bookmarkManager = remember { BookmarkManager(context) }
    val bookmarkCount = remember { bookmarkManager.getBookmarkCount() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Laws of Carrom") },
                actions = {
                    IconButton(onClick = onSearchClick) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 320.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                HomeMenuCard(
                    title = "Browse Rules",
                    description = "Read all 18 sections and 151 rules",
                    icon = Icons.Filled.MenuBook,
                    trailingIcon = Icons.Filled.LibraryBooks,
                    trailingTint = MaterialTheme.colorScheme.primary,
                    onClick = onBrowseRulesClick
                )
            }
            item {
                HomeMenuCard(
                    title = "Foul Checker",
                    description = "Identify fouls and their consequences",
                    icon = Icons.Filled.Warning,
                    trailingIcon = Icons.Filled.Gavel,
                    trailingTint = MaterialTheme.colorScheme.tertiary,
                    onClick = onFoulCheckerClick
                )
            }
            item {
                HomeMenuCard(
                    title = "Queen Scenarios",
                    description = "Walk through complex Queen rulings",
                    icon = Icons.Filled.Help,
                    trailingIcon = Icons.Filled.Brightness1,
                    trailingTint = MaterialTheme.colorScheme.tertiary,
                    onClick = onScenarioClick
                )
            }
            item {
                HomeMenuCard(
                    title = "Quiz Mode",
                    description = "Test your knowledge of the rules",
                    icon = Icons.Filled.Quiz,
                    trailingIcon = Icons.Filled.Lightbulb,
                    trailingTint = CarromGold,
                    onClick = onQuizClick
                )
            }
            item {
                HomeMenuCard(
                    title = "Bookmarks",
                    description = if (bookmarkCount > 0) "$bookmarkCount saved rules" else "Save rules for quick access",
                    icon = Icons.Filled.Star,
                    trailingIcon = Icons.Filled.Bookmarks,
                    trailingTint = CarromGold,
                    onClick = onBookmarksClick
                )
            }
            item {
                HomeMenuCard(
                    title = "Search",
                    description = "Find specific rules quickly",
                    icon = Icons.Filled.Search,
                    trailingIcon = Icons.Filled.ManageSearch,
                    trailingTint = MaterialTheme.colorScheme.primary,
                    onClick = onSearchClick
                )
            }
            item {
                HomeMenuCard(
                    title = "Settings",
                    description = "Adjust font size and preferences",
                    icon = Icons.Filled.Settings,
                    trailingIcon = Icons.Filled.Tune,
                    trailingTint = MaterialTheme.colorScheme.primary,
                    onClick = onSettingsClick
                )
            }
        }
    }
}

@Composable
fun HomeMenuCard(
    title: String,
    description: String,
    icon: ImageVector,
    trailingIcon: ImageVector,
    trailingTint: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.padding(end = 16.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.size(12.dp))
            Icon(
                imageVector = trailingIcon,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = trailingTint
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RulesListScreen(
    onSectionClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val repository = remember { RulesRepository(context) }
    val sections = remember { repository.getSections() }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.primary,
        topBar = {
            TopAppBar(
                title = { Text("Browse Rules") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 320.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            gridItems(sections) { section ->
                SectionCard(
                    section = section,
                    onClick = { onSectionClick(section.id) }
                )
            }
        }
    }
}

@Composable
fun SectionCard(section: Section, onClick: () -> Unit) {
    val (icon, iconTint) = sectionVisual(section.id)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        border = BorderStroke(0.7.dp, CarromLineBlack.copy(alpha = 0.6f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${section.id}. ${section.title}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = section.description.expandAbbreviations(),
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${section.rules.size} rules",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.size(12.dp))
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = iconTint
            )
        }
    }
}

/**
 * Picks an icon and tint that fit each section's topic, so cards show a relevant
 * trailing glyph instead of just text-on-blank. Tints: primary (rosewood) for
 * neutral topics, tertiary (queen red) for foul / penalty / loss / queen,
 * CarromGold for the "moment" sections (toss, break, score).
 */
@Composable
private fun sectionVisual(sectionId: String): Pair<ImageVector, Color> {
    val primary = MaterialTheme.colorScheme.primary
    val accent = MaterialTheme.colorScheme.tertiary
    return when (sectionId) {
        "I" -> Icons.Filled.GridOn to primary                 // Standard Equipment
        "II" -> Icons.Filled.MenuBook to primary              // Interpretations
        "III" -> Icons.Filled.Chair to primary                // Sitting Position
        "IV" -> Icons.Filled.TouchApp to primary              // How to Strike
        "V" -> Icons.Filled.Casino to CarromGold              // Toss
        "VI" -> Icons.Filled.Replay to primary                // Trial Board
        "VII" -> Icons.Filled.Bolt to CarromGold              // Break
        "VIII" -> Icons.Filled.Cached to primary              // Turn of Play
        "IX" -> Icons.Filled.Scoreboard to CarromGold         // How to Score
        "X" -> Icons.Filled.SwapHoriz to primary              // Change of Sides
        "XI" -> Icons.Filled.Warning to accent                // Foul
        "XII" -> Icons.AutoMirrored.Filled.Logout to primary  // C/m Overboard
        "XIII" -> Icons.Filled.Layers to primary              // Rolling and Overlapping
        "XIV" -> Icons.Filled.Gavel to accent                 // Dues and Penalties
        "XV" -> Icons.Filled.Brightness1 to accent            // Queen — solid red disc
        "XVI" -> Icons.Filled.Info to primary                 // General
        "XVII" -> Icons.Filled.Block to accent                // Loss of Entire Match
        "XVIII" -> Icons.Filled.Campaign to primary           // Protest
        else -> Icons.Filled.Info to primary
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SectionDetailScreen(
    sectionId: String,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val repository = remember { RulesRepository(context) }
    val bookmarkManager = remember { BookmarkManager(context) }
    val section = remember(sectionId) {
        repository.getSections().find { it.id == sectionId }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.primary,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = section?.let { "${it.id}. ${it.title}" } ?: "Section",
                        maxLines = 1
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        if (section == null) {
            Text(
                text = "Section not found",
                modifier = Modifier.padding(innerPadding).padding(16.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(section.rules) { rule ->
                    RuleCard(
                        rule = rule,
                        bookmarkManager = bookmarkManager
                    )
                }
            }
        }
    }
}

@Composable
fun RuleCard(rule: Rule, bookmarkManager: BookmarkManager) {
    var isBookmarked by remember { mutableStateOf(bookmarkManager.isBookmarked(rule.id)) }
    var showDemo by remember(rule.id) { mutableStateOf(false) }
    val context = LocalContext.current
    val hasVisual = RuleVisuals.hasVisualFor(rule.id)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        border = BorderStroke(0.7.dp, CarromLineBlack.copy(alpha = 0.6f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "${rule.id} — ${rule.title}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                // Share button
                IconButton(
                    onClick = { shareRule(context, rule) }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Share,
                        contentDescription = "Share rule",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                // Bookmark button
                IconButton(
                    onClick = {
                        isBookmarked = bookmarkManager.toggleBookmark(rule.id)
                    }
                ) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                        contentDescription = if (isBookmarked) "Remove bookmark" else "Bookmark",
                        tint = if (isBookmarked) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = rule.text.expandAbbreviations(),
                style = MaterialTheme.typography.bodyMedium
            )
            if (hasVisual) {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = { showDemo = !showDemo },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = if (showDemo) Icons.Filled.Close else Icons.Filled.PlayArrow,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(if (showDemo) "Hide demo" else "Watch demo")
                }
                if (showDemo) {
                    Spacer(modifier = Modifier.height(12.dp))
                    VisualFor(rule.id)
                }
            }
        }
    }
}

/**
 * Opens Android's share menu with formatted rule text.
 */
fun shareRule(context: android.content.Context, rule: Rule) {
    val shareText = buildString {
        append("📜 Rule ${rule.id}: ${rule.title}\n\n")
        append(rule.text.expandAbbreviations())
        append("\n\n— Official Laws of Carrom (ICF)")
    }

    val shareIntent = android.content.Intent().apply {
        action = android.content.Intent.ACTION_SEND
        type = "text/plain"
        putExtra(android.content.Intent.EXTRA_TEXT, shareText)
        putExtra(android.content.Intent.EXTRA_SUBJECT, "Carrom Rule ${rule.id}")
    }

    val chooser = android.content.Intent.createChooser(shareIntent, "Share rule via")
    chooser.flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(chooser)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onBackClick: () -> Unit,
    onResultClick: (String) -> Unit
) {
    val context = LocalContext.current
    val repository = remember { RulesRepository(context) }
    val sections = remember { repository.getSections() }

    var searchQuery by remember { mutableStateOf("") }

    val searchResults = remember(searchQuery) {
        if (searchQuery.length < 1) {
            emptyList()
        } else {
            val query = searchQuery.trim().lowercase()
            val cleanedQuery = query
                .removePrefix("rule ")
                .removePrefix("rule")
                .trim()

            val queryWords = cleanedQuery.split(" ").filter { it.isNotBlank() }

            val matches = mutableListOf<Pair<Int, SearchResult>>()

            sections.forEach { section ->
                section.rules.forEach { rule ->
                    val ruleIdLower = rule.id.lowercase()
                    val titleLower = rule.title.lowercase()
                    val textLower = rule.text.lowercase()
                    val expandedTextLower = rule.text.expandAbbreviations().lowercase()
                    val tagsLower = rule.tags.map { it.lowercase() }

                    var score = 0

                    if (ruleIdLower == cleanedQuery) {
                        score += 1000
                    }
                    if (ruleIdLower.startsWith(cleanedQuery) && ruleIdLower != cleanedQuery) {
                        score += 500
                    }
                    if (titleLower.contains(cleanedQuery)) {
                        score += 200
                    }
                    if (queryWords.isNotEmpty() && queryWords.all { titleLower.contains(it) }) {
                        score += 100
                    }
                    if (tagsLower.any { tag -> queryWords.any { tag.contains(it) } }) {
                        score += 50
                    }
                    if (expandedTextLower.contains(cleanedQuery) || textLower.contains(cleanedQuery)) {
                        score += 30
                    }
                    if (queryWords.isNotEmpty() && queryWords.all { word ->
                            textLower.contains(word) || expandedTextLower.contains(word)
                        }) {
                        score += 10
                    }

                    if (score > 0) {
                        matches.add(score to SearchResult(section, rule))
                    }
                }
            }

            matches
                .sortedWith(compareByDescending<Pair<Int, SearchResult>> { it.first }
                    .thenBy { it.second.rule.id })
                .map { it.second }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search Rules") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search by keyword or rule number (e.g., 'queen', 'rule 15')") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                imageVector = Icons.Filled.Clear,
                                contentDescription = "Clear"
                            )
                        }
                    }
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (searchQuery.length >= 1) {
                Text(
                    text = "Found ${searchResults.size} matching rules",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(searchResults) { result ->
                    SearchResultCard(
                        result = result,
                        onClick = { onResultClick(result.section.id) },
                        searchQuery = searchQuery.trim()
                            .removePrefix("rule ")
                            .removePrefix("rule")
                            .trim()
                    )
                }
            }
        }
    }
}

@Composable
fun SearchResultCard(result: SearchResult, onClick: () -> Unit, searchQuery: String = "") {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "${result.section.id}. ${result.section.title}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            // Highlight search query in title
            Text(
                text = "${result.rule.id} — ${result.rule.title}".highlightSearchTerm(searchQuery),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            val expandedText = result.rule.text.expandAbbreviations()
            val previewText = expandedText.take(150) + if (expandedText.length > 150) "..." else ""
            // Highlight search query in preview text
            Text(
                text = previewText.highlightSearchTerm(searchQuery),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoulCheckerScreen(
    onCategoryClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val repository = remember { RulesRepository(context) }
    val categories = remember { repository.getFoulCategories() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Foul Checker") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "What kind of issue happened?",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Tap a category to see scenarios and consequences",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    FoulCategoryCard(
                        category = category,
                        onClick = { onCategoryClick(category.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun FoulCategoryCard(category: FoulCategory, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = category.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${category.scenarios.size} scenarios",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoulCategoryScreen(
    categoryId: String,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val repository = remember { RulesRepository(context) }
    val category = remember(categoryId) {
        repository.getFoulCategories().find { it.id == categoryId }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = category?.title ?: "Category",
                        maxLines = 1
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        if (category == null) {
            Text(
                text = "Category not found",
                modifier = Modifier.padding(innerPadding).padding(16.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(category.scenarios) { scenario ->
                    FoulScenarioCard(scenario = scenario)
                }
            }
        }
    }
}

@Composable
fun FoulScenarioCard(scenario: FoulScenario) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = scenario.question.expandAbbreviations(),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = scenario.consequence.expandAbbreviations(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(8.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = scenario.details.expandAbbreviations(),
                style = MaterialTheme.typography.bodySmall
            )
            if (scenario.rule_refs.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Rules: ${scenario.rule_refs.joinToString(", ")}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScenarioSimulatorScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val repository = remember { RulesRepository(context) }
    val scoringScenarios = remember { repository.getScoringScenarios() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Queen Scenarios") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = scoringScenarios.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = scoringScenarios.description,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(scoringScenarios.scenarios) { scenario ->
                    ScoringScenarioCard(scenario = scenario)
                }
            }
        }
    }
}

@Composable
fun ScoringScenarioCard(scenario: ScoringScenario) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "SITUATION",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = scenario.situation.expandAbbreviations(),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "✓ PROPER STROKE",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = scenario.proper_stroke.expandAbbreviations(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "✗ IMPROPER STROKE",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = scenario.improper_stroke.expandAbbreviations(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            if (scenario.rule_refs.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Rules: ${scenario.rule_refs.joinToString(", ")}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarksScreen(
    onBackClick: () -> Unit,
    onRuleClick: (String) -> Unit
) {
    val context = LocalContext.current
    val repository = remember { RulesRepository(context) }
    val bookmarkManager = remember { BookmarkManager(context) }

    var refreshTrigger by remember { mutableStateOf(0) }

    val bookmarkedRules = remember(refreshTrigger) {
        val bookmarkedIds = bookmarkManager.getBookmarkedRuleIds()
        repository.getSections().flatMap { section ->
            section.rules
                .filter { it.id in bookmarkedIds }
                .map { rule -> SearchResult(section, rule) }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bookmarks") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        if (bookmarkedRules.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.BookmarkBorder,
                    contentDescription = null,
                    modifier = Modifier.padding(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "No bookmarks yet",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Tap the bookmark icon on any rule to save it for quick access",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(bookmarkedRules) { result ->
                    BookmarkRuleCard(
                        result = result,
                        bookmarkManager = bookmarkManager,
                        onClick = { onRuleClick(result.section.id) },
                        onUnbookmarked = { refreshTrigger++ }
                    )
                }
            }
        }
    }
}

@Composable
fun BookmarkRuleCard(
    result: SearchResult,
    bookmarkManager: BookmarkManager,
    onClick: () -> Unit,
    onUnbookmarked: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${result.section.id}. ${result.section.title}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${result.rule.id} — ${result.rule.title}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(onClick = {
                    bookmarkManager.toggleBookmark(result.rule.id)
                    onUnbookmarked()
                }) {
                    Icon(
                        imageVector = Icons.Filled.Bookmark,
                        contentDescription = "Remove bookmark",
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            val expandedText = result.rule.text.expandAbbreviations()
            Text(
                text = expandedText.take(150) + if (expandedText.length > 150) "..." else "",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val repository = remember { RulesRepository(context) }
    val quizManager = remember { QuizManager(context, repository) }

    var quizState by remember { mutableStateOf<QuizState>(QuizState.NotStarted) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quiz Mode") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (val state = quizState) {
                is QuizState.NotStarted -> {
                    QuizStartScreen(
                        highScore = quizManager.getHighScore(),
                        onStart = {
                            val questions = quizManager.generateQuestions(10)
                            quizState = QuizState.InProgress(questions, 0, 0, null)
                        }
                    )
                }
                is QuizState.InProgress -> {
                    QuizQuestionScreen(
                        state = state,
                        onAnswer = { answerIndex ->
                            quizState = state.copy(selectedAnswer = answerIndex)
                        },
                        onNext = {
                            val isCorrect = state.selectedAnswer == state.questions[state.currentIndex].correctAnswerIndex
                            val newScore = if (isCorrect) state.score + 1 else state.score

                            if (state.currentIndex + 1 >= state.questions.size) {
                                quizManager.saveQuizResult(newScore, state.questions.size)
                                quizState = QuizState.Finished(newScore, state.questions.size)
                            } else {
                                quizState = state.copy(
                                    currentIndex = state.currentIndex + 1,
                                    score = newScore,
                                    selectedAnswer = null
                                )
                            }
                        }
                    )
                }
                is QuizState.Finished -> {
                    QuizResultScreen(
                        score = state.score,
                        total = state.total,
                        highScore = quizManager.getHighScore(),
                        onRestart = {
                            val questions = quizManager.generateQuestions(10)
                            quizState = QuizState.InProgress(questions, 0, 0, null)
                        },
                        onExit = onBackClick
                    )
                }
            }
        }
    }
}

sealed class QuizState {
    data object NotStarted : QuizState()
    data class InProgress(
        val questions: List<QuizQuestion>,
        val currentIndex: Int,
        val score: Int,
        val selectedAnswer: Int?
    ) : QuizState()
    data class Finished(val score: Int, val total: Int) : QuizState()
}

@Composable
fun QuizStartScreen(
    highScore: Int,
    onStart: () -> Unit
) {
    val context = LocalContext.current
    val repository = remember { RulesRepository(context) }
    val quizManager = remember { QuizManager(context, repository) }

    val totalQuizzes = quizManager.getTotalQuizzes()
    val totalQuestions = quizManager.getTotalQuestions()
    val totalCorrect = quizManager.getTotalCorrect()
    val accuracy = quizManager.getAccuracyPercentage()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Icon(
            imageVector = Icons.Filled.Quiz,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Test Your Knowledge",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Answer 10 questions about the Laws of Carrom",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Stats card - only show if user has played at least one quiz
        if (totalQuizzes > 0) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.EmojiEvents,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            text = "Your Stats",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    StatRow(label = "Best Score", value = "$highScore / 10")
                    StatRow(label = "Quizzes Completed", value = "$totalQuizzes")
                    StatRow(label = "Questions Answered", value = "$totalQuestions")
                    StatRow(label = "Correct Answers", value = "$totalCorrect")
                    StatRow(label = "Overall Accuracy", value = "$accuracy%")
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        Button(
            onClick = onStart,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (totalQuizzes > 0) "Play Again" else "Start Quiz",
                fontSize = 18.sp,
                modifier = Modifier.padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun StatRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun QuizQuestionScreen(
    state: QuizState.InProgress,
    onAnswer: (Int) -> Unit,
    onNext: () -> Unit
) {
    val question = state.questions[state.currentIndex]
    val showResult = state.selectedAnswer != null
    val isCorrect = state.selectedAnswer == question.correctAnswerIndex

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Progress bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Question ${state.currentIndex + 1} of ${state.questions.size}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "Score: ${state.score}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { (state.currentIndex + 1).toFloat() / state.questions.size },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Question
        Card(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = question.questionText.expandAbbreviations(),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Options
        question.options.forEachIndexed { index, option ->
            val isSelected = state.selectedAnswer == index
            val isCorrectAnswer = index == question.correctAnswerIndex
            val showCorrect = showResult && isCorrectAnswer
            val showWrong = showResult && isSelected && !isCorrectAnswer

            val containerColor = when {
                showCorrect -> Color(0xFFC8E6C9)  // Light green for correct answer
                showWrong -> MaterialTheme.colorScheme.errorContainer
                isSelected -> MaterialTheme.colorScheme.primaryContainer
                else -> MaterialTheme.colorScheme.surface
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable(enabled = !showResult) { onAnswer(index) },
                colors = CardDefaults.cardColors(containerColor = containerColor)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = option.expandAbbreviations(),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                    if (showCorrect) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Correct",
                            tint = Color(0xFF1B5E20)  // Dark green
                        )
                    } else if (showWrong) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Wrong",
                            tint = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
        }

        // After answering: show result banner, explanation, and Next button
        if (showResult) {
            Spacer(modifier = Modifier.height(16.dp))

            // Result banner - clearly shows Correct (green) or Incorrect (red)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (isCorrect)
                        Color(0xFFC8E6C9)  // Light green background
                    else
                        MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isCorrect) Icons.Filled.Check else Icons.Filled.Close,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = if (isCorrect)
                            Color(0xFF1B5E20)  // Dark green
                        else
                            MaterialTheme.colorScheme.onErrorContainer
                    )
                    Spacer(modifier = Modifier.size(12.dp))
                    Text(
                        text = if (isCorrect) "Correct!" else "Incorrect",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = if (isCorrect)
                            Color(0xFF1B5E20)  // Dark green
                        else
                            MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Explanation
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "EXPLANATION",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = question.explanation,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Next button
            Button(
                onClick = onNext,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (state.currentIndex + 1 >= state.questions.size)
                        "See Results"
                    else
                        "Next Question →",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(8.dp)
                )
            }

            // Bottom padding so button isn't cut off
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun QuizResultScreen(
    score: Int,
    total: Int,
    highScore: Int,
    onRestart: () -> Unit,
    onExit: () -> Unit
) {
    val percentage = (score * 100) / total
    val message = when {
        percentage >= 90 -> "Outstanding! You're a Carrom expert!"
        percentage >= 70 -> "Great job! You know the rules well."
        percentage >= 50 -> "Not bad! Keep studying."
        else -> "Keep practicing! Review the rules and try again."
    }
    val isNewHighScore = score >= highScore && score > 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Filled.EmojiEvents,
            contentDescription = null,
            modifier = Modifier.size(96.dp),
            tint = MaterialTheme.colorScheme.tertiary
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isNewHighScore) {
            Text(
                text = "🎉 NEW HIGH SCORE! 🎉",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.tertiary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Text(
            text = "$score / $total",
            style = MaterialTheme.typography.displayLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "$percentage%",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onRestart,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Try Again", fontSize = 18.sp, modifier = Modifier.padding(8.dp))
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onExit,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Home", fontSize = 18.sp, modifier = Modifier.padding(8.dp))
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val settingsManager = remember { SettingsManager(context) }
    var currentFontSize by remember { mutableStateOf(settingsManager.getFontSize()) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Font Size",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Adjusts text size throughout the app",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))

            FontSize.values().forEach { size ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {
                            if (currentFontSize != size) {
                                currentFontSize = size
                                settingsManager.setFontSize(size)
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "Font size changed to ${size.displayName}",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = if (currentFontSize == size)
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.surface
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = size.displayName,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "The quick brown fox jumps over the lazy dog",
                                fontSize = (14 * size.scale).sp
                            )
                        }
                        if (currentFontSize == size) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = "Selected",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Debug card - shows what's actually saved
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "🔍 Debug Info",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Saved value in storage: ${settingsManager.getFontSize().displayName} (scale: ${settingsManager.getFontSize().scale})",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "Currently selected (in UI): ${currentFontSize.displayName}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}