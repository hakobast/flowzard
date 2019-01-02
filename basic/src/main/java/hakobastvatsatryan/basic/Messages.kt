package hakobastvatsatryan.basic

sealed class MainMessage {
	companion object {
		const val code = "main"
	}
    object OnGetRandomNumberClicked : MainMessage()
    class NumberResult(val value: Int) : MainMessage()
}

class RandomNumberMessage(val value: Int) {
	companion object {
		const val code = "random-number"
	}
}