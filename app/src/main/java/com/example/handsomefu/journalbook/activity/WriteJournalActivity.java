package com.example.handsomefu.journalbook.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.handsomefu.journalbook.utils.CommonUtils;
import com.example.handsomefu.journalbook.constants.Constants;
import com.example.handsomefu.journalbook.R;

/**
 * Created by HandsomeFu on 2016/11/2.
 */
public class WriteJournalActivity extends BaseActivity implements View.OnClickListener{
    private EditText etTitle;
    private LinearLayout ll;
    private EditText etContent;
    private Button btSave;
    private Intent intent;
    private int from;
    @Override
    protected void initEvent() {
        if (from == Constants.EDIT) {
            etTitle.setText(intent.getStringExtra(Constants.TITLE));
            etContent.setText(intent.getStringExtra(Constants.CONTENT));
        }
        ll.setOnClickListener(this);
        CommonUtils.autoSoftKeyboard(etTitle);
        btSave.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        intent = getIntent();
        from = intent.getIntExtra(Constants.FROM, 1);
    }

    @Override
    protected void initView() {
        etTitle = (EditText) findViewById(R.id.et_title);
        ll = (LinearLayout) findViewById(R.id.ll);
        etContent = (EditText) findViewById(R.id.et_content);
        btSave = (Button) findViewById(R.id.bt_save);
    }

    @Override
    public int getLayout() {
        return R.layout.ac_write_journal;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll:
                CommonUtils.autoSoftKeyboard(etContent);
                break;
            case R.id.bt_save:
                intent.putExtra(Constants.TITLE, etTitle.getText().toString());
                intent.putExtra(Constants.CONTENT, etContent.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }
}
