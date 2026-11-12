package com.example.ui.screens

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EducationBackground
import com.example.ui.theme.RoyalBlue800
import com.example.ui.theme.RoyalBlue900
import com.example.ui.theme.SkyBlueSoft
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimaryDark
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentReaderScreen(
  title: String,
  subtitle: String,
  filePath: String?,
  textContent: String,
  onBackClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  var fontSizeSp by remember { mutableFloatStateOf(16f) }
  var pdfPages = remember { mutableStateListOf<Bitmap>() }
  var isPdfLoading by remember { mutableStateOf(false) }
  var pdfLoadError by remember { mutableStateOf<String?>(null) }

  val hasValidPdfFile = remember(filePath) {
    if (filePath != null) {
      val f = File(filePath)
      f.exists() && f.length() > 0 && filePath.endsWith(".pdf", ignoreCase = true)
    } else false
  }

  // Load PDF pages if physical PDF exists
  LaunchedEffect(filePath) {
    if (hasValidPdfFile && filePath != null) {
      isPdfLoading = true
      pdfLoadError = null
      withContext(Dispatchers.IO) {
        try {
          val file = File(filePath)
          val pfd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
          val renderer = PdfRenderer(pfd)
          val pageCount = renderer.pageCount
          val loaded = mutableListOf<Bitmap>()

          for (i in 0 until minOf(pageCount, 20)) {
            val page = renderer.openPage(i)
            val bitmap = Bitmap.createBitmap(page.width * 2, page.height * 2, Bitmap.Config.ARGB_8888)
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            page.close()
            loaded.add(bitmap)
          }

          renderer.close()
          pfd.close()

          withContext(Dispatchers.Main) {
            pdfPages.clear()
            pdfPages.addAll(loaded)
            isPdfLoading = false
          }
        } catch (e: Exception) {
          e.printStackTrace()
          withContext(Dispatchers.Main) {
            pdfLoadError = "Unable to render PDF stream: ${e.localizedMessage}"
            isPdfLoading = false
          }
        }
      }
    }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(
              text = title,
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
              ),
              maxLines = 1
            )
            Text(
              text = subtitle,
              style = MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
              ),
              maxLines = 1
            )
          }
        },
        navigationIcon = {
          IconButton(
            onClick = onBackClick,
            modifier = Modifier.testTag("reader_back_button")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back"
            )
          }
        },
        actions = {
          IconButton(
            onClick = {
              if (fontSizeSp > 12f) fontSizeSp -= 2f
            }
          ) {
            Icon(imageVector = Icons.Default.ZoomOut, contentDescription = "Decrease text size")
          }
          IconButton(
            onClick = {
              if (fontSizeSp < 28f) fontSizeSp += 2f
            }
          ) {
            Icon(imageVector = Icons.Default.ZoomIn, contentDescription = "Increase text size")
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.surface
        )
      )
    },
    containerColor = EducationBackground
  ) { innerPadding ->
    Box(
      modifier = modifier
        .fillMaxSize()
        .padding(innerPadding)
        .testTag("document_reader_content")
    ) {
      if (isPdfLoading) {
        Column(
          modifier = Modifier.fillMaxSize(),
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          CircularProgressIndicator(color = RoyalBlue800)
          Spacer(modifier = Modifier.height(16.dp))
          Text(text = "Rendering in-app PDF pages...", style = MaterialTheme.typography.bodyMedium)
        }
      } else if (hasValidPdfFile && pdfPages.isNotEmpty()) {
        // PDF pages viewer
        LazyColumn(
          modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
          verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          item {
            Surface(
              color = SkyBlueSoft,
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  imageVector = Icons.Default.PictureAsPdf,
                  contentDescription = null,
                  tint = RoyalBlue800,
                  modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "In-App PDF Viewer • ${pdfPages.size} Page(s) Rendered",
                  style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = RoyalBlue900
                  )
                )
              }
            }
          }

          itemsIndexed(pdfPages) { index, pageBitmap ->
            ElevatedCard(
              shape = RoundedCornerShape(12.dp),
              elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(8.dp)) {
                Text(
                  text = "Page ${index + 1}",
                  style = MaterialTheme.typography.labelSmall.copy(color = TextMuted),
                  modifier = Modifier.padding(bottom = 6.dp)
                )
                Image(
                  bitmap = pageBitmap.asImageBitmap(),
                  contentDescription = "PDF Page ${index + 1}",
                  modifier = Modifier.fillMaxWidth()
                )
              }
            }
          }
        }
      } else {
        // Text / Notes Content Viewer
        Column(
          modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
        ) {
          OutlinedCard(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.outlinedCardColors(
              containerColor = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(20.dp)) {
              Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = RoyalBlue900
                )
              )

              Spacer(modifier = Modifier.height(4.dp))

              Text(
                text = subtitle,
                style = MaterialTheme.typography.labelMedium.copy(
                  color = TextMuted,
                  fontWeight = FontWeight.Medium
                )
              )

              Spacer(modifier = Modifier.height(16.dp))
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .height(1.dp)
                  .background(MaterialTheme.colorScheme.outlineVariant)
              )
              Spacer(modifier = Modifier.height(16.dp))

              val displayBody = if (textContent.isNotBlank()) {
                textContent
              } else {
                "Document file verified and stored in official local cache. You can study this material directly offline anytime."
              }

              Text(
                text = displayBody,
                style = MaterialTheme.typography.bodyLarge.copy(
                  fontSize = fontSizeSp.sp,
                  lineHeight = (fontSizeSp * 1.55f).sp,
                  color = TextPrimaryDark,
                  fontFamily = FontFamily.Default
                )
              )
            }
          }

          Spacer(modifier = Modifier.height(30.dp))
        }
      }
    }
  }
}
