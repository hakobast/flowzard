package hakobastvatsatryan.flowzard.router

import hakobastvatsatryan.flowzard.Result
import hakobastvatsatryan.flowzard.router.Command
import hakobastvatsatryan.flowzard.router.Navigator

class Router {
	var navigator: Navigator? = null

	fun startFlow(id: String, instanceId: String, requestCode: Int? = null, data: Any? = null) {
		this.applyCommands(Command.StartFlow(id, instanceId, requestCode, data))
	}

	fun endFlow(result: Result? = null) {
		this.applyCommands(Command.EndFlow(result))
	}

	fun navigateTo(screenKey: String, data: Any? = null) {
		this.applyCommands(Command.Forward(screenKey, data))
	}

	fun replace(screenKey: String, data: Any? = null) {
		this.applyCommands(Command.Replace(screenKey, data))
	}

	fun add(screenKey: String, data: Any? = null) {
		this.applyCommands(Command.Add(screenKey, data))
	}

	fun setTo(screenKey: String, data: Any? = null) {
		this.applyCommands(Command.BackTo(), Command.Forward(screenKey, data))
	}

	fun exit() {
		this.applyCommands(Command.BackTo(), Command.Back)
	}

	fun backTo(screenKey: String) {
		this.applyCommands(Command.BackTo(screenKey))
	}

	fun back() {
		this.applyCommands(Command.Back)
	}

	private fun applyCommands(vararg commands: Command) {
		this.navigator!!.applyCommands(arrayOf(*commands))
	}
}