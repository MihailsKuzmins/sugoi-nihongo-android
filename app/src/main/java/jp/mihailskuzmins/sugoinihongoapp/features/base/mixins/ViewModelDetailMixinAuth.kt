package jp.mihailskuzmins.sugoinihongoapp.features.base.mixins

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.addTo
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.DetailViewModelBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.HasAuthAction
import jp.mihailskuzmins.sugoinihongoapp.helpers.rx.RxBus
import jp.mihailskuzmins.sugoinihongoapp.resources.AppConstants.RxBusContracts

fun <TViewModel> TViewModel.createIsAuthDisposable(): Disposable
    where TViewModel : DetailViewModelBase,
    TViewModel : HasAuthAction {

    val d = CompositeDisposable()

    val isAuthInProgress = RxBus.listen(Boolean::class.java, RxBusContracts.isAuthInProgress)
        .startWith(false)
        .publish()

    Observables
        .combineLatest(
            isValidObservable,
            isAuthInProgress)
            { isValid: Boolean, isAuth: Boolean -> isValid && !isAuth }
        .subscribe { authButton.isEnabled = it }
        .addTo(d)

    isAuthInProgress
        .map(Boolean::not)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { detailItems.forEach { x -> x.isEnabled = it } }
        .addTo(d)

    isAuthInProgress
        .connect()
        .addTo(d)

    return d
}