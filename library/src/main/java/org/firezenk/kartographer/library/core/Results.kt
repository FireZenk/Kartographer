package org.firezenk.kartographer.library.core

class Results(val core: Core) {

    private lateinit var observer: (Any?) -> Unit

    fun <T> sendResult(requestCode: Int, data: T?) {
        core.current()?.let {
            if (it.forResult == requestCode) {
                observer(data)
            } else {
                core.log?.d("Current route: $it, is not subscribed for the request code: $requestCode")
            }
        } ?: core.log?.d("There is no route to send results")
    }

    fun subscribeForResult(resultCallback: (Any?) -> Unit) {
        observer = resultCallback
    }
}