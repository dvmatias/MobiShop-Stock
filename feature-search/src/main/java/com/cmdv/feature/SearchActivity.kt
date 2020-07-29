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
import com.cmdv.core.utils.logErrorMessage
import com.cmdv.domain.models.LiveDataStatusWrapper
import com.cmdv.feature.adapters.SectionsPagerAdapter
import kotlinx.android.synthetic.main.activity_search.*
import org.koin.android.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {

    private val viewModel: SearchActivityViewModel by viewModel()

    private var query: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        setupToolbar()
        setupSearchView()

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
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
                this@SearchActivity.query = query
                doSearch()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                this@SearchActivity.query = newText
                query = newText ?: ""
                return false
            }
        })
    }

    private fun doSearch() {
        query?.let { query ->
            viewModel.liveDataFilteredProducts.observe(this, Observer { response ->
                response?.let {dataWrapper ->
                    when (dataWrapper.status) {
                        LiveDataStatusWrapper.Status.LOADING -> logErrorMessage("doSearch() LOADING")
                        LiveDataStatusWrapper.Status.SUCCESS -> logErrorMessage("doSearch() SUCCESS")
                        LiveDataStatusWrapper.Status.ERROR -> logErrorMessage("doSearch() ERROR")
                    }
                }
            })
            viewModel.searchProducts(query)
        }
    }

    private fun showFiltersAndHistoryScreen() {
        // TODO
    }

    private fun showLoadingScreen() {

    }

    private fun showNotFoundScreen() {

    }

    private fun showResultsScreen() {

    }

}