package top.whiterasbk.plugins

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import kotlin.test.BeforeTest
import kotlin.test.Test

class QuestionnaireTableTest {


    @BeforeTest
    fun init() {
        DatabaseFactory.init()
    }

    @Test
    fun testAdding(): Unit = runBlocking {

        val games = listOf(
            "moba", "mg", "rpg", "pixel", "sandbox", "genshin", "3a", "together", "teri", "action",
            "chiji", "fps", "ci", "fgo", "chess",  "social", "intel", "com", "run", "zvp", "story",
            "gg", "other"
        )

        for (i in 33..55) {
            val dao = DAOFacadeImpl()
             dao.addNewQuestionnaire(
                 "TESTNAME-$i", i,
                 i % 3,
                 i % 4,
                 0,
                 1,
                 buildSet {
                   repeat(4) {
                        add(games.random())
                   }
                 }.toList(),
                 1,
                 1,
                 1,
                 1,
                 1,
                 1,
                 1
             )
        }
    }

    @Test
    fun testQuerySurvey(): Unit = runBlocking {
        val dao = DAOFacadeImpl()
        println(dao.questionnaire(15))
    }

    @Test
    fun testEditSurvey(): Unit = runBlocking {
        val dao = DAOFacadeImpl()
        println(dao.questionnaire(18))
        println(dao.editQuestionnaire(18, "EDIT", 188, 2, 5, 0, 0, listOf("sandbox", "mg"),0,0,0,0,0,0,0))
    }

    @Test
    fun testQueryHowManyPlayingGames(): Unit = runBlocking {
        val dao = DAOFacadeImpl()
        println(dao.howManyPlayingGames())
    }

    @Test
    fun testQueryGamingFrequency(): Unit = runBlocking {
        val dao = DAOFacadeImpl()
        println(dao.gamingFrequency())
    }

    @Test
    fun testQueryGameTypeCount(): Unit = runBlocking {
        val dao = DAOFacadeImpl()
        println(dao.gameTypeCount())
        println(dao.gamingPurpose())
        println(dao.opinionOnGameCharging())
        println(dao.opinionOnGamePiracy())
        println(dao.opinionOnGameLimit())
        println(dao.opinionOnGamingEffectLife())
        println(dao.opinionOnGradesDeclineReason())
        println(dao.opinionOnGamingTime())
    }


}