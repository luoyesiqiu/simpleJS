package com.luoye.simplejs;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.widget.TextView;

import com.eclipsesource.v8.JavaVoidCallback;
import com.eclipsesource.v8.Releasable;
import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;

/**
 * Created by zyw on 2017/10/28.
 */
public class RunScript extends Activity {
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.console_activity);
        textView=(TextView)findViewById(R.id.console_tv);
        ActionBar actionBar=getActionBar();
        if(actionBar!=null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        String code=getIntent().getStringExtra("code");
        run(code);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private  void run(final String script)
    {
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                final V8 runtime = V8.createV8Runtime();
                runtime.registerJavaMethod(callback, "print");
                try {
                    runtime.executeScript(script);
                    runtime.release();
                }
                catch(Exception e)
                {
                    handler.sendMessage(handler.obtainMessage(0,e.toString()));
                }
            }
        });
        thread.start();
    }
    Handler handler=new  Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            try {
                textView.append(String.valueOf(msg.obj));
            }catch (Exception e)
            {
                textView.append(e.toString());
            }
        }
    };
    JavaVoidCallback callback = new JavaVoidCallback() {
        public void invoke(final V8Object receiver, final V8Array parameters) {
            if (parameters.length() > 0) {

                if(parameters.length()<2)
                {
                    handler.sendMessage(handler.obtainMessage(0,parameters.get(0)));
                }else {
                    Object[] params = new Object[parameters.length()-1];
                    for (int i = 0; i < parameters.length()-1; i++) {
                        params[i] = parameters.get(i+1);
                    }
                    handler.sendMessage( handler.obtainMessage(0,String.format(parameters.getString(0),params)));
                }
                if (parameters instanceof Releasable) {
                    ((Releasable) parameters).release();
                }

            }
        }
    };
}
