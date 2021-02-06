package com.xym.jmetest.myselftest.State;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;

/**
 * 管理灯光
 */
public class LightAppState implements AppState {

    private boolean initialized = false;
    private boolean enabled = true;

    //点光源配置
    private Vector3f lightPos;
    private ColorRGBA pointLightColor;
    private PointLight pointLight;

    //环境光
    private AmbientLight ambientLight;

    //直射光
    private DirectionalLight directionalLight;

    private Node rootNode;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        SimpleApplication simpleApplication = (SimpleApplication) app;
        this.rootNode = simpleApplication.getRootNode();

        //设置点光源
        lightPos = new Vector3f(50, 50, 50);
        pointLightColor = new ColorRGBA(0.8f, 0.8f, 0.8f, 1);
        pointLight = new PointLight(lightPos, pointLightColor);
        pointLight.setRadius(1000);

        //设置环境光
        ColorRGBA ambientLightColor = new ColorRGBA(0.4f, 0.4f, 0.4f, 1);
        ambientLight = new AmbientLight(ambientLightColor);

        app.getViewPort().setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 0.85f, 1f));

        directionalLight =new DirectionalLight(new Vector3f(-1, -1, 1));
        directionalLight.setColor(new ColorRGBA(0.6f,0.6f,0.6f,1));

        initialized = true;
        if (enabled)
            turnOn();
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    public void turnOn() {
        rootNode.addLight(pointLight);
        rootNode.addLight(ambientLight);
        rootNode.addLight(directionalLight);
    }

    public void turnOff() {
        rootNode.removeLight(pointLight);
        rootNode.removeLight(ambientLight);
        rootNode.removeLight(directionalLight);
    }

    @Override
    public void setEnabled(boolean active) {
        if (this.enabled == active) {
            return;
        }
        this.enabled = active;
        if (!initialized) return;
        if (enabled) {
            turnOn();
        } else {
            turnOff();
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
        if (enabled)
            turnOff();
        initialized = false;
    }
}
