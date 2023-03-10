package com.erdemselvi.fotografpaylasmakotlin

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_fotograf.*
import java.net.URL

class PaylasActivity : AppCompatActivity() {

    var secilenGorsel:Uri?=null
    var secilenBitmap:Bitmap?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paylas)
    }
    fun fotografPaylas(view: View){

    }

    fun gorselSec(view: View){

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            //izin verilmediyse
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)

        }else{
            //izin verildiyse
            val intent=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent,2)
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if (requestCode==1){
            if (grantResults.size>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                val intent=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent,2)
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //galeride medyayı alınca ne yapacak
        if(requestCode==2 && resultCode==Activity.RESULT_OK && data!=null){

            secilenGorsel=data.data

            if (secilenGorsel!=null){

                if(Build.VERSION.SDK_INT>=28){
                    val source=ImageDecoder.createSource(this.contentResolver,secilenGorsel!!)
                    secilenBitmap=ImageDecoder.decodeBitmap(source)
                    imageView.setImageBitmap(secilenBitmap)

                }else{
                    val secilenBitmap=MediaStore.Images.Media.getBitmap(this.contentResolver,secilenGorsel)
                    imageView.setImageBitmap(secilenBitmap)
                }

            }

        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}