package hakobastvatsatryan.basic

import hakobastvatsatryan.flowzard.Flow
import hakobastvatsatryan.flowzard.FlowManager

class DefaultFlowManager : FlowManager() {

	override fun createMainFlow(): Flow {
		return MainFlow(this)
	}

	override fun createFlow(id: String): Flow {
		return when (id) {
			Flows.RANDOM_NUMBER -> RandomNumberFlow(this)
			else -> throw RuntimeException("Cannot find flow for id=$id")
		}
	}
}