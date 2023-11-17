package top.whiterasbk.plugins

import io.ktor.server.config.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import top.whiterasbk.data.*
import java.io.File

suspend fun <T> dbQuery(block: suspend () -> T): T =
    newSuspendedTransaction(Dispatchers.IO) { block() }

interface DAOFacade {
    suspend fun allQuestionnaires(): List<Questionnaire>
    suspend fun questionnaire(id: Int): Questionnaire?
    suspend fun addNewQuestionnaire(
        name: String,
        age: Int,
        gender: Int,
        education: Int,
        q1: Int,
        q2: Int,
        q3: List<String>,
        q4: Int,
        q5: Int,
        q6: Int,
        q7: Int,
        q8: Int,
        q9: Int,
        q10: Int
    ): Questionnaire?
    suspend fun editQuestionnaire(
        id: Int,
        name: String,
        age: Int,
        gender: Int,
        education: Int,
        q1: Int,
        q2: Int,
        q3: List<String>,
        q4: Int,
        q5: Int,
        q6: Int,
        q7: Int,
        q8: Int,
        q9: Int,
        q10: Int): Boolean
    suspend fun deleteQuestionnaire(id: Int): Boolean

    suspend fun howManyPlayingGames(): Float

    suspend fun gamingFrequency(): GamingFrequency


}

private const val DEFAULT_DRIVER_NAME = "org.h2.Driver"
private const val DEFAULT_JDBC_URL = "jdbc:h2:file:"
private const val DEFAULT_DB_FILE_PATH = "./db/db2"

object DatabaseFactory {
    fun init(config: ApplicationConfig) {

        val driverClassName = config.propertyOrNull("storage.driverClassName")?.getString() ?: DEFAULT_DRIVER_NAME
        val jdbcURL =
            (config.propertyOrNull("storage.jdbcURL")?.getString() ?: DEFAULT_JDBC_URL) +
            (config.propertyOrNull("storage.dbFilePath")?.getString()?.let { File(it).canonicalFile.absolutePath }
                ?: DEFAULT_DB_FILE_PATH.apply {
                    if (!File(this).parentFile.exists()) File(this).parentFile.mkdir()
                })

        val database = Database.connect(jdbcURL, driverClassName)

        transaction(database) {
            SchemaUtils.create(QuestionnaireTable, GameType, SurveyGameTypes)
            if (GameType.selectAll().count() == 0L) {
                GameType.init()
            }
        }
    }

    fun init() {
        val database = Database.connect(DEFAULT_JDBC_URL + DEFAULT_DRIVER_NAME, DEFAULT_DRIVER_NAME)

        transaction(database) {
            SchemaUtils.create(QuestionnaireTable, GameType, SurveyGameTypes)
            if (GameType.selectAll().count() == 0L) {
                GameType.init()
            }
        }
    }
}

object QuestionnaireTable : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 128)
    val age = integer("age")
    val gender = integer("gender")
    val education = integer("education")
    val q1 = integer("q1")
    val q2 = integer("q2")
    val q4 = integer("q4")
    val q5 = integer("q5")
    val q6 = integer("q6")
    val q7 = integer("q7")
    val q8 = integer("q8")
    val q9 = integer("q9")
    val q10 = integer("q10")
    override val primaryKey = PrimaryKey(id)
}

object GameType : IntIdTable() {
    val type = varchar("type", 128)

    lateinit var mobaTypeId: EntityID<Int>
    lateinit var musicGameTypeId: EntityID<Int>
    lateinit var rpgTypeId: EntityID<Int>
    lateinit var pixelGameTypeId: EntityID<Int>
    lateinit var sandboxTypeId: EntityID<Int>
    lateinit var genshinTypeId: EntityID<Int>
    lateinit var threeATypeId: EntityID<Int>
    lateinit var togetherTypeId: EntityID<Int>
    lateinit var teriTypeId: EntityID<Int>
    lateinit var actionTypeId: EntityID<Int>
    lateinit var chijiTypeId: EntityID<Int>
    lateinit var fpsTypeId: EntityID<Int>
    lateinit var ciTypeId: EntityID<Int>
    lateinit var fgoTypeId: EntityID<Int>
    lateinit var chessTypeId: EntityID<Int>
    lateinit var socialTypeId: EntityID<Int>
    lateinit var intelTypeId: EntityID<Int>
    lateinit var comTypeId: EntityID<Int>
    lateinit var runTypeId: EntityID<Int>
    lateinit var zvpTypeId: EntityID<Int>
    lateinit var storyTypeId: EntityID<Int>
    lateinit var ggTypeId: EntityID<Int>
    lateinit var otherTypeId: EntityID<Int>

    fun init() {
        mobaTypeId =      GameType.insertAndGetId { it[type] = "moba" }
        musicGameTypeId = GameType.insertAndGetId { it[type] = "mg" }
        rpgTypeId =       GameType.insertAndGetId { it[type] = "rpg" }
        pixelGameTypeId = GameType.insertAndGetId { it[type] = "pixel" }
        sandboxTypeId =   GameType.insertAndGetId { it[type] = "sandbox" }
        genshinTypeId =   GameType.insertAndGetId { it[type] = "genshin" }
        threeATypeId =    GameType.insertAndGetId { it[type] = "3a" }
        togetherTypeId =  GameType.insertAndGetId { it[type] = "together" }
        teriTypeId =      GameType.insertAndGetId { it[type] = "teri" }
        actionTypeId =    GameType.insertAndGetId { it[type] = "action" }
        chijiTypeId =     GameType.insertAndGetId { it[type] = "chiji" }
        fpsTypeId =       GameType.insertAndGetId { it[type] = "fps" }
        ciTypeId =        GameType.insertAndGetId { it[type] = "ci" }
        fgoTypeId =       GameType.insertAndGetId { it[type] = "fgo" }
        chessTypeId =     GameType.insertAndGetId { it[type] = "chess" }
        socialTypeId =    GameType.insertAndGetId { it[type] = "social" }
        intelTypeId =     GameType.insertAndGetId { it[type] = "intel" }
        comTypeId =       GameType.insertAndGetId { it[type] = "com" }
        runTypeId =       GameType.insertAndGetId { it[type] = "run" }
        zvpTypeId =       GameType.insertAndGetId { it[type] = "zvp" }
        storyTypeId =     GameType.insertAndGetId { it[type] = "story" }
        ggTypeId =        GameType.insertAndGetId { it[type] = "gg" }
        otherTypeId =     GameType.insertAndGetId { it[type] = "other" }
    }
}

// 中间表，用于建立多对多关系
object SurveyGameTypes: Table() {
    val surveyId = integer("survey_id").references(QuestionnaireTable.id)
    val gameTypeId = integer("game_type_id").references(GameType.id)

    fun rowQuerySurvey(id: Int): List<String> = buildList {
        val gameTypeIds = select { surveyId eq id}.map { it[gameTypeId] }
        gameTypeIds.forEach { typeId ->
            val typeName = GameType.select { GameType.id eq typeId }.singleOrNull()?.get(GameType.type)
            typeName?.let { add(it) } ?: error("type id for $typeId is not exist")
        }
    }

    suspend fun querySurvey(id: Int): List<String> = dbQuery { rowQuerySurvey(id) }

    suspend fun queryGameType(types: List<String>): List<Int> = dbQuery {
        val ids = GameType.select { GameType.type inList types }.map { it[GameType.id].value }
        select { gameTypeId inList ids }.map { it[surveyId] }
    }

    suspend fun deleteSurvey(id: Int) = dbQuery {
        deleteWhere { surveyId eq id }
    }

    fun rowInsert(questionnaireId: Int, gameTypes: List<String>): List<Int> = buildList {
        val gameTypeIds = GameType
            .select { GameType.type inList gameTypes }
            .map { it[GameType.id].value }

        gameTypeIds.forEach { id ->
            val stat = insert { sgtis ->
                sgtis[this.surveyId] = questionnaireId
                sgtis[this.gameTypeId] = id
            }
            stat.resultedValues?.singleOrNull()?.let { row -> add(row[gameTypeId]) } // returns inserted game type id
        }
    }

    suspend fun insert(questionnaireId: Int, gameTypes: List<String>): List<Int> = dbQuery { rowInsert(questionnaireId, gameTypes) }
}

class DAOFacadeImpl : DAOFacade {
    private fun resultRowToQuestionnaire(row: ResultRow) = Questionnaire(
        id = row[QuestionnaireTable.id],
        name = row[QuestionnaireTable.name],
        age = row[QuestionnaireTable.age],
        gender = row[QuestionnaireTable.gender],
        education = row[QuestionnaireTable.education],
        q1 = row[QuestionnaireTable.q1],
        q2 = row[QuestionnaireTable.q2],
        q3 = SurveyGameTypes.rowQuerySurvey(row[QuestionnaireTable.id]),
        q4 = row[QuestionnaireTable.q4],
        q5 = row[QuestionnaireTable.q5],
        q6 = row[QuestionnaireTable.q6],
        q7 = row[QuestionnaireTable.q7],
        q8 = row[QuestionnaireTable.q8],
        q9 = row[QuestionnaireTable.q9],
        q10 = row[QuestionnaireTable.q10]
    )

    override suspend fun allQuestionnaires(): List<Questionnaire> = dbQuery {
        QuestionnaireTable.selectAll().map(::resultRowToQuestionnaire)
    }

    override suspend fun questionnaire(id: Int): Questionnaire? = dbQuery {
        QuestionnaireTable
            .select { QuestionnaireTable.id eq id }
            .map(::resultRowToQuestionnaire)
            .singleOrNull()
    }

    override suspend fun addNewQuestionnaire(
        name: String,
        age: Int,
        gender: Int,
        education: Int,
        q1: Int,
        q2: Int,
        q3: List<String>,
        q4: Int,
        q5: Int,
        q6: Int,
        q7: Int,
        q8: Int,
        q9: Int,
        q10: Int
    ): Questionnaire = dbQuery {
        val survey = QuestionnaireTable.insert {
            it[this.name] = name
            it[this.age] = age
            it[this.gender] = gender
            it[this.education] = education
            it[this.q1] = q1
            it[this.q2] = q2
            it[this.q4] = q4
            it[this.q5] = q5
            it[this.q6] = q6
            it[this.q7] = q7
            it[this.q8] = q8
            it[this.q9] = q9
            it[this.q10] = q10
        }.resultedValues?.singleOrNull()?.let(::resultRowToQuestionnaire) ?: error("failed to add")

        SurveyGameTypes.rowInsert(survey.id, q3)
        survey
    }

    override suspend fun editQuestionnaire(
        id: Int,
        name: String,
        age: Int,
        gender: Int,
        education: Int,
        q1: Int,
        q2: Int,
        q3: List<String>,
        q4: Int,
        q5: Int,
        q6: Int,
        q7: Int,
        q8: Int,
        q9: Int,
        q10: Int
    ): Boolean = dbQuery {

        // 删除之前的关联关系
        SurveyGameTypes.deleteSurvey(id)
        SurveyGameTypes.insert(id, q3)

        QuestionnaireTable.update({ QuestionnaireTable.id eq id }) {
            it[QuestionnaireTable.name] = name
            it[QuestionnaireTable.age] = age
            it[QuestionnaireTable.gender] = gender
            it[QuestionnaireTable.education] = education
            it[QuestionnaireTable.q1] = q1
            it[QuestionnaireTable.q2] = q2
            it[QuestionnaireTable.q4] = q4
            it[QuestionnaireTable.q5] = q5
            it[QuestionnaireTable.q6] = q6
            it[QuestionnaireTable.q7] = q7
            it[QuestionnaireTable.q8] = q8
            it[QuestionnaireTable.q9] = q9
            it[QuestionnaireTable.q10] = q10
        } > 0
    }

    override suspend fun deleteQuestionnaire(id: Int): Boolean = dbQuery {
        SurveyGameTypes.deleteSurvey(id)
        QuestionnaireTable.deleteWhere { QuestionnaireTable.id eq id } > 0
    }

    suspend fun allCount(): Long = dbQuery {
        QuestionnaireTable.selectAll().count()
    }

    suspend fun playData(): PlayCount = dbQuery {
        PlayCount(
            played = QuestionnaireTable.select { QuestionnaireTable.q1 eq 0 }.count(),
            all = QuestionnaireTable.selectAll().count()
        )
    }

    override suspend fun howManyPlayingGames(): Float = dbQuery {
        val all = QuestionnaireTable.selectAll().count()
        val play = QuestionnaireTable.select {
            QuestionnaireTable.q1 eq 0
        }.count()

        play.toFloat() / all.toFloat()
    }

    override suspend fun gamingFrequency(): GamingFrequency = dbQuery {
        GamingFrequency(
            never =   QuestionnaireTable.select { QuestionnaireTable.q2 eq 0 }.count(),
            seldom =  QuestionnaireTable.select { QuestionnaireTable.q2 eq 1 }.count(),
            monthly = QuestionnaireTable.select { QuestionnaireTable.q2 eq 2 }.count(),
            weekly =  QuestionnaireTable.select { QuestionnaireTable.q2 eq 3 }.count(),
            daily =   QuestionnaireTable.select { QuestionnaireTable.q2 eq 4 }.count(),
            nome =    QuestionnaireTable.select { QuestionnaireTable.q2 eq 5 }.count()
        )
    }

    suspend fun gameTypeCount(): GameTypeCounting = dbQuery {
        GameTypeCounting(
            moba = SurveyGameTypes.queryGameType(listOf("moba")).count(),
            mg = SurveyGameTypes.queryGameType(listOf("mg")).count(),
            rpg = SurveyGameTypes.queryGameType(listOf("rpg")).count(),
            pixel = SurveyGameTypes.queryGameType(listOf("pixel")).count(),
            sandbox = SurveyGameTypes.queryGameType(listOf("sandbox")).count(),
            genshin = SurveyGameTypes.queryGameType(listOf("genshin")).count(),
            threeA = SurveyGameTypes.queryGameType(listOf("3a")).count(),
            together = SurveyGameTypes.queryGameType(listOf("together")).count(),
            teri = SurveyGameTypes.queryGameType(listOf("teri")).count(),
            action = SurveyGameTypes.queryGameType(listOf("action")).count(),
            chiji = SurveyGameTypes.queryGameType(listOf("chiji")).count(),
            fps = SurveyGameTypes.queryGameType(listOf("fps")).count(),
            ci = SurveyGameTypes.queryGameType(listOf("ci")).count(),
            fgo = SurveyGameTypes.queryGameType(listOf("fgo")).count(),
            chess = SurveyGameTypes.queryGameType(listOf("chess")).count(),
            social = SurveyGameTypes.queryGameType(listOf("social")).count(),
            intel = SurveyGameTypes.queryGameType(listOf("intel")).count(),
            com = SurveyGameTypes.queryGameType(listOf("com")).count(),
            run = SurveyGameTypes.queryGameType(listOf("run")).count(),
            zvp = SurveyGameTypes.queryGameType(listOf("zvp")).count(),
            story = SurveyGameTypes.queryGameType(listOf("story")).count(),
            gg = SurveyGameTypes.queryGameType(listOf("gg")).count(),
            other = SurveyGameTypes.queryGameType(listOf("other")).count()
        )
    }

    suspend fun gamingPurpose() = dbQuery {
        GamingPurpose(
            wasted =                  QuestionnaireTable.select { QuestionnaireTable.q4 eq 0 }.count(),
            makeFun =                 QuestionnaireTable.select { QuestionnaireTable.q4 eq 1 }.count(),
            vent =                    QuestionnaireTable.select { QuestionnaireTable.q4 eq 2 }.count(),
            proveSelfSix =            QuestionnaireTable.select { QuestionnaireTable.q4 eq 3 }.count(),
            proveSelfSixToTripleSix = QuestionnaireTable.select { QuestionnaireTable.q4 eq 4 }.count(),
            getRidOfDog =             QuestionnaireTable.select { QuestionnaireTable.q4 eq 5 }.count(),
        )
    }

    suspend fun opinionOnGameCharging() = dbQuery {
        GameCharging(
            bad =        QuestionnaireTable.select { QuestionnaireTable.q5 eq 0 }.count(),
            understand = QuestionnaireTable.select { QuestionnaireTable.q5 eq 1 }.count(),
            support =    QuestionnaireTable.select { QuestionnaireTable.q5 eq 2 }.count(),
            piracy =     QuestionnaireTable.select { QuestionnaireTable.q5 eq 3 }.count(),
        )
    }

    suspend fun opinionOnGamePiracy() = dbQuery {
        GamePiracy(
            weakMind =             QuestionnaireTable.select { QuestionnaireTable.q6 eq 0 }.count(),
            nothing =              QuestionnaireTable.select { QuestionnaireTable.q6 eq 1 }.count(),
            supportButUnderstand = QuestionnaireTable.select { QuestionnaireTable.q6 eq 2 }.count(),
            supportAndResist =     QuestionnaireTable.select { QuestionnaireTable.q6 eq 3 }.count(),
            nextTime =             QuestionnaireTable.select { QuestionnaireTable.q6 eq 4 }.count(),
        )
    }

    suspend fun opinionOnGameLimit() = dbQuery {
        GameLimit(
            gameHasNothingGood =  QuestionnaireTable.select { QuestionnaireTable.q7 eq 0 }.count(),
            dialectical =         QuestionnaireTable.select { QuestionnaireTable.q7 eq 1 }.count(),
            freedom =             QuestionnaireTable.select { QuestionnaireTable.q7 eq 2 }.count(),
            totalShit =           QuestionnaireTable.select { QuestionnaireTable.q7 eq 3 }.count(),
        )
    }

    suspend fun opinionOnGamingEffectLife() = dbQuery {
        EffectLife(
            totallyNot =     QuestionnaireTable.select { QuestionnaireTable.q8 eq 0 }.count(),
            goodInfluence =  QuestionnaireTable.select { QuestionnaireTable.q8 eq 1 }.count(),
            badInfluence =   QuestionnaireTable.select { QuestionnaireTable.q8 eq 2 }.count(),
            underControl =   QuestionnaireTable.select { QuestionnaireTable.q8 eq 3 }.count(),
            insane =         QuestionnaireTable.select { QuestionnaireTable.q8 eq 4 }.count(),
        )
    }

    suspend fun opinionOnGradesDeclineReason() = dbQuery {
        GradesDecline(
            itsGamesFault =     QuestionnaireTable.select { QuestionnaireTable.q9 eq 0 }.count(),
            mainlySelfControl = QuestionnaireTable.select { QuestionnaireTable.q9 eq 1 }.count(),
            bothSupervision =   QuestionnaireTable.select { QuestionnaireTable.q9 eq 2 }.count(),
            noneOfBusiness =    QuestionnaireTable.select { QuestionnaireTable.q9 eq 3 }.count(),
            close =             QuestionnaireTable.select { QuestionnaireTable.q9 eq 4 }.count(),
        )
    }

    suspend fun opinionOnGamingTime() = dbQuery {
        GamingTime(
            neverPlaying =   QuestionnaireTable.select { QuestionnaireTable.q10 eq 0 }.count(),
            onlyRealex =     QuestionnaireTable.select { QuestionnaireTable.q10 eq 1 }.count(),
            sometimesOver =  QuestionnaireTable.select { QuestionnaireTable.q10 eq 2 }.count(),
            asLongAsIdle =   QuestionnaireTable.select { QuestionnaireTable.q10 eq 3 }.count(),
            liveOnIt =       QuestionnaireTable.select { QuestionnaireTable.q10 eq 4 }.count(),
            allDay =         QuestionnaireTable.select { QuestionnaireTable.q10 eq 5 }.count(),
        )
    }


}

