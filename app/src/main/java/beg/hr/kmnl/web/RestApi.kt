package beg.hr.kmnl.web

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