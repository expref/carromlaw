package com.vjn.carromlawscompanion.ui.theme

import androidx.compose.ui.graphics.Color

// Carrom-themed colors — modeled on an ICF-standard carrom board:
// pale plywood playing surface, dark rosewood frame, gold trim, and
// the painted carrom red used for queen / corner markings.

// Light theme
val CarromBrown = Color(0xFF3E2418)         // Rosewood frame — primary
val CarromLightBrown = Color(0xFFD4A574)    // Light wood — primary container / surfaceVariant
val CarromCream = Color(0xFFF5E5C5)         // Light cream — surface (cards on top of the board)
val CarromBoardTan = Color(0xFFEDD3A7)      // Playing-surface tan — background
val CarromQueenRed = Color(0xFFA82828)      // Painted carrom red — tertiary / error
val CarromDarkBrown = Color(0xFF2D1810)     // Spine dark — on-primary / on-surface text
val CarromGold = Color(0xFFC9971F)          // Painted gold — secondary

// Dark theme
val CarromBrownDark = Color(0xFFD4A574)     // Light wood — primary in dark
val CarromContainerDark = Color(0xFF5C3A26) // Mid wood — container
val CarromBackgroundDark = Color(0xFF1F1410) // Frame in low light — background
val CarromSurfaceDark = Color(0xFF2D1810)   // Frame dark — surface in dark
val CarromGoldDark = Color(0xFFD4AF37)      // Brighter gold for dark
val CarromQueenRedDark = Color(0xFFE04444)  // Brighter red for dark
val CarromOnDark = Color(0xFFEFEBE9)        // Light text on dark

// Material 3 defaults — kept in case anything still references them.
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)
val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)
