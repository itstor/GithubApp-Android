package com.itstor.githubapp.models

import com.google.gson.annotations.SerializedName

data class SearchResponse<T>(
    @field:SerializedName("total_count")
    val totalCount: Int,

    @field:SerializedName("incomplete_results")
    val incompleteResults: Boolean,

    val items: List<T>
)
