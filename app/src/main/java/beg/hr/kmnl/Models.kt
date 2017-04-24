package beg.hr.kmnl

/**
 * Created by juraj on 24/04/2017.
 */

data class Team(val name: String, val games: Int, val wins: Int,
                val draws: Int, val loses: Int, val goalsScored: Int,
                val goalsAgainst: Int, val goalDiff: Int, val bonusPoints: Int,
                val penaltyPoints: Int, val points: Int)
