package com.geopdfviewer.android;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

import org.litepal.LitePal;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class singlepoi extends AppCompatActivity {

    private static final String TAG = "singlepoi";
    private String POIC;
    private String name;
    private EditText editText_name;
    private EditText editText_des;
    private final static int REQUEST_CODE_PHOTO = 42;
    private final static int REQUEST_CODE_TAPE = 43;
    private final static int TAKE_PHOTO = 41;
    Uri imageUri;
    Spinner type_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singlepoi);
        type_spinner = (Spinner) findViewById(R.id.type_selection);
        //声明ToolBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("兴趣点信息");
        Intent intent = getIntent();
        POIC = intent.getStringExtra("POIC");
        textView_photonum = (TextView) findViewById(R.id.txt_photonumshow);


    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }
int showNum = 0;
    List<bt> bms;
    PointF pt0 = new PointF();
    boolean ges = false;
    TextView textView_photonum;
    String str = "";

    private void refresh(){
        List<POI> pois = LitePal.where("poic = ?", POIC).find(POI.class);
        List<MTAPE> tapes = LitePal.where("poic = ?", POIC).find(MTAPE.class);
        final List<MPHOTO> photos = LitePal.where("poic = ?", POIC).find(MPHOTO.class);

        //
        String[] strings = getResources().getStringArray(R.array.Type);
        for (int i = 0; i < strings.length; i++) {
            Log.w(TAG, "refresh: " + strings[i]);
            if (strings[i].equals(pois.get(0).getType())) type_spinner.setSelection(i);
        }
        //

        type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str = type_spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                str = type_spinner.getSelectedItem().toString();
            }
        });
        getBitmap(photos);
        Log.w(TAG, "pois: " + pois.size() + "\n");
        Log.w(TAG, "tapes1: " + pois.get(0).getTapenum() + "\n");
        Log.w(TAG, "photos1: " + pois.get(0).getPhotonum() + "\n");
        Log.w(TAG, "tapes: " + tapes.size() + "\n");
        Log.w(TAG, "photos: " + photos.size());
        POI poi1 = new POI();
        if (tapes.size() != 0) poi1.setTapenum(tapes.size());
        else poi1.setToDefault("tapenum");
        if (photos.size() != 0) {
            poi1.setPhotonum(photos.size());
            final ImageView imageView = (ImageView) findViewById(R.id.photo_image_singlepoi);
            imageView.setVisibility(View.VISIBLE);
            String path = photos.get(0).getPath();
            File file = new File(path);
            try {
                if (file.exists()) {
                    Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                    int degree = DataUtil.getPicRotate(path);
                    if (degree != 0) {
                        Matrix m = new Matrix();
                        m.setRotate(degree); // 旋转angle度
                        Log.w(TAG, "showPopueWindowForPhoto: " + degree);
                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
                    }
                    imageView.setImageBitmap(bitmap);
                }else {
                    Drawable drawable = MyApplication.getContext().getResources().getDrawable(R.drawable.imgerror);
                    BitmapDrawable bd = (BitmapDrawable) drawable;
                    Bitmap bitmap = Bitmap.createBitmap(bd.getBitmap(), 0, 0, bd.getBitmap().getWidth(), bd.getBitmap().getHeight());
                    bitmap = ThumbnailUtils.extractThumbnail(bitmap, 80, 120,
                            ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
                    imageView.setImageBitmap(bitmap);
                }
            }catch (IOException e){
                Log.w(TAG, e.toString());
            }
            if (photos.size() >= 1) {
                imageView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        float distanceX = 0;
                        float distanceY = 0;
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                //第一个手指按下
                                pt0.set(event.getX(0), event.getY(0));
                                Log.w(TAG, "onTouchdown: " + event.getX());
                                Log.w(TAG, "手指id: " + event.getActionIndex());
                                Log.w(TAG, "ACTION_POINTER_DOWN");
                                Log.w(TAG, "同时按下的手指数量: " + event.getPointerCount());
                                break;
                            case MotionEvent.ACTION_POINTER_DOWN:
                                //第二个手指按下
                                Log.w(TAG, "手指id: " + event.getActionIndex());
                                Log.w(TAG, "onTouchdown: " + event.getX());
                                Log.w(TAG, "ACTION_POINTER_DOWN");
                                Log.w(TAG, "同时按下的手指数量: " + event.getPointerCount());
                                break;
                            case MotionEvent.ACTION_UP:
                                //最后一个手指抬起
                                ges = false;
                                    Log.w(TAG, "onTouchup: " + event.getX());
                                    Log.w(TAG, "getPointerId: " + event.getPointerId(0));
                                    distanceX = event.getX(0) - pt0.x;
                                    distanceY = event.getY(0) - pt0.y;
                                    Log.w(TAG, "onTouch: " + distanceX);
                                    if (Math.abs(distanceX) > Math.abs(distanceY) & Math.abs(distanceX) > 200 & Math.abs(distanceY) < 100) {
                                        if (distanceX > 0) {
                                            Log.w(TAG, "bms.size : " + bms.size());
                                            showNum++;
                                            if (showNum > bms.size() - 1) {
                                                showNum = 0;
                                                imageView.setImageBitmap(bms.get(0).getM_bm());
                                            } else {
                                                imageView.setImageBitmap(bms.get(showNum).getM_bm());
                                            }
                                        } else {
                                            showNum--;
                                            if (showNum < 0) {
                                                showNum = bms.size() - 1;
                                                imageView.setImageBitmap(bms.get(showNum).getM_bm());
                                            } else {
                                                imageView.setImageBitmap(bms.get(showNum).getM_bm());
                                            }
                                        }
                                    Log.w(TAG, "同时抬起的手指数量: " + event.getPointerCount());
                                    Log.w(TAG, "手指id: " + event.getActionIndex());
                                }
                                break;
                            case MotionEvent.ACTION_MOVE:
                                if (event.getPointerCount() == 3) {
                                    Log.w(TAG, "3指滑动");

                                }
                                else if (event.getPointerCount() == 4) {
                                    if (!ges) {
                                        Log.w(TAG, "4指滑动");
                                        AlertDialog.Builder dialog = new AlertDialog.Builder(singlepoi.this);
                                        dialog.setTitle("提示");
                                        dialog.setMessage("确认删除图片吗?");
                                        dialog.setCancelable(false);
                                        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                List<POI> pois1 = LitePal.where("poic = ?", POIC).find(POI.class);
                                                if (pois1.get(0).getPhotonum() > 0) {
                                                    textView_photonum.setText(Integer.toString(pois1.get(0).getPhotonum() - 1));
                                                    POI poi = new POI();
                                                    poi.setPhotonum(pois1.get(0).getPhotonum() - 1);
                                                    poi.updateAll("poic = ?", POIC);
                                                    LitePal.deleteAll(MPHOTO.class, "poic = ? and path = ?", POIC, bms.get(showNum).getM_path());
                                                    bms.remove(showNum);
                                                    if (showNum > pois1.get(0).getPhotonum() - 1) {
                                                        if (bms.size() > 0) imageView.setImageBitmap(bms.get(0).getM_bm());
                                                        else imageView.setVisibility(View.GONE);
                                                    }
                                                    else if (showNum < pois1.get(0).getPhotonum() - 1) imageView.setImageBitmap(bms.get(showNum).getM_bm());
                                                    else imageView.setVisibility(View.GONE);
                                                    Toast.makeText(singlepoi.this, "已经删除图片", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        });
                                        dialog.show();
                                        ges = true;
                                    }
                                }
                                else if (event.getPointerCount() == 5) {
                                    Log.w(TAG, "5指滑动");
                                }
                                else if (event.getPointerCount() == 2) {
                                    Log.w(TAG, "2指滑动");
                                }
                                break;

                        }
                        return true;
                    }
                });
            }
        }
        else poi1.setToDefault("photonum");
        poi1.updateAll("poic = ?", POIC);
        Log.w(TAG, "refresh: " + poi1.updateAll("poic = ?", POIC));
        /*POI poi = new POI();
        poi.setPhotonum(photos.size());
        poi.setTapenum(tapes.size());
        poi.updateAll("POIC = ?", POIC);*/
        List<POI> pois1 = LitePal.where("poic = ?", POIC).find(POI.class);
        Log.w(TAG, "tapes2: " + pois1.get(0).getTapenum() + "\n");
        Log.w(TAG, "photos2: " + pois1.get(0).getPhotonum() + "\n");
        POI poi = pois.get(0);
        name = poi.getName();
        editText_name = (EditText) findViewById(R.id.edit_name);
        editText_name.setText(name);
        editText_des = (EditText) findViewById(R.id.edit_des);
        if (poi.getDescription() != null) {
            editText_des.setText(poi.getDescription());
        }else editText_des.setText("");
        TextView textView_time = (TextView) findViewById(R.id.txt_timeshow);
        textView_time.setText(poi.getTime());
        Log.w(TAG, Integer.toString(tapes.size()));
        textView_photonum.setText(Integer.toString(photos.size()));
        textView_photonum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开图片列表
                Intent intent1 = new Intent(singlepoi.this, photoshow.class);
                intent1.putExtra("POIC", POIC);
                startActivity(intent1);
            }
        });
        TextView textView_tapenum = (TextView) findViewById(R.id.txt_tapenumshow);
        textView_tapenum.setText(Integer.toString(tapes.size()));
        textView_tapenum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开录音列表
                Intent intent2 = new Intent(singlepoi.this, tapeshow.class);
                intent2.putExtra("POIC", POIC);
                startActivity(intent2);
            }
        });
        TextView textView_loc = (TextView) findViewById(R.id.txt_locshow);
        DecimalFormat df = new DecimalFormat("0.0000");
        textView_loc.setText(df.format(poi.getX()) + ", " + df.format(poi.getY()));
        ImageButton addphoto = (ImageButton)findViewById(R.id.addPhoto_singlepoi);
        addphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopueWindowForPhoto();
            }
        });
        ImageButton addtape = (ImageButton)findViewById(R.id.addTape_singlepoi);
        addtape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
                    startActivityForResult(intent, REQUEST_CODE_TAPE);
                }catch (ActivityNotFoundException e){
                    Toast.makeText(MyApplication.getContext(), R.string.TakeTapeError, Toast.LENGTH_LONG).show();
                }

            }
        });
        FloatingActionButton fab_saveinfo = (FloatingActionButton) findViewById(R.id.fab_saveinfo1);
        fab_saveinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                POI poi = new POI();
                poi.setName(editText_name.getText().toString());
                poi.setDescription(editText_des.getText().toString());
                poi.setType(str);
                poi.updateAll("poic = ?", POIC);
                Toast.makeText(singlepoi.this, R.string.SaveInfo, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getBitmap(final List<MPHOTO> photos){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.w(TAG, "run: photo.size" + photos.size());
                bms = new ArrayList<>();
                for (int i = 0; i < photos.size(); i++) {
                    String path = photos.get(i).getPath();
                    File file = new File(path);
                        if (file.exists()) {
                            try {
                            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                            int degree = DataUtil.getPicRotate(path);
                            if (degree != 0) {
                                Matrix m = new Matrix();
                                m.setRotate(degree); // 旋转angle度
                                Log.w(TAG, "showPopueWindowForPhoto: " + degree);
                                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
                            }
                                bms.add(new bt(bitmap,  path));
                            } catch (IOException e) {
                                Log.w(TAG, e.toString());
                                Drawable drawable = MyApplication.getContext().getResources().getDrawable(R.drawable.imgerror);
                                BitmapDrawable bd = (BitmapDrawable) drawable;
                                Bitmap bitmap = Bitmap.createBitmap(bd.getBitmap(), 0, 0, bd.getBitmap().getWidth(), bd.getBitmap().getHeight());
                                bitmap = ThumbnailUtils.extractThumbnail(bitmap, 80, 120,
                                        ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
                                bms.add(new bt(bitmap, ""));
                            }
                        } else {
                            Drawable drawable = MyApplication.getContext().getResources().getDrawable(R.drawable.imgerror);
                            BitmapDrawable bd = (BitmapDrawable) drawable;
                            Bitmap bitmap = Bitmap.createBitmap(bd.getBitmap(), 0, 0, bd.getBitmap().getWidth(), bd.getBitmap().getHeight());
                            bitmap = ThumbnailUtils.extractThumbnail(bitmap, 80, 120,
                                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
                            bms.add(new bt(bitmap, ""));
                        }
                }

                Log.w(TAG, "getBitmap: " + bms.size());
            }
        }).start();
    }

    private void showPopueWindowForPhoto(){
        View popView = View.inflate(this,R.layout.popupwindow_camera_need,null);
        Button bt_album = (Button) popView.findViewById(R.id.btn_pop_album);
        Button bt_camera = (Button) popView.findViewById(R.id.btn_pop_camera);
        Button bt_cancle = (Button) popView.findViewById(R.id.btn_pop_cancel);
        //获取屏幕宽高
        int weight = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels * 1/3;

        final PopupWindow popupWindow = new PopupWindow(popView, weight ,height);
        //popupWindow.setAnimationStyle(R.style.anim_popup_dir);
        popupWindow.setFocusable(true);
        //点击外部popueWindow消失
        popupWindow.setOutsideTouchable(true);

        bt_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchPicker();
                popupWindow.dismiss();

            }
        });
        bt_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
                popupWindow.dismiss();

            }
        });
        bt_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();

            }
        });
        //popupWindow消失屏幕变为不透明
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
        //popupWindow出现屏幕变为半透明
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
        popupWindow.showAtLocation(popView, Gravity.BOTTOM,0,50);

    }

    private void takePhoto(){
        File file2 = new File(Environment.getExternalStorageDirectory() + "/TuZhi/photo");
        if (!file2.exists() && !file2.isDirectory()){
            file2.mkdirs();
        }
        long timenow = System.currentTimeMillis();
        File outputImage = new File(Environment.getExternalStorageDirectory() + "/TuZhi/photo", Long.toString(timenow) + ".jpg");
        try {
            if (outputImage.exists()){
                outputImage.delete();
            }
            outputImage.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24){
            //locError(Environment.getExternalStorageDirectory() + "/maphoto/" + Long.toString(timenow) + ".jpg");
            imageUri = FileProvider.getUriForFile(singlepoi.this, "com.android.tuzhi.fileprovider", outputImage);

        }else imageUri = Uri.fromFile(outputImage);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_PHOTO) {
            Uri uri = data.getData();
            List<POI> POIs = LitePal.where("poic = ?", POIC).find(POI.class);
            POI poi = new POI();
            long time = System.currentTimeMillis();
            poi.setPhotonum(POIs.get(0).getPhotonum() + 1);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(singlepoi.this.getResources().getText(R.string.DateAndTime).toString());
            Date date = new Date(System.currentTimeMillis());
            poi.updateAll("poic = ?", POIC);
            MPHOTO mphoto = new MPHOTO();
            mphoto.setPdfic(POIs.get(0).getIc());
            mphoto.setPoic(POIC);
            //mphoto.setPath(getRealPath(uri.getPath()));
            mphoto.setPath(DataUtil.getRealPathFromUriForPhoto(this, uri));
            mphoto.setTime(simpleDateFormat.format(date));
            mphoto.save();
        }
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_TAPE){
            Uri uri = data.getData();
            //long time = System.currentTimeMillis();
            List<POI> POIs = LitePal.where("poic = ?", POIC).find(POI.class);
            POI poi = new POI();
            poi.setTapenum(POIs.get(0).getTapenum() + 1);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(singlepoi.this.getResources().getText(R.string.DateAndTime).toString());
            Date date = new Date(System.currentTimeMillis());
            poi.updateAll("poic = ?", POIC);
            MTAPE mtape = new MTAPE();
            mtape.setPath(DataUtil.getRealPathFromUriForAudio(this, uri));
            mtape.setPdfic(POIs.get(0).getIc());
            mtape.setPoic(POIC);
            mtape.setTime(simpleDateFormat.format(date));
            mtape.save();
        }
        if (resultCode == RESULT_OK && requestCode == TAKE_PHOTO) {
            Log.w(TAG, "onActivityResult1: " + imageUri.toString());
            String imageuri;
            if (Build.VERSION.SDK_INT >= 24) {
                imageuri = DataUtil.getRealPath(imageUri.toString());
            }else {
                imageuri = imageUri.toString().substring(7);
            }
            File file = new File(imageuri);
            Log.w(TAG, "onActivityResult2: " + imageuri);
            if (file.length() != 0) {
                try {
                    MediaStore.Images.Media.insertImage(getContentResolver(), imageuri, "title", "description");
                    // 最后通知图库更新
                    singlepoi.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + imageuri)));
                }catch (IOException e){
                }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(singlepoi.this.getResources().getText(R.string.DateAndTime).toString());
                Date date = new Date(System.currentTimeMillis());
                List<POI> POIs = LitePal.where("poic = ?", POIC).find(POI.class);
                POI poi = new POI();
                long time = System.currentTimeMillis();
                poi.setPhotonum(POIs.get(0).getPhotonum() + 1);
                poi.updateAll("poic = ?", POIC);
                MPHOTO mphoto = new MPHOTO();
                mphoto.setPoic(POIC);
                mphoto.setPath(imageuri);
                mphoto.setTime(simpleDateFormat.format(date));
                mphoto.save();
            }else {
                file.delete();
                Toast.makeText(singlepoi.this, R.string.TakePhotoError, Toast.LENGTH_LONG).show();
            }
        }
    }

    void launchPicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        try {
            startActivityForResult(intent, REQUEST_CODE_PHOTO);
        } catch (ActivityNotFoundException e) {
            //alert user that file manager not working
            Toast.makeText(this, R.string.toast_pick_file_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.poiinfotoolbar, menu);
        menu.findItem(R.id.back_pois).setVisible(false);
        menu.findItem(R.id.restore_pois).setVisible(false);
        menu.findItem(R.id.add_pois).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.query_poi_map:
                SharedPreferences.Editor editor = getSharedPreferences("update_query_attr_to_map", MODE_PRIVATE).edit();
                editor.putString("poic", POIC);
                editor.apply();
                this.finish();
                break;
            case R.id.back_andupdate:
                POI poi = new POI();
                poi.setName(editText_name.getText().toString());
                poi.setDescription(editText_des.getText().toString());
                poi.setType(str);
                poi.updateAll("poic = ?", POIC);
                this.finish();
                break;
            case R.id.deletepoi:
                AlertDialog.Builder dialog = new AlertDialog.Builder(singlepoi.this);
                dialog.setTitle("提示");
                dialog.setMessage("确认删除兴趣点吗?");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LitePal.deleteAll(POI.class, "poic = ?", POIC);
                        LitePal.deleteAll(MPHOTO.class, "poic = ?", POIC);
                        LitePal.deleteAll(MTAPE.class, "poic = ?", POIC);
                        singlepoi.this.finish();
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog.show();
                break;
            default:
        }
        return true;
    }
}
