package com.luoye.simplejs;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.luoye.simplejs.activity.FileListActivity;
import com.luoye.simplejs.util.ConstantPool;
import com.luoye.simplejs.util.Utils;
import com.luoye.simplejs.view.TextEditor;
import com.myopicmobile.textwarrior.common.WriteThread;

import java.io.File;

public class MainActivity extends Activity {

    private TextEditor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editor=new TextEditor(this);
        setContentView(editor);

        showToast("落叶似秋");
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode== ConstantPool.OK_SELECT_RESULT_CODE) {
            if(data!=null) {
                String path = data.getStringExtra("path");
                editor.open(path);
                ActionBar actionBar = getActionBar();
                if (actionBar != null)
                    actionBar.setSubtitle(editor.getOpenedFile().getName());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_run:
                if(editor.getOpenedFile()!=null) {
                    editor.save(editor.getOpenedFile().getAbsolutePath());
                }
                else
                {
                    showToast("没有打开文件");
                }
            Intent intent=new Intent(MainActivity.this,RunScript.class);
            intent.putExtra("code",editor.getText().toString());
            startActivity(intent);
            break;
            case R.id.menu_close_file:
                editor.setOpenedFile(null);
                ActionBar actionBar=getActionBar();
                if (actionBar != null)
                    actionBar.setSubtitle(null);
                editor.setText("");
                break;
            case R.id.menu_redo:
                editor.redo();
                break;

            case R.id.menu_undo:
                editor.undo();
                break;

            case R.id.menu_open:
                startActivityForResult(new Intent(MainActivity.this, FileListActivity.class),0);
                break;
            case  R.id.menu_save:
                if(editor.getOpenedFile()!=null) {
                    editor.save(editor.getOpenedFile().getAbsolutePath());
                }
                else
                {
                    showToast("没有打开文件");
                }
                break;
            case R.id.menu_save_as:
                final EditText editText=new EditText(MainActivity.this);
                AlertDialog alertDialog=new AlertDialog.Builder(MainActivity.this)
                        .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (!TextUtils.isEmpty(editText.getText())) {
                                    File f = new File(ConstantPool.FILE_PATH);
                                    WriteThread writeThread=new WriteThread(editor.getText().toString(),f.getAbsolutePath() + File.separator + editText.getText(),handler);
                                    writeThread.start();
                                } else {
                                    showToast("请输入文件名");
                                }
                            }
                        })
                        .setTitle("输入文件名")
                        .setView(editText)
                        .create();
                alertDialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private  Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {

        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    private Toast toast;
    private  void showToast(CharSequence text){

        if(toast==null)
            toast=Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT);
        else
        toast.setText(text);
        toast.show();
    }
}
