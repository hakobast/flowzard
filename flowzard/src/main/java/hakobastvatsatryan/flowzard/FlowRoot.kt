package hakobastvatsatryan.flowzard

import hakobastvatsatryan.flowzard.data.DataBunch

class FlowRoot(val flow: Flow) {

	fun onCreate(savedInstance: DataBunch?, data: DataBunch?) {
		flow.internalOnCreate(savedInstance, data)
	}

	fun onResult(requestCode: Int, result: Result) {
		flow.internalOnFlowResult(requestCode, result)
	}
}