package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.PoweredByNarayana
import com.example.ui.theme.EducationBackground
import com.example.ui.theme.RoyalBlue700
import com.example.ui.theme.RoyalBlue800
import com.example.ui.theme.RoyalBlue900
import com.example.ui.theme.SkyBlueSoft
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimaryDark
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
  onNavigateToHome: () -> Unit,
  modifier: Modifier = Modifier
) {
  val scale = remember { Animatable(0.85f) }
  val alpha = remember { Animatable(0.3f) }

  LaunchedEffect(Unit) {
    scale.animateTo(
      targetValue = 1f,
      animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
    )
    alpha.animateTo(
      targetValue = 1f,
      animationSpec = tween(durationMillis = 600)
    )
    // Wait briefly then smoothly proceed straight to Home Screen
    delay(1200)
    onNavigateToHome()
  }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          colors = listOf(
            Color.White,
            EducationBackground,
            SkyBlueSoft.copy(alpha = 0.5f)
          )
        )
      )
      .padding(24.dp)
      .testTag("splash_screen_root"),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
      modifier = Modifier
        .scale(scale.value)
        .padding(bottom = 60.dp)
    ) {
      // IT Learn Emblem
      Surface(
        shape = CircleShape,
        shadowElevation = 8.dp,
        color = Color.White,
        modifier = Modifier.size(130.dp)
      ) {
        Box(
          contentAlignment = Alignment.Center,
          modifier = Modifier.padding(14.dp)
        ) {
          Image(
            painter = painterResource(id = R.drawable.app_icon_itlearn_1794486117525),
            contentDescription = "IT Learn Logo",
            modifier = Modifier
              .fillMaxSize()
              .clip(CircleShape)
          )
        }
      }

      Spacer(modifier = Modifier.height(28.dp))

      // App Title
      Text(
        text = "IT Learn",
        style = MaterialTheme.typography.headlineLarge.copy(
          fontWeight = FontWeight.ExtraBold,
          letterSpacing = 1.sp,
          color = RoyalBlue900
        ),
        textAlign = TextAlign.Center,
        modifier = Modifier.testTag("splash_app_title")
      )

      Spacer(modifier = Modifier.height(6.dp))

      // Tagline
      Text(
        text = "Learn • Practice • Grow",
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.SemiBold,
          letterSpacing = 1.2.sp,
          color = RoyalBlue800
        ),
        textAlign = TextAlign.Center,
        modifier = Modifier.testTag("splash_app_tagline")
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Diploma C-23 Subtitle badge
      Surface(
        color = SkyBlueSoft,
        shape = RoundedCornerShape(12.dp)
      ) {
        Text(
          text = "Diploma C-23 Educational Platform",
          style = MaterialTheme.typography.labelMedium.copy(
            fontWeight = FontWeight.Bold,
            color = RoyalBlue900
          ),
          modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
        )
      }

      Spacer(modifier = Modifier.height(32.dp))

      // Direct entry skip/tap button
      Button(
        onClick = onNavigateToHome,
        colors = ButtonDefaults.buttonColors(
          containerColor = RoyalBlue800,
          contentColor = Color.White
        ),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.testTag("splash_get_started_button")
      ) {
        Text(text = "Open Study Portal", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.size(8.dp))
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowForward,
          contentDescription = null,
          modifier = Modifier.size(18.dp)
        )
      }
    }

    // Bottom Branding Credit: Powered by Narayana
    Box(
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .padding(bottom = 24.dp)
    ) {
      PoweredByNarayana(isSplash = true)
    }
  }
}
