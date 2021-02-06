package com.xym.jmetest.myselftest.Filter;

import com.jme3.app.DebugKeysAppState;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.app.state.ScreenshotAppState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.Filter;
import com.jme3.post.filters.*;
import com.jme3.post.ssao.SSAOFilter;
import com.jme3.water.WaterFilter;

import java.util.ArrayList;
import java.util.List;

import com.simsilica.lemur.*;
import com.simsilica.lemur.style.BaseStyles;
import com.simsilica.lemur.style.ElementId;

public class HelloFilters extends SimpleApplication {

    /**
     * 全部滤镜
     */
    private List<Filter> filterList = new ArrayList<Filter>();

    // 全部滤镜
    private BloomFilter bloom;
    private CartoonEdgeFilter cartoonEdge;
    private ColorOverlayFilter colorOverlay;
    private CrossHatchFilter crossHatch;
    private DepthOfFieldFilter depthOfField;
    private FogFilter fog;
    private LightScatteringFilter lightScattering;
    private SSAOFilter ssao;
    private WaterFilter water;


    public static void main(String[] args) {
        HelloFilters app=new HelloFilters();
        app.start();
    }

    /**
     * 自定义滤镜
     */
//    private  GrayScaleFilter grayScaleFilter;
    public HelloFilters() {
        super(new FlyCamAppState(), new StatsAppState(), new DebugKeysAppState(), new CubeAppState(),
                new MultFilterPostProcessorState(), new ScreenshotAppState("screenshots/", System.currentTimeMillis() + "_"));
    }

    @Override
    public void simpleInitApp() {
        flyCam.setMoveSpeed(10f);
        //设置拖动旋转
        flyCam.setDragToRotate(true);

        GuiGlobals.initialize(this);
        //// 加载 'glass' 样式
        BaseStyles.loadGlassStyle();
        //下面这行代码会把"glass"样式设置为所有GUI元素的默认样式。
        GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");

        initFilters();
        initGui();

    }

    /**
     * 初始化所有滤镜
     */
    private void initFilters() {
        //发光特效
        bloom = new BloomFilter(BloomFilter.GlowMode.SceneAndObjects);

        //卡通边缘
        cartoonEdge = new CartoonEdgeFilter();
        cartoonEdge.setEdgeColor(ColorRGBA.Black);

        //纯色叠加
        colorOverlay = new ColorOverlayFilter(new ColorRGBA(1, 0.8f, 0.8f, 1));

        //阴影交叉
        crossHatch = new CrossHatchFilter();

        //景深
        depthOfField = new DepthOfFieldFilter();
        depthOfField.setFocusDistance(0);
        depthOfField.setFocusRange(20);
        depthOfField.setBlurScale(1.4f);

        //雾化
        fog = new FogFilter(ColorRGBA.White, 1.5f, 200);

        //光线散射
        Vector3f sunDir = stateManager.getState(CubeAppState.class).getSunDirection();
        lightScattering = new LightScatteringFilter(sunDir.mult(-3000));

        //屏幕空间环境光遮蔽
        ssao = new SSAOFilter(7, 13, 0.4f, 0.6f);

        //水
        water = new WaterFilter();
        //设置睡眠范围，若不设置默认无限范围
        water.setCenter(new Vector3f(100, 0, -100));
        //设置水面半径
        water.setRadius(100);
        //设置水面形状
        water.setShapeType(WaterFilter.AreaShape.Square);
        //设置水下颜色
        water.setDeepWaterColor(new ColorRGBA(0.8f, 1, 0.8f, 1));
        //设置光照方向
        water.setLightDirection(sunDir);
        //设置水面高度
        water.setWaterHeight(6f);
        //设置水下视距
        water.setUnderWaterFogDistance(80);

        filterList.add(cartoonEdge);
        filterList.add(bloom);
        filterList.add(colorOverlay);
        filterList.add(crossHatch);
        filterList.add(depthOfField);
        filterList.add(fog);
        filterList.add(lightScattering);
        filterList.add(ssao);
        filterList.add(water);
    }

    /**
     * 初始化GUI
     */
    private void initGui() {
        Container window = new Container();
        guiNode.attachChild(window);
        window.addChild(new Label("Filters", new ElementId("title")));
        for (Filter f : filterList) {
            window.addChild(createCheckBox(f));
        }
        window.setLocalTranslation(10,cam.getHeight()-10,0);
    }

    /**
     * 实例化checkBox,作为滤镜开关
     * @param f
     * @return
     */

    private Checkbox createCheckBox(final Filter f) {
        String name =f.getClass().getSimpleName();
        final  Checkbox cb=new Checkbox(name);

        cb.addClickCommands(new Command<Button>() {
            @Override
            public void execute(Button button) {
                MultFilterPostProcessorState state=stateManager.getState(MultFilterPostProcessorState.class);
                if (cb.isChecked()){
                    state.addFilter(f);
                }else {
                    state.removeFilter(f);
                }
            }
        });

        return  cb;
    }


}
