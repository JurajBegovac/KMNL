package beg.hr.kmnl

import android.app.Application
import android.os.StrictMode
import com.squareup.leakcanary.LeakCanary


/**
 * Created by juraj on 24/04/2017.
 */
class MyApplication : Application() {
  
  override fun onCreate() {
    if (BuildConfig.DEBUG) {
      StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                                     .detectAll()
                                     .penaltyLog()
                                     .build())
      StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                                 .detectAll()
                                 .penaltyLog()
                                 .penaltyDeath()
                                 .build())
    }
    super.onCreate()
    
    if (BuildConfig.DEBUG) {
      if (LeakCanary.isInAnalyzerProcess(this)) {
        // This process is dedicated to LeakCanary for heap analysis.
        // You should not init your app in this process.
        return
      }
      LeakCanary.install(this)
    }
  }
}