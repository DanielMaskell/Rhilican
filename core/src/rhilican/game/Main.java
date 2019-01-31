package rhilican.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.util.HashMap;
import java.util.Map;

public class Main extends ApplicationAdapter implements InputProcessor {

	final HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();

	private Map<Integer, TouchInfo> touches = new HashMap<Integer, TouchInfo>();

	private Animation animation;
	private OrthographicCamera camera;
	private ExtendViewport viewport;
	private TextureAtlas textureAtlas;
	private SpriteBatch batch;
	private BitmapFont font;
	private int w,h;
	private String message = "Touch something slaready!";
	private Texture map;
	private Texture buttons;
	private TiledMapRenderer tiledMapRenderer;

	private TiledMap tiledMap;

	private float elapsedTime = 0;

	//Camera Movement
	private float cameraX = 0;
	private float cameraY = 0;

	//Player Movement
	private float playerXVel = 0;
	private float playerYVel = 0;
	private boolean move = false;
	private float playerSpeed = 400;


	class TouchInfo{
		public float touchX = 0;
		public float touchY = 0;
		public boolean touched = false;
	}

	@Override
	public void create () {

		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera();
		camera.setToOrtho(false,w,h);
		camera.update();

		tiledMap = new TmxMapLoader().load("rehilicanTestMap2.tmx");
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

		viewport = new ExtendViewport(w, h, camera);

		textureAtlas = new TextureAtlas(Gdx.files.internal("sprites.txt"));

		batch = new SpriteBatch();
		animation = new Animation(1/5f, textureAtlas.getRegions());

		font = new BitmapFont();
		font.setColor(Color.RED);

		buttons = new Texture(Gdx.files.internal("test3.png"));

		Gdx.input.setInputProcessor(this);
		for(int i = 0; i < 5; i++){
			touches.put(i, new TouchInfo());
		}

		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		elapsedTime += Gdx.graphics.getDeltaTime();

		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();

		batch.begin();

		cameraMove();

		camera.update();

		message = "";
		for(int i = 0; i < 5; i++){
			if(touches.get(i).touched)
				message += "Finger:" + Integer.toString(i) + "touch at:" +
						Float.toString(touches.get(i).touchX) +
						"," +
						Float.toString(touches.get(i).touchY) +
						"\n";
		}

		GlyphLayout layout = new GlyphLayout();
		layout.setText(font, message);
		float x = w/2.0f - layout.width/2;
		float y = h/2.0f + layout.height/2;

		font.draw(batch, message, x, y);

		// Draws the animation
		batch.draw((TextureRegion) animation.getKeyFrame(elapsedTime, true), x-50, y+400);

		batch.draw(buttons, 0, 0);

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		textureAtlas.dispose();
		font.dispose();
	}

	@Override
	public void resize(int width, int height){
		viewport.update(width, height, true);

		batch.setProjectionMatrix(camera.combined);
	}

	private void drawSprite(String name, float x, float y){
		Sprite sprite = textureAtlas.createSprite(name);

		//Set sprite postion
		sprite.setPosition(x, y);

		sprite.draw(batch);
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		/**if(pointer < 5){
			touches.get(pointer).touchX = screenX;
			touches.get(pointer).touchY = screenY;
			touches.get(pointer).touched = true;
		}**/
		// Right button
		if(737 < screenX && screenX < 970 && 1620 < screenY && screenY < 1755){
			playerXVel = playerSpeed;
            move = true;
		}

		//Left Button
		else if(80 < screenX && screenX < 340 && 1620 < screenY && screenY < 1755){
			playerXVel = -playerSpeed;
			move = true;
		}

		//Up Button
		else if (475 < screenX && screenX < 600 && 1335 < screenY && screenY < 1590){
			playerYVel = playerSpeed;
			move = true;
		}

		//Down Button
		else if(475 < screenX && screenX < 600 && 1805 < screenY && screenY < 2060){
			playerYVel = -playerSpeed;
			move = true;
		}
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(pointer < 5){
			touches.get(pointer).touchX = 0;
			touches.get(pointer).touchY = 0;
			touches.get(pointer).touched = false;
		}

        if(737 < screenX && screenX < 970 && 1620 < screenY && screenY < 1755){
            playerXVel = 0;
            move = false;
        }

        //Left Button
        else if(80 < screenX && screenX < 340 && 1620 < screenY && screenY < 1755){
            playerXVel = 0;
            move = false;
        }

        //Up Button
        else if (475 < screenX && screenX < 600 && 1335 < screenY && screenY < 1590){
            playerYVel = 0;
            move = false;
        }

        //Down Button
        else if(475 < screenX && screenX < 600 && 1805 < screenY && screenY < 2060){
            playerYVel = 0;
            move = false;
        }

		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	public void cameraMove(){

		if(move == true){

			System.out.println("X= "+playerXVel + " " +
					"  Y= " + playerYVel);

			cameraX =+ playerXVel*Gdx.graphics.getDeltaTime();						//Adds players velocity to the camera location.
			cameraY =+ playerYVel*Gdx.graphics.getDeltaTime();

			camera.translate(cameraX, cameraY);			//Moves the camera with new location.
		}


	}
}
