package jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces;

import android.content.Context;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentActivity;

import javax.annotation.Nullable;

public interface AppFragment {

    @IdRes int getNavigationId();
    @Nullable FragmentActivity getActivity();
    @Nullable Context getContext();

    boolean beforeNavigatingBack();
}
