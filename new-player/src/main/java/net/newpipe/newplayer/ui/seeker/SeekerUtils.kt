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

// returns the corresponding position in pixels of progress in the the slider.

/** @hide */
internal fun valueToPx(
    value: Float,
    widthPx: Float,
    range: ClosedFloatingPointRange<Float>
): Float {
    val rangeSIze = range.endInclusive - range.start
    val p = value.coerceIn(range.start, range.endInclusive)
    val progressPercent = (p - range.start) * 100 / rangeSIze
    return (progressPercent * widthPx / 100)
}

// returns the corresponding progress value for a position in slider

/** @hide */
internal fun pxToValue(
    position: Float,
    widthPx: Float,
    range: ClosedFloatingPointRange<Float>
): Float {
    val rangeSize = range.endInclusive - range.start
    val percent = position * 100 / widthPx
    return ((percent * (rangeSize) / 100) + range.start).coerceIn(
        range.start,
        range.endInclusive
    )
}

// converts the start value of a segment to the corresponding start and end pixel values
// at which the segment will start and end on the track.

/** @hide */
internal fun segmentToPxValues(
    segments: List<Segment>,
    range: ClosedFloatingPointRange<Float>,
    widthPx: Float,
): List<SegmentPxs> {

    val rangeSize = range.endInclusive - range.start
    val sortedSegments = segments.distinct().sortedBy { it.start }
    val segmentRangesPxs = sortedSegments.map { segment ->

        // percent of the start of this segment in the range size
        val percentStart = (segment.start - range.start) * 100 / rangeSize
        val percentEnd = (segment.end - range.start) * 100 / rangeSize
        val startPx = percentStart * widthPx / 100
        val endPx = percentEnd * widthPx / 100
        Pair(startPx, endPx)
    }

    return sortedSegments.mapIndexed { index, segment ->
        SegmentPxs(
            name = segment.name,
            color = segment.color,
            startPx = segmentRangesPxs[index].first,
            endPx = segmentRangesPxs[index].second
        )
    }
}


/** @hide */
internal fun chapterSegmentToPxValues(
    segments: List<ChapterSegment>,
    range: ClosedFloatingPointRange<Float>,
    widthPx: Float,
): List<SegmentPxs> {

    val rangeSize = range.endInclusive - range.start
    val sortedSegments = segments.distinct().sortedBy { it.start }

    val segmentStartPxs = sortedSegments.map { segment ->

        // percent of the start of this segment in the range size
        val percent = (segment.start - range.start) * 100 / rangeSize
        val startPx = percent * widthPx / 100
        startPx
    }

    return sortedSegments.mapIndexed { index, segment ->
        val endPx = if (index != sortedSegments.lastIndex) segmentStartPxs[index + 1] else widthPx
        SegmentPxs(
            name = segment.name,
            color = segment.color,
            startPx = segmentStartPxs[index],
            endPx = endPx
        )
    }
}


/** @hide */
internal fun rtlAware(value: Float, widthPx: Float, isRtl: Boolean) =
    if (isRtl) widthPx - value else value


/** @hide */
internal fun lerp(start: Float, end: Float, fraction: Float): Float {
    return (1 - fraction) * start + fraction * end
}