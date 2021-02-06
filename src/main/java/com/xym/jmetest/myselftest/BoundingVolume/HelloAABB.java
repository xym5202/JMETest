package com.xym.jmetest.myselftest.BoundingVolume;

import com.jme3.app.SimpleApplication;
import com.jme3.bounding.BoundingBox;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.debug.WireBox;
import com.jme3.scene.shape.Cylinder;
import com.xym.jmetest.myselftest.Control.FloatControl;
import com.xym.jmetest.myselftest.Control.RotateControl;
import com.xym.jmetest.myselftest.Util.ConstantUtilTest;
import com.xym.jmetest.myselftest.Util.CoordinateAxisState;

public class HelloAABB extends SimpleApplication {

    private Geometry debug;
    private Geometry cylinder;

    @Override
    public void simpleInitApp() {

        // 初始化摄像机
        cam.setLocation(new Vector3f(4.5114727f, 6.176994f, 13.277485f));
        cam.setRotation(new Quaternion(-0.038325474f, 0.96150225f, -0.20146479f, -0.18291113f));
        flyCam.setMoveSpeed(10);

        viewPort.setBackgroundColor(ColorRGBA.LightGray);

        //参考坐标系
        stateManager.attach(new CoordinateAxisState());

        //圆柱体
        Material material=new Material(assetManager, ConstantUtilTest.lightingLoad);
        material.setColor(ConstantUtilTest.Diffuse,ColorRGBA.Yellow);
        material.setColor(ConstantUtilTest.Ambient,ColorRGBA.Yellow);
        material.setColor(ConstantUtilTest.Specular,ColorRGBA.White);
        material.setFloat(ConstantUtilTest.Shininess,24);
        material.setBoolean(ConstantUtilTest.UseMaterialColors,true);

        cylinder = new Geometry("cylinder",new Cylinder(2,36,1,8,true));
        cylinder.setMaterial(material);

        //让圆柱体运动
        cylinder.addControl(new RotateControl());
        cylinder.addControl(new FloatControl(2,2));

        //用于显示包围盒
        Material material1=new Material(assetManager,ConstantUtilTest.unShadedLoad);
        material1.setColor("Color",ColorRGBA.Magenta);
        material1.getAdditionalRenderState().setLineWidth(2);
        debug =new Geometry("debug",new WireBox(1,1,1));
        debug.setMaterial(material1);

        rootNode.attachChild(cylinder);
        rootNode.attachChild(debug);

        // 光源
        rootNode.addLight(new DirectionalLight(new Vector3f(-1, -2, -3)));
        rootNode.addLight(new AmbientLight(new ColorRGBA(0.2f, 0.2f, 0.2f, 1f)));
    }

    @Override
    public void simpleUpdate(float tpf){
        //根据圆柱体当前的包围盒，更新线框的位置和大小
        BoundingBox boundingBox=(BoundingBox)cylinder.getWorldBound();
        debug.setLocalScale(boundingBox.getExtent(null));
        debug.setLocalTranslation(boundingBox.getCenter());
    }

    public static void main(String[] args) {
        HelloAABB app=new HelloAABB();
        app.start();
    }
}
