package org.firezenk.kartographer.animations

import android.animation.Animator
import android.view.View
import org.firezenk.kartographer.annotations.RouteAnimation

class PushScale(animTime: Long = 500) : RouteAnimation(animTime) {

    override fun prepare(prev: Any, next: Any) {
        val width = (prev as View).width.toFloat()

        (next as View).alpha = 0f
        next.x = -width / 2
        next.scaleX = .1f
        next.scaleY = .1f
    }

    override fun animate(prev: Any, next: Any, block: () -> Unit) {
        val width = (prev as View).width.toFloat()

        prev.animate()
                .x(width * 1.5f)
                .alpha(0f)
                .scaleX(.1f)
                .scaleY(.1f)
                .setDuration(animTime)
                .start()

        (next as View).animate()
                .x(0f)
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(animTime)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(p0: Animator?) {}

                    override fun onAnimationEnd(p0: Animator?) {
                        block()
                    }

                    override fun onAnimationCancel(p0: Animator?) {}

                    override fun onAnimationStart(p0: Animator?) {}
                })
                .start()
    }
}