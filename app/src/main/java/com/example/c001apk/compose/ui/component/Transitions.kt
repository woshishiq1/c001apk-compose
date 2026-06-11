package com.example.c001apk.compose.ui.component

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize

// Material Design 3 Easing Curves
val EmphasizedEasing: Easing = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f)
val EmphasizedDecelerateEasing: Easing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1.0f)
val EmphasizedAccelerateEasing: Easing = CubicBezierEasing(0.3f, 0.0f, 0.8f, 0.15f)

sealed class TransitionDurations(private val milliseconds: Int) {
    data object Fast : TransitionDurations(200) // MD3 Short
    data object Normal : TransitionDurations(400) // MD3 Medium-Long
    data object Slow : TransitionDurations(500) // MD3 Extra Long

    fun <T> asTween(delayMillis: Int = 0, easing: Easing = EmphasizedEasing) =
        tween<T>(milliseconds, delayMillis, easing)
}

data class ScaleTransition(val enterScale: Float, val exitScale: Float) {
    fun enterTransition(
        animationSpec: FiniteAnimationSpec<Float> = defaultEnterAnimationSpec(),
    ) = scaleIn(animationSpec, enterScale) + fadeIn(defaultEnterAnimationSpec())

    fun exitTransition(
        animationSpec: FiniteAnimationSpec<Float> = defaultExitAnimationSpec(),
    ) = scaleOut(animationSpec, exitScale) + fadeOut(defaultExitAnimationSpec())

    companion object {
        private const val ShrinkScale = 0.90f
        private const val ExpandScale = 1.10f

        val scaleUp = ScaleTransition(ShrinkScale, ExpandScale)
        val scaleDown = ScaleTransition(ExpandScale, ShrinkScale)

        private fun <T> defaultEnterAnimationSpec() = tween<T>(400, 0, EmphasizedDecelerateEasing)
        private fun <T> defaultExitAnimationSpec() = tween<T>(200, 0, EmphasizedAccelerateEasing)
    }
}

private typealias CalcOffsetFn = (IntSize) -> IntOffset

data class SlideTransition(
    val enterOffset: CalcOffsetFn,
    val exitOffset: CalcOffsetFn,
) {
    fun enterTransition(
        animationSpec: FiniteAnimationSpec<IntOffset> = defaultAnimationSpec(),
    ) = slideIn(animationSpec) { enterOffset(it) } + fadeIn(defaultAnimationSpec())

    fun exitTransition(
        animationSpec: FiniteAnimationSpec<IntOffset> = defaultAnimationSpec(),
    ) = slideOut(animationSpec) { exitOffset(it) } + fadeOut(defaultAnimationSpec())

    companion object {
        private val slideUpOffset: CalcOffsetFn = { IntOffset(0, calculateOffset(-it.height)) }
        private val slideDownOffset: CalcOffsetFn = { IntOffset(0, calculateOffset(it.height)) }
        private val slideLeftOffset: CalcOffsetFn = { IntOffset(calculateOffset(-it.width), 0) }
        private val slideRightOffset: CalcOffsetFn = { IntOffset(calculateOffset(it.width), 0) }

        val slideUp = SlideTransition(slideDownOffset, slideUpOffset)
        val slideDown = SlideTransition(slideUpOffset, slideDownOffset)
        val slideLeft = SlideTransition(slideRightOffset, slideLeftOffset)
        val slideRight = SlideTransition(slideLeftOffset, slideRightOffset)

        private fun calculateOffset(size: Int) = (0.15 * size).toInt()
        private fun <T> defaultAnimationSpec() = TransitionDurations.Normal.asTween<T>()
    }
}

object FadeTransition {
    fun enterTransition(animationSpec: FiniteAnimationSpec<Float> = defaultEnterAnimationSpec()) =
        fadeIn(animationSpec)

    fun exitTransition(animationSpec: FiniteAnimationSpec<Float> = defaultExitAnimationSpec()) =
        fadeOut(animationSpec)

    private fun <T> defaultEnterAnimationSpec() = tween<T>(400, 0, EmphasizedDecelerateEasing)
    private fun <T> defaultExitAnimationSpec() = tween<T>(200, 0, EmphasizedAccelerateEasing)
}
