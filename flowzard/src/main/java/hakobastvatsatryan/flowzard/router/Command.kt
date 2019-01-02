package hakobastvatsatryan.flowzard.router

import hakobastvatsatryan.flowzard.Result

sealed class Command {
	class StartFlow(
			val id: String,
			val instanceId: String,
			val requestCode: Int? = null,
			val data: Any? = null
	) : Command()

	class EndFlow(val result: Result? = null) : Command()
	class Add(val screenKey: String, val data: Any? = null) : Command()
	class BackTo(val screenKey: String? = null, val data: Any? = null) : Command()
	class Forward(val screenKey: String, val data: Any? = null) : Command()
	class Replace(val screenKey: String, val data: Any? = null) : Command()
	object Back : Command()
}