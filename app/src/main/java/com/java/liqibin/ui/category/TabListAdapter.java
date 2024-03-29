package com.java.liqibin.ui.category;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.java.liqibin.R;
import com.java.liqibin.model.bean.Category;

import java.util.List;

import selectlabeltab.jessie.com.libtabmanage.BaseTabListAdapter;
import selectlabeltab.jessie.com.libtabmanage.OnItemDragListener;
import selectlabeltab.jessie.com.libtabmanage.RecyclerviewAnimator;


public class TabListAdapter extends BaseTabListAdapter {

    private LayoutInflater mInflater;
    private List<Category> myItems, otherItems;//我的tab项，其余tab项

    public TabListAdapter(Context context, ItemTouchHelper helper, List<Category> myItems, List<Category> otherItems) {
        super(myItems, otherItems);
        this.mInflater = LayoutInflater.from(context);
        this.mItemTouchHelper = helper;
        this.myItems = myItems;
        this.otherItems = otherItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View view;
        switch (viewType) {
            case BaseTabListAdapter.TYPE_MY_HEADER://我的分页标题
                view = mInflater.inflate(R.layout.item_my_header, parent, false);
                final MyHeaderViewHolder holder = new MyHeaderViewHolder(view);
                holder.tvBtnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isEditMode) {
                            startEditMode((RecyclerView) parent);
                            holder.tvBtnEdit.setText("完成");
                        } else {
                            cancelEditMode((RecyclerView) parent);
                            holder.tvBtnEdit.setText("编辑");
                        }
                    }
                });
                return holder;
            case BaseTabListAdapter.TYPE_MY: //我的分页
                view = mInflater.inflate(R.layout.item_my, parent, false);
                final MyViewHolder myHolder = new MyViewHolder(view);
                //item点击监听
                myHolder.textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        int position = myHolder.getAdapterPosition();
                        //编辑模式
                        if (isEditMode) {
                            //执行移动到其他分页的逻辑和动画
                            synchronized (myItems) {
                                RecyclerviewAnimator.myToOther(position, (RecyclerView) parent, TabListAdapter.this, myHolder, myItems, otherItems);

                            }

                        } else {
                            //执行点击事件回调
                            onMyItemClickListener.onItemClick(v, position - BaseTabListAdapter.COUNT_PRE_MY_HEADER);
                        }

                    }
                });
                //长按监听
                myHolder.textView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(final View v) {
                        //非编辑模式下
                        if (!isEditMode) {
                            RecyclerView recyclerView = ((RecyclerView) parent);
                            //启动开始编辑
                            startEditMode(recyclerView);

                            // header 按钮文字 改成 "完成"
                            View view = recyclerView.getChildAt(0);
                            if (view == recyclerView.getLayoutManager().findViewByPosition(0)) {
                                TextView tvBtnEdit = (TextView) view.findViewById(R.id.tv_btn_edit);
                                tvBtnEdit.setText("完成");
                            }
                        }
                        //开始拖拽
                        mItemTouchHelper.startDrag(myHolder);
                        return true;
                    }
                });
                //触摸监听
                myHolder.textView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (isEditMode) {
                            int position = myHolder.getAdapterPosition();
                            if (position == 1) {//过滤第一个
                                return false;
                            }
                            switch (MotionEventCompat.getActionMasked(event)) {
                                case MotionEvent.ACTION_DOWN:
                                    //按下时间记录
                                    startTime = System.currentTimeMillis();
                                    break;
                                case MotionEvent.ACTION_MOVE:
                                    //如果当前时间-按下时间大于间隔时间，判定为拖拽
                                    if (System.currentTimeMillis() - startTime > BaseTabListAdapter.SPACE_TIME) {
                                        mItemTouchHelper.startDrag(myHolder);
                                    }
                                    break;
                                case MotionEvent.ACTION_CANCEL:
                                case MotionEvent.ACTION_UP:
                                    startTime = 0;
                                    break;
                            }

                        }
                        return false;
                    }
                });
                return myHolder;
            case BaseTabListAdapter.TYPE_OTHER_HEADER: //其他分页标题
                view = mInflater.inflate(R.layout.item_other_header, parent, false);
                return new RecyclerView.ViewHolder(view) {
                };
            case BaseTabListAdapter.TYPE_OTHER: //其他分页
                view = mInflater.inflate(R.layout.item_other, parent, false);
                final OtherViewHolder otherHolder = new OtherViewHolder(view);
                otherHolder.textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        synchronized (otherItems) {
                            RecyclerviewAnimator.otherToMy((RecyclerView) parent, TabListAdapter.this, otherHolder, myItems, otherItems);

                        }
                    }
                });
                return otherHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //我的分页
        if (holder instanceof MyViewHolder) {
            MyViewHolder myHolder = (MyViewHolder) holder;
            myHolder.textView.setText(myItems.get(position - BaseTabListAdapter.COUNT_PRE_MY_HEADER).name);
            if (position == 1) {//过滤第一个,字体颜色更改
                myHolder.textView.setTextColor(Color.GRAY);
            }
            if (isEditMode) {
                myHolder.imgEdit.setVisibility(View.VISIBLE);
            } else {
                myHolder.imgEdit.setVisibility(View.INVISIBLE);
            }

        }
        //其他分页
        else if (holder instanceof OtherViewHolder) {

            ((OtherViewHolder) holder).textView.setText(otherItems.get(position - myItems.size() - BaseTabListAdapter.COUNT_PRE_OTHER_HEADER).name);

        }
        //我的分页标题
        else if (holder instanceof MyHeaderViewHolder) {

            MyHeaderViewHolder headerHolder = (MyHeaderViewHolder) holder;
            if (isEditMode) {
                headerHolder.tvBtnEdit.setText("完成");
            } else {
                headerHolder.tvBtnEdit.setText("编辑");
            }
        }
    }


    //开启编辑
    protected void startEditMode(RecyclerView parent) {
        isEditMode = true;  //更新标志
        //得到item数量
        int visibleChildCount = parent.getChildCount();
        //遍历item的view
        for (int i = 0; i < visibleChildCount; i++) {
            View view = parent.getChildAt(i);
            if (i == 1) {//过滤第一个，屏蔽点击事件
                TextView textView = (TextView) view.findViewById(R.id.tv);
                textView.setTextColor(Color.GRAY);
                textView.setClickable(false);
                textView.setEnabled(false);
            } else { //设置右上角按钮可见
                ImageView imgEdit = (ImageView) view.findViewById(R.id.img_edit);
                if (imgEdit != null) {
                    imgEdit.setVisibility(View.VISIBLE);
                }
            }

        }
    }

    //完成编辑
    protected void cancelEditMode(RecyclerView parent) {
        isEditMode = false;//更新标志
        //得到item数量
        int visibleChildCount = parent.getChildCount();
        //遍历item的view
        for (int i = 0; i < visibleChildCount; i++) {
            View view = parent.getChildAt(i);
            if (i == 1) { //过滤第一个，恢复点击事件
                TextView textView = (TextView) view.findViewById(R.id.tv);
                textView.setTextColor(Color.GRAY);
                textView.setClickable(true);
                textView.setEnabled(true);
            } else { //设置右上角按钮不可见
                ImageView imgEdit = (ImageView) view.findViewById(R.id.img_edit);
                if (imgEdit != null) {
                    imgEdit.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    //我的分页holder
    public static class MyViewHolder extends RecyclerView.ViewHolder implements OnItemDragListener {
        private TextView textView;
        private ImageView imgEdit;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv);
            imgEdit = itemView.findViewById(R.id.img_edit);
        }

        //item 被选中时
        @Override
        public void onItemSelected() {
            textView.setBackgroundResource(R.drawable.bg_channel_p);
        }

        //item 取消选中时
        @Override
        public void onItemFinish() {
            textView.setBackgroundResource(R.drawable.bg_channel);
        }
    }

    //其他分页holder
    public static class OtherViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public OtherViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv);
        }
    }

    //我的分页标题holder
    public static class MyHeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView tvBtnEdit;

        public MyHeaderViewHolder(View itemView) {
            super(itemView);
            tvBtnEdit = itemView.findViewById(R.id.tv_btn_edit);
        }
    }


}
