package br.elois.maktaba.recyclerviewpager;
import android.support.v7.widget.RecyclerView;

import java.lang.reflect.Field;


public class LoopRecyclerViewPagerAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerViewPagerAdapter<VH> {

    public LoopRecyclerViewPagerAdapter(RecyclerViewPager viewPager, RecyclerView.Adapter<VH> adapter) {
        super(viewPager, adapter);
    }

    public int getActualItemCount() {
        return super.getItemCount();
    }

    public int getActualItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public long getActualItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        if (getActualItemCount() > 0) {
            return Integer.MAX_VALUE;
        } else {
            return super.getItemCount();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (getActualItemCount() > 0) {
            return super.getItemViewType(getActualPosition(position));
        } else {
            return 0;
        }
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(getActualPosition(position));
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        super.onBindViewHolder(holder, getActualPosition(position));
        // because of getCurrentPosition may return ViewHolder‘s position,
        // so we must reset mPosition if exists.
        try {
            Field mPosition = RecyclerView.ViewHolder.class.getDeclaredField("mPosition");
            mPosition.setAccessible(true);
            mPosition.set(holder, position);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public int getActualPosition(int position) {
        int actualPosition = position;
        if (getActualItemCount() > 0 && position >= getActualItemCount()) {
            actualPosition = position % getActualItemCount();
        }
        return actualPosition;
    }
}
