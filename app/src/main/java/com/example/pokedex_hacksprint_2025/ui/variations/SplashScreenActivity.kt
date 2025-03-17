import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.example.pokedex_hacksprint_2025.R
import com.example.pokedex_hacksprint_2025.databinding.SplashScreenBinding
import com.example.pokedex_hacksprint_2025.MainActivity

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: SplashScreenBinding
    private val SPLASH_DELAY: Long = 2000 // 2 segundos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Carrega o GIF usando Coil
        val imageLoader = ImageLoader.Builder(this)
            .components {
                if (android.os.Build.VERSION.SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()

        val request = ImageRequest.Builder(this)
            .data(R.drawable.splashscreenpikachu)
            .target(binding.splashScreenPikachuGif)
            .build()

        imageLoader.enqueue(request)

        // Inicia a MainActivity após o atraso
        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_DELAY)
    }
}