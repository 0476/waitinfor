package com.ailikes.waitinfor;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RawRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,TextToSpeech.OnInitListener {

    //定义控件
//    private Button speechButton;
    private TextView speechText;
    private TextToSpeech tts;

    private FloatingActionButton speechButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化TTS
        tts = new TextToSpeech(this, this);
        tts.setPitch(1.5f);
        //设定语速 ，默认1.0正常语速
        tts.setSpeechRate(1.5f);
        //获取控件
        speechText = (TextView)findViewById(R.id.speechTextView);
        speechText.setMovementMethod(ScrollingMovementMethod.getInstance());
        speechButton = (FloatingActionButton)findViewById(R.id.speechButton);
        String text = getStringFromFile(R.raw.a);
        speechText.setText(text);
        //为button添加监听
        speechButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // TODO Auto-generated method stub
                tts.speak(speechText.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });

    }

    @Override
    public void onInit(int status){
        // 判断是否转化成功
        if (status == TextToSpeech.SUCCESS){
            //默认设定语言为中文，原生的android貌似不支持中文。
            int result = tts.setLanguage(Locale.SIMPLIFIED_CHINESE);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Toast.makeText(MainActivity.this, R.string.notAvailable, Toast.LENGTH_SHORT).show();
            }else{
                //不支持中文就将语言设置为英文
                tts.setLanguage(Locale.US);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        if (tts != null && !tts.isSpeaking()) {
            // 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
            tts.setPitch(0.5f);
            //设定语速 ，默认1.0正常语速
            tts.setSpeechRate(1.5f);
            //朗读，注意这里三个参数的added in API level 4   四个参数的added in API level 21
            tts.speak(speechText.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        tts.stop(); // 不管是否正在朗读TTS都被打断
        tts.shutdown(); // 关闭，释放资源
    }



    public String getStringFromFile(int id) {
        InputStream inputStream = getResources().openRawResource(id);

        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuffer sb = new StringBuffer("");
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
