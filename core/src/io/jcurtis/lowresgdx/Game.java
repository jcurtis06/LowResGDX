package io.jcurtis.lowresgdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * A minimal example of how I set up pixel-perfect rendering in LibGDX.
 * This may not be the best way to do it, but it works for me.
 */
public class Game extends ApplicationAdapter {
	// The virtual resolution of the game in pixels
	// Lower values will zoom in, higher values will zoom out
	private static final int VIRTUAL_WIDTH = 320;
	private static final int VIRTUAL_HEIGHT = 180;

	SpriteBatch batch;

	// The texture and sprite used to render the player
	Texture img;
	Sprite sprite;

	// The FrameBuffer used to scale the game to the screen
	FrameBuffer frameBuffer;

	// The camera used to render the scene. Do NOT move this camera around.
	// It will always look at (VIRTUAL_WIDTH/2, VIRTUAL_HEIGHT/2) and have the same size as the virtual screen
	OrthographicCamera fboCamera;

	// the camera used within the game
	OrthographicCamera camera;

	// The viewport used to scale the game to the screen
	Viewport viewport;

	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("slime.png");

		// Create both cameras
		fboCamera = new OrthographicCamera();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

		// Create the viewport
		viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, fboCamera);
		viewport.apply();

		// Create a new FrameBuffer with a specific resolution
		frameBuffer = new FrameBuffer(Pixmap.Format.RGB888, VIRTUAL_WIDTH, VIRTUAL_HEIGHT, false);
		frameBuffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

		// Create the player sprite
		sprite = new Sprite(img);
	}

@Override
public void render () {
	// Move the player to the right and rotate it
	sprite.setX(sprite.getX()+1);
	sprite.rotate(1);
	System.out.println(sprite.getX());

	// Move the camera to the player
	camera.position.x = sprite.getX()+sprite.getWidth()/2;
	camera.position.y = sprite.getY()+sprite.getHeight()/2;

	// Always update the camera after changing its position
	camera.update();

	// Start rendering to the FrameBuffer
	frameBuffer.begin();

	// Clear the screen with a white color
	Gdx.gl.glClearColor(1, 1, 1, 1);
	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

	// Set the projection matrix to the camera's combined matrix
	batch.setProjectionMatrix(camera.combined);

	// Begin the batch
	batch.begin();
	// Everything in here will be rendered to the FrameBuffer
	// Thus making it pixel-perfect
	// For now, we'll just render the player sprite
	sprite.draw(batch);
	// End the batch
	batch.end();

	// Stop rendering to the FrameBuffer
	frameBuffer.end();

	// Clear the screen with a black color
	Gdx.gl.glClearColor(0, 0, 0, 1);
	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

	viewport.apply();
	batch.setProjectionMatrix(fboCamera.combined);
	fboCamera.setToOrtho(false, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

	// Render the FrameBuffer to the screen, scaling it to the screen's resolution
	batch.begin();
	batch.draw(frameBuffer.getColorBufferTexture(), 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight(), 0, 0, 1, 1);
	batch.end();
}

	@Override
	public void resize (int width, int height) {
		viewport.update(width, height);
		fboCamera.update();
	}

	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		frameBuffer.dispose();
	}
}
