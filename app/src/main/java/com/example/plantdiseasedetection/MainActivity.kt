package com.example.plantdiseasedetection

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import kotlinx.coroutines.launch
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var mClassifier: Classifier
    private lateinit var mBitmap: Bitmap
    private lateinit var mPlaceholderText: TextView
    private lateinit var mProgressIndicator: CircularProgressIndicator
    private lateinit var mPhotoImageView: ImageView
    private lateinit var mResultTextView: TextView
    private lateinit var mCameraButton: MaterialButton
    private lateinit var mGalleryButton: MaterialButton
    private lateinit var mDetectButton: MaterialButton
    private lateinit var diseaseInfoButton: MaterialButton
    private val aiService: AIService = AIService() // Initialize directly, no lateinit
    private lateinit var myDialog: Dialog

    private var diseaseName: String? = null // Store disease name for dialog
    private var symptomsView: TextView? = null
    private var managementView: TextView? = null
    private var nameView: TextView? = null

    private val mCameraRequestCode = 0
    private val mGalleryRequestCode = 2
    private val mInputSize = 224
    private val mModelPath = "plant_disease_model.tflite"
    private val mLabelPath = "labels.txt"
    private val mSamplePath = "sample_img.jpg"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.main_activity)

        // Initialize dialog
        myDialog = Dialog(this)

        // Initialize views
        mCameraButton = findViewById(R.id.mCameraButton)
        mGalleryButton = findViewById(R.id.mGalleryButton)
        mDetectButton = findViewById(R.id.mDetectButton)
        diseaseInfoButton = findViewById(R.id.disease_info)
        mPhotoImageView = findViewById(R.id.mPhotoImageView)
        mResultTextView = findViewById(R.id.mResultTextView)
        mPlaceholderText = findViewById(R.id.placeholderText)
        mProgressIndicator = findViewById(R.id.progressIndicator)

        setupToolbar()
        setupClassifier()
        setupClickListeners()
        loadSampleImage()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (myDialog.isShowing) {
            myDialog.dismiss()
        }
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    private fun setupClassifier() {
        try {
            mClassifier = Classifier(assets, mModelPath, mLabelPath, mInputSize)
        } catch (e: Exception) {
            Log.e("MainActivity", "Error initializing classifier: ${e.message}")
            Toast.makeText(this, "Error initializing classifier: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun setupClickListeners() {
        mCameraButton.setOnClickListener {
            val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(callCameraIntent, mCameraRequestCode)
        }

        mGalleryButton.setOnClickListener {
            val callGalleryIntent = Intent(Intent.ACTION_PICK)
            callGalleryIntent.type = "image/*"
            startActivityForResult(callGalleryIntent, mGalleryRequestCode)
        }

        mDetectButton.setOnClickListener {
            if (::mBitmap.isInitialized) {
                performDetection()
            } else {
                Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show()
            }
        }

        diseaseInfoButton.setOnClickListener {
            if (mResultTextView.text.isNotEmpty() && mResultTextView.text != "Image loaded. Click 'Detect' to analyze.") {
                customDialog()
            } else {
                Toast.makeText(this, "Please detect a disease first", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadSampleImage() {
        try {
            resources.assets.open(mSamplePath).use {
                mBitmap = BitmapFactory.decodeStream(it)
                mBitmap = Bitmap.createScaledBitmap(mBitmap, mInputSize, mInputSize, true)
                updateImageDisplay(mBitmap)
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Error loading sample image: ${e.message}")
            Toast.makeText(this, "Error loading sample image: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun updateImageDisplay(bitmap: Bitmap) {
        mPhotoImageView.setImageBitmap(bitmap)
        mPlaceholderText.visibility = View.GONE
        mResultTextView.text = "Image loaded. Click 'Detect' to analyze."
    }

    private fun performDetection() {
        if (!::mClassifier.isInitialized) {
            Toast.makeText(this, "Classifier not initialized", Toast.LENGTH_SHORT).show()
            return
        }

        showLoading(true)
        try {
            val results = mClassifier.recognizeImage(mBitmap).firstOrNull()
            diseaseName = results?.title ?: "No disease detected"
            mResultTextView.text = diseaseName
            Toast.makeText(this, "Analysis completed!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("MainActivity", "Error during detection: ${e.message}")
            mResultTextView.text = "Error during detection: ${e.message}"
            Toast.makeText(this, "Detection failed: ${e.message}", Toast.LENGTH_LONG).show()
        } finally {
            showLoading(false)
        }
    }

    private fun showLoading(show: Boolean) {
        mProgressIndicator.visibility = if (show) View.VISIBLE else View.GONE
        mDetectButton.isEnabled = !show
    }

    private fun customDialog() {
        try {
            myDialog.setContentView(R.layout.detail_dialog_act)
            myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            nameView = myDialog.findViewById(R.id.pltd_name)
            symptomsView = myDialog.findViewById(R.id.symptoms)
            managementView = myDialog.findViewById(R.id.management)

            val disease = diseaseName?.replace("___", " ")?.replace("_", " ")?.lowercase() ?: "Unknown"
            nameView?.text = disease
            symptomsView?.text = "🤖 Generating symptoms..."
            managementView?.text = "🤖 Generating management..."

            lifecycleScope.launch {
                try {
                    val result = aiService.getPlantDiseaseInfo(disease)
                    symptomsView?.text = result.symptoms
                    managementView?.text = result.management
                } catch (e: Exception) {
                    Log.e("MainActivity", "Failed to fetch disease info: ${e.message}")
                    symptomsView?.text = "❌ Error: ${e.message}"
                    managementView?.text = "❌ Error: ${e.message}"
                    Toast.makeText(this@MainActivity, "Failed to load disease info", Toast.LENGTH_LONG).show()
                }
            }

            myDialog.findViewById<View>(R.id.closeButton)?.setOnClickListener { myDialog.dismiss() }
            myDialog.findViewById<View>(R.id.closeDialogButton)?.setOnClickListener { myDialog.dismiss() }
            myDialog.show()
        } catch (e: Exception) {
            Log.e("MainActivity", "Dialog init failed: ${e.message}")
            Toast.makeText(this, "Failed to show dialog", Toast.LENGTH_LONG).show()
        }
    }

    private fun loadJSONFromAsset(): String? {
        return try {
            assets.open("data.json").use { inputStream ->
                val buffer = ByteArray(inputStream.available())
                inputStream.read(buffer)
                String(buffer, Charsets.UTF_8)
            }
        } catch (e: IOException) {
            Log.e("MainActivity", "Error loading JSON from asset: ${e.message}")
            null
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            mCameraRequestCode -> handleCameraResult(resultCode, data)
            mGalleryRequestCode -> handleGalleryResult(data)
            else -> Toast.makeText(this, "Unrecognized request code", Toast.LENGTH_LONG).show()
        }
    }

    private fun handleCameraResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            try {
                mBitmap = data.extras?.get("data") as Bitmap
                mBitmap = scaleImage(mBitmap)
                updateImageDisplay(mBitmap)
                // Optional: Auto-detect after capturing image
            } catch (e: Exception) {
                Log.e("MainActivity", "Error processing camera image: ${e.message}")
                Toast.makeText(this, "Error processing camera image: ${e.message}", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this, "Camera cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleGalleryResult(data: Intent?) {
        try {
            val uri = data?.data
            mBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            mBitmap = scaleImage(mBitmap)
            updateImageDisplay(mBitmap)
            Toast.makeText(this, "Image selected from gallery", Toast.LENGTH_SHORT).show()
            // Optional: Auto-detect after selecting image
        } catch (e: IOException) {
            Log.e("MainActivity", "Error loading image from gallery: ${e.message}")
            Toast.makeText(this, "Error loading image from gallery: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun scaleImage(bitmap: Bitmap?): Bitmap {
        val originalWidth = bitmap!!.width
        val originalHeight = bitmap.height
        val scaleWidth = mInputSize.toFloat() / originalWidth
        val scaleHeight = mInputSize.toFloat() / originalHeight
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        return Bitmap.createBitmap(bitmap, 0, 0, originalWidth, originalHeight, matrix, true)
    }
}