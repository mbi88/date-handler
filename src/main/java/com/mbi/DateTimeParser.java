package com.mbi;


import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Parses duration formulas (e.g., "1y2M3d4h5m6s") into a {@link CustomDateTime} object.
 * <p>
 * Supported units:
 * <ul>
 *     <li>y - years</li>
 *     <li>M - months</li>
 *     <li>d - days</li>
 *     <li>h - hours</li>
 *     <li>m - minutes</li>
 *     <li>s - seconds</li>
 * </ul>
 * <p>
 * Example: "1y2M3d4h5m6s"
 * Where:
 * 1y - one year.
 * 2M - two months.
 * 3d - three days.
 * 4h - four hours.
 * 5m - 5 minutes.
 * 6s - 6 seconds.
 */
final class DateTimeParser {

    private static final ObjectMapper MAPPER = JsonMapper
            .builderWithJackson2Defaults()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
            .build();

    /**
     * Parses a time formula into a {@link CustomDateTime} object in format:
     * Years - y.
     * Months - M.
     * Days - d.
     * Hours - h.
     * Minutes - m.
     * Seconds - s.
     *
     * @param formula string with time units (e.g., "1y2M3d4h5m6s")
     * @return parsed custom datetime object
     * @throws IllegalArgumentException if input format is invalid
     */
    public CustomDateTime parse(final String formula) {
        if (formula == null || formula.isBlank()) {
            throw new IllegalArgumentException("Formula must not be null or blank");
        }

        // Normalize: replace 'M' (months) with 'mo' to avoid conflict with 'm' (minutes)
        final String normalized = formula.replace("M", "mo");

        // Must start with digit and end with letter
        if (!Character.isDigit(normalized.charAt(0))
                || !Character.isLetter(normalized.charAt(normalized.length() - 1))) {
            throw new IllegalArgumentException("Invalid format: must start with digit and end with letter");
        }

        // Split into alternating number and unit tokens
        final var parts = Arrays.asList(normalized.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)"));
        final Map<String, Integer> result = new HashMap<>();
        for (int i = 0; i < parts.size(); i += 2) {
            // Odd = key, even = value
            final String number = parts.get(i);
            final String unit = parts.get(i + 1);
            result.put(unit, Integer.parseInt(number));
        }

        return MAPPER.convertValue(result, CustomDateTime.class);
    }
}
