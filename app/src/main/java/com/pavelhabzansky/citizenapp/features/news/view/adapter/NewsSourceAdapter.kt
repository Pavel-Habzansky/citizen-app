package com.pavelhabzansky.citizenapp.features.news.view.adapter

import android.animation.ValueAnimator
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.animation.doOnEnd
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.pavelhabzansky.citizenapp.BR
import com.pavelhabzansky.citizenapp.R
import com.pavelhabzansky.citizenapp.core.NEWS_ANIMATE_SPEED
import com.pavelhabzansky.citizenapp.core.NEWS_CLOSED_HEIGHT
import com.pavelhabzansky.citizenapp.core.NEWS_MAX_LINES
import com.pavelhabzansky.citizenapp.databinding.ItemNewsItemBinding
import com.pavelhabzansky.citizenapp.features.news.view.vo.NewsItemViewObject

class NewsSourceAdapter(
    private val onLinkClick: (String?) -> Unit
) : RecyclerView.Adapter<NewsSourceAdapter.NewsSourceViewHolder>() {

    private var items: List<NewsItemViewObject> = emptyList()

    fun setItems(newItems: List<NewsItemViewObject>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: NewsSourceViewHolder, position: Int) {
        holder.bind(model = items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsSourceViewHolder {
        val binding = DataBindingUtil.inflate<ItemNewsItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_news_item,
            parent,
            false
        )

        return NewsSourceViewHolder(binding = binding, onLinkClick = onLinkClick)
    }

    inner class NewsSourceViewHolder(
        private val binding: ItemNewsItemBinding,
        private val onLinkClick: (String?) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        private var originalHeight: Int = -1
        private var closedHeight: Int = -1
        private var open: Boolean = false

        fun bind(model: NewsItemViewObject) {
            binding.setVariable(BR.item, model)
            binding.executePendingBindings()

            binding.root.viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    originalHeight = binding.root.height

                    val scale = binding.root.context.resources.displayMetrics.density
                    closedHeight = (NEWS_CLOSED_HEIGHT * scale + 0.5f).toInt()
                    binding.root.layoutParams.height = closedHeight
                    binding.newsText.maxLines = NEWS_MAX_LINES
                    binding.newsText.ellipsize = TextUtils.TruncateAt.END
                    binding.root.requestLayout()
                    binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)

                    binding.testButton.visibility = View.GONE
                }
            })

            binding.root.setOnClickListener {
                if (open) {
                    toggleClose()
                } else {
                    toggleOpen()
                }

            }
        }

        private fun toggleOpen() {
            binding.testButton.visibility = View.VISIBLE
            binding.testButton.setOnClickListener { onLinkClick(binding.item?.url) }
            val valueAnimator = ValueAnimator.ofInt(closedHeight, originalHeight)
                .setDuration(NEWS_ANIMATE_SPEED)

            valueAnimator.addUpdateListener {
                val value = it.animatedValue as Int
                val layoutParams = binding.root.layoutParams
                layoutParams.height = value
                binding.root.layoutParams = layoutParams
                binding.newsText.maxLines = Int.MAX_VALUE
                binding.newsText.ellipsize = null
            }
            valueAnimator.start()
            open = true
        }

        private fun toggleClose() {
            binding.testButton.visibility = View.GONE
            val valueAnimator = ValueAnimator.ofInt(originalHeight, closedHeight)
                .setDuration(NEWS_ANIMATE_SPEED)

            valueAnimator.addUpdateListener {
                val value = it.animatedValue as Int
                val layoutParams = binding.root.layoutParams
                layoutParams.height = value
                binding.root.layoutParams = layoutParams
                binding.newsText.maxLines = NEWS_MAX_LINES
                binding.newsText.ellipsize = TextUtils.TruncateAt.END
            }
            valueAnimator.start()
            open = false
        }

    }

}
