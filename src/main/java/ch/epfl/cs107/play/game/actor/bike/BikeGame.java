/*
 *	Author:      Valentin Kaelin
 *	Date:        25 nov. 2017
 */
package ch.epfl.cs107.play.game.actor.bike;

import java.awt.Color;

import com.sun.glass.events.KeyEvent;

import ch.epfl.cs107.play.game.actor.ActorGame;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.actor.crate.Crate;
import ch.epfl.cs107.play.game.actor.general.Bascule;
import ch.epfl.cs107.play.game.actor.general.Pendule;
import ch.epfl.cs107.play.game.actor.general.Terrain;
import ch.epfl.cs107.play.game.actor.general.Tremplin;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.Polygon;
import ch.epfl.cs107.play.math.Polyline;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Window;

public class BikeGame extends ActorGame {
	// Nous gardons en attribut le fileSystem pour pouvoir l'utiliser lors du reset
	private FileSystem fileSystem;
	
	private boolean fixed = false;
	// Nous n'avons pas mis de friction sur les roues pour pouvoir faire des terrains glissants 
	private float friction = 100.0f;
	
	private Terrain terrain;
	private Polyline formeTerrain = new Polyline (
			-10.0f, -1000.0f,
			-10.0f, 20.0f,
			0.0f, 20.0f,
			0.0f, 0.0f,
			10.0f, 0.0f,
			15.0f, -7.0f,
			25.0f, -7.0f,
			35.0f, 0.0f,
			45.0f, 0.0f,
			50.0f, -20.0f,
			55.0f, 0.0f,
			65.0f, 0.0f,
			70.0f, -20.0f,
			75.0f, 0.0f,
			85.0f, 0.0f,
			100.0f, -20.0f,
			120.0f, -20.0f,
			125.0f, -18.0f,
			135.0f, -45.0f,
			145.0f, -20.0f,
			163.0f, -20.0f,
			163.0f, -21.0f,
			165.0f, -21.0f,
			170.0f, -35.0f,
			175.0f, -20.0f,
			225.0f, -20.0f,
			225.0f, -22.0f,
			235.0f, -22.0f,
			238.0f, -20.0f,
			240.0f, -20.0f,
			240.0f, -25.0f,
			245.0f, -25.0f,
			245.0f, -20.0f,
			260.0f, -20.0f,
			260.0f, 0.0f,
			280.0f, 0.0f,
			280.0f, -1000.0f);
	private Terrain terrainGlissant;
	private Polyline formeTerrainGlissant = new Polyline (
			-0.2f, -1.0f,
			-0.2f, 2.05f,
			13.0f, 2.05f,
			13.0f, -1.0f);
	
	// Nous creeons un tableau de vector contenant les positions
	// des differents buissons sur le terrain
	private Vector[] bushPos = {
			new Vector(-5.0f, 0.0f),
			new Vector(-15.0f, 7.0f),
			new Vector(-22.0f, 7.0f),
			new Vector(-38.0f, 0.0f),
			new Vector(-43.0f, 0.0f),
			new Vector(-63.0f, 0.0f),
			new Vector(-77.0f, 0.0f),
			new Vector(-118.0f, 20.0f),
			new Vector(-150.0f, 20.0f),
			new Vector(-152.0f, 20.0f),
			new Vector(-159.0f, 20.0f),
			new Vector(-177.0f, 20.0f),
			new Vector(-185.0f, 20.0f),
			new Vector(-197.0f, 20.0f),
			new Vector(-204.0f, 20.0f),
			new Vector(-212.0f, 20.0f),
			new Vector(-221.0f, 20.0f),
			new Vector(-243.0f, 25.0f),
			new Vector(-251.0f, 20.0f),
	};
	
	private Crate c1;
	private Crate c2;
	private Crate c3;
	private Crate c4;
	private Crate c5;
	private Crate c6;
	private Crate c7;
	private Crate c8;
	private Crate c9;
	private Vector posCrate1 = new Vector(9.0f, 5.0f);
	private Vector posCrate2 = new Vector(9.0f, 7.0f);
	private Vector posCrate3 = new Vector(9.0f, 6.0f);
	private Vector posCrate4 = new Vector(104.0f, -15.0f);
	private Vector posCrate5 = new Vector(106.0f, -15.0f);
	private Vector posCrate6 = new Vector(108.0f, -15.0f);
	private Vector posCrate7 = new Vector(110.0f, -15.0f);
	private Vector posCrate8 = new Vector(112.0f, -15.0f);
	private Vector posCrate9 = new Vector(114.0f, -15.0f);
	
	private Crate plateforme1;
	private Crate plateforme2;
	private Vector posPlateforme1= new Vector(0.0f, 3.0f);
	private Vector posPlateforme2= new Vector(133f, -18.0f);
	
	private Crate mur1;
	private Vector posMur1 = new Vector(238.0f, -20.0f);
	
	private Polygon polyBike = new Polygon (
			0.0f, 0.5f,
			0.5f, 1.0f,
			0.0f, 2.0f,
			-0.5f, 1.0f);
	private Bike bike;
	// Nous mettons le rayon des roues en final car celui-ci ne change pas
	private final float RAYON_ROUES = 0.5f;
	//private Vector posBike = new Vector(2.0f, 5.0f);
	private Vector posBike = new Vector(155.0f, -20.0f);
	
	private Finish flag;
	private float rayonFlag = 0.5f;
	private Vector posFlag = new Vector(255.0f, -19.5f);
	
	private Pendule pendule1;
	private Pendule pendule2;
	private Pendule pendule3;
	private Vector posPendule1 = new Vector(20.0f, -2.5f);
	private Vector posPendule2 = new Vector(195.0f, -15.5f);
	private Vector posPendule3 = new Vector(210.0f, -15.5f);
	
	private Bascule bascule1;
	private Vector posBascule1 = new Vector(46.0f, -0.25f);
	private Bascule bascule2;
	private Vector posBascule2 = new Vector(50.5f, -0.25f);
	private Bascule bascule3;
	private Vector posBascule3 = new Vector(67.0f, -0.35f);
	
	private Tremplin tremplin1;
	private Vector posTremplin1 = new Vector(163.0f, -21f);
	
	private TextGraphics msgCrash;
	
	public boolean begin(Window window, FileSystem fileSystem) {
		super.begin(window, fileSystem);
		this.fileSystem = fileSystem;
		
		// Nous dessinons un texte vide pour eviter que nos Macs crash environ
		// une seconde a l'affichage du 1er texte
		// de la partie. Sur nos PC, nous n'avons pas rencontre ce probleme
		msgCrash = new TextGraphics("", 0.1f, Color.BLACK);
		msgCrash.draw(window);

		terrain = new Terrain(this, true, Vector.ZERO, formeTerrain, new Color(139, 69, 19), new Color(0, 128, 0),
				0.15f, bushPos, 100.0f);
		terrainGlissant = new Terrain(this, true, new Vector(225.0f, -22.0f), formeTerrainGlissant, new Color(127, 140, 141),
				new Color(66, 66, 66), 0.0f, null, 0.5f);

		c1 = new Crate(this, fixed, posCrate1, 1, 1, "stone.broken.4.png", friction);
		c2 = new Crate(this, fixed, posCrate2, 1, 1, "stone.broken.1.png", friction);
		c3 = new Crate(this, fixed, posCrate3, 1, 1, "stone.broken.2.png", friction);
		c4 = new Crate(this, fixed, posCrate4, 1, 1, "blocker.dead.png", friction);
		c5 = new Crate(this, fixed, posCrate5, 1, 1, "blocker.happy.png", friction);
		c6 = new Crate(this, fixed, posCrate6, 1, 1, "blocker.dead.png", friction);
		c7 = new Crate(this, fixed, posCrate7, 1, 1, "blocker.happy.png", friction);
		c8 = new Crate(this, fixed, posCrate8, 1, 1, "blocker.dead.png", friction);
		c9 = new Crate(this, fixed, posCrate9, 1, 1, "blocker.happy.png", friction);
		
		pendule1 = new Pendule(this, true, posPendule1, 4.0f, 0.5f, friction);
		pendule2 = new Pendule(this, true, posPendule2, 4.0f, 0.5f, friction);
		pendule3 = new Pendule(this, true, posPendule3, -5.0f, 0.5f, friction);
		
		bascule1 = new Bascule(this, false, posBascule1, 3.5f, 0.5f, "wood.4.png", terrain, friction);
		bascule2 = new Bascule(this, false, posBascule2, 3.5f, 0.5f, "wood.4.png", terrain, friction);
		bascule3 = new Bascule(this, false, posBascule3, 6.5f, 0.7f, "wood.4.png", terrain, friction);
		
		plateforme1 = new Crate(this, true, posPlateforme1, 5, 0.7f, "wood.3.png", friction);
		plateforme2 = new Crate(this, true, posPlateforme2, 5, 1, "wood.3.png", friction);
		
		mur1 = new Crate(this, fixed, posMur1, 0.5f, 8.0f, "wood.3.png", friction);
		
		bike = new Bike(this, fixed, posBike, polyBike, RAYON_ROUES);
		
		tremplin1 = new Tremplin(this, true, posTremplin1, 2.0f, 1.0f, "jumper.normal.png");
		
		// Nous fixons la camera sur le bike
		setViewCandidate(bike);
		
		flag = new Finish(this, true, posFlag, rayonFlag, "flag.red.png");
		
		return true;
	}
	
	
	public void update(float deltaTime) {
		super.update(deltaTime);
		// Si nous appuyons sur R, nous remettons le jeu a 0 donc nous supprimons
		// tous les acteurs et nous relancons la methode begin
		if (super.getKeyboard().get(KeyEvent.VK_R).isReleased()) {
			super.destroyAllActor();
			begin((Window)getCanvas(), fileSystem);
			}
		// Nous affichons les messages en cas de chute
		if (bike.isHit()) {
			bike.afficheText();
			}
		// Nous lancons l'animation et rendons invincible le bike en cas de victoire
		if (flag.getWin()) {
			bike.celebration();
			bike.deleteListener();
			}
		}
}
