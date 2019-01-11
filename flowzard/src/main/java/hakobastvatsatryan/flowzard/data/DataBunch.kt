package hakobastvatsatryan.flowzard.data

import java.io.Serializable

interface DataBunch {
	fun getBunch(key: String): DataBunch?
	fun setBunch(key: String, dataBunch: DataBunch)
	fun getString(key: String): String?
	fun setString(key: String, value: String)
	fun setInt(key: String, value: Int)
	fun getInt(key: String): Int?
	fun getInt(key: String, default: Int): Int
	fun setDouble(key: String, value: Double)
	fun getDouble(key: String): Double?
	fun getDouble(key: String, default: Double): Double
	fun setSerializable(key: String, obj: Serializable)
	fun getSerializable(key: String): Serializable?
}