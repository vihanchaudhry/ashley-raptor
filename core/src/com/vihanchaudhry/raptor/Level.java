package com.vihanchaudhry.raptor;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.vihanchaudhry.raptor.components.AnimationComponent;
import com.vihanchaudhry.raptor.components.BackgroundComponent;
import com.vihanchaudhry.raptor.components.BoundsComponent;
import com.vihanchaudhry.raptor.components.CameraComponent;
import com.vihanchaudhry.raptor.components.MovementComponent;
import com.vihanchaudhry.raptor.components.RaptorComponent;
import com.vihanchaudhry.raptor.components.StateComponent;
import com.vihanchaudhry.raptor.components.StructureComponent;
import com.vihanchaudhry.raptor.components.TextureComponent;
import com.vihanchaudhry.raptor.components.TransformComponent;
import com.vihanchaudhry.raptor.systems.RenderingSystem;

/**
 * Created by Vihan on 1/10/2016.
 */
public class Level {
    public static final float WORLD_WIDTH = 10;
    public static final float WORLD_HEIGHT = 15 * 20; // I think the second number is the number of screens
    public static final int WORLD_STATE_RUNNING = 0;

    public float heightSoFar;
    public int state;
    public int score;

    private PooledEngine engine;

    public Level(PooledEngine engine) {
        this.engine = engine;
    }

    public void create() {
        Entity raptor = createRaptor();
        createCamera(raptor);
        createBackground();
        createStructure(StructureComponent.SIZE_SMALL, 3.0f, 10.0f);
        generateLevel();

        this.heightSoFar = 0;
        this.state = WORLD_STATE_RUNNING;
        this.score = 0;
    }

    private void generateLevel() {
        // create obstacles
        // create enemies
    }

    private void createCamera(Entity target) {
        Entity entity = engine.createEntity();

        CameraComponent camera = new CameraComponent();
        camera.camera = engine.getSystem(RenderingSystem.class).getCamera();
        camera.target = target;

        entity.add(camera);

        engine.addEntity(entity);
    }

    private Entity createRaptor() {
        Entity entity = engine.createEntity();

        AnimationComponent animation = engine.createComponent(AnimationComponent.class);
        BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
        MovementComponent movement = engine.createComponent(MovementComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        RaptorComponent raptor = engine.createComponent(RaptorComponent.class);
        StateComponent state = engine.createComponent(StateComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);

        animation.animations.put(RaptorComponent.STATE_NORMAL, Assets.shipNormal);

        bounds.bounds.width = RaptorComponent.WIDTH;
        bounds.bounds.height = RaptorComponent.HEIGHT;

        position.pos.set(5.0f, 2.5f, 0.0f);
        position.scale.set(2.0f / 3.0f, 2.0f / 3.0f);

        state.set(RaptorComponent.STATE_NORMAL);

        entity.add(animation);
        entity.add(bounds);
        entity.add(movement);
        entity.add(position);
        entity.add(raptor);
        entity.add(state);
        entity.add(texture);

        engine.addEntity(entity);

        return entity;
    }

    private void createStructure(int size, float x, float y) {
        Entity entity = new Entity();

        StructureComponent structure = engine.createComponent(StructureComponent.class);
        BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        StateComponent state = engine.createComponent(StateComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);

        bounds.bounds.width = StructureComponent.WIDTH;
        bounds.bounds.height = StructureComponent.HEIGHT;

        position.pos.set(x, y, 1.0f);

        texture.region = Assets.roofRegion;

        state.set(StructureComponent.STATE_ALIVE);

        structure.size = size;
        if (size == StructureComponent.SIZE_SMALL) {
            // small structure
        }
        else {
            // large structure
        }

        entity.add(structure);
        entity.add(bounds);
        entity.add(position);
        entity.add(state);
        entity.add(texture);

        engine.addEntity(entity);
    }

    private void createBackground() {
        Entity entity = engine.createEntity();

        BackgroundComponent background = engine.createComponent(BackgroundComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);

        texture.region = Assets.backgroundRegion;

        entity.add(background);
        entity.add(position);
        entity.add(texture);

        engine.addEntity(entity);
    }
}
