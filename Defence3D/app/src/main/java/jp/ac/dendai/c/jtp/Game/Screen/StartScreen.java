package jp.ac.dendai.c.jtp.Game.Screen;

import android.graphics.Bitmap;

import jp.ac.dendai.c.jtp.Bezier;
import jp.ac.dendai.c.jtp.Game.Constant;
import jp.ac.dendai.c.jtp.Game.GameObject;
import jp.ac.dendai.c.jtp.Graphics.Camera.Camera;
import jp.ac.dendai.c.jtp.Graphics.Model.Mesh;
import jp.ac.dendai.c.jtp.Graphics.Model.Primitive.Plane;
import jp.ac.dendai.c.jtp.Graphics.Renderer.Renderer;
import jp.ac.dendai.c.jtp.Graphics.Shader.DiffuseShader;
import jp.ac.dendai.c.jtp.Math.Vector3;
import jp.ac.dendai.c.jtp.ModelConverter.Wavefront.WavefrontObjConverter;
import jp.ac.dendai.c.jtp.Time.Time;
import jp.ac.dendai.c.jtp.TouchUtil.Input;
import jp.ac.dendai.c.jtp.defence3d.R;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;

/**
 * Created by Goto on 2016/09/29.
 */

public class StartScreen extends Screenable {
    protected enum STATE{
        OPENING,
        MOVE,
        MODE_SELECT
    }
    protected STATE state = STATE.OPENING;
    protected DiffuseShader diffuseShader;
    protected Renderer renderer;
    protected Mesh text_3d;
    protected Plane title_mesh,push,mode_mesh;
    protected Camera camera;
    protected Bitmap title_image,mode_select_string;
    protected GameObject title_plane1,title_plane2,title_plane3,text_3d_object;
    protected GameObject mode_select_plane;
    protected Vector3 bezie_start,bezie_end,bezie1,bezie2,target_pos;
    protected float timeBuffer,moveTime;
    protected float length,offset = 1f;
    public StartScreen(){
        camera = new Camera(Camera.CAMERA_MODE.PERSPECTIVE,0,0,-5f,0,0,0);
        diffuseShader = (DiffuseShader)Constant.getShader(Constant.SHADER.diffuse);
        diffuseShader.setCamera(camera);
        diffuseShader.setFogColor(0,0,0);
        diffuseShader.setFogDist(10f,15f);
        renderer = new Renderer();
        renderer.setShader(diffuseShader);

        text_3d = WavefrontObjConverter.createModel("textobject.obj");

        title_image = GLES20Util.loadBitmap(R.mipmap.title);
        float aspect = (float)title_image.getWidth() / (float)title_image.getHeight();

        mode_select_string = GLES20Util.stringToBitmap("MODE SELECT",50,255,255,255);

        title_mesh = new Plane();
        title_mesh.setImage(title_image);

        mode_mesh = new Plane();
        mode_mesh.setImage(mode_select_string);

        title_plane1 = new GameObject();
        title_plane1.getRenderMediator().mesh = title_mesh;
        title_plane1.getRenderMediator().isDraw = true;
        title_plane1.getScl().setZ(0.5f);
        title_plane1.getScl().setX(aspect * 0.5f);
        title_plane1.getRot().setX(-90f);
        title_plane1.getRot().setZ(180f);
        title_plane1.getPos().setY(1f);

        title_plane2 = new GameObject();
        title_plane2.getRenderMediator().mesh = title_mesh;
        title_plane2.getRenderMediator().isDraw = true;
        title_plane2.getScl().setZ(0.5f);
        title_plane2.getScl().setX(aspect * 0.5f);
        title_plane2.getRot().setX(-90f);
        title_plane2.getRot().setZ(180f);
        title_plane2.getPos().setY(1f);
        title_plane2.getPos().setZ(0.2f);

        title_plane3 = new GameObject();
        title_plane3.getRenderMediator().mesh = title_mesh;
        title_plane3.getRenderMediator().isDraw = true;
        title_plane3.getScl().setZ(0.5f);
        title_plane3.getScl().setX(aspect * 0.5f);
        title_plane3.getRot().setX(-90f);
        title_plane3.getRot().setZ(180f);
        title_plane3.getPos().setY(1f);
        title_plane3.getPos().setZ(0.4f);

        text_3d_object = new GameObject();
        text_3d_object.getRenderMediator().mesh = text_3d;
        text_3d_object.getRenderMediator().isDraw = true;
        text_3d_object.getRot().setY(180f);
        text_3d_object.getPos().setY(-0.5f);

        aspect = (float)mode_select_string.getWidth() / (float)mode_select_string.getHeight();

        mode_select_plane = new GameObject();
        mode_select_plane.getRenderMediator().mesh = mode_mesh;
        mode_select_plane.getRenderMediator().isDraw = false;
        mode_select_plane.getRot().setX(-90f);
        mode_select_plane.getRot().setZ(180f);
        mode_select_plane.getScl().setZ(0.5f);
        mode_select_plane.getScl().setX(aspect*0.5f);
        mode_select_plane.getPos().setZ(10f);
        mode_select_plane.getPos().setY(0.7f);

        renderer.addItem(title_plane1);
        renderer.addItem(title_plane2);
        renderer.addItem(title_plane3);
        renderer.addItem(text_3d_object);
        renderer.addItem(mode_select_plane);

        length = Math.abs(text_3d_object.getPos().getZ()-offset - camera.getPosition(Camera.POSITION.Z));

        target_pos = new Vector3(camera.getPosition(Camera.POSITION.X),camera.getPosition(Camera.POSITION.Y),camera.getPosition(Camera.POSITION.Z));
        bezie_start = new Vector3(camera.getPosition(Camera.POSITION.X),camera.getPosition(Camera.POSITION.Y),camera.getPosition(Camera.POSITION.Z));
        bezie_end = new Vector3(0,0,7f);
        bezie1 = new Vector3(0,0,-5f);
        bezie2 = new Vector3(0,0,7f);
        moveTime = 1f;
    }
    @Override
    public void Proc() {
        if(state == STATE.MOVE) {
            Bezier.bezier3Trajectory(target_pos, bezie_start, bezie_end, bezie1, bezie2, 1f / moveTime * timeBuffer);
            timeBuffer += Time.getDeltaTime();
        }
        if(state == STATE.OPENING || state == STATE.MOVE) {
            camera.setPosition(0, 0, target_pos.getZ());
            camera.setLookPosition(0, 0, target_pos.getZ() + 0.05f);
            text_3d_object.getRenderMediator().alpha = (1f / length * Math.abs(text_3d_object.getPos().getZ() - offset - camera.getPosition(Camera.POSITION.Z)));
        }
        if(state == STATE.MOVE || state == STATE.MODE_SELECT){
            mode_select_plane.getRenderMediator().isDraw = true;
        }
        if(state == STATE.MODE_SELECT){
            text_3d_object.getRenderMediator().isDraw = false;
            title_plane1.getRenderMediator().isDraw = false;
            title_plane2.getRenderMediator().isDraw = false;
            title_plane3.getRenderMediator().isDraw = false;
        }
        if(timeBuffer != -1 && timeBuffer > moveTime){
            timeBuffer = -1;
            state = STATE.MODE_SELECT;
        }

    }

    @Override
    public void Draw(float offsetX, float offsetY) {
        renderer.drawAll();
    }

    @Override
    public void Touch() {
        if(state == STATE.OPENING){
            for(int n = 0;n < Input.getTouchArray().length;n++){
                if(Input.getTouchArray()[n].getTouchID() != -1)
                    state = STATE.MOVE;
            }
        }else if(state == STATE.MODE_SELECT){

        }
    }

    @Override
    public void death() {
        title_mesh.deleteBufferObject();
        text_3d.deleteBufferObject();
    }

    @Override
    public void init() {
        title_mesh.useBufferObject();
        text_3d.useBufferObject();
    }
}
