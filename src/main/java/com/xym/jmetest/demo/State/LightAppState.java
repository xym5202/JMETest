package com.xym.jmetest.demo.State;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class LightAppState extends BaseAppState {

    private Node rootNode;
    private AssetManager assetManager;
    private AmbientLight ambientLight;
    private DirectionalLight directionalLight;

    @Override
    protected void initialize(Application application) {
        this.rootNode = ((SimpleApplication) application).getRootNode();
        this.assetManager = application.getAssetManager();
        ambientLight = new AmbientLight();
        ambientLight.setColor(new ColorRGBA(0.7f, 0.7f, 0.7f, 1f));

        directionalLight = new DirectionalLight();
        directionalLight.setDirection(new Vector3f(-1, -2, -3).normalizeLocal());


    }

    @Override
    protected void cleanup(Application application) {

    }

    @Override
    protected void onEnable() {
        rootNode.addLight(ambientLight);
        rootNode.addLight(directionalLight);
    }

    @Override
    protected void onDisable() {
     rootNode.removeLight(ambientLight);
     rootNode.removeLight(directionalLight);
    }
}
