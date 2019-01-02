package hakobastvatsatryan.flowzard.data

interface DataBunch {
	fun getBunch(key: String): DataBunch?
	fun setBunch(key: String, dataBunch: DataBunch)
	fun getString(key: String): String?
	fun setString(key: String, value: String)
	fun setInt(key: String, value: Int)
	fun getInt(key: String): Int?
	fun getInt(key: String, default: Int): Int
}