package jp.mihailskuzmins.sugoinihongoapp.persistence.kanaRepoTests

import android.content.Context
import io.mockk.mockk
import jp.mihailskuzmins.sugoinihongoapp.persistence.repos.asset.KanaRepo

abstract class KanaRepoTestsBase {

	private val mockContext = mockk<Context>()

	protected fun createClass() =
		KanaRepo(mockContext)

	protected val katakanaMap = mapOf(
		"annaisuru" to "アンナイスル",
		"baa" to "バー",
		"basu" to "バス",
		"kamera" to "カメラ",
		"sentaa" to "センター",
		"kuriiningu" to "クリーニング",
		"kurabu" to "クラブ",
		"koinrokkaa" to "コインロッカー",
		"koraboreeshon" to "コラボレーション",
		"korekushon" to "コレクション",
		"kontorasuto" to "コントラスト",
		"koonaa" to "コーナー",
		"kauntaa" to "カウンター",
		"desutineeshon" to "デスティネーション",
		"dainingu" to "ダイニング",
		"dinaabuffe" to "ディナーブッフェ",
		"erebeetaa" to "エレベーター",
		"esukareetaa" to "エスカレーター",
		"ibento" to "イベント",
		"ekusupuresu" to "エクスプレス",
		"fan" to "ファン",
		"furenchi" to "フレンチ",
		"gyararii" to "ギャラリー",
		"gaaden" to "ガーデン",
		"gifutoserekushon" to "ギフトセレクション",
		"guruppo" to "グルッポ",
		"gaido" to "ガイド",
		"hoteru" to "ホテル",
		"imeeji" to "イメージ",
		"itarian" to "イタリアン",
		"kiiboodo" to "キーボード",
		"remon" to "レモン",
		"raunji" to "ラウンジ",
		"mashiin" to "マシーン",
		"menyuu" to "メニュー",
		"paaku" to "パーク",
		"paakingu" to "パーキング",
		"pointo" to "ポイント",
		"puraibeeto" to "プライベート",
		"risaikuringu" to "リサイクリング",
		"resutoran" to "レストラン",
		"saamon" to "サーモン",
		"sandowitchi" to "サンドウィッチ",
		"shoppu" to "ショップ",
		"supotto" to "スポット",
		"sutoretchaa" to "ストレッチャー",
		"suupaamaaketto" to "スーパーマーケット",
		"taimingu" to "タイミング",
		"tobaku" to "トバク",
		"toire" to "トイレ",
		"churippu" to "チュリップ",
		"uisukii" to "ウイスキー"
	)

	protected val hiraganaMap = mapOf(
		"annaisuru" to "あんないする",
		"houtteoku" to "ほうっておく",
		"hyoushinuke" to "ひょうしぬけ",
		"kansha" to "かんしゃ",
		"kaoiru" to "かおいる",
		"ketteisuru" to "けっていする",
		"konnichiha" to "こんにちは",
		"kyoukai" to "きょうかい",
		"kyuuryou" to "きゅうりょう",
		"kyuukou" to "きゅうこう",
		"massuguiku" to "まっすぐいく",
		"seijinsuru" to "せいじんする",
		"sekkyousuru" to "せっきょうする",
		"shiryoku" to "しりょく",
		"shippaisuru" to "しっぱいする",
		"tokkyuu" to "とっきゅう",
		"yuugana" to "ゆうがな"
	)

	protected val combinedMap = mapOf(
		"moteru" to "モテる",
		"beraberana" to "べらべらな"
	)
}