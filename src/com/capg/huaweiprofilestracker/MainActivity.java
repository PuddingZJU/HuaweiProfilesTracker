package com.capg.huaweiprofilestracker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.CheckBoxPreference;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.R.bool;
import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity implements SensorEventListener, OnInitListener {
	private boolean _isFirstFrame=true;
	private double _baseTimestamp=0;
	private long _beginTimeInNano=0;
	private Button onOffButton = null;
	private Button historyButton = null;
	private ImageView statusView = null;
	private int statulabel = 0;
	private boolean storeLocally = true;
	private int sample_rate;
	private float checktimer;
	private SensorManager mySensorManager;
	private Timer statusTimer;
	private String pathName = "/sdcard/HuaweiProfilesTracker/";
	private String fileName = "history.txt";
	private File path;
	private File file;
	private int selectlabel = 0;
	private TextToSpeech mTts;  
	private String about_str = "Write About Here....";
	 private static final int REQ_TTS_STATUS_CHECK = 0;
	private String[] statuslabelname = { "Avtivities of Daliy Life", "Walking",
			"Running", "Driving", "Falling" };
	final Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == 1) {
				Toast.makeText(getApplicationContext(),
						Float.valueOf(checktimer).toString() + "s Passed",
						Toast.LENGTH_SHORT).show();
				CheckStatus();
			}
		}

	};

	private void CheckStatus() {
		//edit code here
		Random random = new Random();
		SetStatue(random.nextInt(5), "System");
	}
	private void PushAccelerometerData(float[] values,double timestamp) {
		//sensorData
		//add your code here 
	}
	private void PushGyroscopeData(float[] values,double timestamp) {
		//sensorData
		//add your code here 
	}
	private void PushMagneticFiledData(float[] values,double timestamp) {
		//sensorData
		//add your code here 
	}
	private void PushRotationData(float[] values,double timestamp) {
		//sensorData
		//add your code here 
	}
	private void SetStatue(int label, String Opeartor) {
		if (label == statulabel) {
			return;
		}
		statulabel = label;
		if (statulabel >= statuslabelname.length) {
			statulabel = 0;
		}
		if (storeLocally) {
			FileOutputStream stream;
			try {
				stream = new FileOutputStream(file, true);
				SimpleDateFormat sDateFormat = new SimpleDateFormat(
						"yyyy-MM-dd hh:mm:ss:SSS ZZZZ");
				String s = sDateFormat.format(new java.util.Date()) + ":	"
						+ Opeartor + "	changed status to "
						+ statuslabelname[statulabel] + "\n";
				byte[] buf = s.getBytes();
				stream.write(buf);
				stream.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		switch (statulabel) {
		case 1: {
			statusView.setImageResource(R.drawable.walk);
			mTts.speak(statuslabelname[statulabel]+"Mode Detected",TextToSpeech.QUEUE_ADD, null);
			break;
		}
		case 2: {
			statusView.setImageResource(R.drawable.run);
			mTts.speak(statuslabelname[statulabel]+"Mode Detected", TextToSpeech.QUEUE_ADD, null);
			break;
		}
		case 3: {
			statusView.setImageResource(R.drawable.drive);
			mTts.speak(statuslabelname[statulabel]+"Mode Detected", TextToSpeech.QUEUE_ADD, null);
			break;
		}
		case 4: {
			statusView.setImageResource(R.drawable.fall);
			mTts.speak(statuslabelname[statulabel]+"Mode Detected", TextToSpeech.QUEUE_ADD, null);
			break;
		}
		default: {
			statusView.setImageResource(R.drawable.stand);
			mTts.speak(statuslabelname[statulabel]+"Mode Detected", TextToSpeech.QUEUE_ADD, null);
			break;
		}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == R.id.action_settings) {
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, SettingsActivity.class);
			startActivity(intent);
		}
		if (item.getItemId() == R.id.action_about) {
			AlertDialog.Builder builder = new Builder(MainActivity.this);
			builder.setMessage(about_str);
			builder.setTitle("About");
			builder.setNegativeButton("OK", new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create().show();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		path = new File(pathName);
		file = new File(pathName + fileName);
		setContentView(R.layout.activity_main);
		onOffButton = (Button) findViewById(R.id.OnOffButton);
		historyButton = (Button) findViewById(R.id.HistoryButton);
		statusView = (ImageView) findViewById(R.id.conditionimageView);
		mySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		onOffButton.setText("Service Off");
		Intent checkIntent = new Intent();  
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);  
		startActivityForResult(checkIntent, REQ_TTS_STATUS_CHECK);  
		initPreference();
	}
	protected  void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQ_TTS_STATUS_CHECK)
		{
			switch (resultCode) {
			case TextToSpeech.Engine.CHECK_VOICE_DATA_PASS:
				//这个返回结果表明TTS Engine可以用
			{
				mTts = new TextToSpeech(this, this);
				//Log.v(TAG, "TTS Engine is installed!");
				
			}
				
				break;
			case TextToSpeech.Engine.CHECK_VOICE_DATA_BAD_DATA:
				//需要的语音数据已损坏
			case TextToSpeech.Engine.CHECK_VOICE_DATA_MISSING_DATA:
				//缺少需要语言的语音数据
			case TextToSpeech.Engine.CHECK_VOICE_DATA_MISSING_VOLUME:
				//缺少需要语言的发音数据
			{
				//这三种情况都表明数据有错,重新下载安装需要的数据
				//Log.v(TAG, "Need language stuff:"+resultCode);
				Intent dataIntent = new Intent();
				dataIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(dataIntent);
				
			}
				break;
			case TextToSpeech.Engine.CHECK_VOICE_DATA_FAIL:
				//检查失败
			default:
				//Log.v(TAG, "Got a failure. TTS apparently not available");
				break;
			}
		}
		else
		{
			//其他Intent返回的结果
		}
		}
	public void OnOffButtonClicked(View view) {
		if (onOffButton.getText() == "Service On") {
			onOffButton.setText("Service Off");
			Service_Off();
		} else {
			onOffButton.setText("Service On");
			Service_On();
		}
	}

	public void HistoryButtonClicked(View view) {
		if (storeLocally) {
			Intent intent = new Intent("android.intent.action.VIEW");
			intent.addCategory("android.intent.category.DEFAULT");
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			Uri uri = Uri.fromFile(file);
			intent.setDataAndType(uri, "text/plain");
			startActivity(intent);
		} else {
			Toast.makeText(getApplicationContext(),
					"You didn't store histoty locally!", Toast.LENGTH_LONG)
					.show();
		}
	}

	public void StatusViewClicked(View view) {
		new AlertDialog.Builder(MainActivity.this)
				.setTitle("Please select the Correct Mode:")
				.setItems(statuslabelname,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								selectlabel = which;
								new AlertDialog.Builder(MainActivity.this)
										.setTitle(
												"You chose:"
														+ statuslabelname[which])
										.setMessage(
												"Are you sure to change the Mode?")
										.setPositiveButton(
												"Yes",
												new DialogInterface.OnClickListener() {

													public void onClick(
															DialogInterface dialog,
															int which) {
														SetStatue(selectlabel,
																"User");
													}
												})
										.setNegativeButton(
												"No",
												new DialogInterface.OnClickListener() {

													public void onClick(
															DialogInterface dialog,
															int which) {
														// 这里点击取消之后可以进行的操作
													}
												}).show();
							}
						}).show();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onRestart() {
		initPreference();
		super.onRestart();
	}

	@Override
	protected void onResume() {
		initPreference();
		super.onResume();
	}

	private void initPreference() {
		SharedPreferences mPreferences = getSharedPreferences("settings",
				Context.MODE_PRIVATE);
		storeLocally = mPreferences.getBoolean("sedl", true);
		sample_rate = mPreferences.getInt("sample_rate", 50);
		checktimer = mPreferences.getFloat("stimer", 5);
		if (storeLocally) {
			if (!path.exists()) {
				path.mkdir();
			}
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					Toast.makeText(getApplicationContext(),
							"Can't Write To SDCard!!", Toast.LENGTH_LONG)
							.show();
					Editor editor = mPreferences.edit();
					editor.putBoolean("sedl", false);
					editor.commit();
					storeLocally = false;
					e.printStackTrace();
				}
			}
		} else {
			Toast.makeText(getApplicationContext(),
					"Stopped Writing History To SDCard", Toast.LENGTH_LONG)
					.show();
		}
	}

	private void Service_On() {
		mTts.speak("Servive On", TextToSpeech.QUEUE_ADD, null);
		statusTimer = new Timer();
		TimerTask statustimerTask = new TimerTask() {

			@Override
			public void run() {
				Message message = new Message();
				message.what = 1;
				handler.sendMessage(message);
			}

		};
		_baseTimestamp=System.currentTimeMillis();
		statusTimer.schedule(statustimerTask, 0, (int) checktimer * 1000);
		mySensorManager.registerListener(this,
				mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				sample_rate);
		mySensorManager.registerListener(this,
				mySensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
				sample_rate);
		mySensorManager.registerListener(this,
				mySensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
				sample_rate);
		mySensorManager.registerListener(this,
				mySensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
				sample_rate);
		
	}

	private void Service_Off() {
		_baseTimestamp=0;
		_beginTimeInNano=0;
		statusTimer.cancel();
		mySensorManager.unregisterListener(this);
		if(mTts != null)  
	        //activity暂停时也停止TTS  
	    {  
	        mTts.stop();  
	    }  
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		long ts=event.timestamp;
		if(_isFirstFrame){
			_isFirstFrame=false;
			
			_beginTimeInNano=ts;
		}
		double epochTime=_baseTimestamp*Consts.MS2S+(ts-_beginTimeInNano)*Consts.NS2S;
		System.out.println("epochTime: "+epochTime);
		// TODO Auto-generated method stub
		
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
		 	PushAccelerometerData(event.values,epochTime);
		}
		if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
			PushGyroscopeData(event.values,epochTime);
		}
		if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
			PushMagneticFiledData(event.values,epochTime);
		}
		if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
			PushRotationData(event.values, epochTime);
		}
	}

	@Override
	public void onInit(int arg0) {
		// TODO Auto-generated method stub
		if(arg0 == TextToSpeech.SUCCESS)
		{
			int result = mTts.setLanguage(Locale.US);
			//设置发音语言
			if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
			//判断语言是否可用
			{
				//Log.v(TAG, "Language is not available");
				//speakBtn.setEnabled(false);
			}
			else
			{
				mTts.speak("System Ready", TextToSpeech.QUEUE_ADD, null);
			}
				//speakBtn.setEnabled(true);
			}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		 mTts.shutdown(); 
		super.onDestroy();
	}
}
