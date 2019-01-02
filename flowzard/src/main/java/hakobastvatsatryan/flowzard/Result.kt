package hakobastvatsatryan.flowzard

import hakobastvatsatryan.flowzard.data.DataBunch

sealed class Result {
    object CANCEL : Result()
    class SUCCESS(val data: DataBunch? = null) : Result()
}