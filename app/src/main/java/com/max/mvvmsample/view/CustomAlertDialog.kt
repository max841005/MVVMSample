package com.max.mvvmsample.view

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.View.VISIBLE
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.MATCH_PARENT
import androidx.databinding.DataBindingUtil
import com.max.mvvmsample.R
import com.max.mvvmsample.databinding.LayoutAlertDialogBinding

@Suppress("unused")
class CustomAlertDialog(
    context: Context
) {

    private var binding: LayoutAlertDialogBinding

    private val activity = context as AppCompatActivity

    private val view = View.inflate(context, R.layout.layout_alert_dialog, null)
    private var popupWindow: PopupWindow

    init {

        binding = DataBindingUtil.bind(view)!!

        popupWindow = PopupWindow(view, MATCH_PARENT, MATCH_PARENT, false).apply {
            animationStyle = R.style.AlertDialogAnimation
            isClippingEnabled = false
        }
    }

    fun setTitle(title: Int) = binding.title.setText(title)

    fun setTitle(title: String) = binding.title.apply { text = title }

    fun setTitle(title: Int, color: Int) = binding.title.apply {

        setText(title)

        setTextColor(color)
    }

    fun setMessage(message: Int) = binding.message.setText(message)

    fun setMessage(message: String) = binding.message.apply { text = message }

    fun setPositiveButton(text: Int) = binding.positive.apply {

        setText(text)

        setOnClickListener { view -> view.postDelayed({ popupWindow.dismiss() }, 100) }
    }

    fun setPositiveButton(text: Int, listener: View.OnClickListener) = binding.positive.apply {

        setText(text)

        setOnClickListener { view ->

            view.postDelayed({

                popupWindow.dismiss()

                listener.onClick(view)

            }, 100)
        }
    }

    fun setNegativeButton(text: Int) = binding.negative.apply {

        visibility = VISIBLE

        setText(text)

        setOnClickListener { view -> view.postDelayed({ popupWindow.dismiss() }, 100) }
    }

    fun setNegativeButton(text: Int, listener: View.OnClickListener) = binding.negative.apply {

        visibility = VISIBLE

        setText(text)

        setOnClickListener { view ->

            view.postDelayed({

                popupWindow.dismiss()

                listener.onClick(view)

            }, 100)
        }
    }

    fun show() {
        
        if (!popupWindow.isShowing) {
            
            if (!activity.isFinishing) {
                activity.findViewById<View>(R.id.root).post { popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0) }
            }
        }
    }

    fun isShowing() = popupWindow.isShowing
}