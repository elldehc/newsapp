package com.java.liqibin.ui.category;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.java.liqibin.R;
import com.java.liqibin.model.bean.Category;
import com.java.liqibin.model.db.NewsDatabase;

import java.util.ArrayList;
import java.util.List;

import selectlabeltab.jessie.com.libtabmanage.BaseTabListAdapter;
import selectlabeltab.jessie.com.libtabmanage.ItemDragHelperCallback;

public class EditCategoryActivity extends Activity {

    private RecyclerView recyclerView;

    private ArrayList<Category> myTabs;
    private ArrayList<Category> otherTabs;
    private ArrayList<Category> originalTabs;

    private boolean modified = false;

    public static final int FINISH_RESULT_CODE = 8;
    public static final int JUMP_RESULT_CODE = 9;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        myTabs = new ArrayList<>();
        otherTabs = new ArrayList<>();

        Category latest = new Category();
        latest.name = "最新";
        latest.position = 0;
        myTabs.add(latest);

        SQLiteDatabase database = NewsDatabase.getReadable();
        Cursor cursor = database.query("categories", new String[]{"*"},
                null, null, null, null, "position");
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Category category = new Category();
            category.id = cursor.getInt(0);
            category.name = cursor.getString(1);
            category.position = cursor.getInt(2);
            if (category.position >= 0) {
                myTabs.add(category);
            } else {
                otherTabs.add(category);
            }
        }

        originalTabs = new ArrayList<>(myTabs);

        recyclerView = findViewById(R.id.recyclerView);
        GridLayoutManager manager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(manager);

        ItemDragHelperCallback callback = new ItemDragHelperCallback();
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);

        TabListAdapter adapter = new TabListAdapter(this, helper, myTabs, otherTabs);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = adapter.getItemViewType(position);
                return viewType == BaseTabListAdapter.TYPE_MY || viewType == BaseTabListAdapter.TYPE_OTHER ? 1 : 4;
            }
        });
        recyclerView.setAdapter(adapter);

        adapter.setOnMyItemClickListener(((view, i) -> {
            updateDatabase();
            Intent intent = new Intent();
            intent.putExtra("target", i);
            intent.putExtra("modified", modified);
            setResult(JUMP_RESULT_CODE, intent);
            finish();
        }));

        findViewById(R.id.icon_collapse).setOnClickListener((view -> {
            updateDatabase();
            Intent intent = new Intent();
            intent.putExtra("modified", modified);
            setResult(FINISH_RESULT_CODE, intent);
            finish();
        }));
    }

    @Override
    public void onBackPressed() {
        updateDatabase();
        Intent intent = new Intent();
        intent.putExtra("modified", modified);
        setResult(FINISH_RESULT_CODE, intent);
        finish();
    }

    private void updateDatabase() {
        SQLiteDatabase database = NewsDatabase.getWritable();
        ContentValues contentValues = new ContentValues();
        for (int i = 1; i < myTabs.size(); i++) {
            contentValues.clear();
            Category category = myTabs.get(i);
            contentValues.put("position", category.position = i - 1);
            database.update("categories", contentValues, "id=" + category.id, null);
        }
        contentValues.clear();
        contentValues.put("position", -1);
        for (Category category : otherTabs) {
            database.update("categories", contentValues, "id=" + category.id, null);
        }

        if (originalTabs.size() != myTabs.size()) {
            modified = true;
        } else {
            for (int i = 0; i < myTabs.size(); i++) {
                if (originalTabs.get(i).id != myTabs.get(i).id) {
                    modified = true;
                    break;
                }
            }
        }
    }
}
