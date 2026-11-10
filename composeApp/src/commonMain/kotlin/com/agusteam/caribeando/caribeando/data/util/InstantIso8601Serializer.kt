package com.agusteam.caribeando.caribeando.data.util

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Explicit ISO-8601 serializer for kotlin.time.Instant (e.g. "2024-01-01T00:00:00Z" — the same
 * format Instant.toString() produces), wired up per-field via
 * `@Serializable(with = InstantIso8601Serializer::class)`.
 *
 * Added 2026-08-19 as part of the kotlinx-datetime 0.6.2 -> 0.8.0 migration off
 * kotlinx.datetime.Clock/Instant onto kotlin.time.Clock/Instant. kotlinx.serialization 1.9.0+
 * ships a built-in `Instant.serializer()` producing this exact ISO-8601 format — this object
 * just wraps it with an explicit name so every affected @Serializable field opts in
 * deliberately instead of relying on kotlinx.serialization's automatic type resolution for a
 * still-@ExperimentalTime-marked serializer. This keeps the wire format byte-for-byte identical
 * to what the old kotlinx.datetime.Instant default serializer produced, so caribeando-backend
 * (which serializes java.time.Instant the same ISO-8601 way) needs no changes.
 */
@OptIn(ExperimentalTime::class)
object InstantIso8601Serializer : KSerializer<Instant> by Instant.serializer()
