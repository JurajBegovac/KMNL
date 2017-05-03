package beg.hr.kmnl.ui

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import beg.hr.kmnl.R
import beg.hr.kmnl.table.TableFragment

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    val toolbar = findViewById(R.id.toolbar) as Toolbar
    setSupportActionBar(toolbar)
    
    val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
    val toggle = ActionBarDrawerToggle(
        this,
        drawer,
        toolbar,
        R.string.navigation_drawer_open,
        R.string.navigation_drawer_close)
    drawer.addDrawerListener(toggle)
    toggle.syncState()
    
    val navigationView = findViewById(R.id.nav_view) as NavigationView
    navigationView.setNavigationItemSelectedListener(this)
    navigationView.setCheckedItem(R.id.nav_first_league)
    
    if (savedInstanceState == null)
      fragmentManager.beginTransaction()
          .replace(R.id.main, TableFragment.newInstance())
          .commit()
  }
  
  override fun onBackPressed() {
    val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START)
    } else {
      super.onBackPressed()
    }
  }
  
  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    // Inflate the menu; this adds items to the action bar if it is present.
    menuInflater.inflate(R.menu.main, menu)
    return true
  }
  
  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    val id = item.itemId
    
    
    if (id == R.id.action_settings) {
      return true
    }
    
    return super.onOptionsItemSelected(item)
  }
  
  override fun onNavigationItemSelected(item: MenuItem): Boolean {
    // Handle navigation view item clicks here.
    when (item.itemId) {
      R.id.nav_first_league -> {
        // currently do nothing
      }
      R.id.nav_send -> {
        Toast.makeText(this, "Clicked send", Toast.LENGTH_LONG).show()
      }
      R.id.nav_share -> {
        Toast.makeText(this, "Clicked share", Toast.LENGTH_LONG).show()
      }
    }
    val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
    drawer.closeDrawer(GravityCompat.START)
    return true
  }
}
