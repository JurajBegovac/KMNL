package beg.hr.kmnl

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.io.File

/**
 * Created by juraj on 24/04/2017.
 */

data class Team(val name: String, val games: Int, val wins: Int,
                val draws: Int, val loses: Int, val goalsScored: Int,
                val goalsAgainst: Int, val goalDiff: Int, val bonusPoints: Int,
                val penaltyPoints: Int, val points: Int)

fun parseTeams(input: File): List<Team> =
    Jsoup.parse(input, "UTF-8")
        .getElementById("table")
        .getElementsByClass("team")
        .map(::parseTeam)

private fun parseTeam(team: Element): Team {
  val name = team.getElementsByClass("table_stat team_name").text()
  val games = team.getElementsByClass("table_stat team_games").text().toInt()
  val wins = team.getElementsByClass("table_stat team_wins").text().toInt()
  val draws = team.getElementsByClass("table_stat team_draws").text().toInt()
  val loses = team.getElementsByClass("table_stat team_loses").text().toInt()
  val scored = team.getElementsByClass("table_stat team_scored").text().toInt()
  val against = team.getElementsByClass("table_stat team_against").text().toInt()
  val difference = team.getElementsByClass("table_stat team_difference").text().toInt()
  val bonus = team.getElementsByClass("table_stat team_bonus").text().toInt()
  val penalty = team.getElementsByClass("table_stat team_penalty").text().toInt()
  val points = team.getElementsByClass("table_stat team_points").text().toInt()
  return Team(name, games, wins, draws, loses, scored, against, difference, bonus, penalty, points)
}

data class Player(val teamName: String,
                  val name: String,
                  val goals: Int,
                  val suspensions: Int,
                  val swear: Int,
                  val disqualified: Int)

fun parsePlayers(input: File): List<Player> =
    Jsoup.parse(input, "UTF-8")
        .getElementById("table")
        .getElementsByClass("team_details")
        .map {
          val teamName = it.previousElementSibling().getElementsByClass("table_stat team_name").text()
          it.getElementsByClass("team_details_player")
              .map { parsePlayer(it, teamName) }
        }
        .flatten()

private fun parsePlayer(playerElement: Element, teamName: String): Player {
  val name = playerElement.getElementsByClass("team_details_stat team_details_stat_name").text()
  val goals = playerElement.getElementsByClass("team_details_stat team_details_stat_goals").text().toInt()
  val suspensions = playerElement.getElementsByClass("team_details_stat team_details_stat_suspensions").text().toInt()
  val swear = playerElement.getElementsByClass("team_details_stat team_details_stat_swearing").text().toInt()
  val disqualified = playerElement.getElementsByClass("team_details_stat team_details_stat_disqualified").text().toInt()
  return Player(teamName, name, goals, suspensions, swear, disqualified)
}

data class Game(val id: Int,
                val team1: String,
                val team2: String,
                val team1Score: Int,
                val team2Score: Int)

fun parseGames(input: File): List<Game> =
    Jsoup.parse(input, "UTF-8")
        .getElementById("rounds")
        .getElementsByClass("round")
        .map {
          it.getElementsByClass("round_games")
              .first()
              .getElementsByClass("team_details_game_a")
              .map {
                val gameId = it.attr("game").toInt()
                val roundGame = it.getElementsByClass("round_game")
                    .first()
                parseGame(gameId, roundGame)
              }
        }
        .flatten()

private fun parseGame(gameId: Int, roundGame: Element): Game {
  val team1 = roundGame.getElementsByClass("team_details_game_stat team_details_game_stat_1")
      .first().text()
  val team1Score = roundGame.getElementsByClass("team_details_game_stat team_details_game_stat_s1")
      .first().text().toInt()
  val team2 = roundGame.getElementsByClass("team_details_game_stat team_details_game_stat_2")
      .first().text()
  val team2Score = roundGame.getElementsByClass("team_details_game_stat team_details_game_stat_s2")
      .first().text().toInt()
  return Game(gameId, team1, team2, team1Score, team2Score)
}
