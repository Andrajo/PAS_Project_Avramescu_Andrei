package com.android.androidprojectapplication

import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.os.Bundle
import android.text.format.Time
import android.util.Property
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import kotlinx.android.synthetic.main.fragment_animation.*
import kotlinx.android.synthetic.main.fragment_animation.view.*
import java.time.Duration

class AnimationFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_animation, container, false)

        view.animateButton.setOnClickListener {
            animation(box,
                    View.TRANSLATION_Y,
                    box.translationY,
                    value(box.translationY - 400f),
                    500,
                    DecelerateInterpolator())

            animation(box,
                View.ALPHA,
                1f,
                0.0f,
                500,
                LinearInterpolator())
        }
        return view
    }

    fun animation (view: View,
                    property: Property <View, Float>,
                    from : Float,
                    to : Float,
                    duration: Long,
                    interpolator: TimeInterpolator
                    ) {
        val translation = ObjectAnimator.ofFloat(view,
            property,
            from,
            value(to))
        translation.duration = duration
        translation.interpolator = interpolator
        translation.start()

    }

    private fun value(fl: Float): Float {
        if(fl < -500) {
            return 500f
        }
        else {
            return fl
        }
    }
}