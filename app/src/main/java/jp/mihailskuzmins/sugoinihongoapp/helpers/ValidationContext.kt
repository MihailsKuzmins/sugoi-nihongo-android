package jp.mihailskuzmins.sugoinihongoapp.helpers

import android.content.Context
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.ReplaySubject
import io.reactivex.subjects.Subject
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.rules.RuleBase
import jp.mihailskuzmins.sugoinihongoapp.helpers.rx.SourceList

class ValidationContext<T: Any>(
	context: Context,
	valueObservable: Observable<T>
): Disposable {

	private val mRuleSource = SourceList<RuleBase<T>>()
	private val mCleanUp = CompositeDisposable()

	private val mIsValidSubject: Subject<Boolean> = ReplaySubject.createWithSize(1)
	private val mValidationMessageSubject: Subject<String> = ReplaySubject.createWithSize(1)
    private val mTriggerSubject: Subject<Unit> = BehaviorSubject.createDefault(Unit)

	val isValidObservable: Observable<Boolean>
		get() = mIsValidSubject

	val validationMessageObservable: Observable<String>
		get() = mValidationMessageSubject

	init {
		val ruleObservable = Observables
			.combineLatest(
				valueObservable,
				mRuleSource.collectionChanged.startWith(listOf<RuleBase<T>>()),
                mTriggerSubject)
				{ x: T, y: List<RuleBase<T>>, _: Unit -> Pair(x, y) }
			.map { getValidationResult(context, it.first, it.second) }
			.publish()

		ruleObservable
			.map { it.first }
			.distinctUntilChanged()
			.subscribe { mIsValidSubject.onNext(it) }
			.addTo(mCleanUp)

		ruleObservable
			.map { it.second }
			.distinctUntilChanged()
			.subscribe { mValidationMessageSubject.onNext(it) }
			.addTo(mCleanUp)

		ruleObservable
			.connect()
			.addTo(mCleanUp)
	}

	fun addRule(rule: RuleBase<T>) =
		mRuleSource.add(rule)

	fun removeRules(clazz: Class<*>) =
		mRuleSource.remove(clazz)

    fun triggerValidation(observable: Observable<*>): Disposable =
        observable
            .subscribe { mTriggerSubject.onNext(Unit) }

	override fun isDisposed() =
		mCleanUp.isDisposed

	override fun dispose() =
		mCleanUp.dispose()

	/**
	 * Returns isValid if any of the rules is not valid.
	 * Returns the first error message of a rule which is visible (it might not be the same rule which sets isValid to false)
	 */
	private fun getValidationResult(context: Context, value: T, rules: List<RuleBase<T>>): Pair<Boolean, String> {
		var isValid = true
		var errorMessage = ""

		for (rule in rules) {
			if (!rule.validate(value)) {
				isValid = false

				if (rule.isVisible) {
					errorMessage = rule.getErrorMessage(context)
					break
				}
			}
		}

		return Pair(isValid, errorMessage)
	}
}