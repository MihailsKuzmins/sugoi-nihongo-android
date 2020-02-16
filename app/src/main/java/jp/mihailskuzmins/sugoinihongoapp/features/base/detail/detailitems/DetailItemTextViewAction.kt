package jp.mihailskuzmins.sugoinihongoapp.features.base.detail.detailitems

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.helpers.delegatedprops.BindableProp
import jp.mihailskuzmins.sugoinihongoapp.resources.Action

@ColorRes
private const val mDefaultTextColor: Int = R.color.colorDetailText

open class DetailItemTextViewAction(
		context: Context,
		@StringRes label: Int) :
	DetailItemBase<String>(context, label, "") {

	private val mDefaultLambda = {}
	private val mOnClickActionSubject: Subject<Action> = BehaviorSubject.createDefault(mDefaultLambda)

	var isClickable by BindableProp(BR.clickable, false)
		@Bindable get
		private set

	var textColor: Int? by BindableProp(BR.textColor, mDefaultTextColor)
		@Bindable @ColorRes get
		private set

	init {
		val isClickableObservable = Observables
			.combineLatest(
				isEnabledObservable,
				mOnClickActionSubject.map { it !== mDefaultLambda }.distinctUntilChanged())
				{ x, y -> x && y }
			.publish()

		isClickableObservable
			.subscribe { isClickable = it }
			.addTo(cleanUp)

		isClickableObservable
			.map {
				if (it) R.color.colorAccent
				else mDefaultTextColor
			}.subscribe { textColor = it }
			.addTo(cleanUp)

		isClickableObservable
			.connect()
			.addTo(cleanUp)
	}

	var onClickAction: Action = mDefaultLambda
		set(value) {
			field = value
			mOnClickActionSubject.onNext(value)
		}
}