package com.xym.jmetest.myselftest.Control;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.xym.jmetest.myselftest.State.LightAppState;
import com.xym.jmetest.myselftest.Util.ConstantUtilTest;

public class HelloControlTest extends SimpleApplication {
    public static void main(String[] args) {
        HelloControlTest helloControlTest=new HelloControlTest();
        helloControlTest.start();
    }

    @Override
    public void simpleInitApp() {
        cam.setLocation(new Vector3f(3.3435764f, 3.7595856f, 6.611723f));
        cam.setRotation(new Quaternion(-0.05573249f, 0.9440857f, -0.23910178f, -0.22006002f));

        Material material=new Material(assetManager, ConstantUtilTest.lightingLoad);
        Geometry geometry=new Geometry("盒子",new Box(1,1,1));
        geometry.setMaterial(material);

//        RotateControl rotateControl=new RotateControl(FastMath.PI);
//        geometry.addControl(rotateControl);
//        FloatControl floatControl=new FloatControl();
//        geometry.addControl(floatControl);

        MotionControl motionControl=new MotionControl();
        geometry.addControl(motionControl);
        motionControl.setWalkSpeed(2f);
        motionControl.setTarget(new Vector3f(10,5,0));
        rootNode.attachChild(geometry);
        stateManager.attach(new LightAppState());
    }
}
