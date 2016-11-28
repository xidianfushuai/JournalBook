package com.example.handsomefu.journalbook.activity;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.handsomefu.journalbook.constants.Constants;
import com.example.handsomefu.journalbook.R;
import com.example.handsomefu.journalbook.db.bean.Journal;
import com.example.handsomefu.journalbook.db.dao.JournalDao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private RecyclerView rvList;
    private FloatingActionButton fabAdd;
    private JournalDao journalDao;
    private List<Journal> journalList;
    private HomeAdapter adapter;
    private int edit_item_id;

    @Override
    protected void initEvent() {
        fabAdd.setOnClickListener(this);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HomeAdapter();
        adapter.setOnItemClickLitener(new RVOnItemClickListener());
        rvList.setAdapter(adapter);

    }

    @Override
    protected void initData() {
        journalDao = new JournalDao(this);
        journalList = journalDao.queryForAll();
    }

    @Override
    protected void initView() {
        rvList = (RecyclerView) findViewById(R.id.rv_list);
        fabAdd = (FloatingActionButton) findViewById(R.id.fab_add);
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
        private OnItemClickLitener mOnItemClickLitener;
        Journal journal = null;

        public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
            this.mOnItemClickLitener = mOnItemClickLitener;
        }

        @Override
        public HomeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    MainActivity.this).inflate(R.layout.item_home, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final HomeAdapter.MyViewHolder holder, final int position) {
            journal = journalList.get(journalList.size() - 1 - position);
            holder.tvTitle.setText(journal.getTitle());
            holder.tvContent.setText(journal.getContent());
            holder.tvTime.setText(journal.getTime());
            // 如果设置了回调，则设置点击事件
            if (mOnItemClickLitener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickLitener.onItemClick(holder.itemView, position);
                    }
                });

                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mOnItemClickLitener.onItemLongClick(holder.itemView, position);
                        return false;
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return journalList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle;
            TextView tvContent;
            TextView tvTime;

            public MyViewHolder(View itemView) {
                super(itemView);
                tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
                tvContent = (TextView) itemView.findViewById(R.id.tv_content);
                tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            }
        }
    }

    @Override
    public int getLayout() {
        return R.layout.ac_main;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_add:
                startActivity(new Intent(this, WriteJournalActivity.class));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.WRITE:
                if (resultCode == RESULT_OK) {
                    Journal journal = new Journal(data.getStringExtra(Constants.TITLE),
                            data.getStringExtra(Constants.CONTENT), getCurrentTime());
                    //将新添加的Journal添加到数据库
                    journalDao.add(journal);
                    updateList();
                }
                break;
            case Constants.EDIT:
                if (resultCode == RESULT_OK) {
                    Journal journal = new Journal(data.getStringExtra(Constants.TITLE),
                            data.getStringExtra(Constants.CONTENT), getCurrentTime());
                    //修改数据库中的信息
                    journalDao.update(edit_item_id, journal);
                    updateList();
                }
                break;
        }
    }

    private String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        return formatter.format(curDate);
    }

    private class RVOnItemClickListener implements OnItemClickLitener {
        @Override
        public void onItemClick(View view, int position) {
            edit_item_id = journalList.get(journalList.size() - 1 - position).getId();
            Intent intent = new Intent(MainActivity.this, WriteJournalActivity.class);
            intent.putExtra(Constants.FROM, Constants.EDIT);
            intent.putExtra(Constants.TITLE, journalList.get(journalList.size() - 1 - position).getTitle());
            intent.putExtra(Constants.CONTENT, journalList.get(journalList.size() - 1 - position).getContent());
            startActivityForResult(intent, Constants.EDIT);
        }

        @Override
        public void onItemLongClick(View view, final int position) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("确认删除该条记录吗？");
            builder.setTitle("警告");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //删除该条记录
                    journalDao.delete(journalList.get(journalList.size() - 1 - position).getId());
                    dialog.dismiss();
                    updateList();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }
    }

    private void updateList() {
        journalList = journalDao.queryForAll();
        adapter.notifyDataSetChanged();
    }
}
