package com.github.wrdlbrnft.sortedlistadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.Comparator;
import java.util.List;

/**
 * Created with Android Studio<br>
 * User: Xaver<br>
 * Date: 27/03/2017
 */
class ModularSortedListAdapterImpl<T extends SortedListAdapter.ViewModel> extends SortedListAdapter<T> {

    static class Module<M extends ViewModel, VH extends ViewHolder<M>> {

        private final int mViewType;
        private final Class<M> mItemClass;
        private final ViewHolderFactory<VH> mHolderFactory;

        Module(int viewType, Class<M> itemClass, ViewHolderFactory<VH> holderFactory) {
            mViewType = viewType;
            mItemClass = itemClass;
            mHolderFactory = holderFactory;
        }
    }

    private final List<Module<?, ?>> mModules;

    public ModularSortedListAdapterImpl(Context context, Class<T> itemClass, Comparator<T> comparator, List<Module<?, ?>> modules) {
        super(context, itemClass, comparator);
        mModules = modules;
    }

    @Override
    protected ViewHolder<? extends T> onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        for (Module<?, ?> module : mModules) {
            if (module.mViewType == viewType) {
                return (ViewHolder<? extends T>) module.mHolderFactory.create(inflater, parent, false);
            }
        }

        throw new IllegalStateException("No mapping for " + viewType + " exists.");
    }

    @Override
    public int getItemViewType(int position) {
        final T item = getItem(position);
        final Class<? extends ViewModel> itemClass = item.getClass();
        for (Module<?, ?> module : mModules) {
            if (module.mItemClass.isAssignableFrom(itemClass)) {
                return module.mViewType;
            }
        }

        throw new IllegalStateException("No mapping for " + itemClass + " exists.");
    }
}