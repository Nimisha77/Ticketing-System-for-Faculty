package edu.gatech.cs6301.backend3.models;

import com.google.gson.annotations.SerializedName;

public enum Status {
    @SerializedName("open")
    OPEN,
    @SerializedName("closed")
    CLOSED,
    @SerializedName("needs-attention")
    NEEDS_ATTENTION,
    INVALID
}
