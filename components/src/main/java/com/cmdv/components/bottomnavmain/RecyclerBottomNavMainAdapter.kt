package com.cmdv.components.bottomnavmain

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cmdv.components.R
import com.cmdv.core.helpers.DimensHelper.Companion.dpToPx
import com.cmdv.domain.models.ItemMainPageModel
import kotlinx.android.synthetic.main.item_styling_bottom_nav_main.view.*


class RecyclerBottomNavMainAdapter(
	private val context: Context,
	private val listener: OnItemClickListener,
	private var itemMainPageList: List<ItemMainPageModel>
) : RecyclerView.Adapter<RecyclerBottomNavMainAdapter.BottomNavItemViewHolder>() {

	companion object {

		private var isViewInitialized: Boolean = false

	}

	init {
		setupItemList()
	}

	/**
	 * Remove not enabled tabs.
	 */
	private fun setupItemList() {
		val mockItemList = itemMainPageList.filter { item ->
			item.enable
		}
		itemMainPageList = mockItemList
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomNavItemViewHolder =
		BottomNavItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_styling_bottom_nav_main, parent, false))

	override fun onBindViewHolder(holder: BottomNavItemViewHolder, position: Int) {
		val params = holder.itemView.container.layoutParams
		val displayMetrics = context.resources.displayMetrics
		val dpWidth = displayMetrics.widthPixels / displayMetrics.density
		params.width = dpToPx(context, (dpWidth / itemCount)).toInt()

		holder.bindItem(itemMainPageList[position], listener, position)

		if (position == 0 && !isViewInitialized) {
			isViewInitialized = true
			holder.initialize()
		}
	}

	override fun getItemCount(): Int =
		itemMainPageList.size

	fun updateSelected(selectedView: View?, unselectedView: View?) {
		selectedView?.let { setSelected(it) }
		unselectedView?.let { setUnselected(it) }
	}

	private fun setSelected(selectedView: View) {
		selectedView.apply {
			tvLabel.isSelected = true
			ivIcon.isSelected = true
		}
	}

	private fun setUnselected(unselectedView: View) {
		unselectedView.apply {
			tvLabel.isSelected = false
			ivIcon.isSelected = false
		}
	}

	/**
	 * Holder class.
	 */
	class BottomNavItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		private lateinit var listener: OnItemClickListener

		fun bindItem(itemMainPage: ItemMainPageModel, listener: OnItemClickListener, position: Int) {
			this.listener = listener

			itemView.apply {
				ivIcon.setImageDrawable(ContextCompat.getDrawable(context, itemMainPage.icon))
				tvLabel.text = itemMainPage.label
				ivBadge.visibility = View.GONE
				container.setOnClickListener { handleItemClick(position) }
				tag = itemMainPage.tag
			}
		}

		/**
		 * At init, the item selected is "HOME" item.
		 */
		fun initialize() {
			itemView.apply {
				performClick()
				tvLabel.isSelected = true
				ivIcon.isSelected = true
			}
		}

		private fun handleItemClick(position: Int) {
			listener.onItemClick(position)
		}

	}

	/**
	 *
	 */
	interface OnItemClickListener {
		fun onItemClick(position: Int)
	}

}