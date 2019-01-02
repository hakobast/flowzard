package hakobastvatsatryan.basic

import android.app.Application
import hakobastvatsatryan.flowzard.FlowManager
import hakobastvatsatryan.flowzard.FlowManagerProvider

class App : Application(), FlowManagerProvider {

	private val flowManager = DefaultFlowManager()

	override fun getProvideManager(): FlowManager {
		return flowManager
	}
}