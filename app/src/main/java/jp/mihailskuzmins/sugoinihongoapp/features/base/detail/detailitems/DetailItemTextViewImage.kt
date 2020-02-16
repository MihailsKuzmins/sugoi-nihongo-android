package jp.mihailskuzmins.sugoinihongoapp.features.base.detail.detailitems

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import jp.mihailskuzmins.sugoinihongoapp.helpers.delegatedprops.BindableProp
import jp.mihailskuzmins.sugoinihongoapp.resources.Action

class DetailItemTextViewImage(
		context: Context,
		@StringRes label: Int,
		@DrawableRes image: Int? = null) :
	DetailItemBase<String>(context, label, "") {

	private var mDefaultLambda = {}
	private val mOnClickActionSubject: Subject<Action> = BehaviorSubject.createDefault(mDefaultLambda)

	var image by BindableProp(BR.image, image)
		@Bindable get
		private set

	var isClickable by BindableProp(BR.clickable, false)
		@Bindable get
		private set

	init {
		mOnClickActionSubject
			.map { it !== mDefaultLambda }
			.distinctUntilChanged()
			.subscribe { isClickable = it }
			.addTo(cleanUp)
	}

	var onClickAction: Action = mDefaultLambda
		set(value) {
			field = value
			mOnClickActionSubject.onNext(value)
		}
}