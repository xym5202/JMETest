package com.xym.jmetest.myselftest.Control;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.math.FastMath;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

import java.io.IOException;

public class RotateControl implements Control {

    private Spatial spatial;

    //每秒旋转180°
    private float rotateSpeed = FastMath.PI;

    public RotateControl() {
        this.rotateSpeed = FastMath.PI;
    }

    public RotateControl(float rotateSpeed) {
        this.rotateSpeed = rotateSpeed;
    }


    @Override
    public Control cloneForSpatial(Spatial spatial) {
        RotateControl control=new RotateControl(rotateSpeed);
        control.setSpatial(spatial);
        return control;
    }

    @Override
    public void setSpatial(Spatial spatial) {
        this.spatial = spatial;
    }

    @Override
    public void update(float tpf) {
        spatial.rotate(0,tpf*rotateSpeed,0);
    }

    @Override
    public void render(RenderManager rm, ViewPort vp) {

    }

    @Override
    public void write(JmeExporter ex) throws IOException {

    }

    @Override
    public void read(JmeImporter im) throws IOException {

    }
}