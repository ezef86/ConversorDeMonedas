package com.aluracursos.conversordemonedas;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

public record Monedas(
        String result, // "success" or "error"
        String documentation,
        @SerializedName("terms_of_use") String termsOfUse,
        @SerializedName("time_last_update_unix") long timeLastUpdateUnix,
        @SerializedName("time_last_update_utc") String timeLastUpdateUtc,
        @SerializedName("time_next_update_unix") long timeNextUpdateUnix,
        @SerializedName("time_next_update_utc") String timeNextUpdateUtc,
        @SerializedName("base_code") String baseCode,
        @SerializedName("conversion_rates") Map<String, Double> conversionRates
) {
}
