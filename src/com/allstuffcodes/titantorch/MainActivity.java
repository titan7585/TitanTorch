package com.allstuffcodes.titantorch;


import android.app.Activity;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends Activity {
	
	private Button togglebutton;
	private Camera camera;
	private boolean isFlashOn;
	private boolean hasFlash;
	Parameters params;
	

	  

	  private static final int COLOR_DARK = 0xCC000000;
	  private static final int COLOR_LIGHT = 0xCCf2dd87;
	  private static final int COLOR_WHITE = 0xFFFFFFFF;
	  private View screenlight;
	  

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //flash button
        togglebutton = (Button) findViewById(R.id.togglebutton);
        //screenlight for devices without flash
        screenlight = (View) findViewById(R.id.screenlight);
        
        //checking whether flash is present
        //hasFlash = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        
        //if(!hasFlash){
        	//screenlight.setBackgroundColor(COLOR_WHITE);
        	//return;
       // }
        
      //get the camera details
        getCamera();
        
        //switch flash on/off
        togglebutton.setOnClickListener(new View.OnClickListener() {
        	 
            @Override
            public void onClick(View v) {
                if (isFlashOn) {
                    // turn off flash
                    turnOffFlash();
                } else {
                    // turn on flash
                    turnOnFlash();
                }
            }
        });

        	 
            
        
    }
    
  //getCamera() function
  	private void getCamera(){
  		if(camera == null){
  			try{
  				camera = Camera.open();
  				params = camera.getParameters();
  			}
  			catch(RuntimeException e){
  				Log.e("Failed to open camera. Error : ", e.getMessage());
  			}
  		}
  	}
  	
  	
 // Turning On flash
    private void turnOnFlash() {
        if (!isFlashOn) {
            if (camera == null) {
                return;
            }
            
            if(params == null){
            	//use screen light
            	screenlight.setBackgroundColor(COLOR_WHITE);
            	return;
            }
            
            hasFlash = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
            if(!hasFlash){
            	screenlight.setBackgroundColor(COLOR_WHITE);
            	WindowManager.LayoutParams lp = getWindow().getAttributes();
            	lp.screenBrightness = 1;
            	getWindow().setAttributes(lp);
            	
            }
            else{
            	params = camera.getParameters();
            	params.setFlashMode(Parameters.FLASH_MODE_TORCH);
            	camera.setParameters(params);
            	
            	screenlight.setBackgroundColor(COLOR_LIGHT);
            	WindowManager.LayoutParams lp = getWindow().getAttributes();
            	lp.screenBrightness = 1;
            	getWindow().setAttributes(lp);
            	camera.startPreview();
            	            	
            }
            togglebutton.setText("Turn Off");
            //startWakeLock();
            isFlashOn = true;
        }
 
    }
  	
  	
 // Turning Off flash
    private void turnOffFlash() {
        if (isFlashOn) {
            if (camera == null) {
                return;
            }
            
            if(params == null){
            	//turn screen light off
            	screenlight.setBackgroundColor(COLOR_DARK);
            	return;
            }
            
            hasFlash = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
            if(!hasFlash){
            	screenlight.setBackgroundColor(COLOR_DARK);
            	
            }
            else{
            	params = camera.getParameters();
            	params.setFlashMode(Parameters.FLASH_MODE_OFF);
            	camera.setParameters(params);
            	
            	screenlight.setBackgroundColor(COLOR_DARK);
            	camera.stopPreview();
            
            }
            togglebutton.setText("Turn On");
            //stopWakeLock();
            isFlashOn = false;
        }
    }
	
  	
  	
  	     
    @Override
    public void onBackPressed() {
    	super.onBackPressed();
    	
    	params = camera.getParameters();
        params.setFlashMode(Parameters.FLASH_MODE_OFF);
        camera.setParameters(params);
        camera.stopPreview();
        isFlashOn = false;
        
      if (camera != null) {
        
        camera.release();
        camera = null;
      }
      
    }

    


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
