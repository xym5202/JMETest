package com.xym.jmetest.myselftest.Collidable;

import com.jme3.app.DebugKeysAppState;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.material.TechniqueDef;
import com.jme3.math.*;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import com.xym.jmetest.myselftest.Filter.CubeAppState;
import com.xym.jmetest.myselftest.Util.ConstantUtilTest;

/**
 * 利用射线检测，实现拾取
 */
public class HelloPickingTest extends SimpleApplication implements ActionListener {

    //空格键 切换摄像机模式
    public final static String CHANGE_CAM_MODE = "change_camera_mode";
    //鼠标左键：拾取
    public final static String PICKING = "pick";

    //准星
    private Spatial cross;

    //拾取标记
    private Spatial flag;

    //射线
    private Ray ray;

    public HelloPickingTest() {
        super(new FlyCamAppState(), new DebugKeysAppState(), new CubeAppState());

        //初始化射线
        ray = new Ray();
        //设置检测最远距离，可以将射线变为线段
        ray.setLimit(50);

    }


    public static void main(String[] args) {
        HelloPickingTest helloPickingTest=new HelloPickingTest();
        helloPickingTest.start();
    }


    @Override
    public void simpleInitApp() {
        //初始化摄像机
        flyCam.setMoveSpeed(10);
        cam.setLocation(new Vector3f(89.0993f, 10.044929f, -86.18647f));
        cam.setRotation(new Quaternion(0.063343525f, 0.18075047f, -0.01166729f, 0.9814177f));

        //设置灯光渲染模式为单通道，可以更加明亮
        renderManager.setPreferredLightMode(TechniqueDef.LightMode.SinglePass);
        renderManager.setSinglePassLightBatchSize(2);

        //做准星
        cross = makeCross();


        //做拾取标记
        flag = makeFlag();

        //用户输入
        inputManager.addMapping(PICKING, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping(CHANGE_CAM_MODE, new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(this, PICKING, CHANGE_CAM_MODE);
    }


    /**
     * 制作一个小球，用户标记拾取地点
     *
     * @return
     */
    private Spatial makeFlag() {
        Material material = new Material(assetManager, ConstantUtilTest.unShadedLoad);
        material.setColor("Color", ColorRGBA.Green);
        material.getAdditionalRenderState().setWireframe(true);

        Geometry geometry = new Geometry("flag", new Sphere(8, 8, 1));
        geometry.setMaterial(material);
        return geometry;
    }

    //在摄像头中央贴纸做准星
    private Spatial makeCross() {
        //采用GUI默认字体，做加好当准星
        BitmapText text = guiFont.createLabel("+");
        text.setColor(ColorRGBA.Red);

        //居中
        float x = (cam.getWidth() - text.getLineWidth()) * 0.5f;
        float y = (cam.getHeight() + text.getLineHeight()) * 0.5f;
        text.setLocalTranslation(x, y, 0);

        guiNode.attachChild(text);

        return text;
    }


    @Override
    public void onAction(String s, boolean isPressed, float tpf) {
        if (isPressed) {
            if (PICKING.equals(s)) {
                pick();
            } else if (CHANGE_CAM_MODE.equals(s)) {
                if (flyCam.isDragToRotate()) {
                    //设置自由模式
                    flyCam.setDragToRotate(false);
                    guiNode.attachChild(cross);
                } else {
                    //设置拖拽模式
                    flyCam.setDragToRotate(true);
                    cross.removeFromParent();
                }
            }
        }
    }

    /**
     * 使用射线检测，判断离摄像机最近的点
     */
    private void pick() {
        Ray ray= updateRay();
        CollisionResults collisionResults=new CollisionResults();

        Node cubeNode=stateManager.getState(CubeAppState.class).getRootNode();
        cubeNode.collideWith(ray,collisionResults);

        //打印结果
        print(collisionResults);

        /**
         * 判断检测结果
         */
        if (collisionResults.size()>0){
            //放置拾取标记
            Vector3f position=collisionResults.getClosestCollision().getContactPoint();
            flag.setLocalTranslation(position);

            if (flag.getParent()==null){
                rootNode.attachChild(flag);
            }
        }else {
            //移除标记
            if (flag.getParent()!=null){
                flag.removeFromParent();
            }

        }
    }

    /**
     * 更新射线对象 位置，方向，长度
     * @return
     */
    private Ray updateRay() {
        //使用摄像机位置作为原点
        ray.setOrigin(cam.getLocation());

        if (!flyCam.isDragToRotate()){
            /**
             * 自由模式下，直接使用摄像机计算射线方向
             */
            ray.setDirection(cam.getDirection());
        }else {
            /**
             * 拖拽模式下，通过鼠标位置计算射线方向
             */
            Vector2f screenCoord=inputManager.getCursorPosition();//鼠标在近平面的坐标
            Vector3f worldCoord=cam.getWorldCoordinates(screenCoord,1);

            //计算方向
            Vector3f d=worldCoord.subtract(cam.getLocation());
            d.normalizeLocal();
            ray.setDirection(d);
        }
        return  ray;
    }

    /**
     * 打印碰撞结果
     * @param results
     */
    private void print(CollisionResults  results){
        System.out.println("碰撞结果："+results.size());
        System.out.println("射线："+ray);

        if (results.size()>0){
            for (CollisionResult collisionResult:results){
                float dist=collisionResult.getDistance();
                Vector3f point=collisionResult.getContactPoint();
                Vector3f normal=collisionResult.getContactNormal();
                Geometry geometry=collisionResult.getGeometry();

                System.out.printf("距离：%.2f, 物体名称：%s, 交点：%s, 交点法线：%s\n", dist, geometry.getName(), point, normal);
            }

            //离射线原点最近的交点
            Vector3f closest=results.getClosestCollision().getContactPoint();
            //离射线原点最远的交点
            Vector3f farthest=results.getFarthestCollision().getContactPoint();

            System.out.printf("最近点：%s, 最远点：%s\n", closest, farthest);
        }
    }

}
