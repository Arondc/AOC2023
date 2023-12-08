package de.startat.aoc2023

import kotlin.math.abs


fun LongRange.union(other : LongRange) : Set<LongRange>{
    if(overlaps(other) || bordersTouch(other)){
        return setOf(kotlin.math.min(this.first, other.first)..kotlin.math.max(this.last, other.last))
    }
    return setOf(this, other)
}

private fun LongRange.bordersTouch(other: LongRange) =
    abs(this.first - other.last) == 1L || abs(this.last - other.first) == 1L


fun LongRange.slice(other : LongRange) : Set<LongRange>{
    if(!this.overlaps(other)){
        return setOf(this,other)
    } else if(completeInside(other)) {
        return setOf(other.first..<this.first, this, this.last + 1..other.last)
    }
    return setOf()
}

fun LongRange.overlaps(other: LongRange) : Boolean {
    return if (this.first > other.first) {
        other.last >= this.first
    } else {
        this.last >= other.first
    }
}

fun LongRange.completeInside(other: LongRange): Boolean =
    this.first > other.first && this.last < other.last

