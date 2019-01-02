package hakobastvatsatryan.flowzard.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import hakobastvatsatryan.flowzard.data.BundleDataBunch
import hakobastvatsatryan.flowzard.router.Navigator
import hakobastvatsatryan.flowzard.Flow
import hakobastvatsatryan.flowzard.FlowManager
import hakobastvatsatryan.flowzard.FlowManagerProvider
import hakobastvatsatryan.flowzard.FlowRoot
import hakobastvatsatryan.flowzard.Result

class FlowActivityDelegate(
		val activity: Activity,
		val navigator: Navigator
) {
	lateinit var flowRoot: FlowRoot

	val flow: Flow
		get() {
			return flowRoot.flow
		}

	private val flowManager: FlowManager
		get() {
			return (activity.applicationContext as FlowManagerProvider).getProvideManager()
		}

	private val isMain = activity.intent?.action == Intent.ACTION_MAIN

	fun onCreate(savedInstanceState: Bundle?) {
		flowRoot = if (isMain) {
			flowManager.getOrRestoreMainFlow()
		} else {
			val id = activity.intent.getStringExtra("flow-id")
			val instanceId = activity.intent.getStringExtra("flow-instance-id")
			flowManager.getOrRestoreFlow(id, instanceId)
		}
		flow.router.navigator = navigator

		flowRoot.onCreate(
				savedInstanceState?.let { BundleDataBunch(it) },
				activity.intent?.extras?.let { BundleDataBunch(it) })
	}

	fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		flowRoot.onResult(requestCode, codeToResult(resultCode, data))
	}

	fun onResume() {
		if (flow.router.navigator == null) {
			flow.router.navigator = navigator
		}
	}

	fun onPause() {
		flow.router.navigator = null
	}

	fun onDestroy() {
		if (!activity.isChangingConfigurations) {
			if (isMain) {
				flowManager.removeMainFlow()
			} else {
				flowManager.removeFlow(activity.intent.getStringExtra("flow-instance-id"))
			}
		}
	}

	private fun codeToResult(resultCode: Int, data: Intent?): Result {
		return when (resultCode) {
			Activity.RESULT_OK -> data?.extras?.let { Result.SUCCESS(BundleDataBunch(it)) }
					?: Result.SUCCESS()
			Activity.RESULT_CANCELED -> Result.CANCEL
			else -> Result.CANCEL
		}
	}
}