package hakobastvatsatryan.flowzard

import java.util.*

abstract class FlowManager {
	private val map: HashMap<String, Flow> = hashMapOf()

	protected abstract fun createFlow(id: String): Flow
	protected abstract fun createMainFlow(): Flow

	internal fun newFlow(flow: Flow, id: String, requestCode: Int? = null, data: Any? = null) {
		val newFlow = createFlow(id)
		initFlow(newFlow)

		val instanceId = UUID.randomUUID().toString()
		map[instanceId] = newFlow
		flow.router.startFlow(id, instanceId, requestCode, data)
	}

	internal fun endFlow(flow: Flow, result: Result?) {
		flow.router.endFlow(result)
	}

	internal fun getOrRestoreMainFlow(): FlowRoot {
		return if (map.containsKey("main")) {
			map["main"]!!.flowRoot
		} else {
			val flow = createMainFlow()
			initFlow(flow)
			map["main"] = flow
			flow.flowRoot
		}
	}

	internal fun getOrRestoreFlow(id: String, instanceId: String): FlowRoot {
		return if (map.containsKey(instanceId)) {
			map[instanceId]!!.flowRoot
		} else {
			val flow = createFlow(id)
			initFlow(flow)
			map[instanceId] = flow
			flow.flowRoot
		}
	}

	internal fun removeFlow(instanceId: String) {
		map.remove(instanceId)
	}

	internal fun removeMainFlow() {
		map.remove("main")
	}

	private fun initFlow(flow: Flow) {
		flow.flowRoot = FlowRoot(flow)
	}
}