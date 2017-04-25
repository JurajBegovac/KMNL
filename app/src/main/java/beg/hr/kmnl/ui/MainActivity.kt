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
import android.widget.TextView
import beg.hr.kmnl.Command
import beg.hr.kmnl.MyApplication
import beg.hr.kmnl.R
import beg.hr.kmnl.State
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
  
  private lateinit var text: TextView
  private lateinit var disposable: Disposable
  
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
    drawer.setDrawerListener(toggle)
    toggle.syncState()
    
    val navigationView = findViewById(R.id.nav_view) as NavigationView
    navigationView.setNavigationItemSelectedListener(this)
    
    findViewById(R.id.button).setOnClickListener {
      (application as MyApplication).dispatch(Command.Fetch())
    }
    text = findViewById(R.id.text) as TextView
  }
  
  override fun onStart() {
    super.onStart()
    disposable = (application as MyApplication).observeState()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { render(it) }
  }
  
  private fun render(state: State) {
    when (state) {
      is State.Data -> text.text = state.toString()
      is State.Fetching -> text.text = "Fetching Data"
      is State.Error -> text.text = state.msg
      is State.Parsing -> text.text = "Parsing Data"
      is State.Start -> text.text = "Start"
      else -> text.text = "Unknown"
    }
  }
  
  override fun onStop() {
    super.onStop()
    disposable.dispose()
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
    val id = item.itemId
    
    if (id == R.id.nav_camera) {
      // Handle the camera action
    } else if (id == R.id.nav_gallery) {
      
    } else if (id == R.id.nav_slideshow) {
      
    } else if (id == R.id.nav_manage) {
      
    } else if (id == R.id.nav_share) {
      
    } else if (id == R.id.nav_send) {
      
    }
    
    val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
    drawer.closeDrawer(GravityCompat.START)
    return true
  }
}
