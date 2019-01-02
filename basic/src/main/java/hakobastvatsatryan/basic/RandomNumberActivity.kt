package hakobastvatsatryan.basic

import android.os.Bundle
import hakobastvatsatryan.flowzard.activity.FlowActivity
import hakobastvatsatryan.flowzard.router.Navigator
import hakobastvatsatryan.flowzard.router.NullFlowNavigator
import kotlinx.android.synthetic.main.activity_random_number.*

class RandomNumberActivity : FlowActivity() {

	override val navigator: Navigator
		get() = NullFlowNavigator(this)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_random_number)

		randomizeButton.setOnClickListener {
			numberTextView.text = (Math.random() * 99999).toInt().toString()
		}
		sendButton.setOnClickListener {
			numberTextView.text.toString().toIntOrNull()?.let { number ->
				flow.sendMessage(RandomNumberMessage.code, RandomNumberMessage(number))
			}
		}
	}
}
