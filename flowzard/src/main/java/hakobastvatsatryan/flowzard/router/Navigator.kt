package hakobastvatsatryan.flowzard.router

interface Navigator {
	fun applyCommands(commands: Array<Command>)
}