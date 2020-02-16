package jp.mihailskuzmins.sugoinihongoapp.features.base.detail

import android.app.Application
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import jp.mihailskuzmins.sugoinihongoapp.extensions.whenActivated
import jp.mihailskuzmins.sugoinihongoapp.features.base.ViewModelBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.detailitems.DetailItem
import jp.mihailskuzmins.sugoinihongoapp.helpers.delegatedprops.BindableProp

typealias initAction = () -> Unit

private const val mDefaultIsValid = false

abstract class DetailViewModelBase(app: Application): ViewModelBase(app) {

	private val mIsValidSubject: Subject<Boolean> = PublishSubject.create()

	lateinit var detailItems: Array<DetailItem>
		private set

	init {
		whenActivated {d ->
			detailItems = initDetailItems()
				.also { x -> initSubscriptions(x, d) }

			initDetailItemValues { isInitialisedSubject.onNext(true) }
		}
	}

	var isValid by BindableProp(BR.valid, false, mIsValidSubject::onNext)
		@Bindable get
		private set

	val isValidObservable: Observable<Boolean>
		get() = mIsValidSubject.startWith(mDefaultIsValid).distinctUntilChanged()

	protected abstract fun initDetailItems(): Array<DetailItem>

	protected abstract fun initDetailItemValues(onInit: initAction)

	private fun initSubscriptions(items: Array<DetailItem>, d: CompositeDisposable) {
		items.forEach { it.addTo(d) }

		Observable.merge(items.map(DetailItem::isValidObservable))
			.map { items.all(DetailItem::isValid) }
			.distinctUntilChanged()
			.subscribe { isValid = it }
			.addTo(d)
	}
}