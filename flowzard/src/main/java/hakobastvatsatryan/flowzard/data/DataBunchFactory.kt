package hakobastvatsatryan.flowzard.data

import android.os.Bundle
import hakobastvatsatryan.flowzard.data.BundleDataBunch
import hakobastvatsatryan.flowzard.data.DataBunch

object DataBunchFactory {
	fun create(): DataBunch {
		return BundleDataBunch(Bundle())
	}
}