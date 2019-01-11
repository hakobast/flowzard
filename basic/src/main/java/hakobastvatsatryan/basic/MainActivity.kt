package hakobastvatsatryan.basic

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import hakobastvatsatryan.flowzard.activity.FlowActivity
import hakobastvatsatryan.flowzard.router.Navigator
import hakobastvatsatryan.flowzard.router.SimpleFlowNavigator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : FlowActivity() {

	override val navigator: Navigator
		get() = object : SimpleFlowNavigator(this) {

			override fun getActivityIntent(id: String, data: Any?): Intent {
				return when (id) {
					Flows.RANDOM_NUMBER -> Intent(activity, RandomNumberActivity::class.java)
					else -> throw RuntimeException("Cannot find activity for id=$id")
				}
			}
		}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		getRandomNumberButton.setOnClickListener {
			flow.sendMessage(MainMessage.code, MainMessage.OnGetRandomNumberClicked)
		}

		setMessageListener(MainMessage.code) {
			if (it is MainMessage.NumberResult) {
				numberTextView.text = it.value.toString()
			}
		}
	}
}
