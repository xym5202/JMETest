package com.xym.jmetest.demo;

import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.bullet.BulletAppState;
import com.xym.jmetest.demo.State.CharacterAppState;
import com.xym.jmetest.demo.State.InputAppState;
import com.xym.jmetest.demo.State.LightAppState;
import com.xym.jmetest.demo.State.SceneAppState;

/**
 * 使用第三人称控制Jaime在地图上自由行走
 */
public class JaimeTest extends SimpleApplication {

    public JaimeTest(){
        super(new StatsAppState());
    }


    @Override
    public void simpleInitApp() {
        stateManager.attachAll(new BulletAppState(),new LightAppState(),new SceneAppState(),new CharacterAppState(),new InputAppState());
    }

    public static void main(String[] args) {
        JaimeTest app=new JaimeTest();
        app.setShowSettings(false);
        app.start();
    }
}
