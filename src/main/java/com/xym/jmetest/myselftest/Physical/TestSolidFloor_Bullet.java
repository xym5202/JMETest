package com.xym.jmetest.myselftest.Physical;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.MeshCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;
import com.jme3.scene.shape.Sphere;
import com.xym.jmetest.myselftest.Util.ConstantUtilTest;
import com.xym.jmetest.myselftest.Util.CoordinateAxisState;

/**
 * 使用Bullet物理引擎进行碰撞检测
 */
public class TestSolidFloor_Bullet extends SimpleApplication {
    @Override
    public void simpleInitApp() {
        stateManager.attach(new CoordinateAxisState());

        cam.setLocation(new Vector3f(0f, 16f, 20f));
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);

        viewPort.setBackgroundColor(ColorRGBA.White);

        /**
         * 场景
         */
        //方块
        Material material=new Material(assetManager, ConstantUtilTest.unShadedLoad);
        material.setColor("Color",ColorRGBA.Green);
        Geometry sphere =new Geometry("Green",new Sphere(8,8,1));
        sphere.setMaterial(material);
        sphere.move(-10,10,0);

        //地板
        material=new Material(assetManager,ConstantUtilTest.unShadedLoad);
        material.setColor("Color",ColorRGBA.Pink);

        Geometry floor =new Geometry("Floor",new Quad(12,4));
        floor.rotate(-FastMath.HALF_PI,0,0);
        floor.move(-10,0,2);
        floor.setMaterial(material);
        rootNode.attachChild(sphere);
        rootNode.attachChild(floor);


        /**
         * Bullet物理引擎
         */
        BulletAppState bulletAppState=new BulletAppState();
        stateManager.attach(bulletAppState);

        PhysicsSpace space=bulletAppState.getPhysicsSpace();

        //地板
        //刚体空间（形状，质量）。质量为0将不受任何里的影响，适合做地面
        RigidBodyControl rigidBodyControl=new RigidBodyControl(new MeshCollisionShape(new Quad(12,4)),0);
        rigidBodyControl.setRestitution(1);
        floor.addControl(rigidBodyControl);
        space.add(rigidBodyControl);

        //球体
        rigidBodyControl =new RigidBodyControl(new SphereCollisionShape(1),1f);
        rigidBodyControl.setRestitution(0.6f);
        sphere.addControl(rigidBodyControl);

        space.add(sphere);


        rigidBodyControl.setLinearVelocity(new Vector3f(1,10,0));//线速度
        rigidBodyControl.setGravity(new Vector3f(0,-9.8f,0));//重力加速度
    }

    public static void main(String[] args) {
        TestSolidFloor_Bullet testSolidFloor_bullet=new TestSolidFloor_Bullet();
        testSolidFloor_bullet.setShowSettings(false);
        testSolidFloor_bullet.start();
    }
}
