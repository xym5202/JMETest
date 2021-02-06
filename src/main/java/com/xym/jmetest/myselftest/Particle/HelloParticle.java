package com.xym.jmetest.myselftest.Particle;

import com.jme3.app.SimpleApplication;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

public class HelloParticle extends SimpleApplication {

    public static void main(String[] args) {
        HelloParticle helloParticle=new HelloParticle();
        helloParticle.start();
    }

    @Override
    public void simpleInitApp() {

        //粒子发射器
        ParticleEmitter fire =new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle,30);

        //粒子的生存周期
        fire.setLowLife(1);//最短1秒
        fire.setHighLife(3);//最长3秒

        /**
         * 粒子外观
         */
        //加载材质
        Material material=new Material(assetManager,"Common/MatDefs/Misc/Particle.j3md");
        material.setTexture("Texture",assetManager.loadTexture("Effects/Explosion/flame.png"));
        fire.setMaterial(material);

        //设置2*2颜色
        fire.setImagesX(2);
        fire.setImagesY(2);

        //初始颜色,结束颜色
        fire.setStartColor(new ColorRGBA(1,1,0,0.5f));//黄色
        fire.setEndColor(new ColorRGBA(1,0,0,1));//红色

        //初始大小，结束大小
        fire.setStartSize(1.5f);
        fire.setEndSize(0.1f);

        /**
         * 粒子行为
         */
        //初速度
        fire.getParticleInfluencer().setInitialVelocity(new Vector3f(0,2,0));
        //速度变化
        fire.getParticleInfluencer().setVelocityVariation(0.3f);
        //不受重力影响
        fire.setGravity(0,0,0);

        rootNode.attachChild(fire);
    }

}
