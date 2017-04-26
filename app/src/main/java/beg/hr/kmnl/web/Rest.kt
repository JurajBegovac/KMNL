package beg.hr.kmnl.web

import beg.hr.kmnl.MyApplication
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by juraj on 25/04/2017.
 */
interface KmnlAPI {
  @GET("/league.php?l=1")
  fun league1(): Call<ResponseBody>
}

fun getLeague1Html(): Observable<String> = Observable.create {
  try {
    val response = MyApplication.component.restApi().league1().execute()
    if (response.isSuccessful) {
      it.onNext(response.body().string())
      it.onComplete()
    } else {
      it.onError(Throwable(response.message()))
    }
  } catch (error: Exception) {
    it.onError(Throwable(error.message))
  }
}
