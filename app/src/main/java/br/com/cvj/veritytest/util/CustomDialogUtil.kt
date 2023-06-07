package br.com.cvj.veritytest.util

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.LayoutRes
import br.com.cvj.veritytest.R
import br.com.cvj.veritytest.util.extension.gone
import com.google.android.material.button.MaterialButton

class CustomDialogUtil(private val context: Context) {

    private fun getDialog(@LayoutRes layoutRes: Int, isCancellable: Boolean): Dialog {
        Dialog(context).apply {
            setContentView(layoutRes)
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setCancelable(isCancellable)
            return this
        }
    }
    fun buildDialogHorizontal(
        isCancellable: Boolean = true,
        title: String? = null,
        subTitle: String? = null,
        description: String? = null,
        primaryBtnText: String? = null,
        onPrimaryClickListener: ((dialog: Dialog) -> Unit)? = null,
        secondaryBtnText: String? = null,
        onSecondaryClickListener: ((dialog: Dialog) -> Unit)? = null
    ): Dialog {
        return createDialog(
            false,
            isCancellable,
            title,
            subTitle,
            description,
            primaryBtnText,
            onPrimaryClickListener,
            secondaryBtnText,
            onSecondaryClickListener
        )
    }

    fun buildDialogVertical(
        isCancellable: Boolean = true,
        title: String? = null,
        subTitle: String? = null,
        description: String? = null,
        primaryBtnText: String? = null,
        onPrimaryClickListener: ((dialog: Dialog) -> Unit)? = null,
        secondaryBtnText: String? = null,
        onSecondaryClickListener: ((dialog: Dialog) -> Unit)? = null
    ): Dialog {
        return createDialog(
            true,
            isCancellable,
            title,
            subTitle,
            description,
            primaryBtnText,
            onPrimaryClickListener,
            secondaryBtnText,
            onSecondaryClickListener
        )
    }

    private fun createDialog(
        isVertical: Boolean,
        isCancellable: Boolean,
        title: String? = null,
        subTitle: String? = null,
        description: String? = null,
        primaryBtnText: String? = null,
        onPrimaryClickListener: ((dialog: Dialog) -> Unit)? = null,
        secondaryBtnText: String? = null,
        onSecondaryClickListener: ((dialog: Dialog) -> Unit)? = null
    ): Dialog {
        val layout = when (isVertical) {
            true -> R.layout.custom_dialog_vertical
            else -> R.layout.custom_dialog_horizontal
        }

        val dialog = getDialog(layout, isCancellable)
        with(DialogHolder(dialog.window?.decorView)) {

            title?.let {
                titleView?.text = it
            } ?: run {
                titleView?.gone()
            }

            subTitle?.let {
                subTitleView?.text = it
            } ?: run {
                subTitleView?.gone()
            }

            description?.let {
                descriptionView?.text = it
            } ?: run {
                descriptionView?.gone()
            }

            primaryBtnText?.let {
                primaryBtnView?.text = it
            } ?: run {
                primaryBtnView?.gone()
            }

            onPrimaryClickListener?.run {
                primaryBtnView?.setOnClickListener {
                    invoke(dialog)
                }
            }

            secondaryBtnText?.let {
                secondaryBtnView?.text = it
            } ?: run {
                secondaryBtnView?.gone()
            }

            onSecondaryClickListener?.run {
                secondaryBtnView?.setOnClickListener {
                    invoke(dialog)
                }
            }

            closeView?.setOnClickListener {
                dialog.dismiss()
            }
        }

        return dialog
    }

    inner class DialogHolder(view: View?) {
        val titleView: TextView? = view?.findViewById(R.id.custom_dialog_title)
        val subTitleView: TextView? = view?.findViewById(R.id.custom_dialog_sub_title)
        val descriptionView: TextView? = view?.findViewById(R.id.custom_dialog_description)
        val primaryBtnView: Button? = view?.findViewById(R.id.custom_dialog_primary_btn)
        val secondaryBtnView: MaterialButton? = view?.findViewById(R.id.custom_dialog_secondary_btn)
        val closeView: ImageButton? = view?.findViewById(R.id.custom_dialog_close)
    }
}