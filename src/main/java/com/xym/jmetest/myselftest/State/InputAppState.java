package com.xym.jmetest.myselftest.State;

import com.jme3.app.Application;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.renderer.RenderManager;

/**
 * 键盘鼠标输入模块
 */
public  class InputAppState implements AppState, ActionListener {

    //电灯开关
    public final static String SWITCH_LIGHT = "switch_light";
    public final static Trigger TRIGGER_KEY_T = new KeyTrigger(KeyInput.KEY_T);

    //子场景开关
    public final static String TOGGLE_SUBSCENE = "toggle_subscene";
    public final static Trigger TRIGGER_KEY_G = new KeyTrigger(KeyInput.KEY_G);

    private boolean initialized = false;
    private boolean enabled = true;

    /**
     * 保存我们所需要的系统对象
     */
    private InputManager inputManager;
    private AppStateManager appStateManager;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        this.appStateManager = stateManager;
        this.inputManager = app.getInputManager();
        initialized = true;
        if (enabled) {
            addInputs();
        }

    }

    /**
     * 添加输入
     */
    public void addInputs() {
        inputManager.addMapping(SWITCH_LIGHT, TRIGGER_KEY_T);
        inputManager.addMapping(TOGGLE_SUBSCENE, TRIGGER_KEY_G);
        inputManager.addListener(this, SWITCH_LIGHT, TOGGLE_SUBSCENE);
    }

    /**
     * 移除输入
     */
    public void removeInputs() {
        inputManager.deleteTrigger(SWITCH_LIGHT, TRIGGER_KEY_T);
        inputManager.deleteTrigger(TOGGLE_SUBSCENE, TRIGGER_KEY_G);
        inputManager.removeListener(this);
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void setEnabled(boolean active) {
        if (this.enabled = active) {
            return;
        }
        this.enabled = active;
        if (!initialized) return;
        if (enabled) {
            addInputs();
        } else {
            removeInputs();
        }
    }


    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void stateAttached(AppStateManager stateManager) {

    }

    @Override
    public void stateDetached(AppStateManager stateManager) {

    }

    @Override
    public void update(float tpf) {

    }

    @Override
    public void render(RenderManager rm) {

    }

    @Override
    public void postRender() {

    }

    @Override
    public void cleanup() {
        if (enabled) {
            removeInputs();
        }
        initialized = false;
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (isPressed) {
            if (SWITCH_LIGHT.equals(name)) {
                LightAppState lightAppState = appStateManager.getState(LightAppState.class);
                if (lightAppState != null) {
                    if (!lightAppState.isEnabled()) {
                        System.out.println("开灯");
                    } else {
                        System.out.println("关灯");
                    }
                    lightAppState.setEnabled(!lightAppState.isEnabled());
                }
            }else if (TOGGLE_SUBSCENE.equals(name)) {
                VisualAppState visualAppState = appStateManager.getState(VisualAppState.class);
                if (!visualAppState.isEnabled()) {
                    System.out.println("显示物体");
                } else {
                    System.out.println("关闭显示物体");
                }
                if (visualAppState != null) {
                    visualAppState.setEnabled(!visualAppState.isEnabled());
                }
            }
        }
    }
}