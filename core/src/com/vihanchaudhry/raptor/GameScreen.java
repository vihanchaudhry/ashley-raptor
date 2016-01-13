package com.vihanchaudhry.raptor;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.vihanchaudhry.raptor.systems.AnimationSystem;
import com.vihanchaudhry.raptor.systems.BackgroundSystem;
import com.vihanchaudhry.raptor.systems.BoundsSystem;
import com.vihanchaudhry.raptor.systems.CameraSystem;
import com.vihanchaudhry.raptor.systems.MovementSystem;
import com.vihanchaudhry.raptor.systems.RaptorSystem;
import com.vihanchaudhry.raptor.systems.RenderingSystem;
import com.vihanchaudhry.raptor.systems.StateSystem;
import com.vihanchaudhry.raptor.systems.StructureSystem;

/**
 * Created by Vihan on 1/10/2016.
 */
public class GameScreen extends ScreenAdapter {
    static final int GAME_READY = 0;
    static final int GAME_RUNNING = 1;

    Raptor game;
    Level level;
    PooledEngine engine;

    private int state;

    public GameScreen(Raptor game) {
        this.game = game;

        // TODO: Change to ready when updateRunning() and updateReady() are both done
        state = GAME_RUNNING;

        engine = new PooledEngine();

        level = new Level(engine);

        engine.addSystem(new RaptorSystem(level));
        engine.addSystem(new CameraSystem());
        engine.addSystem(new BackgroundSystem());
        engine.addSystem(new MovementSystem());
        engine.addSystem(new BoundsSystem());
        engine.addSystem(new StateSystem());
        engine.addSystem(new AnimationSystem());
        engine.addSystem(new StructureSystem());
        engine.addSystem(new RenderingSystem(game.batch));

        engine.getSystem(BackgroundSystem.class).setCamera(engine.getSystem(RenderingSystem.class).getCamera());

        level.create();

        // TODO: Change to pauseSystems() once first state is READY
        resumeSystems();
    }

    public void update (float deltaTime) {
        if (deltaTime > 0.1f) deltaTime = 0.1f;

        engine.update(deltaTime);

        switch (state) {
            case GAME_READY:
                updateReady();
                break;
            case GAME_RUNNING:
                updateRunning(deltaTime);
                break;
        }
    }

    private void updateReady () {
        if (Gdx.input.justTouched()) {
            state = GAME_RUNNING;
            resumeSystems();
        }
    }

    private void updateRunning(float deltaTime) {
        Application.ApplicationType appType = Gdx.app.getType();

        // should work also with Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer)
        float accelX = 0.0f;

        if (appType == Application.ApplicationType.Android || appType == Application.ApplicationType.iOS) {
            accelX = Gdx.input.getAccelerometerX();
        } else {
            if (Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)) accelX = 2.0f;
            if (Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)) accelX = -2.0f;
        }

        engine.getSystem(RaptorSystem.class).setAccelX(accelX);
    }

    private void presentReady () {
        // In Super Jumper:
        // game.batch.draw(Assets.ready, 160 - 192 / 2, 240 - 32 / 2, 192, 32);
    }

    private void presentRunning () {
        // nothing yet
    }

    private void pauseSystems() {
        engine.getSystem(RaptorSystem.class).setProcessing(false);
        engine.getSystem(StructureSystem.class).setProcessing(false);
        engine.getSystem(MovementSystem.class).setProcessing(false);
        engine.getSystem(BoundsSystem.class).setProcessing(false);
        engine.getSystem(StateSystem.class).setProcessing(false);
        engine.getSystem(AnimationSystem.class).setProcessing(false);
    }

    private void resumeSystems() {
        engine.getSystem(MovementSystem.class).setProcessing(true);
        engine.getSystem(StructureSystem.class).setProcessing(true);
        engine.getSystem(BoundsSystem.class).setProcessing(true);
        engine.getSystem(StateSystem.class).setProcessing(true);
        engine.getSystem(AnimationSystem.class).setProcessing(true);
    }

    public void draw() {
        game.batch.begin();
        switch(state) {
            case GAME_READY:
                presentReady();
                break;
            case GAME_RUNNING:
                presentRunning();
                break;
        }
        game.batch.end();
    }

    @Override
    public void render(float delta) {
        update(delta);
        draw();
    }
}
