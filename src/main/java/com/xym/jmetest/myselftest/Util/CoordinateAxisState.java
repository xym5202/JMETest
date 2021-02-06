package com.xym.jmetest.myselftest.Util;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.debug.Grid;

public class CoordinateAxisState extends BaseAppState implements ActionListener {

    private AssetManager assetManager;

    private Node node = new Node("AxisNode");

    public static final String TOOGLE_AXIS = "toogle_axis";

    private InputManager inputManager;

    private AppStateManager appStateManager;
    @Override
    protected void initialize(Application app) {
        assetManager=app.getAssetManager();
        inputManager =app.getInputManager();

        //调用其他的State，需要在其他的State初始化后调用
        //VisualAppState visualAppState=appStateManager.getState(VisualAppState.class);

        Geometry geometry=new Geometry("Grid",new Grid(21,21,1));
        Material material=new Material(assetManager, ConstantUtilTest.unShadedLoad);
        material.setColor("Color", ColorRGBA.DarkGray);
        geometry.setMaterial(material);
        geometry.center().move(0,0,0);
        geometry.setShadowMode(RenderQueue.ShadowMode.Off);

        node.attachChild(geometry);

        createArrow("X", Vector3f.UNIT_X.mult(5f), ColorRGBA.Red);
        createArrow("Y", Vector3f.UNIT_Y.mult(5f), ColorRGBA.Green);
        createArrow("Z", Vector3f.UNIT_Z.mult(5f), ColorRGBA.Blue);

        inputManager.addMapping(TOOGLE_AXIS, new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(this,TOOGLE_AXIS);
    }

    /**
     * 坐标轴开关
     *
     * @return
     */
    public boolean toggleAxis() {
        SimpleApplication application = (SimpleApplication) getApplication();
        if (application.getRootNode().hasChild(node)) {
            node.removeFromParent();
            return false;
        } else {
            application.getRootNode().attachChild(node);
            return true;
        }
    }

    public void createArrow(String name, Vector3f vector3f, ColorRGBA colorRGBA) {
        //创建材质
        Material material = new Material(assetManager, ConstantUtilTest.unShadedLoad);
        material.setColor("Color", colorRGBA);
        material.getAdditionalRenderState().setLineWidth(3f);
        material.getAdditionalRenderState().setWireframe(true);

        //创建箭头网格
        Geometry geometry = new Geometry("Axis_" + name, new Arrow(vector3f));
        geometry.setMaterial(material);
        geometry.setShadowMode(RenderQueue.ShadowMode.Off);

        node.attachChild(geometry);
    }

    ;

    @Override
    protected void cleanup(Application app) {

    }

    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (isPressed) {
            if(TOOGLE_AXIS.equals(name)){
                toggleAxis();
            }
        }
    }
}
