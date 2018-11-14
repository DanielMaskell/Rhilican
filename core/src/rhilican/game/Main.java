package rhilican.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.util.HashMap;

public class Main extends ApplicationAdapter {

	final HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();

	Animation animation;

	OrthographicCamera camera;

	ExtendViewport viewport;

	TextureAtlas textureAtlas;

	private float elapsedTime = 0;

	SpriteBatch batch;
	
	@Override
	public void create () {
		camera = new OrthographicCamera();
		viewport = new ExtendViewport(800, 600, camera);

		textureAtlas = new TextureAtlas(Gdx.files.internal("sprites.txt"));

		batch = new SpriteBatch();
		animation = new Animation(1/5f, textureAtlas.getRegions());
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		elapsedTime += Gdx.graphics.getDeltaTime();
		batch.begin();

		// Draws the animation
		batch.draw((TextureRegion) animation.getKeyFrame(elapsedTime, true), 0, 0);

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();

		textureAtlas.dispose();
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
}
