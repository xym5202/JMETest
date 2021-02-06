package com.xym.jmetest.myselftest.BoundingVolume;

import com.jme3.app.SimpleApplication;
import com.jme3.bounding.BoundingSphere;
import com.jme3.collision.CollisionResults;
import com.jme3.input.RawInputListener;
import com.jme3.input.event.*;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.xym.jmetest.myselftest.Util.ConstantUtilTest;
import com.xym.jmetest.myselftest.Util.CoordinateAxisState;

/**
 * 下面我们分别使用方块和球体来进行碰撞检测。
 * 方块使用BoundingBox作为包围体，固定于坐标系中间不动；
 * 球体使用BoundingSphere作为包围体，你可以通过鼠标来控制球体的位置。
 * 当球体与方块相交时，球体的颜色会发生改变。
 */
public class TestCollisionWith extends SimpleApplication implements RawInputListener {

    private Geometry green;
    private Geometry pink;


    public TestCollisionWith() {
        super(new CoordinateAxisState());
    }

    public static void main(String[] args) {
        TestCollisionWith app = new TestCollisionWith();
        app.setShowSettings(false);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        cam.setLocation(new Vector3f(0, 18, 22));
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);

        viewPort.setBackgroundColor(ColorRGBA.White);

        //绿色物体
        Material material = new Material(assetManager, ConstantUtilTest.unShadedLoad);
        material.setColor("Color", ColorRGBA.Green);

        green = new Geometry("Green", new Sphere(6, 12, 1));
        green.setMaterial(material);
        //使用包围球
        green.setModelBound(new BoundingSphere());
        green.updateModelBound();//更新包围球


        //粉色物体
        material = new Material(assetManager, ConstantUtilTest.unShadedLoad);
        material.setColor("Color", ColorRGBA.Pink);
        pink = new Geometry("pink", new Box(1, 1, 1));
        pink.setMaterial(material);

        rootNode.attachChild(green);
        rootNode.attachChild(pink);

        inputManager.addRawInputListener(this);

    }

    @Override
    public void beginInput() {

    }

    @Override
    public void endInput() {

    }

    @Override
    public void onJoyAxisEvent(JoyAxisEvent joyAxisEvent) {

    }

    @Override
    public void onJoyButtonEvent(JoyButtonEvent joyButtonEvent) {

    }

    @Override
    public void onMouseMotionEvent(MouseMotionEvent mouseMotionEvent) {
        //根据鼠标的位置来该表绿色方块的坐标，并将其限制在（-10,10）到（10,10）的范围内
        float x = mouseMotionEvent.getX();
        float y = mouseMotionEvent.getY();

        //设置坐标系20*20
        x = x * 20 / cam.getWidth() - 10;
        y = y * 20 / cam.getHeight() - 10;
        green.setLocalTranslation(x, 0, -y);

        //碰撞检测
        CollisionResults results = new CollisionResults();
        pink.collideWith(green.getWorldBound(), results);

        if (results.size() > 0) {
            green.getMaterial().setColor("Color", ColorRGBA.Red);
        } else {
            green.getMaterial().setColor("Color", ColorRGBA.Green);
        }
    }

    @Override
    public void onMouseButtonEvent(MouseButtonEvent mouseButtonEvent) {

    }

    @Override
    public void onKeyEvent(KeyInputEvent keyInputEvent) {

    }

    @Override
    public void onTouchEvent(TouchEvent touchEvent) {

    }
}
