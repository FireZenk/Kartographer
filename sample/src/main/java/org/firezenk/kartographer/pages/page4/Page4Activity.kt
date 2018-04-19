package org.firezenk.kartographer.pages.page4

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_page4.*
import org.firezenk.kartographer.ExternalRoute
import org.firezenk.kartographer.R
import org.firezenk.kartographer.SampleApplication
import org.firezenk.kartographer.annotations.RoutableActivity
import org.firezenk.kartographer.library.Kartographer
import org.firezenk.kartographer.library.dsl.routeExternal
import org.firezenk.kartographer.pages.page3.Page3
import java.util.*
import javax.inject.Inject

@RoutableActivity(path = "PAGE4", flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
class Page4Activity : AppCompatActivity() {

    @Inject lateinit var router: Kartographer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page4)

        SampleApplication.component.injectTo(this)

        val param = router.bundle<Bundle>()?.getInt("TESTINT")

        button.setOnClickListener {
            router next routeExternal {
                target = ExternalRoute()
                params = mapOf<String, Any>("test" to "HEY!")
            }
        }
    }

    override fun onBackPressed() {
        router.backOnPath({ super.onBackPressed() })
    }

    override fun finish() {
        router.sendResult(Page3.REQUEST_CODE, "Result from activity " + Random().nextInt())
        super.finish()
    }
}