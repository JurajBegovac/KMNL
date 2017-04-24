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
  fun testParseTeams_fromFullPage() {
    val fileName: String = "Page.html"
    val fullPath: String = javaClass.classLoader.getResource(fileName).file
    val input: File = File(fullPath)
    val parseTeams = parseTeams(input)
    parseTeams.size shouldEqual 13
  }
}
