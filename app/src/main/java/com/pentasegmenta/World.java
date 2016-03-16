package com.pentasegmenta;

import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
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

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
class GameGlobals
{
	/* On/Off switches */
	public static int TVModeFlag = 0; 
	public static int PlayFieldHeightIncrease = 0; //2;
	
	public static final int UNIQUE_ID_NONE = 0;
	public static final int GROUP_ID_NONE = 0;
	
	/* Add constants for group ids */
	public static final int GROUP_ID_MENU_A = 201;
	public static final int GROUP_ID_MENU_B = 202;
	public static final int GROUP_ID_MENU_C = 203;
	public static final int GROUP_ID_MENU_C2 = 204;
	public static final int GROUP_ID_MENU_D = 205;
	public static final int GROUP_ID_MENU_E = 206;
	public static final int GROUP_ID_MENU_F = 207;
	public static final int GROUP_ID_MENU_G = 208;
	public static final int GROUP_ID_MENU_H = 209;
	public static final int GROUP_ID_MENU_I = 210;
	
	/* Add constants for unique ids */	
	public static final int UNIQUE_ID_BUTTON_START = 101;

	public static final int UNIQUE_ID_BUTTON_OPTIONS = 102;
	public static final int UNIQUE_ID_BUTTON_HIGH_SCORES = 103;
	
	public static final int UNIQUE_ID_CHECKBOX_OPTION_SOUNDS = 113;

	public static final int UNIQUE_ID_BUTTON_OPTION_OKAY = 116;	
	public static final int UNIQUE_ID_CAPTION_HIGH_SCORES_LIST = 117;
	public static final int UNIQUE_ID_BUTTON_HIGH_SCORES_OKAY = 118;
	public static final int UNIQUE_ID_BUTTON_HIGH_SCORES_LEFT = 119;
	public static final int UNIQUE_ID_BUTTON_HIGH_SCORES_RIGHT = 120;
    public static final int UNIQUE_ID_CAPTION_GAME_OVER_NAME = 121;
	public static final int UNIQUE_ID_BUTTON_GAME_OVER_LEFT = 122;
    public static final int UNIQUE_ID_BUTTON_GAME_OVER_DOWN = 123;
    public static final int UNIQUE_ID_BUTTON_GAME_OVER_UP = 124;
    public static final int UNIQUE_ID_BUTTON_GAME_OVER_RIGHT = 125;
    public static final int UNIQUE_ID_BUTTON_GAME_OVER_OKAY = 126;
    public static final int UNIQUE_ID_BUTTON_GAME_OVER_OKAY_ADD_SCORE = 127;
    
    public static final int UNIQUE_ID_CAPTION_MAIN_TITLE = 128;
    public static final int UNIQUE_ID_CAPTION_GENERAL = 129;
    
    public static final int UNIQUE_ID_BUTTON_ABOUT_OKAY = 153;
    public static final int UNIQUE_ID_BUTTON_ABOUT = 154;
	
	public static final int Y_SHIFT = 0; //-48;
	
	public static final int UNIQUE_ID_CAPTION_OPTION_AREA_SELECTION = 2001;
	public static final int UNIQUE_ID_CAPTION_OPTION_SHIP_SELECTION = 2002;
	public static final int UNIQUE_ID_MULTISTATE_BUTTON_OPTION_AREA_SELECTION = 2003;
	public static final int UNIQUE_ID_MULTISTATE_BUTTON_OPTION_SHIP_SELECTION = 2004;
    public static final int UNIQUE_ID_CAPTION_OPTION_TILES_SELECTION = 2005;
    public static final int UNIQUE_ID_MULTISTATE_BUTTON_OPTION_TILES_SELECTION = 2006;
    
    public static SimpleBackGround BackDrop = null;
    
    /* for debugging purposes */
    public static Caption debugcaption;    
    
  
    // * * * * * * * * * * * * * * * 
    
    public static GameEngine GE;       
    
    protected static Random rand = new Random();
    
    public static int HeaderFontSize1 = 25;
    public static int HeaderFontSize2 = 30;
    public static int RegularFontSize1 = 20;
    
//-------------------------------------------------------------------------------------
    public GameGlobals()
    {
    	int i;  	     
      	   
      debugcaption = new Caption(10, 20, "");
      debugcaption.SetLayers( GameEngine.CurrentObj.LAYER_3, GameEngine.CurrentObj.LAYER_2 );
      debugcaption.SetTextOptions( 10, Color.rgb(255,255,255), 0, 20, true, 20 );   
      
      HighScores.PentaSegmenta_Init();
  
      if( CheckVersionCode() != 0 )
      {
        HighScores.Clear();
        GameOptions.Clear();
      }
      else
      {
        HighScores.CurrentObj.LoadHighScores();
        GameOptions.Load();
      }
    }
//-------------------------------------------------------------------------------------
public static int random( int lower, int upper )
{
   return rand.nextInt(upper - lower + 1) + lower;
}
//--------------------------------------------------------------------------    
public static void ChangeBackGround()
{
  int Digit1 = 0;
  int Digit0 = 0;
	
  BackDrop.SetTileStyle( GameControl.BACKGROUND_330x490_0 + GameOptions.BackGroundIndex, Digit1, Digit0, 1, 1, 
	 SimpleBackGround.BACKGROUND_TILE_WIDTH_PIXELS, SimpleBackGround.BACKGROUND_TILE_HEIGHT_PIXELS, 255 );
}
//-------------------------------------------------------------------------------------    
public static void DrawTileImageOne( int ImageIndex, int X, int Y, int Z, int AlphaValue, int CellX, int CellY,
	    	int nCellWidthPixels, int nCellHeightPixels)
{
	      GE.DrawSubImage( ImageIndex, X, Y, Z, AlphaValue, CellX * (nCellWidthPixels + 1) + 1, 
	    		  CellY * (nCellHeightPixels + 1) + 1, nCellWidthPixels-1, nCellHeightPixels-1 );      
/*
 0123456789
0##########
1#  #  #  #
2#  #  #  #
3##########  
4#  #  #  #
5#  #  #  #
6########## 
7#  #  #  #
8#  #  #  #
9##########
 
(w + 1) * x + 1 
(h + 1) * y + 1
*/
}	    
//-------------------------------------------------------------------------------------
public static void DrawTileImageOne( int ImageIndex, int X, int Y, int Z, int AlphaValue, int CellX, int CellY,
	    	int nCellWidthPixels, int nCellHeightPixels, int nXScalePercent, int nYScalePercent )
{
	      GE.DrawSubImage( ImageIndex, X, Y, Z, AlphaValue, CellX * (nCellWidthPixels + 1) + 1, 
	    		  CellY * (nCellHeightPixels + 1) + 1, nCellWidthPixels-1, nCellHeightPixels-1,
	    		  nXScalePercent, nYScalePercent);  
}	    
//-------------------------------------------------------------------------------------  
public static void DrawTileImageGeneralUltra( int ImageIndex, int X, int Y, int Z, int AlphaValue, int CellX, int CellY,
	    		int Width_Cells, int Height_Cells, int TileWidthPixels, int TileHeightPixels )
{
	    	int TileWidthPixelsEnlarged = TileWidthPixels + 2;
	    	int TileHeightPixelsEnlarged = TileHeightPixels + 2; 	
	         
	        GE.DrawSubImage( ImageIndex, X, Y, Z, AlphaValue, CellX * (TileWidthPixelsEnlarged + 2) + 1, 
	      		  CellY * (TileHeightPixelsEnlarged + 2) + 1,
	      		  TileWidthPixels * Width_Cells, 
	      		  TileHeightPixels * Height_Cells );
} 
//------------------------------------------------------------------------------------- 	
public static String repeat(String str, int times)
{
   StringBuilder ret = new StringBuilder();
   for(int i = 0;i < times;i++) 
	   ret.append(str);
   return ret.toString();
}
//-------------------------------------------------------------------------------------
public static int CheckVersionCode()
{
   int version_code = PentaSegmentaActivity.CurrentObj.GetVersionCode();
   int saved_version_code = 0, return_value = -1;
   
   GameEngine ge = GameEngine.CurrentObj;
   
   ge.FileName = "version.txt";
   ge.DoRequest(ge.G_REQUEST_READ_FILE);	
   String str = ge.FileDataStr;
   
   ge.FileName = "version.txt";
   ge.FileDataStr = version_code + ", ";
   ge.DoRequest(ge.G_REQUEST_WRITE_FILE);	   
  
   if( str.length() < 1 )
	   return return_value;
   
   String[] list = str.split(",");
   saved_version_code = Integer.parseInt(list[0]);
   
   if( list.length < 1 )
	    return return_value;
   
   if( saved_version_code > version_code )
	   return_value = -1;
   else
   if( saved_version_code == version_code )
	   return_value = 0;  
   else
   if( saved_version_code < version_code )
	   return_value = 1; 

   return return_value;
}
//------------------------------------------------------------------------------------
public static String SetMinTextWidthLeft( String str, String pad_char, int min_width )
{
  int i, difference, length = str.length();

  if( length < min_width )
  {
    StringBuilder strb = new StringBuilder();
    difference = min_width - length;

    for( i = 0; i < difference; i++ )
        strb.append(pad_char);
    
    strb.append(str);
    return strb.toString();
  }
  else
    return str;
}
//-------------------------------------------------------------------------------------
public static void CalculateFontSize()
{
   HeaderFontSize1 = 25 * GameEngine.ScreenWidth/480;
   HeaderFontSize2 = 30 * GameEngine.ScreenWidth/480;
   RegularFontSize1 = 20 * GameEngine.ScreenWidth/480;
}
//-------------------------------------------------------------------------------------
static int PowerRandom( int lowerbound, int upperbound )
{
  float rnd = (float)Math.pow( 10, 2 * Math.random() )/100;

  return (int)(Math.floor((upperbound - lowerbound + 1) * rnd )) + lowerbound;
}
//-------------------------------------------------------------------------------------
static void PlaySound( int sound_index )
{
	if( GameOptions.SoundsFlag >= 1 )
		GameEngine.PlaySound(sound_index);
}
//-------------------------------------------------------------------------------------
}
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
class HighScores
{
public final static int LIST_LENGTH = 10;
public final static int MAX_NAME_LENGTH = 15;
public final static String FileName = "highscores.txt";

int[][] ScoreList;
String[][] NameList;
String[] ListTypeNames;

protected int ListTypeCount;
public int ListTypeIndex = 0;

public static HighScores CurrentObj = null;

//-------------------------------------------------------------------------------------
static public HighScores PentaSegmenta_Init()
{
   String[] typenames = { "Area 0", "Area 1", "Area 2", "Area 3", "Area 4",
		       "Area 5", "Area 6", "Area 7", "Area 8", "Area 9"};
   
   CurrentObj = new HighScores( 10, typenames );
   
   return CurrentObj;	
}
//-------------------------------------------------------------------------------------
public HighScores( int nListTypeCount, String[] sListTypeNames )
{
  ListTypeCount = nListTypeCount;
  ScoreList = new int[LIST_LENGTH][ListTypeCount];
  NameList = new String[LIST_LENGTH][ListTypeCount];
  ListTypeNames = new String[ListTypeCount];

  String[] firstnames = {"Aiden","Daniel","Matthew","Elijah","Anthony","James","Benjamin","Andrew","David","Logan",
"Christopher","Gabriel","Lucas","Isaac","Caleb","Christian","Jonathan","Isaiah","Brandon","Dylan","Landon","Tyler",
"Nicholas","Hunter","Colton","Zachary","Kevin","Gavin","Sophia","Emma","Isabella","Olivia","Ava","Emily","Abigail",
"Amelia","Harper","Lillian","Samantha","Evelyn","Victoria","Brooklyn","Zoe","Layla","Hailey","Leah",
"Kaylee" };
  
String[] lastnames = { "A.", "B.", "C.", "D.", "E.", "F.", "G.", "H.", "I.", "J.", "K.", "L.", "M.", "N.",
		   "O.", "P.", "Q.", "R.", "S.", "T.", "U.", "V.", "W.", "X.", "Y.", "Z." };
  
int i, h;
  String str;
  
  for( h = 0; h < ListTypeCount; h++ )
  {
   for( i = 0; i < LIST_LENGTH; i++ )
   {
     ScoreList[i][h] = (int)(LIST_LENGTH * 200 - i * 100);
     str = firstnames[ GameGlobals.random(0,firstnames.length - 1)] + " " + 
         lastnames[ GameGlobals.random(0,lastnames.length - 1)] ;
     NameList[i][h] = AddSpacesLeft(str, MAX_NAME_LENGTH );
   }
   ListTypeNames[h] = new String(sListTypeNames[h]);
  }
}
//-------------------------------------------------------------------------------------
static public String AddSpacesLeft( String str, int minlength )
{
 String space_str = " ";
 int spaces_add = minlength - str.length();

 if( spaces_add >= 1 )
 {    
    str = str + GameGlobals.repeat(space_str, spaces_add);
 }
    return str;
}
//-------------------------------------------------------------------------------------
public int CheckNewHighScore( int nListTypeIndex, int Score )
{
  int i, returnvalue = 0;

  for( i = 0; i < LIST_LENGTH; i++ )
    if( Score > ScoreList[i][nListTypeIndex] )
    {
      returnvalue = 1;
      break;
    }

  return returnvalue;
}
//-------------------------------------------------------------------------------------
public static void Highest2Lowest( String[] sList, int[] List )
{
  int x, y, z;
  String str;
  
  for( y = 0; y < List.length; y++ )
   for( x = 0; x < List.length; x++ )
    if( List[x] < List[y] )
    {
       z = List[x];
       List[x] = List[y];
       List[y] = z;
       str = sList[x];
       sList[x] = sList[y];
       sList[y] = str;
    }
}    
//-------------------------------------------------------------------------------------
public void Add2HighScore( int nListType, String name, int Score )
{
  ScoreList[LIST_LENGTH - 1][nListType] = Score;
  NameList[LIST_LENGTH - 1][nListType] = name; 
  
  int[] ScoreListCopy = new int[LIST_LENGTH];
  String[] NameListCopy = new String[LIST_LENGTH];
  
  int i;
  
  for( i = 0; i < LIST_LENGTH; i++ )
  {
    ScoreListCopy[i] = ScoreList[i][nListType];
    NameListCopy[i] = NameList[i][nListType];
  }
  
  Highest2Lowest( NameListCopy, ScoreListCopy );
  
  for( i = 0; i < LIST_LENGTH; i++ )
  {
    ScoreList[i][nListType] = ScoreListCopy[i];
    NameList[i][nListType] = NameListCopy[i];
  }  
}
//-------------------------------------------------------------------------------------
public void SaveHighScores()
{
  GameEngine ge = GameEngine.CurrentObj;
  String str;
  ge.FileName = FileName;
  
  int i, h;
  
  str = ListTypeCount + ",";
  
  for( h = 0; h < ListTypeCount; h++ )
  {
	str += ListTypeNames[h] + ",";
	for( i = 0; i < LIST_LENGTH; i++ )
	  str += NameList[i][h] + "," + ScoreList[i][h] + ",";
  }	   
  
  ge.FileDataStr = str;
  ge.DoRequest(ge.G_REQUEST_WRITE_FILE);
}
//-------------------------------------------------------------------------------------
public void LoadHighScores()
{
  GameEngine ge = GameEngine.CurrentObj;
    
  ge.FileName = FileName;
  ge.DoRequest(ge.G_REQUEST_READ_FILE);	
  String str = ge.FileDataStr;
  
  if( str == null )
	  return;
  
  if( str.length() < 1 )
	  return;
 
  int h, i, index = 0;
  String[] list = str.split(",");
  
  ListTypeCount = Integer.parseInt(list[index]);
  ScoreList = new int[LIST_LENGTH][ListTypeCount];
  NameList = new String[LIST_LENGTH][ListTypeCount];
  ListTypeNames = new String[ListTypeCount];
  
  index++;
  
  for( h = 0; h < ListTypeCount; h++ )
  {
	ListTypeNames[h] = list[index];
	index++;
	
	for( i = 0; i < LIST_LENGTH; i++ )
	{
	  NameList[i][h] = list[index];
	  index++;
      ScoreList[i][h] = Integer.parseInt(list[index]);
      index++;
	}
  }	     
}
//-------------------------------------------------------------------------------------
public void ChangeListTypeIndex( int IndexDirection )
{
   ListTypeIndex += IndexDirection;
   
   if( ListTypeIndex >= ListTypeCount )
	   ListTypeIndex = 0;
   if( ListTypeIndex < 0 )
	   ListTypeIndex = ListTypeCount - 1;
}
//-------------------------------------------------------------------------------------
public String CurrentList2String()
{
  int i;
  
  String str = ListTypeNames[ListTypeIndex] + "\n";
  
  for( i = 0; i < LIST_LENGTH; i++ )
	str += NameList[i][ListTypeIndex] + "  " + ScoreList[i][ListTypeIndex] + "\n";
    
  return str;
}
//-------------------------------------------------------------------------------------
public static void Clear()
{
  GameEngine ge = GameEngine.CurrentObj;
  ge.FileName = FileName;
  ge.FileDataStr = "";
  ge.DoRequest(ge.G_REQUEST_WRITE_FILE);	
}
//-------------------------------------------------------------------------------------
public int GetHighestScore( int nListTypeIndex )
{
   return ScoreList[0][nListTypeIndex];
}
//-------------------------------------------------------------------------------------
}
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
class GameOptions
{

public static int SoundsFlag = 1;
public static int BackGroundIndex = 0;

public final static int MIN_SHIP_SELECTION_INDEX = 0, MAX_SHIP_SELECTION_INDEX = 9;
public final static int MIN_AREA_SELECTION_INDEX = 0, MAX_AREA_SELECTION_INDEX = 9;
public static int ShipSelectionIndex = 0;
public static int AreaSelectionIndex = 0;
public static int TilesSelectionIndex = 0;

public static String CurrentPlayerName = GameGlobals.repeat("-", HighScores.MAX_NAME_LENGTH ); 
public static int CurrentPlayerNameCursorPosition = 0;
public static int CurrentPlayerScore = 0;
public static int MainFontColor = Color.rgb( 255, 255, 0 );
public static int MainFontShadowColor = Color.rgb( 20, 20, 20 );

public final static String FileName = "options.txt";

//-------------------------------------------------------------------------------------
public static void Save()
{
  GameEngine ge = GameEngine.CurrentObj;  
  ge.FileName = FileName;	
  ge.FileDataStr = ShipSelectionIndex + "," + AreaSelectionIndex + "," + TilesSelectionIndex + "," + SoundsFlag + ", ";
  
  ge.DoRequest(ge.G_REQUEST_WRITE_FILE);
}
//-------------------------------------------------------------------------------------
public static void Load()
{

  GameEngine ge = GameEngine.CurrentObj; 
  ge.FileName = FileName;
  ge.DoRequest(ge.G_REQUEST_READ_FILE);	
 
  String str = ge.FileDataStr;  

  if( str.length() <= 1 )
	  return;

  String[] list = str.split(",");
 
  if( list.length < 3 )
	  return;	

  ShipSelectionIndex = Integer.parseInt(list[0]);   
  AreaSelectionIndex = Integer.parseInt(list[1]); 
  TilesSelectionIndex = Integer.parseInt(list[2]); 
  SoundsFlag = Integer.parseInt(list[3]);
  
  BackGroundIndex = AreaSelectionIndex;
}
//-------------------------------------------------------------------------------------
static public void EditCurrentPlayerName( int ChangeCursorPosition, int ChangeCharacter )
{
  char[] NameChars = CurrentPlayerName.toCharArray();
		  
  if( ChangeCursorPosition < 0 )
	  CurrentPlayerNameCursorPosition--;
  else
  if( ChangeCursorPosition > 0 )
	  CurrentPlayerNameCursorPosition++;
  
  if( CurrentPlayerNameCursorPosition < 0 )
	  CurrentPlayerNameCursorPosition = 0;
  
  if( CurrentPlayerNameCursorPosition > HighScores.MAX_NAME_LENGTH  - 1 )
	  CurrentPlayerNameCursorPosition = HighScores.MAX_NAME_LENGTH  - 1;    

  if( ChangeCharacter < 0 )
	  NameChars[CurrentPlayerNameCursorPosition]--;
  else
  if( ChangeCharacter > 0 )
	  NameChars[CurrentPlayerNameCursorPosition]++;
 
  
  if( NameChars[CurrentPlayerNameCursorPosition] < 45 )
	  NameChars[CurrentPlayerNameCursorPosition] = 95;
  
  if( NameChars[CurrentPlayerNameCursorPosition] > 95 )
	  NameChars[CurrentPlayerNameCursorPosition] = 45;
  
  CurrentPlayerName = new String(NameChars); 
}
//-------------------------------------------------------------------------------------
public static void Clear()
{
  GameEngine ge = GameEngine.CurrentObj;  
  ge.FileName = FileName;	
  ge.FileDataStr = "";  
  ge.DoRequest(ge.G_REQUEST_WRITE_FILE);
}
//-------------------------------------------------------------------------------------
}
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
abstract class GameObject
{
public float X, Y;
public int GridX, GridY;
public String Name;

public static int MouseX, MouseY, MouseStatus;
public static int KeyCode, KeyStatus;

public boolean MouseEventNotifyFlag, KeyEventNotifyFlag;

public static final int ME_NOTHING = -1;
public static final int ME_PRESS_DOWN = 0;
public static final int ME_RELEASE = 1; 
public static final int ME_MOVE = 2;  

public static final int ME_KEY_DOWN = 3;
public static final int ME_KEY_UP = 4;

public int MouseX_Dup, MouseY_Dup, MouseStatus_Dup;
public int KeyCode_Dup, KeyStatus_Dup;

public static final int ScreenWidth = 320;
public static final int ScreenHeight = 480;

protected static GameEngine GE;

protected int TextLayer;
protected int PictureLayer;

public int UniqueId;
public int GroupId;

public char[] ClassType;
public static final int MAX_CLASS_TYPES = 100;

/* input delay */

public int InputDelayMax;
public int InputDelayCounter;

/* Add extended class types here */
  
public static final char TYPE_BUTTON = 0;
public static final char TYPE_CAPTION = 1;
public static final char TYPE_CHECKBOX = 2;
public static final char TYPE_RADIO_BUTTON = 3;
public static final char TYPE_MULTI_STATE_BUTTON = 4;
public static final char TYPE_SIMPLE_BACKGROUND = 5;
public static final char TYPE_BLOCK_GAME_ENGINE = 6;
public static final char TYPE_MENU_CURSOR_PARALLELIA = 7;
public static final char TYPE_CAPTION_BOX = 8;
public static final char TYPE_BLOCKCELL = 9;
public static final char TYPE_WAVY_PILLARS = 10;
public static final char TYPE_PARABOLIC_PROJECTILE_CELLS = 11;
public static final char TYPE_SWAP_CELL = 12;
public static final char TYPE_SWAP_CELL_GRID = 13;
public static final char TYPE_CUSTOM_DRAW_TEXT = 14;
public static final char TYPE_COPYRIGHT_CAPTION = 15;
public static final char TYPE_SWAP_CELL_DEMO_DISPLAY = 16;
public static final char TYPE_SCORE_BAR = 17;
public static final char TYPE_SWAP_CELL_GRID_CONTROLLER = 18;

public static final char TYPE_CELL = 19;
public static final char TYPE_CELL_GRID = 20;
public static final char TYPE_PLAYER_SHIP = 21;
public static final char TYPE_MOVING_CELL = 22;
public static final char TYPE_TRACTOR_BEAM = 23;
public static final char TYPE_PLAYER_CONTROL_PANEL = 24;
public static final char TYPE_CELL_FOUNTAIN_SPRAY = 25;

//-------------------------------------------------------------------------------------
public GameObject()
{
  X = Y = 0.0f; GridX = GridY = 0;
  Name = new String("");
  ClassType = new char[MAX_CLASS_TYPES];
  MouseEventNotifyFlag = KeyEventNotifyFlag = false;
  UniqueId = 0; GroupId = 0;
  PictureLayer = GE.LAYER_0; 
  TextLayer = GE.LAYER_1;
  ClearDupInput();
  
  InputDelayMax = 5;
  InputDelayCounter = 0;
}
//-------------------------------------------------------------------------------------
public static void SetGameEngine( GameEngine pGE )
{
  GE = pGE;
}
//-------------------------------------------------------------------------------------
public void SetLayers( int nTextLayer, int nPictureLayer )
{
  TextLayer = nTextLayer; PictureLayer = nPictureLayer;
}
//-------------------------------------------------------------------------------------
public void Draw() {}
public void Do() {}
public void OnClick() {}
public void OnKey() {}
//-------------------------------------------------------------------------------------
public void CopyDupInput()
{
  MouseX_Dup = MouseX;
  MouseY_Dup = MouseY;
  MouseStatus_Dup = MouseStatus;
  KeyCode_Dup = KeyCode;
  KeyStatus_Dup = ME_NOTHING;	
}
//-------------------------------------------------------------------------------------
public void ClearDupInput()
{
 MouseX_Dup = MouseY_Dup = MouseStatus_Dup =
   KeyCode_Dup = KeyStatus_Dup = ME_NOTHING;
}
//-------------------------------------------------------------------------------------
}

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
class Button extends GameObject
{

public static final int BUTTON_RELEASED = 0;
public static final int BUTTON_PRESSED = 1;

public static final int STYLE_PRIMITIVE = 1;
public static final int STYLE_PICTURE = 2;

public static final int BUTTON_MECHANIC_NORMAL = 0;
public static final int BUTTON_MECHANIC_STICKY = 1;

protected int ButtonState;
protected int Width, Height;
protected int BorderColor0, InnerColor0, DrawStyle0, PictureIndex0, TextColor0;
protected int BorderColor1, InnerColor1, DrawStyle1, PictureIndex1, TextColor1;
protected int FontSize;
protected int ButtonMechanic;

protected int PictureIndex0_TileX, PictureIndex0_TileY;
protected int PictureIndex1_TileX, PictureIndex1_TileY;
protected int PictureWidthCells, PictureHeightCells;
protected int TileWidthPixels, TileHeightPixels;
protected int AlphaValue;

protected int DrawAltFlag = 0;

public static final int UNCLICK_WHEN_OUTSIDE = 1;
public static final int UNCLICK_WHEN_RELEASE = 2;
public int ReleaseType = UNCLICK_WHEN_OUTSIDE;

//-------------------------------------------------------------------------------------
public Button()
{
super();
ClassType[TYPE_BUTTON] = 1;
ButtonState = BUTTON_RELEASED;
MouseEventNotifyFlag = true;
PictureLayer = GE.LAYER_1;
TextLayer = GE.LAYER_2;
AlphaValue = 255;
}
//-------------------------------------------------------------------------------------
public Button( String Caption, int nX, int nY, int nWidth, int nHeight )
{
super();
ClassType[TYPE_BUTTON] = 1;
Name = new String(Caption);
X = nX; Y = nY; Width = nWidth; Height = nHeight;
ButtonState = BUTTON_RELEASED;
MouseEventNotifyFlag = true;

DrawStyle0 = STYLE_PRIMITIVE;
InnerColor0 = Color.rgb(100,100,100);
BorderColor0 = Color.rgb(255, 0, 0);
TextColor0 = Color.rgb(255, 0, 0);
PictureIndex0 = 0;

DrawStyle1 = STYLE_PRIMITIVE;
InnerColor1 = Color.rgb(100,100,100);
BorderColor1 = Color.rgb(255, 255, 255);
TextColor1 = Color.rgb(255, 255, 255);
PictureIndex1 = 0;

FontSize = 18;
ButtonMechanic = BUTTON_MECHANIC_NORMAL;
PictureLayer = GE.LAYER_1;
TextLayer = GE.LAYER_2;

PictureIndex0_TileX = PictureIndex0_TileY = -1;
PictureIndex1_TileX = PictureIndex1_TileY = -1;
PictureWidthCells = PictureHeightCells = -1;
AlphaValue = 255;
}
//-------------------------------------------------------------------------------------
public void SetReleasedStyle( int nDrawStyle, int nBorderColor, int nInnerColor, int nPictureIndex, 
         int nTextColor )
{
DrawStyle0 = nDrawStyle; BorderColor0 = nBorderColor; InnerColor0 = nInnerColor; 
PictureIndex0 = nPictureIndex; TextColor0 = nTextColor;
}
//------------------------------------------------------------------------------------- 
public void SetPressedStyle( int nDrawStyle, int nBorderColor, int nInnerColor, int nPictureIndex, 
         int nTextColor )
{
DrawStyle1 = nDrawStyle; BorderColor1 = nBorderColor; InnerColor1 = nInnerColor; 
PictureIndex1 = nPictureIndex; TextColor1 = nTextColor;
}
//------------------------------------------------------------------------------------- 
protected boolean CheckClickWithin()
{
boolean returnvalue = false;

if( X <= MouseX && MouseX < X + Width &&
    Y <= MouseY && MouseY < Y + Height )
    returnvalue = true;
return returnvalue;    
}
//-------------------------------------------------------------------------------------
protected void DrawPressed()
{
if( DrawStyle1 == STYLE_PRIMITIVE )
{
  GE.DrawBox( X, Y, X+Width, Y+Height, PictureLayer, BorderColor1, 
    GameEngine.G_STYLE_FILL );
  GE.DrawBox( X+2, Y+2, X+Width-2, Y+Height-2, PictureLayer, InnerColor1, 
    GameEngine.G_STYLE_FILL );
  GE.DrawTextColor( Name,(int)X+5, (int)Y+Height-10, TextLayer, TextColor1, FontSize );
}
else
if( DrawStyle1 == STYLE_PICTURE )
{
  if( PictureIndex1_TileX > -1 )
      GameGlobals.DrawTileImageGeneralUltra( PictureIndex1, (int)X, (int)Y, PictureLayer, AlphaValue, PictureIndex1_TileX, PictureIndex1_TileY, 
    	PictureWidthCells, PictureHeightCells, TileWidthPixels, TileHeightPixels );  
  else	
      GE.DrawImage( PictureIndex1, (int)X, (int)Y, PictureLayer );    
}
}
//-------------------------------------------------------------------------------------
protected void DrawReleased()
{
if( DrawStyle0 == STYLE_PRIMITIVE )
{
  GE.DrawBox( X, Y, X+Width, Y+Height, PictureLayer, BorderColor0, 
    GameEngine.G_STYLE_FILL );
  GE.DrawBox( X+2, Y+2, X+Width-2, Y+Height-2, PictureLayer, InnerColor0, 
    GameEngine.G_STYLE_FILL );
  GE.DrawTextColor( Name,(int)X+5, (int)Y+Height-10, TextLayer, TextColor0, FontSize );
}
else
if( DrawStyle0 == STYLE_PICTURE )
{
  if( PictureIndex0_TileX > -1 )
	  GameGlobals.DrawTileImageGeneralUltra( PictureIndex0, (int)X, (int)Y, PictureLayer, AlphaValue, PictureIndex0_TileX, PictureIndex0_TileY, 
	    PictureWidthCells, PictureHeightCells, TileWidthPixels, TileHeightPixels );  
  else
     GE.DrawImage( PictureIndex0, (int)X, (int)Y, PictureLayer );    
}
}
//-------------------------------------------------------------------------------------
public void Draw()
{  
	
if( DrawAltFlag == 1 )
{
if( ButtonState == BUTTON_RELEASED )
   DrawAlt(0); //DrawReleased();
else
if( ButtonState == BUTTON_PRESSED )
   DrawAlt(1); //DrawPressed();   
}
else
{
if( ButtonState == BUTTON_RELEASED )
   DrawReleased();
else
if( ButtonState == BUTTON_PRESSED )
   DrawPressed();   
}

}
//-------------------------------------------------------------------------------------
public void OnClick() 
{
	int YesFlag = 0;
	
	if( (MouseStatus == ME_PRESS_DOWN || MouseStatus == ME_MOVE ))
	{
     if(  CheckClickWithin() )
	 {
		if( InputDelayMax > 0 )
		{
		  if( InputDelayCounter >= InputDelayMax )
		  {
		    YesFlag = 1;
		    InputDelayCounter = 0;	
		  }
	  
		}	
		else
		  YesFlag = 1;
		
		if( YesFlag == 1 )
		{
		    ButtonState = BUTTON_PRESSED;
		    CopyDupInput();
		    OnClickDo();    
		}
	 }
     else
     {
       if( ReleaseType == UNCLICK_WHEN_OUTSIDE )
    	   ButtonState = BUTTON_RELEASED;
       else
       if( ReleaseType == UNCLICK_WHEN_RELEASE && ButtonState == BUTTON_PRESSED )
    	   CopyDupInput();
     }
     
	}
	else 
    if( MouseStatus == ME_RELEASE && ButtonMechanic != BUTTON_MECHANIC_STICKY )
    {  
       ButtonState = BUTTON_RELEASED;
       if( CheckClickWithin() )
           CopyDupInput();
       OnUnClickDo();    
    }
}
//-------------------------------------------------------------------------------------
protected void OnClickDo()
{
}
//-------------------------------------------------------------------------------------
protected void OnUnClickDo()
{
}
//-------------------------------------------------------------------------------------
public void SetMechanic( int Option )
{
  switch( Option )
  { 
    case BUTTON_MECHANIC_NORMAL:
    case BUTTON_MECHANIC_STICKY:
      ButtonMechanic = Option;
    default:
      break;
  }
}
//-------------------------------------------------------------------------------------
public void Do()
{
  if( InputDelayCounter < InputDelayMax )
      InputDelayCounter++;	
}
//-------------------------------------------------------------------------------------
public void SetTileStyle( int nPictureIndex0, int nPictureIndex1, int nPictureIndex0_TileX,
		 int nPictureIndex0_TileY,  int nPictureIndex1_TileX,  int nPictureIndex1_TileY,
		 int nPictureWidthCells, int nPictureHeightCells, int nTileWidthPixels,
		 int nTileHeightPixels, int nAlphaValue )
{
  DrawStyle0 = DrawStyle1 = STYLE_PICTURE;	 
  PictureIndex0 = nPictureIndex0;
  PictureIndex1 = nPictureIndex1;
  PictureIndex0_TileX = nPictureIndex0_TileX;
  PictureIndex0_TileY = nPictureIndex0_TileY;
  PictureIndex1_TileX = nPictureIndex1_TileX;
  PictureIndex1_TileY = nPictureIndex1_TileY;
  PictureWidthCells = nPictureWidthCells;
  PictureHeightCells = nPictureHeightCells;
  TileWidthPixels = nTileWidthPixels;
  TileHeightPixels = nTileHeightPixels;
  AlphaValue = nAlphaValue;
}
//-------------------------------------------------------------------------------------
public void Create_WidthxHeight( int nX, int nY, int nWidth, int nHeight, int nGroupId, int nUniqueId, int nPictureLayer, int nTextLayer, 
   int nPictureIndex, int nRowIndex0, int nRowIndex1, int nAlpha )
{
 Width = nWidth; Height = nHeight;

 X = nX; Y = nY; GroupId = nGroupId; UniqueId = nUniqueId;
 PictureLayer = nPictureLayer; TextLayer = nTextLayer;

int ScaledWidth = (int)(GameControl.ImageScaleRelative_320x480[nPictureIndex] * Width);
int ScaledHeight = (int)(GameControl.ImageScaleRelative_320x480[nPictureIndex] * Height);

SetTileStyle( nPictureIndex, nPictureIndex, 0, nRowIndex0, 0, nRowIndex1, 1, 1, ScaledWidth, ScaledHeight, nAlpha  );

DrawAltFlag = 1;
}
//--------------------------------------------------------------------------
public void DrawAlt( int nState )
{
  if( nState == 0 )
     GameGlobals.DrawTileImageOne( PictureIndex0, (int)X, (int)Y, PictureLayer, AlphaValue, PictureIndex0_TileX, PictureIndex0_TileY, 
    	 TileWidthPixels, TileHeightPixels );
  else
  if( nState == 1 )
     GameGlobals.DrawTileImageOne( PictureIndex1, (int)X, (int)Y, PictureLayer, AlphaValue, PictureIndex1_TileX, PictureIndex1_TileY, 
    	 TileWidthPixels, TileHeightPixels );
}
//--------------------------------------------------------------------------
public int GetButtonState()
{
   return ButtonState;
}
//--------------------------------------------------------------------------
}
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
class CustomDrawText extends GameObject
{

protected int FontImageIndex = GameControl.IMAGE_FONT_LM_18x27;
protected int FontWidthPixels = 18;
protected int FontHeightPixels = 27;

protected String Text;
protected int TextLength = 0;
protected int AlphaValue = 255;

//-------------------------------------------------------------------------------------
public CustomDrawText( int nX, int nY, int nFontImageIndex, int nFontWidthPixels, int nFontHeightPixels )
{
super();
ClassType[TYPE_CUSTOM_DRAW_TEXT] = 1;
MouseEventNotifyFlag = false;
PictureLayer = GE.LAYER_2;
TextLayer = GE.LAYER_2;
X = nX; Y = nY;
FontImageIndex = nFontImageIndex; FontWidthPixels = nFontWidthPixels; FontHeightPixels = nFontHeightPixels;
}
//-------------------------------------------------------------------------------------
public void SetText( String str )
{
Text = str;
TextLength = str.length();
}
//-------------------------------------------------------------------------------------
public void DrawText( int nX, int nY, String str )
{
  int i, x = 0, y = 0, textlength = str.length();
  char c;
  
  for( i = 0; i < textlength; i++ )
  {
    c = str.charAt(i);
    
    if( c == 13 )
    	continue;
    else
    if( c == 10 )
    {
    	y += FontHeightPixels;
    	x = 0;
    	continue;
    }
    
    GameGlobals.DrawTileImageOne( FontImageIndex, nX + x, nY + y, PictureLayer, AlphaValue, 0, c,
	    	FontWidthPixels, FontHeightPixels );
    
    x += FontWidthPixels;
  }
}
//-------------------------------------------------------------------------------------
public void Draw()
{
  int i, x = 0, y = 0;
  char c;
  
  for( i = 0; i < TextLength; i++ )
  {
    c = Text.charAt(i);
    
    if( c == 13 )
    	continue;
    else
    if( c == 10 )
    {
    	y += FontHeightPixels;
    	x = 0;
    	continue;
    }
    
    GameGlobals.DrawTileImageOne( FontImageIndex, (int)X + x, (int)Y + y, PictureLayer, AlphaValue, 0, c,
	    	FontWidthPixels, FontHeightPixels);
    
    x += FontWidthPixels;
  }
}
//-------------------------------------------------------------------------------------
public int GetStringWidthPixels()
{
	return FontWidthPixels * TextLength;
}
//-------------------------------------------------------------------------------------
}
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
class Caption extends GameObject
{
  public static final int STYLE_TEXT = 0;
  public static final int STYLE_PICTURE = 1;
  public static final int STYLE_TEXT_PICTURE = 2;

  protected int Style;  
  protected int PictureIndex;
  protected int FontSize;
  protected int FontColor;
  protected String Text;
  protected int TextRelativeX;
  protected int TextRelativeY;    
  protected int DistanceBetweenLines;
  protected boolean MultiLineFlag;
  protected boolean DrawCursorFlag;
  protected int CursorCharPosition;
  
  protected int PictureIndex_TileX, PictureIndex_TileY;
  protected int PictureWidthCells, PictureHeightCells;
  protected int TileWidthPixels, TileHeightPixels;
  protected int AlphaValue = 255;  
  
  protected int TileImageFlag = 0;
  
  public static int TextShadowFlag = 1;
  public static int TextShadowColor = Color.rgb(20,20,20);
  public static int TextShadowDisplaceX = -1;
  public static int TextShadowDisplaceY = -1;
  public static int TextShadowCount = 2;
  
//-------------------------------------------------------------------------------------
 public Caption()
 {
  super();
  ClassType[TYPE_CAPTION] = 1;	
  PictureIndex_TileX = PictureIndex_TileY = -1;
 }
 //-------------------------------------------------------------------------------------
public Caption( int nX, int nY, int nPictureIndex )
{
 super();
 ClassType[TYPE_CAPTION] = 1;
 FontSize = 20;
 FontColor = Color.rgb(255,255,255);
 TextRelativeX = 0; TextRelativeY = 20;
	  
  Style = STYLE_PICTURE;
  X = nX; Y = nY; PictureIndex = nPictureIndex;
  MultiLineFlag = false;
  DistanceBetweenLines = 20;
  PictureLayer = GE.LAYER_1;
  TextLayer = GE.LAYER_2;  
  DrawCursorFlag = false;
  
  PictureIndex_TileX = PictureIndex_TileY = -1;
}
//-------------------------------------------------------------------------------------
public Caption( int nX, int nY, String sText )
{
  super();
  ClassType[TYPE_CAPTION] = 1;
  FontSize = 20;
  FontColor = Color.rgb(255,255,255);
  TextRelativeX = 0; TextRelativeY = 20;
	 
  Style = STYLE_TEXT;
  X = nX; Y = nY; Text = new String(sText);
  MultiLineFlag = false;
  DistanceBetweenLines = 20;
  PictureLayer = GE.LAYER_1;
  TextLayer = GE.LAYER_2;    
  DrawCursorFlag = false;
  
  PictureIndex_TileX = PictureIndex_TileY = -1;
}
//-------------------------------------------------------------------------------------
public void SetTextOptions( int nFontSize, int nColor, int nTextRelativeX, int nTextRelativeY, boolean bMultiLineFlag,
   int nDistanceBetweenLines )
{
   FontSize = nFontSize;
   FontColor = nColor;
   TextRelativeX = nTextRelativeX;
   TextRelativeY = nTextRelativeY;
   MultiLineFlag = bMultiLineFlag;
   DistanceBetweenLines = nDistanceBetweenLines;
}
//-------------------------------------------------------------------------------------
public void SetText( String sText )
{
  Text = new String(sText);
}
//-------------------------------------------------------------------------------------
public void SetTextPicture( String sText, int nPictureIndex )
{
  Text = new String(sText);
  PictureIndex = nPictureIndex;
  Style = STYLE_TEXT_PICTURE;
}
//-------------------------------------------------------------------------------------
public void Draw() 
{
 int i, k;
 
 if( Style == STYLE_TEXT || Style == STYLE_TEXT_PICTURE )
 {
  if( !MultiLineFlag )
  {
	
   if( TextShadowFlag >= 1 )
   {
    for( i = 1; i <= TextShadowCount; i++ )   
       GE.DrawTextColor( Text,(int)(X + TextRelativeX + TextShadowDisplaceX * i), (int)(Y + TextRelativeY + TextShadowDisplaceY * i), 
         TextLayer, TextShadowColor, FontSize );
   }
   
   GE.DrawTextColor( Text,(int)(X + TextRelativeX), (int)(Y + TextRelativeY), 
       TextLayer, FontColor, FontSize );
   
   if( DrawCursorFlag )
   {
	  int length = Text.length();
	  char[] chlist = new char[length];	  

	  for( i = 0; i < length; i++ )
		   chlist[i] = ' ';
	  chlist[CursorCharPosition] = '_';
	  String cursorstr = new String(chlist);
	  
	   if( TextShadowFlag >= 1 )
	   {
	    for( i = 1; i <= TextShadowCount; i++ )   
         GE.DrawTextColor( cursorstr,(int)(X + TextRelativeX + TextShadowDisplaceX * i), (int)(Y + TextRelativeY + TextShadowDisplaceY * i), 
         TextLayer, TextShadowColor, FontSize );
	   }
	   
	   GE.DrawTextColor( cursorstr,(int)(X + TextRelativeX), (int)(Y + TextRelativeY), 
		       TextLayer, FontColor, FontSize );	  
   }
  }
  else
  {
   int LineY;
   String[] StrLines = Text.split("\n");
   
   for( i = 0, LineY = 0; i < StrLines.length; i++, LineY += DistanceBetweenLines )
   {
     if( TextShadowFlag >= 1 )
     {
      for( k = 1; k <= TextShadowCount; k++ )   
         GE.DrawTextColor( StrLines[i],(int)(X + TextRelativeX + TextShadowDisplaceX * k), 
           (int)(Y + TextRelativeY + LineY + TextShadowDisplaceY * k), TextLayer, TextShadowColor, FontSize );
     }
	 
     GE.DrawTextColor( StrLines[i],(int)(X + TextRelativeX), (int)(Y + TextRelativeY + LineY), 
       TextLayer, FontColor, FontSize );      
   }
  }
 }

 if( TileImageFlag == 1 )
     GameGlobals.DrawTileImageOne( PictureIndex, (int)X, (int)Y, PictureLayer, AlphaValue, 0, PictureIndex_TileY, 
    	 TileWidthPixels, TileHeightPixels ); 
 else
 if( Style == STYLE_PICTURE || Style == STYLE_TEXT_PICTURE )
   GE.DrawImage( PictureIndex, (int)X, (int)Y, PictureLayer );    
}
//-------------------------------------------------------------------------------------
public void SetTileStyle( int nPictureIndex, 
		  int nPictureIndex_TileX, int nPictureIndex_TileY,
		  int nPictureWidthCells, int nPictureHeightCells,
		  int nTileWidthPixels, int nTileHeightPixels,
		  int nAlphaValue )
{
	  PictureIndex = nPictureIndex;
	  PictureIndex_TileX = nPictureIndex_TileX;
	  PictureIndex_TileY = nPictureIndex_TileY;
	  PictureWidthCells = nPictureWidthCells;
	  PictureHeightCells = nPictureHeightCells;
	  TileWidthPixels = nTileWidthPixels;
	  TileHeightPixels = nTileHeightPixels;
	  AlphaValue = nAlphaValue;  	  	  
}
//-------------------------------------------------------------------------------------
public void SetFontColor( int nColor)
{
	FontColor = nColor;	
}
//-------------------------------------------------------------------------------------
public Caption( int nX, int nY, int nGroupId, int nUniqueId, int nStyle, int nTextLayer, int nPictureLayer )
{	
  super();
  ClassType[TYPE_CAPTION] = 1;
  
  X = nX; Y = nY;
  GroupId = nGroupId; UniqueId = nUniqueId;
  Style = nStyle; 
  
  if( nTextLayer < 0 || nPictureLayer < 0 )
  {
	  TextLayer = GE.LAYER_2; PictureLayer = GE.LAYER_1;	 
  }
  else
  {
    TextLayer = nTextLayer; PictureLayer = nPictureLayer;
  }
}
//-------------------------------------------------------------------------------------
public void Init_1( String str, int nFontSize, int nColor, int nTextRelativeX, int nTextRelativeY, boolean bMultiLineFlag,
   int nDistanceBetweenLines )
{
	Text = new String(str);
	
   FontSize = nFontSize;
   FontColor = nColor;
   TextRelativeX = nTextRelativeX;
   TextRelativeY = nTextRelativeY;
   MultiLineFlag = bMultiLineFlag;
   DistanceBetweenLines = nDistanceBetweenLines;	
}
//-------------------------------------------------------------------------------------
public void Init_2( int nPictureIndex, int nWidth, int nHeight, int nRowIndex0, int nAlphaValue, int nTileImageFlag )
{
  int ScaledWidth = (int)(GameControl.ImageScaleRelative_320x480[nPictureIndex] * nWidth);
  int ScaledHeight = (int)(GameControl.ImageScaleRelative_320x480[nPictureIndex] * nHeight);  

  SetTileStyle( nPictureIndex, 0, nRowIndex0,  1, 1, ScaledWidth, ScaledHeight, nAlphaValue );
  TileImageFlag = nTileImageFlag;
}
//-------------------------------------------------------------------------------------
public void GetTextWidthHeightPixels( int[] wh )
{
Paint paintobj = new Paint();	
paintobj.setTextSize(FontSize);
	
Rect bounds = new Rect();
paintobj.getTextBounds(Text, 0, Text.length(), bounds);

wh[0] = bounds.width() / Text.length();	
wh[1] = bounds.height();

}
//-------------------------------------------------------------------------------------
}
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
class CopyRightCaption extends GameObject
{
protected int FontColor, FontShadowColor, FontSize, FontWidth, FontHeight;
protected String Text;

//-------------------------------------------------------------------------------------
public CopyRightCaption()
{
  FontColor = GameOptions.MainFontColor;
  FontShadowColor = Color.rgb( 20, 20, 20 );
  FontSize = 12;

  Text = "© 2014 Y.X.Y. All Rights Reserved.";
  String teststr = "X                                                    X";

  Paint paintobj = new Paint();
  paintobj.setTypeface(Typeface.MONOSPACE);
  paintobj.setTextSize(FontSize);

  Rect bounds = new Rect();  
  paintobj.getTextBounds(teststr, 0, teststr.length(), bounds);
  
  FontHeight = bounds.height();
  FontWidth = bounds.width() / teststr.length();

  PictureLayer = GE.LAYER_2;
  TextLayer = GE.LAYER_4;
  ClassType[TYPE_COPYRIGHT_CAPTION] = 1;
  
  paintobj.getTextBounds(Text, 0, Text.length(), bounds);
  
  X = GameEngine.TARGET_SCREEN_WIDTH/2 - (bounds.width()/2)/(GE.Xscale);
  Y = GameEngine.TARGET_SCREEN_HEIGHT - 5 + GameGlobals.Y_SHIFT;
}
//-------------------------------------------------------------------------------------
public void Draw()
{
   GE.DrawTextColor( Text, (int)X - 1, (int)Y - 1, TextLayer, FontShadowColor, FontSize );	
   GE.DrawTextColor( Text, (int)X, (int)Y, TextLayer, FontColor, FontSize );	
}
//-------------------------------------------------------------------------------------
}
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
class CaptionBox extends GameObject
{
protected int FontColor, FontSize, FontWidth, FontHeight, BoxColor;
public final static int MAX_LINES = 20000;
public final static int VERTICAL_FONT_SPACING = 3;

protected int LineIndex, TotalLines;
protected int BoxWidthPixels, BoxHeightPixels, MaxColumns, MaxRows;
protected String[] TextList;
protected int TopLeftX, TopLeftY;

protected String[] VisibleTextList;
protected char[] TextCharArray;

//-------------------------------------------------------------------------------------
public CaptionBox( int nFontSize, int nFontColor, int nTopLeftX, int nTopLeftY,
                   int nBoxWidthPixels, int nBoxHeightPixels, int nBoxColor )
{
super();

String teststr = "X                                                    X";

FontSize = nFontSize; FontColor = nFontColor; BoxColor = nBoxColor;
TopLeftX = nTopLeftX; TopLeftY = nTopLeftY;
BoxWidthPixels = nBoxWidthPixels; BoxHeightPixels = nBoxHeightPixels;

Paint paintobj = new Paint();
paintobj.setTypeface(Typeface.MONOSPACE);
paintobj.setTextSize(FontSize);

Rect bounds = new Rect();
paintobj.getTextBounds(teststr, 0, teststr.length(), bounds);

FontHeight = bounds.height() + VERTICAL_FONT_SPACING;
FontWidth = bounds.width() / teststr.length();

MaxColumns = (int)(BoxWidthPixels / FontWidth * GE.Xscale);
MaxRows = BoxHeightPixels / FontHeight;
LineIndex = 0;
VisibleTextList = new String[MaxRows];

PictureLayer = GE.LAYER_2;
TextLayer = GE.LAYER_4;
ClassType[TYPE_CAPTION_BOX] = 1;

}
//-------------------------------------------------------------------------------------
public void SetText( String str )
{
if( str.length() < 1 )
    str = " ";
str = str.replace("\r\n", "\n");

int strlength = str.length();
int i, k, ColumnCellCount, WhiteSpaceCount;
char ch; 
StringBuilder sb = new StringBuilder();

char[] char_array = new char[strlength+1];
char[] char_array_nl = new char[strlength+1];

str.getChars (0, strlength-1, char_array, 0);
strlength = char_array.length;

ColumnCellCount = WhiteSpaceCount = 0;

for( i = 0; i < strlength; )
{	
   ch = char_array[i];
     
   if( ch == '\n' )
     ColumnCellCount = 0;
   else        
     ColumnCellCount++;
     
   if( ch == 0x20 || ch == 0x09 || ch == '\n' )  
	   WhiteSpaceCount++;
       
   if( ColumnCellCount >= MaxColumns )
   {     
    if( ch != 0x20 && ch != 0x09 && ch != '\n' )
    {     
      if( WhiteSpaceCount >= 1 )
      {
	    for( k = i - 1; k > 0; k-- )
	    {
		 ch = char_array[k];
		 if( ch == 0x20 || ch == 0x09 || ch == '\n' )
			 break;
	     }
         char_array[k] = '\n';
         i = k;
      }
      else
      {
         char_array_nl[i] = '\n';
      }
      
      WhiteSpaceCount = ColumnCellCount = 0;
    }
    else
      char_array[i] = '\n';
   
   }
   i++;
}

  for( i = 0; i < strlength; i++ )
  {
     sb.append(char_array[i]);
     if( char_array_nl[i] == '\n' )
         sb.append('\n');
  }
//TextList = str.split("\n");
//TotalLines = TextList.length;
//LineIndex = 0;

  str = sb.toString();
  TextCharArray = new char[str.length()];
  str.getChars (0, str.length()-1, TextCharArray, 0);

  TotalLines = 0;
  for( i = 0; i < TextCharArray.length; i++ )
	if( TextCharArray[i] == '\n' )
		TotalLines++;
	else
	if( i == strlength - 1 )
		TotalLines++;   

  SetLineIndex(0);

}
//-------------------------------------------------------------------------------------
public void SetLineIndex( int nLineIndex )
{
	if( nLineIndex >= 0 && nLineIndex < TotalLines )
	  LineIndex = nLineIndex;
	
   StringBuilder sb = new StringBuilder();
   int i, j, k, countlineindex = 0, visiblelineindex = 0;
 
   j = 0;
   
   for( i = 0; i < TextCharArray.length; i++ )
   { 
	 if( LineIndex == countlineindex )
	 {
	    for( k = i; k < TextCharArray.length; k++ )
	    {
	      sb.append(TextCharArray[k]);
	      if( TextCharArray[k] == '\n' )
		      visiblelineindex++;	  
	      if( visiblelineindex >= MaxRows - 1 )
	    	  break;
	    }	
	    break;
	 }

	 if( TextCharArray[i] == '\n' )
		 countlineindex++;
   } 
   VisibleTextList = sb.toString().split("\n");
  
}
//-------------------------------------------------------------------------------------
public void MoveIndex( int nDirection )
{
	int nLineIndex = LineIndex + nDirection;
	
	if( nLineIndex >= 0 && nLineIndex < TotalLines )
	  LineIndex = nLineIndex;   	
	SetLineIndex(LineIndex);
}
//-------------------------------------------------------------------------------------
public void Draw()
{
   int i, k, end_index;
   
   GE.DrawBox( TopLeftX, TopLeftY, TopLeftX + BoxWidthPixels + FontWidth, TopLeftY + BoxHeightPixels + FontHeight, 
		   PictureLayer, BoxColor, GameEngine.G_STYLE_FILL );
/*  
   end_index = LineIndex + MaxRows - 1;
   if( end_index > TotalLines - 1 )
	   end_index = TotalLines - 1;
   
   for( i = LineIndex, k = 0; i <= end_index; i++, k++ )	   
   {	   
	  GE.DrawTextColor( TextList[i], TopLeftX + FontWidth, TopLeftY + k * FontHeight + FontHeight, 
		    TextLayer, FontColor, FontSize );		  
   }
*/  
   for( i = 0; i < VisibleTextList.length; i++  )	   
   {	   
	  GE.DrawTextColor( VisibleTextList[i], TopLeftX + FontWidth, TopLeftY + i * FontHeight + FontHeight, 
		    TextLayer, FontColor, FontSize );		  
   }   
}	   
//-------------------------------------------------------------------------------------	   
}
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
class CheckBox extends GameObject
{
  protected final int STYLE_PRIMITIVE = 0;
  protected final int STYLE_PICTURE = 1;

  protected int OuterColor;
  protected int InnerColor;
  protected int Style;
  protected int Width, Height;  
  protected int CheckedPictureIndex;
  protected int UnCheckedPictureIndex;

  public boolean CheckedFlag = false;
  
  protected int CheckedPictureIndex_TileX, CheckedPictureIndex_TileY;
  protected int UnCheckedPictureIndex_TileX, UnCheckedPictureIndex_TileY;
  protected int PictureWidthCells, PictureHeightCells;
  protected int TileWidthPixels, TileHeightPixels;
  protected int AlphaValue;    
  
  protected int DrawAltFlag = 0;
  
//------------------------------------------------------------------------------------- 
public CheckBox( int nX, int nY, boolean bCheckedFlag )
{
  super();  
  ClassType[TYPE_CHECKBOX] = 1;
  MouseEventNotifyFlag = true;
  CheckedFlag = bCheckedFlag;
  X = nX; Y = nY;
  Width = Height = 48;
  SetPrimitiveStyle( Color.rgb(255,0,0), Color.rgb(100,100,100));
  PictureLayer = GE.LAYER_1;
  CheckedPictureIndex_TileX = -1;
}
//-------------------------------------------------------------------------------------
public void SetPrimitiveStyle( int nOuterColor, int nInnerColor )
{
  Style = STYLE_PRIMITIVE;
  OuterColor = nOuterColor;
  InnerColor = nInnerColor;
}
//-------------------------------------------------------------------------------------
public void SetPictureStyle( int nCheckedPictureIndex, int nUnCheckedPictureIndex )
{
  Style = STYLE_PICTURE;
  CheckedPictureIndex = nCheckedPictureIndex;
  UnCheckedPictureIndex = nUnCheckedPictureIndex;
}
//-------------------------------------------------------------------------------------
protected void DrawPrimitive()
{
 GE.DrawBox( X, Y, X+Width, Y+Height, PictureLayer, InnerColor, 
	    	   GameEngine.G_STYLE_FILL ); 	    
 GE.DrawBox( X, Y, X+Width, Y+Height, PictureLayer, OuterColor, 
   GameEngine.G_STYLE_STROKE );    
 
 if( CheckedFlag )
 {
   GE.DrawLine( X, Y + Height/2, X + Width/2, Y + Height, GE.LAYER_1 , OuterColor);
   GE.DrawLine( X + Width/2, Y + Height, X + Width, Y, GE.LAYER_1 , OuterColor);
 }	
}
//-------------------------------------------------------------------------------------
protected void DrawPicture()
{
  if( CheckedPictureIndex_TileX > -1 )
  {
    if( CheckedFlag )  
    	GameGlobals.DrawTileImageGeneralUltra( CheckedPictureIndex, (int)X, (int)Y, PictureLayer, AlphaValue, 
    			CheckedPictureIndex_TileX, CheckedPictureIndex_TileY, 
    	    	PictureWidthCells, PictureHeightCells, TileWidthPixels, TileHeightPixels );      	
    else	
    	GameGlobals.DrawTileImageGeneralUltra( UnCheckedPictureIndex, (int)X, (int)Y, PictureLayer, AlphaValue, 
    			UnCheckedPictureIndex_TileX, UnCheckedPictureIndex_TileY, 
    	    	PictureWidthCells, PictureHeightCells, TileWidthPixels, TileHeightPixels );     	
  }
  else
  {
	 if( CheckedFlag )  
	   GE.DrawImage( CheckedPictureIndex, (int)X, (int)Y, PictureLayer  );
     else
	   GE.DrawImage( UnCheckedPictureIndex, (int)X, (int)Y, PictureLayer  );	
  }
}
//-------------------------------------------------------------------------------------
public void Draw()
{
 if( DrawAltFlag == 1 )
 {
	 DrawAlt();
 }
 else
 {
  if( Style == STYLE_PRIMITIVE )
    DrawPrimitive();
  else
  if( Style == STYLE_PICTURE )
    DrawPicture();
 } 
}
//-------------------------------------------------------------------------------------
protected boolean CheckClickWithin()
{
boolean returnvalue = false;

if( X <= MouseX && MouseX < X + Width &&
    Y <= MouseY && MouseY < Y + Height )
    returnvalue = true;
return returnvalue;    
}
//-------------------------------------------------------------------------------------
public void OnClick() 
{
	int YesFlag = 0;

	if(( MouseStatus == ME_PRESS_DOWN || MouseStatus == ME_MOVE ) && CheckClickWithin() )
	{
		if( InputDelayMax > 0 )
		{
		  if( InputDelayCounter >= InputDelayMax )
		  {	
	        YesFlag = 1;
	        InputDelayCounter = 0;
		  }
		}
		else
		  YesFlag = 1;
		
		if( YesFlag == 1 )
		{
	        if( CheckedFlag )
	            CheckedFlag = false;
	        else
	            CheckedFlag = true;
	 
	        CopyDupInput();
	        OnClickDo();		
		}
		
	}
}
//-------------------------------------------------------------------------------------
protected void OnClickDo()
{
}
//-------------------------------------------------------------------------------------
public void Do()
{
  if( InputDelayCounter < InputDelayMax )
      InputDelayCounter++;	
}
//-------------------------------------------------------------------------------------
public void SetTileStyle( int nCheckedPictureIndex, int nUnCheckedPictureIndex, int nCheckedPictureIndex_TileX,
		   int nCheckedPictureIndex_TileY,  int nUnCheckedPictureIndex_TileX,  int nUnCheckedPictureIndex_TileY,
		   int nPictureWidthCells, int nPictureHeightCells, int nTileWidthPixels,
		   int nTileHeightPixels, int nAlphaValue )
{
		 CheckedPictureIndex = nCheckedPictureIndex;
		 UnCheckedPictureIndex = nUnCheckedPictureIndex;
		 CheckedPictureIndex_TileX = nCheckedPictureIndex_TileX;
		 CheckedPictureIndex_TileY = nCheckedPictureIndex_TileY;
		 UnCheckedPictureIndex_TileX = nUnCheckedPictureIndex_TileX;
		 UnCheckedPictureIndex_TileY = nUnCheckedPictureIndex_TileY;
		 PictureWidthCells = nPictureWidthCells;
		 PictureHeightCells = nPictureHeightCells;
		 TileWidthPixels = nTileWidthPixels;
		 TileHeightPixels = nTileHeightPixels;
		 AlphaValue = nAlphaValue; 	
}
//-------------------------------------------------------------------------------------
public CheckBox( int nX, int nY, int nWidth, int nHeight, int nGroupId, int nUniqueId, int nPictureLayer,
		int nPictureIndex, int nRowIndex0, int nRowIndex1, int nAlpha, boolean bCheckedFlag )
{
  super();  
  ClassType[TYPE_CHECKBOX] = 1;
  MouseEventNotifyFlag = true;
  CheckedFlag = bCheckedFlag;
  X = nX; Y = nY;  Width = nWidth; Height = nHeight;
  GroupId = nGroupId; UniqueId = nUniqueId;
  PictureLayer = nPictureLayer; 
  
int ScaledWidth = (int)(GameControl.ImageScaleRelative_320x480[nPictureIndex] * Width);
int ScaledHeight = (int)(GameControl.ImageScaleRelative_320x480[nPictureIndex] * Height);  

DrawAltFlag = 1;

SetTileStyle( nPictureIndex, nPictureIndex, 0, nRowIndex1,  0,  nRowIndex0,
		   1, 1, ScaledWidth, ScaledHeight, nAlpha );

}
//-------------------------------------------------------------------------------------
protected void DrawAlt()
{
 if( CheckedFlag )
     GameGlobals.DrawTileImageOne( CheckedPictureIndex, (int)X, (int)Y, PictureLayer, AlphaValue, 0, CheckedPictureIndex_TileY, 
    	 TileWidthPixels, TileHeightPixels );
 else
     GameGlobals.DrawTileImageOne( UnCheckedPictureIndex, (int)X, (int)Y, PictureLayer, AlphaValue, 0, UnCheckedPictureIndex_TileY, 
    	 TileWidthPixels, TileHeightPixels );        	
}
//-------------------------------------------------------------------------------------
}
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
class RadioButton extends CheckBox
{

private final int MAX_RADIO_BUTTON_LIST_LENGTH = 12;
private RadioButton[] List;
private int ListCount;

//-------------------------------------------------------------------------------------
public RadioButton( int nX, int nY, boolean bCheckedFlag )
{
   super( nX, nY, bCheckedFlag );
   ClassType[TYPE_RADIO_BUTTON] = 1;
   ListCount = 0;
   List = new RadioButton[MAX_RADIO_BUTTON_LIST_LENGTH];
   PictureLayer = GE.LAYER_1; 
}
//-------------------------------------------------------------------------------------
protected void CopyDaisyChain( RadioButton DestRadio )
{
  int i;
  for( i = 0; i < MAX_RADIO_BUTTON_LIST_LENGTH; i++ )
     DestRadio.List[i] = List[i];
  DestRadio.ListCount = ListCount;
}
//-------------------------------------------------------------------------------------
public void Add2DaisyChain( RadioButton NewRadio )
{
  int i;
  for( i = 0; i < MAX_RADIO_BUTTON_LIST_LENGTH; i++ )
    if( List[i] == null )
    {
      List[i] = NewRadio;
      ListCount++;
      break;
    }

  for( i = 0; i < ListCount; i++ )
    if( List[i] != null && List[i] != this )
        CopyDaisyChain( List[i] ); 
}
//-------------------------------------------------------------------------------------
protected void DrawPrimitive()
{
 GE.DrawCircle( X + Width/2, Y + Height/2, Width/2, PictureLayer, InnerColor, GE.G_STYLE_FILL ); 
 
 if( CheckedFlag )
     GE.DrawCircle( X + Width/2, Y + Height/2, Width/3, PictureLayer, OuterColor, GE.G_STYLE_FILL );
}
//-------------------------------------------------------------------------------------
protected void OnClickDo()
{
  int i;
  for( i = 0; i < ListCount; i++ )
    if( List[i] != null && List[i] != this )
        List[i].CheckedFlag = false;
  CheckedFlag = true;
}
//------------------------------------------------------------------------------------- 
}
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
class MultiStateButton extends GameObject
{
/*
enhanced verision of the ordinary button
*/

protected int MaxStates;
protected int FontSize;
protected int[] StateInnerColor;
protected int[] StateOuterColor;
protected String[] CaptionList;
protected int[] StatePictureIndex;
protected int ButtonState;
protected int Width, Height;

public static final int STYLE_PRIMITIVE = 1;
public static final int STYLE_PICTURE = 2;
public int DrawStyle;

protected int TilePictureIndex;
protected int [] TilePictureIndex_TileX, TilePictureIndex_TileY;
protected int PictureWidthCells, PictureHeightCells;
protected int TileWidthPixels, TileHeightPixels;
protected int AlphaValue;

protected int DrawAltFlag = 0;

//-------------------------------------------------------------------------------------
public MultiStateButton( int nX, int nY, int nWidth, int nHeight, int nMaxStates, int nFontSize )
{
	  super();
	  ClassType[TYPE_MULTI_STATE_BUTTON] = 1;
	  Name = new String("Multistate Button");
	  X = nX; Y = nY; Width = nWidth; Height = nHeight;
	  ButtonState = 0;
	  MaxStates = nMaxStates;
	  MouseEventNotifyFlag = true;
	  
	  DrawStyle = STYLE_PICTURE; 
	  
	  if( nFontSize > 0 )
	     FontSize = nFontSize;
	  else
	     FontSize = 18;	
	  
	  TextLayer = GE.LAYER_2;
	  PictureLayer = GE.LAYER_1;	
	  
	  TilePictureIndex_TileX = TilePictureIndex_TileY = null;
}
//-------------------------------------------------------------------------------------
public MultiStateButton( int[] nStatePictureList, String[] sCaptionList, 
	     int[] nStateInnerColor, int[] nStateOuterColor,
	     int nX, int nY, int nWidth, int nHeight, int nMaxStates, int nFontSize )
	{
	  super();
	  ClassType[TYPE_MULTI_STATE_BUTTON] = 1;
	  Name = new String("Multistate Button");
	  X = nX; Y = nY; Width = nWidth; Height = nHeight;
	  ButtonState = 0;
	  MaxStates = nMaxStates;
	  MouseEventNotifyFlag = true;
	  
	  int i;
	  
	  if( nFontSize > 0 )
	     FontSize = nFontSize;
	  else
	     FontSize = 18;
	   
	  if( nStatePictureList != null )
	  {  
	     StatePictureIndex = new int[MaxStates];
	     
	     for( i = 0; i < MaxStates; i++ )
	          StatePictureIndex[i] = nStatePictureList[i];
	     
	     DrawStyle = STYLE_PICTURE;     
	           
	  }
	  if( sCaptionList != null )
	  {
	     CaptionList = new String[MaxStates];
	     
	     for( i = 0; i < MaxStates; i++ )
	        CaptionList[i] = new String(sCaptionList[i]);
	     
	     DrawStyle = STYLE_PRIMITIVE;    
	  } 
	  if( nStateInnerColor != null )
	  {
	     StateInnerColor = new int[nStateInnerColor.length];
	     StateOuterColor = new int[nStateOuterColor.length];
	     
	     for( i = 0; i < nStateInnerColor.length; i++ )
	     {
	       StateInnerColor[i] = nStateInnerColor[i];
	       StateOuterColor[i] = nStateOuterColor[i];  
	     }
	  }
	  else
	  {
	     StateInnerColor = new int[nMaxStates];
	     StateOuterColor = new int[nMaxStates];  
	     
	     for( i = 0; i < MaxStates; i++ )
	     {
	       StateInnerColor[i] = Color.rgb(100,100,100);
	       StateOuterColor[i] = Color.rgb(255, 0, 0);     
	     }
	  }
	  
	  TextLayer = GE.LAYER_2;
	  PictureLayer = GE.LAYER_1;
	  
	  TilePictureIndex_TileX = TilePictureIndex_TileY = null;
	}
	//-------------------------------------------------------------------------------------
    public void SetMaxStates( int nMaxStates )
    {    	
    	MaxStates = nMaxStates;	
    }
    //-------------------------------------------------------------------------------------
    public void SetButtonState( int nButtonState )
    {    	
      ButtonState = nButtonState;
    }
    //-------------------------------------------------------------------------------------    
	public int GetButtonState()
	{
	   return ButtonState;
	}
	//-------------------------------------------------------------------------------------
	protected boolean CheckClickWithin()
	{
	boolean returnvalue = false;

	if( X <= MouseX && MouseX < X + Width &&
	    Y <= MouseY && MouseY < Y + Height )
	    returnvalue = true;
	return returnvalue;    
	}
	//-------------------------------------------------------------------------------------	
	protected void DrawState()
	{
if( DrawAltFlag == 1 )
{
  GameGlobals.DrawTileImageOne( TilePictureIndex, (int)X, (int)Y, PictureLayer, AlphaValue, TilePictureIndex_TileX[ButtonState], 
		   TilePictureIndex_TileY[ButtonState], TileWidthPixels, TileHeightPixels );	
}
else
{
	if( DrawStyle == STYLE_PRIMITIVE )
	{
	  GE.DrawBox( X, Y, X+Width, Y+Height, PictureLayer, StateOuterColor[ButtonState], 
	    GameEngine.G_STYLE_FILL );
	  GE.DrawBox( X+2, Y+2, X+Width-2, Y+Height-2, PictureLayer, StateInnerColor[ButtonState],
	    GameEngine.G_STYLE_FILL );
	  GE.DrawTextColor( CaptionList[ButtonState],(int)X+5, (int)Y+Height-10, 
	    TextLayer, StateOuterColor[ButtonState], FontSize );
	}
	else
	if( DrawStyle == STYLE_PICTURE )
	{
	  if( TilePictureIndex_TileX != null )
	      GameGlobals.DrawTileImageGeneralUltra( TilePictureIndex, (int)X, (int)Y, PictureLayer, AlphaValue, 
	    		    TilePictureIndex_TileX[ButtonState], TilePictureIndex_TileY[ButtonState], 
	    	    	PictureWidthCells, PictureHeightCells, TileWidthPixels, TileHeightPixels );  		  
	  else
	     GE.DrawImage( StatePictureIndex[ButtonState], (int)X, (int)Y, PictureLayer );    
	}
}

	}
	//-------------------------------------------------------------------------------------
	public void Draw()
	{  
	  DrawState();
	}
	//-------------------------------------------------------------------------------------
	public void OnClick() 
	{
		int YesFlag = 0;
		
		if( (MouseStatus == ME_PRESS_DOWN || MouseStatus == ME_MOVE )  && CheckClickWithin() )
		{
			if( InputDelayMax > 0 )
			{
			  if( InputDelayCounter >= InputDelayMax )
			  {		
	            YesFlag = 1;
		        InputDelayCounter = 0;
			  }

			}
			else
				YesFlag = 1;

		   if( YesFlag == 1)
		   {
	        ButtonState++;
	        if( ButtonState > MaxStates - 1 )
	           ButtonState = 0;
	        OnClickDo();
	        CopyDupInput();		
		   }	  
		}
		else 
		if( MouseStatus == ME_RELEASE )
		{  
		    OnUnClickDo();	    
		}
	}
	//-------------------------------------------------------------------------------------
	public void InstantClick()
	{
	  ButtonState++;
	  if( ButtonState > MaxStates - 1 )
	      ButtonState = 0;
	}
	//-------------------------------------------------------------------------------------	
	protected void OnClickDo()
	{
	}
	//-------------------------------------------------------------------------------------
	protected void OnUnClickDo()
	{
	}
	//-------------------------------------------------------------------------------------
	public void Do()
	{
	  if( InputDelayCounter < InputDelayMax )
	      InputDelayCounter++;	
	}
	//-------------------------------------------------------------------------------------		
	public void SetTileStyle( int nTilePictureIndex, int[] nTilePictureIndex_TileX,
			 int[] nTilePictureIndex_TileY,  
			 int nPictureWidthCells, int nPictureHeightCells, int nTileWidthPixels,
			 int nTileHeightPixels, int nAlphaValue )
	{
	
	  int i;
	  
	  TilePictureIndex = nTilePictureIndex;
	  TilePictureIndex_TileX = new int[MaxStates];
	  TilePictureIndex_TileY = new int[MaxStates];
	  
	  for( i = 0; i < MaxStates; i++ )
	  {
		TilePictureIndex_TileX[i] = nTilePictureIndex_TileX[i];
		TilePictureIndex_TileY[i] = nTilePictureIndex_TileY[i];		
	  }
	  
	  PictureWidthCells = nPictureWidthCells;
	  PictureHeightCells = nPictureHeightCells;
	  TileWidthPixels = nTileWidthPixels;
	  TileHeightPixels = nTileHeightPixels;
	  AlphaValue = nAlphaValue;
	}
	//-------------------------------------------------------------------------------------	
public MultiStateButton( int nX, int nY, int nWidth, int nHeight, int nGroupId, int nUniqueId, int nPictureLayer, int nTextLayer,
        int nMaxStates, int nPictureIndex, int nRowStartIndex, int nAlphaValue )
{
   super();
   ClassType[TYPE_MULTI_STATE_BUTTON] = 1;
   Name = new String("Multistate Button");
   X = nX; Y = nY; Width = nWidth; Height = nHeight;
   ButtonState = 0;
   MaxStates = nMaxStates;
   MouseEventNotifyFlag = true;	  
   DrawStyle = STYLE_PICTURE; 

   Width = nWidth; Height = nHeight;
   X = nX; Y = nY; GroupId = nGroupId; UniqueId = nUniqueId;
   PictureLayer = nPictureLayer; TextLayer = nTextLayer;

   int i;
   int x = 0, y = nRowStartIndex;
   int[] TileX = new int[MaxStates];
   int[] TileY = new int[MaxStates];

   for( i = 0; i < MaxStates; i++ )
   {
      TileX[i] = x; TileY[i] = y + i;
   }
   
int ScaledWidth = (int)(GameControl.ImageScaleRelative_320x480[nPictureIndex] * Width);
int ScaledHeight = (int)(GameControl.ImageScaleRelative_320x480[nPictureIndex] * Height);   

   SetTileStyle( nPictureIndex, TileX, TileY, 1, 1, ScaledWidth, ScaledHeight, nAlphaValue );

   DrawAltFlag = 1;
}
//-------------------------------------------------------------------------------------	
	}
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
class SimpleBackGround extends GameObject
{

  public final static int STYLE_FILL_COLOR = 1;
  public final static int STYLE_PICTURE_TILED = 2;
  public final static int STYLE_PICTURE_CENTER = 3;
  public final static int BACKGROUND_TILE_WIDTH_PIXELS = 96;
  public final static int BACKGROUND_TILE_HEIGHT_PIXELS = 96;

  int Style;
  int FillColor;
  int PictureIndex;
  int PictureWidth, PictureHeight;

  protected int PictureIndex_TileX, PictureIndex_TileY;
  protected int PictureWidthCells, PictureHeightCells;
  protected int TileWidthPixels, TileHeightPixels;
  protected int AlphaValue;
  
//------------------------------------------------------------------------------------- 
public SimpleBackGround()
{
  super();
  ClassType[TYPE_SIMPLE_BACKGROUND] = 1;
  PictureLayer = GE.LAYER_0;
  TextLayer = GE.LAYER_1;
  FillColor = Color.BLUE;
  Style = STYLE_FILL_COLOR ;
  PictureIndex = GameControl.BACKGROUND_330x490_0;
  PictureIndex_TileX = PictureIndex_TileY = 0;
  
  SetTileStyle( GameControl.BACKGROUND_330x490_0, 0, 0, 1, 1, BACKGROUND_TILE_WIDTH_PIXELS, 
		  BACKGROUND_TILE_HEIGHT_PIXELS, 255 );
}
//-------------------------------------------------------------------------------------
public void SetProperties( int nStyle, int nFillColor, int nPictureIndex, 
        int nPictureWidth, int nPictureHeight )
{
  Style = nStyle; FillColor = nFillColor; PictureIndex = nPictureIndex; 
  PictureWidth = nPictureWidth; PictureHeight = nPictureHeight;
}                   
//-------------------------------------------------------------------------------------
public void Draw()
{
  int ix, iy;

  if( Style == STYLE_FILL_COLOR )
  {
     GE.DrawBox( 0, 0, ScreenWidth, ScreenHeight, PictureLayer, FillColor, 
	      GameEngine.G_STYLE_FILL );   
  }
  else
  if( Style == STYLE_PICTURE_TILED )
  { 
	if( PictureIndex_TileX > -1 )
	{
	 for( iy = 0; iy < ScreenHeight; iy += (PictureHeight-1) )
	   for( ix = 0; ix < ScreenWidth; ix += (PictureWidth-1) )		
	    GameGlobals.DrawTileImageOne( PictureIndex, ix, iy, PictureLayer, AlphaValue, 
	      PictureIndex_TileX, PictureIndex_TileY, TileWidthPixels, TileHeightPixels );  
	}
	else
	{
      for( iy = 0; iy < ScreenHeight; iy += (PictureHeight-1) )
       for( ix = 0; ix < ScreenWidth; ix += (PictureWidth-1) )
         GE.DrawImage( PictureIndex, ix, iy, PictureLayer );     
	}
  }
  else
  if( Style == STYLE_PICTURE_CENTER )
  {
    ix = ScreenWidth/2 - PictureWidth/2;
    iy = ScreenHeight/2 - PictureHeight/2;
    
    GE.DrawImage( PictureIndex, ix, iy, PictureLayer );  
  }
}
//-------------------------------------------------------------------------------------
public void SetTileStyle( int nPictureIndex, int nPictureIndex_TileX, int nPictureIndex_TileY, 
		 int nPictureWidthCells, int nPictureHeightCells, int nTileWidthPixels,
		 int nTileHeightPixels, int nAlphaValue )
{
PictureIndex =  nPictureIndex;  
PictureIndex_TileX =  nPictureIndex_TileX;
PictureIndex_TileY = nPictureIndex_TileY;
PictureWidthCells = nPictureWidthCells; 	 
PictureHeightCells = nPictureHeightCells;
TileWidthPixels = nTileWidthPixels;
TileHeightPixels =nTileHeightPixels;
AlphaValue = nAlphaValue;
}
//------------------------------------------------------------------------------------- 
}
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
class MenuCursorParallelia extends GameObject
{  

public static int
  MENU_A = 0, MENU_B = 1, MENU_C = 2, MENU_C2 = 3, MENU_D = 4, MENU_D_SUB = 5, MENU_E = 6, MENU_F = 7, MENU_G = 8, MENU_H = 9;

int MenuIndex, TilePictureIndex, AlphaValue;
int[] Menu_PosX, Menu_PosY, MenuItemSize;

public static int PIXELS_16 = 16, PIXELS_32 = 32, PIXELS_64 = 64, PIXELS_128 = 128;
public static int PIXELS_48 = 48, PIXELS_96 = 96, PIXELS_144 = 144, PIXELS_192 = 192;
public static int CELLS_1 = 1, CELLS_2 = 2, CELLS_3 = 3, CELLS_4 = 4;
int MenuSelectionIndex, MaxMenuSelectionIndex;

public final static int CURSOR_48x48 = 1, CURSOR_96x48 = 2, CURSOR_144x48 = 3, CURSOR_192x48 = 4;
protected static int CursorWidthPixels, CursorHeightPixels;

//-------------------------------------------------------------------------------------   
public MenuCursorParallelia( int nMenuIndex )
	{
	  super();
	  ClassType[TYPE_MENU_CURSOR_PARALLELIA] = 1;
	  Name = new String("Menu Cursor Parallelia");
	  X = 0; Y = 0;
	  KeyEventNotifyFlag = true;
	  
      MenuIndex = nMenuIndex;	        	  	  
	  TextLayer = GE.LAYER_4;
	  PictureLayer = GE.LAYER_4;
      
      SetTileStyle();
	}   
	//------------------------------------------------------------------------------------- 
	protected void DrawState()
	{
       int i = MenuSelectionIndex, x_shift = 0;
       
       switch( MenuItemSize[i] )
       {
        case CURSOR_48x48:
           break;
        case CURSOR_96x48:
           x_shift = 96/2 - 48/2;
           break;
        case CURSOR_144x48:
           x_shift = 144/2 - 48/2;
           break;
        case CURSOR_192x48:
           x_shift = 192/2 - 48/2;
           break;
        default:
           break;
       }
       GameGlobals.DrawTileImageOne( TilePictureIndex, Menu_PosX[i] + x_shift, Menu_PosY[i], PictureLayer, AlphaValue, 
    		0, 0, CursorWidthPixels, CursorHeightPixels );
	    
	}
	//-------------------------------------------------------------------------------------
	public void Draw()
	{  
	  DrawState();
	}
	//-------------------------------------------------------------------------------------
    protected void HandleKeyDown()
    {
     int IndexChange = 0;
    
        switch( KeyCode )
        {
          case KeyEvent.KEYCODE_DPAD_DOWN:
            IndexChange++;
            break;
          case KeyEvent.KEYCODE_DPAD_LEFT:
            IndexChange--;
            break;
          case KeyEvent.KEYCODE_DPAD_RIGHT:
            IndexChange++;
            break;
          case KeyEvent.KEYCODE_DPAD_UP:
            IndexChange--;
            break;
          case KeyEvent.KEYCODE_BACK:
            break;
          case KeyEvent.KEYCODE_ENTER:
          case KeyEvent.KEYCODE_DPAD_CENTER:
            MouseX = Menu_PosX[MenuSelectionIndex] + PIXELS_16; 
            MouseY = Menu_PosY[MenuSelectionIndex] + PIXELS_16;
            MouseStatus = ME_PRESS_DOWN;
            GE.TheControlManager.DoClickEvent( MouseStatus, MouseX, MouseY );
            break;
          default:
            break;
        }

      if( InputDelayCounter >= InputDelayMax )
      {
         InputDelayCounter = 0;
         MenuSelectionIndex += IndexChange;
      }
 
      if( MenuSelectionIndex < 0 )
          MenuSelectionIndex = 0;   
      else
      if( MenuSelectionIndex > MaxMenuSelectionIndex )
          MenuSelectionIndex = MaxMenuSelectionIndex;
    }
    //-------------------------------------------------------------------------------------
    protected void HandleKeyUp()
    {
     int IndexChange = 0;
    
        switch( KeyCode )
        {
          case KeyEvent.KEYCODE_DPAD_DOWN:
          case KeyEvent.KEYCODE_DPAD_LEFT:
          case KeyEvent.KEYCODE_DPAD_RIGHT:
          case KeyEvent.KEYCODE_DPAD_UP:
            break;
          case KeyEvent.KEYCODE_BACK:
            break;
          case KeyEvent.KEYCODE_ENTER:
          case KeyEvent.KEYCODE_DPAD_CENTER:
            MouseX = Menu_PosX[MenuSelectionIndex] + PIXELS_16; 
            MouseY = Menu_PosY[MenuSelectionIndex] + PIXELS_16;
            MouseStatus = ME_RELEASE;
            GE.TheControlManager.DoClickEvent( MouseStatus, MouseX, MouseY );
            break;
          default:
            break;
        }

         InputDelayCounter = InputDelayMax;
    }
    //-------------------------------------------------------------------------------------    
	public void OnKey() 
	{ 
      if( KeyStatus == ME_KEY_DOWN )
         HandleKeyDown();
      else
      if( KeyStatus == ME_KEY_UP )
         HandleKeyUp();  
	}
	//-------------------------------------------------------------------------------------
	public void Do()
	{
	  if( InputDelayCounter < InputDelayMax )
	      InputDelayCounter++;	         
	}
	//-------------------------------------------------------------------------------------	
	protected void InitArrays( int length )
	{
        Menu_PosX = new int[length];
        Menu_PosY = new int[length];
        MenuItemSize = new int[length];
	}
	//-------------------------------------------------------------------------------------	
	protected void InitCursor( int Index, int PosX, int PosY, int nMenuItemSize )
	{
        Menu_PosX[Index] = PosX; Menu_PosY[Index] = PosY;
        MenuItemSize[Index] = nMenuItemSize;	
	}
	//-------------------------------------------------------------------------------------		
	protected void SetTileStyle()
	{
	  MenuSelectionIndex = 0;
      TilePictureIndex = GameControl.IMAGE_MENU_CURSOR;
      
      CursorWidthPixels = (int)(PIXELS_48 * GameControl.ImageScaleRelative_320x480[GameControl.IMAGE_MENU_CURSOR]);
      CursorHeightPixels =(int)(PIXELS_48 * GameControl.ImageScaleRelative_320x480[GameControl.IMAGE_MENU_CURSOR]);
 
      if( MenuIndex == MENU_A )
      {
        MaxMenuSelectionIndex = 3;
        InitArrays( MaxMenuSelectionIndex+1 );
        
        InitCursor( 0, 65,193, CURSOR_192x48);
        InitCursor( 1, 65,256, CURSOR_192x48);
        InitCursor( 2, 65,322, CURSOR_192x48); 
        InitCursor( 3, 65,322+66, CURSOR_192x48);  
      }
      else
      if( MenuIndex == MENU_B )
      {
        MaxMenuSelectionIndex = 2;
        InitArrays( MaxMenuSelectionIndex+1 );
        
        InitCursor( 0, 65,193,  CURSOR_192x48);
        InitCursor( 1, 65,256,  CURSOR_192x48);
        InitCursor( 2, 65,322,  CURSOR_192x48);          
      }
      else
      if( MenuIndex == MENU_C )
      {    	  
        MaxMenuSelectionIndex = 8;
        InitArrays( MaxMenuSelectionIndex+1 );
        
       InitCursor( 0, 212,81,  CURSOR_48x48);
       InitCursor( 1, 267,81, CURSOR_48x48);
       InitCursor( 2, 212,146, CURSOR_48x48);
       InitCursor( 3, 267,146,  CURSOR_48x48);
       InitCursor( 4, 238,211, CURSOR_48x48);
       InitCursor( 5, 238,275, CURSOR_48x48);

       InitCursor( 6, 214,342, CURSOR_48x48);
       InitCursor( 7, 267,342,  CURSOR_48x48);
       
       InitCursor( 8, 109,419,  CURSOR_96x48);   	  
      }
      else
      if( MenuIndex == MENU_D )
      {    	  
        MaxMenuSelectionIndex = 4;
        InitArrays( MaxMenuSelectionIndex+1 );  
        
        InitCursor( 0, 40,357,  CURSOR_48x48);
        InitCursor( 1, 100,357,  CURSOR_48x48);
        InitCursor( 2, 160,357, CURSOR_48x48);
        InitCursor( 3, 220,357, CURSOR_48x48);
        InitCursor( 4, 104,416, CURSOR_96x48);        
      }
      else
      if( MenuIndex == MENU_D_SUB )
      {    	  
        MaxMenuSelectionIndex = 0;
        InitArrays( MaxMenuSelectionIndex+1 ); 
        InitCursor( 0, 104,200,  CURSOR_96x48);
      }      
      else
      if( MenuIndex == MENU_E )
      {    	  
        MaxMenuSelectionIndex = 2;
        InitArrays( MaxMenuSelectionIndex+1 );   
        InitCursor( 0, 13,419,  CURSOR_48x48);
        InitCursor( 1, 109,419, CURSOR_96x48);
        InitCursor( 2, 253,419,  CURSOR_48x48);        
      }
      else
      if( MenuIndex == MENU_F )
      {    	  
      }
      else
      if( MenuIndex == MENU_G )
      {
        MaxMenuSelectionIndex = 0;
        InitArrays( MaxMenuSelectionIndex+1 ); 
        InitCursor( 0, 109,419,  CURSOR_96x48);    	  
      }
      else
      if( MenuIndex == MENU_H )
      {
        MaxMenuSelectionIndex = 2;
        InitArrays( MaxMenuSelectionIndex+1 );   
        InitCursor( 0, 109,419, CURSOR_96x48);
        InitCursor( 1, 109+96,419,  CURSOR_48x48);       
        InitCursor( 2, 109+96+48,419,  CURSOR_48x48);    	 
      }
      
      AlphaValue = 200;
	}
	//-------------------------------------------------------------------------------------	
}
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
class ControlManager
{
  protected GameObject[] ControlList;
  protected final static int CONTROL_LIST_LENGTH = 200;
  protected int i;
  
  MenuCursorParallelia ItsMenuCursorParallelia = null;
  
  GameGlobals gg;
  GameEngine GE;
  GameOptions gop;
  protected int CaptionDistanceBetweenLines = 15;
  
  int DoneLoadingFlag = 0;
  CaptionBox EulaCaptionBox = null;
  
  public static int PauseFlag = 0;

//-------------------------------------------------------------------------------------
  public ControlManager()
  {
	ControlList = new GameObject[CONTROL_LIST_LENGTH];
	gg = new GameGlobals();
	gg.GE = GameEngine.CurrentObj;
	GE = GameEngine.CurrentObj;
	GameOptions.Load();	
  }
//-------------------------------------------------------------------------------------  
  protected void AddControl( GameObject obj )
  {
    for( i = 0; i < CONTROL_LIST_LENGTH; i++ )
      if( ControlList[i] == null )
      {
          ControlList[i] = obj;
          break;
      }    
  }  
//-------------------------------------------------------------------------------------    
  // *** replaceable in descendant class ***
  public void CreateMenuA()
  {	  
	  //GemstonedActivity.ChangeAdPosition(0);
	  
	  Caption Caption1;
	  Button Button1;	  
	  Destroy(0, 0);
	  
	  int i, x, y;
	  
      Caption1 = new Caption( 19 + 50, 65 + gg.Y_SHIFT, gg.GROUP_ID_MENU_A, gg.UNIQUE_ID_CAPTION_GENERAL, Caption.STYLE_PICTURE, GE.LAYER_2, GE.LAYER_3 );
      Caption1.Init_2(  GameControl.IMAGE_MAIN_TITLE, 320, 480, 0, 255, 0 );
	  AddControl(Caption1);	
/*	  
	  for( i = 0; i < 5; i++ )
	  {
		x = 19 + GameGlobals.random( -SwapCellGrid.TILE_WIDTH_PIXELS, SwapCellGrid.TILE_WIDTH_PIXELS );
		y = 65 + i * GameGlobals.random( -SwapCellGrid.TILE_WIDTH_PIXELS, SwapCellGrid.TILE_WIDTH_PIXELS );
	    AddControl( new SwapCellDemoDisplay( x, y, 1 ) );
	  }
*/	  
	  Button1 = new Button( "Start", 0,0,0,0 );
	  Button1.Create_WidthxHeight( 65, 193 + gg.Y_SHIFT, 192, 48, gg.GROUP_ID_MENU_A, gg.UNIQUE_ID_BUTTON_START, GE.LAYER_2, GE.LAYER_1, 
        GameControl.IMAGE_TILES_4x1, 0, 8, 255 );
      AddControl(Button1);
/*
	  Button1 = new Button( "Continue", 0,0,0,0 );
	  Button1.Create_WidthxHeight( 65, 193 + 63 * 1 + gg.Y_SHIFT, 192, 48, gg.GROUP_ID_MENU_A, gg.UNIQUE_ID_BUTTON_CONTINUE, GE.LAYER_2, GE.LAYER_1, 
        GameControl.IMAGE_TILES_4x1, 1, 8, 255 );
      AddControl(Button1);            
 */          
      Button1 = new Button( "Options", 0,0,0,0 );
	  Button1.Create_WidthxHeight( 65, 193 + 63 * 1 + gg.Y_SHIFT, 192, 48, gg.GROUP_ID_MENU_A, gg.UNIQUE_ID_BUTTON_OPTIONS, GE.LAYER_2, GE.LAYER_1, 
        GameControl.IMAGE_TILES_4x1, 2, 8, 255 );
      AddControl(Button1);
	 
      Button1 = new Button( "High Scores", 0,0,0,0 );
	  Button1.Create_WidthxHeight( 65, 193 + 63 * 2 + gg.Y_SHIFT, 192, 48, gg.GROUP_ID_MENU_A, gg.UNIQUE_ID_BUTTON_HIGH_SCORES, GE.LAYER_2, GE.LAYER_1, 
        GameControl.IMAGE_TILES_4x1, 3, 8, 255 );
      AddControl(Button1);

/*      
      Button1 = new Button( "About", 0,0,0,0 );
	  Button1.Create_WidthxHeight( 65, 322+66, 192, 48, gg.GROUP_ID_MENU_A, gg.UNIQUE_ID_BUTTON_ABOUT_EULA, GE.LAYER_2, GE.LAYER_1, 
        GameControl.IMAGE_TILES_4x1, 6, 6, 255 );
      AddControl(Button1);
*/      	  
      //SetGameObjectImages();
      
      CopyRightCaption crc = new CopyRightCaption();
      AddControl(crc);
      
      GameOptions.Load();
      GameGlobals.ChangeBackGround();
      
      CellFountainSpray cfs = new CellFountainSpray();
      AddControl(cfs);
      
      if( GameGlobals.TVModeFlag > 0 )
          ItsMenuCursorParallelia = new MenuCursorParallelia( MenuCursorParallelia.MENU_A );
  }
//-------------------------------------------------------------------------------------
  // *** replaceable in descendant class ***
  public void CreateMenuB()
  {	
	  Caption Caption1;
	  Button Button1;	  
	  Destroy(0, 0);
	  
	  int x, y, i;
/*	  
      Caption1 = new Caption( 19 + 50, 65 + gg.Y_SHIFT, gg.GROUP_ID_MENU_B, gg.UNIQUE_ID_CAPTION_GENERAL, Caption.STYLE_PICTURE, GE.LAYER_2, GE.LAYER_3 );
      Caption1.Init_2(  GameControl.IMAGE_MAIN_TITLE, 320, 480, 0, 255, 0 );
	  AddControl(Caption1);	
	  
	  for( i = 0; i < 5; i++ )
	  {
		x = 19 + GameGlobals.random( -SwapCellGrid.TILE_WIDTH_PIXELS, SwapCellGrid.TILE_WIDTH_PIXELS );
		y = 65 + i * GameGlobals.random( -SwapCellGrid.TILE_WIDTH_PIXELS, SwapCellGrid.TILE_WIDTH_PIXELS );
	    AddControl( new SwapCellDemoDisplay( x, y, 1 ) );
	  }
	  
      Button1 = new Button( "Normal", 0,0,0,0 );
	  Button1.Create_WidthxHeight( 65, 193 + gg.Y_SHIFT, 192, 48, gg.GROUP_ID_MENU_B, gg.UNIQUE_ID_BUTTON_GAME_MODE_NORMAL, GE.LAYER_2, GE.LAYER_1, 
        GameControl.IMAGE_TILES_4x1, 4, 4, 255 );	  
      AddControl(Button1);
      
      Button1 = new Button( "Infinite", 0,0,0,0 );
	  Button1.Create_WidthxHeight( 65, 193+63*1 + gg.Y_SHIFT, 192, 48, gg.GROUP_ID_MENU_B, gg.UNIQUE_ID_BUTTON_GAME_MODE_INFINITE, GE.LAYER_2, GE.LAYER_1, 
        GameControl.IMAGE_TILES_4x1, 5, 5, 255 );	            	  
      AddControl(Button1);
      
      Button1 = new Button( "Count Down", 0,0,0,0 );
	  Button1.Create_WidthxHeight( 65, 193+63*2 + gg.Y_SHIFT, 192, 48, gg.GROUP_ID_MENU_B, gg.UNIQUE_ID_BUTTON_GAME_MODE_COUNTDOWN, GE.LAYER_2, GE.LAYER_1, 
        GameControl.IMAGE_TILES_4x1, 6, 6, 255 );	            	  
      AddControl(Button1);   
      
      Button1 = new Button( "Special", 0,0,0,0 );
	  Button1.Create_WidthxHeight( 65, 193+63*3 + gg.Y_SHIFT, 192, 48, gg.GROUP_ID_MENU_B, gg.UNIQUE_ID_BUTTON_GAME_MODE_SPECIAL, GE.LAYER_2, GE.LAYER_1, 
        GameControl.IMAGE_TILES_4x1, 7, 7, 255 );	            	  
      AddControl(Button1);          
                       
      if( GameGlobals.TVModeFlag > 0 )
          ItsMenuCursorParallelia = new MenuCursorParallelia( MenuCursorParallelia.MENU_B );
*/          
  }
//-------------------------------------------------------------------------------------
  // *** replaceable in descendant class ***
  public void CreateMenuC()
  {	  
	  Caption Caption1;
	  Button Button1;	  
	  CheckBox CheckBox1;
	  MultiStateButton MultiStateButton1;
	  Destroy(0, 0);
	  
      Caption1 = new Caption( 63, 10, gg.GROUP_ID_MENU_C, gg.UNIQUE_ID_CAPTION_GENERAL, Caption.STYLE_TEXT, -1, -1 );
      Caption1.Init_1( "Options", gg.HeaderFontSize1, gop.MainFontColor, 49, 20, false, 15 ); 
	  AddControl(Caption1);	

	  Caption1 = new Caption( 20, 81 + 48/3 + gg.Y_SHIFT, gg.GROUP_ID_MENU_C, gg.UNIQUE_ID_CAPTION_OPTION_AREA_SELECTION, Caption.STYLE_TEXT, -1, -1 );
      Caption1.Init_1( "Area Select ", gg.RegularFontSize1, gop.MainFontColor, 10, 20, false, 15 ); 
	  AddControl(Caption1);		
	  
      Caption1 = new Caption( 20, 81 + 67*1 + 48/3 + gg.Y_SHIFT, gg.GROUP_ID_MENU_C, gg.UNIQUE_ID_CAPTION_OPTION_SHIP_SELECTION, Caption.STYLE_TEXT, -1, -1 );
      Caption1.Init_1( "Ship Select ", gg.RegularFontSize1, gop.MainFontColor, 10, 20, false, 15 ); 
	  AddControl(Caption1);		    
	  
      Caption1 = new Caption( 20, 81 + 67*2 + 48/3 + gg.Y_SHIFT, gg.GROUP_ID_MENU_C, gg.UNIQUE_ID_CAPTION_OPTION_TILES_SELECTION, Caption.STYLE_TEXT, -1, -1 );
      Caption1.Init_1( "Tiles Select ", gg.RegularFontSize1, gop.MainFontColor, 10, 20, false, 15 ); 
	  AddControl(Caption1);	
	  
	  PlayerShip ps = new PlayerShip( null );
	  ps.GroupId = gg.GROUP_ID_MENU_C;
	  ps.X = 238 - 55; ps.Y = 81+67*1 + 48/3 + gg.Y_SHIFT + 12;
	  AddControl(ps);
	  
	  Cell pcell;
	  
	  pcell = new Cell( 1, 156, 233 );
	  pcell.SpecialFlag = Cell.FLAG_DEMO;
	  AddControl(pcell);
	  pcell = new Cell( 2, 156 + 20, 233 );
	  pcell.SpecialFlag = Cell.FLAG_DEMO;
	  AddControl(pcell);
	  pcell = new Cell( 3, 156, 233 + 20 );
	  pcell.SpecialFlag = Cell.FLAG_DEMO;
	  AddControl(pcell);
	  pcell = new Cell( 4, 156 + 20, 233 + 20 );
	  pcell.SpecialFlag = Cell.FLAG_DEMO;
	  AddControl(pcell);	  
	  
/*
      Caption1 = new Caption( 20, 148 + 67 + 48/3 + gg.Y_SHIFT, gg.GROUP_ID_MENU_C, gg.UNIQUE_ID_CAPTION_OPTION_GEM_TYPE_COUNT, Caption.STYLE_TEXT, -1, -1 );
      Caption1.Init_1( "Gem Count " + gop.GemTypeCount, 20, gop.MainFontColor, 10, 20, false, 15 ); 
	  AddControl(Caption1);			  
*/
      Caption1 = new Caption( 20, 81 + 67*3 + 48/3 + gg.Y_SHIFT, gg.GROUP_ID_MENU_C, gg.UNIQUE_ID_CAPTION_GENERAL, Caption.STYLE_TEXT, -1, -1 );
      Caption1.Init_1( "Sounds", gg.RegularFontSize1, gop.MainFontColor, 10, 20, false, 15 ); 
	  AddControl(Caption1);	  
/*
      Caption1 = new Caption( 20, 277 + 67 + gg.Y_SHIFT, gg.GROUP_ID_MENU_C, gg.UNIQUE_ID_CAPTION_GENERAL, Caption.STYLE_TEXT, -1, -1 );
      Caption1.Init_1( "Graphics Choice", 20, gop.MainFontColor, 10, 20, false, 15 ); 
	  AddControl(Caption1);	  	    

      Button1 = new Button( "+", 0,0,0,0 );
	  Button1.Create_WidthxHeight( 212, 81 + gg.Y_SHIFT, 48, 48, gg.GROUP_ID_MENU_C, gg.UNIQUE_ID_BUTTON_OPTION_INC_GRID_WIDTH, GE.LAYER_2, GE.LAYER_1, 
      GameControl.IMAGE_TILES_1x1, 16, 16, 255 );	  
      AddControl(Button1);      
      
      Button1 = new Button( "-", 0,0,0,0 );
	  Button1.Create_WidthxHeight( 267, 81 + gg.Y_SHIFT, 48, 48, gg.GROUP_ID_MENU_C, gg.UNIQUE_ID_BUTTON_OPTION_DEC_GRID_WIDTH, GE.LAYER_2, GE.LAYER_1, 
      GameControl.IMAGE_TILES_1x1, 17, 17, 255 );	  
      AddControl(Button1);    
      
      Button1 = new Button( "+", 0,0,0,0 );
	  Button1.Create_WidthxHeight( 212, 81 + 67 + gg.Y_SHIFT, 48, 48, gg.GROUP_ID_MENU_C, gg.UNIQUE_ID_BUTTON_OPTION_INC_GRID_HEIGHT, GE.LAYER_2, GE.LAYER_1, 
      GameControl.IMAGE_TILES_1x1, 16, 16, 255 );	  
      AddControl(Button1);      
      
      Button1 = new Button( "-", 0,0,0,0 );
	  Button1.Create_WidthxHeight( 267, 81 + 67 + gg.Y_SHIFT, 48, 48, gg.GROUP_ID_MENU_C, gg.UNIQUE_ID_BUTTON_OPTION_DEC_GRID_HEIGHT, GE.LAYER_2, GE.LAYER_1, 
      GameControl.IMAGE_TILES_1x1, 17, 17, 255 );	  
      AddControl(Button1);    
    
      Button1 = new Button( "+", 0,0,0,0 );
	  Button1.Create_WidthxHeight( 212, 81+63*1 + 67 + gg.Y_SHIFT, 48, 48, gg.GROUP_ID_MENU_C, gg.UNIQUE_ID_BUTTON_OPTION_INC_GEM_TYPE_COUNT, 
			  GE.LAYER_2, GE.LAYER_1, 
      GameControl.IMAGE_TILES_1x1, 16, 16, 255 );	  
      AddControl(Button1);    
    
      Button1 = new Button( "-", 0,0,0,0 );
	  Button1.Create_WidthxHeight( 267, 81+63*1 + 67 + gg.Y_SHIFT, 48, 48, gg.GROUP_ID_MENU_C,gg.UNIQUE_ID_BUTTON_OPTION_DEC_GEM_TYPE_COUNT, 
			  GE.LAYER_2, GE.LAYER_1, 
      GameControl.IMAGE_TILES_1x1, 17, 17, 255 );	  
      AddControl(Button1);           
*/      
    CheckBox1 = new CheckBox( 238, 81 + 67*3 + 48/3 + gg.Y_SHIFT, 48, 48, gg.GROUP_ID_MENU_C, gg.UNIQUE_ID_CHECKBOX_OPTION_SOUNDS, GE.LAYER_2, 
      GameControl.IMAGE_TILES_1x1, 19, 18, 255, false );
    
          Button1 = new Button( "Okay", 0,0,0,0 );
	  Button1.Create_WidthxHeight( 109, 419 + gg.Y_SHIFT, 96, 48, gg.GROUP_ID_MENU_C, gg.UNIQUE_ID_BUTTON_OPTION_OKAY, GE.LAYER_2, GE.LAYER_1, 
      GameControl.IMAGE_TILES_2x1, 0, 0, 255 );	  
      AddControl(Button1);    
 
	  if( gop.SoundsFlag > 0 )
		  CheckBox1.CheckedFlag = true;
	  else
		  CheckBox1.CheckedFlag = false;	  
	  AddControl(CheckBox1);
	    

    MultiStateButton1 = new MultiStateButton( 238, 81 + 48/3 + gg.Y_SHIFT,  48,48, gg.GROUP_ID_MENU_C, gg.UNIQUE_ID_MULTISTATE_BUTTON_OPTION_AREA_SELECTION,
            GE.LAYER_2, GE.LAYER_1, 10, GameControl.IMAGE_TILES_1x1, 0, 255); 
    MultiStateButton1.ButtonState = gop.AreaSelectionIndex;
    AddControl(MultiStateButton1);
    MultiStateButton1 = new MultiStateButton( 238, 81+67*1 + 48/3 + gg.Y_SHIFT,  48,48, gg.GROUP_ID_MENU_C, gg.UNIQUE_ID_MULTISTATE_BUTTON_OPTION_SHIP_SELECTION,
            GE.LAYER_2, GE.LAYER_1, 10, GameControl.IMAGE_TILES_1x1, 0, 255); 	
     MultiStateButton1.ButtonState = gop.ShipSelectionIndex; //gop.GemStyleIndex;
	AddControl(MultiStateButton1);

    MultiStateButton1 = new MultiStateButton( 238, 81+67*2 + 48/3 + gg.Y_SHIFT,  48,48, gg.GROUP_ID_MENU_C, gg.UNIQUE_ID_MULTISTATE_BUTTON_OPTION_TILES_SELECTION,
            GE.LAYER_2, GE.LAYER_1, 10, GameControl.IMAGE_TILES_1x1, 0, 255); 	
     MultiStateButton1.ButtonState = gop.TilesSelectionIndex;
	AddControl(MultiStateButton1);
	
/*	
	  Button1 = new Button(">>", 253, 419, 48, 48);
	  AddButton( 253, 419, 48, 48, gg.GROUP_ID_MENU_C, gg.UNIQUE_ID_BUTTON_OPTION_RIGHT, 24 );

	  BlockCell DemoBlockCell = new BlockCell();
	  DemoBlockCell.X_pos = 30; DemoBlockCell.Y_pos = 381;
	  AddControl(DemoBlockCell);
*/
/*	
	 SwapCellDemoDisplay scdd = new SwapCellDemoDisplay( 30, 381 + gg.Y_SHIFT, 0 );
	 scdd.UniqueId = GameGlobals.UNIQUE_ID_TYPE_SWAP_CELL_DEMO_DISPLAY_1;
	 AddControl(scdd);
*/	 
	  if( GameGlobals.TVModeFlag > 0 )
	      ItsMenuCursorParallelia = new MenuCursorParallelia( MenuCursorParallelia.MENU_C );
	  	  
  }
//-------------------------------------------------------------------------------------
  // *** replaceable in descendant class ***  
  public void CreateMenuD()
  {
	  Caption Caption1;
	  Button Button1;	  
	  Destroy(0, 0);
	  	  
	  
      Caption1 = new Caption( 20, 60, gg.GROUP_ID_MENU_D, gg.UNIQUE_ID_CAPTION_GENERAL, Caption.STYLE_TEXT, -1, -1 );
      Caption1.Init_1( "Game Over", gg.HeaderFontSize2, gop.MainFontColor, 40, 60, false, 15 ); 
	  AddControl(Caption1);
/*	  
	  if( HighScores.CurrentObj.CheckNewHighScore( GameOptions.CurrentGameType, GameOptions.CurrentPlayerScore ) >= 1 ) 
	  {		  
*/		        
      Caption1 = new Caption( 40, 140, gg.GROUP_ID_MENU_D, gg.UNIQUE_ID_CAPTION_GENERAL, Caption.STYLE_TEXT, -1, -1 );
      Caption1.Init_1( "New High Score!\n" + gop.CurrentPlayerScore, gg.HeaderFontSize1, gop.MainFontColor, 10, 40, true, 45 ); 
	  AddControl(Caption1);			    

      //GameOptions.CurrentPlayerName = "------------";      	    
      Caption1 = new Caption( 40, 300 + gg.Y_SHIFT, gg.GROUP_ID_MENU_D, gg.UNIQUE_ID_CAPTION_GAME_OVER_NAME, Caption.STYLE_TEXT, -1, -1 );
      Caption1.Init_1( GameOptions.CurrentPlayerName, gg.HeaderFontSize1, gop.MainFontColor, 10, 40, false, 15 ); 
      Caption1.DrawCursorFlag = true;
	  AddControl(Caption1);		    

       Button1 = new Button("<", 0,0,0,0);
       Button1.Create_WidthxHeight( 40, 357 + gg.Y_SHIFT, 48, 48, gg.GROUP_ID_MENU_D, gg.UNIQUE_ID_BUTTON_GAME_OVER_LEFT, GE.LAYER_1, GE.LAYER_2 , 
       GameControl.IMAGE_TILES_1x1, 12, 12, 255 );		
	   Button1.InputDelayMax = 5;	  
       AddControl(Button1);     

       Button1 = new Button("^", 0,0,0,0);
       Button1.Create_WidthxHeight( 100, 357 + gg.Y_SHIFT, 48, 48, gg.GROUP_ID_MENU_D, gg.UNIQUE_ID_BUTTON_GAME_OVER_UP, GE.LAYER_1, GE.LAYER_2 , 
       GameControl.IMAGE_TILES_1x1, 11, 11, 255 );	  
	   Button1.InputDelayMax = 5;	  
       AddControl(Button1);

       Button1 = new Button("\\/", 0,0,0,0);
       Button1.Create_WidthxHeight( 160, 357 + gg.Y_SHIFT, 48, 48, gg.GROUP_ID_MENU_D, gg.UNIQUE_ID_BUTTON_GAME_OVER_DOWN, GE.LAYER_1, GE.LAYER_2 , 
       GameControl.IMAGE_TILES_1x1, 10, 10, 255 );	    
		  Button1.InputDelayMax = 5;	
        AddControl(Button1);
        
       Button1 = new Button(">", 0,0,0,0);
       Button1.Create_WidthxHeight( 220, 357 + gg.Y_SHIFT, 48, 48, gg.GROUP_ID_MENU_D, gg.UNIQUE_ID_BUTTON_GAME_OVER_RIGHT, GE.LAYER_1, GE.LAYER_2 , 
       GameControl.IMAGE_TILES_1x1, 13, 13, 255 );		
		  Button1.InputDelayMax = 5;	
        AddControl(Button1);    
        
       Button1 = new Button("Okay", 0,0,0,0);
       Button1.Create_WidthxHeight( 104, 416 + gg.Y_SHIFT, 96, 48, gg.GROUP_ID_MENU_D, gg.UNIQUE_ID_BUTTON_GAME_OVER_OKAY_ADD_SCORE, GE.LAYER_1, GE.LAYER_2 , 
       GameControl.IMAGE_TILES_2x1, 0, 0, 255 );	 		    
        AddControl(Button1);            
        
	  if( GameGlobals.TVModeFlag > 0 )
	      ItsMenuCursorParallelia = new MenuCursorParallelia( MenuCursorParallelia.MENU_D );       
/*	  
	  }
	  else
	  {		
		  
       Button1 = new Button("Okay", 0,0,0,0);
       Button1.Create_WidthxHeight( 104, 200, 96, 48, gg.GROUP_ID_MENU_D, gg.UNIQUE_ID_BUTTON_GAME_OVER_OKAY, GE.LAYER_1, GE.LAYER_2 , 
       GameControl.IMAGE_TILES_2x1, 0, 0, 255 );	    
       AddControl(Button1);           
        
	  if( GameGlobals.TVModeFlag > 0 )
	      ItsMenuCursorParallelia = new MenuCursorParallelia( MenuCursorParallelia.MENU_D_SUB );        
	  }
*/	  
  }
//-------------------------------------------------------------------------------------  
  // *** replaceable in descendant class ***
  public void CreateMenuE()
  {
	  Caption Caption1;
	  Button Button1;	  
	  Destroy(0, 0);	  
	  
      Caption1 = new Caption( 63, 10, gg.GROUP_ID_MENU_E, gg.UNIQUE_ID_CAPTION_GENERAL, Caption.STYLE_TEXT, -1, -1 );
      Caption1.Init_1( "High Scores", gg.HeaderFontSize1, gop.MainFontColor, 5, 20, false, 15 ); 
	  AddControl(Caption1);		  

      Caption1 = new Caption( 63, 70 + gg.Y_SHIFT/2, gg.GROUP_ID_MENU_E, gg.UNIQUE_ID_CAPTION_HIGH_SCORES_LIST, Caption.STYLE_TEXT, -1, -1 );
      Caption1.Init_1( "", gg.RegularFontSize1, gop.MainFontColor, 5, 10, true, 30 ); 
      //Caption1.SetText( gg.SelectHighScoreBoardtoString(0) );
      Caption1.SetText( HighScores.CurrentObj.CurrentList2String() );
	  AddControl(Caption1);		 

      Button1 = new Button("Okay", 0,0,0,0);
      Button1.Create_WidthxHeight( 109, 419 + gg.Y_SHIFT, 96, 48, gg.GROUP_ID_MENU_E, gg.UNIQUE_ID_BUTTON_HIGH_SCORES_OKAY, GE.LAYER_1, GE.LAYER_2 , 
      GameControl.IMAGE_TILES_2x1, 0, 0, 255 );	    
      AddControl(Button1);

       Button1 = new Button("<<", 0,0,0,0);
       Button1.Create_WidthxHeight( 13, 419 + gg.Y_SHIFT, 48, 48, gg.GROUP_ID_MENU_E, gg.UNIQUE_ID_BUTTON_HIGH_SCORES_LEFT, GE.LAYER_1, GE.LAYER_2 , 
       GameControl.IMAGE_TILES_1x1, 12, 12, 255 );	    
       AddControl(Button1);   	       

       Button1 = new Button(">>", 0,0,0,0);
       Button1.Create_WidthxHeight( 253, 419 + gg.Y_SHIFT, 48, 48, gg.GROUP_ID_MENU_E, gg.UNIQUE_ID_BUTTON_HIGH_SCORES_RIGHT, GE.LAYER_1, GE.LAYER_2 , 
       GameControl.IMAGE_TILES_1x1, 13, 13, 255 );	    
       AddControl(Button1);   	   

	  if( GameGlobals.TVModeFlag > 0 )
	      ItsMenuCursorParallelia = new MenuCursorParallelia( MenuCursorParallelia.MENU_E );      
	      
 }
//-------------------------------------------------------------------------------------  
// *** replaceable in descendant class ***
  public void CreateMenuF( int option )
  {	  	  
/*	  
	  SwapCellGridController scgc = new SwapCellGridController();
	  scgc.GroupId = gg.GROUP_ID_MENU_F;

	  if( option == 1 )
	  {		
		if( scgc.DoFile(scgc.LOAD_FILE) == 0 )
			return;
	  }
	  
	  Destroy(0, 0);
	  AddControl(scgc);
*/	  	 	  
	  Destroy( 0, 0 );
	  
      gop.BackGroundIndex = gop.AreaSelectionIndex;
      gg.ChangeBackGround();	  
	  
	  CellGrid cg = new CellGrid();
	  cg.GroupId = gg.GROUP_ID_MENU_F;
	  AddControl(cg);
	  
	  PlayerShip ps = new PlayerShip(cg);
	  ps.GroupId = gg.GROUP_ID_MENU_F;
	  AddControl(ps);
	  
	  PlayerControlPanel pcp = new PlayerControlPanel( ps, cg );
	  AddControl(pcp);
	  
	  ItsMenuCursorParallelia = null;
  }
//------------------------------------------------------------------------------------- 
// *** replaceable in descendant class ***
  public void CreateMenuG()
  {
	 Caption Caption1;
	 Button Button1;	  
	 Destroy(0, 0);
	 
       Button1 = new Button("Okay", 0,0,0,0);
       Button1.Create_WidthxHeight( 109, 419, 96, 48, gg.GROUP_ID_MENU_G, gg.UNIQUE_ID_BUTTON_ABOUT_OKAY, GE.LAYER_2, GE.LAYER_3 , 
       GameControl.IMAGE_TILES_2x1, 0, 0, 255 );	    
       AddControl(Button1);   	    
/*	 
      Caption1 = new Caption( 0, 0, gg.GROUP_ID_MENU_G, gg.UNIQUE_ID_CAPTION_GENERAL, Caption.STYLE_PICTURE, -1, -1 );
      Caption1.Init_2(  GameControl.IMAGE_ABOUT, 320, 480, 0, 255, 0 );
	  AddControl(Caption1);		      
*/      
	 if( GameGlobals.TVModeFlag > 0 )
	     ItsMenuCursorParallelia = new MenuCursorParallelia( MenuCursorParallelia.MENU_G );  	  	  
  }
//-------------------------------------------------------------------------------------   
// *** replaceable in descendant class ***
  public void Init() 
  { 	 
      GameGlobals.BackDrop = new SimpleBackGround();

      GameGlobals.BackDrop.SetProperties( SimpleBackGround.STYLE_PICTURE_CENTER, Color.rgb(0,100,0), GameControl.BACKGROUND_330x490_0, 
        GameEngine.TARGET_SCREEN_WIDTH, GameEngine.TARGET_SCREEN_HEIGHT );	 
      
      GameGlobals.CalculateFontSize();
     CreateMenuA();
     gop.Load();
     GameGlobals.ChangeBackGround();
  }
//-------------------------------------------------------------------------------------  
  public void DoClickEvent( int TouchAction, int TouchX, int TouchY ) 
  {    
    if( PauseFlag == 1 )
    	return;
    
    GameObject.MouseStatus = TouchAction;
    GameObject.MouseX = TouchX;
    GameObject.MouseY = TouchY;

    for( i = 0; i < CONTROL_LIST_LENGTH; i++ ) 
      if( ControlList[i] != null )
       if( ControlList[i].MouseEventNotifyFlag )      
    	  ControlList[i].OnClick();  
  }
//-------------------------------------------------------------------------------------  
  public void DoKeyEvent( int KeyAction, int KeyCode )  
  {
    if( PauseFlag == 1 )
    	return;
    
    GameObject.KeyStatus = KeyAction;
    GameObject.KeyCode = KeyCode;
    
    if( ItsMenuCursorParallelia != null )
        ItsMenuCursorParallelia.OnKey();
         
    for( i = 0; i < CONTROL_LIST_LENGTH; i++ ) 
      if( ControlList[i] != null )
       if( ControlList[i].KeyEventNotifyFlag )      
    	  ControlList[i].OnKey();     
    

  }  
//-------------------------------------------------------------------------------------
  protected GameObject FindFirstControlById( int UniqueId )
  {	
	GameObject obj = null;
	
	for( i = 0; i < CONTROL_LIST_LENGTH; i++ )
	  if( ControlList[i] != null )
	   if( ControlList[i].UniqueId == UniqueId )
	   {
		  obj = ControlList[i];
		  break;
	   }
	
	return obj;
  }
//-------------------------------------------------------------------------------------  
// *** replaceable in descendant class ***
  protected void CheckControlListState() 
  {
    int UniqueId, GroupId;
    GameObject go;
    Caption cap;
    MultiStateButton msb;
        
    for( i = 0; i < CONTROL_LIST_LENGTH; i++ )
     if( ControlList[i] != null )
     {  
       go = ControlList[i];
       UniqueId = go.UniqueId;
       GroupId = go.GroupId;
       
       if( go.MouseStatus_Dup == go.ME_PRESS_DOWN ||
    	   go.MouseStatus_Dup == go.ME_MOVE )
       {
    	switch( UniqueId )
    	{
    	  //---------------------------------------------------------------------
    	case GameGlobals.UNIQUE_ID_BUTTON_START:    		 
              CreateMenuF(0);
            break;
          //---------------------------------------------------------------------            
    	case GameGlobals.UNIQUE_ID_BUTTON_OPTIONS:
        	CreateMenuC();
            break; 
          //---------------------------------------------------------------------        
    	case GameGlobals.UNIQUE_ID_BUTTON_HIGH_SCORES:     	
        	CreateMenuE();
            break;
          //---------------------------------------------------------------------  
        case GameGlobals.UNIQUE_ID_BUTTON_ABOUT:
        	CreateMenuG();
        	break;
          //---------------------------------------------------------------------       	
        case GameGlobals.UNIQUE_ID_BUTTON_ABOUT_OKAY:  	
        	CreateMenuA();
        	break;       	
          //---------------------------------------------------------------------          
    	case GameGlobals.UNIQUE_ID_CHECKBOX_OPTION_SOUNDS:    		

           if( ((CheckBox)go).CheckedFlag )
        	   gop.SoundsFlag = 1;
           else
        	   gop.SoundsFlag = 0;

           break;
         //---------------------------------------------------------------------                   
    	case GameGlobals.UNIQUE_ID_MULTISTATE_BUTTON_OPTION_AREA_SELECTION:
    	       		    		
           gop.BackGroundIndex = ((MultiStateButton)go).ButtonState;
           gop.AreaSelectionIndex = ((MultiStateButton)go).ButtonState;
           gg.ChangeBackGround();
           
           break;           
         //---------------------------------------------------------------------            
    	case GameGlobals.UNIQUE_ID_MULTISTATE_BUTTON_OPTION_SHIP_SELECTION:
           //gop.GemStyleIndex = ((MultiStateButton)go).ButtonState;   
    	   gop.ShipSelectionIndex = ((MultiStateButton)go).ButtonState; 	
           //SwapCellDemoDisplay scdd = (SwapCellDemoDisplay)FindFirstControlById(gg.UNIQUE_ID_TYPE_SWAP_CELL_DEMO_DISPLAY_1);
           //scdd.Update();
           //BlockCell.ChangeRow(gg.CellLookIndex); 
           break;
         //---------------------------------------------------------------------      
    	case GameGlobals.UNIQUE_ID_MULTISTATE_BUTTON_OPTION_TILES_SELECTION:
    		gop.TilesSelectionIndex = ((MultiStateButton)go).ButtonState; 
    		break;
         //---------------------------------------------------------------------     		
    	case GameGlobals.UNIQUE_ID_BUTTON_OPTION_OKAY:
    	   GameOptions.Save();
           CreateMenuA();        	
           break;
    	case GameGlobals.UNIQUE_ID_BUTTON_HIGH_SCORES_OKAY:
    		
           CreateMenuA();        	
           break;
         //---------------------------------------------------------------------           
    	case GameGlobals.UNIQUE_ID_BUTTON_HIGH_SCORES_LEFT:

        	cap = (Caption)FindFirstControlById(gg.UNIQUE_ID_CAPTION_HIGH_SCORES_LIST);
        	//cap.SetText( gg.SelectHighScoreBoardtoString(-1) );   
        	
        	HighScores.CurrentObj.ChangeListTypeIndex(-1);
            cap.SetText( HighScores.CurrentObj.CurrentList2String() );
            
           break;
         //---------------------------------------------------------------------           
    	case GameGlobals.UNIQUE_ID_BUTTON_HIGH_SCORES_RIGHT:

        	cap = (Caption)FindFirstControlById(gg.UNIQUE_ID_CAPTION_HIGH_SCORES_LIST);
        	//cap.SetText( gg.SelectHighScoreBoardtoString(1) );        
        	
        	HighScores.CurrentObj.ChangeListTypeIndex(1);
            cap.SetText( HighScores.CurrentObj.CurrentList2String() );
            
           break;
         //---------------------------------------------------------------------                    
    	case GameGlobals.UNIQUE_ID_BUTTON_GAME_OVER_OKAY_ADD_SCORE:
    	
    	   //GameGlobals.Add2HighScore( GameGlobals.CurrentPlayerName );
    	   //GameGlobals.SaveOptionsData();
    		
    	   HighScores.CurrentObj.Add2HighScore( GameOptions.AreaSelectionIndex, GameOptions.CurrentPlayerName, 
    			GameOptions.CurrentPlayerScore );
    	   HighScores.CurrentObj.SaveHighScores();
           CreateMenuA();           
           break;    
         //---------------------------------------------------------------------
    	case GameGlobals.UNIQUE_ID_BUTTON_GAME_OVER_LEFT:  
    	   GameOptions.EditCurrentPlayerName( -1, 0 );
       	   cap = (Caption)FindFirstControlById(gg.UNIQUE_ID_CAPTION_GAME_OVER_NAME);
           cap.SetText( GameOptions.CurrentPlayerName );      
           cap.CursorCharPosition = GameOptions.CurrentPlayerNameCursorPosition;
    	   break;
         //---------------------------------------------------------------------      	   
    	case GameGlobals.UNIQUE_ID_BUTTON_GAME_OVER_DOWN: 
    		GameOptions.EditCurrentPlayerName( 0, 1 );
        	cap = (Caption)FindFirstControlById(gg.UNIQUE_ID_CAPTION_GAME_OVER_NAME);
            cap.SetText(GameOptions.CurrentPlayerName );     		
    	   break;
         //---------------------------------------------------------------------     	   
    	case GameGlobals.UNIQUE_ID_BUTTON_GAME_OVER_UP: 
    		GameOptions.EditCurrentPlayerName( 0, -1 );
        	cap = (Caption)FindFirstControlById(gg.UNIQUE_ID_CAPTION_GAME_OVER_NAME);
            cap.SetText( GameOptions.CurrentPlayerName );     		
    	   break;
         //---------------------------------------------------------------------    	   
    	case GameGlobals.UNIQUE_ID_BUTTON_GAME_OVER_RIGHT:     		
    		GameOptions.EditCurrentPlayerName( 1, 0 );
        	cap = (Caption)FindFirstControlById(gg.UNIQUE_ID_CAPTION_GAME_OVER_NAME);
            cap.SetText( GameOptions.CurrentPlayerName );   
            cap.CursorCharPosition = GameOptions.CurrentPlayerNameCursorPosition;
    	   break;
         //---------------------------------------------------------------------      		
        default:
           break;
    	}
    	
       }
        // *** clear mouse and key input flags for control ***       
        go.ClearDupInput();    
/*        
        if( go.ClassType[GameObject.TYPE_BLOCK_GAME_ENGINE] > 0 )
        {
          BlockGameEngine bge = (BlockGameEngine)go;
          
          if( bge.GameOverFlag > 0 )
          {
            ControlList[i] = null;
			GameGlobals.ClearSaveGameData();
            CreateMenuD();
          }
        }
*/       
        
     }
  }  
//-------------------------------------------------------------------------------------
  protected void Destroy( int UniqueId, int GroupId )
  {
    if( UniqueId > 0 )
    {
      // *** remove all objects with matching UniqueId ***
     for( i = 0; i < CONTROL_LIST_LENGTH; i++ )
       if( ControlList[i] != null )  
         if( ControlList[i].UniqueId == UniqueId )
             ControlList[i] = null;
    }
    else
    if( GroupId > 0 )
    {
      // *** remove all objects with matching GroupId ***
     for( i = 0; i < CONTROL_LIST_LENGTH; i++ )
       if( ControlList[i] != null )  
         if( ControlList[i].GroupId == GroupId )
             ControlList[i] = null;      
    }
    else
    if( GroupId == 0 && UniqueId == 0 )
    {
      // *** remove all objects ***
     for( i = 0; i < CONTROL_LIST_LENGTH; i++ )
          ControlList[i] = null;      
    }
    
    EulaCaptionBox = null;
  }
//-------------------------------------------------------------------------------------
  public void Draw()
  {
    if( ItsMenuCursorParallelia != null )
        ItsMenuCursorParallelia.Draw();
    
    for( i = 0; i < CONTROL_LIST_LENGTH; i++ )
      if( ControlList[i] != null )  
          ControlList[i].Draw();
/*
    String str;
    str = new String(GameGlobals.dummystr + "\n\n " + GameControl.ScreenWidth + ", " 
  	+ GameControl.ScreenHeight );
*/  	
   
    //gg.debugcaption.SetText(gg.dummystr);	       
    //GameGlobals.debugcaption.Draw();
   
    if( GameGlobals.BackDrop != null )
        GameGlobals.BackDrop.Draw();    
  }  
//-------------------------------------------------------------------------------------
  // *** call it during screen onDraw ***
  public void Do()
  {
    if( PauseFlag == 1 )
    	return;
    
	if( ItsMenuCursorParallelia != null )
		ItsMenuCursorParallelia.Do();
	
    CheckControlListState();
    
    for( i = 0; i < CONTROL_LIST_LENGTH; i++ )
      if( ControlList[i] != null )
      {	  
          ControlList[i].Do();
       	
          if( ControlList[i].ClassType[GameObject.TYPE_PLAYER_CONTROL_PANEL] >= 1 )   
          {
        	PlayerControlPanel pcp = (PlayerControlPanel)ControlList[i];
        	
        	if( pcp.GameState == PlayerControlPanel.GAME_OVER_END )
        	{
        		pcp.CurrentObj = null;
        		CreateMenuA();
        	}
        	else
            if( pcp.GameState == PlayerControlPanel.GAME_GOTO_HIGHSCORE )
            {
            	GameOptions.CurrentPlayerNameCursorPosition = 0;
            	CreateMenuD();
            }
          } 	
      }
    
   if( DoneLoadingFlag == 0  ) 
    if( GameControl.CheckDoneLoading() > 0 )
    { 	
    	Init();
        DoneLoadingFlag = 1;
    }       	   
   
  }
//-------------------------------------------------------------------------------------
public int DoBackKey()
{
	int exitflag = 0;
	
    for( i = 0; i < CONTROL_LIST_LENGTH; i++ )
      if( ControlList[i] != null )  
      {
    	 if( ControlList[i].GroupId == GameGlobals.GROUP_ID_NONE )
    		 continue;
    	 
    	 switch( ControlList[i].GroupId )
    	 {
    	   case GameGlobals.GROUP_ID_MENU_A:
    		 exitflag = 1;
    		 break;
    	   case GameGlobals.GROUP_ID_MENU_B:
    	   case GameGlobals.GROUP_ID_MENU_C:    		   
    	   case GameGlobals.GROUP_ID_MENU_C2:
    	   case GameGlobals.GROUP_ID_MENU_D:
    	   case GameGlobals.GROUP_ID_MENU_E:
    	   case GameGlobals.GROUP_ID_MENU_G:
    	   case GameGlobals.GROUP_ID_MENU_I:
             CreateMenuA();	             
             break;
    	   case GameGlobals.GROUP_ID_MENU_F:
    		 CreateMenuA();
    		 break;
           default:
        	exitflag = 1;
        	break;
    	 }
    	 break;
      }
  
    return exitflag;
}
//------------------------------------------------------------------------------------- 
}
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%