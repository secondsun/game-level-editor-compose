import androidx.compose.runtime.compositionLocalOf

data class KeyboardState(val isShiftDown: Boolean = false)
val LocalKeyboard = compositionLocalOf { KeyboardState() }