package com.erdemselvi.fotografpaylasmakotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.TokenWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_feed.*
import java.util.ArrayList

class FeedActivity : AppCompatActivity() {
    private lateinit var auth:FirebaseAuth
    private lateinit var db:FirebaseFirestore
    var postList=ArrayList<Post>()
    private lateinit var recyclerViewAdapter:FeedRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        auth=FirebaseAuth.getInstance()
        db= FirebaseFirestore.getInstance()

        verileriAl()

        //RecyclerView
        var layoutManager=LinearLayoutManager(this)
        recyclerView.layoutManager=layoutManager
        recyclerViewAdapter=FeedRecyclerAdapter(postList)
        recyclerView.adapter=recyclerViewAdapter
    }
    fun verileriAl(){
        // db.collection("Post").document("documenid").get()
        //Yukarıdaki gibi yaparsak sadece id ye göre tek bir kayıtta getirebiliriz.

        //collection("post").whereEqualTo("kullaniciemail","erdem@erdem.com")  diyerek bu kullanıcı emailine ait kayıtları listeleyebilirdik.
        db.collection("Post").orderBy("tarih",Query.Direction.DESCENDING).addSnapshotListener { snapshot, exception ->

            if(exception!=null){
                Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()
            }else{
                if(snapshot!=null){
                    if(!snapshot.isEmpty){ //snapshot.isEmpty==false

                        val documents=snapshot.documents

                        postList.clear()

                        for(document in documents){
                            document.id
                            val kullaniciEmail=document.get("kullaniciemail") as String
                            val kullaniciYorum=document.get("kullaniciyorumu") as String
                            val gorselUrl=document.get("gorselurl") as String
                            val timestamp=document.get("tarih") as Timestamp
                            val tarih=timestamp.toDate()

                            val indirilenPost=Post(kullaniciEmail,kullaniciYorum,gorselUrl)
                            postList.add(indirilenPost)

                        }

                        recyclerViewAdapter.notifyDataSetChanged()

                    }


                }
            }

        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val manuInflater=menuInflater
        menuInflater.inflate(R.menu.options_menu_fotograf,menu)


        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.fotograf_ekle){
            val intent=Intent(applicationContext,FotografActivity::class.java)
            startActivity(intent)

        }else if(item.itemId==R.id.cikis_yap){
            auth.signOut()
            val intent=Intent(applicationContext,KullaniciActivity::class.java)
            startActivity(intent)
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}