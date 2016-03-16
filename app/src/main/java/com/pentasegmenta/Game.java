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
class Cell extends GameObject
{
public final static int CELL_WIDTH_PIXELS = 20;
public final static int CELL_HEIGHT_PIXELS = 20;
public static int Min_Id = 1, Max_Id = 10;

protected int PictureIndex, PictureRow, PictureColumn, AlphaValue;
public int Id, VisualGridX, VisualGridY;

public int SpecialFlag = FLAG_NORMAL;

public final static int FLAG_NORMAL = 0;
public final static int FLAG_RELATED_NEIGHBOR = 1;
public final static int FLAG_FINISHED_NEIGHBOR_TESTING = 2;
public final static int FLAG_DYING = 3;
public final static int FLAG_DEAD = 4;
public final static int FLAG_PARABOLIC_FLIGHT = 5;
public final static int FLAG_DEMO = 6;

public final static float FLIGHT_SPEED_MULTIPLIER = 5.0f;
public final static float FLIGHT_GRAVITY = 1.0f;
protected int DownwardAcceleration = 0, FlightAngle = 0;

public int WidthPercent = 100, HeightPercent = 100;

//-------------------------------------------------------------------------------------
public Cell( int nId, float fX , float fY )
{
super();
ClassType[TYPE_CELL] = 1;
PictureLayer = GE.LAYER_1;
AlphaValue = 255;
X = fX; Y = fY;
Id = nId;
PictureRow = GameOptions.TilesSelectionIndex;
PictureColumn = nId;

PictureIndex = GameControl.IMAGE_CELLS;
}
//-------------------------------------------------------------------------------------
public void Draw()
{
GameGlobals.DrawTileImageOne( PictureIndex, (int)X, (int)Y, PictureLayer, AlphaValue, PictureColumn, PictureRow,
	    	CELL_WIDTH_PIXELS, CELL_HEIGHT_PIXELS, WidthPercent, HeightPercent );
}
//-------------------------------------------------------------------------------------
public void CalculateGrid( int XGridShift, int YGridShift )
{
	VisualGridX = (int)(Math.floor(X/CELL_WIDTH_PIXELS)); VisualGridY = (int)(Math.floor(Y/CELL_HEIGHT_PIXELS));
	GridX = VisualGridX - XGridShift; GridY = VisualGridY - YGridShift;
}
//-------------------------------------------------------------------------------------
public void Do()
{
   if( SpecialFlag == FLAG_PARABOLIC_FLIGHT )
   {
	   DoParabolicFlight();
	   return;
   }
   else
   if( SpecialFlag == FLAG_DYING )
   {
	 AlphaValue = AlphaValue - 20;
	 if( AlphaValue <= 0 )
	 {	 
  	    AlphaValue = 0;
  	    SpecialFlag = FLAG_DEAD;
	 }
   }
   else
   if( SpecialFlag == FLAG_DEMO )
   {
	  PictureRow = GameOptions.TilesSelectionIndex;
   }
}
//-------------------------------------------------------------------------------------
public Cell GetCopy()
{
   Cell cell_obj = new Cell( Id, X, Y );
   cell_obj.VisualGridX = VisualGridX; cell_obj.VisualGridY = VisualGridY;
   cell_obj.GridX = GridX; cell_obj.GridY = GridY;
   cell_obj.AlphaValue = AlphaValue;
   
   return cell_obj;
}
//-------------------------------------------------------------------------------------
public void StartParabolicFlight()
{
 SpecialFlag = FLAG_PARABOLIC_FLIGHT;
 AlphaValue = 150;
 DownwardAcceleration = 0;
 FlightAngle = GameGlobals.random(190,340);
}
//-------------------------------------------------------------------------------------
public void DoParabolicFlight()
{
 X = X + FLIGHT_SPEED_MULTIPLIER * (float)Math.cos(Math.PI/180 * FlightAngle);
 Y = Y + FLIGHT_SPEED_MULTIPLIER * (float)Math.sin(Math.PI/180 * FlightAngle);
 Y = Y + DownwardAcceleration;
	   
 if( Y > GameEngine.TARGET_SCREEN_HEIGHT )
    SpecialFlag = FLAG_DEAD;
	   
 DownwardAcceleration += FLIGHT_GRAVITY;
}
//-------------------------------------------------------------------------------------
}

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
class CellGrid extends GameObject
{
public static CellGrid CurrentObj = null;
public final static int GRID_WIDTH = 16;
public final static int GRID_HEIGHT = 20 + 20;

public final static int TOP_LEFT_X_PIXELS = 0;
public final static int TOP_LEFT_Y_PIXELS = -Cell.CELL_HEIGHT_PIXELS * 20;
public final static int TOP_LEFT_X_GRID = 0;
public final static int TOP_LEFT_Y_GRID = -20;

public final static int SEGMENT_COUNT = 5;

protected Cell[][] Grid = new Cell[GRID_WIDTH][GRID_HEIGHT];
protected Cell[][] DeathGrid = new Cell[GRID_WIDTH][GRID_HEIGHT];	
public static int DyingCount = 0;

protected float MoveSpeedY = 1.0f/16.0f;
public static float MoveSpeedYMultiplier = 1;

//... 1/16, 1/8, 1/4, 1/2, 1, 2, 4, 5, 10

public static float CellGridMoveYInnerDistance = 0.0f;

public final static int MAX_FLIGHT_CELLS = 30;

protected Cell[] FlightCellList = new Cell[MAX_FLIGHT_CELLS];

String debugtext = "";

public int CellsPassedBottomLineCount;

//-------------------------------------------------------------------------------------
public CellGrid()
{
  super();
  ClassType[TYPE_CELL_GRID] = 1;	
  CurrentObj = this;
  
  RefillGrid(0, GameOptions.AreaSelectionIndex);
  DyingCount = 0;
  CellsPassedBottomLineCount = 0;
  
  int i;
  for( i = 0; i < MAX_FLIGHT_CELLS; i++ )
	   FlightCellList[i] = null;
  
  MoveSpeedYMultiplier = 1;
}
//-------------------------------------------------------------------------------------
public void Do()
{
  int x, y, i;
  Cell cell_obj;

  DyingCount = RemoveDeadCellsDeathGrid();
  
  if( DyingCount > 0 )
  {
      ShiftCellsUp(GRID_HEIGHT - 1);  
      CheckFormContinuousSegements(SEGMENT_COUNT);
      return;
  }

  for( y = GRID_HEIGHT - 1; y >= 0; y-- )
   for( x = 0; x < GRID_WIDTH; x++ )
   {
    cell_obj = Grid[x][y];
    
    if( cell_obj != null )
    	cell_obj.Do();
   }
  
  if( PlayerControlPanel.CurrentObj.GameState == PlayerControlPanel.GAME_PLAYING )
  {  
    RefillGrid(1, GameOptions.AreaSelectionIndex);
    MoveGridDown();  
  }
  
  for( i = 0; i < MAX_FLIGHT_CELLS; i++ )
	if( FlightCellList[i] != null )
	{
	   FlightCellList[i].Do();
	   if( FlightCellList[i].SpecialFlag == Cell.FLAG_DEAD )
		   FlightCellList[i] = null;
	}
}
//-------------------------------------------------------------------------------------
public void MoveGridDown()
{
int x, y, GridYOld;
Cell cell_obj;

for( y = GRID_HEIGHT - 1; y >= 0; y-- )
 for( x = 0; x < GRID_WIDTH; x++ )
 {
    cell_obj = Grid[x][y];

    if( cell_obj == null )
    	continue;
    
    GridYOld = cell_obj.GridY;
    cell_obj.Y += MoveSpeedY * MoveSpeedYMultiplier;
    cell_obj.CalculateGrid( TOP_LEFT_X_GRID, TOP_LEFT_Y_GRID );
    
    CellGridMoveYInnerDistance = (float)(cell_obj.Y - cell_obj.VisualGridY * Cell.CELL_HEIGHT_PIXELS);
    
    if( cell_obj.GridY > GridYOld )   
    {
      if( cell_obj.GridY > GRID_HEIGHT - 1 )
      {
   	      //*** teleport cell back to the top ***
/*    	  
    	   Grid[cell_obj.GridX][GRID_HEIGHT - 1] = null;    			  
           cell_obj.Y = TOP_LEFT_Y_PIXELS;
           cell_obj.CalculateGrid( TOP_LEFT_X_GRID, TOP_LEFT_Y_GRID );
           Grid[x][0] = cell_obj;
*/        
           //*** remove cell ***
    	   Grid[x][y] = null;
    	   CellsPassedBottomLineCount++;
      }
      else
      {
    	if( cell_obj.GridY - 1 >= 0 )
         	Grid[cell_obj.GridX][cell_obj.GridY - 1] = null;    
    	Grid[cell_obj.GridX][cell_obj.GridY] = cell_obj;
      }
    }
          
  }
}
//-------------------------------------------------------------------------------------
public void Draw()
{
int x, y, i;

for( y = 0; y < GRID_HEIGHT; y++ )
 for( x = 0; x < GRID_WIDTH; x++ )	
	 if( Grid[x][y] != null )
		 Grid[x][y].Draw();

for( y = 0; y < GRID_HEIGHT; y++ )
 for( x = 0; x < GRID_WIDTH; x++ )	
	 if( DeathGrid[x][y] != null )
		 DeathGrid[x][y].Draw();

  for( i = 0; i < MAX_FLIGHT_CELLS; i++ )
	if( FlightCellList[i] != null )
		FlightCellList[i].Draw();
  
 //GE.DrawTextColor( debugtext, 5, 480-20, GE.LAYER_3, Color.rgb(0,255,0), 16 );  
}
//-------------------------------------------------------------------------------------
public Cell GetCellVisualGridPos( int nVGridX, int nVGridY )
{
	nVGridX = nVGridX - TOP_LEFT_X_GRID;
	nVGridY = nVGridY - TOP_LEFT_Y_GRID;
	
	if( 0 <= nVGridX && nVGridX < GRID_WIDTH && 0 <= nVGridY && nVGridY < GRID_HEIGHT )
	{
	    return Grid[nVGridX][nVGridY];
	}
	
	return null;
}
//-------------------------------------------------------------------------------------
public void SetCellVisualGridPos( Cell pCell, int nVGridX, int nVGridY )
{
	nVGridX = nVGridX - TOP_LEFT_X_GRID;
	nVGridY = nVGridY - TOP_LEFT_Y_GRID;
	
	if( 0 <= nVGridX && nVGridX < GRID_WIDTH && 0 <= nVGridY && nVGridY < GRID_HEIGHT )
	{
	    Grid[nVGridX][nVGridY] = pCell;
	}	
}
//-------------------------------------------------------------------------------------
public void RefillGrid( int option, int area_selection_index )
{
   int x, y, y_start, y_end, id;
   int min_id = 1, max_id = 4, max_y_reach = 8;
   int power_random_flag = 0;
   
switch( area_selection_index )
{
  case 0:
	 min_id = 1; max_id = 4;
	 MoveSpeedY = 1.0f/16.0f;
	 max_y_reach = 8;
	 GameOptions.BackGroundIndex = 0; 
	 break;
  case 1:
	 min_id = 1; max_id = 5; 
	 MoveSpeedY = 1.0f/16.0f;
	 max_y_reach = 8;
	 GameOptions.BackGroundIndex = 1;
	 break;
  case 2:
	 min_id = 1; max_id = 6; 
	 MoveSpeedY = 1.0f/16.0f;
	 max_y_reach = 8;
	 GameOptions.BackGroundIndex = 2; 
	 break;
  case 3:
	 min_id = 1; max_id = 7; 
	 MoveSpeedY = 1.0f/16.0f;
	 max_y_reach = 8;
	 GameOptions.BackGroundIndex = 3; 	 
	 break;	 
  case 4:
	 min_id = 1; max_id = 8; 	 
	 MoveSpeedY = 1.0f/16.0f;
	 GameOptions.BackGroundIndex = 4; 	 
	 break;	 
  case 5:
	 min_id = 1; max_id = 9; 
	 MoveSpeedY = 1.0f/16.0f;
	 max_y_reach = 8;
	 GameOptions.BackGroundIndex = 5; 	 
	 break;	 
  case 6:
	 min_id = 1; max_id = 10; 
	 MoveSpeedY = 1.0f/16.0f;
	 max_y_reach = 8;
	 GameOptions.BackGroundIndex = 6; 	 
	 break;
  case 7:
	 min_id = 1; max_id = 10; 
	 MoveSpeedY = 1.0f/16.0f + 1.0f/32.0f;
	 max_y_reach = 8;
	 power_random_flag = 1;
	 GameOptions.BackGroundIndex = 7; 	 
	 break;
  case 8:
	 min_id = 1; max_id = 5; 
	 MoveSpeedY = 1.0f/4.0f;
	 max_y_reach = 4;
	 GameOptions.BackGroundIndex = 8; 	 
	 power_random_flag = 1;
	 break;
  case 9:
	 min_id = 1; max_id = 6; 
	 MoveSpeedY = 1.0f/4.0f;
	 max_y_reach = 4;
	 GameOptions.BackGroundIndex = 9; 	 
	 power_random_flag = 1;
	 break;
  default:	 
	 break;
}
  
if( option == 0 )
{
  for( x = 1; x < GRID_WIDTH - 1; x++ ) 
  {
	 y_start = 0; y_end = GameGlobals.random( GRID_HEIGHT/2, GRID_HEIGHT/2 + max_y_reach );
	 
	 for( y = y_start; y <= y_end; y++ )
	 if( Grid[x][y] == null )	 
	 {	 
	   if( power_random_flag == 1 )
		  id = GameGlobals.PowerRandom( min_id, max_id );
	   else
	      id = GameGlobals.random( min_id, max_id );

	   Grid[x][y] = new Cell( id, x * Cell.CELL_WIDTH_PIXELS + TOP_LEFT_X_PIXELS, 
			   (y * Cell.CELL_HEIGHT_PIXELS + TOP_LEFT_Y_PIXELS + CellGridMoveYInnerDistance) );
	   Grid[x][y].CalculateGrid( TOP_LEFT_X_GRID, TOP_LEFT_Y_GRID );		 
	 }
  }
  
  GameGlobals.ChangeBackGround(); 
}
else
if( option == 1 )
{
   y_start = 0; y_end = GRID_HEIGHT/2 - 2;

 for( y = y_start; y <= y_end; y++ )  
   for( x = 1; x < GRID_WIDTH - 1; x++ )
	if( Grid[x][y] == null ) 	
	{
	   if( power_random_flag == 1 )
		  id = GameGlobals.PowerRandom( min_id, max_id );
	   else
	      id = GameGlobals.random( min_id, max_id );
	   
	   Grid[x][y] = new Cell( id, x * Cell.CELL_WIDTH_PIXELS + TOP_LEFT_X_PIXELS, 
			   (y * Cell.CELL_HEIGHT_PIXELS + TOP_LEFT_Y_PIXELS + CellGridMoveYInnerDistance) );
	   Grid[x][y].CalculateGrid( TOP_LEFT_X_GRID, TOP_LEFT_Y_GRID );		
	}
}

}
//-------------------------------------------------------------------------------------
int CheckFormContinuousSegements( int SegmentCount )
{
int x, y, result_segment_count = 0;
int y_start, y_end;
y_start = GRID_HEIGHT/2 - 1; y_end = GRID_HEIGHT - 1;
   
 ResetCellFlags();
 
 for( y = y_start; y <= y_end; y++ )
  for( x = 0; x < GRID_WIDTH; x++ )
  {
     if( Grid[x][y] != null )
      if( Grid[x][y].SpecialFlag == Cell.FLAG_NORMAL )
     {
       Grid[x][y].SpecialFlag = Cell.FLAG_RELATED_NEIGHBOR;
       result_segment_count += NeighborExpandingSearch(SegmentCount);
     }
  }
  
  return result_segment_count;
}
//-------------------------------------------------------------------------------------	
public int NeighborExpandingSearch( int expansion_count )
{

int h, i, nx, ny, neighborcount = 0;
Cell tempcell, centercell;

int y_start, y_end;
y_start = GRID_HEIGHT/2 - 1; y_end = GRID_HEIGHT - 1;

int[] x = new int[4];
int[] y = new int[4];

for( h = 1; h <= expansion_count; h++ )
{

for( ny = y_start; ny <= y_end; ny++ )
for( nx = 0; nx < GRID_WIDTH; nx++ )
{

// top
x[0] = 0 + nx; y[0] = -1 + ny;
// left
x[1] = -1 + nx; y[1] = 0 + ny;
// right
x[2] = 1 + nx; y[2] = 0 + ny;
// bottom
x[3] = 0 + nx; y[3] = 1 + ny;

centercell = Grid[nx][ny];

if( centercell == null )
    continue;

if( centercell.SpecialFlag != Cell.FLAG_RELATED_NEIGHBOR )
    continue;

for( i = 0; i < 4; i++ )
{
  if( 0 <= x[i] && x[i] < GRID_WIDTH && 0 <= y[i] && y[i] < GRID_HEIGHT )
  {
    tempcell = Grid[ x[i] ][ y[i] ];
    if( tempcell != null )
     if( tempcell.SpecialFlag == Cell.FLAG_NORMAL ) 
      if( tempcell.Id == centercell.Id )
        tempcell.SpecialFlag = Cell.FLAG_RELATED_NEIGHBOR;
  }
}

}

}

//*** count the number of cells with its SpecialFlag == Cell.FLAG_RELATED_NEIGHBOR ***

for( ny = y_start; ny <= y_end; ny++ )
for( nx = 0; nx < GRID_WIDTH; nx++ )
{
  tempcell = Grid[nx][ny];

  if( tempcell != null )
    if( tempcell.SpecialFlag ==  Cell.FLAG_RELATED_NEIGHBOR )
        neighborcount++;
}

//*** if number of cells with above condition == expansion_count, mark these cells for removal ***

if( neighborcount >= expansion_count )
{
for( ny = y_start; ny <= y_end; ny++ )
for( nx = 0; nx < GRID_WIDTH; nx++ )
{
  tempcell = Grid[nx][ny];

  if( tempcell != null )
    if( tempcell.SpecialFlag == Cell.FLAG_RELATED_NEIGHBOR )
    {
    	DeathGrid[nx][ny] = Grid[nx][ny];
    	DeathGrid[nx][ny].SpecialFlag = Cell.FLAG_DYING;
    	Grid[nx][ny] = null;    	
    }
}
}
else
{
for( ny = 0; ny < GRID_HEIGHT; ny++ )
for( nx = 0; nx < GRID_WIDTH; nx++ )
{
  tempcell = Grid[nx][ny];

  if( tempcell != null )
    if( tempcell.SpecialFlag ==  Cell.FLAG_RELATED_NEIGHBOR )
        tempcell.SpecialFlag = Cell.FLAG_FINISHED_NEIGHBOR_TESTING;
}	
}

   return neighborcount;
}
//-------------------------------------------------------------------------------------	
public void ShiftCellsUp( int iterations )
{
int x, y, i, cells_shifted_count = 0;
int y_start, y_end;

y_start = GRID_HEIGHT/2; y_end = GRID_HEIGHT - 1;

for( i = 0; i < iterations; i++ )
{	
 cells_shifted_count = 0;
 
 for( y = y_start; y <= y_end; y++ )
  for( x = 0; x < GRID_WIDTH; x++ )
  {
	if( Grid[x][y - 1] == null )
	 if( Grid[x][y] != null )
	  {
		 Grid[x][y - 1] = Grid[x][y];
		 Grid[x][y - 1].Y -= Cell.CELL_HEIGHT_PIXELS;
		 Grid[x][y - 1].CalculateGrid( TOP_LEFT_X_GRID, TOP_LEFT_Y_GRID );
		 Grid[x][y] = null;
		 cells_shifted_count++;
	  }
  }
 
 if( cells_shifted_count <= 0 )
	 break;
}

}
//-------------------------------------------------------------------------------------
public void ResetCellFlags()
{
  int nx, ny;
  Cell tempcell;

  for( ny = 0; ny < GRID_HEIGHT; ny++ )
   for( nx = 0; nx < GRID_WIDTH; nx++ )
   {
     tempcell = Grid[nx][ny];

     if( tempcell != null )
         tempcell.SpecialFlag = Cell.FLAG_NORMAL;	
   }
}
//-------------------------------------------------------------------------------------
public void AddFlightCell( Cell cell_obj )
{
  int i;
  for( i = 0; i < MAX_FLIGHT_CELLS; i++ )
	if( FlightCellList[i] == null )
	{
		FlightCellList[i] = cell_obj.GetCopy();
		FlightCellList[i].StartParabolicFlight();
		break;
	}
}
//-------------------------------------------------------------------------------------
public int RemoveDeadCellsDeathGrid()
{
 int dying_count = 0, death_count = 0;
 
 int x, y; Cell cell_obj;
 
  for( y = GRID_HEIGHT - 1; y >= 0; y-- )
    for( x = 0; x < GRID_WIDTH; x++ )
      if( DeathGrid[x][y] != null )        
        {
           cell_obj = DeathGrid[x][y];
    	   if( cell_obj.SpecialFlag == Cell.FLAG_DEAD )
    	   {    		 
    		 AddFlightCell( DeathGrid[x][y] );  
    		 DeathGrid[x][y] = null;
    		 death_count++;
    	   }
    	   else
    	   {
     	     cell_obj.Do();
     	     dying_count++;
    	   }
        }
  
  if( death_count >= 1 )
      GameGlobals.PlaySound(2);
  
  PlayerControlPanel.CurrentObj.AddScore( death_count * 2 );
  
  return dying_count;
}
//-------------------------------------------------------------------------------------
}
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
class MovingCell extends Cell
{
public PlayerShip ItsPlayerShip;
public CellGrid ItsCellGrid;
  
protected int MoveYSpeed = 0;
public int StatusFlag = 0;
protected final static int STATUS_STOP_SHIP = 1, STATUS_STOP_CELL_GRID = 2, STATUS_MOVING_DOWN = 3, STATUS_MOVING_UP = 4;

public final static int MOVE_Y_SPEED = 32;
public int ArrayIndex = 0;

//-------------------------------------------------------------------------------------	
public MovingCell( Cell pCell )
{
  super( pCell.Id, (int)pCell.X, (int)pCell.Y );
  ClassType[TYPE_MOVING_CELL] = 1;
  PictureLayer = GE.LAYER_2;  
}
//-------------------------------------------------------------------------------------	
public MovingCell( int nId, int nX, int nY )	
{
  super( nId, nX, nY );
  ClassType[TYPE_MOVING_CELL] = 1;
  PictureLayer = GE.LAYER_1;	 

}
//-------------------------------------------------------------------------------------
public void Init( int nStatusFlag, int nMoveYSpeed, PlayerShip pPlayerShip, CellGrid pCellGrid, int index )
{
	StatusFlag = nStatusFlag; MoveYSpeed = nMoveYSpeed;	
	ItsPlayerShip = pPlayerShip; ItsCellGrid = pCellGrid; ArrayIndex = index;
}
//-------------------------------------------------------------------------------------
public void Do()
{		   
  if( PlayerControlPanel.CurrentObj.GameState != PlayerControlPanel.GAME_PLAYING )
      return;
  
   if( StatusFlag == STATUS_MOVING_DOWN || StatusFlag == STATUS_MOVING_UP )
       Y += MoveYSpeed;
   
   if( StatusFlag == STATUS_MOVING_DOWN )
   {
	  if( Y >= ItsPlayerShip.Y - Cell.CELL_HEIGHT_PIXELS * (ArrayIndex + 1) )
	  {
		 X = ItsPlayerShip.X;
		 Y = ItsPlayerShip.Y - Cell.CELL_HEIGHT_PIXELS * (ArrayIndex + 1);		 
		 StatusFlag = STATUS_STOP_SHIP;
	  }
   }
   else
   if( StatusFlag == STATUS_MOVING_UP )
   {
	  if( MovingUpCheckCollideCellGrid() >= 1 )
		  StatusFlag = STATUS_STOP_CELL_GRID;
   }
}
//-------------------------------------------------------------------------------------
public int MovingUpCheckCollideCellGrid()
{
  int vgridx, vgridy, i;
  Cell TempCell;
  Cell ThisCell;
 
  for( i = 0; i < 16; i++ )
  {
    vgridx = (int)(Math.floor(X/CELL_WIDTH_PIXELS));
    vgridy = (int)(Math.floor((Y - i)/CELL_HEIGHT_PIXELS));
  
   TempCell = ItsCellGrid.GetCellVisualGridPos( vgridx, vgridy );
   
   if( TempCell != null )
   {
	 ThisCell = (Cell)this;
	 ThisCell.X = TempCell.X;
	 ThisCell.Y = TempCell.Y + CELL_HEIGHT_PIXELS;
	 ThisCell.CalculateGrid( CellGrid.TOP_LEFT_X_GRID, CellGrid.TOP_LEFT_Y_GRID );
	 
	 ItsCellGrid.SetCellVisualGridPos( (Cell)this.GetCopy(), vgridx, vgridy + 1 );
	 ItsCellGrid.CheckFormContinuousSegements(CellGrid.SEGMENT_COUNT);
	 
     GameGlobals.PlaySound(3);	 
	 return 1;
   }		   
   else
   if( i == 0 && vgridy <= 0 )
   {
	 //*** mark the MovingCell for removal if it passes out of visual range ***
   
	 ThisCell = (Cell)this;	 
	 ThisCell.CalculateGrid( CellGrid.TOP_LEFT_X_GRID, CellGrid.TOP_LEFT_Y_GRID ); 
	 if( ThisCell.VisualGridY < -1 )
		 return 1;		 
   }
   
  }
/*  
  vgridx = (int)(Math.floor(X/CELL_WIDTH_PIXELS));
  vgridy = (int)(Math.floor(Y/CELL_HEIGHT_PIXELS));
		  
  if( vgridy <= 0 )
  {
	ThisCell = (Cell)this;
    ThisCell.Y = ItsCellGrid.CellGridMoveYInnerDistance;
    ItsCellGrid.SetCellVisualGridPos( (Cell)this, vgridx, 0 );
    return 1;
  }
*/  
  return 0;
}
//-------------------------------------------------------------------------------------
public int CheckCollideCellGrid()
{
	int gridx, gridy;
	Cell TempCell;
	
    gridx = (int)(Math.floor(X/CELL_WIDTH_PIXELS));
    gridy = (int)(Math.floor(Y/CELL_HEIGHT_PIXELS));
  
   TempCell = ItsCellGrid.GetCellVisualGridPos( gridx, gridy );
   
   if( TempCell != null )
	   return 1;
   
   return 0;
}
//-------------------------------------------------------------------------------------
}
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
class TractorBeam extends GameObject
{ 
  public final static int MAX_BEAM_SEGMENTS = 30;
  public final static int CELL_WIDTH_PIXELS = 20, CELL_HEIGHT_PIXELS = 20;
  
  public PlayerShip ItsPlayerShip = null;	
  public int ActiveFlag = 1;
  public int AlphaValue = 255;
  int PictureIndex = GameControl.IMAGE_BEAMS, PictureColumn = 0, PictureRow = 0;
  
//-------------------------------------------------------------------------------------  
public TractorBeam( PlayerShip pPlayerShip )
{
ClassType[TYPE_TRACTOR_BEAM] = 1;
PictureLayer = GE.LAYER_1;	

ItsPlayerShip = pPlayerShip;
PictureRow = GameOptions.ShipSelectionIndex;
}
//-------------------------------------------------------------------------------------
public void Do()
{
  if( ItsPlayerShip.ItsMovingCell == null )
	  ActiveFlag = 0;
}
//-------------------------------------------------------------------------------------
public void Draw()
{
  int topindex = ItsPlayerShip.GetTopMostMovingCellIndex();
  
  if( ItsPlayerShip != null )
  if( ItsPlayerShip.ItsMovingCell[topindex] != null )
  {
   if( ItsPlayerShip.ItsMovingCell[topindex].StatusFlag == MovingCell.STATUS_MOVING_DOWN ||
	   ItsPlayerShip.ItsMovingCell[topindex].StatusFlag == MovingCell.STATUS_MOVING_UP )   
   {
	   int i;
	   int y_delta = (int)Math.abs(Math.floor(ItsPlayerShip.Y - ItsPlayerShip.ItsMovingCell[topindex].Y));	   
	   float segment_interval = y_delta/MAX_BEAM_SEGMENTS;
	   float y_top = ItsPlayerShip.ItsMovingCell[topindex].Y;
			   
	   if( segment_interval < 1.0f )
		   segment_interval = 1.0f;
	  
	   float xpos, ypos;
	   
	   xpos = ItsPlayerShip.X;
	   AlphaValue = 255;
	   
	   for( ypos = ItsPlayerShip.Y - CELL_HEIGHT_PIXELS; ypos >= y_top; ypos -= segment_interval )
	   {		  
		 AlphaValue -= 255 / MAX_BEAM_SEGMENTS;
		 if( AlphaValue < 0 )
			 AlphaValue = 0;
		 
		 PictureColumn = GameGlobals.random( 0, 3 );
		 
		 GameGlobals.DrawTileImageOne( PictureIndex, (int)xpos, (int)ypos, PictureLayer, AlphaValue, PictureColumn, PictureRow,
	    	CELL_WIDTH_PIXELS, CELL_HEIGHT_PIXELS);
	   }
   }
  }
}
//-------------------------------------------------------------------------------------
}
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
class PlayerShip extends GameObject
{
  public int AlphaValue = 255;
  public int PictureIndex = GameControl.IMAGE_PLAYER_SHIP, PictureColumn, PictureRow;
  public final static int DRAW_X_SHIFT = -10, DRAW_Y_SHIFT = -10;
  public final static int VISUAL_WIDTH_PIXELS = 40, VISUAL_HEIGHT_PIXELS = 40;
  public final static int CELL_WIDTH_PIXELS = 20, CELL_HEIGHT_PIXELS = 20;
  
  public final static int ACTION_MOVE_LEFT = 1;
  public final static int ACTION_MOVE_RIGHT = 2;  
  public final static int LEFT_X_WALL = CELL_WIDTH_PIXELS;
  public final static int RIGHT_X_WALL = GameEngine.TARGET_SCREEN_WIDTH - CELL_WIDTH_PIXELS;
  
  public final static int TRACTOR_BEAM_PUSH_PULL = 0;
  public final static int TRACTOR_BEAM_PUSH = 1;
  public final static int TRACTOR_BEAM_PULL = 2;
  public final static int MAX_MOVING_CELLS = 5;
  
  public MovingCell[] ItsMovingCell;
  public TractorBeam ItsTractorBeam = null;  
  public CellGrid ItsCellGrid = null;
  
  protected int TargetCursorPictureIndex = GameControl.IMAGE_CELLS;
  protected float TargetCursorX = 0.0f, TargetCursorY = 0.0f;
  protected int TargetCursorAlpha = 255, TargetCursorAlphaCounter = 0;
  
  public final static int MAX_ANIMATION_FRAME_INDEX = 3;
  protected int AnimationFrameIndex = 0;
  
  public int VisualGridX, VisualGridY;
  
//-------------------------------------------------------------------------------------
public PlayerShip( CellGrid pCellGrid )
{
super();
ClassType[TYPE_PLAYER_SHIP] = 1;
PictureLayer = GE.LAYER_3;

X = (int)(CellGrid.GRID_WIDTH/2 * CELL_WIDTH_PIXELS);
Y = (int)((CellGrid.GRID_HEIGHT - 2 + CellGrid.TOP_LEFT_Y_GRID) * CELL_HEIGHT_PIXELS);

ItsCellGrid = pCellGrid;

PictureColumn = 0; PictureRow = GameOptions.ShipSelectionIndex;

ItsMovingCell = new MovingCell[MAX_MOVING_CELLS];

int i;

for( i = 0; i < MAX_MOVING_CELLS; i++ )
 ItsMovingCell[i] = null;

}
//-------------------------------------------------------------------------------------
public void Draw()
{

GameGlobals.DrawTileImageOne( PictureIndex, (int)X + DRAW_X_SHIFT, (int)Y + DRAW_Y_SHIFT, PictureLayer, 
		AlphaValue, PictureColumn + AnimationFrameIndex, PictureRow, VISUAL_WIDTH_PIXELS, VISUAL_HEIGHT_PIXELS );

int i;

for( i = 0; i < MAX_MOVING_CELLS; i++ )
  if( ItsMovingCell[i] != null )
	  ItsMovingCell[i].Draw();

  if( ItsTractorBeam != null )
	  ItsTractorBeam.Draw();

  if( ItsCellGrid != null )
     GameGlobals.DrawTileImageOne( TargetCursorPictureIndex, (int)TargetCursorX, (int)TargetCursorY, PictureLayer, 
   	   TargetCursorAlpha, 0, 0, CELL_WIDTH_PIXELS, CELL_HEIGHT_PIXELS );  
}
//-------------------------------------------------------------------------------------
protected void FireTractorBeam( int direction )
{
  int vgridx, vgridy, topindex;
  Cell TempCell;

  topindex = GetTopMostMovingCellIndex();
  
if( ((ItsMovingCell[topindex] == null && direction == TRACTOR_BEAM_PUSH_PULL) || direction == TRACTOR_BEAM_PULL ) && topindex < MAX_MOVING_CELLS - 1 )
{		  
	if( ItsMovingCell[topindex] != null )
		topindex++;
	
    vgridx = (int)(Math.floor(X/CELL_WIDTH_PIXELS));
    vgridy = (int)(Math.floor(Y/CELL_HEIGHT_PIXELS));

    for( ; vgridy >= -1; vgridy-- )
    {
	 TempCell = ItsCellGrid.GetCellVisualGridPos( vgridx, vgridy );
	 if( TempCell != null )
	 {
		ItsCellGrid.SetCellVisualGridPos( null, vgridx, vgridy );
		ItsMovingCell[topindex] = new MovingCell( TempCell );
		ItsMovingCell[topindex].Init( MovingCell.STATUS_MOVING_DOWN, MovingCell.MOVE_Y_SPEED, this, ItsCellGrid, topindex );
		ItsTractorBeam = new TractorBeam( this );
		break;
	 }	
    }
  
  GameGlobals.PlaySound(0);
}  
else
if( ItsMovingCell[topindex] != null && ( direction == TRACTOR_BEAM_PUSH_PULL || direction == TRACTOR_BEAM_PUSH ))
{
   if( ItsMovingCell[topindex].StatusFlag == MovingCell.STATUS_STOP_SHIP )
   {
       ItsMovingCell[topindex].Init( MovingCell.STATUS_MOVING_UP, -MovingCell.MOVE_Y_SPEED, this, ItsCellGrid, topindex );
       ItsTractorBeam = new TractorBeam( this );
       GameGlobals.PlaySound(1);
   }  
}
       
}
//-------------------------------------------------------------------------------------
public void Do()
{         
int i;

for( i = 0; i < MAX_MOVING_CELLS; i++ )
{
  if( ItsMovingCell[i] != null )
  {
     if( ItsMovingCell[i].StatusFlag == MovingCell.STATUS_STOP_CELL_GRID )	 
       ItsMovingCell[i] = null;	 
     else 
	   ItsMovingCell[i].Do();
  }
}  
  
  if( ItsTractorBeam != null )
  {
	 ItsTractorBeam.Do();
	 
	 if( ItsTractorBeam.ActiveFlag == 0 )
		 ItsTractorBeam = null;
  }
  
  if( ItsCellGrid != null )
      GetTargetCursorPos();
  
  AnimationFrameIndex++;
  
  if( AnimationFrameIndex > MAX_ANIMATION_FRAME_INDEX )
	  AnimationFrameIndex = 0;
  
  PictureRow = GameOptions.ShipSelectionIndex;
  
  if( PlayerControlPanel.CurrentObj != null )
    if( PlayerControlPanel.CurrentObj.GameState != PlayerControlPanel.GAME_PLAYING )
        return;  
}
//-------------------------------------------------------------------------------------
public int LockXMovingCell( int ship_action )
{
	float Ship_X_Old = X;
	
if( ship_action == ACTION_MOVE_LEFT )
	X -= CELL_WIDTH_PIXELS;
else
if( ship_action == ACTION_MOVE_RIGHT )
	X += CELL_WIDTH_PIXELS;

if( X < LEFT_X_WALL )
	X = Ship_X_Old;
else
if( X >= RIGHT_X_WALL )
	X = Ship_X_Old;

int i;

for( i = 0; i < MAX_MOVING_CELLS; i++ )
{
if( ItsMovingCell[i] != null )
{
	float X_Old = ItsMovingCell[i].X;
	
    ItsMovingCell[i].X = X;
    if( ItsMovingCell[i].CheckCollideCellGrid() >= 1 )
    {
       ItsMovingCell[i].X = X_Old;
       X = Ship_X_Old;
       return 1;
    }
    
    ItsMovingCell[i].CalculateGrid( CellGrid.TOP_LEFT_X_GRID, CellGrid.TOP_LEFT_Y_GRID );
}
}
    return 0;
}
//-------------------------------------------------------------------------------------
protected void GetTargetCursorPos()
{
  int vgridx, vgridy;
  Cell TempCell;

  vgridx = (int)(Math.floor(X/CELL_WIDTH_PIXELS));
  vgridy = (int)(Math.floor(Y/CELL_HEIGHT_PIXELS));

  TargetCursorX = X;
  TargetCursorY = Y;
  
  for( ; vgridy >= -1; vgridy-- )
  {
	TempCell = ItsCellGrid.GetCellVisualGridPos( vgridx, vgridy );	
	if( TempCell != null )
	{
	 TargetCursorX = TempCell.X; TargetCursorY = TempCell.Y;
	 break;
	}
	
	TargetCursorY = vgridy * CELL_HEIGHT_PIXELS;
  }
  
  TargetCursorAlpha = (int)(Math.sin(TargetCursorAlphaCounter * 3.14159/180) * 255);
  TargetCursorAlphaCounter += 20;
  
  if( TargetCursorAlphaCounter > 180 )
	  TargetCursorAlphaCounter = 0;
}
//-------------------------------------------------------------------------------------
public void CalculateGrid( int XGridShift, int YGridShift )
{
	VisualGridX = (int)(Math.floor(X/CELL_WIDTH_PIXELS)); VisualGridY = (int)(Math.floor(Y/CELL_HEIGHT_PIXELS));
	GridX = VisualGridX - XGridShift; GridY = VisualGridY - YGridShift;
}
//-------------------------------------------------------------------------------------
public int GetTopMostMovingCellIndex()
{
  int i, topindex = 0;
  
  for( i = MAX_MOVING_CELLS - 1; i >= 0; i-- )
    if( ItsMovingCell[i] != null )
    {
    	topindex = i;
    	break;
    }
  
  return topindex;
}
//-------------------------------------------------------------------------------------
}
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
class PlayerControlPanel extends GameObject
{
  protected PlayerShip ItsPlayerShip;
  protected CellGrid ItsCellGrid;	
  public static PlayerControlPanel CurrentObj;
  
  public final static int MAX_BUTTONS = 5;
  public final static int BUTTON_WIDTH = 48, BUTTON_HEIGHT = 48;
  
  public final static int BUTTON_MOVE_LEFT = 0;
  public final static int BUTTON_MOVE_TRACTOR_BEAM_LEFT = 1;
  public final static int BUTTON_FASTER = 2;
  public final static int BUTTON_MOVE_TRACTOR_BEAM_RIGHT = 3;
  public final static int BUTTON_MOVE_RIGHT = 4;  
  
  public final static int UNIQUE_ID_MOVE_LEFT = 10001;
  public final static int UNIQUE_ID_LEFT_BEAM = 10002;
  public final static int UNIQUE_ID_FASTER = 10003;
  public final static int UNIQUE_ID_RIGHT_BEAM = 10004;
  public final static int UNIQUE_ID_MOVE_RIGHT = 10005;
  
  Button[] ButtonList = new Button[MAX_BUTTONS];  
  
  public final static int Y_SHIFT = -30;
  
  public int CurrentScore = 0, WorkingScore = 0, HighScore = 0;
  protected Caption ItsCaption;
  protected String InfoString = "";
  
  public final static int GAME_PLAYING = 0;
  public final static int GAME_OVER_INTERMISSION = 1;
  public final static int GAME_OVER_END = 2;
  public final static int GAME_GOTO_HIGHSCORE = 3;
  
  public int GameState = GAME_PLAYING;
  
  protected CustomDrawText ItsCustomDrawTextMessage;
  public final static int MAX_TICK_COUNTER_MAX = 40;
  protected int MessageTickCounter = 0;
  
//-------------------------------------------------------------------------------------  
PlayerControlPanel( PlayerShip pPlayerShip, CellGrid pCellGrid )
{
super();

ClassType[TYPE_PLAYER_CONTROL_PANEL] = 1;
PictureLayer = GE.LAYER_1;	
MouseEventNotifyFlag = true; KeyEventNotifyFlag = true;
ItsPlayerShip = pPlayerShip; ItsCellGrid = pCellGrid;
CurrentObj = this;

int x_spacing = 60;  // 52 to fit 6 buttons
int x_start = 15; // 5;
		 
Button but;

but = new Button( "<-", 0,0,0,0 );
but.Create_WidthxHeight( x_start + x_spacing * 0, 430 + Y_SHIFT, BUTTON_WIDTH, BUTTON_HEIGHT, GameGlobals.GROUP_ID_NONE, 
		UNIQUE_ID_MOVE_LEFT, GE.LAYER_2, GE.LAYER_1, GameControl.IMAGE_TILES_1x1, 12, 19, 255 );
ButtonList[BUTTON_MOVE_LEFT] = but;

but = new Button( "^|", 0,0,0,0 );
but.Create_WidthxHeight( x_start + x_spacing * 1, 430 + Y_SHIFT, BUTTON_WIDTH, BUTTON_HEIGHT, GameGlobals.GROUP_ID_NONE, 
		UNIQUE_ID_LEFT_BEAM, GE.LAYER_2, GE.LAYER_1, GameControl.IMAGE_TILES_1x1, 11, 19, 255 );
ButtonList[BUTTON_MOVE_TRACTOR_BEAM_LEFT] = but;

but = new Button( "||", 0,0,0,0 );
but.Create_WidthxHeight( x_start + x_spacing * 2, 430 + Y_SHIFT, BUTTON_WIDTH, BUTTON_HEIGHT, GameGlobals.GROUP_ID_NONE, 
		UNIQUE_ID_FASTER, GE.LAYER_2, GE.LAYER_1, GameControl.IMAGE_TILES_1x1, 10, 19, 255 );
ButtonList[BUTTON_FASTER] = but;

but = new Button( "|^", 0,0,0,0 );
but.Create_WidthxHeight( x_start + x_spacing * 3, 430 + Y_SHIFT, BUTTON_WIDTH, BUTTON_HEIGHT, GameGlobals.GROUP_ID_NONE, 
		UNIQUE_ID_RIGHT_BEAM, GE.LAYER_2, GE.LAYER_1, GameControl.IMAGE_TILES_1x1, 11, 19, 255 );
ButtonList[BUTTON_MOVE_TRACTOR_BEAM_RIGHT] = but;

but = new Button( "->", 0,0,0,0 );
but.Create_WidthxHeight( x_start + x_spacing * 4, 430 + Y_SHIFT, BUTTON_WIDTH, BUTTON_HEIGHT, GameGlobals.GROUP_ID_NONE, 
		UNIQUE_ID_MOVE_RIGHT, GE.LAYER_2, GE.LAYER_1, GameControl.IMAGE_TILES_1x1, 13, 19, 255 );
ButtonList[BUTTON_MOVE_RIGHT] = but;


  int x, y;
  y = GameEngine.TARGET_SCREEN_HEIGHT - Cell.CELL_HEIGHT_PIXELS/4;
  x = Cell.CELL_WIDTH_PIXELS/2;
  
  ItsCaption = new Caption( x, y, "" ); 
  ItsCaption.SetTextOptions( GameGlobals.RegularFontSize1, GameOptions.MainFontColor, 0, 0, true, Cell.CELL_HEIGHT_PIXELS );
  ItsCaption.TextLayer = GameEngine.LAYER_3;
  ItsCaption.UniqueId = GameGlobals.UNIQUE_ID_CAPTION_GENERAL;  
  
  HighScore = HighScores.CurrentObj.GetHighestScore(GameOptions.AreaSelectionIndex);
  
  InfoString = "Score " + GameGlobals.SetMinTextWidthLeft( "" + CurrentScore, "0", 8 ) +
 "  High " + GameGlobals.SetMinTextWidthLeft( "" + HighScore, "0", 8);
  
  ItsCustomDrawTextMessage = new CustomDrawText( 0, 0, GameControl.IMAGE_FONT_LM_18x27, 18, 27 );
  
  GameState = GAME_PLAYING;
}
//-------------------------------------------------------------------------------------
public void Do()
{
  if( InputDelayCounter < InputDelayMax )
	  InputDelayCounter++;	 	
  
  if( GameState != GAME_PLAYING )
  {
	  DoTextMessage();
      return;
  }
  
  if( WorkingScore > 0 )
  {
	 WorkingScore--;
	 CurrentScore++;
	   
	 if( CurrentScore > HighScore )
		 HighScore = CurrentScore;
	 
     InfoString = "Score " + GameGlobals.SetMinTextWidthLeft( "" + CurrentScore, "0", 8 ) +
       "  High " + GameGlobals.SetMinTextWidthLeft( "" + HighScore, "0", 8);          
  }    
  
  int i, UniqueId;
  Button but;
  
  for( i = 0; i < MAX_BUTTONS; i++ )
  {
    UniqueId = ButtonList[i].UniqueId;
    but = ButtonList[i];
    but.Do();
		   
    if( but.MouseStatus_Dup == but.ME_PRESS_DOWN ||
		but.MouseStatus_Dup == but.ME_MOVE )
	{ 
		      
       switch(UniqueId)
       {
       case UNIQUE_ID_MOVE_LEFT:
    	  ItsPlayerShip.LockXMovingCell( PlayerShip.ACTION_MOVE_LEFT ); 
    	  break;
       case UNIQUE_ID_LEFT_BEAM:
    	  ItsPlayerShip.FireTractorBeam(PlayerShip.TRACTOR_BEAM_PUSH_PULL);  
    	  break;
       case UNIQUE_ID_FASTER:
    	   //CellGrid.MoveSpeedYMultiplier = 16;
    	   ItsPlayerShip.FireTractorBeam(PlayerShip.TRACTOR_BEAM_PULL);  
    	   break;
       case UNIQUE_ID_RIGHT_BEAM:
    	  ItsPlayerShip.FireTractorBeam(PlayerShip.TRACTOR_BEAM_PUSH_PULL);  
    	  break;
       case UNIQUE_ID_MOVE_RIGHT:
    	  ItsPlayerShip.LockXMovingCell(PlayerShip.ACTION_MOVE_RIGHT ); 
    	  break;
       default:
    	  break;
       }
       
       
	}
    else
    if( but.MouseStatus_Dup == but.ME_RELEASE )
    {
      if( UniqueId == UNIQUE_ID_FASTER )
    	  CellGrid.MoveSpeedYMultiplier = 1;
    }
    
    but.ClearDupInput();
  }	
  
  if( CheckFatalSituation() >= 1 )
	  BeginGameOver();
}
//-------------------------------------------------------------------------------------
protected void HandleKeyDown()
{
   if( InputDelayCounter >= InputDelayMax )
   {
       InputDelayCounter = 0;
   }
   else
      return;
    
   if( CellGrid.DyingCount >= 1 )
	   return;
   
        switch( KeyCode )
        {
          case KeyEvent.KEYCODE_DPAD_DOWN:     
        	//CellGrid.MoveSpeedYMultiplier = 16;
        	ItsPlayerShip.FireTractorBeam(PlayerShip.TRACTOR_BEAM_PULL);  
            break;
          case KeyEvent.KEYCODE_DPAD_LEFT:
            ItsPlayerShip.LockXMovingCell( PlayerShip.ACTION_MOVE_LEFT );
            break;
          case KeyEvent.KEYCODE_DPAD_RIGHT:
            ItsPlayerShip.LockXMovingCell( PlayerShip.ACTION_MOVE_RIGHT );
            break;
          case KeyEvent.KEYCODE_DPAD_UP:
        	ItsPlayerShip.FireTractorBeam(PlayerShip.TRACTOR_BEAM_PUSH_PULL);  
            break;           
          case KeyEvent.KEYCODE_BACK:
            break;
          case KeyEvent.KEYCODE_ENTER:
          case KeyEvent.KEYCODE_DPAD_CENTER:
            break;
          default:
            break;
        }
}
//-------------------------------------------------------------------------------------
protected void HandleKeyUp()
{
  int IndexChange = 0;
    
  switch( KeyCode )
  {
     case KeyEvent.KEYCODE_DPAD_DOWN:
    	 CellGrid.MoveSpeedYMultiplier = 1;
    	 break;
     case KeyEvent.KEYCODE_DPAD_LEFT:
     case KeyEvent.KEYCODE_DPAD_RIGHT:
     case KeyEvent.KEYCODE_DPAD_UP:    
          break;
     case KeyEvent.KEYCODE_BACK:
            break;
     case KeyEvent.KEYCODE_ENTER:
     case KeyEvent.KEYCODE_DPAD_CENTER:
            break;
          default:
            break;
        }

         InputDelayCounter = InputDelayMax;
}
//-------------------------------------------------------------------------------------    
public void OnKey() 
{ 
	if( GameState != GAME_PLAYING )
		return;
	
      if( KeyStatus == ME_KEY_DOWN )
         HandleKeyDown();
      else
      if( KeyStatus == ME_KEY_UP )
         HandleKeyUp();  
}
//-------------------------------------------------------------------------------------
public void OnClick()
{
	if( GameState != GAME_PLAYING )
		return;
	
    int i;
    for( i = 0; i < MAX_BUTTONS; i++ )
         ButtonList[i].OnClick();
}
//-------------------------------------------------------------------------------------
public void Draw()
{
  int i;
  
  for( i = 0; i < MAX_BUTTONS; i++ )
	 ButtonList[i].Draw();
    
  ItsCaption.SetText( InfoString );
  ItsCaption.Draw();
  
  if( GameState != GAME_PLAYING )
	  ItsCustomDrawTextMessage.Draw();
  
  //GE.DrawLine(0, 400, 320, 400, GE.LAYER_2, Color.rgb(128,0,128) );
}
//-------------------------------------------------------------------------------------
public void AddScore( int value )
{
   WorkingScore += value;
}
//-------------------------------------------------------------------------------------
public void BeginGameOver()
{
  GameState = GAME_OVER_INTERMISSION;
  
  ItsCustomDrawTextMessage.SetText("GAME OVER");    
  ItsCustomDrawTextMessage.X = GameEngine.TARGET_SCREEN_WIDTH/2 - ItsCustomDrawTextMessage.GetStringWidthPixels()/2;   
  ItsCustomDrawTextMessage.Y = 0;
  MessageTickCounter = 0;
}
//-------------------------------------------------------------------------------------
public void DoTextMessage()
{
if( GameState == GAME_OVER_INTERMISSION )
{
	if( MessageTickCounter >= MAX_TICK_COUNTER_MAX )
	{
	 CurrentScore += WorkingScore;
	 WorkingScore = 0;
	 GameOptions.CurrentPlayerScore = CurrentScore;
	 
     if( HighScores.CurrentObj.CheckNewHighScore( GameOptions.AreaSelectionIndex, GameOptions.CurrentPlayerScore ) >= 1 )
        GameState = GAME_GOTO_HIGHSCORE;
     else
        GameState = GAME_OVER_END;
     return;
	}
			
	MessageTickCounter++;	
	ItsCustomDrawTextMessage.Y += 5;
}
}
//-------------------------------------------------------------------------------------
public int CheckFatalSituation()
{
  if( CellGrid.CurrentObj.CellsPassedBottomLineCount >= 1 )
      return 1;

  ItsPlayerShip.CalculateGrid( CellGrid.TOP_LEFT_X_GRID, CellGrid.TOP_LEFT_Y_GRID );

  Cell TempCell = ItsCellGrid.GetCellVisualGridPos( ItsPlayerShip.VisualGridX, ItsPlayerShip.VisualGridY );

  if( TempCell != null )
      return 1;

      return 0;
}
//-------------------------------------------------------------------------------------
}
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
class CellFountainSpray extends GameObject
{
public final static int MAX_CELLS = 30;

protected Cell[] List = new Cell[MAX_CELLS];

//-------------------------------------------------------------------------------------
public CellFountainSpray()
{
  super();
  ClassType[TYPE_CELL_FOUNTAIN_SPRAY] = 1;
  PictureLayer = GE.LAYER_1;

  int i, sizepercent; float xpos, ypos;

  for( i = 0; i < MAX_CELLS; i++ ) 
  {
    xpos = GameGlobals.random( 0, GameEngine.TARGET_SCREEN_WIDTH );
    ypos = GameGlobals.random( 0, GameEngine.TARGET_SCREEN_WIDTH/4 );

    List[i] = new Cell( GameGlobals.random( Cell.Min_Id, Cell.Max_Id ), xpos, ypos );
    sizepercent = GameGlobals.random( 25, 255 );
    List[i].StartParabolicFlight();
    List[i].WidthPercent = sizepercent; List[i].HeightPercent = sizepercent;
    List[i].AlphaValue = sizepercent;
  }
}
//-------------------------------------------------------------------------------------
public void Do()
{
  int i;

  for( i = 0; i < MAX_CELLS; i++ ) 
  {
   if( List[i] != null )   
   {
      List[i].Do();
      if( List[i].SpecialFlag == Cell.FLAG_DEAD )
          List[i] = null;
   }
  }

  RandomRefill();
}
//-------------------------------------------------------------------------------------
public void Draw()
{
  int i;

  for( i = 0; i < MAX_CELLS; i++ ) 
    if( List[i] != null )   
        List[i].Draw();
}
//-------------------------------------------------------------------------------------
public void RandomRefill()
{
  int i, sizepercent; float xpos, ypos;

  for( i = 0; i < MAX_CELLS; i++ ) 
    if( List[i] == null )   
    if( GameGlobals.random(0,10) == 1 )
    {
      xpos = GameGlobals.random( 0, GameEngine.TARGET_SCREEN_WIDTH );
      ypos = GameGlobals.random( 0, GameEngine.TARGET_SCREEN_WIDTH/4 );

      List[i] = new Cell( GameGlobals.random( Cell.Min_Id, Cell.Max_Id ), xpos, ypos );
      sizepercent = GameGlobals.random( 25, 255 );
      List[i].StartParabolicFlight();
      List[i].WidthPercent = sizepercent; List[i].HeightPercent = sizepercent;
      List[i].AlphaValue = sizepercent;
    }
}
//-------------------------------------------------------------------------------------
}
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
