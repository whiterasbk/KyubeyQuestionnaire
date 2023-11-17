package top.whiterasbk.data

import kotlinx.serialization.Serializable

@Serializable
data class PlayCount(
    val played: Long,
    val all: Long
)

@Serializable
data class Questionnaire(
    val id: Int,
    val name: String,
    val age: Int,
    val gender: Int,
    val education: Int,
    val q1: Int,
    val q2: Int,
    val q3: List<String>,
    val q4: Int,
    val q5: Int,
    val q6: Int,
    val q7: Int,
    val q8: Int,
    val q9: Int,
    val q10: Int
)

@Serializable
data class GamingTime (
    val neverPlaying: Long,
    val onlyRealex: Long,
    val sometimesOver: Long,
    val asLongAsIdle: Long,
    val liveOnIt: Long,
    val allDay: Long,
)

@Serializable
data class GradesDecline (
    val itsGamesFault: Long,
    val mainlySelfControl: Long,
    val bothSupervision: Long,
    val noneOfBusiness: Long,
    val close: Long,
)

@Serializable
data class EffectLife (
    val totallyNot: Long,
    val goodInfluence: Long,
    val badInfluence: Long,
    val underControl: Long,
    val insane: Long,
)

@Serializable
data class GameLimit (
    val gameHasNothingGood: Long,
    val dialectical: Long,
    val freedom: Long,
    val totalShit: Long,
)

@Serializable
data class GamePiracy (
    val weakMind: Long,
    val nothing: Long,
    val supportButUnderstand: Long,
    val supportAndResist: Long,
    val nextTime: Long
)

@Serializable
data class GameCharging (
    val bad: Long,
    val understand: Long,
    val support: Long,
    val piracy: Long
)

@Serializable
data class GamingPurpose(
    val wasted: Long,
    val makeFun: Long,
    val vent: Long,
    val proveSelfSix: Long,
    val proveSelfSixToTripleSix: Long,
    val getRidOfDog: Long
)

@Serializable
data class GameTypeCounting(
    val moba: Int, val mg: Int, val rpg: Int, val pixel: Int, val sandbox: Int, val genshin: Int, val threeA: Int, val together: Int,
    val teri: Int, val action: Int, val chiji: Int, val fps: Int, val ci: Int, val fgo: Int, val chess: Int, val social: Int,
    val intel: Int, val com: Int, val run: Int, val zvp: Int, val story: Int, val gg: Int, val other: Int
)

@Serializable
data class GamingFrequency(
    val never: Long,
    val seldom: Long,
    val monthly: Long,
    val weekly: Long,
    val daily: Long,
    val nome: Long
)