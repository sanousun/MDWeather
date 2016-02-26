package com.sanousun.mdweather.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class ItemSwipeHelperCallBack extends ItemTouchHelper.Callback {

    private final ItemSwipeHelperAdapter mAdapter;

    public ItemSwipeHelperCallBack(ItemSwipeHelperAdapter adapter) {
        this.mAdapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = 0;
        int swipeFlags;
        if (viewHolder.getAdapterPosition() == 0) {
            swipeFlags = 0;
        } else {
            swipeFlags = ItemTouchHelper.START;
        }
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView,
                          RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }

    public interface ItemSwipeHelperAdapter {
        void onItemDismiss(int pos);
    }
}
