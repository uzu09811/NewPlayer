 /*
 * Copyright 2023 Vivek Singh
 *
 * @Author Vivek Singh
 * @Author Christian Schabesberger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Original code was taken from: https://github.com/2307vivek/Seeker/
 *
 */
package net.newpipe.newplayer.ui.seeker

import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

/**
 * A state object which can be hoisted to observe the current segment of Seeker. In most cases this
 * will be created by [rememberSeekerState]
 * */
@Stable

/** @hide */
internal class SeekerState() {

    /**
     * The current segment corresponding to the current seeker value.
     * */
    var currentSegment: Segment by mutableStateOf(Segment.Unspecified)

    
/** @hide */
internal var onDrag: ((Float) -> Unit)? = null

    
/** @hide */
internal val draggableState = DraggableState {
        onDrag?.invoke(it)
    }

    
/** @hide */
internal fun currentSegment(
        value: Float,
        segments: List<Segment>
    ) = (segments.findLast { value >= it.start } ?: Segment.Unspecified).also { this.currentSegment = it }
}

/**
 * Creates a SeekerState which will be remembered across compositions.
 * */
@Composable

/** @hide */
internal fun rememberSeekerState(): SeekerState = remember {
    SeekerState()
}

/**
 * A class to hold information about a segment.
 * @param name name of the segment
 * @param start the value at which this segment should start in the track. This should must be in the
 * range of the Seeker range values.
 * @param color the color of the segment
 * */
@Immutable

/** @hide */
internal data class Segment(
    val name: String,
    val start: Float,
    val end: Float,
    val color: Color = Color.Unspecified
) {
    companion object {
        val Unspecified = Segment(name = "", start = 0f, end = 0f)
    }
}

 @Immutable
 
/** @hide */
internal data class ChapterSegment(
     val name: String,
     val start: Float,
     val color: Color = Color.Unspecified
 ) {
     companion object {
         val Unspecified = ChapterSegment(name = "", start = 0f)
     }
 }

@Immutable

/** @hide */
internal data class SegmentPxs(
    val name: String,
    val startPx: Float,
    val endPx: Float,
    val color: Color
)