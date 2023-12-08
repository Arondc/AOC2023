package de.startat.aoc2023


import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class RangeUtilKtTest {

    @Test
    fun union_completeInside() {
        val range1 = 1L..5
        val range2 = 2L..4
        assertThat(range1.union(range2)).isEqualTo(setOf(1L..5))
        assertThat(range2.union(range1)).isEqualTo(setOf(1L..5))
    }

    @Test
    fun union_partiallyOverlapping() {
        val range1 = 1L..5
        val range2 = 3L..7
        assertThat(range1.union(range2)).isEqualTo(setOf(1L..7))
        assertThat(range2.union(range1)).isEqualTo(setOf(1L..7))
    }

    @Test
    fun union_borderOverlapping() {
        val range1 = 1L..5
        val range2 = 5L..7
        assertThat(range1.union(range2)).isEqualTo(setOf(1L..7))
        assertThat(range2.union(range1)).isEqualTo(setOf(1L..7))
    }

    @Test
    fun union_borderTouch() {
        val range1 = 1L..4
        val range2 = 5L..7
        assertThat(range1.union(range2)).isEqualTo(setOf(1L..7))
        assertThat(range2.union(range1)).isEqualTo(setOf(1L..7))
    }

    @Test
    fun union_notOverlapping() {
        val range1 = 1L..4
        val range2 = 6L..7
        assertThat(range1.union(range2)).containsExactlyInAnyOrder(1L..4, 6L..7)
        assertThat(range2.union(range1)).containsExactlyInAnyOrder(1L..4, 6L..7)
    }
    
    @Test
    fun completeInside() {
        val rangeOuter = 2L..10
        val rangeInner = 3L..9
        val rangeInnerLeft = 2L..5
        val rangeInnerRight = 6L..10
        val rangeOverlappingLeft = 0L..3
        val rangeOverlappingRight = 9L..12
        val rangeBorderingLeft = 0L .. 1L
        val rangeBorderingRight = 11L .. 12L
        val rangeOffLeft = -1L..0L
        val rangeOffRight = 12L..14L
        
        assertAll(
            { assertThat(rangeOuter.completeInside(rangeOuter)).isFalse() },
            { assertThat(rangeInner.completeInside(rangeOuter)).isTrue() },
            { assertThat(rangeInnerLeft.completeInside(rangeOuter)).isFalse() },
            { assertThat(rangeInnerRight.completeInside(rangeOuter)).isFalse() },
            { assertThat(rangeOverlappingLeft.completeInside(rangeOuter)).isFalse() },
            { assertThat(rangeOverlappingRight.completeInside(rangeOuter)).isFalse() },
            { assertThat(rangeBorderingLeft.completeInside(rangeOuter)).isFalse() },
            { assertThat(rangeBorderingRight.completeInside(rangeOuter)).isFalse() },
            { assertThat(rangeOffLeft.completeInside(rangeOuter)).isFalse() },
            { assertThat(rangeOffRight.completeInside(rangeOuter)).isFalse() },
        )
    }

    @Test
    fun overlaps() {
        val rangeLeft = 1L..4
        val rangeCenter = 5L..7
        val rangeRight = 8L..10
        val rangeLeftAndCenter = 1L..7
        val rangeComplete = 1L..10

        assertAll(
            //left & center don't overlap
            {assertThat(rangeLeft.overlaps(rangeCenter)).isFalse()},
            {assertThat(rangeCenter.overlaps(rangeLeft)).isFalse()},
            //center & right don't overlap
            {assertThat(rangeCenter.overlaps(rangeRight)).isFalse()},
            {assertThat(rangeRight.overlaps(rangeCenter)).isFalse()},

            //a range overlaps with a range containing its start or end
            {assertThat(rangeCenter.overlaps(rangeCenter.first..rangeCenter.first)).isTrue()},
            {assertThat(rangeCenter.overlaps(rangeCenter.last..rangeCenter.last)).isTrue()},

            //ranges fully contained within each other overlap (with matching start or/end)
            {assertThat(rangeLeft.overlaps(rangeLeftAndCenter)).isTrue()},
            {assertThat(rangeCenter.overlaps(rangeLeftAndCenter)).isTrue()},
            {assertThat(rangeLeftAndCenter.overlaps(rangeLeft)).isTrue()},
            {assertThat(rangeLeftAndCenter.overlaps(rangeLeft)).isTrue()},

            //a fully contained range overlaps with another without sharing start or end
            {assertThat(rangeCenter.overlaps(rangeComplete)).isTrue()},
            {assertThat(rangeComplete.overlaps(rangeCenter)).isTrue()},
        )
    }
}