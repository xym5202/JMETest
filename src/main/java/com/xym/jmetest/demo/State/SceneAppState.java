package com.xym.jmetest.demo.State;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * 场景管理模块
 */
public class SceneAppState extends BaseAppState {

    private Node rootNode;

    private Spatial sceneModel;
    private RigidBodyControl landScape;

    private AssetManager assetManager;

    @Override
    protected void initialize(Application application) {
        this.assetManager=application.getAssetManager();
        this.rootNode=((SimpleApplication)getApplication()).getRootNode();

        //从zip文件中加载地图场景
        assetManager.registerLocator("town.zip", ZipLocator.class);
        this.sceneModel=assetManager.loadModel("main.scene");

        //为地图创建精确网格形状
        CollisionShape sceneShaper= CollisionShapeFactory.createMeshShape(sceneModel);
        this.landScape=new RigidBodyControl(sceneShaper,0);
        sceneModel.addControl(landScape);
    }

    @Override
    protected void cleanup(Application application) {

    }

    @Override
    protected void onEnable() {
        rootNode.attachChild(sceneModel);
        BulletAppState bulletAppState=getStateManager().getState(BulletAppState.class);
        if (bulletAppState!=null){
            bulletAppState.getPhysicsSpace().add(landScape);
        }
    }

    @Override
    protected void onDisable() {
        sceneModel.removeFromParent();
        PhysicsSpace space=landScape.getPhysicsSpace();
        if (space!=null){
            space.remove(landScape);
        }
    }
}
