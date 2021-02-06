package com.xym.jmetest.myselftest.Filter;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.post.Filter;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.SceneProcessor;
import com.jme3.renderer.ViewPort;

import java.util.ArrayList;
import java.util.List;

public class MultFilterPostProcessorState extends BaseAppState {
    private AssetManager assetManager;
    private ViewPort viewPort;
    private FilterPostProcessor fpp;

    private List<Filter> filters = new ArrayList<Filter>();
    private List<Filter> filtersremove = new ArrayList<Filter>();


    @Override
    protected void initialize(Application app) {
        this.assetManager = app.getAssetManager();
        this.viewPort = app.getViewPort();

        /**
         * 检查用户是否已设置过FilterPostProcessor
         */

        for (SceneProcessor processor : viewPort.getProcessors()) {
            if (processor instanceof FilterPostProcessor) {
                fpp = (FilterPostProcessor) processor;
                break;
            }
        }

        if (fpp == null) {
            fpp = new FilterPostProcessor(assetManager);
            int numSamples = app.getContext().getSettings().getSamples();
            if (numSamples > 0) {
                fpp.setNumSamples(numSamples);
            }
        }
    }

    @Override
    public void update(float ftp){
        if (filters.size()>0){
            addAllFilter();
            filters.clear();
        }
        if (filtersremove.size()>0){
            removeAllFilter();
            filtersremove.clear();
        }
    }



    /**
     * 添加滤镜
     *
     * @param filter
     */
    public void addFilter(Filter filter) {
        if (filter == null &&filters.contains(filter)) {
            return;
        }
        filters.add(filter);
    }

    /**
     * 移除滤镜
     *
     * @param filter
     */
    public void removeFilter(Filter filter) {
        if (filter == null) {
            return;
        }
        filtersremove.add(filter);
    }

    /**
     * 添加所有滤镜
     */
    public void addAllFilter() {
        for (Filter f : filters) {
            if (null == fpp.getFilter(f.getClass())){
                fpp.addFilter(f);
            }
        }
    }

    /**
     * 移除所有滤镜
     */
    public void removeAllFilter() {
        for (Filter f : filtersremove) {
            fpp.removeFilter(f);
        }
    }


    @Override
    protected void cleanup(Application app) {
        filters.clear();
        filtersremove.clear();
    }

    @Override
    protected void onEnable() {
        viewPort.addProcessor(fpp);
    }

    @Override
    protected void onDisable() {
        viewPort.removeProcessor(fpp);
    }
}
