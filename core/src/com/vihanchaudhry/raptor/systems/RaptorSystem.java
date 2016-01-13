package com.vihanchaudhry.raptor.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.vihanchaudhry.raptor.Level;
import com.vihanchaudhry.raptor.components.MovementComponent;
import com.vihanchaudhry.raptor.components.RaptorComponent;
import com.vihanchaudhry.raptor.components.StateComponent;
import com.vihanchaudhry.raptor.components.TransformComponent;

/**
 * Created by Vihan on 1/10/2016.
 */
public class RaptorSystem extends IteratingSystem {
    private static final Family family = Family.all(RaptorComponent.class,
            StateComponent.class,
            TransformComponent.class,
            MovementComponent.class).get();

    private float accelX = 0.0f;
    private Level level;

    private ComponentMapper<RaptorComponent> rm;
    private ComponentMapper<StateComponent> sm;
    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<MovementComponent> mm;

    public RaptorSystem(Level level) {
        super(family);

        this.level = level;

        rm = ComponentMapper.getFor(RaptorComponent.class);
        sm = ComponentMapper.getFor(StateComponent.class);
        tm = ComponentMapper.getFor(TransformComponent.class);
        mm = ComponentMapper.getFor(MovementComponent.class);
    }

    public void setAccelX(float accelX) {
        this.accelX = accelX;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        accelX = 0.0f;
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        TransformComponent t = tm.get(entity);
        StateComponent state = sm.get(entity);
        MovementComponent mov = mm.get(entity);
        RaptorComponent raptor = rm.get(entity);

        // Movement handling
        if (state.get() == RaptorComponent.STATE_NORMAL) {
            mov.velocity.x = -accelX / 10.0f * RaptorComponent.MOVE_VELOCITY_X * deltaTime;
            mov.velocity.y = RaptorComponent.MOVE_VELOCITY_Y * deltaTime;
        }

        // Bounds checking
        if (t.pos.x - raptor.WIDTH / 2 < 0) {
            t.pos.x = raptor.WIDTH / 2;
        }

        if (t.pos.x + raptor.WIDTH / 2 > Level.WORLD_WIDTH) {
            t.pos.x = Level.WORLD_WIDTH - raptor.WIDTH / 2;
        }

        // Tilting
        if (mov.velocity.x > 0) {
            t.rotation = -0.1f;
        }

        if (mov.velocity.x < 0) {
            t.rotation = 0.1f;
        }

        if (mov.velocity.x == 0) {
            t.rotation = 0.0f;
        }

        raptor.heightSoFar = t.pos.y;
    }
}
