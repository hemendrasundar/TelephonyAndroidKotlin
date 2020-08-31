package com.hemendra.telephonytest

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.telephony.SmsManager
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {

     var uri:Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_call.setOnClickListener {

            call()
        }
 btn_attach.setOnClickListener {

     addAttachment()
 }

        btn_send_email.setOnClickListener {

            sendMail()
        }

        btn_sendsms.setOnClickListener {
            sendSMS()
        }

    }


    fun call()
    {
        var i = Intent()
        i.setAction(Intent.ACTION_CALL)
        i.setData(Uri.parse("tel:"+et_mobile.text.toString()))
        startActivity(i)
    }

    fun sendMail()
    {
         var intent = Intent()

        intent.setAction(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(et_email.text.toString()))
        intent.putExtra(Intent.EXTRA_SUBJECT,et_subject.text.toString())
        intent.putExtra(Intent.EXTRA_TEXT,et_email_message.text.toString())
        intent.putExtra(Intent.EXTRA_STREAM,uri)
        intent.setType("message/rfc822")
        startActivity(intent)
    }



    fun addAttachment()
    {

        var aDialog = AlertDialog.Builder(this)

        aDialog.setTitle("Attachment")
        aDialog.setMessage("choose Attachment")
        aDialog.setPositiveButton("Camera")
        {dialogInterface: DialogInterface, i: Int ->
            var  intent = Intent()
            intent.setAction("android.media.action.IMAGE_CAPTURE")
            startActivityForResult(intent,100)

        }

        aDialog.setNegativeButton("File")
        {dialogInterface: DialogInterface, i: Int ->
            var  intent = Intent()
            intent.setAction(Intent.ACTION_GET_CONTENT)
            intent.setType("*/*")
            startActivityForResult(intent,101)
        }
        aDialog.show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 100 && resultCode == Activity.RESULT_OK)
        {

                var bmp:Bitmap = data!!.extras!!.get("data") as Bitmap
                attach_iv.setImageBitmap(bmp)

                uri = getImageUri(this@MainActivity,bmp)

        }
        else if(requestCode == 101 && resultCode == Activity.RESULT_OK)
        {

            uri = data!!.data

            attach_iv.setImageURI(uri)
        }




    }

     fun getImageUri(context: Context, bmp:Bitmap):Uri?{

         var bytes = ByteArrayOutputStream()
         bmp.compress(Bitmap.CompressFormat.PNG,100,bytes)

         var path = MediaStore.Images.Media.insertImage(context.contentResolver,bmp,"test",null)




         return Uri.parse(path)
     }


    fun sendSMS()
    {

        var sIntent = Intent(this,SentActivity::class.java)
        var dIntent = Intent(this,DelieverActivity::class.java)

        var spIntent = PendingIntent.getActivity(this,0,sIntent,PendingIntent.FLAG_CANCEL_CURRENT)
        var dpIntent = PendingIntent.getActivity(this,0,dIntent,PendingIntent.FLAG_CANCEL_CURRENT)
        var sManager = SmsManager.getDefault()
        sManager.sendTextMessage(
            et_mobile.text.toString(),null,et_message.text.toString(),null,null)

    }



}