package com.example.sd2020.Diary;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.loader.content.CursorLoader;

import com.bumptech.glide.Glide;
import com.example.sd2020.Fragmentc2;
import com.example.sd2020.Fragmentp2;
import com.example.sd2020.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class WritediaryActivity extends AppCompatActivity {

    String strId;
    String strFamilyId;
    String strDate;
    String strTag;
    String strUri;

    EditText etTag;
    EditText etDate;

    // 파이어베이스 데이터베이스
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseReference = mDatabase.getReference();
    private FirebaseAuth mAuth= FirebaseAuth.getInstance();

    // 파이어베이스 스토리지
    private FirebaseStorage storage = FirebaseStorage.getInstance("gs://su-7bccc.appspot.com");
    private StorageReference storageReference = storage.getReference();
    private StorageReference imgReference;

    // 허가
    private static final int GALLERY_CODE=10;
    private static final int CAMERA_CODE=20;
    private Uri cameraUri;
    private String imageFilePath;

    // 업로드에 필요한 전역 변수
    private Uri file,downloadUri,prefile;
    private UploadTask uploadTask;
    private String picture="";
    private ImageView imageViewAddNameContest;
    private int[] endDays = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    // 진행 상황
    private ProgressDialog pdialog=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writediary);

        Intent it = getIntent();
        strId = it.getStringExtra("id");
        strFamilyId = it.getStringExtra("familyId");

        /*Edit text*/
        etTag = (EditText)findViewById(R.id.edittext_title);
        etDate = (EditText)findViewById(R.id.edittext_date);
        imageViewAddNameContest = (ImageView)findViewById(R.id.imageViewAddNameContest);

        if(!strId.equals("null")){
            mDatabaseReference.child("Diary").child(strFamilyId).child(strId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Article article = dataSnapshot.getValue(Article.class);
                    strTag = article.getTag();
                    strDate = article.getDate();
                    etTag.setText(strTag);
                    etDate.setText(strDate);
                    strUri = article.getUriImage();
                    if(!strId.equals("null")) {
                        cameraUri = Uri.parse(strUri);
                        Glide.with(WritediaryActivity.this).load(cameraUri).into(imageViewAddNameContest);
                        prefile = cameraUri;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.w("TAG: ", "Failed to read value", databaseError.toException());
                }

            });

        }
        else{
            long now = System.currentTimeMillis();
            Date mDate = new Date(now);
            SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
            strDate = simpleDate.format(mDate);
            etDate.setText(strDate);
            //et_date.setText(userEmail);
        }

        checkSelfPermission();
    }

    public void checkSelfPermission() {
        String temp = "";

        //읽기 권한
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.READ_EXTERNAL_STORAGE + " ";
        }

        //쓰기 권한
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.WRITE_EXTERNAL_STORAGE + " ";
        }

        //카메라 권한
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.CAMERA + " ";
        }

        //권한 체크
        if (TextUtils.isEmpty(temp) == false) {
            ActivityCompat.requestPermissions(this, temp.trim().split(" "),1);
        }
        else {
            //Toast.makeText(this, "갤러리 접근 허용", Toast.LENGTH_SHORT).show();
        }
    }

    public void delete(View v){
        String path = "Diary/" + strFamilyId + "/" + strId + ".jpg";
        imgReference = storageReference.child(path);
        imgReference.delete();

        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mDatabaseReference.child("Diary").child(strFamilyId).child(strId).removeValue();
                Log.d("일기","성공"+strTag);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("일기","실패");
            }
        });

        mDatabaseReference.child("Diary").child(strFamilyId).child(strId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Intent intent = getIntent();
                intent.putExtra("result", "cancel");
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void ToastText(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void addName() {
        if (file == null&&prefile==null) {
            ToastText("사진을 추가해주세요.");
            return;
        }

        uploadStorage();
    }

    private int leap_year(int year, int month) {
        if(month != 2) return 0;
        if(year % 400 == 0) return 1;
        if(year % 100 ==0) return 0;
        if(year % 4 == 0)return 1;
        return 0;
    }

    private int cal_time(int time) {
        for(int i=0;i<7;i++) {
            time += 1;
            int year = time/10000;
            time %= 10000;
            int month = time / 100;
            int day = time % 100;

            if(day > endDays[month] + leap_year(year, month)) {
                day = 1;
                if(++month == 13) {
                    month = 1;
                    year++;
                }
            }

            time = year * 10000 + month * 100 + day;
        }

        return time;
    }

    private void uploadStorage() {
        final Long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String startTime = simpleDateFormat.format(mDate);
        int time = Integer.parseInt(startTime.substring(0,8));
        /*time -= 8;
        startTime = time + startTime.substring(8, 14);*/

        time = cal_time(time);
        String endTime = time + startTime.substring(8, 14);

        // 랜덤 id 생성
        if(strId.equals("null")){
            Random random = new Random();
            strId= strFamilyId + "|" + now + "|" + random.nextLong();
        }

        String path = "Diary/" + strFamilyId + "/" + strId + ".jpg";
        imgReference = storageReference.child(path);
        if(file == null&&prefile==null) {
            Toast.makeText(this, "이미지 파일이 존재하지 않습니다!", Toast.LENGTH_SHORT).show();
            return;
        }

        strTag = etTag.getText().toString().trim();
        strDate = etDate.getText().toString().trim();

        final Article nameContestData = new Article(strId, strFamilyId, strDate, strTag, path);

        if(strTag.equals("")){
            ToastText("태그를 입력해주세요.");
            return;
        }
        
        if(file==null){
            nameContestData.setUriImage(prefile.toString());
            addFinalData(nameContestData);
        }
        else {
            uploadTask = imgReference.putFile(file);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.v("ADDNAMECONTEST", "addUri()");
                    addUri(nameContestData);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
    }

    public void imageDownload(View v){
        if(strId.equals("null")){
            Toast.makeText(this, "이미지 파일이 존재하지 않습니다!", Toast.LENGTH_SHORT).show();
            return;
        }

        String path = "Diary/" + strFamilyId + "/" + strId + ".jpg";
        imgReference = storageReference.child(path);

        try{
            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            final File localFile = File.createTempFile(strId, ".jpg", storageDir);
            imgReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(), "다운로드 성공", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(Uri.fromFile(localFile));
                    sendBroadcast(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(getApplicationContext(), "다운로드 실패", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) { Toast.makeText(getApplicationContext(), "다운로드 실패", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void addUri(final Article nameContestData) {

        Task<Uri> urlTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(!task.isSuccessful()){
                    throw task.getException();
                }
                return imgReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){
                    downloadUri=task.getResult();
                    assert downloadUri != null;
                    nameContestData.setUriImage(downloadUri.toString());
                    Log.v("ADDNAMECONTEST", "addFinalData()");
                    addFinalData(nameContestData);
                }
            }
        });
    }

    private void addFinalData(final Article nameContestData) {

        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mDatabaseReference.child("Diary").child(strFamilyId).child(strId).setValue(nameContestData);
                Log.d("일기","성공"+strTag);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("일기","실패");
            }
        });


        // 변경 이벤트가 있으면 현재 하던 행동 취소.. 아마 충돌 없애려고 만든듯
        mDatabaseReference.child("Diary").child(strFamilyId).child(strId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Intent intent = getIntent();
                intent.putExtra("result", "cancel");
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Intent intent = getIntent();
                intent.putExtra("result", "cancel");
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_CODE && data != null) {

            Uri uri = data.getData();
            Log.v("12345", uri.toString());

            if(uri != null) {
                file = uri;
                try{
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    imageViewAddNameContest.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        else if (requestCode == CAMERA_CODE && resultCode==RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);
            ExifInterface exif = null;
            try{
                exif = new ExifInterface(imageFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int exifOrientation;
            int exifDegree;
            if(exif!=null){
                exifOrientation=exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree=exifOrientationToDegree(exifOrientation);
            }
            else{
                exifDegree=0;
            }
            bitmap=rotate(bitmap,exifDegree);
            imageViewAddNameContest.setImageBitmap(bitmap);
            String imageSaveUri= MediaStore.Images.Media.insertImage(getContentResolver(),bitmap,"사진 저장","저장되었다");
            Uri uri = Uri.parse(imageSaveUri);
            file=uri;
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,uri));
        }
    }

    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private int exifOrientationToDegree(int exifOrientation){
        if(exifOrientation== ExifInterface.ORIENTATION_ROTATE_90){
            return 90;
        }
        else if(exifOrientation== ExifInterface.ORIENTATION_ROTATE_180){
            return 180;
        }
        else if(exifOrientation== ExifInterface.ORIENTATION_ROTATE_270){
            return 270;
        }
        return 0;
    }

    public String getPath(Uri uri){
        String[] proj={MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this,uri,proj,null,null,null);

        Cursor cursor= cursorLoader.loadInBackground();
        int index=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();
        return cursor.getString(index);
    }

    private void goToAlbum() {
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent,GALLERY_CODE);
    }

    private void takePhoto() {
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager())!=null){
            File photoFile=null;
            try{
                photoFile=createImageFile();
            }catch(IOException e){
                e.printStackTrace();
            }
            if (photoFile != null) {
                cameraUri = FileProvider.getUriForFile(this, getPackageName(), photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
                startActivityForResult(intent, CAMERA_CODE);
            }
        }
    }

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "TEST_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,      /* prefix */
                ".jpg",         /* suffix */
                storageDir          /* directory */
        );
        imageFilePath = image.getAbsolutePath();
        return image;
    }

    private void imageUpload() {

        new AlertDialog.Builder(WritediaryActivity.this)
                .setTitle("사진 업로드")
                .setNegativeButton("사진촬영", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e("AddName", "takePhoto()");
                        takePhoto();
                    }
                })
                .setPositiveButton("앨범선택", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e("AddName", "goToAlbum()");
                        goToAlbum();
                    }
                })
                .setNeutralButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ToastText("취소하였습니다");
                    }
                })
                .show();
    }

    public void onClickAddNameContest(View v) {
        switch (v.getId()) {
            case R.id.button_save:
                //register();
                addName();
                break;
            case R.id.imageViewAddNameContest:
                imageUpload();
                break;
        }
    }

}


