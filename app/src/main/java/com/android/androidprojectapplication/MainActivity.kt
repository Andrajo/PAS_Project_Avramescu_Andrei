package com.android.androidprojectapplication

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var listAdapter : ItemsAdaptor

    private lateinit var bottomNavigationView: BottomNavigationView

    private val settingsFragment = SettingsFragment()

    private val notificationFragment = NotificationFragment()

    private val listFragment = ListFragment()


    override fun onCreate(savedInstanceState: Bundle?) {

        //TODO: change the main fragment

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listAdapter = ItemsAdaptor(mutableListOf())
        bottomNavigationView = findViewById(R.id.bottom_navigation_menu)

        itemsList.adapter = listAdapter
        itemsList.layoutManager = LinearLayoutManager(this)

        addItem.setOnClickListener {
            val itemsName = newListItem.text.toString()
            if(itemsName.isNotEmpty()) {
                val item = ItemInList(itemsName, R.drawable.ic_baseline_image_24)
                listAdapter.addItemInList(item)
                newListItem.text.clear()
            }
        }

        deleteItem.setOnClickListener {
            listAdapter.deleteCheckedItems()
        }

        shareButton.setOnClickListener {
            var shareIntent = Intent().apply {
                this.action = Intent.ACTION_SEND
                this.putExtra(Intent.EXTRA_TEXT, "We are sharing data!")
                this.type = "text/plain"
            }
            startActivity(shareIntent)
        }

        replaceFragment(listFragment)

        bottom_navigation_menu.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.home -> replaceFragment(listFragment)
                R.id.settings -> replaceFragment(settingsFragment)
                R.id.notifications -> replaceFragment(notificationFragment)
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        if(fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment)
            transaction.commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val item = menu?.findItem(R.id.search_action)
        val searchView = item?.actionView as SearchView
        var searchText = String()
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (searchText.isNotEmpty()) {
                    listAdapter.filterList(searchText)
                }
                else {
                    listAdapter.recoverList()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchText = newText!!.toLowerCase(Locale.getDefault())
                return false
            }

        })

        return super.onCreateOptionsMenu(menu)
    }
}