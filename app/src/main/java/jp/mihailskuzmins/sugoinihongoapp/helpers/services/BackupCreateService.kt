package jp.mihailskuzmins.sugoinihongoapp.helpers.services

import android.content.Context
import com.google.firebase.firestore.DocumentSnapshot
import com.google.gson.GsonBuilder
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.backupDirectory
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.backupSyncDirectory
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.createAppInjector
import jp.mihailskuzmins.sugoinihongoapp.extensions.format
import jp.mihailskuzmins.sugoinihongoapp.extensions.invoke
import jp.mihailskuzmins.sugoinihongoapp.extensions.toDate
import jp.mihailskuzmins.sugoinihongoapp.extensions.writeToZip
import jp.mihailskuzmins.sugoinihongoapp.helpers.delegatedprops.ReactiveProp
import jp.mihailskuzmins.sugoinihongoapp.helpers.utils.SharedPreferencesUtil
import jp.mihailskuzmins.sugoinihongoapp.persistence.repos.firestore.GrammarRuleRepo
import jp.mihailskuzmins.sugoinihongoapp.persistence.repos.firestore.SentenceRepo
import jp.mihailskuzmins.sugoinihongoapp.persistence.repos.firestore.WordRepo
import jp.mihailskuzmins.sugoinihongoapp.resources.AppConstants
import kotlinx.coroutines.Dispatchers
import java.io.File
import java.io.FileOutputStream
import java.text.DateFormat
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import javax.inject.Inject
import jp.mihailskuzmins.sugoinihongoapp.resources.FirestoreConstants.Keys as FirestoreKeys
import jp.mihailskuzmins.sugoinihongoapp.resources.PreferencesConstants.Keys as PreferencesKeys

private const val mGrammarRulesFile = "grammarRules.json"
private const val mGrammarRuleExercisesFile = "grammarRuleExercises.json"
private const val mGrammarRuleExerciseTasksFile = "grammarRuleExerciseTasks.json"
private const val mSentencesFile = "sentences.json"
private const val mWordsFile = "words.json"
private const val mWordPropsFile = "wordProps.json"

private typealias DocumentsAction = (List<DocumentSnapshot>) -> Unit

class BackupCreateService(context: Context) : AppService(context, Dispatchers.IO) {

	private val mGson = GsonBuilder()
		.setDateFormat(DateFormat.LONG)
		.create()

	private val mBackupDirectory: File
	private val mBackupTemporaryDirectory: File
	private lateinit var mZipDisposable: Disposable


	@Inject lateinit var sharedPreferencesUtil: SharedPreferencesUtil
	@Inject lateinit var grammarRuleRepo: GrammarRuleRepo
	@Inject lateinit var sentenceRepo: SentenceRepo
	@Inject lateinit var wordRepo: WordRepo

	init {
		context
			.createAppInjector()
			.inject(this)

		mBackupDirectory = context.backupDirectory
		mBackupTemporaryDirectory = Date()
			.format(AppConstants.DateFormats.dateTimeFormat)
			.run { File(mBackupDirectory, "tmp_$this") }
	}

	override suspend fun run() {
		if (!mBackupTemporaryDirectory.mkdir())
			return

		val fileCreators = listOf(
			FileCreator(grammarRuleRepo::getCollection, mGrammarRulesFile),
			FileCreator(sentenceRepo::getCollection, mSentencesFile),
			FileCreator(wordRepo::getCollection, mWordsFile)
		)

		mZipDisposable = Observable.merge(fileCreators.map(FileCreator::isCreatedObservable))
			.map { fileCreators.all(FileCreator::isCreated) }
			.distinctUntilChanged()
			.filter { it }
			.take(1)
			.subscribe {
				zipBackups()
					.invoke(::copyToBackupDirectory)
					.invoke { onFinalise() }
			}

		fileCreators
			.forEach(FileCreator::invoke)
	}

	private fun zipBackups(): File? {
		mZipDisposable.dispose()

		val backupFileFilter = listOf(
			mGrammarRulesFile,
			mGrammarRuleExercisesFile,
			mGrammarRuleExerciseTasksFile,
			mSentencesFile,
			mWordsFile,
			mWordPropsFile)

		val files = mBackupTemporaryDirectory
			.listFiles()
			.filter { x -> backupFileFilter.contains(x.name) }

		if (files.isEmpty())
			return null

		val backupFile = Date()
			.format(AppConstants.DateFormats.dateTimeFormat)
			.run { File(mBackupTemporaryDirectory, this + AppConstants.backupExtension) }

		FileOutputStream(backupFile).use {
			ZipOutputStream(it).use { zipStream ->
				files.asSequence()
					.onEach { x -> zipStream.putNextEntry(ZipEntry(x.name)) }
					.forEach { x -> x.writeToZip(zipStream) }
			}
		}

		return backupFile
	}

	private fun copyToBackupDirectory(backupFile: File?) {
		if (backupFile == null)
			return

		val finalBackupFile = File(mBackupDirectory, backupFile.name)
		val syncFile = File(context.backupSyncDirectory, backupFile.name)

		with(backupFile) {
			copyTo(finalBackupFile, true)
			copyTo(syncFile, true)
		}
	}

	private fun onFinalise() {
		mBackupTemporaryDirectory.deleteRecursively()

		Date().toDate().time
			.invoke { sharedPreferencesUtil.setLong(PreferencesKeys.latestBackupCreationEpoch, it) }
	}

	private inner class FileCreator(
		private val mFunc: (DocumentsAction) -> Unit,
		private val mFileName: String
	) {

		private val mIsCreatedSubject: Subject<Boolean> = BehaviorSubject.createDefault(false)

		val isCreatedObservable: Observable<Boolean>
			get() = mIsCreatedSubject.distinctUntilChanged()

		var isCreated by ReactiveProp(false, mIsCreatedSubject::onNext)
			private set

		fun invoke() {
			mFunc.invoke {
				createFile(it, mFileName)
				isCreated = true
			}
		}

		private fun createFile(data: List<DocumentSnapshot>, fileName: String) {
			if (data.isEmpty())
				return

			val listData = data
				.map {
					mutableMapOf<String, Any>("id" to it.id)
						.also { x -> x.putAll(it.data!!) }
						.also { x -> x.remove(FirestoreKeys.userId) }
				}

			File(mBackupTemporaryDirectory, fileName)
				.writeText(mGson.toJson(listData))
		}
	}
}