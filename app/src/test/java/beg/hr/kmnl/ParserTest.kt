package beg.hr.kmnl

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
    val fileName: String = "OneTeam.html"
    val fullPath: String = javaClass.classLoader.getResource(fileName).file
    val input: File = File(fullPath)
    val parseTeams = parseTeams(input)
    parseTeams shouldContain Team("Kraljica sv.Krunice", 10, 1, 3, 6, 13, 37, -24, 0, 0, 6)
  }
  
  @Test
  fun testParseTeams_fullPage() {
    val fileName: String = "Page.html"
    val fullPath: String = javaClass.classLoader.getResource(fileName).file
    val input: File = File(fullPath)
    val parseTeams = parseTeams(input)
    parseTeams.size shouldEqual 13
  }
  
  @Test
  fun testParsePlayers_oneTeam() {
    val fileName: String = "OneTeam.html"
    val fullPath: String = javaClass.classLoader.getResource(fileName).file
    val input: File = File(fullPath)
    val parsePlayers = parsePlayers(input)
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
    val goalScorers = parsePlayers(input).filter { it.goals > 0 }.sortedByDescending { it.goals }
    goalScorers.size shouldEqual 5
    goalScorers.first().name shouldEqual "Juraj Begovac"
    goalScorers.first().goals shouldEqual 6
  }
  
  @Test
  fun testParsePlayers_bestGoalScorer_fullPage() {
    val fileName: String = "Page.html"
    val fullPath: String = javaClass.classLoader.getResource(fileName).file
    val input: File = File(fullPath)
    val goalScorers = parsePlayers(input).filter { it.goals > 0 }.sortedByDescending { it.goals }
    goalScorers.first().name shouldEqual "Matija Rajh"
    goalScorers.first().goals shouldEqual 16
    goalScorers.first().teamName shouldEqual "BDM Žalosna"
  }
  
  @Test
  fun testParseGames_oneGame() {
    val fileName: String = "OneGame.html"
    val fullPath: String = javaClass.classLoader.getResource(fileName).file
    val input: File = File(fullPath)
    val games = parseGames(input)
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
    val games = parseGames(input)
    games.size shouldEqual 64
  }
}
