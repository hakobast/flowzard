package hakobastvatsatryan.flowzard

import hakobastvatsatryan.flowzard.data.DataBunch
import hakobastvatsatryan.flowzard.router.Router
import java.util.HashMap

typealias MessageHandler = (message: Any) -> Unit

abstract class Flow(private val flowManager: FlowManager) {

	val router: Router = Router()

	internal lateinit var flowRoot: FlowRoot
	private val listeners: HashMap<String, MessageHandler> = hashMapOf()

	fun sendMessage(code: String, message: Any) {
		onMessage(code, message)
	}

	fun setMessageListener(code: String, listener: MessageHandler) {
		listeners[code] = listener
	}

	fun removeMessageListener(code: String) {
		listeners.remove(code)
	}

	protected fun newFlow(id: String, requestCode: Int? = null, data: Any? = null) {
		flowManager.newFlow(this, id, requestCode, data)
	}

	protected fun endFlow(result: Result? = null) {
		flowManager.endFlow(this, result)
	}

	protected fun sendMessageFromFlow(code: String, message: Any) {
		listeners[code]?.invoke(message)
	}

	protected open fun onCreate(savedInstance: DataBunch?, data: DataBunch?) {
	}

	protected open fun onFlowResult(requestCode: Int, result: Result) {
	}

	protected open fun onMessage(code: String, message: Any) {
	}

	internal open fun internalOnCreate(savedInstance: DataBunch?, data: DataBunch?) {
		onCreate(savedInstance, data)
	}

	internal open fun internalOnFlowResult(requestCode: Int, result: Result) {
		onFlowResult(requestCode, result)
	}
}