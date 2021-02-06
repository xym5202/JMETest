package com.xym.jmetest.myselftest.Nodes;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import com.xym.jmetest.myselftest.Util.ConstantUtilTest;

public class HelloNodesTest extends SimpleApplication {

    private Spatial spatial;

    public static void main(String[] args) {
        HelloNodesTest helloNodesTest = new HelloNodesTest();
        helloNodesTest.setShowSettings(false);
        helloNodesTest.start();
    }

    @Override
    public void simpleInitApp() {
        flyCam.setMoveSpeed(2);
        Mesh mesh = new Sphere(10, 20, 1);

        Geometry geometry = new Geometry("红色球", mesh);
        geometry.setMaterial(newLightMaterial(ColorRGBA.Red));

        Geometry geometry1 = new Geometry("蓝色球", mesh);
        geometry1.setMaterial(newLightMaterial(ColorRGBA.Blue));

        //将两个球添加到一个Node中
        Node node = new Node("原点");
        node.attachChild(geometry);
        node.attachChild(geometry1);

        //设置两个球的相对位置
        geometry.setLocalTranslation(1, 1, 1);
        geometry1.setLocalTranslation(3, 1, 1);
        rootNode.attachChild(node);
        addLight();
        this.spatial=node;
    }

    @Override
    public  void  simpleUpdate(float tpf){
        if(spatial!=null){
            spatial.rotate(0, 0.001f* FastMath.PI,0);
        }
    }

    private Material newLightMaterial(ColorRGBA colorRGBA) {
        Material material = new Material(assetManager, ConstantUtilTest.lightingLoad);
        material.setColor(ConstantUtilTest.Diffuse, colorRGBA);
        material.setColor(ConstantUtilTest.Ambient, colorRGBA);
        material.setColor(ConstantUtilTest.Specular, ColorRGBA.White);
        material.setFloat(ConstantUtilTest.Shininess, 24);
        material.setBoolean(ConstantUtilTest.UseMaterialColors, true);
        return material;
    }

    private void addLight() {
        DirectionalLight directionalLight = new DirectionalLight();
        directionalLight.setDirection(new Vector3f(-1, -2, -3));

        AmbientLight ambientLight = new AmbientLight();

        ColorRGBA colorRGBA = new ColorRGBA();
        directionalLight.setColor(colorRGBA.mult(0.8f));
        ambientLight.setColor(colorRGBA.mult(0.2f));
        rootNode.addLight(directionalLight);
        rootNode.addLight(ambientLight);

    }

}
