package jp.mihailskuzmins.sugoinihongoapp.helpers.rx

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import jp.mihailskuzmins.sugoinihongoapp.resources.FileFilter
import java.io.File
import java.util.concurrent.TimeUnit

class RxFileScanner(
	private val mFile: File,
	private val mFileFilter: FileFilter? = null
) {

	val filesObservable: Observable<List<File>> = Observable
		.interval(0L, 1L, TimeUnit.SECONDS)
		.observeOn(Schedulers.io())
		.map {
			mFile
				.listFiles { dir, name -> mFileFilter?.invoke(dir, name) ?: true }
				.run { toList() }
		}
}