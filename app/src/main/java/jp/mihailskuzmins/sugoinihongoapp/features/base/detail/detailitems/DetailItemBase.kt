package jp.mihailskuzmins.sugoinihongoapp.features.base.detail.detailitems

import android.content.Context
import androidx.annotation.StringRes
import androidx.databinding.Bindable
import androidx.databinding.PropertyChangeRegistry
import androidx.databinding.library.baseAdapters.BR
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import jp.mihailskuzmins.sugoinihongoapp.extensions.isEqual
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.rules.RuleBase
import jp.mihailskuzmins.sugoinihongoapp.helpers.BaseObservable
import jp.mihailskuzmins.sugoinihongoapp.helpers.ValidationContext
import jp.mihailskuzmins.sugoinihongoapp.helpers.delegatedprops.BindableProp
import jp.mihailskuzmins.sugoinihongoapp.helpers.delegatedprops.ReactiveProp
import jp.mihailskuzmins.sugoinihongoapp.resources.ActionT
import java.util.concurrent.TimeUnit

abstract class DetailItemBase<T: Any>(
		context: Context,
		override val label: String,
		value: T) :
	DetailItem, BaseObservable {

	constructor(context: Context, @StringRes label: Int, value: T) :
			this(context, context.getString(label), value)

	private val mValueChanged: Subject<T> = BehaviorSubject.createDefault(value)
	private val mInitialValueChanged: Subject<T> = BehaviorSubject.createDefault(value)
	private val mIsValidSubject: Subject<Boolean> = BehaviorSubject.createDefault(false)
	private val mIsEnabledSubject: Subject<Boolean> = BehaviorSubject.createDefault(true)
	private val mValidationContext: ValidationContext<T> = ValidationContext(context, mValueChanged)
	private var mIsValueChangedActionSet = false

	protected val cleanUp = CompositeDisposable()

	final override var tag: String? = null
	final override var callbacks: PropertyChangeRegistry? = null

	init { mValidationContext.addTo(cleanUp) }

	var initialValue by ReactiveProp(value) {
		mInitialValueChanged.onNext(it)
		this.value = it
	}

	var value by BindableProp(BR.value, value, mValueChanged::onNext)
		@Bindable get

	final override var valueAny: Any = value
		private set

	final override var isVisible by BindableProp(BR.visible, true)
		@Bindable get

	final override var isEnabled by BindableProp(BR.enabled, true, mIsEnabledSubject::onNext)
		@Bindable get

	var validationMessage by BindableProp(BR.validationMessage, "")
		@Bindable get

	final override var isValid by ReactiveProp(false, mIsValidSubject::onNext)
		private set

	init {
		Observables
			.combineLatest(
				mValidationContext.isValidObservable,
				mIsEnabledSubject.distinctUntilChanged())
				{ isValid: Boolean, isEnabled: Boolean -> isValid || !isEnabled }
			.distinctUntilChanged()
			.subscribe { isValid = it }
			.addTo(cleanUp)

		Observables
			.combineLatest(
				mValidationContext.validationMessageObservable,
				mIsEnabledSubject.distinctUntilChanged())
				{ x: String, y: Boolean -> if (y) x else "" }
			.subscribe { validationMessage = it }
			.addTo(cleanUp)

		mValueChanged
			.cast(Any::class.java)
			.subscribe { valueAny = it }
			.addTo(cleanUp)
	}

	final override val isValueChanged: Boolean
		get() = !value.isEqual(initialValue)

	val valueChanged: Observable<T>
		get() = mValueChanged.distinctUntilChanged()

	override val isValidObservable: Observable<Boolean>
		get() = mIsValidSubject.distinctUntilChanged()

	protected val initialValueChanged: Observable<T>
		get() = mInitialValueChanged.distinctUntilChanged()

	protected val isEnabledObservable: Observable<Boolean>
		get() = mIsEnabledSubject.distinctUntilChanged()

	fun addRule(rule: RuleBase<T>) =
		mValidationContext.addRule(rule)

	fun addRules(vararg rules: RuleBase<T>) =
		rules.forEach(mValidationContext::addRule)

	fun removeRule(clazz: Class<*>) =
		mValidationContext.removeRules(clazz)

	fun triggerValidation(observable: Observable<*>): Disposable =
		mValidationContext.triggerValidation(observable)

	fun addValueChangedAction(action: ActionT<T>) {
		check(!mIsValueChangedActionSet) { "ValueChangedAction has already been set" }

		mValueChanged
			.skip(1)
			.distinctUntilChanged()
			.debounce(250, TimeUnit.MILLISECONDS)
			.subscribe(action)
			.addTo(cleanUp)

		mIsValueChangedActionSet = true
	}

	override fun isDisposed() =
		cleanUp.isDisposed

	override fun dispose() =
		cleanUp.dispose()
}