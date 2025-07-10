package com.example.ch19_map

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity(),
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    OnMapReadyCallback {

    lateinit var providerClient: FusedLocationProviderClient
    lateinit var apiClient: GoogleApiClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback
    var googleMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 지도 초기화
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // 위치 클라이언트 설정
        providerClient = LocationServices.getFusedLocationProviderClient(this)

        // 위치 권한 요청
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            if (it.all { permission -> permission.value == true }) {
                apiClient.connect()
            } else {
                Toast.makeText(this, "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }

        // Google API 클라이언트 설정
        apiClient = GoogleApiClient.Builder(this)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build()

        // 권한 확인
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION))
        } else {
            apiClient.connect()
        }
    }

    override fun onConnected(p0: Bundle?) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {

            // 위치 요청 설정
            locationRequest = LocationRequest.create().apply {
                interval = 5000              // 5초마다 업데이트
                fastestInterval = 2000       // 최소 2초
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }

            // 위치 콜백 정의
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    val location = result.lastLocation
                    if (location != null) {
                        moveMap(location.latitude, location.longitude)
                    }
                }
            }

            // 위치 업데이트 요청
            providerClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                mainLooper
            )
        }
    }

    override fun onConnectionSuspended(p0: Int) {}
    override fun onConnectionFailed(p0: ConnectionResult) {}

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
    }

    private fun moveMap(latitude: Double, longitude: Double) {
        val latLng = LatLng(latitude, longitude)
        val position = CameraPosition.Builder()
            .target(latLng)
            .zoom(16f)
            .build()
        googleMap?.moveCamera(CameraUpdateFactory.newCameraPosition(position))

        val marker = MarkerOptions()
            .position(latLng)
            .title("현재 위치")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))

        googleMap?.clear()
        googleMap?.addMarker(marker)
    }

    override fun onStop() {
        super.onStop()
        if (::locationCallback.isInitialized) {
            providerClient.removeLocationUpdates(locationCallback)
        }
    }
}