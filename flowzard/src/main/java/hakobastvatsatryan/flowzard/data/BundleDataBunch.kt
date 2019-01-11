package hakobastvatsatryan.flowzard.data

import android.os.Bundle
import java.io.Serializable

class BundleDataBunch(val bundle: Bundle) : DataBunch {

	override fun getBunch(key: String): DataBunch? {
		return bundle.getBundle(key)?.let { BundleDataBunch(it) }
	}

	override fun setBunch(key: String, dataBunch: DataBunch) {
		if (dataBunch is BundleDataBunch) {
			bundle.putBundle(key, dataBunch.bundle)
		}
	}

	override fun getString(key: String): String? {
		return bundle.getString(key)
	}

	override fun setString(key: String, value: String) {
		bundle.putString(key, value)
	}

	override fun setInt(key: String, value: Int) {
		bundle.putInt(key, value)
	}

	override fun getInt(key: String): Int? {
		return bundle.get(key) as? Int
	}

	override fun getInt(key: String, default: Int): Int {
		return bundle.getInt(key, default)
	}

	override fun setDouble(key: String, value: Double) {
		bundle.putDouble(key, value)
	}

	override fun getDouble(key: String): Double? {
		return bundle.get(key) as? Double
	}

	override fun getDouble(key: String, default: Double): Double {
		return bundle.getDouble(key, default)
	}

	override fun setSerializable(key: String, obj: Serializable) {
		bundle.putSerializable(key, obj)
	}

	override fun getSerializable(key: String): Serializable? {
		return bundle.getSerializable(key)
	}
}