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
import jp.mihailskuzmins.sugoinihongoapp.resources.Action

open class DetailItemTextView(
		context: Context,
		@StringRes label: Int,
		hideIfEmpty: Boolean = true) :
	DetailItemBase<String>(context, label, "") {

	private val mDefaultLambda = {}
	private val mOnLongClickActionSubject: Subject<Action> = BehaviorSubject.createDefault(mDefaultLambda)

	var isClickable by BindableProp(BR.clickable, false)
		@Bindable get
		private set

	init {
		if (hideIfEmpty) {
			valueChanged
				.map { it.isNotBlank() }
				.subscribe { isVisible = it }
				.addTo(cleanUp)
		}

		Observables
			.combineLatest(
				isEnabledObservable,
				mOnLongClickActionSubject.map { it !== mDefaultLambda }.distinctUntilChanged())
			{ x, y -> x && y }
			.subscribe { isClickable = it }
			.addTo(cleanUp)
	}

	var onLongClickAction: Action? = null
		set(value) {
			if (value == null)
				return

			field = value
			mOnLongClickActionSubject.onNext(value)
		}
}