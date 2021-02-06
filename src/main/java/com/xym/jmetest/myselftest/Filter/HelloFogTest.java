package com.xym.jmetest.myselftest.Filter;

import com.jme3.app.SimpleApplication;
import com.jme3.math.ColorRGBA;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.post.filters.FogFilter;
import com.xym.jmetest.myselftest.State.LightAppState;

public class HelloFogTest extends SimpleApplication {

    @Override
    public void simpleInitApp() {

        stateManager.attach(new CubeAppState());
        stateManager.attach(new LightAppState());
//        FilterPostProcessor filterPostProcessor=new FilterPostProcessor(assetManager);
//        filterPostProcessor.setNumSamples(4);
//        viewPort.addProcessor(filterPostProcessor);
//
//        FogFilter fogFilter=new FogFilter(ColorRGBA.White,1.5f,100);
//        filterPostProcessor.addFilter(fogFilter);
//        BloomFilter bloomFilter=new BloomFilter();
//        filterPostProcessor.addFilter(bloomFilter);
        flyCam.setMoveSpeed(10f);
    }

    public static void main(String[] args) {
        HelloFogTest app=new HelloFogTest();
        app.start();
    }
}
