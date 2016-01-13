package com.vihanchaudhry.raptor.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.vihanchaudhry.raptor.components.StateComponent;
import com.vihanchaudhry.raptor.components.StructureComponent;
import com.vihanchaudhry.raptor.components.TransformComponent;

/**
 * Created by Vihan on 1/11/2016.
 */
public class StructureSystem extends IteratingSystem {
    private static final Family family = Family.all(StructureComponent.class,
            StateComponent.class,
            TransformComponent.class).get();
    private Engine engine;

    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<StructureComponent> rm;
    private ComponentMapper<StateComponent> sm;

    public StructureSystem() {
        super(Family.all(StructureComponent.class).get());

        tm = ComponentMapper.getFor(TransformComponent.class);
        rm = ComponentMapper.getFor(StructureComponent.class);
        sm = ComponentMapper.getFor(StateComponent.class);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        this.engine = engine;
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        StateComponent state = sm.get(entity);
        TransformComponent t = tm.get(entity);

        if (state.get() == StructureComponent.STATE_DEAD) {
            engine.removeEntity(entity);
        }
    }

    public void die(Entity entity) {
        if (family.matches(entity)) {
            StateComponent state = sm.get(entity);
            state.set(StructureComponent.STATE_DEAD);
        }
    }
}
