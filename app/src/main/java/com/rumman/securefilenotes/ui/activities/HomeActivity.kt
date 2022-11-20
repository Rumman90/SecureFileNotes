package com.rumman.securefilenotes.ui.activities


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.rumman.securefilenotes.R
import com.rumman.securefilenotes.data.interfaces.PermissionListener
import com.rumman.securefilenotes.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var homeBinding: ActivityHomeBinding
    private lateinit var permissionListener: PermissionListener
    private val PERMISSION_REQUEST_CODE : Int = 200


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeBinding = DataBindingUtil.setContentView(this,R.layout.activity_home)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.home_fragment_host) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)

    }

    fun checkRequiredPermissions(permissionListener: PermissionListener){
        this.permissionListener = permissionListener
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            ) {
                this.permissionListener.permissionGranted()
            }else{
                if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) || shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)){
                    showRationaleDialog("Permission Required","Read/Write Storage Permissions are Required")

                }else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ),
                        PERMISSION_REQUEST_CODE
                    )
                }
                }
        }else{
            this.permissionListener.permissionGranted()
        }
    }

    private fun showRationaleDialog(title: String, desc: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(desc)
            .setCancelable(false)
            .setPositiveButton("Ok") { dialog, which ->
                dialog.cancel()
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package",this.packageName,null)
                intent.data = uri
                startActivity(intent)
            }
        builder.create().show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == PERMISSION_REQUEST_CODE){
           if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
               permissionListener.permissionGranted()
           } else{
               showRationaleDialog("Permission Required","Read/Write Storage Permissions are Required")
           }
        }
    }

    fun showProgressBar(){
        if(homeBinding.progressBar.isGone)
            homeBinding.progressBar.visibility = View.VISIBLE
    }

    fun hideProgressBar(){
        if (homeBinding.progressBar.isVisible)
            homeBinding.progressBar.visibility = View.GONE
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return true
    }

}