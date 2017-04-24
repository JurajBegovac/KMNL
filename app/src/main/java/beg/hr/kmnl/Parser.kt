package beg.hr.kmnl

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.File

/**
 * Created by juraj on 24/04/2017.
 */

fun parseTeams(input: File): List<Team> {
  val doc: Document = Jsoup.parse(input, "UTF-8")
  val table = doc.getElementById("table")
  val teams = table.getElementsByClass("team")
  return List(teams.size, parseTeam(teams))
}

private fun parseTeam(teams: Elements): (index: Int) -> Team = {
  val team = teams[it]
  val name = team.getElementsByClass("table_stat team_name").text()
  val games = team.getElementsByClass("table_stat team_games").text()
  val wins = team.getElementsByClass("table_stat team_wins").text()
  val draws = team.getElementsByClass("table_stat team_draws").text()
  val loses = team.getElementsByClass("table_stat team_loses").text()
  val scored = team.getElementsByClass("table_stat team_scored").text()
  val against = team.getElementsByClass("table_stat team_against").text()
  val difference = team.getElementsByClass("table_stat team_difference").text()
  val bonus = team.getElementsByClass("table_stat team_bonus").text()
  val penalty = team.getElementsByClass("table_stat team_penalty").text()
  val points = team.getElementsByClass("table_stat team_points").text()
  Team(name,
       games.toInt(),
       wins.toInt(),
       draws.toInt(),
       loses.toInt(),
       scored.toInt(),
       against.toInt(),
       difference.toInt(),
       bonus.toInt(),
       penalty.toInt(),
       points.toInt())
}
