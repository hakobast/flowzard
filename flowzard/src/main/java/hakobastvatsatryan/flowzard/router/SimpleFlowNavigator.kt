package hakobastvatsatryan.flowzard.router

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import hakobastvatsatryan.flowzard.Result
import hakobastvatsatryan.flowzard.data.BundleDataBunch

abstract class SimpleFlowNavigator(val activity: AppCompatActivity
) : Navigator {

	protected abstract fun getActivityIntent(id: String, data: Any?): Intent

	override fun applyCommands(commands: Array<Command>) {
		commands.forEach { applyCommand(it) }
	}

	protected open fun applyCommand(command: Command) {
		when (command) {
			is Command.StartFlow -> processStartFlowCommand(command)
			is Command.EndFlow -> processEndFlowCommand(command)
		}
	}

	private fun processStartFlowCommand(command: Command.StartFlow) {
		val intent = getActivityIntent(command.id, command.data)
		intent.putExtra("flow-id", command.id)
		intent.putExtra("flow-instance-id", command.instanceId)
		if (command.requestCode != null) {
			activity.startActivityForResult(intent, command.requestCode)
		} else {
			activity.startActivity(intent)
		}
	}

	private fun processEndFlowCommand(command: Command.EndFlow) {
		if (command.result != null) {
			activity.setResult(resultToCode(command.result), resultToData(command.result))
		}
		activity.finish()
	}

	private fun resultToCode(result: Result): Int {
		return when (result) {
			Result.CANCEL -> Activity.RESULT_CANCELED
			is Result.SUCCESS -> Activity.RESULT_OK
		}
	}

	private fun resultToData(result: Result): Intent? {
		return ((result as? Result.SUCCESS)?.data as? BundleDataBunch)?.let { Intent().putExtras(it.bundle) }
	}
}