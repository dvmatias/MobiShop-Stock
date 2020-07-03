package com.cmdv.components.colorquantity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatSpinner
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.cmdv.components.R
import com.cmdv.core.helpers.SimpleTextWatcher
import com.cmdv.core.utils.logErrorMessage

internal class RecyclerColorQuantityAdapter(
    private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val mutableLiveItemList: MutableLiveData<ArrayList<Pair<String, Int>>> = MutableLiveData()

    private var itemsList: ArrayList<Pair<String, Int>> = arrayListOf()

    private lateinit var viewMode: Mode

    interface AbracadabraInterface {
        fun onItemUpdate(position: Int, colorName: String, colorQuantity: Int)
        fun onItemDeleted(pairPosition: Int)
    }

    private val listener = object : AbracadabraInterface {
        override fun onItemUpdate(position: Int, colorName: String, colorQuantity: Int) {
            logErrorMessage("${RecyclerColorQuantityAdapter::class.java.simpleName} onItemUpdate()")
            itemsList[position] = Pair(colorName, colorQuantity)
            mutableLiveItemList.value = itemsList
        }

        override fun onItemDeleted(pairPosition: Int) {
            logErrorMessage("${RecyclerColorQuantityAdapter::class.java.simpleName} onItemDeleted()")
            itemsList.remove(itemsList[pairPosition])
            mutableLiveItemList.value = itemsList
            notifyDataSetChanged()
        }
    }

    internal fun setNoEditModeAdapter(data: ArrayList<ComponentProductColorView.ColorQuantity>) {
        logErrorMessage("${RecyclerColorQuantityAdapter::class.java.simpleName} setNoEditModeAdapter()")
        for (i in 0 until data.size) {
            logErrorMessage("${RecyclerColorQuantityAdapter::class.java.simpleName} $i")
            itemsList[i] = Pair(data[i].name, data[i].quantity)
        }
        this.viewMode = Mode.NO_EDIT
        notifyDataSetChanged()
    }

    fun setEditModeAdapter() {
        logErrorMessage("${RecyclerColorQuantityAdapter::class.java.simpleName} setEditModeAdapter()")
        this.viewMode = Mode.EDIT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        logErrorMessage("${RecyclerColorQuantityAdapter::class.java.simpleName} onCreateViewHolder()")
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_color_quantity_recycler, parent, false)
        return ColorQuantityItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        logErrorMessage("${RecyclerColorQuantityAdapter::class.java.simpleName} onBindViewHolder()")
        (holder as ColorQuantityItemViewHolder)
            .bindView(itemsList[position], viewMode, context, position, listener)
    }

    override fun getItemCount(): Int =
        this.itemsList.size

    private fun getItem(key: Int): Pair<String, Int>? =
        this.itemsList[key]

    /**
     * View holder.
     */
    class ColorQuantityItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val editTextColorName =
            itemView.findViewById<AppCompatEditText>(R.id.editTextColorName)
        private val spinnerColorQuantity =
            itemView.findViewById<AppCompatSpinner>(R.id.spinnerColorQuantity)
        private val imageViewDeleteViewButton =
            itemView.findViewById<AppCompatImageView>(R.id.imageViewDeleteViewButton)
        private var colorName: String = ""
        private var colorQuantity: Int = 0
        private lateinit var listener: AbracadabraInterface
        private lateinit var spinnerAdapter: SpinnerColorQuantityAdapter

        fun bindView(
            pair: Pair<String, Int>,
            viewMode: Mode,
            context: Context,
            pairPosition: Int,
            listener: AbracadabraInterface
        ) {
            logErrorMessage("${RecyclerColorQuantityAdapter::class.java.simpleName} bindView()")
            // TODO Manage case NO_EDIT mode
            if (Mode.EDIT == viewMode) {
                this.listener = listener
                editTextColorName.setText(pair.first)
                editTextColorName.addTextChangedListener(object : SimpleTextWatcher() {
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        colorName = s?.toString() ?: ""
                        updatePair(pairPosition)
                    }
                })

                spinnerAdapter = SpinnerColorQuantityAdapter(context)
                spinnerColorQuantity.adapter = spinnerAdapter
                spinnerColorQuantity.setSelection(spinnerAdapter.getItemPosition(pair.second.toString()))
                spinnerColorQuantity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(p0: AdapterView<*>?) { }

                    override fun onItemSelected(adapter: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                        colorQuantity = if (position == 0) 0 else (spinnerColorQuantity.selectedItem as String).toInt()
                        updatePair(pairPosition)
                    }
                }

                imageViewDeleteViewButton.setOnClickListener {
                    deletePair(pairPosition)
                }
            }
        }

        private fun updatePair(pairPosition: Int) {
            logErrorMessage("${RecyclerColorQuantityAdapter::class.java.simpleName} updatePair()")
            listener.onItemUpdate(pairPosition, colorName, colorQuantity)
        }

        private fun deletePair(pairPosition: Int) {
            logErrorMessage("${RecyclerColorQuantityAdapter::class.java.simpleName} deletePair()")
            listener.onItemDeleted(pairPosition)
        }

    }

    /**
     * Add the view to add a color-quantity pair view if possible.
     */
    fun addEditableViewIfPossible() {
        logErrorMessage("${RecyclerColorQuantityAdapter::class.java.simpleName} addEditableViewIfPossible()")
        if (canAddNewView()) {
            val positionToAdd: Int = itemCount
            this.itemsList.add(itemCount, Pair("", 0))
            notifyDataSetChanged()
        } else {
            // TODO Manage case when is not possible to add a new view.
        }
    }

    private fun canAddNewView(): Boolean {
        logErrorMessage("${RecyclerColorQuantityAdapter::class.java.simpleName} canAddNewView()")
        if (this.itemsList.isEmpty() || areAllFieldsValid()) {
            return true
        }
        return false
    }

    private fun areAllFieldsValid(): Boolean {
        logErrorMessage("${RecyclerColorQuantityAdapter::class.java.simpleName} areAllFieldsValid()")
        for (i in 0 until itemsList.size) {
            logErrorMessage("${RecyclerColorQuantityAdapter::class.java.simpleName} $i")
            val pair = itemsList[i] ?: return false
            if (pair.first.isEmpty()) return false
            if (pair.second == 0) return false
        }
        return true
    }

}
