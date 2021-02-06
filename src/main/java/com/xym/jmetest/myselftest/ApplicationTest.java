package com.xym.jmetest.myselftest;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.light.SpotLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import com.jme3.scene.shape.Sphere;
import com.jme3.shadow.EdgeFilteringMode;
import com.jme3.shadow.PointLightShadowRenderer;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;
import com.xym.jmetest.myselftest.Util.ConstantUtilTest;

public class ApplicationTest extends SimpleApplication {

    private Geometry geometry;

    /**
     * 初始化场景方法
     */
    @Override
    public void simpleInitApp() {
//       //设置camera位置
//        cam.setLocation(new Vector3f(3.0180411f, 3.526049f, -8.088949f));
//        cam.setRotation(new Quaternion(0.103158996f, -0.05727794f, 0.0059502474f, 0.9929965f));
        viewPort.setBackgroundColor(ColorRGBA.Blue);
        flyCam.setMoveSpeed(2);
        //添加发光滤镜
//        FilterPostProcessor filterPostProcessor=new FilterPostProcessor(assetManager);
//        BloomFilter b=new BloomFilter(BloomFilter.GlowMode.Objects);
//        filterPostProcessor.addFilter(b);
//        viewPort.addProcessor(filterPostProcessor);


        /**
         * 发光色 GlowColor
         * 设置发光色的物体不能设置透明
         */
        createDiffuseMapTexture();
//        createTranslucentObject(new Box(1, 1, 1), "Color");
//        addUnshadedMesh("Color",new Sphere(20,40,1),ColorRGBA.Red,"红色无光小球",4,4,0);
        addUnshadedMesh("ColorMap",new Quad(5,5),null,"普石材质",-3,-3,-3);
        addLightingMesh(new Sphere(20, 40, 1), ColorRGBA.Red, "反光小球", 0, 0, 0, 32);
//        addLightingMesh(new Box(1,1,1),null,"贴图小块",0,0,0,32.0f);
        addAmbientLight();
        addPointLight();
        //addSpotLight();
    }

    //发光材质
    private void addLightingMesh(Mesh mesh, ColorRGBA colorRGBA, String meshName, Integer x, Integer y, Integer z, float shiniess) {
        Material material = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        if (mesh instanceof Sphere) {
            material.setColor("Diffuse", colorRGBA);
            material.setColor("Ambient", colorRGBA);
            material.setColor("Specular", ColorRGBA.White);
            material.setBoolean("UseMaterialColors", true);
        } else {
            //漫反射贴图
            Texture texture;
            texture = assetManager.loadTexture("Textures/Terrain/BrickWall/BrickWall.jpg");
            material.setTexture("DiffuseMap", texture);
            //法线反射
            texture = assetManager.loadTexture("Textures/Terrain/BrickWall/BrickWall_normal.jpg");
            material.setTexture("NormalMap", texture);
            //视差贴图
//            texture=assetManager.loadTexture("Textures/Terrain/BrickWall/BrickWall_height.jpg");
//            material.setTexture("ParallaxMap",texture);
        }
        material.setFloat("Shininess", shiniess);
        Geometry geometry = new Geometry(meshName, mesh);
        geometry.setMaterial(material);
        geometry.move(x, y, z);
        rootNode.attachChild(geometry);
    }

    //无光材质
    private void addUnshadedMesh(String colorName, Mesh mesh, ColorRGBA colorRGBA, String meshName, Integer x, Integer y, Integer z) {

        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        if (mesh instanceof Sphere) {
            material.setColor(colorName, colorRGBA);
        } else if (mesh instanceof Box) {
            Texture tex = assetManager.loadTexture("Textures/Terrain/BrickWall/BrickWall.jpg");
            material.setTexture(colorName, tex);
        }
        Geometry geometry = new Geometry(meshName, mesh);
        geometry.setMaterial(material);
        geometry.move(x, y, z);
        rootNode.attachChild(geometry);
    }

    //产生定向光
    private void addDirectionalLight() {
        DirectionalLight directionalLight = new DirectionalLight();
        directionalLight.setDirection(new Vector3f(-1, -2, -3));
        ColorRGBA GAMA = new ColorRGBA();
        directionalLight.setColor(GAMA.mult(0.8f));
        rootNode.addLight(directionalLight);
    }

    //产生环境光
    public  void addAmbientLight() {
        AmbientLight ambientLight = new AmbientLight();
        ColorRGBA GAMA = new ColorRGBA();
        ambientLight.setColor(GAMA.mult(0.2f));
        rootNode.addLight(ambientLight);
    }

    //产生点光源
    private void addPointLight() {
        PointLight pointLight=new PointLight();
        pointLight.setColor(ColorRGBA.Yellow);
        pointLight.setRadius(4f);
        pointLight.setPosition(new Vector3f(0,0,3));
        rootNode.addLight(pointLight);
        //点光源阴影
        PointLightShadowRenderer pointLightShadowRenderer=new PointLightShadowRenderer(assetManager,1024);
        pointLightShadowRenderer.setLight(pointLight);
        pointLightShadowRenderer.setEdgeFilteringMode(EdgeFilteringMode.PCF8);
        viewPort.addProcessor(pointLightShadowRenderer);
    }

    //生成聚光灯源
    private void addSpotLight(){
        SpotLight spotLight=new SpotLight();
        //射程
        spotLight.setSpotRange(100f);
        //光锥内角
        spotLight.setSpotInnerAngle(5f* FastMath.DEG_TO_RAD);
        //光锥外角
        spotLight.setSpotInnerAngle(15f* FastMath.DEG_TO_RAD);
        //光源颜色
        spotLight.setColor(ColorRGBA.White.mult(1.3f));
        //光源位置
        spotLight.setPosition(new Vector3f(9.5f,13.5f,9f));
        //光源方向
        spotLight.setDirection(new Vector3f(-0.06764714f, -0.647349f, -0.7591859f));
        rootNode.addLight(spotLight);
    }

    //半透明不受光材质
    private void createTranslucentObject(Mesh mesh, String ColorName) {
        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setColor(ColorName, new ColorRGBA(1, 1, 1, 0.5f));

        Geometry geometry = new Geometry("半透明不受光物体", mesh);
        material.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        geometry.setMaterial(material);
        geometry.setQueueBucket(RenderQueue.Bucket.Transparent);
        geometry.move(-1, -1, 5);
        rootNode.attachChild(geometry);
    }

    //半透明受光材质
    private void createTranslucentObjectLight(Mesh mesh) {
        Material material = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        material.setColor("Diffuse", new ColorRGBA(1, 1, 1, 0.5f));
        material.setColor("Ambient", ColorRGBA.Cyan);
        material.setColor("Specular", ColorRGBA.White);
        material.setFloat("Shininess", 16f);
        material.setBoolean("UseMaterialColors", true);
        //设置网格的线框模式
//        material.getAdditionalRenderState().setWireframe(true);
        Geometry geometry = new Geometry("半透明受光物体", mesh);
        material.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        geometry.setMaterial(material);
        geometry.setQueueBucket(RenderQueue.Bucket.Transparent);
        geometry.move(-1, -1, 5);
        rootNode.attachChild(geometry);
    }

    //设置纹理贴图半透明Moneky.png
    private void createDiffuseMapTexture() {
        Material material = new Material(assetManager, ConstantUtilTest.unShadedLoad);
        material.setTexture("ColorMap", assetManager.loadTexture("Textures/ColoredTex/Monkey.png"));

        Geometry geometry = new Geometry("monkey", new Quad(4, 4));
        geometry.setMaterial(material);
        geometry.move(0, 0, 4);
        rootNode.attachChild(geometry);

        //以下两步很重要！！！
        //将材质的混色模式设置为BlendMode.Alpha
        material.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        //将Geometry的渲染序列设置为transparent，这会使它在其他不透明物体绘制后再绘制
        geometry.setQueueBucket(RenderQueue.Bucket.Transparent);

    }

    public static void main(String[] args) {
        AppSettings settings = new AppSettings(true);
        //设置屏幕是否可以拖动大小
        settings.setResizable(true);
        //设置分辨率
        settings.setResolution(800, 600);
        // 启动jME3程序
        ApplicationTest app = new ApplicationTest();
        app.setShowSettings(false);
        app.start();
    }
}
