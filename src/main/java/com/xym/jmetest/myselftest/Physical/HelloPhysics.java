package com.xym.jmetest.myselftest.Physical;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;
import com.xym.jmetest.myselftest.State.LightAppState;
import com.xym.jmetest.myselftest.Util.ConstantUtilTest;


/**
 * 按键发射小球轰击砖墙
 */
public class HelloPhysics extends SimpleApplication implements ActionListener {

    /**
     * 左键触发开火
     */
    public static final String FIRE = "fire";

    /**
     * 显示或隐藏BulletAppState的debug形状。按空格键触发。
     */
    public static final String DEBUG = "debug";

    /**
     * 砖块的尺寸
     */
    private static final float brickLength = 0.48f;
    private static final float brickWidth = 0.24f;
    private static final float brickHeight = 0.12f;

    private BulletAppState bulletAppState;

    public static void main(String[] args) {
        HelloPhysics app=new HelloPhysics();
        app.setShowSettings(false);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        cam.setLocation(new Vector3f(0, 4f, 6f));
        cam.lookAt(new Vector3f(2, 2, 0), Vector3f.UNIT_Y);

        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        stateManager.attach(new LightAppState());

        initKeys();
        //初始化场景
        initScene();


    }


    private void initScene() {
        makeFloor();
        makeWall();
    }

    /**
     * 做墙
     */
    private void makeWall() {
        //利用for循环生成众多砖块组成的墙
        float startpt = brickLength / 4;
        float height = 0;
        for (int j = 0; j < 15; j++) {
            for (int i = 0; i < 6; i++) {
                Vector3f v = new Vector3f(i * brickLength * 2 + startpt, brickHeight + height, 0);
                makeBrick(v);
            }
            startpt = -startpt;
            height += 2 * brickHeight;
        }

    }

    /**
     * 在指定位置放置一个物理砖块
     *
     * @param v
     */
    private void makeBrick(Vector3f v) {
        //网格
        Box box = new Box(brickLength, brickHeight, brickWidth);
        box.scaleTextureCoordinates(new Vector2f(1, 5));

        //材质
        Material material = new Material(assetManager, ConstantUtilTest.unShadedLoad);
        Texture t = assetManager.loadTexture("Textures/Terrain/BrickWall/BrickWall.jpg");
        material.setTexture("ColorMap", t);

        //几何体
        Geometry geometry = new Geometry("brick", box);
        geometry.setMaterial(material);
        geometry.setLocalTranslation(v);

        //刚体
        RigidBodyControl bodyControl = new RigidBodyControl(2);
        geometry.addControl(bodyControl);
        bodyControl.setCollisionShape(new BoxCollisionShape(new Vector3f(brickLength, brickHeight, brickWidth)));

        rootNode.attachChild(geometry);
        bulletAppState.getPhysicsSpace().add(bodyControl);


    }

    /**
     * 做地板
     */
    private void makeFloor() {
        Box floorbox = new Box(10f, 0.1f, 5f);
       floorbox.scaleTextureCoordinates(new Vector2f(3, 6));

        //材质
        Material material = new Material(assetManager, ConstantUtilTest.unShadedLoad);
        Texture texture = assetManager.loadTexture("Textures/Terrain/Pond/Pond.jpg");
        material.setTexture("ColorMap", texture);
        texture.setWrap(Texture.WrapMode.Repeat);

        Geometry geometry = new Geometry("floor", floorbox);
        geometry.setMaterial(material);
        geometry.setLocalTranslation(0, -0.05f, 0);//将地板下一一定距离，让表面与xoz重合

        //刚体
        RigidBodyControl rigidBodyControl = new RigidBodyControl(0);
        geometry.addControl(rigidBodyControl);
        rigidBodyControl.setCollisionShape(new BoxCollisionShape(new Vector3f(10f, 0.1f, 5f)));

        rootNode.attachChild(geometry);
        bulletAppState.getPhysicsSpace().add(rigidBodyControl);
    }

    @Override
    public void onAction(String s, boolean isPressed, float v) {
        if (isPressed){
            if (FIRE.equals(s)){
                shootBall();
            }else if (DEBUG.equals(s)){
                boolean debugEnabled=bulletAppState.isDebugEnabled();
                bulletAppState.setDebugEnabled(!debugEnabled);
            }
        }
    }

    /**
     * 初始化按键输入
     */
    private void initKeys() {
        inputManager.addMapping(FIRE, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping(DEBUG, new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(this, FIRE, DEBUG);
    }


    private void shootBall() {
        //网格
        Sphere sphere = new Sphere(32, 32, 0.4f, true, false);
        sphere.setTextureMode(Sphere.TextureMode.Projected);

        //材质
        Material material = new Material(assetManager, ConstantUtilTest.unShadedLoad);
        Texture texture = assetManager.loadTexture("Textures/Terrain/Rock/Rock.PNG");
        material.setTexture("ColorMap", texture);

        //几何体
        Geometry geometry = new Geometry("ball", sphere);
        geometry.setMaterial(material);

        //刚体
        RigidBodyControl rigidBodyControl = new RigidBodyControl(1);
        geometry.addControl(rigidBodyControl);
        rigidBodyControl.setCollisionShape(new SphereCollisionShape(0.4f));
        rigidBodyControl.setPhysicsLocation(cam.getLocation());
        rigidBodyControl.setLinearVelocity(cam.getDirection().mult(25));//设定初速度

        rootNode.attachChild(geometry);
        bulletAppState.getPhysicsSpace().add(rigidBodyControl);

    }

}
