package org.firezenk.kartographer.pages.page4

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.firezenk.kartographer.R
import org.firezenk.kartographer.annotations.RoutableActivity

@RoutableActivity
class Page4Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page4)
    }

}