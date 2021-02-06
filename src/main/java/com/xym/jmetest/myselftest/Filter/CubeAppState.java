package com.xym.jmetest.myselftest.Filter;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import com.jme3.util.SkyFactory;
import com.xym.jmetest.myselftest.State.LightAppState;
import com.xym.jmetest.myselftest.Util.ConstantUtilTest;

/**
 * 测试应用场景
 */
public class CubeAppState extends BaseAppState {

    private Node rootNode =new Node("Scene root");

    private AmbientLight ambientLight;
    private PointLight pointLight;
    private DirectionalLight sun;
    private Vector3f sunDirection=new Vector3f(-0.65093255f, -0.11788898f, 0.7499261f);
    private AssetManager assetManager;

    public Vector3f getSunDirection() {
        return sunDirection;
    }

    public Node getRootNode() {
        return rootNode;
    }

    public void setRootNode(Node rootNode) {
        this.rootNode = rootNode;
    }

    @Override
    protected void initialize(Application app) {
    this.assetManager =app.getAssetManager();
    //创造地板
        Material material=assetManager.loadMaterial("Textures/Terrain/Pond/Pond.j3m");
        Quad quad=new Quad(200,200);
        //伸展纹理坐标
        quad.scaleTextureCoordinates(new Vector2f(20,20));

        Geometry geometry=new Geometry("Floor",quad);
        geometry.setMaterial(material);
        geometry.rotate(-FastMath.HALF_PI,0,0);

        rootNode.attachChild(geometry);

        float scalar =20;
        float side =3f;
        for (int y=0;y<9;y++){
            for (int x=0;x<9;x++){
                geometry=new Geometry("Cube",new Box(side,side*2,side));
                geometry.setMaterial(getMaterial(new ColorRGBA(1-x/8f,y/8f,1f,1f)));
                geometry.move((x+1)*scalar,side*2,-(y+1)*scalar);
                rootNode.attachChild(geometry);
            }
        }

        //天空
        Spatial spatial= SkyFactory.createSky(assetManager,"Scenes/Beach/FullskiesSunset0068.dds", SkyFactory.EnvMapType.CubeMap);
        spatial.setLocalScale(350);
        rootNode.attachChild(spatial);

        //创造光源

//        sun =new DirectionalLight();
//        sun.setDirection(sunDirection);
//        sun.setColor(new ColorRGBA(0.6f,0.6f,0.6f,1));
//        ambientLight =new AmbientLight();
//        ambientLight.setColor(new ColorRGBA(0.4f, 0.4f, 0.4f, 1f));
//        pointLight =new PointLight();
//        pointLight.setPosition(new Vector3f(100, 200, 100));
//        pointLight.setRadius(1000);
    }

    private Material getMaterial(ColorRGBA colorRGBA) {
        Material material=new Material(assetManager, ConstantUtilTest.lightingLoad);
        material.setColor(ConstantUtilTest.Diffuse,colorRGBA);
        material.setColor(ConstantUtilTest.Ambient,colorRGBA);
        material.setColor(ConstantUtilTest.Specular, ColorRGBA.White);
        material.setFloat(ConstantUtilTest.Shininess,20f);
        material.setBoolean(ConstantUtilTest.UseMaterialColors,true);
        return  material;
    }

    @Override
    protected void cleanup(Application app) {

    }

    @Override
    protected void onEnable() {
        SimpleApplication simpleApplication=(SimpleApplication) getApplication();
        simpleApplication.getRootNode().attachChild(rootNode);
//        simpleApplication.getRootNode().addLight(ambientLight);
//        simpleApplication.getRootNode().addLight(pointLight);
//        simpleApplication.getRootNode().addLight(sun);
    }

    @Override
    protected void onDisable() {
        SimpleApplication app = (SimpleApplication) getApplication();
        app.getRootNode().detachChild(rootNode);
//        app.getRootNode().removeLight(ambientLight);
//        app.getRootNode().removeLight(pointLight);
//        app.getRootNode().removeLight(sun);
    }
}
