package jp.mihailskuzmins.sugoinihongoapp.features.base.detail.detailitems

import android.content.Context
import androidx.annotation.StringRes
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import jp.mihailskuzmins.sugoinihongoapp.helpers.delegatedprops.BindableProp
import java.util.concurrent.TimeUnit

class DetailItemSeekBar(
		context: Context,
		@StringRes label: Int,
		maxValue: Int,
		minValue: Int = 1) :
	DetailItemBase<Int>(context, label, minValue) {

	private val mMinValueChangedSubject: Subject<Int> = BehaviorSubject.createDefault(minValue)
	private val mMaxValueChangedSubject: Subject<Int> = BehaviorSubject.createDefault(maxValue)

	var minValue by BindableProp(BR.minValue, minValue, mMinValueChangedSubject::onNext)
		@Bindable get

	var maxValue by BindableProp(BR.maxValue, maxValue, mMaxValueChangedSubject::onNext)
		@Bindable get

	init {
		val valueObservable = valueChanged
			.debounce(15, TimeUnit.MILLISECONDS)
			.distinctUntilChanged()
			.publish()

		Observables
			.combineLatest(
				valueObservable,
				mMinValueChangedSubject.distinctUntilChanged())
				{ x: Int, y: Int -> Pair(x, y) }
			.filter { it.first < it.second }
			.subscribe { value = it.second }
			.addTo(cleanUp)

		Observables
			.combineLatest(
				valueObservable,
				mMaxValueChangedSubject.distinctUntilChanged())
				{ x: Int, y: Int -> Pair(x, y) }
			.filter { it.first > it.second }
			.subscribe { value = it.second }
			.addTo(cleanUp)

		valueObservable
			.connect()
			.addTo(cleanUp)
	}
}