package org.firezenk.kartographer.animations

import android.animation.Animator
import android.view.View
import org.firezenk.kartographer.annotations.RouteAnimation

class PushLeft(animTime: Long = 500) : RouteAnimation(animTime) {

    override fun prepare(prev: Any, next: Any) {
        val width = (prev as View).width.toFloat()
        (next as View).x = width
    }

    override fun animate(prev: Any, next: Any, block: () -> Unit) {
        val width = (prev as View).width.toFloat()

        prev.animate().x(-width).setDuration(animTime).start()
        (next as View).animate().x(0f).setDuration(animTime).setListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {}

            override fun onAnimationEnd(p0: Animator?) {
                block()
            }

            override fun onAnimationCancel(p0: Animator?) {}

            override fun onAnimationStart(p0: Animator?) {}
        }).start()
    }
}