package beg.hr.kmnl

import beg.hr.kmnl.web.Team
import beg.hr.kmnl.web.parseGames
import beg.hr.kmnl.web.parsePlayers
import beg.hr.kmnl.web.parseTeams
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldEqual
import org.junit.Test
import java.io.File


/**
 * Created by juraj on 24/04/2017.
 */
class ParserTest {
  
  @Test
  fun testParseTeams_oneTeam() {
    val fileName = "OneTeam.html"
    val fullPath = javaClass.classLoader.getResource(fileName).file
    val input = File(fullPath)
    val parseTeams = parseTeams(input.readText())
    parseTeams.size shouldEqual 1
    parseTeams shouldContain Team("Kraljica sv.Krunice", 10, 1, 3, 6, 13, 37, -24, 0, 0, 6,
                                  listOf(3006, 3045, 3063, 3142, 3105, 3123, 3143, 3164, 3186,
                                         3187))
    parseTeams.first().name shouldEqual "Kraljica sv.Krunice"
  }
  
  @Test
  fun testParseTeams_fullPage() {
    val fileName: String = "Page.html"
    val fullPath: String = javaClass.classLoader.getResource(fileName).file
    val input: File = File(fullPath)
    val parseTeams = parseTeams(input.readText())
    parseTeams.size shouldEqual 13
  }
  
  @Test
  fun testParsePlayers_oneTeam() {
    val fileName: String = "OneTeam.html"
    val fullPath: String = javaClass.classLoader.getResource(fileName).file
    val input: File = File(fullPath)
    val parsePlayers = parsePlayers(input.readText())
    parsePlayers.size shouldEqual 15
    for (player in parsePlayers) {
      player.teamName shouldEqual "Kraljica sv.Krunice"
    }
  }
  
  @Test
  fun testParsePlayers_bestGoalScorer_oneTeam() {
    val fileName: String = "OneTeam.html"
    val fullPath: String = javaClass.classLoader.getResource(fileName).file
    val input: File = File(fullPath)
    val goalScorers = parsePlayers(input.readText()).filter { it.goals > 0 }.sortedByDescending { it.goals }
    goalScorers.size shouldEqual 5
    goalScorers.first().name shouldEqual "Juraj Begovac"
    goalScorers.first().goals shouldEqual 6
  }
  
  @Test
  fun testParsePlayers_bestGoalScorer_fullPage() {
    val fileName: String = "Page.html"
    val fullPath: String = javaClass.classLoader.getResource(fileName).file
    val input: File = File(fullPath)
    val goalScorers = parsePlayers(input.readText()).filter { it.goals > 0 }.sortedByDescending { it.goals }
    goalScorers.first().name shouldEqual "Matija Rajh"
    goalScorers.first().goals shouldEqual 16
    goalScorers.first().teamName shouldEqual "BDM Žalosna"
  }
  
  @Test
  fun testParseGames_oneGame() {
    val fileName: String = "OneGame.html"
    val fullPath: String = javaClass.classLoader.getResource(fileName).file
    val input: File = File(fullPath)
    val games = parseGames(input.readText())
    games.size shouldEqual 1
    games.first().team1 shouldEqual "Kraljica sv.Krunice"
    games.first().team1Score shouldEqual 6
    games.first().team2 shouldEqual "Sv.Vid Brdovec"
    games.first().team2Score shouldEqual 3
  }
  
  @Test
  fun testParseGames_fullPage() {
    val fileName: String = "Page.html"
    val fullPath: String = javaClass.classLoader.getResource(fileName).file
    val input: File = File(fullPath)
    val games = parseGames(input.readText())
    games.size shouldEqual 64
  }
  
  @Test
  fun testParseJoinGamesAndTeams() {
    val fileName: String = "Page.html"
    val fullPath: String = javaClass.classLoader.getResource(fileName).file
    val input: File = File(fullPath)
    val inputHtmlString = input.readText()
    val games = parseGames(inputHtmlString)
    val teams = parseTeams(inputHtmlString)
    val team = teams.first { it.name == "Kraljica sv.Krunice" }
    
    team.name shouldEqual "Kraljica sv.Krunice"
    
    val teamGames = games.filter { team.matchIds.contains(it.id) }
    
    teamGames.size shouldEqual 10
    teamGames.filter { it.team1 == team.name || it.team2 == team.name }.size shouldEqual 10
  }
}
