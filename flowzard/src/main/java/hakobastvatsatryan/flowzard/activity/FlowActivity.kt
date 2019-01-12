package hakobastvatsatryan.flowzard.activity

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import hakobastvatsatryan.flowzard.Flow
import hakobastvatsatryan.flowzard.FlowProvider
import hakobastvatsatryan.flowzard.MessageHandler
import hakobastvatsatryan.flowzard.router.Navigator

abstract class FlowActivity : AppCompatActivity(), FlowProvider {

    private lateinit var flowActivityDelegate: FlowActivityDelegate

    override val flow: Flow
        get() = flowActivityDelegate.flow

    abstract val navigator: Navigator
    open val isMain: Boolean = false

    private val lazyNav: Navigator by lazy {
        navigator
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        flowActivityDelegate = FlowActivityDelegate(this, lazyNav, isMain)
        flowActivityDelegate.onCreate(savedInstanceState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        flowActivityDelegate.onActivityResult(requestCode, resultCode, data)
    }

    override fun onPause() {
        super.onPause()
        flowActivityDelegate.onPause()
    }

    override fun onResume() {
        super.onResume()
        flowActivityDelegate.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        flowActivityDelegate.onDestroy()
    }

    fun setMessageListener(code: String, listener: MessageHandler) {
        flow.setMessageListener(code, listener)
        lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                flow.removeMessageListener(code)
            }
        })
    }
}