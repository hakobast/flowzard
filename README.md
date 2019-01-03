# Flowzard Features
Isolates navigation from UI and Business logic by providing simple navigation mechanism.

Encapsulates exact implementation. It means navigation can be organized across Activities, Fragments or Views.

Survives configuration changes, can save his state and restore after process kill.

## Using Flow

Gradle:

```groovy
implementation 'com.squareup.flow:flow:1.0.0-alpha3'
```

Create flows.

```kotlin
object Flows {
	const val LOGIN = "login"
}

class MainFlow(flowManager: FlowManager) : Flow(flowManager) {

	override fun onMessage(code: String, message: Any) {
		super.onMessage(code, message)
		if(code == "main" && message == "login"){
		    newFlow(Flows.LOGIN)
		} 
	}
}

class LoginFlow(flowManager: FlowManager) : Flow(flowManager) {

    override fun onCreate(savedInstance: DataBunch?, data: DataBunch?) {
        super.onCreate(savedInstance, data)
        router.navigateTo("sign-up")
    }
    
	override fun onMessage(code: String, message: Any) {
		super.onMessage(code, message)
		if(code == "sign-up"){
		    when(message){
		        "complete" -> endFlow()
		        "have-account" -> router.navigateTo("login")
		    }
		}else if(code == "login" && message == "complete"){
		    endFlow()
		}
	}
}
```

Extend Flow manager 

```kotlin
class DefaultFlowManager : FlowManager() {

	override fun createMainFlow(): Flow {
		return MainFlow(this)
	}

	override fun createFlow(id: String): Flow {
		return when (id) {
			Flows.LOGIN -> LoginFlow(this)
			else -> throw RuntimeException("Cannot find flow for id=$id")
		}
	}
}
```
and provide it in Application class.
```kotlin
class App : Application(), FlowManagerProvider {
    private val flowManager = DefaultFlowManager() 
    override fun getProvideManager(): FlowManager {
        return flowManager
    }
}
```

Create flow Activity. 

```kotlin
class MainActivity : FlowActivity() {

    override val navigator: Navigator
        get() = object : SimpleFlowNavigator(this) {

            override fun getActivityIntent(id: String, data: Any?): Intent {
                return when (id) {
                    Flows.LOGIN -> Intent(activity, LoginActivity::class.java)
                    else -> throw RuntimeException("Cannot find activity for id=$id")
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginButton.setOnClickListener {
            flow.sendMessage("main", "login")
        }
    }
}
```

By default library provides FlowActivity.kt to easily integrate flowzard via activities.

### Flowzard functionality
Flow provides navigation(with results) across flows.
```kotlin
protected fun newFlow(id: String, requestCode: Int? = null, data: Any? = null) // creates new flow
protected fun endFlow(result: Result? = null) // finishes current flow
protected open fun onFlowResult(requestCode: Int, result: Result) // receives result from flow 
```
Flow also lets screens to send/receive messages to/from flow.
```kotlin
protected fun sendMessageFromFlow(code: String, message: Any) // send message to screen.
protected open fun onMessage(code: String, message: Any) // receives result from screen.
fun sendMessage(code: String, message: Any) // send message from screen.
fun setMessageListener(code: String, listener: MessageHandler) // register to messages in screen.
fun removeMessageListener(code: String) // unregister from messages in screen.
```

Navigation in flow between screens via Router.kt using Navigator.kt which must be provided(for example in FlowActivity.kt).
```kotlin
fun navigateTo(screenKey: String, data: Any? = null) // adds new screen to backstack
fun replace(screenKey: String, data: Any? = null) // replaces current screen with new one
fun add(screenKey: String, data: Any? = null) // adds new screen
fun setTo(screenKey: String, data: Any? = null) // clears backstack and adds new screen to it
fun exit() // clears backstack
fun backTo(screenKey: String) // pops backstack until screen.
fun back() // pops backstack
```

### Surviving configuration changes and process death
The Api built having in mind configuration change and process death problems. Flows survive configuration changes and restores its state when process dies.

## Sample projects

* [Basic Sample](basic) - Fully configured Flow.