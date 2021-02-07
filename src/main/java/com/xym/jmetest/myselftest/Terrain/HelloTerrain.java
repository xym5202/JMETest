package com.xym.jmetest.myselftest.Terrain;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.scene.Spatial;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.geomipmap.lodcalc.DistanceLodCalculator;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import com.jme3.water.WaterFilter;

import java.util.Date;

/**
 * 通过高度图加载地形
 */
public class HelloTerrain extends SimpleApplication {

    public static void main(String[] args) {
        HelloTerrain app=new HelloTerrain();
        app.setShowSettings(false);
        app.start();
    }

    @Override
    public void simpleInitApp() {

        cam.setLocation(new Vector3f(-100, 80, 50));

        flyCam.setMoveSpeed(20f);

        initLight();
        initSky();
        initWater();
        initTerrain();
    }

    /**
     * 初始化地形
     */

    private void initTerrain() {
        //加载地形的高度图
        Texture heightMapImage = assetManager.loadTexture("Scenes/Maps/DefaultMap/default.png");
        //根据图像内容，生成高度图
        ImageBasedHeightMap heightMap = new ImageBasedHeightMap(heightMapImage.getImage(), 1);
        heightMap.load();

        //高斯平滑
        GaussianBlur gaussianBlur=new GaussianBlur();
        float[] heightData=heightMap.getHeightMap();
        int width=heightMapImage.getImage().getWidth();
        int height=heightMapImage.getImage().getHeight();

        heightData =gaussianBlur.filter(heightData,width,height);

        /**
         * 根据高度图生成实际的地形，该地形被分解成边长65（64*64）的矩形区块，用于优化网格。高度图的边长为257，分辨率256*256
         */
        TerrainQuad terrainQuad=new TerrainQuad("terrain",65,257,heightMap.getHeightMap());

        //层次细节
        TerrainLodControl control=new TerrainLodControl(terrainQuad,getCamera());
        control.setLodCalculator(new DistanceLodCalculator(65,2.4f));
        terrainQuad.addControl(control);

        //地形材质
        terrainQuad.setMaterial(assetManager.loadMaterial("Scenes/Maps/DefaultMap/default.j3m"));

        terrainQuad.setLocalTranslation(0,-100,0);
        rootNode.attachChild(terrainQuad);

    }

    /**
     * 初始化水面
     */
    private void initWater() {
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        viewPort.addProcessor(fpp);

        //水面
        WaterFilter waterFilter = new WaterFilter();
        waterFilter.setWaterHeight(50f);//水面高度
        waterFilter.setWaterTransparency(0.2f);//透明度
        waterFilter.setWaterColor(new ColorRGBA(0.4314f, 0.9373f, 0.8431f, 1f));//水面颜色

        fpp.addFilter(waterFilter);
    }

    /**
     * 初始化天空
     */
    private void initSky() {
        Spatial sky = SkyFactory.createSky(assetManager, "Textures/Sky/SkySphereMap.jpg", SkyFactory.EnvMapType.SphereMap);
        rootNode.attachChild(sky);
    }

    /**
     * 初始化灯光
     */
    private void initLight() {
        AmbientLight ambientLight = new AmbientLight();
        ambientLight.setColor(new ColorRGBA(0.298f, 0.2392f, 0.2745f, 1f));
        rootNode.addLight(ambientLight);

        DirectionalLight directionalLight = new DirectionalLight();
        directionalLight.setDirection((new Vector3f(0.097551f, -0.733139f, -0.673046f)).normalizeLocal());
        directionalLight.setColor(new ColorRGBA(1, 1, 1, 1));
        rootNode.addLight(directionalLight);
    }


}
