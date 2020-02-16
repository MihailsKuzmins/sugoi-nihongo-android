package jp.mihailskuzmins.sugoinihongoapp.helpers.rx

import android.os.FileObserver
import io.reactivex.observables.ConnectableObservable
import io.reactivex.subjects.BehaviorSubject
import jp.mihailskuzmins.sugoinihongoapp.extensions.invoke
import jp.mihailskuzmins.sugoinihongoapp.resources.FileFilter
import java.io.File

class RxFileObserver(
	file: File,
	private val mFileFilter: FileFilter? = null
) : FileObserver(file.absolutePath, MODIFY or MOVED_FROM or MOVED_TO or CREATE or DELETE or DELETE_SELF or MOVE_SELF) {

	private val mPath = file.absolutePath

	private val mFilesSubject = BehaviorSubject
		.createDefault<List<File>>(getFiles())
		.toSerialized()

	val filesObservable: ConnectableObservable<List<File>> = mFilesSubject
		.doOnSubscribe { startWatching() }
		.doOnDispose { stopWatching() }
		.publish()

	override fun onEvent(event: Int, path: String?) {
		path?.let {
			getFiles()
				.invoke(mFilesSubject::onNext)
		}
	}

	private fun getFiles() =
		File(mPath)
			.listFiles { dir, name -> mFileFilter?.invoke(dir, name) ?: true }
			.run { toList() }
}