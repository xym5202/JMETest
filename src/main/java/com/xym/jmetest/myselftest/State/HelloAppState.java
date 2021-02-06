package com.xym.jmetest.myselftest.State;

import com.jme3.app.SimpleApplication;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.xym.jmetest.myselftest.Util.CoordinateAxisState;

public class HelloAppState extends SimpleApplication {

    public static void main(String[] args) {
        HelloAppState appState = new HelloAppState();
        appState.setShowSettings(false);
        appState.start();
    }

    @Override
    public void simpleInitApp() {
        stateManager.attach(new LightAppState());
        stateManager.attach(new VisualAppState());
        stateManager.attach(new InputAppState());
        stateManager.attach(new CoordinateAxisState());


        cam.setLocation(new Vector3f(2.4611378f, 2.8119917f, 9.150583f));
        cam.setRotation(new Quaternion(-0.020502187f, 0.97873497f, -0.16252096f, -0.1234684f));
    }
}


