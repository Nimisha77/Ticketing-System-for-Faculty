package edu.gatech.cs6301.backend3.models;

import com.google.gson.annotations.SerializedName;

public enum Category {
    @SerializedName("travel authorization")
    TRAVEL_AUTHORIZATION,

    @SerializedName("reimbursment")
    REIMBURSEMENT,

    @SerializedName("meeting organization")
    MEETING_ORGANIZATION,

    @SerializedName("student hiring")
    STUDENT_HIRING,
    @SerializedName("proposals")
    PROPOSALS,
    @SerializedName("miscellaneous")
    MISCELLANEOUS,

    @SerializedName("invalid")
    INVALID
}
