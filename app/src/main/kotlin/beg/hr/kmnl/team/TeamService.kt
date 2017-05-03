package beg.hr.kmnl.team

import beg.hr.kmnl.MyApplication
import beg.hr.kmnl.State
import beg.hr.kmnl.web.Team
import io.reactivex.Observable

/**
 * Created by juraj on 29/04/2017.
 */
class TeamService {
  
  fun getAll(): Observable<List<Team>> =
      (MyApplication.component.application() as MyApplication).state()
          .ofType(State.Data::class.java)
          .map { it.teams }
  
  
  fun getByLeague(league: String): Observable<List<Team>> {
    TODO()
  }
  
  fun getByName(name: String): Observable<Team> =
      (MyApplication.component.application() as MyApplication).state()
          .ofType(State.Data::class.java)
          .map { it.teams.filter { it.name == name }.first() }
}
