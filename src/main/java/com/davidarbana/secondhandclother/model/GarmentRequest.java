package com.davidarbana.secondhandclother.model;

import jakarta.annotation.Nullable;

public record GarmentRequest(@Nullable String type,
                             @Nullable String description,
                             @Nullable Long publisherId,
                             @Nullable String size,
                             @Nullable double price) {
}
