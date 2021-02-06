package com.xym.jmetest.myselftest.Physical;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.*;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import com.jme3.scene.shape.Sphere;
import com.xym.jmetest.myselftest.State.LightAppState;
import com.xym.jmetest.myselftest.Util.ConstantUtilTest;
import com.xym.jmetest.myselftest.Util.CoordinateAxisState;

import java.util.ArrayList;
import java.util.List;

public class TestBulley2 extends SimpleApplication {

    private List<Geometry> boxs = new ArrayList<>(10);

    public static void main(String[] args) {
        TestBulley2 app = new TestBulley2();
        app.setShowSettings(false);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        cam.setLocation(new Vector3f(-14.574227f, 6.8078837f, 3.2619495f));
        cam.setRotation(new Quaternion(0.13746259f, 0.86010045f, -0.3107305f, 0.38049686f));
        flyCam.setMoveSpeed(10f);
        stateManager.attach(new LightAppState());
        stateManager.attach(new CoordinateAxisState());
        //制作小球
        Material material = new Material(assetManager, ConstantUtilTest.lightingLoad);
        material.setColor(ConstantUtilTest.Diffuse, ColorRGBA.Red);
        material.setColor(ConstantUtilTest.Ambient, ColorRGBA.Red);
        material.setColor(ConstantUtilTest.Specular, ColorRGBA.White);
        material.setFloat(ConstantUtilTest.Shininess, 10);
        material.setBoolean(ConstantUtilTest.UseMaterialColors, true);
        Mesh mesh = new Sphere(40, 80, 0.2f);

        Geometry ballg = new Geometry("ball", mesh);
        ballg.setMaterial(material);

        //制作地板
        material = assetManager.loadMaterial("Textures/Terrain/Pond/Pond.j3m");
        Quad floor = new Quad(40, 40);
        //伸展纹理坐标
        floor.scaleTextureCoordinates(new Vector2f(10, 10));

        Geometry floor1 = new Geometry("Floor", floor);
        floor1.setMaterial(material);
        floor1.move(-5, -2, 4);
        floor1.rotate(-FastMath.HALF_PI, 0, 0);


        //制作木板
        material = new Material(assetManager, ConstantUtilTest.unShadedLoad);
        material.setColor("Color", ColorRGBA.White);
        Box box = new Box(1.8f, 1.05f, 0.03f);
        Geometry boxg = new Geometry("box", box);
        boxg.setMaterial(material);
        for (int i = 0; i < 10; i++) {
           Geometry geometry= boxg.clone();
           boxs.add(geometry);
        }

        // 初始化物理引擎，设置物理空间的边界。
        BulletAppState bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);

        PhysicsSpace physicsSpace = bulletAppState.getPhysicsSpace();
        //创建球形刚体
        RigidBodyControl ballControl = new RigidBodyControl(1);
        ballControl.setCollisionShape(new SphereCollisionShape(0.2f));
        ballControl.setPhysicsLocation(new Vector3f(0, 0, 0));
       // ballControl.setLinearVelocity(new Vector3f(0, 3, -10));
        ballControl.setFriction(0.2f);
        ballControl.setRestitution(0.8f);
        ballControl.setGravity(new Vector3f(0, -9.8f, 0));
        ballControl.applyImpulse((new Vector3f(0, 3, -10)),new Vector3f(0,3,-9));
        ballg.addControl(ballControl);




        physicsSpace.add(ballControl);

        //创建地板
        RigidBodyControl floorControl = new RigidBodyControl(0);
        Vector3f half = new Vector3f(40, 40, 0);
        floorControl.setCollisionShape(new BoxCollisionShape(half));
        floorControl.setRestitution(0.8f);

        floor1.addControl(floorControl);
        physicsSpace.add(floorControl);


        //创建挡板
        RigidBodyControl rigidBodyControl = new RigidBodyControl(0.2f);
        Vector3f extents = new Vector3f(1.8f, 1.08f, 0.03f);
        rigidBodyControl.setCollisionShape(new BoxCollisionShape(extents));


        for (int i=0;i<boxs.size();i++) {
            Geometry b= boxs.get(i);
            RigidBodyControl bodyControl = (RigidBodyControl) rigidBodyControl.jmeClone();
            bodyControl.setPhysicsLocation(new Vector3f(1.8f, 1.05f, 0.03f));
            b.move(0,-0.95f,-20+2*i);
            b.addControl(bodyControl);
            //刚体加到Bullet物理空间
            physicsSpace.add(bodyControl);
            rootNode.attachChild(b);
        }

        rootNode.attachChild(ballg);
        rootNode.attachChild(floor1);
    }
}
