package com.cmdv.feature

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.cmdv.domain.models.LiveDataStatusWrapper
import com.cmdv.domain.models.ProductModel
import com.cmdv.feature.adapters.SectionsPagerAdapter
import com.cmdv.feature.fragments.EventSearchProductObserver
import com.cmdv.feature.fragments.ObservableSearch
import kotlinx.android.synthetic.main.activity_search.*
import org.koin.android.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity(), ObservableSearch {

    private val viewModel: SearchActivityViewModel by viewModel()

    private var queryString: String = ""

    private lateinit var sectionsPagerAdapter: SectionsPagerAdapter

    private var searchProductsObservers: ArrayList<EventSearchProductObserver> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        setupToolbar()
        setupSearchView()

        sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        viewPager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(viewPager)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = null
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupSearchView() {
        searchView.isIconified = false

        val editTextSearch: EditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text) as EditText
        val closeButton: ImageView = searchView.findViewById(R.id.search_close_btn) as ImageView
        val searchPlateView: View = searchView.findViewById(androidx.appcompat.R.id.search_plate)

        editTextSearch.hint = resources.getString(R.string.hintSearch)

        closeButton.setOnClickListener {
            editTextSearch.setText("")
            searchView.isIconified = false
            editTextSearch.hint = resources.getString(R.string.hintSearch)
        }

        searchPlateView.setBackgroundColor(
            ContextCompat.getColor(
                this,
                android.R.color.transparent
            )
        )

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                this@SearchActivity.queryString = query ?: ""
                doSearch()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                this@SearchActivity.queryString = newText ?: ""
                if (this@SearchActivity.queryString.isEmpty()) {
                    searchProductsObservers.forEach {
                        it.eventShowFiltersAndHistory()
                    }
                }
                return false
            }
        })
    }

    private fun doSearch() {
        queryString?.let { queryString ->
            viewModel.liveDataFilteredProducts.observe(this, Observer { dataWrapper ->
               notifyObservers(dataWrapper)
            })
            viewModel.searchProducts(queryString)
        }
    }

    /**
     * [ObservableSearch] implementation.
     */
    override fun registerObserver(eventSearchProductObserver: EventSearchProductObserver) {
        if(!searchProductsObservers.contains(eventSearchProductObserver)) {
            searchProductsObservers.add(eventSearchProductObserver);
        }
    }

    override fun removeObserver(eventSearchProductObserver: EventSearchProductObserver) {
        if(searchProductsObservers.contains(eventSearchProductObserver)) {
            searchProductsObservers.remove(eventSearchProductObserver);
        }
    }

    override fun notifyObservers(dataStatusWrapper: LiveDataStatusWrapper<List<ProductModel>>?) {
        searchProductsObservers.forEach { observer ->
            dataStatusWrapper?.let {
                when (dataStatusWrapper.status) {
                    LiveDataStatusWrapper.Status.LOADING -> observer.eventOnSearchProductsLoading()
                    LiveDataStatusWrapper.Status.SUCCESS -> observer.eventOnSearchProductsSuccess(dataStatusWrapper.data)
                    LiveDataStatusWrapper.Status.ERROR -> observer.eventOnSearchProductsError()
                }
            }
        }
    }

}