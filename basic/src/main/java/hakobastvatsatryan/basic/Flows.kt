package hakobastvatsatryan.basic

import hakobastvatsatryan.flowzard.Flow
import hakobastvatsatryan.flowzard.FlowManager
import hakobastvatsatryan.flowzard.Result
import hakobastvatsatryan.flowzard.data.DataBunchFactory

object Flows {
	const val MAIN = "first"
	const val RANDOM_NUMBER = "random-number"
}

class MainFlow(flowManager: FlowManager) : Flow(flowManager) {

	companion object {
		const val RANDOM_NUMBER_REQUEST_CODE = 1
	}

	override fun onMessage(code: String, message: Any) {
		super.onMessage(code, message)
		if (code == MainMessage.code && message is MainMessage.OnGetRandomNumberClicked) {
			newFlow(Flows.RANDOM_NUMBER, RANDOM_NUMBER_REQUEST_CODE)
		}
	}

	override fun onFlowResult(requestCode: Int, result: Result) {
		super.onFlowResult(requestCode, result)
		if (requestCode == RANDOM_NUMBER_REQUEST_CODE && result is Result.SUCCESS) {
			result.data?.getInt("number")?.let {
				sendMessageFromFlow(MainMessage.code, MainMessage.NumberResult(it))
			}
		}
	}
}

class RandomNumberFlow(flowManager: FlowManager) : Flow(flowManager) {

	override fun onMessage(code: String, message: Any) {
		super.onMessage(code, message)
		if (code == RandomNumberMessage.code && message is RandomNumberMessage) {
			endFlow(
					Result.SUCCESS(
							DataBunchFactory.create().apply {
								setInt("number", message.value)
							}
					)
			)
		}
	}
}