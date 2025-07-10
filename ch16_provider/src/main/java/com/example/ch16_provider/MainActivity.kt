package com.example.ch16_provider

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.ch16_provider.databinding.ActivityMainBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var filePath: String // 카메라로 찍은 사진이 저장될 파일 경로

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding 설정
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 갤러리 앱 호출 및 결과 처리에 사용할 launcher 등록
        val requestGalleryLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
            try {
                // 선택한 이미지의 적절한 해상도 비율 계산
                val calRatio = calculateInSampleSize(
                    it.data!!.data!!,
                    resources.getDimensionPixelSize(R.dimen.imgSize),
                    resources.getDimensionPixelSize(R.dimen.imgSize)
                )

                // 비트맵 옵션 설정 (리사이징 적용)
                val option = BitmapFactory.Options()
                option.inSampleSize = calRatio

                // 이미지 스트림 열고 Bitmap 으로 디코딩
                var inputStream = contentResolver.openInputStream(it.data!!.data!!)
                val bitmap = BitmapFactory.decodeStream(inputStream, null, option)
                inputStream!!.close()

                // 이미지 설정
                bitmap?.let {
                    binding.userImageView.setImageBitmap(bitmap)
                } ?: let {
                    Log.d("kkang", "bitmap null") // 디코딩 실패 로그
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // 갤러리 버튼 클릭 시, 갤러리 앱 실행
        binding.galleryButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*" // 이미지 타입만
            requestGalleryLauncher.launch(intent)
        }

        // 카메라 앱 실행 후 결과를 받을 launcher 등록
        val requestCameraFileLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
            // 저장된 파일 경로로부터 비트맵 디코딩
            val calRatio = calculateInSampleSize(
                Uri.fromFile(File(filePath)),
                resources.getDimensionPixelSize(R.dimen.imgSize),
                resources.getDimensionPixelSize(R.dimen.imgSize)
            )
            val option = BitmapFactory.Options()
            option.inSampleSize = calRatio
            val bitmap = BitmapFactory.decodeFile(filePath, option)

            // 이미지 설정
            bitmap?.let {
                binding.userImageView.setImageBitmap(bitmap)
            }
        }

        // 카메라 버튼 클릭 시
        binding.cameraButton.setOnClickListener {
            // 저장할 파일 생성
            val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val file = File.createTempFile(
                "JPEG_${timeStamp}_", // 파일 이름 형식
                ".jpg", // 확장자
                storageDir // 저장 위치
            )
            filePath = file.absolutePath // 파일 경로 저장

            // FileProvider 를 통해 외부 접근 가능한 content:// URI 생성
            val photoURI: Uri = FileProvider.getUriForFile(
                this,
                "com.example.ch16_provider.fileprovider", // manifest 와 일치해야 함
                file
            )

            // 카메라 앱 실행
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI) // 사진 저장 위치 지정
            requestCameraFileLauncher.launch(intent)
        }
    }

    /**
     * 이미지의 크기를 줄이기 위해 비율 계산
     */
    private fun calculateInSampleSize(fileUri: Uri, reqWidth: Int, reqHeight: Int): Int {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true // 실제 이미지 로드하지 않고 정보만 얻기

        try {
            var inputStream = contentResolver.openInputStream(fileUri)
            BitmapFactory.decodeStream(inputStream, null, options) // 정보만 추출
            inputStream!!.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // 원본 이미지 크기 가져오기
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        // 지정된 크기보다 크다면 2의 배수로 줄여가며 계산
        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize // 최종 샘플 비율 반환
    }
}