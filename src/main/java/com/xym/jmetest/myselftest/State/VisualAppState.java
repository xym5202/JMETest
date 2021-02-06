package com.xym.jmetest.myselftest.State;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.xym.jmetest.myselftest.Util.ConstantUtilTest;

/**
 * 子场景管理
 */
public class VisualAppState implements AppState {

    private boolean initialized = false;
    private boolean enabled = true;

    /**
     * 创建一个独立的子节点
     */
    private Node node = new Node("MyScene");

    /**
     * 构建几何
     */
    private Geometry geometry = null;


    private SimpleApplication simpleApplication;
    private AssetManager assetManager;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        this.simpleApplication = (SimpleApplication) app;
        this.assetManager = app.getAssetManager();

        //创建方块
        Material material = new Material(assetManager, ConstantUtilTest.lightingLoad);
        material.setColor(ConstantUtilTest.Diffuse, ColorRGBA.Blue);
        material.setColor(ConstantUtilTest.Ambient, ColorRGBA.Red);
        material.setColor(ConstantUtilTest.Specular, ColorRGBA.Black);
        material.setFloat(ConstantUtilTest.Shininess, 1);
        material.setBoolean(ConstantUtilTest.UseMaterialColors, true);

        Mesh mesh = new Box(1, 1, 1);
        geometry = new Geometry("物体", mesh);
        geometry.setMaterial(material);
//        ChaseCameraAppState chaseCameraAppState=new  ChaseCameraAppState();
//        chaseCameraAppState.setTarget(geometry);
//        stateManager.attach(chaseCameraAppState);
        node.attachChild(geometry);
        initialized = true;
        if (enabled) {
            simpleApplication.getRootNode().attachChild(node);
        }
    }

    public Geometry getGeometry(){
        return geometry;
    }

    public Node getNode(){
        return node;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void setEnabled(boolean active) {
        if (this.enabled == active) {
            return;
        }
        this.enabled = active;
        if (!initialized)
            return;

        if (enabled) {
            simpleApplication.getRootNode().attachChild(node);
        } else {
            node.removeFromParent();
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
        geometry.rotate(0, tpf * FastMath.PI, 0);
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
            node.removeFromParent();
        }
        initialized = false;
    }
}

