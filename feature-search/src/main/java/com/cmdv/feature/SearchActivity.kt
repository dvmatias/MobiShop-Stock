package com.cmdv.feature

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.cmdv.feature.adapters.SectionsPagerAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

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

        editTextSearch.hint = "Search"

        closeButton.setOnClickListener {
            editTextSearch.setText("")
            searchView.isIconified = false
            editTextSearch.hint = "Search"
        }

        searchPlateView.setBackgroundColor(
            ContextCompat.getColor(
                this,
                android.R.color.transparent
            )
        )
    }

    private fun showLoadingScreen() {

    }

    private fun showNotFoundScreen() {

    }

    private fun showResultsScreen() {

    }

}