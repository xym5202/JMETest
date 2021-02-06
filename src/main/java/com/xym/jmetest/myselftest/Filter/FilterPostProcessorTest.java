package com.xym.jmetest.myselftest.Filter;

import com.jme3.app.SimpleApplication;
import com.jme3.math.ColorRGBA;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FogFilter;

public class FilterPostProcessorTest extends SimpleApplication {


    @Override
    public void simpleInitApp() {
        //初始化滤镜处理器
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        viewPort.addProcessor(fpp);

        //添加雾化滤镜
        FogFilter fogFilter = new FogFilter(ColorRGBA.White, 1.5f, 100f);
        fpp.addFilter(fogFilter);

        //开启滤镜后，抗锯齿会失效，需要设置抗锯齿倍率
        fpp.setNumSamples(4);//开启4倍抗锯齿

        //用户启动设置抗锯齿倍率
        int numSamples = getContext().getSettings().getSamples();
        if (numSamples > 0) {
            fpp.setNumSamples(numSamples);
        }
        /**
         * jME3和 Swing、JavaFX等框架集成，此时通过 setNumSamples()
         * 设置的多重采样抗拒齿会失效，那么你就得用快速近似抗拒齿过滤器（com.jme3.post.filter.FXAAFilter）了
         */


    }
}
