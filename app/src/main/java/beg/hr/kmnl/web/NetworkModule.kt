package beg.hr.kmnl.web

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created by juraj on 09/02/2017.
 */
@Module
class NetworkModule {
  
  @Provides
  @Singleton
  fun retrofit(): Retrofit =
      Retrofit.Builder()
          .baseUrl("http://www.pastoralsportasa.hr/")
          .build()
  
  @Provides
  @Singleton
  fun restApi(retrofit: Retrofit): KmnlAPI = retrofit.create(KmnlAPI::class.java)
}
