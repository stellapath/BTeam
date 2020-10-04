package com.project.bteam.board;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.project.bteam.R;
import com.project.bteam.user.model.UserVO;
import com.project.bteam.util.Common;
import com.project.bteam.util.MyMotionToast;

import java.util.HashMap;
import java.util.Map;

public class BoardInsertActivity extends AppCompatActivity {

    private EditText title, content;
    private TextView writer, email, image_name;
    private LinearLayout attachment;
    private Button insert;
    private FirebaseUser firebaseUser;
    private DatabaseReference refer;
    private StorageReference storageReference;
    private StorageTask uploadTask;
    private Uri imageUri;

    private String userImageURL;
    private String category;
    private String imageURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_insert);

        initView();

        if (getIntent() != null) {
            category = getIntent().getStringExtra("category");
        }

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        refer = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        email.setText(firebaseUser.getEmail());
        refer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserVO vo = snapshot.getValue(UserVO.class);
                writer.setText(vo.getUsername());
                userImageURL = vo.getImageURL();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // 이미지 첨부 클릭 시
        attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePicker();
            }
        });

        // 게시글 작성 클릭 시
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // useruid username useremail userimageurl time title content imageurl
                uploadImage();

                refer = FirebaseDatabase.getInstance().getReference(category);
                Map<String, Object> map = new HashMap<>();
                map.put("userUid", firebaseUser.getUid());
                map.put("username", writer.getText().toString());
                map.put("userEmail", firebaseUser.getEmail());
                map.put("userImageURL", userImageURL);
                map.put("time", System.currentTimeMillis());
                map.put("title", title.getText().toString());
                map.put("content", content.getText().toString());
                map.put("imageURL", imageURL);

                refer.child("Boards").push().setValue(map);
            }
        });
    }

    private void initView() {
        title = findViewById(R.id.board_insert_title);
        writer = findViewById(R.id.board_insert_writer);
        email = findViewById(R.id.board_insert_email);
        content = findViewById(R.id.board_insert_content);
        image_name = findViewById(R.id.board_insert_image);
        attachment = findViewById(R.id.board_insert_attachment);
        insert = findViewById(R.id.board_insert_button);
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, Common.REQUEST_ATTACHMENT);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {
        if (imageUri != null) {
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUri));

            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWith(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return  fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        imageURL = downloadUri.toString();

                    } else {
                        MyMotionToast.errorToast(BoardInsertActivity.this, "이미지 업로드에 실패했습니다.");
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    MyMotionToast.errorToast(BoardInsertActivity.this, "이미지 업로드에 실패했습니다.");
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Common.REQUEST_ATTACHMENT && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
        }
    }
}