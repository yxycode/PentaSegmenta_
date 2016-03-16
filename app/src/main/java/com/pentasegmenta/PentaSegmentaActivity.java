package com.pentasegmenta;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.KeyEvent;
import android.media.SoundPool;
import android.media.AudioManager;
import android.graphics.Typeface;

import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.FileNotFoundException;

import android.widget.RelativeLayout;
import android.view.*;
import android.content.res.Configuration;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.graphics.Rect;
import android.util.DisplayMetrics;

import com.google.ads.*;
import android.content.pm.PackageManager;
import android.content.pm.PackageInfo;

//=======================================================================================
//#######################################################################################
//=======================================================================================

class GameEngine
{
  public static int LandScapeModeFlag = 0;
  public final static int ME_PRESS_DOWN = 0;
  public final static int ME_RELEASE = 1; 
  public final static int ME_MOVE = 2;    
  
  public static final int ME_NOTHING = -1;
    
  public final static int G_EVENT_KEY = 1;
  public final static int G_EVENT_TOUCH = 2;
  public final static int G_EVENT_SCREEN_SIZE_CHANGE = 3;
  public final static int G_EVENT_PROGRAM_CLOSING = 4;
  public final static int G_EVENT_PROGRAM_STARTED = 5;
  
  public final static int G_REQUEST_WRITE_FILE = 6;
  public final static int G_REQUEST_READ_FILE = 7;  
  public final static int G_REQUEST_SET_SCREEN_ADJUSTMENTS = 8;
  public final static int G_REQUEST_SET_PAINT_OPTIONS = 9;
  public final static int G_REQUEST_DRAW_IMAGE_TEXT_PRIMITIVE = 10;
  public final static int G_REQUEST_PLAY_SOUND = 11;
  
  public final static int G_DRAW_POINT = 1000;
  public final static int G_DRAW_LINE = 1001;
  public final static int G_DRAW_BOX = 1002;
  public final static int G_DRAW_CIRCLE = 1003;  
  public final static int G_STYLE_FILL = 2000;
  public final static int G_STYLE_FILL_AND_STROKE = 2001;
  public final static int G_STYLE_STROKE = 2002;
  
  private final static int MAX_DRAW_STRINGS = 900;
  private final static int MAX_DRAW_IMAGES = 2000;
  private final static int MAX_DRAW_PRIMITIVES = 900;

  public static int TouchAction = ME_NOTHING;
  public static int TouchX = ME_NOTHING;
  public static int TouchY = ME_NOTHING;

  public static final int ME_KEY_DOWN = 3;
  public static final int ME_KEY_UP = 4;
  public static int KeyCode = 0;
  public static int KeyEventStatus = ME_NOTHING;

  public final static int TARGET_SCREEN_WIDTH = 320;
  public final static int TARGET_SCREEN_HEIGHT = 480;
    
  public static int ScreenWidth = TARGET_SCREEN_WIDTH;
  public static int ScreenHeight = TARGET_SCREEN_HEIGHT;
  public static int InnerScreenWidth = TARGET_SCREEN_WIDTH;
  public static int InnerScreenHeight = TARGET_SCREEN_HEIGHT;
  public static float Xscale = 1.0f;
  public static float Yscale = 1.0f;
  public static boolean RescaleFlag = false;
  
  public static int PaintColor = 0;
  public static int PaintTextSize = 20;
  public static float StrokeWidth = 0.0f;
  
  public static String FileName;
  public static String FileDataStr;
  public static int MaxFileReadLength = 50000;
  public static int FileAppendFlag;
  
  public static int SoundPlayIndex;

  public static final int MAX_Z_ORDER = 4;
  public static final int LAYER_0 = 0;
  public static final int LAYER_1 = 1;
  public static final int LAYER_2 = 2;
  public static final int LAYER_3 = 3;
  public static final int LAYER_4 = 4;
  
  public static int[] ImageIndexList;
  public static int[] ImageZOrder;
  public static int[] ImageX;
  public static int[] ImageY;
  public static int[] ImageAlpha;
  public static int[] ImageSubData;
  public static int ImageIndexListLength;
  
  public final static int IMAGE_SUB_DATA_LENGTH = 7;

  public static String[] TextOutList;
  public static int[] TextZOrder;
  public static int[] TextX;
  public static int[] TextY;
  public static int[] TextColor;
  public static int[] TextSize;  
  public static int TextOutListLength;
  
  public static int[] PrimitiveType;
  public static float[] Primitive1;
  public static float[] Primitive2;
  public static float[] Primitive3;
  public static float[] Primitive4;
  public static int[] PrimitiveStyle;
  public static int[] PrimitiveColor;
  public static int[] PrimitiveZOrder;
  public static int PrimitiveCount;
  
  public static GameEngine CurrentObj = null;
  
  ControlManager TheControlManager;
  
  public static int FileCompressFlag = 0;
  
  public static String dstr = "WH = 0, 0;";
  
//---------------------------------------------------------------------------------------
public GameEngine()
  {
	   TouchAction = ME_NOTHING;
	   TouchX = ME_NOTHING;
	   TouchY = ME_NOTHING;
	   KeyCode = 0;
	   KeyEventStatus = ME_NOTHING;
	   ScreenWidth = 320;
	   ScreenHeight = 480;
	   InnerScreenWidth = 320;
	   InnerScreenHeight = 480;
	   Xscale = 1.0f;
	   Yscale = 1.0f;
	   RescaleFlag = false;
	   PaintColor = 0;
	   PaintTextSize = 20;	  
  	   ImageIndexListLength = 0;
	   TextOutListLength = 0;

	    ImageIndexList = new int[MAX_DRAW_IMAGES];
	    ImageZOrder = new int[MAX_DRAW_IMAGES];
	    ImageX = new int[MAX_DRAW_IMAGES];
	    ImageY = new int[MAX_DRAW_IMAGES];
	    ImageAlpha = new int[MAX_DRAW_IMAGES];
	    ImageSubData = new int[MAX_DRAW_IMAGES * IMAGE_SUB_DATA_LENGTH];

	    TextOutList = new String[MAX_DRAW_STRINGS];
	    TextZOrder = new int [MAX_DRAW_STRINGS];
	    TextX =new int [MAX_DRAW_STRINGS];
	    TextY =new int [MAX_DRAW_STRINGS];
	    TextColor =new int [MAX_DRAW_STRINGS];
	    TextSize =new int [MAX_DRAW_STRINGS];
	    
	    PrimitiveType = new int[MAX_DRAW_PRIMITIVES];
	    Primitive1 = new float[MAX_DRAW_PRIMITIVES];
	    Primitive2 = new float[MAX_DRAW_PRIMITIVES];
	    Primitive3 = new float[MAX_DRAW_PRIMITIVES];
	    Primitive4 = new float[MAX_DRAW_PRIMITIVES];
	    PrimitiveStyle = new int[MAX_DRAW_PRIMITIVES];
	    PrimitiveColor = new int[MAX_DRAW_PRIMITIVES];
	    PrimitiveZOrder = new int[MAX_DRAW_PRIMITIVES];
	    PrimitiveCount = 0;  
	    
	    FileAppendFlag = 0;
	    
	    CurrentObj = this;
	    GameObject.SetGameEngine(this);
	    
	    TheControlManager = new ControlManager();
	    //TheControlManager.Init();            
  }
//---------------------------------------------------------------------------------------
public void DoRequest( int Request )
{
	GameControl.CurrentObj.ProcessGameEngineRequest( Request );
}
//---------------------------------------------------------------------------------------
public void NotifyEvent( int GEventId )
  {
    switch( GEventId )
    {
       case G_EVENT_PROGRAM_STARTED:
    	   break;
       case G_EVENT_PROGRAM_CLOSING:
    	   if( TheControlManager != null )
    	   {
    		   GameOptions.Save();
    	   }
    	   
    	   // *** debug ***
/*    	   
    	   GameEngine.CurrentObj.FileName = "closelog.txt";
    	   GameEngine.CurrentObj.FileDataStr = "program closed," + GameGlobals.counter + ",";
    	   GameEngine.CurrentObj.DoRequest(GameEngine.CurrentObj.G_REQUEST_WRITE_FILE);
*/
    	   break;
       case G_EVENT_SCREEN_SIZE_CHANGE:    	 
    	 //** landscape method **
   
    	 if( LandScapeModeFlag > 0 )
    	 {
    	   Yscale = (ScreenHeight + 0.0f) / InnerScreenHeight;
    	   Xscale = Yscale;
           InnerScreenHeight = ScreenHeight; // ** resize to fit vertical size of the screen **  
           InnerScreenWidth = (int)(InnerScreenWidth * Xscale + 0.0f);                            
           RescaleFlag = true;
    	 }
    	 else
    	 {
    	 //** potrait method **
    	   
    	   Xscale = (ScreenWidth + 0.0f) / InnerScreenWidth;
    	   Yscale = Xscale;
           InnerScreenWidth = ScreenWidth; // ** resize to fit horizontal size of the screen **  
           InnerScreenHeight = (int)(InnerScreenHeight * Yscale + 0.0f);                            
           RescaleFlag = true;           
            
           //dstr = "WH = " + ScreenWidth + ", "+ ScreenHeight + ";";
    	 }
           DoRequest(G_REQUEST_SET_SCREEN_ADJUSTMENTS);  	     
    	 break;
       case G_EVENT_KEY:
         /*
           KeyEventStatus, KeyCode
         */    	 
    	 //mouseinfo = new String("Key: " + KeyCode); 
    	 
         TheControlManager.DoKeyEvent( KeyEventStatus, KeyCode );         
         KeyEventStatus = KeyCode = ME_NOTHING;           
         break;
       case G_EVENT_TOUCH:
         /*
           TouchAction { ME_PRESS_DOWN, ME_RELEASE, ME_MOVE }
         */   
    	 GameObject.MouseStatus = TouchAction;
    	 GameObject.MouseX = TouchX;
    	 GameObject.MouseY = TouchY;
    	 
    	 TheControlManager.DoClickEvent(TouchAction, TouchX, TouchY);
                          
         break;           
       default:
         break;
    };
  }
//---------------------------------------------------------------------------------------
public void DrawText( String str, int X, int Y, int Z)
{
	if( TextOutListLength >= MAX_DRAW_STRINGS )
		return;
	
	TextOutList[TextOutListLength] = new String(str);
	TextX[TextOutListLength] = X;
	TextY[TextOutListLength] = Y;
	TextZOrder[TextOutListLength] = Z;
	TextSize[TextOutListLength] = 0;
	TextOutListLength++;  
}
//---------------------------------------------------------------------------------------
public void DrawTextColor( String str, int X, int Y, int Z, int nColor, int Size )
{	
	if( TextOutListLength >= MAX_DRAW_STRINGS )
		return;
	
	TextOutList[TextOutListLength] = new String(str);
	TextX[TextOutListLength] = X;
	TextY[TextOutListLength] = Y;
	TextZOrder[TextOutListLength] = Z;
	TextColor[TextOutListLength] = nColor;
	TextSize[TextOutListLength] = Size;	
	TextOutListLength++; 	
}
//---------------------------------------------------------------------------------------
public void DrawImage( int ImageIndex, int X, int Y, int Z )
{
  if( ImageIndexListLength >= MAX_DRAW_IMAGES )
	  return;
  
  ImageIndexList[ImageIndexListLength] = ImageIndex;
  ImageX[ImageIndexListLength] = X;
  ImageY[ImageIndexListLength] = Y;
  ImageZOrder[ImageIndexListLength] = Z;
  ImageAlpha[ImageIndexListLength] = 255;
  ImageSubData[ImageIndexListLength*IMAGE_SUB_DATA_LENGTH] = 0;
  ImageIndexListLength++;
}
//---------------------------------------------------------------------------------------
public void DrawImageAlpha( int ImageIndex, int X, int Y, int Z, int AlphaValue )
{
  if( ImageIndexListLength >= MAX_DRAW_IMAGES )
	  return;
  
  ImageIndexList[ImageIndexListLength] = ImageIndex;
  ImageX[ImageIndexListLength] = X;
  ImageY[ImageIndexListLength] = Y;
  ImageZOrder[ImageIndexListLength] = Z;
  ImageAlpha[ImageIndexListLength] = AlphaValue;
  ImageIndexListLength++;  
}
//---------------------------------------------------------------------------------------
public void DrawSubImage( int ImageIndex, int X, int Y, int Z, int Alpha, int SrcX, int SrcY, int SrcWidth, int SrcHeight )
{
  if( ImageIndexListLength >= MAX_DRAW_IMAGES )
	  return;	
	
  ImageIndexList[ImageIndexListLength] = ImageIndex;
  ImageX[ImageIndexListLength] = X;
  ImageY[ImageIndexListLength] = Y;
  ImageZOrder[ImageIndexListLength] = Z;
  ImageAlpha[ImageIndexListLength] = Alpha;
  
  int k = ImageIndexListLength * IMAGE_SUB_DATA_LENGTH;
  
  ImageSubData[k] = 1;
  ImageSubData[k + 1] = SrcX;
  ImageSubData[k + 2] = SrcY;
  ImageSubData[k + 3] = SrcWidth + 1;
  ImageSubData[k + 4] = SrcHeight + 1;
  ImageSubData[k + 5] = 100;
  ImageSubData[k + 6] = 100;
				  
  ImageIndexListLength++;
  
}
//---------------------------------------------------------------------------------------
public void DrawSubImage( int ImageIndex, int X, int Y, int Z, int Alpha, int SrcX, int SrcY, int SrcWidth, int SrcHeight,
		int nScalePercent_X, int nScalePercent_Y )
{
  if( ImageIndexListLength >= MAX_DRAW_IMAGES )
	  return;
  
  ImageIndexList[ImageIndexListLength] = ImageIndex;
  ImageX[ImageIndexListLength] = X;
  ImageY[ImageIndexListLength] = Y;
  ImageZOrder[ImageIndexListLength] = Z;
  ImageAlpha[ImageIndexListLength] = Alpha;
  
  int k = ImageIndexListLength * IMAGE_SUB_DATA_LENGTH;
  
  ImageSubData[k] = 1;
  ImageSubData[k + 1] = SrcX;
  ImageSubData[k + 2] = SrcY;
  ImageSubData[k + 3] = SrcWidth + 1;
  ImageSubData[k + 4] = SrcHeight + 1;
  ImageSubData[k + 5] = nScalePercent_X;
  ImageSubData[k + 6] = nScalePercent_Y;
  
  ImageIndexListLength++;
  
}
//---------------------------------------------------------------------------------------
public void DrawPoint( float X, float Y, int Z, int cColor )
{
  if( PrimitiveCount >= MAX_DRAW_PRIMITIVES )
	  return;
  
  PrimitiveType[PrimitiveCount] = G_DRAW_POINT;
  Primitive1[PrimitiveCount] = X;
  Primitive2[PrimitiveCount] = Y;
  PrimitiveStyle[PrimitiveCount] = G_STYLE_STROKE;
  PrimitiveZOrder[PrimitiveCount] = Z;
  PrimitiveColor[PrimitiveCount] = cColor;
  PrimitiveCount++;
}
//---------------------------------------------------------------------------------------
public void DrawLine( float X1, float Y1, float X2, float Y2, int Z, int cColor )
{
  if( PrimitiveCount >= MAX_DRAW_PRIMITIVES )
	  return;
  
	  PrimitiveType[PrimitiveCount] = G_DRAW_LINE ;
	  Primitive1[PrimitiveCount] = X1;
	  Primitive2[PrimitiveCount] = Y1;
	  Primitive3[PrimitiveCount] = X2;
	  Primitive4[PrimitiveCount] = Y2;	  
	  PrimitiveStyle[PrimitiveCount] = G_STYLE_STROKE;
	  PrimitiveZOrder[PrimitiveCount] = Z;
	  PrimitiveColor[PrimitiveCount] = cColor;
	  PrimitiveCount++;
}
//---------------------------------------------------------------------------------------
public void DrawBox( float X1, float Y1, float X2, float Y2, int Z, int cColor, int Style )
{
  if( PrimitiveCount >= MAX_DRAW_PRIMITIVES )
	  return;
  
	  PrimitiveType[PrimitiveCount] = G_DRAW_BOX;
	  Primitive1[PrimitiveCount] = X1;
	  Primitive2[PrimitiveCount] = Y1;
	  Primitive3[PrimitiveCount] = X2;
	  Primitive4[PrimitiveCount] = Y2;	  
	  PrimitiveStyle[PrimitiveCount] = Style;
	  PrimitiveZOrder[PrimitiveCount] = Z;
	  PrimitiveColor[PrimitiveCount] = cColor;
	  PrimitiveCount++;	
}
//---------------------------------------------------------------------------------------
public void DrawCircle( float X1, float Y1, float Radius, int Z, int cColor, int Style )
{
  if( PrimitiveCount >= MAX_DRAW_PRIMITIVES )
	  return;
  
	  PrimitiveType[PrimitiveCount] = G_DRAW_CIRCLE;
	  Primitive1[PrimitiveCount] = X1;
	  Primitive2[PrimitiveCount] = Y1;
	  Primitive3[PrimitiveCount] = Radius;  
	  PrimitiveStyle[PrimitiveCount] = Style;
	  PrimitiveZOrder[PrimitiveCount] = Z;
	  PrimitiveColor[PrimitiveCount] = cColor;
	  PrimitiveCount++;	
}
//---------------------------------------------------------------------------------------
public void MainLoop()
{	
    PaintColor = Color.GREEN;
    PaintTextSize = 20;
    StrokeWidth = 2;    
    DoRequest(G_REQUEST_SET_PAINT_OPTIONS);

	 TheControlManager.Do();
	 TheControlManager.Draw();
	
	 //DrawTextColor( dstr, 10, 30, LAYER_3, Color.rgb(255,255,0), 16 );
	 
     DoRequest(G_REQUEST_DRAW_IMAGE_TEXT_PRIMITIVE);
          
     
}
//---------------------------------------------------------------------------------------
public static void PlaySound( int sound_index )
{
   GameControl.CurrentObj.PlaySound( sound_index );
}
//---------------------------------------------------------------------------------------
}
//=======================================================================================
//#######################################################################################
//=======================================================================================
class ToolBox
{
//---------------------------------------------------------------------------------------	
public static boolean CheckDigit( char c )
{
	  boolean result = true;
	  
	  String DigitStr = new String("1234567890");	 
	  
	  if( DigitStr.indexOf(c) == -1)
		  result = false;
	  
	  return result;
}
//--------------------------------------------------------------------------------------- 
public static int SafeString2Int( String str )
{	  
	int i, result = 0;
    int length = str.length();
	String SafeStr;
	
  for( i = 0; i < length; i++ )
  if( CheckDigit(str.charAt(i)) == false )
      break;
  
  if( i - 1 < 0 )
      i = 0;
  
  SafeStr = new String(str.substring(0, i - 1));
 
  result = Integer.parseInt(SafeStr);
	return result;
}
}
//=======================================================================================
//#######################################################################################
//=======================================================================================
class JukeBox
{
  private final int MAX_SOUNDS = 10;
  private final int SOUND_POOL_PRIORITY = 1;
  
  public final static JukeBox CurrentObj = new JukeBox();
  
  private SoundPool SoundPoolObj;
  private AudioManager MyAudioManager;
  private int[] SoundIdList = new int[MAX_SOUNDS];
  private float StreamVolume;
  private float MaxStreamVolume;
  private float SOUND_RATE = 1;
  private int LOOP_FLAG = 0;
  private Context ContextObj;
  public static int SoundsLoadedCount = 0;
  
//---------------------------------------------------------------------------------------
  public void Init(Context pContextObj)
  {
	ContextObj = pContextObj;
    MyAudioManager = (AudioManager)ContextObj.getSystemService(Context.AUDIO_SERVICE);   
	StreamVolume = MyAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    MaxStreamVolume = MyAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	StreamVolume = MyAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	SoundPoolObj = new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC, 100);	

    SoundPoolObj.setOnLoadCompleteListener( new SoundPool.OnLoadCompleteListener() 
       {
        public void onLoadComplete(SoundPool soundPool, int sampleId, int status)
              {
                   SoundsLoadedCount++;
              }
      });
    
    SoundsLoadedCount = 0;
  }
//---------------------------------------------------------------------------------------  
  public void AddSound(int index, int SoundResourceId)
  {
	SoundIdList[index] = SoundPoolObj.load(ContextObj, SoundResourceId, SOUND_POOL_PRIORITY); 
  }
//---------------------------------------------------------------------------------------  
  public void PlaySound(int index)
  {	  
	SoundPoolObj.play(SoundIdList[index], StreamVolume, StreamVolume, SOUND_POOL_PRIORITY, LOOP_FLAG, SOUND_RATE); 
  }
//---------------------------------------------------------------------------------------    
  public void PlaySoundMuteLoop( int index )
  {
	// play any sound in a muted loop to solve lag problem	  
	SoundPoolObj.play(SoundIdList[index], 0, 0, 1, -1, 1f);
  }
//---------------------------------------------------------------------------------------    
}
//=======================================================================================
//#######################################################################################
//=======================================================================================
class GameLoopThread extends Thread 
{ 
  static final long FramesPerSecond = 30;
  private GameView GameViewObj;
  private boolean RunningFlag = false;
  
  protected int StartedFlag = 0;
  
//---------------------------------------------------------------------------------------
  public void SetView( GameView ViewObj )
  {
	  GameViewObj = ViewObj;
  }
//---------------------------------------------------------------------------------------  
  public synchronized void SetRunning(boolean runflag) 
  {
     RunningFlag = runflag;
  }
//---------------------------------------------------------------------------------------
  @Override
  public void run() 
  {	 
     while (RunningFlag) 
     {         
         long ticksPS = 1000 / FramesPerSecond;
         long startTime;
         long sleepTime;    	   
         Canvas CanvasObj = null;
         startTime = System.currentTimeMillis();
                    
        try 
        {
      	  CanvasObj = GameViewObj.getHolder().lockCanvas();
      
            synchronized (GameViewObj.getHolder()) 
             {
            	GameViewObj.onDraw(CanvasObj);
             }
         
        } 
        finally 
        {
         if (CanvasObj != null) 
            {
        	 GameViewObj.getHolder().unlockCanvasAndPost(CanvasObj);
            }
        }

        sleepTime = ticksPS-(System.currentTimeMillis() - startTime);

        try 
        {
               if (sleepTime > 0)
                   sleep(sleepTime);
               else
                   sleep(10);
        } 
        catch (Exception e) 
        { 
        }
     }
 }   
//=======================================================================================
@Override
public synchronized void start() 
{
    if (StartedFlag == 0 )
    {
    	StartedFlag = 1;
        super.start();
    }
}
//=======================================================================================
}
//=======================================================================================
//#######################################################################################
//=======================================================================================
class FileWorker
{
  private static Context ContextObj;
  private final static int MAX_FILE_READ_SIZE = 300000;

//----------------------------------------------------------------------- 
  public static void SetContext( Context pContextObj )
  {
	  ContextObj = pContextObj;
  }
//----------------------------------------------------------------------- 
  public static void WriteCompressed( String filename, String outstr )
  {
	    try
	    {   	
	     FileOutputStream fout = ContextObj.openFileOutput(filename, Context.MODE_WORLD_WRITEABLE);  
	     GZIPOutputStream zos = new GZIPOutputStream(fout);
	     zos.write(outstr.getBytes("UTF-8"));
	     zos.close();   
	    }
	    catch (IOException ioe) 
	    { 
	    }  	  
  }
//-----------------------------------------------------------------------
  public static String ReadCompressed( String filename, final int MaxLength )
  {
		 String ReadString = new String("");	
		 int BytesRead;
		
		 File file = new File(filename);
		 
		 File file2 = ContextObj.getFileStreamPath(filename);
		 String fullfilepath = new String( file2.getAbsolutePath());
				 
	    if( file2.exists())
	    {	  	    	
		try
		{ 
		     
	     FileInputStream fis = ContextObj.openFileInput(filename);
	     GZIPInputStream zis = new GZIPInputStream(fis);
	     
	     byte[] InputBuffer = new byte[MaxLength];
	     BytesRead = zis.read(InputBuffer, 0, MaxLength);
	     zis.close();
	     
	       if( BytesRead > 0 ) 
	    	 ReadString = new String(InputBuffer);
   	       
	    }
	    catch (IOException ioe) 
	    { 
	    } 
	     
	   } 

	    return ReadString;	  
  }
//-----------------------------------------------------------------------
  public static void Write( String filename, String outstr )
  {
	  if( GameEngine.FileCompressFlag > 0 )
	  {
		  WriteCompressed( filename, outstr );
		  return;
	  }
/*	  
	  FileOutputStream fos = null;
	  try 
	  {
	  fos = ContextObj.openFileOutput(filename, ContextObj.MODE_WORLD_WRITEABLE);
	  } 
	  catch (FileNotFoundException e) 
	  {
	  }
	  try 
	  {
	  fos.write(outstr.getBytes());
	  } 
	  catch (IOException e) 
	  {
	  }
	  try 
	  {
	  fos.close();
	  } 
	  catch (IOException e) 
	  {

	  }	  
*/  	  
    try
    {   	
     FileOutputStream fout = ContextObj.openFileOutput(filename, Context.MODE_WORLD_WRITEABLE);     
     OutputStreamWriter osw = new OutputStreamWriter(fout);   
     osw.write(outstr);
     osw.flush();
     osw.close();  
   
    }
    catch (IOException ioe) 
    { 
    }     
  }
//-----------------------------------------------------------------------  
  public static String Read( String filename, final int MaxLength )
  {	  
	  if( filename.indexOf("raw:") >= 0 )
		  return ReadRawTextFile( Integer.valueOf(filename.substring(4)) );
				  
	  if( GameEngine.FileCompressFlag > 0 )
	  {
		  return ReadCompressed( filename, MaxLength );		  
	  }
	  
	 String ReadString = new String("");	
	 int BytesRead;
	
	 File file = new File(filename);
	 
	 File file2 = ContextObj.getFileStreamPath(filename);
	 String fullfilepath = new String( file2.getAbsolutePath());
			 
    if( file2.exists())
    {	  
   	
	try
	{ 
	     
     FileInputStream fis = ContextObj.openFileInput(filename);

     byte[] InputBuffer = new byte[MaxLength];
     BytesRead = fis.read(InputBuffer, 0, MaxLength);
     fis.close();
     
       if( BytesRead > 0 ) 
       {	
    	ReadString = new String(InputBuffer, "UTF-8");  
 /*   	   
        StringBuilder sb = new StringBuilder(BytesRead);
        int i;
        for( i = 0; i < file2.length(); i++ )
    	  sb.append((char)InputBuffer[i]);

        ReadString = new String(sb.substring(0, (int)file2.length()-1));
*/        
       }     
    }
    catch (IOException ioe) 
    { 
    } 
     
   } 

    return ReadString;
  }
//-----------------------------------------------------------------------   
  public static String ReadRawTextFile(int resId)
  {
       InputStream inputStream = ContextObj.getResources().openRawResource(resId);

          InputStreamReader inputreader = new InputStreamReader(inputStream);
          BufferedReader buffreader = new BufferedReader(inputreader);
           String line;
           StringBuilder text = new StringBuilder();

           try {
             while (( line = buffreader.readLine()) != null) {
                 text.append(line);
                 text.append('\n');
               }
         } catch (IOException e) {
             return null;
         }
           return text.toString();
  }  
//-----------------------------------------------------------------------
public static void CopyFile( String filename_dest, String filename_src )
{
  String str = Read( filename_src, MAX_FILE_READ_SIZE );
  Write( filename_dest, str );
}
//-----------------------------------------------------------------------
public static boolean FileExists( String filename )
{
  File file = ContextObj.getFileStreamPath(filename);

  if(file.exists())
	 return true;
  else
	 return false;
}
//-----------------------------------------------------------------------
}
//=======================================================================================
//#######################################################################################
//=======================================================================================
public class PentaSegmentaActivity extends Activity 
{
	private static GameView TheGameView;
    private GameLoopThread TheGameLoopThread;
	public static PentaSegmentaActivity CurrentObj;

	public static RelativeLayout ItsRelativeLayout;
	
	public static int ScreenWidth;
	public static int ScreenHeight;

	protected static AdView ItsAdView;
    protected static final String MY_AD_UNIT_ID = "ca-app-pub-0089998212238224/7417265462";
    
  //---------------------------------------------------------------------------------------  		
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {  	
        super.onCreate(savedInstanceState);  
        Init();
    }
  //--------------------------------------------------------------------------------------- 
      public void Init()
      {
          requestWindowFeature(Window.FEATURE_NO_TITLE);
          getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
          
          CurrentObj = this;        
          FileWorker.SetContext(this);
          JukeBox.CurrentObj.Init(this);       
          
          GameControl.CurrentObj = new GameControl();
          GameControl.CurrentObj.MainActivityObj = this;
          GameEngine.CurrentObj = new GameEngine();
          
          TheGameLoopThread = new GameLoopThread();        
          TheGameView = new GameView(this, TheGameLoopThread);    
          
          ItsRelativeLayout = new RelativeLayout(this);
      	  ItsRelativeLayout.addView(TheGameView);
      	  
          //*** Create the adView ***
          //ItsAdView = new AdView(this, AdSize.BANNER, MY_AD_UNIT_ID);      	  
      	
      	  RelativeLayout.LayoutParams TheLayoutParams = new RelativeLayout.LayoutParams(
      	  RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

      	  TheLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
      	  TheLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
      	
      	  //*** Add the adView to it ***
      	  //ItsRelativeLayout.addView(ItsAdView, TheLayoutParams);
          
      	  //*** Initiate a generic request to load it with an ad ***
          //ItsAdView.loadAd(new AdRequest());
         
          TheGameLoopThread.SetView(TheGameView);        
          setContentView(ItsRelativeLayout);                      
          TheGameView.setFocusable(true);
          TheGameView.setFocusableInTouchMode(true);        
          
          DisplayMetrics displaymetrics = new DisplayMetrics();
          getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
          ScreenHeight = displaymetrics.heightPixels;
          ScreenWidth = displaymetrics.widthPixels;

      }
//---------------------------------------------------------------------------------------      
@Override
public void onDestroy() 
{
  super.onDestroy();
}    
//---------------------------------------------------------------------------------------	 
@Override
public void onPause()
{
  super.onPause();
  ControlManager.PauseFlag = 1;	
}
//---------------------------------------------------------------------------------------	 
@Override
public void onResume()
{
  super.onResume();
  ControlManager.PauseFlag = 0;		
}
//---------------------------------------------------------------------------------------
public static int GetVersionCode()
{
  int result = 0;
  
try 
{	
  PackageManager manager = CurrentObj.getPackageManager();
  PackageInfo info = manager.getPackageInfo(CurrentObj.getPackageName(), 0);
  result = info.versionCode;
}  
catch (Exception e)
{
}
  return result;
}
//---------------------------------------------------------------------------------------		  
}

//=======================================================================================
//#######################################################################################
//=======================================================================================
class GameControl
{
  public static final int MAX_BITMAPS = 100;
  public static final int MAX_Z_ORDER = GameEngine.MAX_Z_ORDER;
  public static final int ME_NOTHING = -1;
  public static final int ME_PRESS_DOWN = 0;
  public static final int ME_RELEASE = 1; 
  public static final int ME_MOVE = 2;
  
  public static final int ME_KEY_DOWN = 3;
  public static final int ME_KEY_UP = 4;
  
  public static Bitmap[] BitmapList = new Bitmap[MAX_BITMAPS];  
  public static int BitmapListLength = 0;
  public static float[] ImageScaleRelative_320x480 = new float[MAX_BITMAPS];
  
  public static int TouchAction = ME_NOTHING;
  public static int TouchX = ME_NOTHING;
  public static int TouchY = ME_NOTHING;
  public static float TouchXf = 0;
  public static float TouchYf = 0;

  public static int KeyCode = 0;
  public static int KeyEventStatus = ME_NOTHING; 
    
  private static int InnerScreenWidth = 320;
  private static int InnerScreenHeight = 480;
  public static int ScreenWidth = 0;
  public static int ScreenHeight = 0;
  private static int XShift = 0;
  private static int YShift = 0;
  private static int ClipXTopLeft = 0;
  private static int ClipYTopLeft = 0;
  private static int ClipXBottomRight = 0;
  private static int ClipYBottomRight = 0;
  private static float XScale = 1.0f;
  private static float YScale = 1.0f;
  private static boolean RescaleFlag = false;
  
  public boolean DoDrawRequestNext_onDraw_Flag = false;
  
  private Paint PaintObj = null;
  private Paint PaintObjPrimitive = null;
  private Paint PaintObjAlpha = null;
  
  public static Canvas CanvasObj; 
  public static GameControl CurrentObj;
  public PentaSegmentaActivity MainActivityObj;
  
//---------------------------------------------------------------------------------------

  public final static int G_EVENT_KEY = 1;
  public final static int G_EVENT_TOUCH = 2;
  public final static int G_EVENT_SCREEN_SIZE_CHANGE = 3;
  public final static int G_EVENT_PROGRAM_CLOSING = 4;
  public final static int G_EVENT_PROGRAM_STARTED = 5;
  
  public final static int G_REQUEST_WRITE_FILE = 6;
  public final static int G_REQUEST_READ_FILE = 7;  
  public final static int G_REQUEST_SET_SCREEN_ADJUSTMENTS = 8;
  public final static int G_REQUEST_SET_PAINT_OPTIONS = 9;
  public final static int G_REQUEST_DRAW_IMAGE_TEXT_PRIMITIVE = 10;
  public final static int G_REQUEST_PLAY_SOUND = 11;
  
//---------------------------------------------------------------------------------------  
    public final static int IMAGE_TILES_1x1 = 1;
    public final static int IMAGE_TILES_2x1 = 2;
    public final static int IMAGE_TILES_4x1 = 3;
    public final static int IMAGE_MENU_CURSOR = 4;
    public final static int IMAGE_MAIN_TITLE = 5;
    
    public final static int IMAGE_FONT_LM_18x27 = 6;
    public final static int IMAGE_CELLS = 7;
    public final static int IMAGE_PLAYER_SHIP = 8;
    public final static int IMAGE_BEAMS = 9;
    
    public final static int BACKGROUND_330x490_0 = 10;
    
    public final static int IMAGE_LAST_INDEX = 19;
    
     
//---------------------------------------------------------------------------------------      
  public GameControl()
  {
      ScreenWidth = 320;
      ScreenHeight = 480;
      InnerScreenWidth = ScreenWidth; InnerScreenHeight = ScreenHeight;
      XShift = 0; YShift = 0;
      ClipXTopLeft = 0;
      ClipYTopLeft = 0;
      ClipXBottomRight = ScreenWidth;
      ClipYBottomRight = ScreenHeight;    
      XScale = YScale = 1.0f;
      RescaleFlag = false; 
      DoDrawRequestNext_onDraw_Flag = false;
      PaintObj = new Paint();
      PaintObj.setTypeface(Typeface.MONOSPACE);
      PaintObjPrimitive = new Paint();
      PaintObjAlpha = new Paint();
  }  
//---------------------------------------------------------------------------------------
  public Bitmap CreateScaledBitmap( Bitmap bmp )
  {
	 int w, h, new_w, new_h;
	 final boolean FilterFlag = false;
	 
	 w = bmp.getWidth();
	 h = bmp.getHeight();
	 
	 new_w = (int)(XScale * w); 
     new_h = (int)(YScale * h);
	 
	 return Bitmap.createScaledBitmap (bmp, new_w, new_h, FilterFlag);
  }
//--------------------------------------------------------------------------------------- 
  public void LoadImage( int BitmapListIndex, int ResourceIndex, BitmapFactory.Options op )
  {
	  BitmapList[BitmapListIndex] = BitmapFactory.decodeResource( MainActivityObj.getResources(), ResourceIndex, op );	  
  }
//--------------------------------------------------------------------------------------- 
  public void LoadImages()
  {
	int i;
	BitmapFactory.Options op = new BitmapFactory.Options();
	
	if( Integer.parseInt(Build.VERSION.SDK) > 4)			
	    op.inScaled = false;
	
    LoadImage( 0, R.drawable.blocks, op );

    LoadImage(1, R.drawable.tiles_1x1, op );
    LoadImage(2, R.drawable.tiles_2x1, op );
    LoadImage(3, R.drawable.tiles_4x1, op );
    LoadImage(4, R.drawable.menu_cursor, op);
    LoadImage(5, R.drawable.maintitle , op );
    LoadImage(6, R.drawable.font_lm_18x27, op ); 
    LoadImage(7, R.drawable.cells, op ); 
    LoadImage(8, R.drawable.playership, op );
    LoadImage(9, R.drawable.beams, op );    
    LoadImage(10, R.drawable.background_330x490_0, op );
    LoadImage(11, R.drawable.background_330x490_1, op );
    LoadImage(12, R.drawable.background_330x490_2, op );
    LoadImage(13, R.drawable.background_330x490_3, op );
    LoadImage(14, R.drawable.background_330x490_4, op );
    LoadImage(15, R.drawable.background_330x490_5, op );
    LoadImage(16, R.drawable.background_330x490_6, op );
    LoadImage(17, R.drawable.background_330x490_7, op );
    LoadImage(18, R.drawable.background_330x490_8, op );
    LoadImage(19, R.drawable.background_330x490_9, op );  
    
    Bitmap TempBitMap;
    
    RescaleFlag = false;
    if(RescaleFlag)
    {
     for( i = 0; i < MAX_BITMAPS; i++ )
      if( BitmapList[i] != null )
      {
    	  TempBitMap = BitmapList[i];
          BitmapList[i] = CreateScaledBitmap(TempBitMap); 
          TempBitMap.recycle();          
      }
      else
    	  break;
    }    
    
  for( i = 0; i < MAX_BITMAPS; i++ )
       ImageScaleRelative_320x480[i] = 1.0f;   
  
    ImageScaleRelative_320x480[1] = 1.5f; // IMAGE_TILES_1x1
    ImageScaleRelative_320x480[2] = 1.5f; // IMAGE_TILES_2x1
    ImageScaleRelative_320x480[3] = 1.5f; // IMAGE_TILES_4x1 
    ImageScaleRelative_320x480[4] = 1.5f; // IMAGE_MENU_CURSOR
    ImageScaleRelative_320x480[5] = 2.0f; // IMAGE_MAIN_TITLE
    
  }
//--------------------------------------------------------------------------------------- 
public static int CheckDoneLoading()
{
	if( BitmapList[IMAGE_LAST_INDEX] != null && 
	    JukeBox.SoundsLoadedCount >= 4 )
	{	
		JukeBox.CurrentObj.PlaySoundMuteLoop(0);
		return 1;
	}
	else
		return 0;
}  
//--------------------------------------------------------------------------------------- 
  public void LoadSounds()
  {	 
    JukeBox.CurrentObj.AddSound(0, R.raw.beam);
    JukeBox.CurrentObj.AddSound(1, R.raw.push);	
    JukeBox.CurrentObj.AddSound(2, R.raw.scatter);	
    JukeBox.CurrentObj.AddSound(3, R.raw.bip );
  }  
//---------------------------------------------------------------------------------------  
  public void SetCanvas( Canvas pCanvas )
  {
    CanvasObj = pCanvas;
    CanvasObj.clipRect(ClipXTopLeft, ClipYTopLeft, ClipXBottomRight, ClipYBottomRight);
  }
//---------------------------------------------------------------------------------------   
  public void SetScreenSize(int nScreenWidth, int nScreenHeight)
  {	 
	  ScreenWidth = InnerScreenWidth = nScreenWidth; ScreenHeight = InnerScreenHeight = nScreenHeight;
	  XShift = ScreenWidth/2 - InnerScreenWidth/2;
	  YShift = ScreenHeight/2 - InnerScreenHeight/2; 
      ClipXTopLeft = XShift;
      ClipYTopLeft = YShift;
      ClipXBottomRight = XShift + InnerScreenWidth;
      ClipYBottomRight = YShift + InnerScreenHeight;                
  }
//--------------------------------------------------------------------------------------- 
  public void SetScreenAdjustments( int innerscreenwidth, int innerscreenheight, 
		  float xscale, float yscale, boolean rescaleflag )
  {
      InnerScreenWidth = innerscreenwidth;
      InnerScreenHeight = innerscreenheight;
	  XShift = ScreenWidth/2 - InnerScreenWidth/2;
	  YShift = ScreenHeight/2 - InnerScreenHeight/2;      
      XScale = xscale;
      YScale = yscale;
      
      ClipXTopLeft = XShift;
      ClipYTopLeft = YShift;
      ClipXBottomRight = XShift + InnerScreenWidth;
      ClipYBottomRight = YShift + InnerScreenHeight; 
      RescaleFlag = rescaleflag;    
  }  
//---------------------------------------------------------------------------------------  
  public void SetPaintOptions(int Color, int TextSize, float StrokeWidth)
  {	 
	PaintObj.setColor(Color); 
	
	if( TextSize > 0 )
  	  PaintObj.setTextSize(TextSize); 
	
	if( StrokeWidth >= 0 )
	{
	  PaintObj.setStrokeWidth(StrokeWidth);
	  PaintObjPrimitive.setStrokeWidth(StrokeWidth);
	}
  }
//---------------------------------------------------------------------------------------  
  public void DrawImage( int Index, int X, int Y )  
  {	  	
/*	  
	 X = (int)(X * XScale + XShift); Y = (int)(Y * YScale + YShift);
     CanvasObj.drawBitmap(BitmapList[Index], X, Y, null);       
*/     
	 X = (int)(X * XScale + XShift); Y = (int)(Y * YScale + YShift); 
	 int width = BitmapList[Index].getWidth(), height = BitmapList[Index].getHeight(); 
	 int DestWidth = (int)(width*XScale), DestHeight = (int)(height*YScale);
	 
	 DestWidth = (int)(DestWidth / ImageScaleRelative_320x480[Index]);
	 DestHeight = (int)(DestHeight / ImageScaleRelative_320x480[Index]);	 

	  PaintObjAlpha.setAlpha(255);
      CanvasObj.drawBitmap(BitmapList[Index], new Rect(0,0,width,height),
		  new Rect(X, Y, X+DestWidth, Y+DestHeight), PaintObjAlpha ); 	
  }
//---------------------------------------------------------------------------------------  
  public void DrawImageAlpha( int Index, int X, int Y, int AlphaValue )
  {
/*	  
	     PaintObjAlpha.setAlpha(AlphaValue);
		 X = (int)(X * XScale + XShift); Y = (int)(Y * YScale + YShift);
	     CanvasObj.drawBitmap(BitmapList[Index], X, Y, PaintObjAlpha ); 	     
*/	      
	 X = (int)(X * XScale + XShift); Y = (int)(Y * YScale + YShift); 
	 int width = BitmapList[Index].getWidth(), height = BitmapList[Index].getHeight(); 
	 int DestWidth = (int)(width*XScale), DestHeight = (int)(height*YScale);	
	 
	 DestWidth = (int)(DestWidth / ImageScaleRelative_320x480[Index]);
	 DestHeight = (int)(DestHeight / ImageScaleRelative_320x480[Index]);	 

	  PaintObjAlpha.setAlpha(AlphaValue);
      CanvasObj.drawBitmap(BitmapList[Index], new Rect(0,0,width,height),
		  new Rect(X, Y, X+DestWidth, Y+DestHeight), PaintObjAlpha ); 	 
  }
//--------------------------------------------------------------------------------------- 
  public void DrawSubImage( int Index, int X, int Y, int AlphaValue, int SrcX, int SrcY, int SrcWidth, int SrcHeight )
  {
		 X = (int)(X * XScale + XShift); Y = (int)(Y * YScale + YShift);
		 int DestWidth = (int)((SrcWidth*XScale) / ImageScaleRelative_320x480[Index]);
		 int DestHeight = (int)((SrcHeight*YScale) / ImageScaleRelative_320x480[Index]);
		 
	     int SrcX2 = SrcX+SrcWidth;
	     int SrcY2 = SrcY+SrcHeight;
	     
	     if( AlphaValue != 255 )
	     {
	        PaintObjAlpha.setAlpha(AlphaValue);
	        //PaintObjAlpha.setFlags(Paint.FILTER_BITMAP_FLAG | Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG );
		    CanvasObj.drawBitmap(BitmapList[Index], new Rect(SrcX,SrcY,SrcX2,SrcY2),
		      new Rect(X, Y, X+DestWidth, Y+DestHeight), PaintObjAlpha ); 	         
	     }
	     else
	     {
	     //PaintObjAlpha.setAlpha(255);
	     //PaintObjAlpha.setFlags(Paint.FILTER_BITMAP_FLAG | Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG );
	     CanvasObj.drawBitmap(BitmapList[Index], new Rect(SrcX,SrcY,SrcX2,SrcY2),
	    		 new Rect(X, Y, X+DestWidth, Y+DestHeight), /*PaintObjAlpha*/ null ); 	   
	     }   		 
  }  
//---------------------------------------------------------------------------------------   
  public void DrawSubImage( int Index, int X, int Y, int AlphaValue, int SrcX, int SrcY, int SrcWidth, int SrcHeight,
		  int nScaleXPercent, int nScaleYPercent )
  {
		 X = (int)(X * XScale + XShift); Y = (int)(Y * YScale + YShift);
		 int DestWidth = (int)((SrcWidth*XScale) / ImageScaleRelative_320x480[Index]);
		 int DestHeight = (int)((SrcHeight*YScale) / ImageScaleRelative_320x480[Index]);
		 
		 DestWidth *= nScaleXPercent / 100.0f;
		 DestHeight *= nScaleYPercent / 100.0f;
		 
	     int SrcX2 = SrcX+SrcWidth;
	     int SrcY2 = SrcY+SrcHeight;
	     
	     if( AlphaValue != 255 )
	     {
	        PaintObjAlpha.setAlpha(AlphaValue);
	        //PaintObjAlpha.setFlags(Paint.FILTER_BITMAP_FLAG | Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG );
		    CanvasObj.drawBitmap(BitmapList[Index], new Rect(SrcX,SrcY,SrcX2,SrcY2),
		      new Rect(X, Y, X+DestWidth, Y+DestHeight), PaintObjAlpha ); 	         
	     }
	     else
	     {
	     //PaintObjAlpha.setAlpha(255);
	     //PaintObjAlpha.setFlags(Paint.FILTER_BITMAP_FLAG | Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG );
	     CanvasObj.drawBitmap(BitmapList[Index], new Rect(SrcX,SrcY,SrcX2,SrcY2),
	    		 new Rect(X, Y, X+DestWidth, Y+DestHeight), /*PaintObjAlpha*/ null ); 	   
	     }   		 
  }  
//---------------------------------------------------------------------------------------     
  public void DrawText( String str, int X, int Y )
  {	  
	  X = (int)(X * XScale + XShift); Y = (int)(Y * YScale + YShift);
	 CanvasObj.drawText(str, X, Y, PaintObj);  
  }
//---------------------------------------------------------------------------------------
  public void DrawTextB( String str, int X, int Y, int Color, int TextSize )
  {	 
	  X = (int)(X * XScale + XShift); Y = (int)(Y * YScale + YShift);
	 PaintObj.setColor(Color); 
	 PaintObj.setTextSize(TextSize);
	 CanvasObj.drawText(str, X, Y, PaintObj);  
  }
//---------------------------------------------------------------------------------------
  public void DrawPrimitive( int PrimitiveType, float p1, float p2, float p3, float p4, int Style,
		  int cColor )
  {
	  PaintObjPrimitive.setColor(cColor);
	  	  
	  switch( Style )
	  {
	   case GameEngine.G_STYLE_FILL:
  	    PaintObjPrimitive.setStyle(Paint.Style.FILL);
  	    break;
	   case GameEngine.G_STYLE_FILL_AND_STROKE:  
	    PaintObjPrimitive.setStyle(Paint.Style.FILL_AND_STROKE);
	    break;
 	   case GameEngine.G_STYLE_STROKE:
	    PaintObjPrimitive.setStyle(Paint.Style.STROKE);
	    break;
	   default:
		break;
	  }
	  
	 switch( PrimitiveType )
	 {
  	   case GameEngine.G_DRAW_POINT:
  		 p1 = p1 * XScale + XShift; p2 = p2 * YScale + YShift;
  		 CanvasObj.drawPoint( p1, p2, PaintObjPrimitive );
  		 break;
  	   case GameEngine.G_DRAW_LINE:
  		 p1 = p1 * XScale + XShift; p2 = p2 * YScale + YShift;
  		 p3 = p3 * XScale + XShift; p4 = p4 * YScale + YShift;
  		 CanvasObj.drawLine( p1, p2, p3, p4, PaintObjPrimitive );
  		 break;
  	   case GameEngine.G_DRAW_BOX:
    	 p1 = p1 * XScale + XShift; p2 = p2 * YScale + YShift;
      	 p3 = p3 * XScale + XShift; p4 = p4 * YScale + YShift;  
  		 CanvasObj.drawRect( p1, p2, p3, p4, PaintObjPrimitive );
  		 break;
  	   case GameEngine.G_DRAW_CIRCLE:  
  		 p1 = p1 * XScale + XShift; p2 = p2 * YScale + YShift;
  		 p3 *= (YScale + XScale)/2;  		 
  		 CanvasObj.drawCircle( p1, p2, p3, PaintObjPrimitive );
  		 break;
  	   default:
  		 break;
	 }
  }
//---------------------------------------------------------------------------------------
  public void DrawImageTextPrimitiveLists( int ImageIndex[], int ImageX[], int ImageY[], int ImageAlpha[],
	      int ImageZOrder[], int ImageIndexListLength,  
	      String TextList[], int TextX[], int TextY[], int TextZOrder[], int TextListLength,
	      int[] TextColor, int[] TextSize,
	      int[] PrimitiveType, float[] Primitive1, float[] Primitive2, float[] Primitive3, float[] Primitive4,
	      int PrimitiveCount, int[] PrimitiveStyle, int[] PrimitiveColor, int[] PrimitiveZOrder,
	      int[] ImageSubData )
	  {

	     int i, k, m;
	     for( i = 0; i <= MAX_Z_ORDER; i++ )
	     {
	        for( k = 0; k < ImageIndexListLength; k++ )
	          if( ImageZOrder[k] == i )
	          {
	            if( ImageSubData[k * GameEngine.IMAGE_SUB_DATA_LENGTH] == 1)
	            { 
	              m = k * GameEngine.IMAGE_SUB_DATA_LENGTH;              
	              DrawSubImage( ImageIndex[k], ImageX[k], ImageY[k], ImageAlpha[k], ImageSubData[m+1], ImageSubData[m+2], 
	            		  ImageSubData[m+3], ImageSubData[m+4], ImageSubData[m+5], ImageSubData[m+6] );
	            }
	            else
	        	if( ImageAlpha[k] >= 255 )
	               DrawImage( ImageIndex[k], ImageX[k], ImageY[k] );
	        	else
	        	   DrawImageAlpha( ImageIndex[k], ImageX[k], ImageY[k], ImageAlpha[k] );
	          }
	        
	        for( k = 0; k < TextListLength; k++ )
	          if( TextZOrder[k] == i )            
	          {
	            if(TextSize[k] <= 0 )
	               DrawText( TextList[k], TextX[k], TextY[k] );  
	            else
	               DrawTextB( TextList[k], TextX[k], TextY[k], TextColor[k], TextSize[k] );             
	          }
	        
	        for( k = 0; k < PrimitiveCount; k++ )
	          if( PrimitiveZOrder[k] == i )            
	            DrawPrimitive( PrimitiveType[k], Primitive1[k], Primitive2[k], Primitive3[k], Primitive4[k], 
	              PrimitiveStyle[k], PrimitiveColor[k] );   		  
	     }
	  }  
//---------------------------------------------------------------------------------------  
  public void PlaySound( int index )
  {	 
	 JukeBox.CurrentObj.PlaySound(index);
  }
//---------------------------------------------------------------------------------------
  public void ProcessKeyUpDownRealTime()
  { 
  }
//---------------------------------------------------------------------------------------  
  public void ProcessKeyUpDownOnDraw()
  {	  
      SendEvent( G_EVENT_KEY );
      KeyCode = KeyEventStatus = ME_NOTHING;
  }
//---------------------------------------------------------------------------------------   
  public void ProcessOnTouchRealTime()
  {	  
  }
//---------------------------------------------------------------------------------------  
  public void ProcessOnTouchOnDraw()
  {	  
      SendEvent( G_EVENT_TOUCH );
      TouchAction = TouchX = TouchY = ME_NOTHING;
  }
//---------------------------------------------------------------------------------------    
  public void SaveFile( String filename, String str )
  {
    FileWorker.Write( filename, str );	  
  }
//---------------------------------------------------------------------------------------
  public String ReadFile( String filename, int MaxLength )
  {	  
	 return FileWorker.Read( filename, MaxLength );
  }  
//---------------------------------------------------------------------------------------  
  public void ProcessGameEngineRequest( int GRequest )
  {
/*
   called directly by the game engine
*/  	  
  switch( GRequest )
  {
   case G_REQUEST_DRAW_IMAGE_TEXT_PRIMITIVE:
	  DoDrawRequestNext_onDraw_Flag = true;
	  break;
   case G_REQUEST_PLAY_SOUND:
	  PlaySound(GameEngine.SoundPlayIndex);
	  break;  
   case G_REQUEST_SET_PAINT_OPTIONS:
	  SetPaintOptions(GameEngine.PaintColor, GameEngine.PaintTextSize, GameEngine.StrokeWidth);
	  break;  
   case G_REQUEST_WRITE_FILE:
	  SaveFile( GameEngine.FileName, GameEngine.FileDataStr ); 
	  break;
   case G_REQUEST_READ_FILE:
	   GameEngine.FileDataStr = new String(ReadFile( GameEngine.FileName, GameEngine.MaxFileReadLength )); 
	  break;
   case G_REQUEST_SET_SCREEN_ADJUSTMENTS:
	   SetScreenAdjustments( GameEngine.InnerScreenWidth, GameEngine.InnerScreenHeight, 
		GameEngine.Xscale,GameEngine.Yscale, GameEngine.RescaleFlag );	   
	  break;
   default:
      break;
  };
  }
//---------------------------------------------------------------------------------------
  public void SendEvent( int GEventId )
  {
    switch( GEventId )
    {
      case G_EVENT_KEY:
        GameEngine.KeyCode = KeyCode;
        GameEngine.KeyEventStatus = KeyEventStatus;       
        break;
      case G_EVENT_TOUCH:
        GameEngine.TouchAction = TouchAction;
        GameEngine.TouchX = (int)((TouchX - XShift) * 1/XScale + 0.0f);
        GameEngine.TouchY = (int)((TouchY - YShift) * 1/YScale + 0.0f);
        break;
      case G_EVENT_SCREEN_SIZE_CHANGE:
        GameEngine.ScreenWidth = ScreenWidth;
        GameEngine.ScreenHeight = ScreenHeight;
        break;
      case G_EVENT_PROGRAM_CLOSING:
        break;
      case G_EVENT_PROGRAM_STARTED:
        break;
      default:
        break;
    }

   GameEngine.CurrentObj.NotifyEvent( GEventId ); 
  }
//----------------------------------------------------------------------------------------
public static void ShutDown()
{
   CurrentObj.MainActivityObj.finish();
   System.exit(0);	
}
//----------------------------------------------------------------------------------------
}
//=======================================================================================
//#######################################################################################
//=======================================================================================
class GameView extends SurfaceView
{
    private static SurfaceHolder holder;
    public static GameLoopThread GameLoopThread = null;
    private int ViewWidth, ViewHeight;
    
    public final static int ME_PRESS_DOWN = 0;
    public final static int ME_RELEASE = 1;
    public final static int ME_MOVE = 2;

	public final static int ME_KEY_DOWN = 3;
	public final static int ME_KEY_UP = 4;
	
//---------------------------------------------------------------------------------------        
    public GameView(Context context, GameLoopThread methread) 
    {        
          super(context);
          
          //GameLoopThread = new GameLoopThread(this);
          GameLoopThread = methread;
          holder = getHolder();
                  		  
          holder.addCallback(new SurfaceHolder.Callback() 
          {        	  
       	  
        	  //@Override
              public void surfaceDestroyed(SurfaceHolder holder)
              {
/*            	              	  
                     boolean retry = true;                     
                     GameLoopThread.SetRunning(false);                                                               
          
                     while (retry) 
                     {
                            try 
                            {
                                  GameLoopThread.join();
                                  retry = false;
                            } 
                            catch (InterruptedException e) 
                            {
                            }
                     }                       
*/                                                             
                //GameControl.CurrentObj.SendEvent(GameControl.G_EVENT_PROGRAM_CLOSING);    
                //GameControl.CurrentObj.MainActivityObj.finish();
                //GameControl.ShutDown();
                
              }

              //@Override
              public void surfaceCreated(SurfaceHolder holder) 
              {
                     GameLoopThread.SetRunning(true);
                     GameLoopThread.start();  
                     GameControl.CurrentObj.SendEvent(GameControl.G_EVENT_PROGRAM_STARTED);
              }
              
              //@Override
              public void surfaceChanged(SurfaceHolder holder, int format,
                            int width, int height) 
              {
              }

       });                   

    }
//---------------------------------------------------------------------------------------      
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
    	if( ViewWidth >= w || ViewHeight > h )
    	    return;
    	    	
    	ViewWidth = w; ViewHeight = h;
    	GameControl.CurrentObj.SetScreenSize(w, h);        
    	GameControl.CurrentObj.SendEvent(GameControl.G_EVENT_SCREEN_SIZE_CHANGE);  
        GameControl.CurrentObj.LoadSounds();
        GameControl.CurrentObj.LoadImages();
        //GameEngine.dstr = "WH: " + w + ", " + h + "; lalala;";
    }   
//---------------------------------------------------------------------------------------    
    @Override
    public boolean onTouchEvent (MotionEvent event)    
    {    	       
       GameControl.CurrentObj.TouchAction = event.getAction();
       GameControl.CurrentObj.TouchXf = event.getX();       
       GameControl.CurrentObj.TouchYf = event.getY();
       GameControl.CurrentObj.TouchX = (int)GameControl.CurrentObj.TouchXf;
       GameControl.CurrentObj.TouchY = (int)GameControl.CurrentObj.TouchYf;
/*       
       GameGlobals.dummystr = new String( "TOUCH: " + 
          GameControl.CurrentObj.TouchAction + ", " +
          GameControl.CurrentObj.TouchX  + ", " +
          GameControl.CurrentObj.TouchY );
*/         
       return true;       
    }
//---------------------------------------------------------------------------------------   
    @Override
 public boolean onKeyDown(int keyCode, KeyEvent event) 
 {  
    GameControl.CurrentObj.KeyCode = keyCode;
    GameControl.CurrentObj.KeyEventStatus = ME_KEY_DOWN;  
    
    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)     
    {
    	 if( GameEngine.CurrentObj.TheControlManager.DoBackKey() == 1 )
    	 {
    	   GameControl.CurrentObj.SendEvent(GameControl.G_EVENT_PROGRAM_CLOSING); 
    	   //GameControl.CurrentObj.MainActivityObj.finish();
    	   GameControl.ShutDown();
    	 }
        return true;
    }
    
    return false;
  }
//---------------------------------------------------------------------------------------
 @Override
 public boolean onKeyUp (int keyCode, KeyEvent event)
 {
    GameControl.CurrentObj.KeyCode = keyCode;
	GameControl.CurrentObj.KeyEventStatus = ME_KEY_UP;
    return false;	 
 } 
//---------------------------------------------------------------------------------------    
 @Override
 protected void onDraw(Canvas canvas) 
 {
    if( canvas == null )
        return;
   
	 GameControl.CurrentObj.SetCanvas( canvas ); 
	 
	 if( GameControl.CurrentObj.TouchAction >= 0 )
	     GameControl.CurrentObj.ProcessOnTouchOnDraw();
	 if( GameControl.CurrentObj.KeyCode >= 0 )
   	     GameControl.CurrentObj.ProcessKeyUpDownOnDraw();
	 
	 canvas.drawColor(Color.rgb(0,0,0));  
     
	 GameEngine.CurrentObj.MainLoop();
	 
	 if( GameControl.CurrentObj.DoDrawRequestNext_onDraw_Flag == true )
	 {
      GameControl.CurrentObj.DrawImageTextPrimitiveLists( GameEngine.CurrentObj.ImageIndexList, 
        GameEngine.CurrentObj.ImageX, GameEngine.CurrentObj.ImageY,  GameEngine.CurrentObj.ImageAlpha,
        GameEngine.CurrentObj.ImageZOrder, GameEngine.CurrentObj.ImageIndexListLength,  
        GameEngine.CurrentObj.TextOutList, GameEngine.CurrentObj.TextX, GameEngine.CurrentObj.TextY, 
        GameEngine.CurrentObj.TextZOrder, GameEngine.CurrentObj.TextOutListLength,
        GameEngine.CurrentObj.TextColor, GameEngine.CurrentObj.TextSize,
        GameEngine.CurrentObj.PrimitiveType, GameEngine.CurrentObj.Primitive1, GameEngine.CurrentObj.Primitive2,
        GameEngine.CurrentObj.Primitive3, GameEngine.CurrentObj.Primitive4, GameEngine.CurrentObj.PrimitiveCount,
        GameEngine.CurrentObj.PrimitiveStyle, GameEngine.CurrentObj.PrimitiveColor, 
        GameEngine.CurrentObj.PrimitiveZOrder,
        GameEngine.CurrentObj.ImageSubData );	

      /*
      int[] TextColor, int[] TextSize,
      int[] PrimitiveType, float[] Primitive1, float[] Primitive2, float[] Primitive3, float[] Primitive4,
      int PrimitiveCount, int[] PrimitiveStyle, int[] PrimitiveColor, int[] PrimitiveZOrder )       
       */
      
        GameEngine.CurrentObj.TextOutListLength = 0;
        GameEngine.CurrentObj.ImageIndexListLength = 0;
        GameEngine.CurrentObj.PrimitiveCount = 0;
        GameControl.CurrentObj.DoDrawRequestNext_onDraw_Flag = false;
	 }
	 
 }
//---------------------------------------------------------------------------------------
}
//=======================================================================================
//#######################################################################################
//=======================================================================================
