package hakobastvatsatryan.flowzard.router

import android.content.Intent
import android.support.v7.app.AppCompatActivity

class NullFlowNavigator(activity: AppCompatActivity) : SimpleFlowNavigator(activity) {

	override fun getActivityIntent(id: String, data: Any?): Intent {
		throw RuntimeException("Cannot find activity for id=$id")
	}
}