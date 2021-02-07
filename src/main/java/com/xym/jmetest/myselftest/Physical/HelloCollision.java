package com.xym.jmetest.myselftest.Physical;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;


public class HelloCollision extends SimpleApplication implements ActionListener {
    /**
     * 显示或隐藏BulletAppState的debug形状，按1键触发
     */
    public static final String DEBUG = "debug";

    //前后左右跳跃
    public static final String FORWARD = "forward";
    public static final String BACKWARD = "backward";
    public static final String LEFT = "left";
    public static final String RIGHT = "right";
    public static final String JUMP = "jump";

    private BulletAppState bulletAppState;

    private Node character;

    //角色控制器
    private CharacterControl player;
    private Vector3f walkDirection = new Vector3f();
    private boolean left = false, right = false, up = false, down = false;

    //临时变量，用于保存摄像机的方向，避免在simpleUpdate中重复创建对象
    private Vector3f camDir = new Vector3f();
    private Vector3f camLeft = new Vector3f();


    public static void main(String[] args) {
        HelloCollision app=new HelloCollision();
        app.setShowSettings(false);
        app.start();
    }



    @Override
    public void simpleInitApp() {


        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);

        //初始化按键
        initKeys();

        //初始化光照
        initLights();

        //初始化场景
        initScene();

        //初始化玩家
        initPlayer();
    }

    @Override
    public void  simpleUpdate(float tpf){

        camDir.set(cam.getDirection()).multLocal(0.3f);
        camLeft.set(cam.getLeft().multLocal(0.4f));
        walkDirection.set(0,0,0);

        //计算运动方向
        boolean changed=false;
        if (left){
            walkDirection.addLocal(camLeft);
            changed=true;
        }
        if (right){
            walkDirection.addLocal(camLeft.negate());
            changed=true;
        }
        if (up){
            walkDirection.addLocal(camDir);
            changed=true;
        }
        if (down){
            walkDirection.addLocal(camDir.negate());
            changed=true;
        }

        if (changed){
            walkDirection.y=0;//将行走速度的方向限制在水平面上
            walkDirection.normalizeLocal();//计算行走的单位向量
            walkDirection.multLocal(0.1f);//改变速率
        }
      player.setWalkDirection(walkDirection);
        cam.setLocation(player.getPhysicsLocation());
    }




    /**
     * 初始化玩家
     */
    private void initPlayer() {
        float radius=0.3f;
        float height=0.8f;
        int axis=1;
        float stepHeight = 0.5f;//玩家能登上多高的台阶

        //创建角色根节点
        character =new Node("character");
        rootNode.attachChild(character);

        //加载角色模型
        Spatial model =assetManager.loadModel("Models/Jaime/Jaime.j3o");
        model.move(0,-(height/2+radius),0);
        model.scale(1.8f);
        character.attachChild(model);//将模型挂在到角色根节点下

        //使用胶囊体作为玩家的碰撞形状
        CapsuleCollisionShape capsuleCollisionShape = new CapsuleCollisionShape(radius, height, axis);

        //使用CharactorControl来控制玩家物体
        player = new CharacterControl(capsuleCollisionShape, stepHeight);
        character.addControl(player);
        player.setJumpSpeed(10);//起跳速度
        player.setFallSpeed(55);//坠落速度
        player.setGravity(new Vector3f(0, -9.8f*3, 0));//重力加速度
        player.setPhysicsLocation(new Vector3f(0, 2, 0));//位置

        bulletAppState.getPhysicsSpace().add(player);
    }

    /**
     * 初始化场景
     */
    private void initScene() {
        //从zip文件中加载地图场景
        assetManager.registerLocator("town.zip", ZipLocator.class);
        Spatial sceneModel = assetManager.loadModel("main.scene");

        //为地图创建精确网格形状
        CollisionShape collisionShape = CollisionShapeFactory.createMeshShape(sceneModel);
        RigidBodyControl landSpace = new RigidBodyControl(collisionShape, 0);

        sceneModel.addControl(landSpace);

        rootNode.attachChild(sceneModel);
        bulletAppState.getPhysicsSpace().add(landSpace);
    }

    /**
     * 初始化光照
     */
    private void initLights() {
        //环境光
        AmbientLight ambientLight = new AmbientLight();
        ambientLight.setColor(new ColorRGBA(0.3f, 0.3f, 0.3f, 1));

        //阳光
        DirectionalLight directionalLight = new DirectionalLight();
        directionalLight.setDirection(new Vector3f(-1, -2, -3).normalizeLocal());

        rootNode.addLight(ambientLight);
        rootNode.addLight(directionalLight);
    }

    /**
     * 初始化按键
     */

    private void initKeys() {
        inputManager.addMapping(DEBUG, new KeyTrigger(KeyInput.KEY_1));
        inputManager.addMapping(FORWARD, new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping(BACKWARD, new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping(LEFT, new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping(RIGHT, new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping(JUMP, new KeyTrigger(KeyInput.KEY_SPACE));

        inputManager.addListener(this, DEBUG, FORWARD, BACKWARD, LEFT, RIGHT, JUMP);
    }


    @Override
    public void onAction(String s, boolean isPressed, float v) {
        if (DEBUG.equals(s) && isPressed) {
            boolean debugEnabled = bulletAppState.isDebugEnabled();
            bulletAppState.setDebugEnabled(!debugEnabled);
        } else if (FORWARD.equals(s)) {
            up = isPressed;
        } else if (BACKWARD.equals(s)) {
            down = isPressed;
        } else if (LEFT.equals(s)) {
            left = isPressed;
        } else if (RIGHT.equals(s)) {
            right = isPressed;
        } else if (JUMP.equals(s)&& isPressed) {
            player.jump(new Vector3f(0, 2f, 0));
        }

    }
}
