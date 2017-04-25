package beg.hr.kmnl

import android.app.Application
import android.content.Context
import beg.hr.kmnl.web.KmnlAPI
import beg.hr.kmnl.web.NetworkModule
import beg.hr.kotlindesarrolladorandroid.common.dagger2.ApplicationContext
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by juraj on 25/04/2017.
 */

@Singleton
@Component(modules = arrayOf(AppModule::class, NetworkModule::class))
interface AppComponent {
  fun restApi(): KmnlAPI
}

@Module
class AppModule(val application: Application) {
  
  @Provides
  @Singleton
  fun application() = application
  
  @Provides
  @Singleton
  @ApplicationContext
  fun context(): Context = application.applicationContext
}
