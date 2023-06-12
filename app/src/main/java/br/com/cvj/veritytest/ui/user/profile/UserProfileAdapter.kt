package br.com.cvj.veritytest.ui.user.profile

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.core.widget.TextViewCompat
import br.com.cvj.veritytest.R
import br.com.cvj.veritytest.databinding.ListItemUserProfileBinding
import br.com.cvj.veritytest.model.data.UserRepositoryItemResponse
import br.com.cvj.veritytest.ui.base.BaseRecyclerViewAdapter
import br.com.cvj.veritytest.ui.base.BaseViewHolder
import br.com.cvj.veritytest.util.RawUtil
import br.com.cvj.veritytest.util.extension.gone
import br.com.cvj.veritytest.util.extension.styled
import br.com.cvj.veritytest.util.extension.visible
import br.com.cvj.veritytest.util.extension.whenIsEmpty
import br.com.cvj.veritytest.util.extension.whenIsNotEmpty


class UserProfileAdapter(items: List<UserRepositoryItemResponse>):
    BaseRecyclerViewAdapter<UserRepositoryItemResponse>(items.toMutableList()) {

    override fun getViewHolder(view: View) = ViewHolder(ListItemUserProfileBinding.bind(view))

    override fun getLayoutRes() = R.layout.list_item_user_profile

    inner class ViewHolder(val binding: ListItemUserProfileBinding):
    BaseViewHolder<UserRepositoryItemResponse>(binding.root) {
        private val contextView: Context = binding.root.context
        private val repoNameText: TextView = binding.listItemUserProfileName
        private val repoDescText: TextView = binding.listItemUserProfileDescription
        private val repoStartText: TextView = binding.userProfileStars
        private val repoLangText: TextView = binding.userProfileLanguage

        override fun bind(data: UserRepositoryItemResponse) {
            bindData(data)
        }

        override fun bindData(item: UserRepositoryItemResponse) {
            with (item) {
                repoNameText.text = item.name
                repoDescText.text = if (item.description?.isEmpty() == true || item.description?.isBlank() == true || item.description == null) {
                    contextView.getString(R.string.user_profile_repos_list_no_description).styled()
                } else {
                    item.description
                }
                repoStartText.text = stargazersCount.toString()
                language.whenIsNotEmpty {
                    repoLangText.text = it
                    repoLangText.visible()
                    RawUtil.getColor(contextView.resources, R.raw.github_lang_colors, it)?.let { rawColor ->
                        TextViewCompat.setCompoundDrawableTintList(repoLangText, ColorStateList.valueOf(rawColor))
                    }?.run {
                        Color.BLACK
                    }
                }.whenIsEmpty {
                    repoLangText.gone()
                }
            }
        }
    }
}