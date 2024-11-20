package com.davidarbana.secondhandclother.model;

public record GarmentRequest(String type,
                             String description,
                             Long publisherId,
                             String size,
                             Double price) {
}
