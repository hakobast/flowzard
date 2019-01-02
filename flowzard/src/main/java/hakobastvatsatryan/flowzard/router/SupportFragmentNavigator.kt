package hakobastvatsatryan.flowzard.router

import android.app.Activity
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import hakobastvatsatryan.flowzard.Result
import hakobastvatsatryan.flowzard.data.BundleDataBunch
import java.util.*

abstract class SupportFragmentNavigator(val activity: AppCompatActivity,
										private val containerId: Int
) : Navigator {

	protected abstract fun getActivityIntent(id: String, data: Any?): Intent
	protected abstract fun createFragment(screenKey: String, data: Any?): Fragment?
	protected abstract fun onExit()

	protected val stack: LinkedList<String?> = LinkedList()

	private val fragmentManager: FragmentManager
		get() {
			return activity.supportFragmentManager
		}

	override fun applyCommands(commands: Array<Command>) {
		this.fragmentManager.executePendingTransactions()
		this.copyStackFromFragmentManager()
		commands.forEach { applyCommand(it) }
	}

	protected open fun applyCommand(command: Command) {
		when (command) {
			is Command.StartFlow -> processStartFlowCommand(command)
			is Command.EndFlow -> processEndFlowCommand(command)
			is Command.Forward -> processForwardCommand(command)
			is Command.Replace -> processReplaceCommand(command)
			is Command.BackTo -> processBackToCommand(command)
			is Command.Back -> processBackCommand(command)
			is Command.Add -> processAddCommand(command)
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

	protected open fun processForwardCommand(command: Command.Forward) {
		val fragment = this.createFragment(command.screenKey, command.data)
		if (fragment == null) {
			this.unknownScreen(command)
		} else {
			val transaction = this.fragmentManager.beginTransaction()
			this.processTransaction(
					command,
					this.fragmentManager.findFragmentById(this.containerId),
					fragment,
					transaction
			)
			transaction.replace(this.containerId, fragment)
					.addToBackStack(command.screenKey)
					.commit()
			this.stack.add(command.screenKey)
		}
	}

	protected open fun processReplaceCommand(command: Command.Replace) {
		val fragment = this.createFragment(command.screenKey, command.data)
		if (fragment == null) {
			this.unknownScreen(command)
		} else {
			if (this.stack.size > 0) {
				this.fragmentManager.popBackStack()
				this.stack.pop()
			}

			val transaction = this.fragmentManager.beginTransaction()
			this.processTransaction(
					command,
					this.fragmentManager.findFragmentById(this.containerId),
					fragment,
					transaction
			)
			transaction.replace(this.containerId, fragment)
					.addToBackStack(command.screenKey)
					.commit()
			this.stack.add(command.screenKey)
		}
	}

	protected open fun processAddCommand(command: Command.Add) {
		val fragment = this.createFragment(command.screenKey, command.data)
		if (fragment == null) {
			this.unknownScreen(command)
		} else {
			val transaction = this.fragmentManager.beginTransaction()
			this.processTransaction(
					command,
					this.fragmentManager.findFragmentById(this.containerId),
					fragment,
					transaction
			)
			transaction.add(this.containerId, fragment)
					.addToBackStack(command.screenKey)
					.commit()
			this.stack.add(command.screenKey)
		}
	}

	protected open fun processBackToCommand(command: Command.BackTo) {
		val key = command.screenKey
		if (key == null) {
			this.backToRoot()
		} else {
			val index = this.stack.indexOf(key)
			val size = this.stack.size
			if (index != -1) {
				for (i in 1 until size - index) {
					this.stack.pop()
				}
				this.fragmentManager.popBackStack(key, 0)
			} else {
				this.backToUnexisting(command.screenKey)
			}
		}
	}

	protected open fun processBackCommand(command: Command.Back) {
		if (this.stack.size > 0) {
			this.fragmentManager.popBackStack()
			this.stack.pop()
		} else {
			this.onExit()
		}
	}

	private fun backToRoot() {
		this.fragmentManager.popBackStack(null, 1)
		this.stack.clear()
	}

	protected open fun processTransaction(command: Command,
										  currentFragment: Fragment?,
										  nextFragment: Fragment,
										  fragmentTransaction: FragmentTransaction
	) {
	}

	protected open fun unknownScreen(command: Command) {
		throw RuntimeException("Can't create a screen for passed screenKey.")
	}

	protected open fun backToUnexisting(screenKey: String) {
		this.backToRoot()
	}

	private fun copyStackFromFragmentManager() {
		val stackSize = this.fragmentManager.backStackEntryCount

		for (i in 0 until stackSize) {
			this.stack.add(this.fragmentManager.getBackStackEntryAt(i).name)
		}
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